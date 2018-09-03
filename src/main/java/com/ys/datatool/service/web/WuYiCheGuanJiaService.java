package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
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

    private static final String MEMBERCARDITEM_URL = "http://www.51chegj.com:8089/scm/member/memberStatistics/qryPackageCardConsumptionBargainPage?store_id=100675&tenant_id=10675&keys=&card_id=&prod_sku_name=&limit=20";

    private static final String BILLDETAIL_URL = "http://www.51chegj.com:8089/scm/member/memberStatistics/qryConsumptionDetailedPage?store_id=100675&tenant_id=10675&keys=&limit=20";

    private static final String BILL_URL = "http://www.51chegj.com:8089/scm/member/memberStatistics/qryCardConsumptionPage?store_id=100675&tenant_id=10675&keys=&limit=20";

    private static final String MEMBERCARD_URL = "http://www.51chegj.com:8089/scm/member/memberStatistics/qryEffectiveMemberPage?tenantId=10675&keys=&card_id=&sale_person_id=&store_id=100675&limit=25";

    private static final String CARINFO_URL = "http://www.51chegj.com:8089/scm/payment/custInfo/findCustInfo?tenantId=10675&storeId=100675&keys=&vipFlag=&limit=23";

    private static final String SUPPLIER_URL = "http://www.51chegj.com:8089/scm/store/supplierMana/qrySupplierManaByPage?supplierName=&supplierCode=&provGeoName=&cityGeoName=&beloneId=100675&tenantId=10675&limit=15";

    private static final String STOCK_URL = "http://www.51chegj.com:8089/scm/stroeInventory/inventoryStatistics/qryInventoryPage?store_id=100675&tenantId=10675&keys=&prod_cata_id=&limit=20";

    private static final String COOKIE = "JSESSIONID=CE9335CB84CDD3EFF1165FDE813BF017; 49BAC005-7D5B-4231-8CEA-16939BEACD67=gongwenxiang";

    private static final String ACCEPT = "*/*";

    private static final String CONNECTION = "keep-alive";

    private static final String HOST = "www.51chegj.com:8089";

    private static final String REFERER = "http://www.51chegj.com:8089/scm/main";

    private static final String UPGRADE_INSECURE_REQUESTS = "1";

    private static final String X_REQUESTED_WITH = "XMLHttpRequest";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "totalRows";

    private String startDate = "2005-01-01";

    private String endDate = "2017-08-08";

    private String companyName = "51车管家";

    private String cardNoStr = "&card_no=";

    private String memberIdStr = "&member_id=";


    /**
     * 单据明细
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDetailData() throws IOException {
        List<BillDetail> billDetails = new ArrayList<>();
        List<MemberCard> memberCards = new ArrayList<>();

        int memberCardTotalPage = WebClientUtil.getTotalPageWithDoGet(getURL(MEMBERCARD_URL, 1, 25), ACCEPT, COOKIE, CONNECTION, HOST, REFERER, X_REQUESTED_WITH, UPGRADE_INSECURE_REQUESTS, USER_AGENT, MAPPER, fieldName, 25);
        if (memberCardTotalPage > 0) {
            for (int i = 1; i <= memberCardTotalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARD_URL, i, 25), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String memberCardId = element.get("MEMBER_ID").asText();
                    String cardNo = element.get("CARD_NO").asText();
                    String carNumber = element.get("CAR_PLATE_NUM").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardId(memberCardId);
                    memberCard.setCardCode(cardNo);
                    memberCard.setCarNumber(carNumber);
                    memberCards.add(memberCard);
                }
            }
        }

        if (memberCards.size() > 0) {
            for (MemberCard memberCard : memberCards) {
                //encode
                int billDetailTotalPage = WebClientUtil.getTotalPageWithDoGet(getURLInDifferentConditions(BILLDETAIL_URL, 1, 20, startDate, endDate, memberIdStr, memberCard.getMemberCardId()).replace(" ", "%20"), ACCEPT, COOKIE, CONNECTION, HOST, REFERER, X_REQUESTED_WITH, UPGRADE_INSECURE_REQUESTS, USER_AGENT, MAPPER, fieldName, 20);
                if (billDetailTotalPage > 0) {
                    for (int i = 1; i <= billDetailTotalPage; i++) {
                        Response response = ConnectionUtil.doGetWithLeastParams(getURLInDifferentConditions(BILLDETAIL_URL, i, 20, startDate, endDate, memberIdStr, memberCard.getMemberCardId()).replace(" ", "%20"), COOKIE);
                        JsonNode result = MAPPER.readTree(response.returnContent().asString());

                        Iterator<JsonNode> it = result.get("result").iterator();
                        while (it.hasNext()) {
                            JsonNode element = it.next();

                            BillDetail billDetail = new BillDetail();
                            billDetail.setBillNo(element.get("OUTBOUND_ID").asText());
                            billDetail.setItemName(element.get("PROD_SKU_NAME").asText());
                            billDetail.setTotalAmount(CommonUtil.priceFormat(element.get("TOTAL_MONEY").asText()));
                            billDetail.setPrice(CommonUtil.priceFormat(element.get("SALE_PRICE").asText()));
                            billDetail.setNum(element.get("AMOUNT").asText());
                            billDetail.setDiscountRate(element.get("DISCOUNTS").asText());
                            billDetail.setFirstCategoryName(element.get("PROD_SKU_TYPE").asText());
                            billDetail.setCarNumber(memberCard.getCarNumber());
                            billDetails.add(billDetail);
                        }
                    }
                }
            }
        }
    }


    /**
     * 单据
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    public void fetchBillData() throws IOException, URISyntaxException {
        List<Bill> bills = new ArrayList<>();
        List<MemberCard> memberCards = new ArrayList<>();

        int memberCardTotalPage = WebClientUtil.getTotalPageWithDoGet(getURL(MEMBERCARD_URL, 1, 25), ACCEPT, COOKIE, CONNECTION, HOST, REFERER, X_REQUESTED_WITH, UPGRADE_INSECURE_REQUESTS, USER_AGENT, MAPPER, fieldName, 25);
        if (memberCardTotalPage > 0) {
            for (int i = 1; i <= memberCardTotalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARD_URL, i, 25), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String cardNo = element.get("CARD_NO").asText();
                    String name = element.get("CUSTOMER_NAME").asText();
                    String phone = element.get("PHONE_NUMBER").asText();
                    String carNumber = element.get("CAR_PLATE_NUM").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCardCode(cardNo);
                    memberCard.setName(name);
                    memberCard.setPhone(phone);
                    memberCard.setCarNumber(carNumber);
                    memberCards.add(memberCard);
                }
            }
        }

        if (memberCards.size() > 0) {
            for (MemberCard memberCard : memberCards) {
                //encode
                int billTotalPage = WebClientUtil.getTotalPageWithDoGet(getURLInDifferentConditions(BILL_URL, 1, 20, startDate, endDate, cardNoStr, memberCard.getCardCode()).replace(" ", "%20"), ACCEPT, COOKIE, CONNECTION, HOST, REFERER, X_REQUESTED_WITH, UPGRADE_INSECURE_REQUESTS, USER_AGENT, MAPPER, fieldName, 20);
                if (billTotalPage > 0) {
                    for (int i = 1; i <= billTotalPage; i++) {
                        Response response = ConnectionUtil.doGetWithLeastParams(getURLInDifferentConditions(BILL_URL, i, 20, startDate, endDate, cardNoStr, memberCard.getCardCode()).replace(" ", "%20"), COOKIE);
                        JsonNode result = MAPPER.readTree(response.returnContent().asString());

                        Iterator<JsonNode> it = result.get("result").iterator();
                        while (it.hasNext()) {
                            JsonNode element = it.next();

                            Bill bill = new Bill();
                            bill.setBillNo(element.get("OUTBOUND_ID").asText());
                            bill.setCarNumber(memberCard.getCarNumber());
                            bill.setPhone(memberCard.getPhone());
                            bill.setName(memberCard.getName());
                            bill.setDateAdded(element.get("CREATE_DATE").asText());
                            bill.setDateEnd(element.get("CREATE_DATE").asText());
                            bill.setDateExpect(element.get("CREATE_DATE").asText());
                            bill.setActualAmount(CommonUtil.priceFormat(element.get("ACCOUNTS_RECEIVABLE").asText()));
                            bill.setTotalAmount(CommonUtil.priceFormat(element.get("ACCOUNTS_PAID").asText()));
                            bill.setWaitInStore("否");
                            bills.add(bill);
                        }
                    }
                }
            }
        }
    }

    /**
     * 卡内项目
     *
     * @throws IOException
     */
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        int totalPage = WebClientUtil.getTotalPageWithDoGet(getURL(MEMBERCARDITEM_URL, 1, 20), ACCEPT, COOKIE, CONNECTION, HOST, REFERER, X_REQUESTED_WITH, UPGRADE_INSECURE_REQUESTS, USER_AGENT, MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARDITEM_URL, i, 20), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    MemberCardItem memberCardItem = new MemberCardItem();
                    memberCardItem.setCardCode(element.get("CARD_NO").asText());
                    memberCardItem.setItemName(element.get("PROD_SKU_NAME").asText());
                    memberCardItem.setOriginalNum(element.get("TOTAL").asText());
                    memberCardItem.setNum(element.get("SURPLUS").asText());
                    memberCardItem.setDiscount("0");
                    memberCardItems.add(memberCardItem);
                }
            }
        }
    }

    /**
     * 会员卡
     *
     * @throws IOException
     */
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        int totalPage = WebClientUtil.getTotalPageWithDoGet(getURL(MEMBERCARD_URL, 1, 25), ACCEPT, COOKIE, CONNECTION, HOST, REFERER, X_REQUESTED_WITH, UPGRADE_INSECURE_REQUESTS, USER_AGENT, MAPPER, fieldName, 25);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(getURL(MEMBERCARD_URL, i, 25), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("result").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCardCode(element.get("CARD_NO").asText());
                    memberCard.setMemberCardName(element.get("CARD_NAME").asText());
                    memberCard.setCarNumber(element.get("CAR_PLATE_NUM").asText());
                    memberCard.setDateCreated(element.get("CREATE_DATE").asText());
                    memberCard.setBalance(CommonUtil.priceFormat(element.get("BALANCE").asText()));
                    memberCard.setNum(element.get("residual_number").asText());
                    memberCard.setName(element.get("CUSTOMER_NAME").asText());
                    memberCard.setPhone(element.get("PHONE_NUMBER").asText());
                    memberCard.setCardType(element.get("CARD_NAME").asText());
                    memberCards.add(memberCard);
                }
            }
        }
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

        System.out.println("结果为" + totalPage);

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
