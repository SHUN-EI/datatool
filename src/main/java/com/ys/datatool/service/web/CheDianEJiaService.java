package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.HtmlTag;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mo on @date  2018-06-06.
 * 车店E家系统
 */

@Service
public class CheDianEJiaService {

    private String STOCK_URL = "http://s.66ejia.com/RepertoryCenter/Repertorys.aspx?c=0";

    private String SUPPLIER_URL = "http://s.66ejia.com/RepertoryCenter/SupplierCenter.aspx";

    private String MEMBERCARD_URL = "http://s.66ejia.com/ShopMembers/ShopMemberList.aspx";

    private String CARINFODETAIL_URL = "http://s.66ejia.com/ShopMembers/";

    private String CARINFO_URL = "http://s.66ejia.com/ShopMembers/ShopDriverList.aspx";

    private String MEMBERCARDITEMDETAIL_URL = "http://s.66ejia.com/ShopMembers/ShopMemberPackageEdit.aspx?id=";

    private String MEMBERCARDITEM_URL = "http://s.66ejia.com/ShopMembers/ShopMemberPackages.aspx";

    private String LOGIN_URL = "http://s.66ejia.com/Login.aspx";

    private List<HtmlPage> pages = new ArrayList();

    private int count = 0;

    private String companyName = "车店E家";

    private String userName = "gelunbu";

    private String password = "123456";

    private String trRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr";

    //获取数据的令牌
    private String COOKIE = "ASP.NET_SessionId=3hsvjj00kow2jondtrxcsrv2; CarSaasShopAdmin=eyJSb2xlQ29udGVudHMiOiIiLCJTaG9wU3RhdGUiOjAsIkNhck51bWJlckhlYWQiOiJcdTdDQTRBIiwiSUQiOiJmNWNjZjllMS1iMjQyLTQxOWUtYTA4MS05NDdiNWQ0MWZlNWIiLCJTaG9wSUQiOiIzZWUzYWM0Ny05ZGNlLTQ4NjctOGRmYS1jZGRiNTVlMTNhNzgiLCJMb2dpbk5hbWUiOiJnZWx1bmJ1IiwiTG9naW5Qd2QiOiIiLCJXWE9wZW5JRCI6IiIsIlRydWVOYW1lIjoiXHU3MzhCIiwiVXNlclBob25lIjoiMTgxMjcwNzc1NzMiLCJSb2xlSUQiOiIwIiwiUm9sZU5hbWUiOiJcdTdCQTFcdTc0MDZcdTU0NTgiLCJBZG1pblN0YXRlIjowLCJBZGRUaW1lIjoiMDkvMTgvMjAxNyAxOToyOTo0NyIsIlNob3BOYW1lIjoiXHU3QzczXHU1MTc2XHU2Nzk3Llx1OUE3MFx1NTJBMFx1NkM3RFx1OEY2Nlx1NjcwRFx1NTJBMVx1NUU5NyIsIk9yZ2FuSUQiOiJHREdaIiwiU2hvcExvZ28iOiIvVXBsb2FkL3B1YmxpYy9iNGYzMGNmZjI1NGE0NDc1YTA0YjJmZDgzNmE1NWZlNS5qcGciLCJTaG9wUGhvbmUiOiIwNzYwLTg2MzYzMDMzIiwiU2hvcE1hc3RlciI6Ilx1OTBFRFx1NUMwRlx1NTlEMCIsIlNob3BNYXN0ZXJQaG9uZSI6IjE4MDIyMTA4NDAwIiwiU2hvcFByb3ZpbmNlIjoiXHU1RTdGXHU0RTFDXHU3NzAxIiwiU2hvcENpdHkiOiJcdTRFMkRcdTVDNzFcdTVFMDIiLCJTaG9wQXJlYSI6Ilx1NTc2Nlx1NkQzMlx1OTU0NyIsIlNob3BBZGRyZXNzIjoiXHU3OEE3XHU1Qjg5XHU4REVGNFx1NTNGN1x1OTUyNlx1N0VFM1x1OTZDNVx1ODJEMTlcdTY3MUZcdUZGMDhcdTczQUZcdTZEMzJcdTUzMTdcdThERUZcdTRFMEVcdTc4QTdcdTVCODlcdThERUZcdTRFQTRcdTYzQTVcdTU5MDRcdUZGMDkiLCJTaG9wUGFyZW50SUQiOiIiLCJTaG9wQWRtaW5UeXBlIjoxMH0=";


    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockData() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        getALLPages(STOCK_URL, 10);

