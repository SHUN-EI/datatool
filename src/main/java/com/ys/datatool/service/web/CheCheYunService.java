package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on  2018/8/2.
 * 车车云系统
 */
@Service
public class CheCheYunService {

    private String MEMBERCARDITEM_URL = "https://www.checheweike.com/crm/index.php?route=member/api/ext_info&exdata=combo&vip_user_id=";

    private String MEMBERCARDCAR_URL = "https://www.checheweike.com/crm/index.php?route=member/api/ext_info&exdata=car&vip_user_id=";

    private String MEMBERCARDCLIENT_URL = "https://www.checheweike.com/crm/index.php?route=member/customer/get&id=";

    private String MEMBERCARD_URL = "https://www.checheweike.com/crm/index.php?route=member/customer/gets&limit=100&order=DESC&page={no}&search_key=&sort=vu.date_added&vip_level_id=";

    private String MEMBERCARDLEVEL_URL = "https://www.checheweike.com/web/index.php?route=member/vip_level/gets";

    private String STOCKDETAIL_URL = "https://www.checheweike.com/erp/index.php?route=stock/balance/get_warehouse_detail&substore_id=1&product_id=";

    private String STOCK_URL = "https://www.checheweike.com/erp/index.php?route=stock/balance/gets&limit=50&order=DESC&query_type=product&sort=ps.date_added&substore_id=1&zero_stock_show_enabled=1&page=";

    private String ITEM_URL = "https://www.checheweike.com/web/index.php?route=catalog/product/gets&limit=50&order=DESC&sort=p.date_added&page=";

    private String SERVICE_URL = "https://www.checheweike.com/web/index.php?route=catalog/service/gets&limit=50&order=DESC&sort=s.date_added&page=";

    private String SUPPLIER_URL = "https://www.checheweike.com/web/index.php?route=supplier/supplier/gets&limit=50&order=DESC&sort=date_added&page=";

    private String CARINFODETAIL_URL = "https://www.checheweike.com/crm/index.php?route=member/car/get&car_id=";

    private String CARINFO_URL = "https://www.checheweike.com/crm/index.php?route=member/car/gets&limit=50&order=DESC&sort=c.date_added&page=";


    private String beginDate = "2001-01-01";

    //当前抓取日期
    private String endDate = "2018-10-16";

    private String BILL_URL = "https://www.checheweike.com/erp/index.php?route=order/order/gets&date_start=" +
            beginDate +
            "&date_end=" +
            endDate +
            "&get_stat=1&limit=200&order=DESC&sort=date_added&substore_id=1&page=";

    private String BILLDETAIL_URL = "https://www.checheweike.com/erp/index.php?route=order/detail/get&id=";

    private String fieldName = "count";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Random random = new Random();

    private String companyName = "车车云";

    private String COOKIE = "_bl_uid=8kjaql27y3pxaa5IhtOsggjv79bX; PHPSESSID=5chtgjvrhn1v7cajoqsen4am66; ccwk_backend_tracking=5chtgjvrhn1v7cajoqsen4am66-10638; Hm_lvt_42a5df5a489c79568202aaf0b6c21801=1539321273,1539573596,1539664069; Hm_lpvt_42a5df5a489c79568202aaf0b6c21801=1539939660; SERVERID=03485b53178f0de6cfb6b08218d57da6|1539941775|1539939551";


    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(BILL_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 200);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWithLeastParams(BILL_URL + String.valueOf(i), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

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
                    dateEnd = DateUtil.formatSQLDateTime(dateEnd);

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

                    Response res2 = ConnectionUtil.doGetWithLeastParams(BILLDETAIL_URL + id, COOKIE);
                    JsonNode content = MAPPER.readTree(res2.returnContent().asString());

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
                            String firstCategoryName = e.get("business_type_name").asText();

                            BillDetail billDetail = new BillDetail();
                            billDetail.setBillNo(billNo);
                            billDetail.setItemName(serviceItemNames);
                            billDetail.setNum(num);
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
                                    String firstCategory = node.get("business_type_name").asText();

                                    BillDetail detail = new BillDetail();
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
                            String firstCategoryName = e.get("business_type_name").asText();

                            BillDetail billDetail = new BillDetail();
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
        ExportUtil.exportConsumptionRecordDataInLocal(bills, ExcelDatas.workbook, pathname);
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
        Map<String, MemberCard> memberCardMap = getMemberCardMap();

        if (memberCardMap.size() > 0) {
            for (String id : memberCardMap.keySet()) {

                Response res = ConnectionUtil.doGetWithLeastParams(MEMBERCARDITEM_URL + id, COOKIE);
                JsonNode content = MAPPER.readTree(res.returnContent().asString());
                JsonNode combo = content.get("combo");

                if (!"".equals(combo.toString())) {

                    Iterator<JsonNode> it = content.get("combo").iterator();
                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String name = element.get("name").asText();
                        String num = element.get("quantity").asText();
                        String validTime = element.get("date_available").asText();
                        String price = element.get("price").asText();
                        String originalNum = element.get("original_quantity").asText();
                        String isValidForever = CommonUtil.getIsValidForever(validTime);


                        MemberCard m = memberCardMap.get(id);
                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setCompanyName(m.getCompanyName());
                        memberCardItem.setCardCode(m.getCardCode());
                        memberCardItem.setItemName(name);
                        memberCardItem.setPrice(price);
                        memberCardItem.setNum(num);
                        memberCardItem.setOriginalNum(originalNum);
                        memberCardItem.setItemType("服务项");
                        memberCardItem.setIsValidForever(isValidForever);
                        memberCardItems.add(memberCardItem);
                    }
                }
            }
        }

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("结果为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\车车云卡内项目.xls";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
    }


    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        Map<String, MemberCard> memberCardMap = getMemberCardMap();

