package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.ys.datatool.domain.Product;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
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
 * Created by mo on @date  2018/7/4.
 * 快修哥系统(好快省)
 */
@Service
public class KuaiXiuGeService {

    private String PART_URL = "http://saas.hks360.com/PartsSet.aspx?page=PartsSet&cid=5188&username=fumeijing";

    private String LOGIN_URL = "http://saas.hks360.com/default.aspx?id=0";

    private String COMPANYID = "ucode";

    private String USERID = "uname";

    private String PWDID = "upwd";

    private String LOGINBUTTON = "Button1";

    private String COMPANYNAME = "5188";

    private String USERNAME = "fumeijing";

    private String PASSWORD = "hks123";

    private String tableRegEx = "#TreeView1 > table";

    private String tableName = "table";

    private String trName = "tr";

    private int count = 0;

    private List<HtmlPage> pages = new ArrayList();

    private int partEnd = 10;//配件维护最后几页

    @Test
    public void testRegEx() throws IOException {

        WebClient webClient = WebClientUtil.getWebClient();
        getAllItemPages(webClient);

        Document doc = Jsoup.parseBodyFragment(pages.get(0).asXml());
        String a = doc.html();
        String b = "";


        System.out.println("结果为" + pages.size());
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

        String pathname = "C:\\exportExcel\\快修哥商品.xls";
        //ExportUtil.exportProductDataInLocal(products, workbook, pathname);
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


}
