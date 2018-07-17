package com.ys.datatool.service.web;

import com.ys.datatool.domain.HtmlTag;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mo on @date  2018/7/17.
 */
@Service
public class DiDiBaBaService {

    private String SUPPLIER_URL = "http://www.didibabachina.com/ctm/supplier";

    private String SUPPLIERDETAIL_URL = "http://www.didibabachina.com/ctm/supplier/detail?___t0.7667218411026064&supplierId=";

    private Charset charset = Charset.forName("UTF-8");

    private String trRegEx = "body > div > div:nth-child(2) > table > tbody > tr";

    private Workbook workbook;

    private String companyName = "非凡尚品汽车美容养护中心";

    private String COOKIE = "pageSize=10; JSESSIONID=034f85b2-d50c-472b-8a8e-683b084a4b1e; listPageUrl=/ctm/supplier; pageNo=2";


    @Test
    public void test() throws IOException {

    }


    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        Response res = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getDetailParams("1"), COOKIE);
        String content = res.returnContent().asString(charset);
        Document doc = Jsoup.parseBodyFragment(content);
        String getTotalRegEx = "(?<=共).*(?=页)";
        String totalStr = CommonUtil.fetchString(doc.toString(), getTotalRegEx);
        String total = totalStr.replace("&nbsp;", "").trim();
        int totalPage = Integer.parseInt(total);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getDetailParams(String.valueOf(i)), COOKIE);
                content = res.returnContent().asString(charset);
                doc = Jsoup.parseBodyFragment(content);

                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String idRegEx = "body > div > div:nth-child(2) > table > tbody > tr:nth-child({no}) > td:nth-child(4) > a:nth-child(1)";
                        String idStr = doc.select(StringUtils.replace(idRegEx, "{no}", String.valueOf(j))).attr("onclick");
                        String getIdRegEx = "(?<=\\().*(?=\\))";
                        String supplierId = CommonUtil.fetchString(idStr, getIdRegEx);
                        ids.add(supplierId);
                    }
                }
            }
        }

        if (ids.size() > 0) {
            for (String id : ids) {
                Response response = ConnectionUtil.doGetWithLeastParams(SUPPLIERDETAIL_URL + id, COOKIE);
                String html =response.returnContent().asString(charset);
                Document document = Jsoup.parseBodyFragment(html);

                String nameRegEx = "body > div > form > ul > li:nth-child(1) > span";
                String contactPhoneRegEx = "body > div > form > ul > li:nth-child(3) > span";
                String contactNameRegEx = "body > div > form > ul > li:nth-child(2) > span";
                String faxRegEx = "body > div > form > ul > li:nth-child(5) > span";
                String addressRegEx = "body > div > form > ul > li:nth-child(10) > span";
                String accountNumberRegEx = "body > div > form > ul > li:nth-child(7) > span";
                String depositBankRegEx = "body > div > form > ul > li:nth-child(8) > span";
                String accountNameRegEx = "body > div > form > ul > li:nth-child(9) > span";
                String remarkRegEx = "body > div > form > ul > li:nth-child(4) > span";//公司电话

                String name = document.select(nameRegEx).text();
                String contactPhone = document.select(contactPhoneRegEx).text();
                String contactName = document.select(contactNameRegEx).text();
                String fax = document.select(faxRegEx).text();
                String address = document.select(addressRegEx).text();
                String accountNumber = document.select(accountNumberRegEx).text();
                String depositBank = document.select(depositBankRegEx).text();
                String accountName = document.select(accountNameRegEx).text();
                String remark = document.select(remarkRegEx).text();

                Supplier supplier = new Supplier();
                supplier.setCompanyName(companyName);
                supplier.setName(name);
                supplier.setContactPhone(contactPhone);
                supplier.setContactName(contactName);
                supplier.setFax(fax);
                supplier.setAddress(address);
                supplier.setAccountNumber(accountNumber);
                supplier.setDepositBank(depositBank);
                supplier.setAccountName(accountName);
                supplier.setRemark(remark);
                suppliers.add(supplier);
            }
        }

        System.out.println("結果為" + suppliers.toString());
        System.out.println("結果為" + suppliers.size());

        String pathname = "C:\\exportExcel\\DiDiBaBa供应商导出.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, workbook, pathname);


    }

    private List<BasicNameValuePair> getDetailParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("pageNo", pageNo));
        params.add(new BasicNameValuePair("pageSize", "10"));

        return params;
    }

}