        for (int i = 0; i < pages.size(); i++) {
            Document doc = Jsoup.parseBodyFragment(pages.get(i).asXml());
            int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);

            for (int j = 2; j <= trSize; j++) {

                String goodsNameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(1)";
                String productCodeRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(2)";
                String brandRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(4)";
                String carModelRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(5)";
                String unitRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(7)";
                String numRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(6)";
                String priceRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(8)";

                String goodsName = doc.select(StringUtils.replace(goodsNameRegEx, "{no}", String.valueOf(j))).text();
                String productCode = doc.select(StringUtils.replace(productCodeRegEx, "{no}", String.valueOf(j))).text();
                String brand = doc.select(StringUtils.replace(brandRegEx, "{no}", String.valueOf(j))).text();
                String carModel = doc.select(StringUtils.replace(carModelRegEx, "{no}", String.valueOf(j))).text();
                String unit = doc.select(StringUtils.replace(unitRegEx, "{no}", String.valueOf(j))).text();
                String num = doc.select(StringUtils.replace(numRegEx, "{no}", String.valueOf(j))).text();
                String price = doc.select(StringUtils.replace(priceRegEx, "{no}", String.valueOf(j))).text();

                Stock stock = new Stock();
                stock.setGoodsName(goodsName);
                stock.setBrand(brand);
                stock.setProductCode(productCode);
                stock.setPrice(price);
                stock.setCompanyName(companyName);
                stock.setInventoryNum(num);
                stocks.add(stock);

                Product product=new Product();
                product.setProductName(goodsName);
                product.setCompanyName(companyName);
                product.setCode(productCode);
                product.setBrandName(brand);
                product.setCarModel(carModel);
                product.setUnit(unit);
                product.setItemType("配件");
                product.setPrice(price);
                products.add(product);
            }
        }

        System.out.println("结果为" + pages.size());

        String pathname = "C:\\exportExcel\\车店E家库存导出.xlsx";
        String pathname2 = "C:\\exportExcel\\车店E家库存商品导出.xlsx";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname2);


    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(SUPPLIER_URL, COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);
        for (int i = 2; i <= trSize; i++) {
            String nameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(1)";
            String contactNameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(2)";
            String contactPhoneRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(3)";
            String addressRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(5)";
            String remarkRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(6)";//当前欠款

            String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(i))).text();
            String contactName = doc.select(StringUtils.replace(contactNameRegEx, "{no}", String.valueOf(i))).text();
            String contactPhone = doc.select(StringUtils.replace(contactPhoneRegEx, "{no}", String.valueOf(i))).text();
            String address = doc.select(StringUtils.replace(addressRegEx, "{no}", String.valueOf(i))).text();
            String remark = doc.select(StringUtils.replace(remarkRegEx, "{no}", String.valueOf(i))).text();

            Supplier supplier = new Supplier();
            supplier.setName(name);
            supplier.setContactName(contactName);
            supplier.setContactPhone(contactPhone);
            supplier.setAddress(address);
            supplier.setRemark(remark);
            supplier.setCompanyName(companyName);
            suppliers.add(supplier);
        }

        System.out.println("结果为" + suppliers.toString());

        String pathname = "C:\\exportExcel\\车店E家供应商导出.xlsx";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }

    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Map<String, CarInfo> carInfoMap = new HashMap<>();
        getALLPages(CARINFO_URL, 15);

        for (int i = 0; i < pages.size(); i++) {
            Document doc = Jsoup.parseBodyFragment(pages.get(i).asXml());
            int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);
            for (int j = 2; j <= trSize; j++) {

                String idRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(9) > a";
                String id = doc.select(StringUtils.replace(idRegEx, "{no}", String.valueOf(j))).attr("href");

                String nameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(1)";
                String phoneRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(2)";
                String brandRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(3)";
                String carNumberRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(4)";
                String vinCodeRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(5)";
                String engineNumberRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(6)";

                String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", String.valueOf(j))).text();
                String brand = doc.select(StringUtils.replace(brandRegEx, "{no}", String.valueOf(j))).text();
                String carNumber = doc.select(StringUtils.replace(carNumberRegEx, "{no}", String.valueOf(j))).text();
                String vinCode = doc.select(StringUtils.replace(vinCodeRegEx, "{no}", String.valueOf(j))).text();
                String engineNumber = doc.select(StringUtils.replace(engineNumberRegEx, "{no}", String.valueOf(j))).text();

                CarInfo carInfo = new CarInfo();
                carInfo.setCompanyName(companyName);
                carInfo.setName(name);
                carInfo.setPhone(phone);
                carInfo.setBrand(brand);
                carInfo.setCarNumber(carNumber);
                carInfo.setVINcode(vinCode);
                carInfo.setEngineNumber(engineNumber);
                carInfoMap.put(id, carInfo);
            }
        }

        if (carInfoMap.size() > 0) {
            for (String id : carInfoMap.keySet()) {
                Response response = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + id, COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String carModelRegEx = "#txtCarModel";
                String vcInsuranceCompanyRegEx = "#txtBXCompany";
                String vcInsuranceValidDateRegEx = "#txtBXTime";
                String remarkRegEx = "#txtNS";//年审日期

                String carModel = doc.select(carModelRegEx).attr("value");
                String vcInsuranceCompany = doc.select(vcInsuranceCompanyRegEx).attr("value");
                String vcInsuranceValidDate = doc.select(vcInsuranceValidDateRegEx).attr("value");
                String remark = doc.select(remarkRegEx).attr("value");

                CarInfo carInfo = carInfoMap.get(id);
                carInfo.setCarModel(carModel);
                carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                carInfo.setRemark(remark);
                carInfos.add(carInfo);
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("结果为" + carInfos.size());

        String pathname = "C:\\exportExcel\\车店E家车辆导出.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }


    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        getALLPages(MEMBERCARD_URL, 15);
        for (int i = 0; i < pages.size(); i++) {

            Document doc = Jsoup.parseBodyFragment(pages.get(i).asXml());
            int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);

            for (int j = 2; j <= trSize; j++) {

                String cardCodeRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(6)";
                String memberCardNameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(7)";
                String carNumberRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(5)";
                String dateCreatedRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(11)";
                String balanceRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(9)";
                String nameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(2)";
                String phoneRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(8)";

                String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", String.valueOf(j))).text();
                String balance = doc.select(StringUtils.replace(balanceRegEx, "{no}", String.valueOf(j))).text();
                String dateCreated = doc.select(StringUtils.replace(dateCreatedRegEx, "{no}", String.valueOf(j))).text();
                String carNumber = doc.select(StringUtils.replace(carNumberRegEx, "{no}", String.valueOf(j))).text();
                String memberCardName = doc.select(StringUtils.replace(memberCardNameRegEx, "{no}", String.valueOf(j))).text();
                String cardCode = doc.select(StringUtils.replace(cardCodeRegEx, "{no}", String.valueOf(j))).text();

                MemberCard memberCard = new MemberCard();
                memberCard.setCardCode(phone);//手机号作为卡号
                memberCard.setName(name);
                memberCard.setPhone(phone);
                memberCard.setCarNumber(carNumber);
                memberCard.setDateCreated(dateCreated);
                memberCard.setBalance(balance.replace("￥", ""));
                memberCard.setMemberCardName(memberCardName);
                memberCard.setCompanyName(companyName);
                memberCards.add(memberCard);
            }
        }

        System.out.println("结果为" + pages.size());

        String pathname = "C:\\exportExcel\\车店E家会员卡导出.xlsx";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    /**
     * 卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, MemberCardItem> memberCardItemMap = new HashMap<>();
        getALLPages(MEMBERCARDITEM_URL, 10);

        for (int i = 0; i < pages.size(); i++) {
            Document doc = Jsoup.parseBodyFragment(pages.get(i).asXml());
            int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);

            for (int j = 2; j <= trSize; j++) {
                String idRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(9) > a:nth-child(1)";
                String idStr = doc.select(StringUtils.replace(idRegEx, "{no}", String.valueOf(j))).attr("href");
                String getIdRegEx = "(?<=').*(?=')";
                String id = CommonUtil.fetchString(idStr, getIdRegEx);

                String nameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(1)";
                String cardCodeRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(2)";
                String memberCardNameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(3)";
                String dateCreatedRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(8)";
                String validTimeRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(6)";

                String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                String cardCode = doc.select(StringUtils.replace(cardCodeRegEx, "{no}", String.valueOf(j))).text();
                String memberCardName = doc.select(StringUtils.replace(memberCardNameRegEx, "{no}", String.valueOf(j))).text();
                String dateCreated = doc.select(StringUtils.replace(dateCreatedRegEx, "{no}", String.valueOf(j))).text();
                String validTime = doc.select(StringUtils.replace(validTimeRegEx, "{no}", String.valueOf(j))).text();
                String isValidForever = CommonUtil.getIsValidForever(validTime);

                MemberCardItem memberCardItem = new MemberCardItem();
                memberCardItem.setCompanyName(companyName);
                memberCardItem.setName(name);
                memberCardItem.setPhone(cardCode);
                memberCardItem.setCardCode(cardCode);
                memberCardItem.setMemberCardName(memberCardName);
                memberCardItem.setDateCreated(dateCreated);
                memberCardItem.setValidTime(validTime.replace("-", "/"));
                memberCardItem.setIsValidForever(isValidForever);
                memberCardItemMap.put(id, memberCardItem);
            }
        }

        if (memberCardItemMap.size() > 0) {
            for (String id : memberCardItemMap.keySet()) {
                Response response = ConnectionUtil.doGetWithLeastParams(MEMBERCARDITEMDETAIL_URL + id, COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String getTRRegEx = "#form1 > div.formbody > table > tbody > tr";
                int tRSize = WebClientUtil.getTagSize(doc, getTRRegEx, HtmlTag.trName);

                for (int i = 2; i <= tRSize; i++) {

                    String itemNameRegEx = "#form1 > div.formbody > table > tbody > tr:nth-child({no}) > td:nth-child(1) > input[type=\"hidden\"]:nth-child(2)";
                    String originalNumRegEx = "#form1 > div.formbody > table > tbody > tr:nth-child({no}) > td:nth-child(2)";
                    String usedNumRegEx = "#form1 > div.formbody > table > tbody > tr:nth-child({no}) > td:nth-child(3) > input";
                    String firstCategoryNameRegEx = "#form1 > div.formbody > table > tbody > tr:nth-child({no}) > td:nth-child(1) > input[type=\"hidden\"]:nth-child(1)";

                    String originalNumStr = doc.select(StringUtils.replace(originalNumRegEx, "{no}", String.valueOf(i))).text();
                    String usedNumStr = doc.select(StringUtils.replace(usedNumRegEx, "{no}", String.valueOf(i))).attr("value");
                    String firstCategoryName = doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(i))).attr("value");
                    String itemName = doc.select(StringUtils.replace(itemNameRegEx, "{no}", String.valueOf(i))).attr("value");

                    int originalNum = Integer.parseInt(originalNumStr);
                    int usedNum = Integer.parseInt(usedNumStr);
                    int num = originalNum - usedNum;

                    MemberCardItem memberCardItem = memberCardItemMap.get(id);
                    memberCardItem.setItemName(itemName);
                    memberCardItem.setOriginalNum(originalNumStr);
                    memberCardItem.setNum(String.valueOf(num));
                    memberCardItem.setFirstCategoryName(firstCategoryName);
                    memberCardItems.add(memberCardItem);
                }
            }
        }


        String pathname = "C:\\exportExcel\\车店E家卡内项目导出.xlsx";
        String pathname2 = "C:\\exportExcel\\车店E家卡内项目导出(模板).xlsx";
        ExportUtil.exportMemberCardItemSomeFieldDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname2);

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("大小为" + memberCardItems.size());

    }

    private void getALLPages(String url, int num) throws IOException {
        WebClient webClient = getLoginWebClient();
        HtmlPage page = webClient.getPage(url);
        Document doc = Jsoup.parseBodyFragment(page.asXml());

        int total = getTotalPage(doc, num);
        pages.add(page);
        nextPage(page, total);
    }

    private int getTotalPage(Document doc, int index) {
        //尾页按钮的位置:index
        String totalRegEx = "#AspNetPager1 > a:nth-child(" + String.valueOf(index) + ")";
        String totalPageStr = doc.select(totalRegEx).attr("href");
        String getTotalRegEx = "(?<=,').*(?=')";

        String totalStr = CommonUtil.fetchString(totalPageStr, getTotalRegEx);
        int total = Integer.parseInt(totalStr);

        return total;
    }

    private WebClient getLoginWebClient() throws IOException {
        WebClient webClient = WebClientUtil.getWebClient();
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);

        HtmlInput user = loginPage.getHtmlElementById("txtLoginName");
        HtmlInput pwd = loginPage.getHtmlElementById("txtLoginPwd");
        HtmlInput btnLogin = loginPage.getHtmlElementById("btnLogin");

        user.setAttribute("value", userName);
        pwd.setAttribute("value", password);
        btnLogin.click();

        return webClient;
    }

    private void nextPage(HtmlPage page, int num) throws IOException {
        ++count;
        if (count == num)
            return;

        String anchorXPath = "//*[@id=\"AspNetPager1\"]/a[8]";
        HtmlAnchor anchor = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = anchor.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num);
    }

}
