package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.ConnectionUtil;
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
 * Created by mo on @date  2018/6/30.
 * 笛威偲腾
 */
@Service
public class SITENService {

    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////



    private static final String COOKIE = "JSESSIONID=CBDC1C32A12A951FB68E8466767A0927; nav1=102; nav2=102023001; SERVERID=47727ad8b9e9dbfa9c94ad11c15091d8|1500097266|1500097119";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private static final String STOCK_URL = "http://erp.51sten.com/partInfo/findInputPartStock";

    private static final String GOOD_URL = "http://erp.51sten.com/partInfo/findPartListPage";

    private static final String SERVICE_URL = "http://erp.51sten.com/item/getItemInfoListNew";

    private static final String BILLITEM_URL = "http://erp.51sten.com/careSheet/findCareItemByCareId?careId={careId}";

    private static final String BILL_URL = "http://erp.51sten.com/sheet/pageCareSheet";

    private static final String CARINFO_URL = "http://erp.51sten.com/autoInfo/findAutoListPageByMap";

    private static final String BILLCSRF_URL = "http://erp.51sten.com/sheet/careSheetIn";

    private String fileName = "笛威偲腾";

    private String fieldName = "total";

    private BasicNameValuePair row = new BasicNameValuePair("rows", "50");

    private BasicNameValuePair firstPage = new BasicNameValuePair("page", "1");




    @Test
    public void fetchStockData() throws IOException {
        List<Stock> storeRoomInventories = new ArrayList<>();

        //库存小于安全存量
        int isOverStockTotalPage = getStockTotalPage(STOCK_URL, "0", "", "", "");
        String isOverParamValue = getParamValue(getCSRF(), "0", "", "", "");

        //接近安全存量
        int isOverdStockTotalPage = getStockTotalPage(STOCK_URL, "", "1", "", "");
        String isOverdParamValue = getParamValue(getCSRF(), "", "1", "", "");

        //有库存
        int haveStockTotalPage = getStockTotalPage(STOCK_URL, "", "", "2", "");
        String haveStockParamValue = getParamValue(getCSRF(), "", "", "2", "");

        //无库存
        int noStockTotalPage = getStockTotalPage(STOCK_URL, "", "", "", "3");
        String noStockParamValue = getParamValue(getCSRF(), "", "", "", "3");

        fetchStockDataInDifferentType(storeRoomInventories, isOverStockTotalPage, isOverParamValue);
        fetchStockDataInDifferentType(storeRoomInventories, isOverdStockTotalPage, isOverdParamValue);
        fetchStockDataInDifferentType(storeRoomInventories, haveStockTotalPage, haveStockParamValue);
        fetchStockDataInDifferentType(storeRoomInventories, noStockTotalPage, noStockParamValue);

        System.out.println("结果为" + storeRoomInventories.toString());
        System.out.println("大小为" + String.valueOf(storeRoomInventories.size()));

    }

