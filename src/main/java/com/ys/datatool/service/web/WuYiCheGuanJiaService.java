package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/6/30.
 * 51车管家系统
 */
@Service
public class WuYiCheGuanJiaService {

    private String ITEM_URL = "http://www.51chegj.com:8089/scm/store/purchase/findChooseProdSkus?_dc=1536042818090";

    private String SERVICE_URL = "http://www.51chegj.com:8089/scm/store/service/qryserviceById?_dc=1535975204356&storeId=100675&tenantId=10675&limit=15";

    private String MEMBERCARDITEM_URL = "http://www.51chegj.com:8089/scm/member/memberStatistics/qryPackageCardConsumptionBargainPage?store_id=100675&tenant_id=10675&keys=&card_id=&prod_sku_name=&limit=20";

    private String BILLDETAIL_URL = "http://www.51chegj.com:8089/scm/member/memberStatistics/qryConsumptionDetailedPage?store_id=100675&tenant_id=10675&keys=&limit=20";

    private String BILL_URL = "http://www.51chegj.com:8089/scm/member/memberStatistics/qryCardConsumptionPage?store_id=100675&tenant_id=10675&keys=&limit=20";

    private String MEMBERCARD_URL = "http://www.51chegj.com:8089/scm/member/memberStatistics/qryEffectiveMemberPage?tenantId=10675&keys=&card_id=&sale_person_id=&store_id=100675&limit=25";

    private String CARINFO_URL = "http://www.51chegj.com:8089/scm/payment/custInfo/findCustInfo?tenantId=10675&storeId=100675&keys=&vipFlag=&limit=23";

    private String SUPPLIER_URL = "http://www.51chegj.com:8089/scm/store/supplierMana/qrySupplierManaByPage?supplierName=&supplierCode=&provGeoName=&cityGeoName=&beloneId=100675&tenantId=10675&limit=15";

    private String STOCK_URL = "http://www.51chegj.com:8089/scm/stroeInventory/inventoryStatistics/qryInventoryPage?store_id=100675&tenantId=10675&keys=&prod_cata_id=&limit=20";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "totalRows";

    private String startDate = "2005-01-01";

    private String endDate = "2018-09-03";

    private String companyName = "51车管家";

    private String storeId = "100675";

    private String tenantId = "10675";

    private String limit = "13";

    private String cardNoStr = "&card_no=";

    private String memberIdStr = "&member_id=";

    private String COOKIE = "JSESSIONID=4C31AD9FF047B785815F4F66F7D448EB; 49BAC005-7D5B-4231-8CEA-16939BEACD67=gongwenxiang";


    /**
     * 商品
     *
     * @throws IOException
     */
    @Test
    public void fetchItemData() throws IOException {
        List<Product> products = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getItemParams("1", "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 13);

        if (totalPage > 0) {
            int start = 0;
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getItemParams(String.valueOf(i), String.valueOf(start)), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                start = start + Integer.parseInt(limit);

                System.out.println("start大小为" + start);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();


                    String productName=element.get("prodName").asText();
                    String brandName=element.get("brandName").asText();
                    String barCode=element.get("barCode").asText();
                    String price=element.get("salePrice").asText();
                    String cost=element.get("purchasePrice").asText();//采购价

                    Product product=new Product();
                    product.setCompanyName(companyName);
                    product.setBrandName(brandName);
                    product.setBarCode(barCode);
                    product.setPrice(price);
                    product.setProductName(productName);
                    products.add(product);
                }
            }
        }

        String pathname = "C:\\exportExcel\\51车管商品导出.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(getURL(SERVICE_URL, 1, 15), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {

