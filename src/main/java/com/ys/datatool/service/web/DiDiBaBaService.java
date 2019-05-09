package com.ys.datatool.service.web;

import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.HtmlTag;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.CarInfo;
import com.ys.datatool.domain.entity.Supplier;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mo on @date  2018/7/17.
 * DiDiBaBa
 */
@Service
public class DiDiBaBaService {

    private String CARINFODETAIL_URL = "http://www.didibabachina.com/ctm/manage/perComDist?id=";

    private String CARINFO_URL = "http://www.didibabachina.com/ctm/manage";

    private String SUPPLIER_URL = "http://www.didibabachina.com/ctm/supplier";

    private String SUPPLIERDETAIL_URL = "http://www.didibabachina.com/ctm/supplier/detail?___t0.7667218411026064&supplierId=";

    private String companyName = "DiDiBaBa系统";

    private String COOKIE = "JSESSIONID=4760cd38-8b10-4168-bd78-6d4223533b14; pageSize=10; pageNo=1; listPageUrl=/ctm/cardmanage";


    /**
     * 获取同一客户多辆车的数据
     *
     * @throws IOException
     */
    @Test
    public void fetchMultiCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        int totalPage = getTotalPage(CARINFO_URL);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getDetailParams(String.valueOf(i)), COOKIE);
                String content = res.returnContent().asString(WebConfig.CHARSET_UTF_8);
                Document doc = Jsoup.parseBodyFragment(content);

                String trRegEx = "#tableID > tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String trElementRegEx = "#tableID > tbody > tr";
                        Elements elements = doc.select(trElementRegEx).tagName("tr");

                        for (Element e : elements) {
                            String carNumberRegEx = "td:nth-child(4)";
                            String carNumber = e.select(carNumberRegEx).text();

                            if (carNumber.contains(",") || carNumber.contains("...")){
                                String idRegEx = "td:nth-child(10) > a:nth-child(1)";
                                String getIdRegEx = "id=.*";
                                String idStr = e.select(idRegEx).attr("href");
                                String carIdstr = CommonUtil.fetchString(idStr, getIdRegEx);
                                String carId = carIdstr.replace("id=", "").trim();
                                ids.add(carId);
                            }
                        }
                    }
                }
            }
        }

        fetchCarInfo(carInfos, ids);

        System.out.println("結果為" + carInfos.toString());
        System.out.println("結果為" + carInfos.size());

        String pathname = "C:\\exportExcel\\DiDiBaBa车辆导出.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        int totalPage = getTotalPage(CARINFO_URL);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getDetailParams(String.valueOf(i)), COOKIE);
                String content = res.returnContent().asString(WebConfig.CHARSET_UTF_8);
                Document doc = Jsoup.parseBodyFragment(content);

                String trRegEx = "#tableID > tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String trElementRegEx = "#tableID > tbody > tr";
                        Elements elements = doc.select(trElementRegEx).tagName("tr");
                        for (Element e : elements) {
                            String idRegEx = "td:nth-child(10) > a:nth-child(1)";
                            String idStr = e.select(idRegEx).attr("href");
                            String getIdRegEx = "id=.*";
                            String carIdstr = CommonUtil.fetchString(idStr, getIdRegEx);
                            String carId = carIdstr.replace("id=", "").trim();
                            ids.add(carId);
                        }
                    }
                }
            }
        }

        fetchCarInfo(carInfos, ids);

        System.out.println("結果為" + carInfos.toString());
        System.out.println("結果為" + carInfos.size());

        String pathname = "C:\\exportExcel\\DiDiBaBa车辆导出.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        int totalPage = getTotalPage(SUPPLIER_URL);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getDetailParams(String.valueOf(i)), COOKIE);
                String content = res.returnContent().asString(WebConfig.CHARSET_UTF_8);
                Document doc = Jsoup.parseBodyFragment(content);

                String trRegEx = "body > div > div:nth-child(2) > table > tbody > tr";
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
                Response response = ConnectionUtil.doGetWith(SUPPLIERDETAIL_URL + id, COOKIE);
                String html = response.returnContent().asString(WebConfig.CHARSET_UTF_8);
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
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }

    private void fetchCarInfo(List<CarInfo> carInfos, Set<String> ids) throws IOException {
        if (ids.size() > 0) {
            for (String id : ids) {
                Response res = ConnectionUtil.doGetWith(CARINFODETAIL_URL + id, COOKIE);
                String html = res.returnContent().asString(WebConfig.CHARSET_UTF_8);
                Document doc = Jsoup.parseBodyFragment(html);

                String preRegEx = "div[class='section']  > ul[class='popup_form threeColumn clearfix'] ";
                String nameRegEx = preRegEx + " > li:nth-child(2) > span > span";
                String phoneRegEx = preRegEx + " > li:nth-child(3) > span ";
                String remarkRegEx = preRegEx + " > li:nth-child(10) > span ";

                String name = doc.select(nameRegEx).text();
                String phone = doc.select(phoneRegEx).text();
                String remark = doc.select(remarkRegEx).text();

                String divRegEx = "div[class='section']  > div[class='section dashed P0 clearfix'] ";
                int divSize = WebClientUtil.getTagSize(doc, divRegEx, HtmlTag.divName);

                if (divSize > 0) {
                    Elements elements = doc.select(divRegEx).tagName("div");
                    for (Element e : elements) {
                        String carNumberRegEx = "ul > li:nth-child(1) > span";
                        String brandRegEx = "ul > li:nth-child(2) > span";
                        String carModelRegEx = "ul > li:nth-child(3) > span";
                        String VINcodeRegEx = "#moreInfo0 > li:nth-child(7) > span";
                        String engineNumberRegEx = "#moreInfo0 > li:nth-child(8) > span";
                        String vcInsuranceValidDateRegEx = "#moreInfo0 > li:nth-child(10) > span";
                        String vcInsuranceCompanyRegEx = "#moreInfo0 > li:nth-child(12) > span";

                        String carNumber = e.select(carNumberRegEx).text();
                        String brand = e.select(brandRegEx).text();
                        String carModel = e.select(carModelRegEx).text();
                        String VINcode = e.select(VINcodeRegEx).text();
                        String engineNumber = e.select(engineNumberRegEx).text();
                        String vcInsuranceValidDate = e.select(vcInsuranceValidDateRegEx).text();
                        String vcInsuranceCompany = e.select(vcInsuranceCompanyRegEx).text();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCompanyName(companyName);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setBrand(brand);
                        carInfo.setCarModel(carModel);
                        carInfo.setVINcode(VINcode);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setRemark(remark);
                        carInfos.add(carInfo);
                    }
                }
            }
        }
    }

    private int getTotalPage(String url) throws IOException {
        Response res = ConnectionUtil.doPostWithLeastParams(url, getDetailParams("1"), COOKIE);
        String content = res.returnContent().asString(WebConfig.CHARSET_UTF_8);
        Document doc = Jsoup.parseBodyFragment(content);

        String getTotalRegEx = "(?<=共).*(?=页)";
        String totalStr = CommonUtil.fetchString(doc.toString(), getTotalRegEx);
        String total = totalStr.replace("&nbsp;", "").trim();
        int totalPage = Integer.parseInt(total);

        return totalPage;
    }

    private List<BasicNameValuePair> getDetailParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("pageNo", pageNo));
        params.add(new BasicNameValuePair("pageSize", "10"));

        return params;
    }

}
