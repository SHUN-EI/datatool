package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.domain.Product;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018-05-29.
 * 智慧车店系统
 */
@Service
public class ZhiHuiCheDianService {

    private String MEMBERCARD_URL = "http://39.108.223.171/cs/membershipCard/info/list";

    private String ITEM_URL = "http://39.108.223.171/cs/product/info/list";

    private String CARINFO_URL = "http://39.108.223.171/cs/user/info/carList";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "total";

    private int num = 100;//分页参数为10、25、50、100

    private String COOKIE = "JSESSIONID=0ED7278FEF1FEF6B37B4ABF38C7BA526";

    @Test
    public void test() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getCarInfoParams(String.valueOf(num), "0"), COOKIE);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());

        System.out.println("结果为" + carInfos.toString());
        System.out.println("大小为" + carInfos.size());

    }



    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Map<String, CarInfo> carInfoMap = new HashMap<>();
        int totalPage = 0;

        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getCarInfoParams(String.valueOf(num), "0"), COOKIE);
        totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getCarInfoParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carNumber = element.get("license").asText();

                    String name = "";
                    String phone = "";
                    if (!"null".equals(element.get("carOwner").asText())) {
                        name = element.get("carOwner").get("name").asText();
                        phone = element.get("carOwner").get("mobileNumber").asText();
                    }

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarNumber(carNumber);
                    carInfo.setName(name);
                    carInfo.setPhone(phone);
                    carInfoMap.put(phone, carInfo);
                }
            }
        }

        response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getCarInfoParams(String.valueOf(num), "0"), COOKIE);
        totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getCarInfoParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String memberCardId = element.get("id").asText();
                    String cardCode = element.get("numberShipNumber").asText();
                    String balance = element.get("totalAmount").asText();
                    String phone = element.get("tel").asText();
                    String name = element.get("memberShipName").asText();
                    String dateCreated = element.get("createTime").asText();

                    CarInfo carInfo = carInfoMap.get(phone);
                    MemberCard memberCard = new MemberCard();
                    memberCard.setCardCode(cardCode);
                    memberCard.setBalance(balance);
                    memberCard.setPhone(phone);
                    memberCard.setName(name);
                    memberCard.setDateCreated(dateCreated);

                    if (carInfo != null)
                        memberCard.setCarNumber(carInfo.getCarNumber());

                    memberCards.add(memberCard);
                }
            }
        }

        String pathname = "C:\\exportExcel\\智慧车店会员卡导出.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    /**
     * 商品
     *
     * @throws IOException
     */
    @Test
    public void fetchItemData() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getCarInfoParams(String.valueOf(num), "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getCarInfoParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);

                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("productName").asText();
                    String price = element.get("guidePrice").asText();
                    String unit = element.get("specifications").asText();

                    String firstCategoryName = "";
                    String secondCategoryName = "";
                    if (!"null".equals(element.get("category").asText())) {
                        firstCategoryName = element.get("category").get("category").asText();
                        secondCategoryName = element.get("category").get("classification").asText();
                    }

                    Product product = new Product();
                    product.setProductName(productName);
                    product.setPrice(price);
                    product.setUnit(unit);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setSecondCategoryName(secondCategoryName);
                    product.setItemType("商品");
                    products.add(product);
                }
            }

            String pathname = "C:\\exportExcel\\智慧车店商品导出.xls";
            ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
        }

        System.out.println("总页数为" + totalPage);
    }

    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getCarInfoParams(String.valueOf(num), "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getCarInfoParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carNumber = element.get("license").asText();
                    String VINCode = element.get("vinnumber").asText();

                    String brand = "";
                    String carModel = "";
                    if (!"null".equals(element.get("carInfo").asText())) {
                        brand = element.get("carInfo").get("brandName").asText();
                        carModel = element.get("carInfo").get("carVersion").asText();
                    }

                    String name = "";
                    String phone = "";
                    if (!"null".equals(element.get("carOwner").asText())) {
                        name = element.get("carOwner").get("name").asText();
                        phone = element.get("carOwner").get("mobileNumber").asText();
                    }

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarNumber(carNumber);
                    carInfo.setVINcode(VINCode == "null" ? "" : VINCode);
                    carInfo.setBrand(brand);
                    carInfo.setCarModel(carModel);
                    carInfo.setName(name);
                    carInfo.setPhone(phone);
                    carInfos.add(carInfo);
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("大小为" + carInfos.size());

        String pathname = "C:\\exportExcel\\智慧车店车辆信息导出.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    private List<BasicNameValuePair> getCarInfoParams(String num, String offset) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("search", ""));
        params.add(new BasicNameValuePair("order", "asc"));
        params.add(new BasicNameValuePair("limit", num));
        params.add(new BasicNameValuePair("offset", offset));
        return params;
    }


}
