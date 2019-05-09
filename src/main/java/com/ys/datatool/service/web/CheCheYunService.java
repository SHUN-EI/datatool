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
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mo on  2018/8/2.
 * 车车云系统
 */
@Service
public class CheCheYunService {

    private String MEMBERCARDITEM_URL = "https://www.checheweike.com/erp/index.php?route=member/api/ext_info&exdata=combo,balance&vip_user_id=";

    private String MEMBERCARDCAR_URL = "https://www.checheweike.com/erp/index.php?route=member/api/ext_info&exdata=car&vip_user_id=";

    private String MEMBERCARDCLIENT_URL = "https://www.checheweike.com/erp/index.php?route=member/customer/detail&vip_user_id=";

    private String MEMBERCARD_URL = "https://www.checheweike.com/erp/index.php?route=member/customer/gets&limit=100&order=DESC&page={no}&sort=date_added&vip_level_id=";

    private String MEMBERCARDLEVEL_URL = "https://www.checheweike.com/web/index.php?route=member/vip_level/gets";

    private String ITEM_URL = "https://www.checheweike.com/web/index.php?route=catalog/product/gets&limit=50&order=DESC&sort=p.date_added&page=";

    private String SERVICE_URL = "https://www.checheweike.com/web/index.php?route=catalog/service/gets&limit=50&order=DESC&sort=s.date_added&page=";

    private String SUPPLIER_URL = "https://www.checheweike.com/web/index.php?route=supplier/supplier/gets&limit=50&order=DESC&sort=date_added&page=";

    private String CARINFODETAIL_URL = "https://www.checheweike.com/crm/index.php?route=member/car/get&car_id=";

    private String CARINFO_URL = "https://www.checheweike.com/crm/index.php?route=member/car/gets&limit=50&order=DESC&sort=c.date_added&page=";

    private String beginDate = "2001-01-01";

    private int num = 500;

    private String type = "all";//all-不限，clear-已还清，unclear-未还清

    //连锁店情况下各店Id都不一样,通常总店或者第一个店为1
    private int substoreId = 4;

    private String BILL_URL = "https://www.checheweike.com/erp/index.php?route=order/order/gets&date_start=" +
            beginDate +
            "&date_end=" +
            DateUtil.formatCurrentDate() +
            "&get_stat=1&limit=200&order=DESC&sort=date_added&substore_id=" +
            substoreId +
            "&page=";

    private String BILLDETAIL_URL = "https://www.checheweike.com/erp/index.php?route=order/detail/get&id=";


    private String ORDER_URL = "https://www.checheweike.com/erp/index.php?route=order/receipt/gets" +
            "&date_checkout_end=" +
            DateUtil.formatCurrentDate() +
            "&date_checkout_start=" +
            beginDate +
            "&limit=" +
            num +
            "&order=DESC&sort=date_added&substore_id=" +
            substoreId +
            "&receipt_status=" +
            type +
            "&page=";

    private String STOCKDETAIL_URL = "https://www.checheweike.com/erp/index.php?route=stock/balance/get_warehouse_detail&substore_id=" +
            substoreId +
            "&product_id=";

    private String STOCK_URL = "https://www.checheweike.com/erp/index.php?route=stock/balance/gets&limit=50&order=DESC&query_type=product&sort=ps.date_added&substore_id=" +
            substoreId +
            "&zero_stock_show_enabled=1&page=";

    private String ORDERDETAIL_URL = "https://www.checheweike.com/erp/index.php?route=order/detail/get&no=";

    private String fieldName = "count";

    private Random random = new Random();

    private String companyName = "车车云";

    private String COOKIE = "_bl_uid=dLjF5nRI5pOkIz6aemCzwz39IvFt; _bl_uid=1gjvwtL15qFhmwzLL9XFthhkjIhq; PHPSESSID=1b26ek19mo6qkpl5vtbpq7j1i2; ccwk_backend_tracking=1b26ek19mo6qkpl5vtbpq7j1i2-10495; Hm_lvt_42a5df5a489c79568202aaf0b6c21801=1551944617,1552369607,1553148536,1554084881; Hm_lpvt_42a5df5a489c79568202aaf0b6c21801=1554097398; SERVERID=44fa044763f68345a9d119d26c10de1c|1554097412|1554084838";


