package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.*;
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

    private String MEMBERCARDITEM_URL = "http://39.108.223.171/cs/membershipCard/info/listBy";

    private String SUPPLIER_URL = "http://39.108.223.171/cs/supplier/info/list";

    private String STOCK_URL = "http://39.108.223.171/cs/reception/info/queryProduct";

    private String MEMBERCARD_URL = "http://39.108.223.171/cs/membershipCard/info/list";

    private String ITEM_URL = "http://39.108.223.171/cs/product/info/list";

    private String CARINFO_URL = "http://39.108.223.171/cs/user/info/carList";

    private String fieldName = "total";

    private String companyName = "智慧车店";

    private int num = 100;//分页参数为10、25、50、100

    private String COOKIE = "JSESSIONID=C7352A6412CB4A6EC01E1AB314396D90; user=zhongqi125; pwd=147258";


    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockData() throws IOException {
        List<Stock> stocks = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams(String.valueOf(num), "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String inventoryNum = element.get("count").asText();
                    String price = element.get("cost").asText();
                    String goodsName = element.get("product").get("productName").asText();
                    String productCode = element.get("product").get("productNumber").asText();
                    String storeRoomName = element.get("warehouse").get("warehouseName").asText();

                    Stock stock = new Stock();
                    stock.setCompanyName(companyName);
                    stock.setPrice(price);
                    stock.setInventoryNum(inventoryNum);
                    stock.setGoodsName(goodsName);
                    stock.setProductCode(productCode);
                    stock.setStoreRoomName(storeRoomName);
                    stocks.add(stock);
                }
            }
        }

        System.out.println("stcok为" + stocks.toString());

        String pathname = "C:\\exportExcel\\智慧车店库存导出.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);

    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams(String.valueOf(num), "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String contactName = element.get("contacts").asText();
                    String contactPhone = element.get("mobilePhone").asText();
                    String name = element.get("supplierName").asText();
                    String code = element.get("supplierCode").asText();
                    String remark = element.get("remark").asText();
                    String address = element.get("addressInfo").asText();

                    Supplier supplier = new Supplier();
                    supplier.setContactName(contactName);
                    supplier.setContactPhone(contactPhone);
                    supplier.setName(name);
                    supplier.setCode(code);
                    supplier.setCompanyName(companyName);
                    supplier.setRemark(remark);
                    supplier.setAddress(CommonUtil.formatString(address));
                    suppliers.add(supplier);
                }
            }
        }

        System.out.println("supplier为" + suppliers.toString());

        String pathname = "C:\\exportExcel\\智慧车店供应商导出.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }

    /**
     * 卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, MemberCard> memberCardMap = new HashMap<>();

        Response response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getParams(String.valueOf(num), "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("id").asText();
                    String cardCode = element.get("numberShipNumber").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCardCode(cardCode);
                    memberCardMap.put(id, memberCard);
                }
            }
        }

        if (memberCardMap.size() > 0) {
            for (String id : memberCardMap.keySet()) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("id", id));

                MemberCard memberCard = memberCardMap.get(id);

                Response res = ConnectionUtil.doPostWithLeastParams(MEMBERCARDITEM_URL, params, COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("list").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String num = element.get("count").asText();
                    String price = element.get("price").asText();
                    String firstCategoryName = element.get("items").get("parentName").asText();
                    String itemName = element.get("items").get("itemName").asText();
                    String code=element.get("items").get("code").asText();

                    String validTimeStr = element.get("endTime").asText();
                    String validTime = CommonUtil.formatString(validTimeStr);
                    if (!"".equals(validTime))
                        validTime = DateUtil.formatSQLDateTime(validTime);

                    String isValidForever = CommonUtil.getIsValidForever(validTime);

                    MemberCardItem memberCardItem = new MemberCardItem();
                    memberCardItem.setCompanyName(companyName);
                    memberCardItem.setCardCode(memberCard.getCardCode());
                    memberCardItem.setItemName(itemName);
                    memberCardItem.setNum(num);
                    memberCardItem.setCode(CommonUtil.formatString(code));
                    memberCardItem.setOriginalNum(num);
                    memberCardItem.setPrice(price);
                    memberCardItem.setFirstCategoryName(firstCategoryName);
                    memberCardItem.setValidTime(validTime);
                    memberCardItem.setIsValidForever(isValidForever);

                    memberCardItems.add(memberCardItem);
                }
            }
        }

        String pathname = "C:\\exportExcel\\智慧车店卡内项目导出.xls";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
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

        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(String.valueOf(num), "0"), COOKIE);
        totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

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

        response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getParams(String.valueOf(num), "0"), COOKIE);
        totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

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
                    memberCard.setCompanyName(companyName);
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

        Response response = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams(String.valueOf(num), "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

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
                    product.setCompanyName(companyName);
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

        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(String.valueOf(num), "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

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
                    carInfo.setCompanyName(companyName);
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

    /**
     * 库存传参 1-全部库存，2-库存正常，3-库存不足
     *
     * @param num
     * @param offset
     * @param type
     * @return
     */
    private List<BasicNameValuePair> getStockParams(String num, String offset, String type) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("search", ""));
        params.add(new BasicNameValuePair("order", "asc"));
        params.add(new BasicNameValuePair("limit", num));
        params.add(new BasicNameValuePair("offset", offset));
        return params;
    }

    private List<BasicNameValuePair> getParams(String num, String offset) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("search", ""));
        params.add(new BasicNameValuePair("order", "asc"));
        params.add(new BasicNameValuePair("limit", num));
        params.add(new BasicNameValuePair("offset", offset));
        return params;
    }


}
