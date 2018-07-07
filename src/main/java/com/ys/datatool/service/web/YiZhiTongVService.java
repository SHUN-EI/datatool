package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.Product;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.poi.ss.usermodel.Workbook;
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
 * Created by mo on @date  2018-05-28.
 * 易智通V+系统
 */

@Service
public class YiZhiTongVService {

    private String SERVICE_URL = "http://121.41.7.113:200/main/task/task_config/task_config_ration.aspx";

    private String CARINFODETAIL_URL = "http://121.41.7.113:200/main/khda/";

    private String LOGIN_URL = "http://121.41.7.113:200/chainindex.aspx";

    private String CARINFO_URL = "http://121.41.7.113:200/main/khda/khxx.aspx";

    private String HOST = "121.41.7.113:200";

    private List<HtmlPage> pages = new ArrayList();

    private int count = 0;

    private Workbook workbook;

    private String PRECOOKIE = "danwei=id=2016030901&name=81; ASP.NET_SessionId=hh04q1pfymcgf0dlcck0cfh4;";

    private String AFTCOOKIE = " userinfo1=id=2016030901&acountid=81&sid=张坤&name=张坤; main=height=607&width=1336";

    @Test
    public void test() throws IOException {

    }

    /**
     * 服务项目
     * @throws IOException
     */
    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();

        WebClient webClient = getLoginWebClient();
        HtmlPage servicePage = webClient.getPage(SERVICE_URL);
        Document doc = Jsoup.parseBodyFragment(servicePage.asXml());
        String totalRegEx = "#GridView1_ctl33_Label1";
        String totalStr = doc.select(totalRegEx).text();
        int total = Integer.parseInt(totalStr);

        pages.add(servicePage);
        nextPage(servicePage, total);

        for (int i = 0; i < pages.size(); i++) {
            for (int j = 2; j <= Integer.parseInt("30"); j++) {
                doc = Jsoup.parseBodyFragment(pages.get(i).asXml());

                String productNameRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(4) > a";
                String productName = doc.select(StringUtils.replace(productNameRegEx, "{no}", j + "")).text();

                String codeRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(3) > a";
                String code = doc.select(StringUtils.replace(codeRegEx, "{no}", j + "")).text();

                String firstCategoryNameRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(5)";
                String firstCategoryName = doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", j + "")).text();

                String priceRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(8)";
                String price = doc.select(StringUtils.replace(priceRegEx, "{no}", j + "")).text();

                String remarkRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(11)";
                String remark = doc.select(StringUtils.replace(remarkRegEx, "{no}", j + "")).text();

                Product product = new Product();
                product.setProductName(productName);
                product.setCode(code);
                product.setFirstCategoryName(firstCategoryName);
                product.setPrice(price);
                product.setRemark(remark);
                product.setItemType("服务项");
                products.add(product);
            }
        }


        System.out.println("服务项目为" + products.toString());
        System.out.println("服务共有为" + products.size());

