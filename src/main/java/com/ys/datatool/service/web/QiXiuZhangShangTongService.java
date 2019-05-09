package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2019/1/2.
 * 汽修掌上通
 */
@Service
public class QiXiuZhangShangTongService {

    private String MEMBERCARDITEM_URL = "http://xlc.qxgs.net/api/pc/def/sp/ownerVip/vip/";

    private String MEMBERCARD_URL = "http://xlc.qxgs.net/api/pc/def/sp/ownerVip/owners?blVip=1&pageSize=10&pageIndex=";

    private String SERVICE_URL = "http://xlc.qxgs.net/api/pc/sp_service_item?pageSize=10&pageNo=";

    private String BILLDETAIL_URL = "http://xlc.qxgs.net/api/pc/def/carinfo/payhistory/detail/";

    private String BILL_URL = "http://xlc.qxgs.net/api/pc/def/carinfo/payhistory";

    private String CARINFO_URL = "http://xlc.qxgs.net/api/pc/def/sp/owner/getNewOwnerList";

    private String STOCKDETAIL_URL = "http://xlc.qxgs.net/api/pc/def/sp/shop_parts/";

    private String STOCK_URL = "http://xlc.qxgs.net/api/pc/def/sp/shop_parts/overview/page?pageSize=10&itemsCount={num}&pageNo=";

    private String SUPPLIER_URL = "http://xlc.qxgs.net/api/pc/def/sp/spsuppliers/find";

    private String companyName = "汽修掌上通";

    private String COOKIE = "Hm_lvt_c86a6dea8a77cec426302f12c57466e0=1546399091,1546849724; shop=%22%E5%AE%89%E7%B4%A2%E6%B1%BD%E8%BD%A6%E5%85%BB%E6%8A%A4%E6%80%BB%E5%BA%97%22; Hm_lpvt_c86a6dea8a77cec426302f12c57466e0=1546918900; sid=e7149abc-f613-4ea8-b865-aed8c9cc55af";