    /**
     * 商品
     *
     * @throws IOException
     */
    public void fetchItemData() throws IOException {
        List<Product> products = new ArrayList<>();

        int serviceTotalPage = getTotalPage(SERVICE_URL);
        if (serviceTotalPage > 0) {
            for (int i = 1; i <= serviceTotalPage; i++) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("page", String.valueOf(i)));
                params.add(row);
                params.add(new BasicNameValuePair("_csrf", getCSRF()));

                Response response = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, params, COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    Product product = new Product();
                    product.setProductName(element.get("itemName").asText());
                    product.setPrice(element.get("unitPrice").asText());
                    product.setFirstCategoryName(element.get("categoryName").asText());
                    product.setCode(element.get("itemNo").asText());
                    product.setItemType("服务项");
                    products.add(product);
                }
            }
        }

        int goodTotalPage = getTotalPage(GOOD_URL);
        if (goodTotalPage > 0) {
            for (int i = 1; i <= goodTotalPage; i++) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("page", i + ""));
                params.add(row);
                params.add(new BasicNameValuePair("_csrf", getCSRF()));

                Response response = ConnectionUtil.doPostWithLeastParams(GOOD_URL, params, COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    Product product = new Product();
                    product.setProductName(element.get("partName").asText());
                    product.setPrice(element.get("marketPrice").asText());
                    product.setFirstCategoryName(element.get("groupName").asText());
                    product.setCode(element.get("firmNo").asText());
                    product.setBrandName(element.get("cnName").asText());
                    product.setUnit(element.get("unit").asText());
                    product.setCode(element.get("selfNo").asText());
                    product.setItemType("配件");
                    products.add(product);
                }
            }
        }
    }

    /**
     * 单据明细
     *
     * @throws IOException
     */
    public void fetchBillDetailData() throws IOException {
        List<BillDetail> billDetails = new ArrayList<>();
        Map<String, String> billMap = new HashMap<>();

        int totalPage = getTotalPage(BILL_URL);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("page", i + ""));
                params.add(row);
                params.add(new BasicNameValuePair("_csrf", getCSRF()));

                Response response = ConnectionUtil.doPostWithLeastParams(BILL_URL, params, COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String careId = element.get("careId").asText();
                    String careNo = element.get("careNo").asText();
                    billMap.put(careId, careNo);
                }
            }
        }

        if (billMap.size() > 0) {
            for (String careId : billMap.keySet()) {
                Response response = ConnectionUtil.doGetWith(StringUtils.replace(BILLITEM_URL, "{careId}", careId), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> careItems = result.get("careItems").iterator();
                while (careItems.hasNext()) {
                    JsonNode element = careItems.next();

                    BillDetail billDetail = new BillDetail();
                    billDetail.setBillNo(billMap.get(careId));
                    billDetail.setItemName(element.get("itemName").asText());
                    billDetail.setNum(element.get("quantity").asText());
                    billDetail.setTotalAmount(element.get("actualPrice").asText());
                    billDetail.setWorkingHour(element.get("hours").asText());
                    billDetails.add(billDetail);
                }

                Iterator<JsonNode> careParts = result.get("careParts").iterator();
                while (careParts.hasNext()) {
                    JsonNode element = careParts.next();

                    BillDetail billDetail = new BillDetail();
                    billDetail.setBillNo(billMap.get(careId));
                    billDetail.setItemName(element.get("partName").asText());
                    billDetail.setNum(element.get("quantity").asText());
                    billDetail.setTotalAmount(element.get("partMoney").asText());
                    billDetail.setFirstCategoryName(element.get("groupName").asText());
                    billDetails.add(billDetail);
                }
            }
        }
    }

    /**
     * 单据
     *
     * @throws IOException
     */
    public void fetchBillData() throws IOException {
        List<Bill> bills = new ArrayList<>();

        int totalPage = getTotalPage(BILL_URL);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("page", i + ""));
                params.add(row);
                params.add(new BasicNameValuePair("_csrf", getCSRF()));

                Response response = ConnectionUtil.doPostWithLeastParams(BILL_URL, params, COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    Bill bill = new Bill();
                    bill.setBillNo(element.get("careNo").asText());
                    bill.setCarNumber(element.get("plateNo").asText());
                    bill.setMileage(element.get("mileage").asText());
                    bill.setPhone(element.get("tel1").asText());
                    bill.setName(element.get("contact").asText());
                    bill.setTotalAmount(element.get("resultPrice").asText());
                    bill.setDiscount("0");
                    bill.setActualAmount(element.get("actualMoney").asText().replace("null", "0"));
                    bill.setDateEnd(element.get("balanceTime").asText().replace("null", ""));
                    bill.setDateExpect(element.get("balanceTime").asText().replace("null", ""));
                    bill.setDateAdded(element.get("intoTime").asText().replace("null", ""));
                    bills.add(bill);
                }
            }
        }
    }

    /**
     * 车辆信息
     *
     * @throws IOException
     */
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        int totalPage = getTotalPage(CARINFO_URL);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("page", i + ""));
                params.add(row);
                params.add(new BasicNameValuePair("_csrf", getCSRF()));

                Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, params, COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarNumber(element.get("plateNo").asText());
                    carInfo.setName(element.get("name").asText());
                    carInfo.setPhone(element.get("tel1").asText());
                    carInfo.setMileage(element.get("autoMileage").asText());
                    carInfo.setBrand(element.get("brandName").asText());
                    carInfo.setCarModel(element.get("modelName").asText() + element.get("engine").asText());
                    carInfo.setEngineNumber(element.get("motorNo").asText().replace("null", ""));
                    carInfo.setVINcode(element.get("vinNo").asText());
                    carInfo.setVcInsuranceValidDate(element.get("vciEndDate").asText().replace("null", ""));
                    carInfo.setVcInsuranceCompany(element.get("insurerName").asText().replace("null", ""));
                    carInfo.setTcInsuranceValidDate(element.get("tciEndDate").asText().replace("null", ""));
                    carInfo.setTcInsuranceCompany(element.get("insurerName").asText().replace("null", ""));
                    carInfos.add(carInfo);
                }
            }
        }
    }

    private void fetchStockDataInDifferentType(List<Stock> storeRoomInventories, int stockTotalPage, String paramValue) throws IOException {
        if (stockTotalPage > 0) {
            for (int i = 1; i <= stockTotalPage; i++) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("param", paramValue));
                params.add(new BasicNameValuePair("page", i + ""));
                params.add(row);
                params.add(new BasicNameValuePair("_csrf", getCSRF()));

                Response response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, params,COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    Stock storeRoomInventory = new Stock();
                    storeRoomInventory.setGoodsName(element.get("partName").asText());
                    storeRoomInventory.setInventoryNum(element.get("quantityCount").asText());
                    storeRoomInventory.setPrice(element.get("buyingPrice1").asText());
                    storeRoomInventories.add(storeRoomInventory);
                }
            }
        }
    }


    private int getStockTotalPage(String url, String isoverType, String isoverdType, String havestockType, String nostockType) throws IOException {

        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("param", getParamValue(getCSRF(), isoverType, isoverdType, havestockType, nostockType)));
        params.add(firstPage);
        params.add(row);
        params.add(new BasicNameValuePair("_csrf", getCSRF()));

        Response res = ConnectionUtil.doPostWithLeastParams(url, params, COOKIE);
        int total = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, 50);

        return total;
    }

    private String getParamValue(String csrf, String isoverType, String isoverdType, String havestockType, String nostockType) {
        String paramValue = "{" + "\"_csrf\":" + "\"" + csrf + "\"" + ","
                + "\"isover\":" + "\"" + isoverType + "\"" + ","
                + "\"isoverd\":" + "\"" + isoverdType + "\"" + ","
                + "\"havestock\":" + "\"" + havestockType + "\"" + ","
                + "\"nostock\":" + "\"" + nostockType + "\"" + ","
                + "\"groupName\":" + "\"\"" + ","
                + "\"partName\":" + "\"\"" + ","
                + "\"selfNo\":" + "\"\"" + ","
                + "\"firmNo\":" + "\"\"" + ","
                + "\"partNo\":" + "\"\"" + ","
                + "\"barCode\":" + "\"\"" + ","
                + "\"partBrand\":" + "\"\"" + ","
                + "\"supplierName\":" + "\"\"" + ","
                + "\"remark\":" + "\"\"" + ","
                + "\"isGeneral\":" + "\"\"" + ","
                + "\"isUsual\":" + "\"\"" + ","
                + "\"storeId\":" + "\"\"" + ","
                + "\"positionId\":" + "\"\""
                + "}";

        return paramValue;
    }

    private int getTotalPage(String url) throws IOException {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(firstPage);
        params.add(row);
        params.add(new BasicNameValuePair("_csrf", getCSRF()));

        Response res = ConnectionUtil.doPostWithLeastParams(url, params, COOKIE);
        int total = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, 50);

        return total;
    }

    private String getCSRF() throws IOException {

        Response response = ConnectionUtil.doGetWith(BILLCSRF_URL, COOKIE);

        String html = response.returnContent().asString();
        Document document = Jsoup.parse(html);

        String csrf = document.select("meta[name='_csrf']").attr("content");

        return csrf != null ? csrf : "";
    }
}
