package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on  2018/8/11.
 * I店系统
 */
@Service
public class IDianService {

    private String MEMBERDETAIL_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=member_person_query_by_id";

    private String fromDate = "2003-01-01";

    private String CAR_MEM_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=kpi_customerDetailQuery_new" +
            "&detailtype=4&" +
            "startTime=" +
            fromDate +
            "&endTime=" +
            DateUtil.formatCurrentDate() +
            "&key=mem&sshopId=&option=&pageSize=50&page=";

    private String CAR_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=kpi_customerDetailQuery_new" +
            "&detailtype=4&" +
            "startTime=" +
            fromDate +
            "&endTime=" +
            DateUtil.formatCurrentDate() +
            "&key=cus&sshopId=&option=&pageSize=50&page=";

    private String MEMBERPACKAGE_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=kpi_memberVerifiAndSurplusQuery" +
            "&detailtype=4&" +
            "startTime=" +
            fromDate +
            "&endTime=" +
            DateUtil.formatCurrentDate() +
            "&key=totalCount" +
            "&sshopId=&timesCardId=&option=&pageSize=50&page=";

    private String MEMBER_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=kpi_memberVerifiAndSurplusQuery" +
            "&detailtype=4&" +
            "startTime=" +
            fromDate +
            "&endTime=" +
            DateUtil.formatCurrentDate() +
            "&key=totalBalance" +
            "&sshopId=&timesCardId=&option=&pageSize=50&page=";

    private String STOCK_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=timesinventory_list&rows=20&level1=1&level2=2&level3=0&level4=0&queryStr=&groupId=&source=1&type=0&sshopId=&page=";

    private String SUPPLIER_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=supplier_query_getsupplierlistbyoption&type=1&option=&pageSize=50&page=";

    private String CARINFO_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=member_customer_query&page=0&pageSize=50&option=";

    private String MEMBERCARDITEM_URL = "http://app.idianchina.com:8082/api/vip/member/get-member-detail";

    private String MEMBERCARD_URL = "http://app.idianchina.com:8082/api/vip/member/query";


    private String BILL_URL = "http://www.idsz.xin:7070/posapi_invoke" +
            "?apiname=saleorder_queryallfilter_new&" +
            "fromDate=" +
            fromDate +
            "&toDate=" +
            DateUtil.formatCurrentDate() +
            "&licensePlate=&userPhone=&billStatus=0&tpyes=0&orderTypes=0&rows=50&page=";

    private String CONSUMPTIONRECORD_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=sale_opensale_vieworderinfo_new";

    private String ACCEPT_ENCODING = "gzip, deflate, sdch";

    private String fieldName = "records";

    private String totalName = "total";

    private String companyName = "I店";

    private String meid = "688FFA7A-C3C9-48A5-AD1C-EE41EE2FB1CF";

    private String sign = "4D21974FAA5F9DF5AC74DB230D10F9CB";

    private String token = "089EAEF21EE02A0DB978A9D4C37BE513";

    private String userPhone = "18924800202";

    //手机APPCookie
    private String COOKIE_PHONE = "JSESSIONID=04A303A0842F861C1FF2DFD8A34E88D9";

    //客户端及网页Cookie
    private String COOKIE_WEB = "JSESSIONID=50BE7B87D7F6B6A1F6320AE6813DF93E";


