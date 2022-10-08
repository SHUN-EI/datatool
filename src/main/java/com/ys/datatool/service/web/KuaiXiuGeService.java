package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.domain.entity.Product;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by mo on @date  2018/7/4.
 * 快修哥系统(好快省)
 */
@Service
public class KuaiXiuGeService {

    private String BILL_URL = "http://www.kuaixiuge.com/MaintenanceOrder.aspx?clientWidth=1680";

    private String BILLDETAIL_URL = "http://www.kuaixiuge.com/MOrderInfoNews.aspx?morder_gch=";

    private String PART_URL = "http://saas.hks360.com/PartsSet.aspx?page=PartsSet&cid=6079&username=admin";

    private String LOGIN_URL = "http://www.kuaixiuge.com/";

    private String COMPANYID = "ucode";

    private String USERID = "uname";

    private String PWDID = "upwd";

    private String LOGINBUTTON = "Button1";

    private String COMPANYNAME = "6079";

    private String USERNAME = "admin";

    private String PASSWORD = "zzq2008824";

    private String tableRegEx = "#TreeView1 > table";

    private String tableName = "table";

    private String trName = "tr";

    private int count = 0;

    private List<HtmlPage> pages = new ArrayList();

    private int partEnd = 10;//配件维护最后几页

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Test
    public void fetchBillData() throws IOException {
        List<Bill> bills = new ArrayList<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllBillDetailPages(webClient);

        for (HtmlPage billPage : pages) {
            HtmlTable billTable = (HtmlTable) billPage.getElementById("GridView1");

            Document document = Jsoup.parse(billTable.asXml());
            Elements trs = document.select("tr");

            for (int i = 0; i < trs.size(); i++) {

                Elements tds = trs.get(i + 1).select("td");

                //订单号
                String billNo = tds.get(0).text();

                HtmlPage billDetailPage = webClient.getPage(BILLDETAIL_URL + billNo);

                String billDetail = billDetailPage.asXml();
                String point = "";

            }


        }

    }