    /**
     * 挂账单明细
     *
     * @throws IOException
     */
    @Test
    public void fetchOrderDetailData() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(ORDER_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 500);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWith(ORDER_URL + String.valueOf(i), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("orders").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("no").asText();
                    String companyName = element.get("substore").asText();

                    Bill bill = new Bill();
                    bill.setBillNo(billNo);
                    bill.setCompanyName(companyName);
                    bills.add(bill);
                }
            }
        }

        if (bills.size() > 0) {
            for (Bill bill : bills) {

                String billNo = bill.getBillNo();
                Response res = ConnectionUtil.doGetWith(ORDERDETAIL_URL + billNo, COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());
                JsonNode dataNode = result.get("data");

                String no = dataNode.get("no").asText();
                String companyName = dataNode.get("substore_name").asText();

                JsonNode serviceNode = dataNode.get("services");
                if (serviceNode.size() > 0) {
                    Iterator<JsonNode> it = serviceNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String serviceItemNames = e.get("name").asText();
                        String total = e.get("amount").asText();//总价
                        String num = e.get("quantity").asText();
                        String price = e.get("sale_price").asText();//单价
                        String firstCategoryName = e.get("business_type_name") == null ? "" : e.get("business_type_name").asText();
                        String serviceCode = e.get("service_no").asText();

                        BillDetail billDetail = new BillDetail();
                        billDetail.setBillNo(billNo);
                        billDetail.setItemName(serviceItemNames);
                        billDetail.setNum(num);
                        billDetail.setPrice(price);
                        billDetail.setCompanyName(companyName);
                        billDetail.setItemCode(serviceCode);
                        billDetail.setItemType("服务项");
                        billDetail.setFirstCategoryName(firstCategoryName);
                        billDetails.add(billDetail);


                        if (e.hasNonNull("products") == true) {
                            Iterator<JsonNode> items = e.get("products").iterator();

                            while (items.hasNext()) {
                                JsonNode node = items.next();

                                String totalPrice = node.get("amount").asText();//总价
                                String goodsNames = node.get("name").asText();
                                String unitPrice = node.get("sale_price").asText();//单价
                                String quantity = node.get("quantity").asText();//数量
                                String firstCategory = node.get("business_type_name") == null ? "" : node.get("business_type_name").asText();
                                String productCode = node.get("product_no").asText();

                                BillDetail detail = new BillDetail();
                                detail.setBillNo(billNo);
                                detail.setItemType("配件");
                                detail.setItemName(goodsNames);
                                billDetail.setItemCode(productCode);
                                detail.setNum(quantity);
                                detail.setPrice(unitPrice);
                                billDetail.setCompanyName(companyName);
                                detail.setFirstCategoryName(firstCategory);
                                billDetails.add(detail);
                            }
                        }
                    }
                }

                JsonNode productNode = dataNode.get("products");
                if (productNode.size() > 0) {
                    Iterator<JsonNode> products = productNode.elements();

                    while (products.hasNext()) {
                        JsonNode e = products.next();

                        String unitPrice = e.get("sale_price").asText();//单价
                        String goodsNames = e.get("name").asText();
                        String total = e.get("amount").asText();
                        String num = e.get("quantity").asText();
                        String firstCategoryName = e.get("business_type_name").asText();
                        String productCode = e.get("product_no").asText();

                        BillDetail billDetail = new BillDetail();
                        billDetail.setBillNo(billNo);
                        billDetail.setItemName(goodsNames);
                        billDetail.setNum(num);
                        billDetail.setItemCode(productCode);
                        billDetail.setPrice(unitPrice);
                        billDetail.setItemType("配件");
                        billDetail.setCompanyName(companyName);
                        billDetail.setFirstCategoryName(firstCategoryName);
                        billDetails.add(billDetail);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\车车云挂账单明细.xls";
        ExportUtil.exportBillDetailSomeFieldDataInLocal(billDetails, ExcelDatas.workbook, pathname);

    }


    /**
     * 挂账单 -进销存-工单-其他
     *
     * @throws IOException
     */
    @Test
    public void fetchOrderData() throws IOException {

        List<Bill> bills = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(ORDER_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 500);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWith(ORDER_URL + String.valueOf(i), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("orders").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("no").asText();
                    String carNumber = element.get("car_license").asText();
                    String totalAmount = element.get("total_amount").asText();
                    String remark = element.get("invoice_type_str").asText();
                    String dateAdded = element.get("date_added").asText();
                    String receptionistName = element.get("receiver_name").asText();
                    String companyName = element.get("substore").asText();
                    String name = element.get("name").asText();
                    String payer = element.get("payer").asText();

                    String debtAmountStr = element.get("arrears").asText();
                    String receivedAmountStr = element.get("money_verified").asText();

                    BigDecimal debtAmount = new BigDecimal(debtAmountStr);
                    BigDecimal receivedAmount = new BigDecimal(receivedAmountStr);
                    BigDecimal amount = debtAmount.subtract(receivedAmount);

                    Bill bill = new Bill();
                    bill.setName(name);
                    bill.setPayer(payer);
                    bill.setBillNo(billNo);
                    bill.setCompanyName(companyName);
                    bill.setReceptionistName(receptionistName);
                    bill.setCarNumber(carNumber);
                    bill.setTotalAmount(totalAmount);
                    bill.setRemark(remark);
                    bill.setDateAdded(dateAdded);
                    bill.setDateEnd(dateAdded);
                    bill.setDebtAmount(debtAmount.toString());
                    bill.setReceivedAmount(receivedAmount.toString());
                    bill.setAmount(amount.toString());
                    bills.add(bill);
                }
            }
        }

        String pathname = "C:\\exportExcel\\车车云挂账单.xls";
        ExportUtil.exportBillSomeFieldDataInLocal(bills, ExcelDatas.workbook, pathname);

    }


    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {

        List<Bill> bills = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(BILL_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 200);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWith(BILL_URL + String.valueOf(i), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("orders").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("no").asText();
                    String id = element.get("id").asText();
                    String companyName = element.get("substore").asText();
                    String carNumber = element.get("car_license").asText();
                    String receptionistName = element.get("receiver").asText();
                    String totalAmount = element.get("total_amount").asText();
                    String actualAmount = element.get("actual_amount").asText();

                    String type = element.get("invoice_type_str").asText();//工单类型
                    String remark = element.get("comment").asText();//备注
                    String state = element.get("text_status").asText();//结算状态;已结算，未结算

                    String dateEnd = element.get("bill_date").asText();
                    dateEnd = DateUtil.formatSQLDate(dateEnd);

                    Bill bill = new Bill();
                    bill.setId(id);
                    bill.setBillNo(billNo);
                    bill.setCompanyName(companyName);
                    bill.setCarNumber(carNumber);
                    bill.setReceptionistName(receptionistName);
                    bill.setTotalAmount(totalAmount);
                    bill.setRemark(remark + " " + state + " " + type);
                    bill.setDateEnd(dateEnd);
                    bill.setMileage("实收:" + actualAmount);

                    System.out.println("正在处理的单号为" + billNo);
                    Response res2 = ConnectionUtil.doGetWith(BILLDETAIL_URL + id, COOKIE);
                    JsonNode content = JsonObject.MAPPER.readTree(res2.returnContent().asString());

                    JsonNode data = content.get("data");

                    JsonNode service = data.get("services");
                    if (service.size() > 0) {
                        Iterator<JsonNode> services = service.elements();

                        while (services.hasNext()) {
                            JsonNode e = services.next();

                            String serviceItemNames = e.get("name").asText();
                            String total = e.get("amount").asText();//总价
                            String num = e.get("quantity").asText();
                            String price = e.get("sale_price").asText();//单价
                            String firstCategoryName = e.get("business_type_name") == null ? "" : e.get("business_type_name").asText();

                            BillDetail billDetail = new BillDetail();
                            billDetail.setBillNo(billNo);
                            billDetail.setItemName(serviceItemNames);
                            billDetail.setNum(num);
                            billDetail.setCompanyName(companyName);
                            billDetail.setPrice(price);
                            billDetail.setItemType("服务项");
                            billDetail.setFirstCategoryName(firstCategoryName);
                            billDetails.add(billDetail);

                            if (null != bill.getServiceItemNames() && !"".equals(serviceItemNames)) {
                                serviceItemNames = serviceItemNames + "*" + total;
                                String s = bill.getServiceItemNames() + "," + serviceItemNames;
                                bill.setServiceItemNames(s);
                            }

                            if (null == bill.getServiceItemNames()) {
                                bill.setServiceItemNames(serviceItemNames + "*" + total);
                            }


                            if (e.hasNonNull("products") == true) {
                                Iterator<JsonNode> items = e.get("products").iterator();

                                while (items.hasNext()) {
                                    JsonNode node = items.next();

                                    String totalPrice = node.get("amount").asText();//总价
                                    String goodsNames = node.get("name").asText();
                                    String unitPrice = node.get("sale_price").asText();//单价
                                    String quantity = node.get("quantity").asText();//数量
                                    String firstCategory = node.get("business_type_name") == null ? "" : node.get("business_type_name").asText();

                                    BillDetail detail = new BillDetail();
                                    detail.setCompanyName(companyName);
                                    detail.setBillNo(billNo);
                                    detail.setItemName(goodsNames);
                                    detail.setNum(quantity);
                                    detail.setPrice(unitPrice);
                                    detail.setItemType("配件");
                                    detail.setFirstCategoryName(firstCategory);
                                    billDetails.add(detail);

                                    if (null != bill.getGoodsNames() && !"".equals(goodsNames)) {
                                        goodsNames = goodsNames + "*" + totalPrice;
                                        String goods = bill.getGoodsNames() + "," + goodsNames;
                                        bill.setGoodsNames(goods);
                                    }

                                    if (null == bill.getGoodsNames()) {
                                        bill.setGoodsNames(goodsNames + "*" + totalPrice);
                                    }

                                }
                            }

                        }
                    }


                    JsonNode product = data.get("products");
                    if (product.size() > 0) {
                        Iterator<JsonNode> products = product.elements();

                        while (products.hasNext()) {
                            JsonNode e = products.next();

                            String goodsNames = e.get("name").asText();
                            String total = e.get("amount").asText();
                            String num = e.get("quantity").asText();
                            String unitPrice = e.get("sale_price").asText();//单价
                            String firstCategoryName = e.get("business_type_name") == null ? "" : e.get("business_type_name").asText();

                            BillDetail billDetail = new BillDetail();
                            billDetail.setCompanyName(companyName);
                            billDetail.setBillNo(billNo);
                            billDetail.setItemName(goodsNames);
                            billDetail.setNum(num);
                            billDetail.setPrice(unitPrice);
                            billDetail.setItemType("配件");
                            billDetail.setFirstCategoryName(firstCategoryName);
                            billDetails.add(billDetail);

                            if (null != bill.getGoodsNames() && !"".equals(goodsNames)) {
                                goodsNames = goodsNames + "*" + total;
                                String goods = bill.getGoodsNames() + "," + goodsNames;
                                bill.setGoodsNames(goods);
                            }

                            if (null == bill.getGoodsNames()) {
                                bill.setGoodsNames(goodsNames + "*" + total);
                            }
                        }
                    }

                    bills.add(bill);
                }
            }
        }

        System.out.println("结果为" + totalPage);

        String pathname = "C:\\exportExcel\\车车云消费记录.xls";
        String pathname2 = "C:\\exportExcel\\车车云消费记录-明细.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportBillDetailDataInLocal(billDetails, ExcelDatas.workbook, pathname2);
    }


    /**
     * 卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        List<MemberCard> cards = getMemberCards();

        if (cards.size() > 0) {
            for (MemberCard card : cards) {
                String id = card.getVipUserId();

                Response res = ConnectionUtil.doGetWith(MEMBERCARDITEM_URL + id, COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString());
                JsonNode combo = content.get("combo");

                if (combo.size() > 0) {

                    Iterator<JsonNode> it = content.get("combo").iterator();
                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String name = element.get("name").asText();
                        String num = element.get("quantity").asText();
                        String validTime = element.get("date_available").asText();
                        String price = element.get("price").asText();
                        String originalNum = element.get("original_quantity").asText();
                        String isValidForever = CommonUtil.getIsValidForever(validTime);

                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setCompanyName(card.getCompanyName());
                        memberCardItem.setCardCode(card.getCardCode());
                        memberCardItem.setItemName(name);
                        memberCardItem.setPrice(price);
                        memberCardItem.setNum(num);
                        memberCardItem.setOriginalNum(originalNum);
                        memberCardItem.setItemType("服务项");
                        memberCardItem.setIsValidForever(isValidForever);
                        memberCardItem.setMemberCardItemId(id);
                        memberCardItems.add(memberCardItem);
                    }
                }
            }
        }

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("结果为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\车车云卡内项目.xls";
        String pathname2 = "C:\\exportExcel\\车车云卡内项目详细.xls";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardItemSomeFieldDataInLocal(memberCardItems, ExcelDatas.workbook, pathname2);
    }


    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> cards = getMemberCards();

        //客户
        if (cards.size() > 0) {
            for (MemberCard card : cards) {
                String id = card.getVipUserId();

                Response res3 = ConnectionUtil.doGetWith(MEMBERCARDCLIENT_URL + id, COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(res3.returnContent().asString());

                JsonNode customer = content.get("customer");
                String name = customer.get("name").asText();
                String phone = customer.get("mobile").asText();
                String balance = customer.get("balance").asText();
                String ctId = customer.get("card_number").asText();

                card.setName(name);
                card.setPhone(phone);
                card.setBalance(balance);
                card.setCtId(ctId);
            }
        }

        //车辆
        if (cards.size() > 0) {
            for (MemberCard card : cards) {

                String id = card.getVipUserId();
                Response res4 = ConnectionUtil.doGetWith(MEMBERCARDCAR_URL + id, COOKIE);
                JsonNode carData = JsonObject.MAPPER.readTree(res4.returnContent().asString());

                Iterator<JsonNode> cars = carData.get("cars").iterator();
                while (cars.hasNext()) {
                    JsonNode e = cars.next();
                    String carNumber = e.get("license").asText();

                    card.setCarNumber(carNumber);
                }
            }
        }

        String pathname = "C:\\exportExcel\\车车云会员卡.xls";
        String pathname2 = "C:\\exportExcel\\车车云会员卡详细.xls";
        ExportUtil.exportMemberCardDataInLocal(cards, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardSomeFieldDataInLocal(cards, ExcelDatas.workbook, pathname2);
    }


    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        Map<String, Stock> stockMap = new HashMap<>();

        Response res = ConnectionUtil.doGetWith(STOCK_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWith(STOCK_URL + i + "", COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("products").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String goodsName = element.get("product_name").asText();
                    String productCode = element.get("product_no").asText();
                    String inventoryNum = element.get("left_quantity").asText();
                    String id = element.get("product_id").asText();
                    String price = element.get("unit_cost").asText();
                    String locationName = element.get("position").asText();

                    Stock stock = new Stock();
                    stock.setGoodsName(goodsName);
                    stock.setProductCode(productCode);
                    stock.setInventoryNum(inventoryNum);
                    stock.setPrice(price);
                    stock.setLocationName(locationName);
                    stockMap.put(id, stock);
                }
            }
        }

        if (stockMap.size() > 0) {
            for (String id : stockMap.keySet()) {
                res = ConnectionUtil.doGetWith(STOCKDETAIL_URL + id + "", COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                String storeRoomName = "";
                String substoreStr = result.get("substores").toString();
                if (substoreStr.contains("batches")) {
                    JsonNode substores = result.get("substores").get(0);
                    JsonNode batches = substores.get("batches").get(0);

                    storeRoomName = batches.get("warehouse_name").asText();
                }

                Stock stock = stockMap.get(id);
                stock.setCompanyName(companyName);
                stock.setStoreRoomName(storeRoomName);
                stocks.add(stock);
            }
        }

        System.out.print("结果为" + stocks.toString());

        String pathname = "C:\\exportExcel\\车车云库存.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
    }

    /**
     * 商品
     *
     * @throws IOException
     */
    @Test
    public void fetchItemDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response res = ConnectionUtil.doGetWith(ITEM_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWith(ITEM_URL + i + "", COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("products").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("name").asText();
                    String code = element.get("product_no").asText();
                    String firstCategoryName = element.get("pcategory_name").asText();//配件分类
                    String secondCategoryName = element.get("business_type_name").asText();//业务类别
                    String price = element.get("price").asText();
                    String unit = element.get("unit").asText();
                    String origin = element.get("manufacturer_name").asText();

                    //启用(上架)-1，禁用(下架)-0
                    String isActive = element.get("status").asText();
                    if ("1".equals(isActive))
                        isActive = "启用";

                    if ("0".equals(isActive))
                        isActive = "禁用";

                    Product product = new Product();
                    product.setProductName(productName);
                    product.setCode(code);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setSecondCategoryName(secondCategoryName);
                    product.setPrice(price);
                    product.setIsActive(isActive);
                    product.setCompanyName(companyName);
                    product.setUnit(unit);
                    product.setOrigin(origin);
                    product.setItemType("配件");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());

        String pathname = "C:\\exportExcel\\车车云商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response res = ConnectionUtil.doGetWith(SERVICE_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWith(SERVICE_URL + i + "", COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("services").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("name").asText();
                    String code = element.get("service_no").asText();
                    String firstCategoryName = element.get("scategory_name").asText();//项目分类
                    String secondCategoryName = element.get("business_type_name").asText();//业务类别
                    String price = element.get("price").asText();

                    //启用-1，禁用-0
                    String isActive = element.get("status").asText();
                    if ("1".equals(isActive))
                        isActive = "启用";

                    if ("0".equals(isActive))
                        isActive = "禁用";

                    Product product = new Product();
                    product.setProductName(productName);
                    product.setCode(code);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setSecondCategoryName(secondCategoryName);
                    product.setPrice(price);
                    product.setIsActive(isActive);
                    product.setCompanyName(companyName);
                    product.setItemType("服务项");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());

        String pathname = "C:\\exportExcel\\车车云服务.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response res = ConnectionUtil.doGetWith(SUPPLIER_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWith(SUPPLIER_URL + i + "", COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("suppliers").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("supplier_id").asText();
                    String name = element.get("name").asText();
                    String contactName = element.get("contact_person").asText();
                    String contactPhone = element.get("mobile").asText();
                    String address = element.get("address").asText();
                    String type = element.get("supplier_type_name").asText();
                    String remark = element.get("comment").asText();
                    String depositBank = element.get("bank").asText();
                    String accountNumber = element.get("bank_account").asText();
                    String fax = element.get("fax").asText();
                    String mainBusiness = element.get("main_business").asText();

                    Supplier supplier = new Supplier();
                    supplier.setName(name);
                    supplier.setCompanyName(companyName);
                    supplier.setContactName(contactName);
                    supplier.setContactPhone(contactPhone);
                    supplier.setAddress(address);
                    supplier.setDepositBank(depositBank);
                    supplier.setAccountNumber(accountNumber);
                    supplier.setFax(fax);
                    supplier.setRemark(CommonUtil.formatString(type) + " " + CommonUtil.formatString(remark));
                    suppliers.add(supplier);
                }
            }
        }

        System.out.println("结果为" + suppliers.toString());

        String pathname = "C:\\exportExcel\\车车云供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }


    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response res = ConnectionUtil.doGetWith(CARINFO_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWith(CARINFO_URL + i + "", COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("cars").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carId = element.get("car_id").asText();
                    String carNumber = element.get("license").asText();
                    String name = element.get("name").asText();
                    String phone = element.get("mobile").asText();
                    String companyName = element.get("substore").asText();
                    String carModel = element.get("model").asText();
                    String VINCode = element.get("vin").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarId(carId);
                    carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                    carInfo.setName(CommonUtil.formatString(name));
                    carInfo.setPhone(CommonUtil.formatString(phone));
                    carInfo.setCompanyName(CommonUtil.formatString(companyName));
                    carInfo.setCarModel(CommonUtil.formatString(carModel));
                    carInfo.setBrand(CommonUtil.formatString(carModel));
                    carInfo.setVINcode(CommonUtil.formatString(VINCode));
                    carInfos.add(carInfo);
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                res = ConnectionUtil.doGetWith(CARINFODETAIL_URL + carInfo.getCarId(), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                JsonNode data = result.get("car");

                String engineNumber = data.get("engine_number").asText();
                String vcInsuranceCompany = data.get("commmercial_insurance_company").asText();
                String tcInsuranceCompany = data.get("compulsory_insurance_company").asText();
                String vcInsuranceValidDate = data.get("date_commmercial_insurance_end").asText();
                String tcInsuranceValidDate = data.get("date_compulsory_insurance_end").asText();
                String registerDate = data.get("date_registered").asText();

                carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                carInfo.setVcInsuranceCompany(CommonUtil.formatString(vcInsuranceCompany));
                carInfo.setVcInsuranceValidDate(CommonUtil.formatString(vcInsuranceValidDate));
                carInfo.setTcInsuranceCompany(CommonUtil.formatString(tcInsuranceCompany));
                carInfo.setTcInsuranceValidDate(CommonUtil.formatString(tcInsuranceValidDate));
                carInfo.setRegisterDate(registerDate);
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("结果为" + carInfos.size());

        String pathname = "C:\\exportExcel\\车车云车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);

    }

    private List<MemberCard> getMemberCards() throws IOException {
        List<MemberCardSort> memberCardSorts = new ArrayList<>();
        List<MemberCard> memberCards = new ArrayList<>();

        //会员卡等级 设置-客户-会员等级
        Response res1 = ConnectionUtil.doGetWith(MEMBERCARDLEVEL_URL, COOKIE);
        JsonNode result = JsonObject.MAPPER.readTree(res1.returnContent().asString());

        Iterator<JsonNode> it = result.get("vip_levels").iterator();
        while (it.hasNext()) {
            JsonNode element = it.next();

            String id = element.get("vip_level_id").asText();
            String levelName = element.get("name").asText();
            MemberCardSort memberCardSort = new MemberCardSort();
            memberCardSort.setMemberCardSortId(id);
            memberCardSort.setName(levelName);
            memberCardSorts.add(memberCardSort);
        }

        if (memberCardSorts.size() > 0) {

            for (MemberCardSort memberCardSort : memberCardSorts) {
                String id = memberCardSort.getMemberCardSortId();
                String name = memberCardSort.getName();

                Response res2 = ConnectionUtil.doGetWith(StringUtils.replace(MEMBERCARD_URL, "{no}", "1") + id, COOKIE);
                int totalPage = WebClientUtil.getTotalPage(res2, JsonObject.MAPPER, fieldName, 100);

                if (totalPage > 0) {
                    for (int i = 1; i <= totalPage; i++) {
                        res2 = ConnectionUtil.doGetWith(StringUtils.replace(MEMBERCARD_URL, "{no}", String.valueOf(i)) + id, COOKIE);
                        JsonNode content = JsonObject.MAPPER.readTree(res2.returnContent().asString());

                        Iterator<JsonNode> customers = content.get("customers").iterator();
                        while (customers.hasNext()) {
                            JsonNode element = customers.next();

                            String vipUserId = element.get("vip_user_id").asText();
                            String dateCreated = element.get("date_added").asText();
                            String companyName = element.get("substore").asText();

                            String cardCode = element.get("card_number").asText();
                            if ("".equals(cardCode))
                                cardCode = String.valueOf(random.nextInt()).replace("-", "");

                            MemberCard memberCard = new MemberCard();
                            memberCard.setCardCode(vipUserId);
                            memberCard.setDateCreated(dateCreated.replace("-", "/"));
                            memberCard.setCompanyName(companyName);
                            memberCard.setMemberCardName(name);
                            memberCard.setVipUserId(vipUserId);
                            memberCard.setMemberCardId(vipUserId);
                            memberCards.add(memberCard);
                        }
                    }
                }
            }
        }
        return memberCards;
    }


}
