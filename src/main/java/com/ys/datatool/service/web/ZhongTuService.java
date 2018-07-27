package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.HtmlTag;
import com.ys.datatool.domain.Product;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018/7/24.
 * <p>
 * 众途系统
 */
@Service
public class ZhongTuService {

    private String LOGIN_URL = "http://crm.zhongtukj.com/Boss/Index.aspx";

    private String SUPPLIER_URL = "http://crm.zhongtukj.com/Boss/Stock/SupplierList.aspx";

    private String SUPPLIERDETAIL_URL = "http://crm.zhongtukj.com/Boss/Stock/SupplierEdit.aspx?action=edit&id=";

    private String ITEM_URL = "http://crm.zhongtukj.com/Boss/Stock/Stockservice/StockShop.ashx";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String USERID = "txtUserId";

    private String PWDID = "txtUserPwd";

    private String BTNLOGIN = "btnLogin";

    private String USERNAME = "5550";

    private String PASSWORD = "xixi15815826629";

    private String fieldName = "total";

    private int count = 0;

    private List<HtmlPage> pages = new ArrayList();

    //车店编号
    private String companyId = "7";

    private String companyName = "众途";

    private String COOKIE = "ASP.NET_SessionId=slyqlmsdtyatk3yvwfmuzjpy; UM_distinctid=164da9efa92343-0bb0e5004758bb-5e442e19-144000-164da9efa938d; pgv_pvi=9327175680; pgv_si=s293537792; _qddaz=QD.xnwun7.nhgsm6.jk3nuc6w; Hm_lvt_8aa0f851a89545e877fad647785568e3=1532676210; Hm_lpvt_8aa0f851a89545e877fad647785568e3=1532685856; ztrjnew@4db97b96-12af-45b0-b232-fd1e9b7a672e=UserId=lHY/sgoxzjE=&CSID=lHY/sgoxzjE=&UserName=zihbF2SO0PqS7n+TuVIZ4A==&SID=TVo+7r+xtys=&RoleId=VBdEVOSspJM=&GroupId=VBdEVOSspJM=";

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllPages(webClient, SUPPLIER_URL);

        if (pages.size() > 0) {
            for (HtmlPage page : pages) {
                Document doc = Jsoup.parseBodyFragment(page.asXml());

                String trRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int i = 2; i <= trSize; i++) {
                        String idRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(3) > a";
                        String getIdRegEx = "id=.*";
                        String idStr = doc.select(StringUtils.replace(idRegEx, "{no}", String.valueOf(i))).attr("href");
                        String id = CommonUtil.fetchString(idStr, getIdRegEx).replace("id=", "");
                        ids.add(id);
                    }
                }
            }
        }

        if (ids.size() > 0) {
            for (String id : ids) {

                Response res = ConnectionUtil.doGetWithLeastParams(SUPPLIERDETAIL_URL + id, COOKIE);
                String html = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                String nameRegEx = "#txt_Name";
                String contactNameRegEx = "#txt_LinkMan";
                String contactPhoneRegEx = "#txt_Mobile";
                String addressRegEx = "#txt_Address";
                String remarkRegEx = "#txt_Remark";
                String accountNameRegEx = "#txt_RealName";
                String depositBankRegEx = "#txt_BankName";
                String accountNumberRegEx = "#txt_BankCode";
                String faxRegEx = "#txt_Fax";
                String phoneRegEx = "#txt_Tel";

                String name = doc.select(nameRegEx).attr("value");
                String contactName = doc.select(contactNameRegEx).attr("value");
                String contactPhone = doc.select(contactPhoneRegEx).attr("value");
                String address = doc.select(addressRegEx).attr("value");
                String remark = doc.select(remarkRegEx).attr("value");
                String accountName = doc.select(accountNameRegEx).attr("value");
                String depositBank = doc.select(depositBankRegEx).attr("value");
                String accountNumber = doc.select(accountNumberRegEx).attr("value");
                String fax = doc.select(faxRegEx).attr("value");
                String phone = doc.select(phoneRegEx).attr("value");

                Supplier supplier = new Supplier();
                supplier.setName(name);
                supplier.setContactName(contactName);
                supplier.setContactPhone(contactPhone);
                supplier.setAddress(address);
                supplier.setAccountName(accountName);
                supplier.setDepositBank(depositBank);
                supplier.setAccountNumber(accountNumber);
                supplier.setRemark(remark);
                supplier.setPhone(phone);
                supplier.setFax(fax);
                supplier.setCompanyName(companyName);
                suppliers.add(supplier);
            }
        }

        System.out.println("结果为" + suppliers.toString());

        String pathname = "C:\\exportExcel\\众途供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }

    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();


    }

    /**
     * 商品
     *
     * @throws IOException
     */
    @Test
    public void fetchItemDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        String act = "GetShopList";
        Response res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams("1", "15", companyId, act), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams(String.valueOf(i), "15", companyId, act), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("Name").asText();
                    String companyName = element.get("GroupName").asText();
                    String code = element.get("ShopCode").asText();
                    String firstCategoryName = element.get("ShopTypeName").asText();
                    String price = element.get("Price").asText();
                    String barCode = element.get("NameCode").asText();
                    String unit = element.get("UnitName").asText();
                    String remark = element.get("GuiGe").asText();//产品规格

                    Product product = new Product();
                    product.setCode(code);
                    product.setProductName(productName);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setUnit(unit);
                    product.setPrice(price);
                    product.setCompanyName(companyName);
                    product.setBarCode(barCode);
                    product.setItemType("配件");
                    product.setRemark(remark);
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());
        System.out.println("结果为" + products.size());

        String pathname = "C:\\exportExcel\\众途商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    private List<BasicNameValuePair> getParams(String page, String rows, String GroupID, String act) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("act", act));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("rows", rows));
        params.add(new BasicNameValuePair("GroupID", GroupID));
        return params;
    }

    private void getAllPages(WebClient webClient, String url) throws IOException {
        login(webClient);

        HtmlPage page = webClient.getPage(url);
        pages.add(page);

        String total = WebClientUtil.getTotalPage(page);
        nextPage(page, Integer.parseInt(total));
    }


    private void login(WebClient webClient) throws IOException {
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);

        HtmlInput user = loginPage.getHtmlElementById(USERID);
        HtmlInput pwd = loginPage.getHtmlElementById(PWDID);
        HtmlInput btnLogin = loginPage.getHtmlElementById(BTNLOGIN);//登录按钮

        user.setAttribute("value", USERNAME);
        pwd.setAttribute("value", PASSWORD);

        //登录操作
        btnLogin.click();
    }

    private void nextPage(HtmlPage page, int num) throws IOException {
        ++count;
        if (count == num)
            return;

        String anchorXPath = "//*[@id=\"AspNetPager\"]/div/a[3]";
        HtmlAnchor nextPage = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = nextPage.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num);//num为总页数
    }

}
