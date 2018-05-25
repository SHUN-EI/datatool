package com.ys.datatool.service.web;

import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.Product;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/5/14.
 * 元乐车宝
 */
@Service
public class YuanLeCheBaoService {

    private static final String CARINFOPAGE_URL = "http://www.carbao.vip/Home/car/carTable";

    private static final String CARINFODETAIL_URL = "http://www.carbao.vip/Home/car/carDetail";

    private static final String ACCEPT = "text/html, */*; q=0.01";

    private static final String ORIGIN = "http://www.carbao.vip";

    private static final String REFERER = "http://www.carbao.vip/Home/Index/index";

    private static final String HOST = "www.carbao.vip";

    private static final String CONNECTION = "keep-alive";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";

    private static final String X_REQUESTED_WITH = "XMLHttpRequest";

    private String trItemRegEx = "#content-tbody > tr";

    private Workbook workbook;

    private static final String COOKIE = "JSESSIONID=6A602C44A662A61EAE481874254FAF30; usfl=zv2cFTL2dDoAZzvwHez; lk=612958b7a20dbf293e8eb2cbf2fba121";

    //车店编号-shopId:82(洗车王国)
    private String companyId = "82";

    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();


    }

    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPost(CARINFOPAGE_URL, getCarInfoPageParams("1"), ACCEPT, COOKIE, CONNECTION, HOST, ORIGIN, REFERER, USER_AGENT, X_REQUESTED_WITH);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String totalPageRegEx = "totalPage =.*";
        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalPageRegEx).replace("totalPage = ", "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPost(CARINFOPAGE_URL, getCarInfoPageParams(String.valueOf(i)), ACCEPT, COOKIE, CONNECTION, HOST, ORIGIN, REFERER, USER_AGENT, X_REQUESTED_WITH);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTRSize(doc, trItemRegEx);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String clientRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(9) > a:nth-child(1)";

                        String carId = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("userid");
                        String phone = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("mobile");
                        String name = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("username");
                        String carnum = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("carnum");
                        String cararea = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("cararea");
                        String carNumber = cararea + carnum;

                        CarInfo carInfo = new CarInfo();
                        carInfo.setPhone(phone);
                        carInfo.setName(name);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setCarId(carId);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String userName = carInfo.getName();
                String userId = carInfo.getCarId();
                String carArea = carInfo.getBrand();
                String carNum = carInfo.getCarNumber();
                String mobile = carInfo.getPhone();

                response = ConnectionUtil.doPost(CARINFODETAIL_URL, getCarInfoDetailParams(userName, userId, carArea, carNum, mobile), ACCEPT, COOKIE, CONNECTION, HOST, ORIGIN, REFERER, USER_AGENT, X_REQUESTED_WITH);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String carNumberRegEx = "#is_bang > div > div:nth-child(1) > span:nth-child(3)";
                String VINRegEx = "#isReset > table > tbody > tr > td:nth-child(1) > span";
                String brandRegEx = "#isReset > table > tbody > tr > td:nth-child(2)";
                String carModelRegEx = "#isReset > table > tbody > tr > td:nth-child(3)";

                String VINcode = doc.select(VINRegEx).text().replace("VIN：", "");
                String carModel = doc.select(carModelRegEx).text().replace("车型：", "");
                String brand = doc.select(brandRegEx).text().replace("品牌：", "");
                String carNumber = doc.select(carNumberRegEx).text();

                carInfo.setCarNumber(carNumber);
                carInfo.setVINcode(VINcode);
                carInfo.setCarModel(carModel);
                carInfo.setBrand(brand);
            }
        }

        System.out.println("结果为" + totalPageStr);
        System.out.println("结果为" + totalPage);
        System.out.println("车辆分别为" + carInfos.toString());
        System.out.println("车辆大小为" + carInfos.size());

        String pathname = "D:\\元乐车宝车辆信息导出.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);
    }

    private List<BasicNameValuePair> getServiceParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("keyword", ""));
        params.add(new BasicNameValuePair("categoryId", ""));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        return params;
    }

    private List<BasicNameValuePair> getCarInfoDetailParams(String userName, String userId, String carArea, String carNum, String mobile) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("staffId", "3574"));
        params.add(new BasicNameValuePair("shopBranchId", "229"));
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("carArea", carArea));
        params.add(new BasicNameValuePair("carNum", carNum));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("pageType", "2"));
        return params;
    }

    private List<BasicNameValuePair> getCarInfoPageParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopBranchId", "229"));
        params.add(new BasicNameValuePair("staffId", "3468"));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        return params;
    }

}