    @Test
    public void fetchItemData() throws IOException {
        List<Product> products = new ArrayList<>();
        Map<String, String> categoryMap = new HashMap<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllItemPages(webClient);

        HtmlPage partPage = webClient.getPage(PART_URL);
        Document doc = Jsoup.parseBodyFragment(partPage.asXml());
        int tableSize = WebClientUtil.getTagSize(doc, tableRegEx, tableName);
        if (tableSize > 0) {
            //0为全部
            for (int i = 1; i < tableSize; i++) {
                String categoryRegEx = "#TreeView1t" + String.valueOf(i);
                String category = doc.select(categoryRegEx).text();

                String preRegEx = ".*-";
                String aftRegEx = "-.*";
                String categoryCode = CommonUtil.fetchString(category, preRegEx).replace("-", "");
                String categoryName = CommonUtil.fetchString(category, aftRegEx).replace("-", "");

                if (StringUtils.isNotBlank(categoryCode))
                    categoryMap.put(categoryCode, categoryName);
            }
        }

        for (HtmlPage page : pages) {
            Document document = Jsoup.parseBodyFragment(page.asXml());

            String trRegEx = "#GridView1 > tbody > tr";
            int trSize = WebClientUtil.getTagSize(document, trRegEx, trName);
            if (trSize > 0) {
                for (int i = 2; i <= trSize; i++) {

                    String codeRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String productNameRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(2)";
                    String unitRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(3)";
                    String priceRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(4)";
                    String originRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(5)";
                    String carModelRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(6)";
                    String firstCategoryNameRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(8)";

                    String code = document.select(StringUtils.replace(codeRegEx, "{no}", String.valueOf(i))).text();
                    String productName = document.select(StringUtils.replace(productNameRegEx, "{no}", String.valueOf(i))).text();
                    String unit = document.select(StringUtils.replace(unitRegEx, "{no}", String.valueOf(i))).text();
                    String price = document.select(StringUtils.replace(priceRegEx, "{no}", String.valueOf(i))).text();
                    String origin = document.select(StringUtils.replace(originRegEx, "{no}", String.valueOf(i))).text();
                    String carModel = document.select(StringUtils.replace(carModelRegEx, "{no}", String.valueOf(i))).text();
                    String firstCategoryCode = document.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(i))).text();
                    String firstCategoryName = categoryMap.get(firstCategoryCode);

                    Product product = new Product();
                    product.setCode(code);
                    product.setProductName(productName);
                    product.setUnit(unit);
                    product.setPrice(price);
                    product.setOrigin(origin);
                    product.setCarModel(carModel);
                    product.setItemType("配件");
                    product.setFirstCategoryName(firstCategoryName);
                    products.add(product);
                }
            }
        }

        System.out.println("products结果为" + products.toString());
        System.out.println("products结果为" + products.size());

        //String pathname = "C:\\exportExcel\\快修哥商品.xls";
        String pathname = "/Users/mo/work/快修哥商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 获取所有订单页
     *
     * @param webClient
     * @throws IOException
     */
    private void getAllBillDetailPages(WebClient webClient) throws IOException {
        login(webClient);

        HtmlPage billPage = webClient.getPage(BILL_URL);
        //工单状态- 全部,在修,已结算,已付款,已作废
        HtmlSelect billStatusSelect = (HtmlSelect) billPage.getElementById("DropDownStatus");
        billStatusSelect.setSelectedAttribute("全部", true);

        //送修日期: 2005-01-01  to  2022-10-08
        HtmlInput billStartTimeInput = (HtmlInput) billPage.getElementById("TextTime1");
        billStartTimeInput.setAttribute("value", "2005-01-01");

        HtmlInput billEndTimeInput = (HtmlInput) billPage.getElementById("TextTime2");
        billEndTimeInput.setValueAttribute("2022-10-08");

        //搜索
        HtmlInput searchInput = (HtmlInput) billPage.getElementById("Button3");
        HtmlPage billAllPage = searchInput.click();

        //获取总页数
        String totalPage = WebClientUtil.getTotalPage(billAllPage);
        //获取所有订单页
        //nextBillPage(billAllPage, Integer.parseInt(totalPage));

        //测试，先取2页
        nextBillPage(billAllPage, 2);

    }


    /**
     * 获取下一页订单
     *
     * @param page 订单页
     * @param num  订单总页数
     * @throws IOException
     */
    private void nextBillPage(HtmlPage page, int num) throws IOException {
        ++count;
        if (count > num)
            return;

        HtmlInput billPageInput = (HtmlInput) page.getElementById("AspNetPager1_input");
        billPageInput.setValueAttribute(String.valueOf(count));

        //go按钮,下一页
        HtmlInput billNextInput = (HtmlInput) page.getElementById("AspNetPager1_btn");
        HtmlPage billNextPage = billNextInput.click();
        pages.add(billNextPage);

        nextBillPage(billNextPage, num);
    }


    private void nextPage(HtmlPage page, int num, int end) throws IOException {
        ++count;
        if (count == num)
            return;

        String anchorXPath = "//*[@id=\"AspNetPager2\"]/a[13]";
        //最后几页的下一页按钮
        if (count > end)
            anchorXPath = "//*[@id=\"AspNetPager2\"]/a[12]";

        HtmlAnchor nextPage = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = nextPage.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num, end);//num为总页数
    }

    private void getAllItemPages(WebClient webClient) throws IOException {
        login(webClient);

        HtmlPage partPage = webClient.getPage(PART_URL);
        //是否有效选项-选择全部
        HtmlSelect select = (HtmlSelect) partPage.getElementById("drpIsEnable");
        select.setSelectedAttribute("全部", true);

        //TreeView1t0
        HtmlInput input = (HtmlInput) partPage.getElementById("Button3");
        HtmlPage allPartPage = input.click();
        pages.add(allPartPage);

        String total = WebClientUtil.getTotalPage(allPartPage);
        nextPage(allPartPage, Integer.parseInt(total), partEnd);
    }

    /**
     * 登录操作
     *
     * @param webClient
     * @throws IOException
     */
    private void login(WebClient webClient) throws IOException {
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);
        HtmlInput company = loginPage.getHtmlElementById(COMPANYID);
        HtmlInput user = loginPage.getHtmlElementById(USERID);
        HtmlInput pwd = loginPage.getHtmlElementById(PWDID);
        HtmlSpan btnLogin = loginPage.getHtmlElementById(LOGINBUTTON);

        company.setValueAttribute(COMPANYNAME);
        user.setValueAttribute(USERNAME);
        pwd.setValueAttribute(PASSWORD);
        btnLogin.click();
    }


}