        if (memberCardMap.size() > 0) {
            for (String id : memberCardMap.keySet()) {

                Response res3 = ConnectionUtil.doGetWithLeastParams(MEMBERCARDCLIENT_URL + id, COOKIE);
                JsonNode content = MAPPER.readTree(res3.returnContent().asString());

                JsonNode customer = content.get("customer");
                String name = customer.get("name").asText();
                String phone = customer.get("mobile").asText();

                MemberCard memberCard = memberCardMap.get(id);
                memberCard.setName(name);
                memberCard.setPhone(phone);
            }
        }

        if (memberCardMap.size() > 0) {
            for (String id : memberCardMap.keySet()) {
                Response res4 = ConnectionUtil.doGetWithLeastParams(MEMBERCARDCAR_URL + id, COOKIE);
                JsonNode carData = MAPPER.readTree(res4.returnContent().asString());

                Iterator<JsonNode> cars = carData.get("cars").iterator();
                while (cars.hasNext()) {
                    JsonNode e = cars.next();
                    String carNumber = e.get("license").asText();

                    MemberCard m = memberCardMap.get(id);
                    MemberCard memberCard = new MemberCard();
                    memberCard.setCarNumber(carNumber);
                    memberCard.setCompanyName(m.getCompanyName());
                    memberCard.setCardCode(m.getCardCode());
                    memberCard.setMemberCardName(m.getMemberCardName());
                    memberCard.setDateCreated(m.getDateCreated());
                    memberCard.setBalance(m.getBalance());
                    memberCard.setName(m.getName());
                    memberCard.setPhone(m.getPhone());
                    memberCards.add(memberCard);
                }
            }
        }

        System.out.print("结果为" + memberCards.toString());

        String pathname = "C:\\exportExcel\\车车云会员卡.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
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

        Response res = ConnectionUtil.doGetWithLeastParams(STOCK_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(STOCK_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

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
                res = ConnectionUtil.doGetWithLeastParams(STOCKDETAIL_URL + id + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

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

        Response res = ConnectionUtil.doGetWithLeastParams(ITEM_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(ITEM_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

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

        Response res = ConnectionUtil.doGetWithLeastParams(SERVICE_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(SERVICE_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

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

        Response res = ConnectionUtil.doGetWithLeastParams(SUPPLIER_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(SUPPLIER_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

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

        Response res = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

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
                res = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + carInfo.getCarId(), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

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

    private Map<String, MemberCard> getMemberCardMap() throws IOException {
        Map<String, String> memberCardLevelMap = new HashMap<>();
        Map<String, MemberCard> memberCardMap = new HashMap<>();

        Response res1 = ConnectionUtil.doGetWithLeastParams(MEMBERCARDLEVEL_URL, COOKIE);
        JsonNode result = MAPPER.readTree(res1.returnContent().asString());

        Iterator<JsonNode> it = result.get("vip_levels").iterator();
        while (it.hasNext()) {
            JsonNode element = it.next();

            String id = element.get("vip_level_id").asText();
            String levelName = element.get("name").asText();
            memberCardLevelMap.put(id, levelName);
        }

        if (memberCardLevelMap.size() > 0) {
            for (String id : memberCardLevelMap.keySet()) {
                Response res2 = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARD_URL, "{no}", "1") + id, COOKIE);
                int totalPage = WebClientUtil.getTotalPage(res2, MAPPER, fieldName, 100);

                String memberCardName = memberCardLevelMap.get(id);
                if (totalPage > 0) {
                    for (int i = 1; i <= totalPage; i++) {
                        res2 = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARD_URL, "{no}", String.valueOf(i)) + id, COOKIE);
                        JsonNode content = MAPPER.readTree(res2.returnContent().asString());

                        Iterator<JsonNode> customers = content.get("customers").iterator();
                        while (customers.hasNext()) {
                            JsonNode element = customers.next();

                            String vipUserId = element.get("vip_user_id").asText();
                            String dateCreated = element.get("date_added").asText();
                            String companyName = element.get("substore").asText();
                            String balance = element.get("balance").asText();

                            String cardCode = element.get("card_number").asText();
                            if ("".equals(cardCode))
                                cardCode = String.valueOf(random.nextInt()).replace("-", "");

                            MemberCard memberCard = new MemberCard();
                            memberCard.setCardCode(cardCode);
                            memberCard.setBalance(balance);
                            memberCard.setDateCreated(dateCreated.replace("-", "/"));
                            memberCard.setCompanyName(companyName);
                            memberCard.setMemberCardName(memberCardName);
                            memberCardMap.put(vipUserId, memberCard);
                        }
                    }
                }
            }
        }
        return memberCardMap;
    }


}