    /**
     * 车辆信息
     * <p>
     * 打开路径：辅助功能-报表-客户统计
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response res = ConnectionUtil.doGetEncode(CAR_URL + 0, COOKIE_WEB, ACCEPT_ENCODING);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, totalName, 50);

        if (totalPage > 0) {
            for (int i = 0; i < totalPage; i++) {
                res = ConnectionUtil.doGetEncode(CAR_URL + i, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                Iterator<JsonNode> it = content.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode e = it.next();

                    String name = e.get("name").asText();
                    String mobile = e.get("mobilePhone").asText();
                    String carNumber = e.get("carNo").asText();
                    String carModel = e.get("carModel").asText();
                    String engineNumber = e.get("engineNumber").asText();
                    String vin = e.get("vin").asText();
                    String brand = e.get("carBrand").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCompanyName(companyName);
                    carInfo.setName(CommonUtil.formatString(name));
                    carInfo.setPhone(CommonUtil.formatString(mobile));
                    carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                    carInfo.setCarModel(CommonUtil.formatString(carModel));
                    carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                    carInfo.setVINcode(CommonUtil.formatString(vin));
                    carInfo.setBrand(CommonUtil.formatString(brand));
                    carInfos.add(carInfo);
                }
            }
        }


        Response response = ConnectionUtil.doGetEncode(CAR_MEM_URL + 0, COOKIE_WEB, ACCEPT_ENCODING);
        int total = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, totalName, 50);

        if (total > 0) {
            for (int i = 0; i < total; i++) {
                response = ConnectionUtil.doGetEncode(CAR_MEM_URL + i, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode result = JsonObject.MAPPER.readTree(response .returnContent().asString(WebConfig.CHARSET_UTF_8));

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode e = it.next();

                    String mobile = e.get("mobilePhone").asText();
                    String name = e.get("name").asText();
                    String carNumber = e.get("carNo").asText();
                    String carModel = e.get("carModel").asText();
                    String engineNumber = e.get("engineNumber").asText();
                    String vin = e.get("vin").asText();
                    String brand = e.get("carBrand").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCompanyName(companyName);
                    carInfo.setName(CommonUtil.formatString(name));
                    carInfo.setPhone(CommonUtil.formatString(mobile));
                    carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                    carInfo.setCarModel(CommonUtil.formatString(carModel));
                    carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                    carInfo.setVINcode(CommonUtil.formatString(vin));
                    carInfo.setBrand(CommonUtil.formatString(brand));
                    carInfos.add(carInfo);
                }
            }
        }

        String pathname = "C:\\exportExcel\\i店车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    /**
     * 会员卡及卡内项目-web端
     * <p>
     * 打开路径：辅助功能-报表-会员报表-会员储值详情
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataInWebStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        //会员储值卡
        Response res = ConnectionUtil.doGetEncode(MEMBER_URL + 0, COOKIE_WEB, ACCEPT_ENCODING);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, totalName, 50);

        if (totalPage > 0) {
            for (int i = 0; i < totalPage; i++) {
                res = ConnectionUtil.doGetEncode(MEMBER_URL + i, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                Iterator<JsonNode> it = content.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode e = it.next();

                    String cardCode = e.get("fid").asText();
                    String carNumber = e.get("carNo").asText();
                    String name = e.get("name").asText();
                    String memberCardName = e.get("cardType").asText();
                    String balance = e.get("leftAmount").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCompanyName(companyName);
                    memberCard.setCardCode(cardCode);
                    memberCard.setCarNumber(carNumber);
                    memberCard.setName(name);
                    memberCard.setMemberCardName(memberCardName);
                    memberCard.setBalance(balance);
                    memberCards.add(memberCard);
                }
            }
        }

        if (memberCards.size() > 0) {
            for (MemberCard memberCard : memberCards) {
                String param = "licensePlate=&id=" + memberCard.getCardCode();

                Response response = ConnectionUtil.doPostWithLeastParamJson(MEMBERDETAIL_URL, param, COOKIE_WEB);
                JsonNode content = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode userObject = content.get("userObject");
                if (userObject != null) {

                    String phone = userObject.get("mobilePhone").asText();
                    String dateCreated = userObject.get("openCardTime").asText();

                    memberCard.setPhone(phone);
                    memberCard.setDateCreated(dateCreated);

                    //车辆
                    JsonNode cars = userObject.get("cars");
                    if (cars.size() > 0) {
                        Iterator<JsonNode> it = cars.iterator();

                        while (it.hasNext()) {
                            JsonNode e = it.next();

                            String name = e.get("userName").asText();
                            String mobile = e.get("userPhone").asText();
                            String carNumber = e.get("licensePlate").asText();
                            String brand = e.get("carBrandName").asText();
                            String engineNumber = e.get("engineNo").asText();
                            String vin = e.get("carFreamNo").asText();

                            CarInfo carInfo = new CarInfo();
                            carInfo.setCompanyName(companyName);
                            carInfo.setName(CommonUtil.formatString(name));
                            carInfo.setPhone(CommonUtil.formatString(mobile));
                            carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                            carInfo.setBrand(CommonUtil.formatString(brand));
                            carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                            carInfo.setVINcode(CommonUtil.formatString(vin));
                            carInfos.add(carInfo);
                        }
                    }

                    //卡内项目
                    JsonNode cardItems = userObject.get("tiemsItems");
                    if (cardItems.size() > 0) {
                        Iterator<JsonNode> it = cardItems.iterator();

                        while (it.hasNext()) {
                            JsonNode e = it.next();

                            String itemName = e.get("goodsName").asText();
                            String code = e.get("goodsId").asText();
                            String num = e.get("qty").asText();
                            String originalNum = e.get("initQty").asText();

                            if ("null".equals(originalNum))
                                originalNum = num;

                            String validTime = e.get("endTime").asText();
                            String isValidForever = CommonUtil.getIsValidForever(validTime);

                            MemberCardItem memberCardItem = new MemberCardItem();
                            memberCardItem.setCompanyName(companyName);
                            memberCardItem.setCardCode(memberCard.getCardCode());
                            memberCardItem.setItemName(itemName);
                            memberCardItem.setNum(num);
                            memberCardItem.setCode(code);
                            memberCardItem.setOriginalNum(originalNum);
                            memberCardItem.setValidTime(validTime);
                            memberCardItem.setIsValidForever(isValidForever);
                            memberCardItems.add(memberCardItem);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\i店会员卡.xls";
        String pathname2 = "C:\\exportExcel\\i店卡内项目.xls";
        String pathname3 = "C:\\exportExcel\\i店会员卡-车辆.xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname2);
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname3);
    }

    /**
     * 套餐卡及卡内项目
     *
     * @throws IOException
     */
    public void fetchMemberCardPackageInWebStandard() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        Response res = ConnectionUtil.doGetEncode(MEMBERPACKAGE_URL + 0, COOKIE_WEB, ACCEPT_ENCODING);
        int totalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, totalName, 50);

        if (totalPage > 0) {
            for (int i = 0; i < totalPage; i++) {
                res = ConnectionUtil.doGetEncode(MEMBERPACKAGE_URL + i, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                Iterator<JsonNode> it = content.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode e = it.next();

                    String cardCode = e.get("fid").asText();
                    String itemName = e.get("goodsName").asText();
                    String validTime = e.get("endTime").asText();
                    String isValidForever = CommonUtil.getIsValidForever(validTime);

                    MemberCardItem memberCardItem = new MemberCardItem();
                    memberCardItem.setCardCode(cardCode);
                }
            }
        }
    }

    /**
     * 库存
     * 打开路径：辅助功能-库存调整
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Response res = ConnectionUtil.doGetEncode(STOCK_URL + 0, COOKIE_WEB, ACCEPT_ENCODING);
        int totalPage = getTotalPage(res);

        if (totalPage > 0) {
            for (int i = 0; i < totalPage; i++) {
                res = ConnectionUtil.doGetEncode(STOCK_URL + i, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                Iterator<JsonNode> it = content.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode e = it.next();

                    String goodsName = e.get("goodsName").asText();
                    String price = e.get("lotPurPrice").asText();
                    String salePrice = e.get("salePrice").asText();
                    String inventoryNum = e.get("qty").asText();
                    String storeRoomName = e.get("depotName").asText();
                    String productCode = e.get("goodsNumber").asText();
                    String firstCategoryName = e.get("goodsGroupName").asText();
                    String barcode = e.get("barcode").asText();
                    String unit = e.get("unit").asText();
                    String manufactory = e.get("supplierName").asText();

                    Stock stock = new Stock();
                    stock.setCompanyName(companyName);
                    stock.setStoreRoomName(storeRoomName);
                    stock.setGoodsName(goodsName);
                    stock.setInventoryNum(inventoryNum);
                    stock.setPrice(price);
                    stock.setProductCode(productCode);
                    stocks.add(stock);

                    Product product = new Product();
                    product.setCompanyName(companyName);
                    product.setProductName(goodsName);
                    product.setItemType("商品");
                    product.setBarCode(CommonUtil.formatString(barcode));
                    product.setPrice(salePrice);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setCode(productCode);
                    product.setUnit(CommonUtil.formatString(unit));
                    product.setManufactory(manufactory);
                    products.add(product);
                }
            }
        }

        String pathname = "C:\\exportExcel\\i店库存.xls";
        String pathname2 = "C:\\exportExcel\\i店库存商品.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname2);

    }


    /**
     * 历史消费记录和消费记录相关车辆
     * <p>
     * 打开路径:开单销售-订单查询-详情
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        Response res = ConnectionUtil.doGetEncode(BILL_URL + 1, COOKIE_WEB, ACCEPT_ENCODING);
        int totalPage = getTotalPage(res);

        /**
         * 单据状态tpyes:全部状态-0,已开单-1,已结算-2,已提车-3,已取消-4
         * billStatus  已开单-10,已结算-20,已提车-30,已取消-40
         */
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetEncode(BILL_URL + i, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                Iterator<JsonNode> it = content.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String company = element.get("companyName").asText();
                    String billNo = element.get("fid").asText();
                    String carNumber = element.get("licensePlate").asText();
                    String name = element.get("userName").asText();
                    String remark = element.get("remark").asText();
                    String dateAdded = element.get("openTime").asText();
                    String dateEnd = element.get("closeTime").asText();


                    String totalAmount = element.get("balAmount").asText();
                    if ("0E-10".equals(totalAmount))
                        totalAmount = "0";

                    String billStatus = element.get("billStatus").asText();
                    switch (billStatus) {
                        case "10":
                            billStatus = "已开单";
                            break;
                        case "20":
                            billStatus = "已结算";
                            break;
                        case "30":
                            billStatus = "已提车";
                            break;
                        case "40":
                            billStatus = "已取消";
                            break;
                    }


                    Bill bill = new Bill();
                    bill.setBillNo(billNo);
                    bill.setCompanyName(companyName);
                    bill.setCarNumber(new String(carNumber.getBytes("UTF-8"), "UTF-8"));
                    bill.setName(name);
                    bill.setTotalAmount(totalAmount);
                    bill.setRemark(remark + " " + billStatus);
                    bill.setDateEnd(dateEnd);
                    bills.add(bill);
                }
            }
        }

        if (bills.size() > 0) {
            for (Bill bill : bills) {
                String billNo = bill.getBillNo();
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("fid", billNo));

                Response response = ConnectionUtil.doPostEncode(CONSUMPTIONRECORD_URL, params, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode content = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                String receptionistName = content.get("receptionName").asText();
                String billNumber = content.get("saleNumber").asText();
                String name = content.get("userName").asText();
                String phone = content.get("userPhone").asText();
                String carNumber = content.get("licensePlate").asText();
                String vin = content.get("vin").asText();
                String engineNumber = content.get("engineNumber").asText();
                String brand = content.get("carFullName").asText();
                String mileage = content.get("enterKilometre").asText();


                //工时项目
                JsonNode serviceNode = content.get("entry");
                if (serviceNode.size() > 0) {
                    Iterator<JsonNode> services = serviceNode.iterator();
                    while (services.hasNext()) {
                        JsonNode element = services.next();
                        String serviceItemName = element.get("goodsName").asText();

                        if (null != bill.getServiceItemNames()) {
                            String service = bill.getServiceItemNames() + "," + serviceItemName;
                            bill.setServiceItemNames(service);
                        }

                        if (null == bill.getServiceItemNames()) {
                            bill.setServiceItemNames(serviceItemName);
                        }
                    }
                }

                String dateEndStr = content.get("openTime").asText();
                if ("null".equals(dateEndStr))
                    dateEndStr = "1900/01/01";

                String dateEnd = DateUtil.formatSQLDate(dateEndStr);

                bill.setReceptionistName(CommonUtil.formatString(receptionistName));
                bill.setBillNo(billNumber);
                bill.setCarNumber(carNumber);
                bill.setMileage(mileage);
                bill.setDateEnd(dateEnd);

                CarInfo carInfo = new CarInfo();
                carInfo.setCompanyName(companyName);
                carInfo.setName(name);
                carInfo.setPhone(phone);
                carInfo.setCarNumber(carNumber);
                carInfo.setVINcode(CommonUtil.formatString(vin));
                carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                carInfos.add(carInfo);
            }
        }

        System.out.println("结果为" + bills.toString());
        System.out.println("结果为" + bills.size());

        String pathname = "C:\\exportExcel\\i店消费记录.xls";
        String pathname2 = "C:\\exportExcel\\i店消费记录-车辆.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname2);

    }


    /**
     * 单据
     * 打开路径:开单销售-订单查询
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response res = ConnectionUtil.doGetEncode(BILL_URL + 1, COOKIE_WEB, ACCEPT_ENCODING);
        int totalPage = getTotalPage(res);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetEncode(BILL_URL + i, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                Iterator<JsonNode> it = content.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String company = element.get("companyName").asText();
                    String billNo = element.get("fid").asText();
                    String carNumber = element.get("licensePlate").asText();
                    String name = element.get("userName").asText();
                    String totalAmount = element.get("totalProfit").asText();
                    String remark = element.get("remark").asText();
                    String dateAdded = element.get("openTime").asText();
                    String dateEnd = element.get("closeTime").asText();

                    Bill bill = new Bill();
                    bill.setCompanyName(companyName);
                    bill.setBillNo(billNo);
                    bill.setCarNumber(new String(carNumber.getBytes("UTF-8"), "UTF-8"));
                    bill.setName(name);
                    bill.setTotalAmount(totalAmount);
                    bill.setActualAmount(totalAmount);
                    bill.setPayType("现金");
                    bill.setRemark(remark);
                    bill.setDateAdded(dateAdded);
                    bill.setDateEnd(dateEnd);
                    bill.setDateExpect(dateEnd);
                    bills.add(bill);
                }
            }
        }

        String pathname = "C:\\exportExcel\\i店单据.xlsx";
        ExportUtil.exportBillDataInLocal(bills, ExcelDatas.workbook, pathname);

    }

    /**
     * 供应商
     * 打开路径:辅助功能-供应商维护(新)
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response res = ConnectionUtil.doGetEncode(SUPPLIER_URL + 0, COOKIE_WEB, ACCEPT_ENCODING);
        int totalPage = getTotalPage(res);

        if (totalPage > 0) {
            for (int i = 0; i < totalPage; i++) {
                res = ConnectionUtil.doGetEncode(SUPPLIER_URL + i, COOKIE_WEB, ACCEPT_ENCODING);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode rows = result.get("rows");
                if (rows.size() > 0) {
                    Iterator<JsonNode> it = rows.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String name = e.get("name").asText();
                        String contactName = e.get("linkMan").asText();
                        String contactPhone = e.get("linkPhone").asText();
                        String remark = e.get("address").asText();
                        String code = e.get("fid").asText();

                        Supplier supplier = new Supplier();
                        supplier.setName(name);
                        supplier.setContactName(contactName);
                        supplier.setContactPhone(contactPhone);
                        supplier.setRemark(remark);
                        supplier.setCompanyName(companyName);
                        supplier.setCode(code);
                        suppliers.add(supplier);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\i店供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }


    /**
     * 卡内项目-APP端
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        int totalPage = getMemberCardTotalPage();
        if (totalPage > 0) {
            for (int i = 0; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParamJsonInPhone(MEMBERCARD_URL, getMemberCardParams(String.valueOf(i)), COOKIE_PHONE);

                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));
                JsonNode userObject = result.get("userObject");
                JsonNode memberList = userObject.get("memberList");

                if (memberList.size() > 0) {
                    Iterator<JsonNode> it = memberList.iterator();
                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String cardCode = element.get("memberId").asText();
                        ids.add(cardCode);
                    }
                }
            }

            if (ids.size() > 0) {
                for (String id : ids) {
                    Response res = ConnectionUtil.doPostWithLeastParamJsonInPhone(MEMBERCARDITEM_URL, getMemberCardItemParams(id), COOKIE_PHONE);

                    JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));
                    JsonNode userObject = result.get("userObject");

                    Iterator<JsonNode> it = userObject.get("timesDetailList").iterator();
                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String validTime = element.get("validityTime").asText();
                        validTime = DateUtil.formatSQLDateTime(validTime);
                        String isValidForever = CommonUtil.getIsValidForever(validTime);

                        JsonNode goodsList = element.get("goodsList");
                        if (goodsList.size() > 0) {
                            Iterator<JsonNode> items = goodsList.iterator();
                            while (items.hasNext()) {
                                JsonNode e = items.next();

                                String code = e.get("fid").asText();
                                String num = e.get("leftCount").asText();
                                String itemName = e.get("projectName").asText();

                                MemberCardItem memberCardItem = new MemberCardItem();
                                memberCardItem.setCardCode(id);
                                memberCardItem.setItemName(itemName);
                                memberCardItem.setNum(num);
                                memberCardItem.setOriginalNum(num);
                                memberCardItem.setCompanyName(companyName);
                                memberCardItem.setCode(code);
                                memberCardItem.setValidTime(validTime);
                                memberCardItem.setIsValidForever(isValidForever);
                                memberCardItems.add(memberCardItem);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("结果为" + ids.size());

        String pathname = "C:\\exportExcel\\i店卡内项目.xlsx";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);

    }

    /**
     * 会员卡-APP端
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        int totalPage = getMemberCardTotalPage();

        //String params = "MEID=1F3F7042-675B-4BAD-BE11-448A267326F0&deviceType=2&format=json&keyword=&memberLevelId=&sign=1B0BC2BC981BF781DDB9D55FAA886D3E&token=A19873FDF327F6D7F14A8110513DB9F7&user_phone=18934388886&versionCode=507&versionName=5.0.7&currentPageIndex=";
        if (totalPage > 0) {
            for (int i = 0; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParamJsonInPhone(MEMBERCARD_URL, getMemberCardParams(String.valueOf(i)), COOKIE_PHONE);

                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));
                JsonNode userObject = result.get("userObject");

                Iterator<JsonNode> it = userObject.get("memberList").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String cardCode = element.get("memberId").asText();
                    String memberCardName = element.get("memberLevelName").asText();
                    String carNumber = element.get("licensePlate").asText();
                    String balance = element.get("amount").asText();
                    String dateCreated = element.get("openCardTime").asText();
                    dateCreated = DateUtil.formatSQLDateTime(dateCreated);

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCardCode(cardCode);
                    memberCard.setMemberCardName(memberCardName == "" ? "普通会员卡" : memberCardName);
                    memberCard.setCarNumber(carNumber);
                    memberCard.setCompanyName(companyName);
                    memberCard.setBalance(balance);
                    memberCard.setDateCreated(dateCreated);
                    memberCards.add(memberCard);
                }
            }
        }

        String pathname = "C:\\exportExcel\\i店会员卡.xlsx";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    private int getMemberCardTotalPage() throws IOException {
        Response response = ConnectionUtil.doPostWithLeastParamJsonInPhone(MEMBERCARD_URL, getMemberCardParams("0"), COOKIE_PHONE);
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));
        JsonNode userObject = result.get("userObject");
        JsonNode totalCount = userObject.get("totalCount");

        int totalPage = WebClientUtil.getTotalPage(totalCount, 10);

        return totalPage != 0 ? totalPage : 0;
    }

    private int getTotalPage(Response res) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));
        String totalStr = result.get(totalName).asText();
        int total = Integer.parseInt(totalStr);

        return total;
    }


    private List<BasicNameValuePair> getMemberCardItemParams(String id) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("MEID", meid));
        params.add(new BasicNameValuePair("deviceType", "2"));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("sign", sign));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("user_phone", userPhone));
        params.add(new BasicNameValuePair("versionCode", "507"));
        params.add(new BasicNameValuePair("versionName", "5.0.7"));
        params.add(new BasicNameValuePair("memberId", id));
        return params;
    }

    private List<BasicNameValuePair> getMemberCardParams(String index) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("MEID", meid));
        params.add(new BasicNameValuePair("deviceType", "2"));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("keyword", ""));
        params.add(new BasicNameValuePair("memberLevelId", ""));
        params.add(new BasicNameValuePair("sign", sign));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("user_phone", userPhone));
        params.add(new BasicNameValuePair("versionCode", "507"));
        params.add(new BasicNameValuePair("versionName", "5.0.7"));
        params.add(new BasicNameValuePair("currentPageIndex", index));
        return params;
    }

}
