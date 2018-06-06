package com.ys.datatool.service.web;

import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.Product;
import com.ys.datatool.util.CommonUtil;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mo on @date  2018-06-05.
 */

@Service
public class DianFengService {

    private String REPAIRITEM_URL = "http://www.jmlijun.com/index.php?g=portal&m=adminrepair&a=orders&menuid=203&p=";

    private String CARINFO_URL = "http://www.jmlijun.com/index.php?g=portal&m=admincars&a=index&menuid=199&p=";

    private String preUrl = "http://www.jmlijun.com";

    private String trItemRegEx = "body > div.wrap > table > tbody > tr";

    private Workbook workbook;

    private String COOKIE = "PHPSESSID=r3tuga77uj20tukn5mvqkji895; admin_username=lijun; refersh_time=0";


    @Test
    public void fetchRepairItemData() throws IOException {
        List<Product> products = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        Response response = ConnectionUtil.doGetWithLeastParams(REPAIRITEM_URL + "1", COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String getTotalRegEx = "(?<=p=).*(?=\">尾页)";
        String totalPageStr = CommonUtil.fetchString(doc.toString(), getTotalRegEx);
        int totalPage = Integer.parseInt(totalPageStr);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(REPAIRITEM_URL + String.valueOf(i), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTRSize(doc, trItemRegEx);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String idRegEx = "body > div.wrap > table > tbody > tr:nth-child({no}) > td:nth-child(9) > a";
                        String id = doc.select(StringUtils.replace(idRegEx, "{no}", String.valueOf(j))).attr("href");
                        ids.add(id);
                    }
                }
            }
        }

        if (ids.size() > 0) {
            for (String id : ids) {

                response = ConnectionUtil.doGetWithLeastParams(preUrl + id, COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String trRegEx = "#baoyang > tbody:nth-child(2) > tr";
                int trSize = WebClientUtil.getTRSize(doc, trRegEx);

                if (trSize > 0) {
                    for (int i = 1; i < trSize; i++) {

                        String productNameRegEx = "#baoyang > tbody:nth-child(2) > tr:nth-child({no}) > td.xiangmu.editable";
                        String priceRegEx = "#baoyang > tbody:nth-child(2) > tr:nth-child({no}) > td.danjia.editable";

                        String productName = doc.select(StringUtils.replace(productNameRegEx, "{no}", String.valueOf(i))).text();
                        String price = doc.select(StringUtils.replace(priceRegEx, "{no}", String.valueOf(i))).text();

                        Product product = new Product();
                        product.setProductName(productName);
                        product.setPrice(price);
                        products.add(product);
                    }
                }
            }
        }

        System.out.println("结果为" + products.toString());
        System.out.println("结果为" + products.size());

        String pathname = "D:\\点疯网络商品.xls";
        ExportUtil.exportProductDataInLocal(products, workbook, pathname);

    }

    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Set<String> carIds = new HashSet<>();

        Response response = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + "1", COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String getTotalRegEx = "(?<=p=).*(?=\">尾页)";
        String totalPageStr = CommonUtil.fetchString(doc.toString(), getTotalRegEx);
        int totalPage = Integer.parseInt(totalPageStr);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + String.valueOf(i), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTRSize(doc, trItemRegEx);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String carIdRegEx = "body > div.wrap > table > tbody > tr:nth-child({no}) > td:nth-child(13) > a:nth-child(1)";
                        String carId = doc.select(StringUtils.replace(carIdRegEx, "{no}", String.valueOf(j))).attr("href");
                        carIds.add(carId);
                    }
                }
            }
        }

        if (carIds.size() > 0) {
            for (String carId : carIds) {
                response = ConnectionUtil.doGetWithLeastParams(preUrl + carId, COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String carNumberRegEx = "body > div.wrap > form > div:nth-child(2) > div > input[type=\"text\"]";
                String nameRegEx = "body > div.wrap > form > div:nth-child(3) > div > input[type=\"text\"]";
                String phoneRegEx = "body > div.wrap > form > div:nth-child(4) > div > input[type=\"text\"]";
                String carModelRegEx = "body > div.wrap > form > div:nth-child(5) > div > select > option[selected]";
                String vcInsuranceCompanyRegEx = "body > div.wrap > form > div:nth-child(7) > div > input[type=\"text\"]";
                String vcInsuranceValidDateRegEx = "body > div.wrap > form > div:nth-child(6) > div > input";

                String carNumber = doc.select(carNumberRegEx).attr("value");
                String name = doc.select(nameRegEx).attr("value");
                String phone = doc.select(phoneRegEx).attr("value");
                String carModel = doc.select(carModelRegEx).text();
                String vcInsuranceCompany = doc.select(vcInsuranceCompanyRegEx).attr("value");
                String vcInsuranceValidDate = doc.select(vcInsuranceValidDateRegEx).attr("value");

                CarInfo carInfo = new CarInfo();
                carInfo.setCarNumber(carNumber);
                carInfo.setName(name);
                carInfo.setPhone(phone);
                carInfo.setCarModel(carModel);
                carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                carInfos.add(carInfo);
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("大小为" + carInfos.size());

        String pathname = "D:\\点疯网络车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);
    }
}
