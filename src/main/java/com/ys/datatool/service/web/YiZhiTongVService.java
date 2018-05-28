package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.poi.ss.usermodel.Workbook;
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
 * Created by mo on @date  2018-05-28.
 */

@Service
public class YiZhiTongVService {

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
        Response response = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + "bjcl.aspx?id=3", PRECOOKIE + AFTCOOKIE);
        String html = response.returnContent().asString();
        Document document = Jsoup.parse(html);


        String result = "";
        String carNumberRegEx = "#TextBox18";
        String carNumber = document.select(carNumberRegEx).attr("value");

        String brandRegEx = "#DropDownList8";
        Elements elements = document.select(brandRegEx);
        for (Element element : elements) {

            if ("selected".equals(element.attr("selected"))) {
                result = element.text();
                break;
            }

        }

        CarInfo carInfo = new CarInfo();
        carInfo.setBrand(result);

        System.out.println("结果为" + carInfo.toString());
    }

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

                String brandRegEx = "#DropDownList8";
                String brand = document.select(brandRegEx).attr("value");

                String carModelRegEx = "#TextBox1";
                String carModel = document.select(carModelRegEx).attr("value");

                String VINCodeRegEx = "#TextBox32";
                String VINCode = document.select(VINCodeRegEx).attr("value");

                String tcInsuranceCompanyRegEx = "#DropDownList13 > option:nth-child(2)";
                String tcInsuranceCompany = document.select(tcInsuranceCompanyRegEx).text();

                String tcInsuranceValidDateRegEx = "#TextBox38";
                String tcInsuranceValidDate = document.select(tcInsuranceValidDateRegEx).attr("value");

                String vcInsuranceCompanyRegEx = "#DropDownList14 > option:nth-child(2)";
                String vcInsuranceCompany = document.select(vcInsuranceCompanyRegEx).text();

                String vcInsuranceValidDateRegEx = "#TextBox39";
                String vcInsuranceValidDate = document.select(vcInsuranceValidDateRegEx).attr("value");

                CarInfo carInfo = carInfoMap.get(carId);
                carInfo.setRemark(remark);
                carInfo.setBrand(brand);
                carInfo.setCarModel(carModel);
                carInfo.setTcInsuranceCompany(tcInsuranceCompany);
                carInfo.setTcInsuranceValidDate(tcInsuranceValidDate);
                carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                carInfo.setVINcode(VINCode);
                carInfos.add(carInfo);
            }
        }

        System.out.println("carInfos为" + carInfos.toString());
        System.out.println("carInfos大小为" + carInfos.size());

        String pathname = "D:\\上海创磊(御车堂奔宝店)车辆信息导出.xlsx";
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