    /**
     * 会员卡及卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(MEMBERCARD_URL + 1, COOKIE);
        int totalPage = getTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWith(MEMBERCARD_URL + i, COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(response.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String cardId = element.get("ownerId").asText();
                        String memberCardName = element.get("vipLevelName").asText();
                        String name = element.get("ownerName").asText();
                        String phone = element.get("ownerMobile").asText();
                        String balance = element.get("mealsBalance").asText();
                        String dateCreated = element.get("vipCreateTime").asText();
                        dateCreated = DateUtil.formatMillisecond2DateTime(dateCreated);

                        Response res = ConnectionUtil.doPutWithJson(CARINFO_URL, getCarInfoParam(phone, 1), COOKIE);
                        int carTotalPage = getTotalPage(res);

                        if (carTotalPage > 0) {
                            for (int j = 1; j <= carTotalPage; j++) {
                                res = ConnectionUtil.doPutWithJson(CARINFO_URL, getCarInfoParam(phone, j), COOKIE);

                                JsonNode carInfoData = JsonObject.MAPPER.readTree(res.returnContent().asString());
                                JsonNode carInfos = carInfoData.get("data").get(0).get("results");

                                if (carInfos.size() > 0) {
                                    Iterator<JsonNode> iterator = carInfos.iterator();

                                    while (iterator.hasNext()) {
                                        JsonNode e = iterator.next();
                                        String carNumber = e.get("plateNo").asText();

                                        //一个车主多辆车
                                        MemberCard memberCard = new MemberCard();
                                        memberCard.setCompanyName(companyName);
                                        memberCard.setName(name);
                                        memberCard.setCardCode(phone);
                                        memberCard.setPhone(phone);
                                        memberCard.setMemberCardName(memberCardName);
                                        memberCard.setBalance(balance);
                                        memberCard.setDateCreated(dateCreated);
                                        memberCard.setCarNumber(CommonUtil.formatString(carNumber));
                                        memberCards.add(memberCard);
                                    }
                                }
                            }
                        }

                        //卡内项目
                        Response res2 = ConnectionUtil.doGetWith(MEMBERCARDITEM_URL + cardId + "/meals?spVipLevelId=10", COOKIE);
                        JsonNode data = JsonObject.MAPPER.readTree(res2.returnContent().asString());

                        JsonNode body = data.get("data").get(0).get("mealItems");
                        if (body != null) {
                            Iterator<JsonNode> iterator = body.iterator();

                            while (iterator.hasNext()) {
                                JsonNode e = iterator.next();

                                String itemName = e.get("itemName").asText();
                                String carNumber = e.get("remark").asText();
                                String num = e.get("freeTimes").asText();
                                String code = e.get("itemNo").asText();
                                String mealsName = e.get("mealsName").asText();//卡套餐名称
                                String validTime = e.get("expire").asText();

                                if ("0".equals(validTime))
                                    validTime = "";

                                if (!"0".equals(validTime) && !"".equals(validTime))
                                    validTime = DateUtil.formatMillisecond2DateTime(validTime);

                                String isValidForever = CommonUtil.getIsValidForever(validTime);

                                MemberCardItem memberCardItem = new MemberCardItem();
                                memberCardItem.setCompanyName(companyName);
                                memberCardItem.setItemName(itemName);
                                memberCardItem.setCardCode(phone);
                                memberCardItem.setNum(num);
                                memberCardItem.setOriginalNum(num);
                                memberCardItem.setValidTime(validTime);
                                memberCardItem.setIsValidForever(isValidForever);
                                memberCardItem.setCode(code);
                                memberCardItems.add(memberCardItem);
                            }
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\汽修掌上通会员卡.xls";
        String pathname2 = "C:\\exportExcel\\汽修掌上通卡内项目.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname2);
    }

    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(SERVICE_URL + 1, COOKIE);
        int totalPage = getTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWith(SERVICE_URL + i, COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(response.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String name = element.get("name").asText();
                        String code = element.get("no").asText();
                        String price = element.get("levelB").asText();
                        String firstCategoryName = element.get("typeName").asText();

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(name);
                        product.setCode(code);
                        product.setPrice(price);
                        product.setFirstCategoryName(firstCategoryName);
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\汽修掌上通服务.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);

    }

    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        Map<String, String> carMap = new HashMap<>();

        //获取所有客户车辆的Uuid
        Response response = ConnectionUtil.doPutWithJson(CARINFO_URL, getPageParam(1), COOKIE);
        int totalPage = getTotalPage(response);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPutWithJson(CARINFO_URL, getPageParam(i), COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(response.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String carUuid = element.get("carUuid").asText();
                        String carNumber = element.get("plateNo").asText();
                        carMap.put(carUuid, carNumber);

                    }
                }
            }
        }

        if (carMap.size() > 0) {
            for (String uuid : carMap.keySet()) {

                String carNumber = carMap.get(uuid);

                Response res = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(uuid, 1), COOKIE);
                int billTotalPage = getTotalPage(res);

                if (billTotalPage > 0) {
                    for (int i = 1; i <= billTotalPage; i++) {
                        res = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(uuid, i), COOKIE);
                        JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString());

                        JsonNode node = content.get("data").get(0).get("results");
                        if (node.size() > 0) {
                            Iterator<JsonNode> it = node.iterator();

                            while (it.hasNext()) {
                                JsonNode element = it.next();

                                String billNo = element.get("orderNo").asText();
                                String orderUuid = element.get("orderUuid").asText();
                                String totalAmount = element.get("allprices").asText();
                                String remark = element.get("orderType").asText();
                                String mileage = element.get("mileage").asText();
                                String dateEnd = element.get("receiveTime").asText();
                                dateEnd = DateUtil.formatMillisecond2DateTime(dateEnd);

                                Bill bill = new Bill();
                                bill.setCompanyName(companyName);
                                bill.setBillNo(billNo);
                                bill.setTotalAmount(totalAmount);
                                bill.setRemark(remark);
                                bill.setMileage(mileage);
                                bill.setDateEnd(dateEnd);
                                bill.setCarNumber(carNumber);

                                Response res2 = ConnectionUtil.doGetWith(BILLDETAIL_URL + orderUuid, COOKIE);
                                JsonNode body = JsonObject.MAPPER.readTree(res2.returnContent().asString());

                                JsonNode data = body.get("data").get(0).get("hyOrderItemResults");
                                if (data.size() > 0) {
                                    Iterator<JsonNode> services = data.iterator();

                                    while (services.hasNext()) {
                                        JsonNode e = services.next();

                                        String receptionistNam = e.get("principalName").asText();
                                        bill.setReceptionistName(receptionistNam);

                                        String serviceItemName = e.get("itemName").asText();
                                        String serviceItemPrice = e.get("itemPrice").asText();
                                        serviceItemName = serviceItemName + "(" + serviceItemPrice + ")";

                                        //汇总项目
                                        if (bill.getServiceItemNames() != null && !"".equals(bill.getServiceItemNames())) {
                                            serviceItemName = bill.getServiceItemNames() + "," + serviceItemName;
                                            bill.setServiceItemNames(serviceItemName);

                                        }

                                        if (bill.getServiceItemNames() == null) {
                                            bill.setServiceItemNames(serviceItemName);
                                        }

                                        JsonNode goodNode = e.get("hyOrderItemPartResults");
                                        if (goodNode.size() > 0) {
                                            Iterator<JsonNode> goods = goodNode.iterator();
                                            while (goods.hasNext()) {

                                                JsonNode e2 = goods.next();

                                                String goodName = e2.get("partName").asText();
                                                String num = e2.get("amount").asText();
                                                String goodCode = e2.get("code").asText();
                                                String price = e2.get("realPrice").asText();

                                                String goodsNames = goodName + "[" + goodCode + "]" + "*" + num + "(" + price + ")";

                                                //汇总配件
                                                if (bill.getGoodsNames() != null && !"".equals(bill.getGoodsNames())) {
                                                    goodsNames = bill.getGoodsNames() + "," + goodsNames;
                                                    bill.setGoodsNames(goodsNames);
                                                }

                                                if (bill.getGoodsNames() == null) {
                                                    bill.setGoodsNames(goodsNames);
                                                }
                                            }
                                        }
                                    }
                                }

                                bills.add(bill);
                            }
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\汽修掌上通消费记录.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
    }


    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPutWithJson(CARINFO_URL, getPageParam(1), COOKIE);
        int totalPage = getTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPutWithJson(CARINFO_URL, getPageParam(i), COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String carNumber = element.get("plateNo").asText();
                        String name = element.get("ownerName").asText();
                        String phone = element.get("mobile").asText();
                        String carModel = element.get("model").asText();
                        String vin = element.get("vin").asText();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCompanyName(companyName);
                        carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                        carInfo.setName(CommonUtil.formatString(name));
                        carInfo.setPhone(CommonUtil.formatString(phone));
                        carInfo.setCarModel(CommonUtil.formatString(carModel));
                        carInfo.setVINcode(CommonUtil.formatString(vin));
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\汽修掌上通车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }


    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(StringUtils.replace(STOCK_URL, "{num}", "0") + 1, COOKIE);
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get(0).get("len");
        int totalPage = WebClientUtil.getTotalPage(totalNode, 10);
        String countNum = totalNode.asText();

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWith(StringUtils.replace(STOCK_URL, "{num}", countNum) + i, COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(response.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String goodsName = element.get("partName").asText();
                        String productCode = element.get("partCode").asText();
                        String price = element.get("price").asText();
                        String inventoryNum = element.get("amount").asText();
                        String brand = element.get("partBrand").asText();
                        String carModel = element.get("appModels").asText();
                        String unit = element.get("spec").asText();

                        Stock stock = new Stock();
                        stock.setCompanyName(companyName);
                        stock.setGoodsName(goodsName);
                        stock.setProductCode(productCode);
                        stock.setPrice(price);
                        stock.setInventoryNum(inventoryNum);

                        String partId = element.get("partId").asText();
                        Response res = ConnectionUtil.doGetWith(STOCKDETAIL_URL + partId + "/stockDetl", COOKIE);
                        JsonNode body = JsonObject.MAPPER.readTree(res.returnContent().asString());
                        JsonNode data = body.get("data");
                        if (data.size() > 0) {
                            JsonNode stockNode = data.get(0);
                            String storeRoomName = stockNode.get("warehouse").asText();
                            String locationName = stockNode.get("warehouseInfo").asText();

                            stock.setStoreRoomName(storeRoomName);
                            stock.setLocationName(locationName);
                        }

                        stocks.add(stock);

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(goodsName);
                        product.setCode(productCode);
                        product.setPrice(price);
                        product.setBrandName(brand);
                        product.setCarModel(CommonUtil.formatString(carModel));
                        product.setUnit(unit);
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\汽修掌上通库存.xls";
        String pathname2 = "C:\\exportExcel\\汽修掌上通库存商品.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname2);

    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(SUPPLIER_URL, getParam(1), COOKIE);
        int totalPage = getTotalPage(response);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(SUPPLIER_URL, getParam(i), COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(response.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String name = element.get("name").asText();
                        String contactPhone = element.get("phone").asText();
                        String contactName = element.get("contact").asText();
                        String address = element.get("addr").asText();
                        String remark = element.get("remark").asText();
                        String depositBank = element.get("bankName").asText();
                        String accountNumber = element.get("bankNo").asText();

                        Supplier supplier = new Supplier();
                        supplier.setCompanyName(companyName);
                        supplier.setName(CommonUtil.formatString(name));
                        supplier.setContactPhone(CommonUtil.formatString(contactPhone));
                        supplier.setContactName(CommonUtil.formatString(contactName));
                        supplier.setAddress(CommonUtil.formatString(address));
                        supplier.setRemark(CommonUtil.formatString(remark));
                        supplier.setDepositBank(CommonUtil.formatString(depositBank));
                        supplier.setAccountNumber(CommonUtil.formatString(accountNumber));
                        suppliers.add(supplier);
                    }
                }
            }
        }


        String pathname = "C:\\exportExcel\\汽修掌上通供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }

    private int getTotalPage(Response response) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get(0).get("len");
        int totalPage = WebClientUtil.getTotalPage(totalNode, 10);

        return totalPage > 0 ? totalPage : 0;
    }

    private String getBillParam(String carUuid, int pageNo) {
        String param = "{" +
                "\"carUuid\":" +
                "\"" + carUuid + "\"" + "," +
                "\"pageSize\":10," +
                "\"pageIndex\":" + pageNo +
                "}";

        return param;
    }

    private String getPageParam(int pageNo) {
        String param = "{" +
                "\"pageSize\":10," +
                "\"pageIndex\":" + pageNo +
                "}";

        return param;
    }

    private String getParam(int pageNo) {
        String param = "{" +
                "\"all\":1," +
                "\"pageSize\":10," +
                "\"pageIndex\":" + pageNo +
                "}";

        return param;
    }

    private String getCarInfoParam(String ownerInfo, int pageNo) {
        String param = "{" +
                "\"ownerInfo\":" + ownerInfo + "," +
                "\"pageSize\":10," +
                "\"pageIndex\":" + pageNo +
                "}";

        return param;
    }
}
