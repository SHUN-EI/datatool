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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/6/30.
 * 典典养车
 */
@Service
public class DianDianYangCheService {

    private String STOCK_URL = "https://ndsm.ddyc.com/ndsm/stock/getList";

    private String BILLDETAIL_URL = "https://ndsm.ddyc.com/ndsm/order/getOrderInfo?orderId=";

    private String BILL_URL = "https://ndsm.ddyc.com/ndsm/work/getWorkPageList";

    private String CARINFODETAIL_URL = "https://ndsm.ddyc.com/ndsm/car/carDetails?carInfoId=";

    private String CARINFO_URL = "https://ndsm.ddyc.com/ndsm/car/getShopCarList";

    private String MEMBERCARD_URL = "https://ndsm.ddyc.com/ndsm/member/list";

    private String SERVICE_URL = "https://ndsm.ddyc.com/ndsm/commodity/service/list";

    private String companyName = "典典养车";

    private int num = 10;

    private static final String COOKIE = "gr_user_id=5b4ec60a-f3cd-4586-a294-73c73b41a61b; JSESSIONID=42C83174AF15462D55A9FC13126088EE; gr_session_id_e2f213a5f5164248817464925de8c1af=db353cbe-24a7-4504-ba0f-8628f53b0093; gr_session_id_e2f213a5f5164248817464925de8c1af_db353cbe-24a7-4504-ba0f-8628f53b0093=true";