        String pathname = "C:\\exportExcel\\易智通服务项目导出.xls";
        ExportUtil.exportProductDataInLocal(products, workbook, pathname);
    }

    /**
     * 车辆信息
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Map<String, CarInfo> carInfoMap = getCarInfos();

        if (carInfoMap.size() > 0) {
            for (String carId : carInfoMap.keySet()) {
                Response response = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + carId, PRECOOKIE + AFTCOOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String carNumberRegEx = "#TextBox18";
                String carNumber = document.select(carNumberRegEx).attr("value");

                String nameRegEx = "#TextBox26";
                String name = document.select(nameRegEx).attr("value");

                String phoneRegEx = "#TextBox28";
                String phone = document.select(phoneRegEx).attr("value");

                String remarkRegEx = "#txtclbz";
                String remark = document.select(remarkRegEx).attr("value");

                String brandRegEx = "#DropDownList8 > option[selected]";
                String brand = document.select(brandRegEx).text();

                String carModelRegEx = "#DropDownList3 > option[selected]";
                String carModel = document.select(carModelRegEx).text();

                String VINCodeRegEx = "#TextBox21";
                String VINCode = document.select(VINCodeRegEx).attr("value");

                String tcInsuranceCompanyRegEx = "#DropDownList13 > option[selected]";
                String tcInsuranceCompany = document.select(tcInsuranceCompanyRegEx).text();

                String tcInsuranceValidDateRegEx = "#TextBox38";
                String tcInsuranceValidDate = document.select(tcInsuranceValidDateRegEx).attr("value");

                String vcInsuranceCompanyRegEx = "#DropDownList14 > option[selected]";
                String vcInsuranceCompany = document.select(vcInsuranceCompanyRegEx).text();

                String vcInsuranceValidDateRegEx = "#TextBox39";
                String vcInsuranceValidDate = document.select(vcInsuranceValidDateRegEx).attr("value");

                //年审
                String annualTrialRegEx = "#TextBox23";
                String annualTrial = document.select(annualTrialRegEx).attr("value");

                //驾照到期时间
                String registerDateRegEx = "#TextBox24";
                String registerDate = document.select(registerDateRegEx).attr("value");

                CarInfo carInfo = carInfoMap.get(carId);
                carInfo.setRemark(annualTrial);
                carInfo.setBrand(brand);
                carInfo.setCarModel(carModel);
                carInfo.setTcInsuranceCompany(tcInsuranceCompany);
                carInfo.setTcInsuranceValidDate(tcInsuranceValidDate);
                carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                carInfo.setVINcode(VINCode);
                carInfo.setRegisterDate(registerDate);
                carInfos.add(carInfo);
            }
        }

        System.out.println("carInfos为" + carInfos.toString());
        System.out.println("carInfos大小为" + carInfos.size());

        String pathname = "C:\\exportExcel\\易智通车辆信息导出.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);

    }


    public Map<String, CarInfo> getCarInfos() throws IOException {
        Map<String, CarInfo> carInfoMap = new HashMap<>();

        WebClient webClient = getLoginWebClient();
        HtmlPage carInfoPage = webClient.getPage(CARINFO_URL);
        Document doc = Jsoup.parseBodyFragment(carInfoPage.asXml());
        String totalRegEx = "#GridView1_ctl33_Label1";
        String totalStr = doc.select(totalRegEx).text();
        int total = Integer.parseInt(totalStr);

        pages.add(carInfoPage);
        nextPage(carInfoPage, total);

        for (int i = 0; i < pages.size(); i++) {
            addCarInfos(carInfoMap, pages.get(i), Integer.parseInt("30"));
        }

        return carInfoMap;
    }

    private void addCarInfos(Map<String, CarInfo> carInfoMap, HtmlPage page, int total) {

        for (int i = 1; i <= total; i++) {
            Document doc = Jsoup.parseBodyFragment(page.asXml());

            String carIdRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(4) > a";
            String carId = doc.select(StringUtils.replace(carIdRegEx, "{no}", i + "")).attr("href");

            String nameRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(7) > a";
            String name = doc.select(StringUtils.replace(nameRegEx, "{no}", i + "")).text();

            String phoneRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(10)";
            String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", i + "")).text();

            String carNumberRegEx = "#GridView1 > tbody > tr:nth-child({no}) > td:nth-child(4) > a";
            String carNumber = doc.select(StringUtils.replace(carNumberRegEx, "{no}", i + "")).text();

            if (StringUtils.isNotBlank(carId)) {
                CarInfo carInfo = new CarInfo();
                carInfo.setCarNumber(carNumber);
                carInfo.setName(name);
                carInfo.setPhone(phone);
                carInfoMap.put(carId, carInfo);
            }
        }
    }

    private WebClient getLoginWebClient() throws IOException {
        WebClient webClient = WebClientUtil.getWebClient();
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);
        HtmlSelect company = loginPage.getHtmlElementById("DropDownList1");
        HtmlSelect user = loginPage.getHtmlElementById("username");
        HtmlInput pwd = loginPage.getHtmlElementById("usrpwd");
        HtmlInput btnLogin = loginPage.getHtmlElementById("Button1");

        company.setSelectedAttribute("2016030901", true);
        user.setSelectedAttribute("81", true);
        pwd.setAttribute("value", "188246aA");
        btnLogin.click();

        return webClient;
    }

    private void nextPage(HtmlPage page, int num) throws IOException {
        ++count;
        if (count == num)
            return;
        String anchorXPath = "//*[@id=\"GridView1_ctl33_lbNext\"]";
        HtmlAnchor anchor = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = anchor.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num);
    }

}