                response = ConnectionUtil.doGetWithLeastParams(getURL(SERVICE_URL, i, 15), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("PROD_NAME").asText();
                    String firstCategoryName = element.get("CATA_NAME").asText();
                    String price = element.get("SALE_PRICE").asText();
                    String remark = element.get("DESCRIPTION").asText();

                    Product product = new Product();
                    product.setCompanyName(companyName);
                    product.setItemType("服务项");
                    product.setPrice(price);
                    product.setRemark(remark);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setProductName(productName);
                    products.add(product);
                }
            }
        }

        String pathname = "C:\\exportExcel\\51车管服务项目导出.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 单据明细
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDetailData() throws IOException {
        List<BillDetail> billDetails = new ArrayList<>();
        List<MemberCard> memberCards = getMemberCards();

        if (memberCards.size() > 0) {
            for (MemberCard memberCard : memberCards) {

                //encode
                Response res = ConnectionUtil.doGetWithLeastParams(getURLInDifferentConditions(BILLDETAIL_URL, 1, 20, startDate, endDate, memberIdStr, memberCard.getMemberCardId()).replace(" ", "%20"), COOKIE);
                int billDetailTotalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 20);

                if (billDetailTotalPage > 0) {
                    for (int i = 1; i <= billDetailTotalPage; i++) {
                        res = ConnectionUtil.doGetWithLeastParams(getURLInDifferentConditions(BILLDETAIL_URL, i, 20, startDate, endDate, memberIdStr, memberCard.getMemberCardId()).replace(" ", "%20"), COOKIE);
                        JsonNode result = MAPPER.readTree(res.returnContent().asString());

                        Iterator<JsonNode> it = result.get("result").iterator();
                        while (it.hasNext()) {
                            JsonNode element = it.next();

                            String billNo = element.get("OUTBOUND_ID").asText();
                            String itemName = element.get("PROD_SKU_NAME").asText();
                            String firstCategoryName = element.get("PROD_SKU_TYPE").asText();
                            String num = element.get("AMOUNT").asText();
                            String totalAmount = element.get("TOTAL_MONEY").asText();
                            String price = element.get("SALE_PRICE").asText();

                            BillDetail billDetail = new BillDetail();
                            billDetail.setBillNo(billNo);
                            billDetail.setCompanyName(companyName);
                            billDetail.setItemName(itemName);
                            billDetail.setTotalAmount(CommonUtil.priceFormat(totalAmount));
                            billDetail.setPrice(CommonUtil.priceFormat(price));
                            billDetail.setNum(num);
                            billDetail.setDiscountRate(element.get("DISCOUNTS").asText());
                            billDetail.setFirstCategoryName(firstCategoryName);
                            billDetail.setCarNumber(memberCard.getCarNumber());
                            billDetails.add(billDetail);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\51车管单据明细导出.xls";
        ExportUtil.exportBillDetailDataInLocal(billDetails, ExcelDatas.workbook, pathname);
    }


    /**
     * 单据
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void fetchBillData() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<MemberCard> memberCards = getMemberCards();

        if (memberCards.size() > 0) {
            for (MemberCard memberCard : memberCards) {

                //encode
                Response res = ConnectionUtil.doGetWithLeastParams(getURLInDifferentConditions(BILL_URL, 1, 20, startDate, endDate, cardNoStr, memberCard.getCardCode()).replace(" ", "%20"), COOKIE);
                int billTotalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 20);

                if (billTotalPage > 0) {
                    for (int i = 1; i <= billTotalPage; i++) {
                        res = ConnectionUtil.doGetWithLeastParams(getURLInDifferentConditions(BILL_URL, i, 20, startDate, endDate, cardNoStr, memberCard.getCardCode()).replace(" ", "%20"), COOKIE);
                        JsonNode result = MAPPER.readTree(res.returnContent().asString());

                        Iterator<JsonNode> it = result.get("result").iterator();
                        while (it.hasNext()) {
                            JsonNode element = it.next();

                            String billNo = element.get("OUTBOUND_ID").asText();
                            String dateAdded = element.get("CREATE_DATE").asText();
                            String actualAmount = element.get("ACCOUNTS_RECEIVABLE").asText();
                            String totalAmount = element.get("ACCOUNTS_PAID").asText();

                            Bill bill = new Bill();
                            bill.setCompanyName(companyName);
                            bill.setBillNo(billNo);
                            bill.setCarNumber(memberCard.getCarNumber());
                            bill.setPhone(memberCard.getPhone());
                            bill.setName(memberCard.getName());
                            bill.setDateAdded(DateUtil.formatSQLDateTime(dateAdded));
                            bill.setDateEnd(DateUtil.formatSQLDateTime(dateAdded));
                            bill.setDateExpect(DateUtil.formatSQLDateTime(dateAdded));
                            bill.setActualAmount(CommonUtil.priceFormat(actualAmount));
                            bill.setTotalAmount(CommonUtil.priceFormat(totalAmount));
                            bills.add(bill);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\51车管单据导出.xls";
        ExportUtil.exportBillDataInLocal(bills, ExcelDatas.workbook, pathname);

    }


    /**
     * 卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARDITEM_URL, 1, 20), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARDITEM_URL, i, 20), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String cardCode = element.get("CARD_NO").asText();
                    String itemName = element.get("PROD_SKU_NAME").asText();
                    String num = element.get("SURPLUS").asText();
                    String originalNum = element.get("TOTAL").asText();

                    String validTime = element.get("EXPIRE_DAY").asText();
                    validTime = DateUtil.formatDateTime(validTime);

                    String isValidForever = CommonUtil.getIsValidForever(validTime);

                    MemberCardItem memberCardItem = new MemberCardItem();
                    memberCardItem.setCompanyName(companyName);
                    memberCardItem.setCardCode(cardCode);
                    memberCardItem.setItemName(itemName);
                    memberCardItem.setNum(num);
                    memberCardItem.setOriginalNum(originalNum);
                    memberCardItem.setValidTime(validTime);
                    memberCardItem.setIsValidForever(isValidForever);
                    memberCardItems.add(memberCardItem);
                }
            }
        }

        System.out.println("结果为" + totalPage);

        String pathname = "C:\\exportExcel\\51车管卡内项目导出.xls";
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

        Response response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARD_URL, 1, 25), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 25);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARD_URL, i, 25), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String cardCode = element.get("CARD_NO").asText();
                    String memberCardName = element.get("CARD_NAME").asText();
                    String carNumber = element.get("CAR_PLATE_NUM").asText();
                    String balance = element.get("BALANCE").asText();
                    String name = element.get("CUSTOMER_NAME").asText();
                    String phone = element.get("PHONE_NUMBER").asText();

                    String dateCreated = element.get("CREATE_DATE").asText();
                    dateCreated = DateUtil.formatDateTime(dateCreated);

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCardCode(cardCode);
                    memberCard.setCompanyName(companyName);
                    memberCard.setCarNumber(carNumber);
                    memberCard.setBalance(CommonUtil.priceFormat(balance));
                    memberCard.setName(name);
                    memberCard.setPhone(phone);
                    memberCard.setDateCreated(dateCreated);
                    memberCard.setMemberCardName(memberCardName);
                    memberCards.add(memberCard);
                }
            }
        }


        String pathname = "C:\\exportExcel\\51车管会员卡导出.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }


    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(getURL(CARINFO_URL, 1, 23), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 23);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(getURL(CARINFO_URL, i, 23), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String name = element.get("CUSTOMER_NAME").asText();
                    String phone = element.get("phoneNumber").asText();
                    String carNumber = element.get("CUSTOMER_NAME").asText();
                    String brand = element.get("BRAND_NAME").asText();
                    String carModel = element.get("MODEL_NAME").asText();
                    String engineNumber = element.get("ENGINE_NUMBER").asText();
                    String vcInsuranceCompany = element.get("company").asText();

                    String vcInsuranceValidDate = element.get("INSURANCE_PERIOND").asText();
                    vcInsuranceValidDate = CommonUtil.formatString(vcInsuranceValidDate);
                    if (vcInsuranceValidDate != "")
                        vcInsuranceValidDate = DateUtil.formatSQLDateTime(vcInsuranceValidDate);


                    CarInfo carInfo = new CarInfo();
                    carInfo.setName(name);
                    carInfo.setPhone(phone);
                    carInfo.setCarNumber(carNumber);
                    carInfo.setBrand(brand);
                    carInfo.setCarModel(carModel);
                    carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                    carInfo.setEngineNumber(engineNumber);
                    carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                    carInfo.setCompanyName(companyName);
                    carInfos.add(carInfo);
                }
            }
        }

        String pathname = "C:\\exportExcel\\51车管车辆导出.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(getURL(SUPPLIER_URL, 1, 15), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(getURL(SUPPLIER_URL, i, 15), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String name = (element.get("supplierName")).asText();
                    String contactName = element.get("contactPerson").asText();
                    String contactPhone = element.get("contactPhone").asText();
                    String address = element.get("contactAddress").asText();
                    String code = element.get("supplierCode").asText();

                    Supplier supplier = new Supplier();
                    supplier.setName(name);
                    supplier.setContactName(contactName);
                    supplier.setContactPhone(contactPhone);
                    supplier.setCompanyName(companyName);
                    supplier.setCode(code);
                    supplier.setAddress(address);
                    suppliers.add(supplier);
                }
            }
        }

        System.out.println("结果为" + suppliers.toString());

        String pathname = "C:\\exportExcel\\51车管供应商导出.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }

    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockData() throws IOException {
        List<Stock> stocks = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(getURL(STOCK_URL, 1, 20), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(getURL(STOCK_URL, i, 20), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();


                    String goodsName = element.get("PROD_NAME").asText();
                    String productCode = element.get("BAR_CODE").asText();//条形码
                    String price = element.get("COST_PRICE").asText();
                    String inventoryNum = element.get("ACTUAL_INVENTORY").asText();
                    String storeRoomName = element.get("STORAGE_NAME").asText();

                    Stock stock = new Stock();
                    stock.setCompanyName(companyName);
                    stock.setGoodsName(goodsName);
                    stock.setPrice(CommonUtil.priceFormat(price));
                    stock.setProductCode(productCode);
                    stock.setInventoryNum(inventoryNum);
                    stock.setStoreRoomName(storeRoomName);
                    stocks.add(stock);
                }
            }
        }

        System.out.println("结果为" + stocks.size());
        System.out.println("结果为" + stocks.toString());

        String pathname = "C:\\exportExcel\\51车管家库存导出.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);

    }

    private List<MemberCard> getMemberCards() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Response response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARD_URL, 1, 25), COOKIE);
        int memberCardTotalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 25);

        if (memberCardTotalPage > 0) {
            for (int i = 1; i <= memberCardTotalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARD_URL, i, 25), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String cardCode = element.get("CARD_NO").asText();
                    String name = element.get("CUSTOMER_NAME").asText();
                    String phone = element.get("PHONE_NUMBER").asText();
                    String carNumber = element.get("CAR_PLATE_NUM").asText();
                    String memberCardId = element.get("MEMBER_ID").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardId(memberCardId);
                    memberCard.setCardCode(cardCode);
                    memberCard.setName(name);
                    memberCard.setPhone(phone);
                    memberCard.setCarNumber(carNumber);
                    memberCards.add(memberCard);
                }
            }
        }

        return memberCards;
    }


    /**
     * 商品传参
     *
     * @param num
     * @param start
     * @return
     */
    private List<BasicNameValuePair> getItemParams(String num, String start) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("storeId", storeId));
        params.add(new BasicNameValuePair("tenantId", tenantId));
        params.add(new BasicNameValuePair("page", num));
        params.add(new BasicNameValuePair("start", start));
        params.add(new BasicNameValuePair("limit", limit));
        params.add(new BasicNameValuePair("prodState", "PUBLISH,CANCEL"));
        return params;
    }

    private String getURLInDifferentConditions(String url, int pageNo, int row, String startDate, String endDate, String condition, String value) {
        String tempURL = getURL(url, pageNo, row);
        String realURL = tempURL + condition + value + "&startDate=" + startDate + "&endDate=" + endDate;

        return realURL;
    }

    private String getURL(String url, int pageNo, int row) {
        String realURL = "";
        int start = 0;

        for (int i = 1; i <= pageNo; i++) {
            realURL = url + "&page=" + i + "&start=" + start;
            start = start + row;
        }
        return realURL;
    }
}