    /**
     * 服务
     * <p>
     * 打开路径：更多-设置-服务项目管理
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();


    }

    /**
     * 库存
     * <p>
     * 打开路径:仓库-库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(STOCK_URL, getStockParam(1), COOKIE);
        int totalPage = getTotalPageNo(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParamJson(STOCK_URL, getStockParam(i), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").get("data").elements();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String goodsName = element.get("commodityName").asText();
                    String productCode = element.get("commodityCode").asText();
                    String inventoryNum = element.get("count").asText();//库存数量
                    String price = element.get("costPrice").asText();//成本价
                    String salePrice = element.get("price").asText();//销售价
                    String firstCategoryName = element.get("lv1CategoryName").asText();
                    String secondCategoryName = element.get("lv2CategoryName").asText();

                    if ("null".equals(price))
                        price = "0";

                    if ("null".equals(salePrice))
                       salePrice = "0";

                    Stock stock = new Stock();
                    stock.setCompanyName(companyName);
                    stock.setGoodsName(goodsName);
                    stock.setProductCode(productCode);
                    stock.setInventoryNum(inventoryNum);
                    stock.setPrice(price);
                    stocks.add(stock);

                    Product product = new Product();
                    product.setCompanyName(companyName);
                    product.setProductName(goodsName);
                    product.setPrice(salePrice);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setSecondCategoryName(secondCategoryName);
                    products.add(product);

                }
            }
        }

        String pathname = "C:\\exportExcel\\典典养车库存.xls";
        String pathname2 = "C:\\exportExcel\\典典养车商品.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname2);
    }

    /**
     * 历史消费记录和消费记录相关车辆
     * <p>
     * 打开路径:工单-工单列表
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(1), COOKIE);
        int totalPage = getTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(i), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").get("data").elements();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("workId").asText();
                    String carNumber = element.get("carNumber").asText();
                    String dateEnd = element.get("businessStarDate").asText();
                    dateEnd = DateUtil.formatSQLDate(dateEnd);

                    String id = element.get("orderId").asText();
                    String totalAmount = element.get("receivableAccount").asText();//应收
                    String actualAmount = element.get("shopGetMoney").asText();//实收

                    Bill bill = new Bill();
                    bill.setCompanyName(companyName);
                    bill.setBillNo(billNo);
                    bill.setCarNumber(carNumber);
                    bill.setDateEnd(dateEnd);
                    bill.setTotalAmount(totalAmount);


                    System.out.println("车牌号为" + carNumber);
                    System.out.println("网址为" + BILLDETAIL_URL + id);

                    if (!"null".equals(id)) {
                        Response res2 = ConnectionUtil.doGetWithLeastParams(BILLDETAIL_URL + id, COOKIE);
                        JsonNode content = JsonObject.MAPPER.readTree(res2.returnContent().asString());

                        if (content.hasNonNull("carServicePackageList") == true) {
                            JsonNode data = content.get("carServicePackageList");
                            if (data.size() > 0) {

                                String num = data.get("amount").asText();
                                String serviceItemNames = data.get("packageName").asText();
                                String price = data.get("unitPrice").asText();

                                if (null != bill.getServiceItemNames() && !"".equals(serviceItemNames)) {
                                    serviceItemNames = serviceItemNames + "*" + price;
                                    String s = bill.getServiceItemNames() + "," + serviceItemNames;
                                    bill.setServiceItemNames(s);
                                }

                                if (null == bill.getServiceItemNames()) {
                                    bill.setServiceItemNames(serviceItemNames + "*" + price);
                                }

                                String receptionistName = data.get("workerList").get(0).get("workerName").asText();
                                bill.setReceptionistName(receptionistName);

                            }
                        }
                    }

                    bills.add(bill);
                }
            }
        }

        System.out.println("结果为" + bills.toString());
        String pathname = "C:\\exportExcel\\典典养车消费记录.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
    }

    /**
     * 车辆信息
     * <p>
     * 打开路径:客户-车辆列表
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getTotalParams("1"), COOKIE);
        int totalPage = getTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getTotalParams(String.valueOf(i)), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").get("data").elements();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carId = element.get("carInfoId").asText();
                    String carNumber = element.get("carNumber").asText();
                    String phone = element.get("phone").asText();
                    String name = element.get("carOwnerName").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCompanyName(companyName);
                    carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                    carInfo.setName(CommonUtil.formatString(name));
                    carInfo.setPhone(CommonUtil.formatString(phone));

                    Response res2 = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + carId, COOKIE);
                    JsonNode content = JsonObject.MAPPER.readTree(res2.returnContent().asString());

                    if (content.hasNonNull("data") == true) {
                        JsonNode data = content.get("data");
                        String brand = data.get("carBrandName").asText();
                        String carModel = data.get("carModelName").asText();
                        String vin = data.get("vinCode").asText();

                        carInfo.setBrand(CommonUtil.formatString(brand));
                        carInfo.setCarModel(CommonUtil.formatString(carModel));
                        carInfo.setVINcode(CommonUtil.formatString(vin));
                    }
                    carInfos.add(carInfo);
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());

        String pathname = "C:\\exportExcel\\典典养车车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);


    }

    /**
     * 会员卡及卡内项目
     * <p>
     * 打开路径:客户-会员充值
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(MEMBERCARD_URL, getParam(1), COOKIE);
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get("total");
        int totalPage = WebClientUtil.getTotalPage(totalNode, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(MEMBERCARD_URL, getParam(i), COOKIE);
                result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardName(element.get("memberId").asText());
                    memberCard.setName(element.get("name").asText());
                    memberCard.setPhone(element.get("phone").asText());
                    memberCard.setBalance(element.get("asset").get("balance").asText());

                    JsonNode carNode = element.get("carList");
                    if ("null" != carNode.asText())
                        memberCard.setCarNumber(carNode.get(0).get("plateNumber").asText());

                    memberCards.add(memberCard);
                }
            }
        }

        System.out.println("总页数为" + totalPage);
        System.out.println("总数为" + memberCards.size());
        System.out.println("结果为" + memberCards.toString());

    }

    private String getStockParam(int pageNo) {
        String param = "{" +
                "\"lv1CategoryCode\":\"\"," +
                "\"lv2CategoryCode\":\"\"," +
                "\"commodityName\":\"\"," +
                "\"pageSize\":10," +
                "\"page\":" + pageNo + "," +
                "\"sortByCount\":false," +
                "\"sortDefType\":false" +
                "}";

        return param;
    }

    private String getBillParam(int pageNo) {
        String param = "{" +
                "\"pageNumber\":" +
                pageNo + "," +
                "\"pageSize\":10," +
                "\"queryParam\":" +
                "{\"carNumber\":\"\"," +
                "\"userPhone\":\"\"," +
                "\"workEndDate\":null," +
                "\"workStarDate\":null," +
                "\"workStatus\":0," +
                "\"workCode\":null," +
                "\"dateRange\":\"\"," +
                "\"workOrderId\":\"\"," +
                "\"salesmanId\":\"\"," +
                "\"workOrderType\":\"\"," +
                "\"orderCategory\":\"0\"," +
                "\"workStatusId\":\"\"}" +
                "}";

        return param;
    }

    private List<BasicNameValuePair> getTotalParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNumber", pageNo));
        return params;
    }

    private String getParam(int index) {
        String page = String.valueOf(index);
        String param = "{" + "\"page\":" + page + "," + "\"pageSize\":" + "10" + "}";

        return param;
    }

    private int getTotalPage(Response response) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
        String totalStr = result.get("data").get("totalPage").asText();
        int total = Integer.parseInt(totalStr);

        return total;

    }

    private int getTotalPageNo(Response response) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get("total");
        int totalPage = WebClientUtil.getTotalPage(totalNode, num);
        return totalPage;
    }
}
