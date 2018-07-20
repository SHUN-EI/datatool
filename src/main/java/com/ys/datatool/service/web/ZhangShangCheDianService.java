package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018-05-30.
 * 掌上车店系统
 */
@Service
public class ZhangShangCheDianService {

    private String STOCK_URL = "http://czbbb.cn/mnt/czbbb/stock/czbbbApi.action";

    private String MEMBERCARDDETAIL_URL = "http://czbbb.cn/mnt/czbbb/card/viewUserCard.action?userCardInfoId=";

    private String MEMBERCARD_URL = "http://czbbb.cn/mnt/czbbb/card/findUserCardInfos.action";

    private String CARINFO_URL = "http://czbbb.cn/mnt/czbbb/storeMember/czbbbApi.action";

    private String SUPPLIER_URL = "http://czbbb.cn/mnt/czbbb/supplierMgmt/czbbbApi.action";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Workbook workbook;

    //会员详情页面方法传参
    private String memberCardMethod = "6015";

    //供应商页面方法传参
    private String supplierMethod = "60701";

    //供应商编辑页面方法传参
    private String supplierDetailMethod = "60702";

    //车辆信息页面方法传参
    private String carInfoMethod = "1912";

    //车辆详情页面方法传参
    private String carInfoDetailMethod = "1242";

    //库存页面方法传参
    private String stockMethod = "60002";

    private String companyName = "掌上车店";

    private String COOKIE = "JSESSIONID=F84CD0E5D29010B57F67480DA23B1D3C; Hm_lvt_678c2a986264dd9650b6a59042718858=1531206994; Authorization=eyJhbGciOiJIUzUxMiJ9.eyJpZCI6ImNjMzYxYzE3LTk0ZGEtNDg1Yi05NDBlLWQxNDkwYzhmMzE1NiIsImV4cCI6MTUzMjA3NjEwNywibmJmIjoxNTMxOTg5NzA3LCJzdG9yZUlkIjoiNzA0YWU4NTMtYTg4Zi00MDE0LTkzZmEtNWJiYjc1NThmYmU3IiwidXNlclR5cGUiOiIwIn0.n6L4dJ6T1jZGTGEm33r5yVa0mibc9F2ReMih8m-yjNF_5_brDUoCSmbIdqP1c_p8JfVG3-Tb7rymnsKNlBeamg; Hm_lpvt_678c2a986264dd9650b6a59042718858=1531991081; SERVERID=fcc0e5fe0ca1ba074f3fd4818c894192|1531993483|1531978888";

    @Test
    public void test() throws IOException {
        String value = "{" + "\"pageSize\":" + "15" + "," + "\"typesName\":" + "\"" + "NGK" + "\"" + "," +
                "\"pageNo\":" + "1" + "}";

        Response res = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams(stockMethod, value), COOKIE);
        int totalPage = getTotalPage(res, 15);
        String a = "";

        System.out.println("结果为" + totalPage);
    }

    /**
     * 库存-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams(stockMethod, getPageValue("0", "15")), COOKIE);
        int totalPage = getTotalPage(res, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams(stockMethod, getPageValue(String.valueOf(i), "15")), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").elements();
                while (it.hasNext()) {

                    JsonNode element = it.next();
                    String goodsName = element.get("productName") != null ? element.get("productName").asText() : "";
                    String productCode = element.get("productCode") != null ? element.get("productCode").asText() : "";
                    String companyName = element.get("storeName") != null ? element.get("storeName").asText() : "";
                    String unit = element.get("unitName") != null ? element.get("unitName").asText() : "";
                    String remark = element.get("memoInfo") != null ? element.get("memoInfo").asText() : "";
                    String inventoryNum = element.get("totalQuantityTemp") != null ? element.get("totalQuantityTemp").asText() : "";
                    String storeRoomName = element.get("groups") != null ? element.get("groups").get(0).get("positionName").asText() : "";
                    //soldPrice
                    String price = element.get("minBuyPrice") != null ? element.get("minBuyPrice").asText() : "";//最低采购价
                    String salePrice = element.get("soldPrice") != null ? element.get("soldPrice").asText() : "";//零售价
                    String firstCategoryName = element.get("typesName") != null ? element.get("typesName").asText() : "";


                    if ("".equals(storeRoomName))
                        storeRoomName = "仓库";

                    Stock stock = new Stock();
                    stock.setCompanyName(companyName);
                    stock.setStoreRoomName(storeRoomName);
                    stock.setGoodsName(goodsName);
                    stock.setInventoryNum(inventoryNum);
                    stock.setProductCode(productCode);
                    stock.setPrice(price);
                    stocks.add(stock);

                    Product product = new Product();
                    product.setProductName(goodsName);
                    product.setProductCode(productCode);
                    product.setCompanyName(companyName);
                    product.setUnit(unit);
                    product.setPrice(salePrice);
                    product.setRemark(remark);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setItemType("配件");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + stocks.toString());
        System.out.println("结果为" + stocks.size());

        String pathname = "C:\\exportExcel\\掌上车店库存.xls";
        String pathname2 = "C:\\exportExcel\\掌上车店库存商品.xls";
        ExportUtil.exportStockDataInLocal(stocks, workbook, pathname);
        ExportUtil.exportProductDataInLocal(products, workbook, pathname2);
    }

    /**
     * 卡内项目-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Set<String> cardIds = getMemberCardId();
        if (cardIds.size() > 0) {
            for (String cardId : cardIds) {
                Response res = ConnectionUtil.doGetWithLeastParams(MEMBERCARDDETAIL_URL + cardId, COOKIE);
                String html = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                String itemIsExistRegEX = "#listCon > tr > td > div";
                String itemIsExist = doc.select(itemIsExistRegEX).text();
                if ("无项目".equals(itemIsExist))
                    continue;

                String cardCodeRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(3) > div:nth-child(2)";
                String validTimeRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(7) > div:nth-child(4)";

                String cardCode = doc.select(cardCodeRegEx).text();
                String validTime = doc.select(validTimeRegEx).text();
                if ("-".equals(validTime))
                    validTime = "";

                String isValidForever = CommonUtil.getIsValidForever(validTime);

                if (!"-".equals(validTime) && StringUtils.isNotBlank(validTime))
                    validTime = DateUtil.formatDate2DateTime(validTime);


                String trRegEx = "#listCon > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {
                        String firstCategoryNameRegEx = "#listCon > tr:nth-child({no}) > td:nth-child(1)";
                        String secondCategoryNameRegEx = "#listCon > tr:nth-child({no}) > td:nth-child(2)";
                        String itemNameRegEx = "#listCon > tr:nth-child({no}) > td:nth-child(3)";
                        String priceRegEx = "#listCon > tr:nth-child({no}) > td:nth-child(4)";
                        String originalNumRegEx = "#listCon > tr:nth-child({no}) > td:nth-child(5)";
                        String numRegEx = "#listCon > tr:nth-child(2) > td:nth-child(7) > b";

                        String firstCategoryName = doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(i))).text();
                        String secondCategoryName = doc.select(StringUtils.replace(secondCategoryNameRegEx, "{no}", String.valueOf(i))).text();
                        String itemName = doc.select(StringUtils.replace(itemNameRegEx, "{no}", String.valueOf(i))).text();
                        String price = doc.select(StringUtils.replace(priceRegEx, "{no}", String.valueOf(i))).text();
                        String originalNum = doc.select(StringUtils.replace(originalNumRegEx, "{no}", String.valueOf(i))).text();
                        String num = doc.select(StringUtils.replace(numRegEx, "{no}", String.valueOf(i))).text();

                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setCompanyName(companyName);
                        memberCardItem.setCardCode(cardCode);
                        memberCardItem.setItemName(itemName);
                        memberCardItem.setPrice(price.replace("￥", ""));
                        memberCardItem.setDiscount("0");
                        memberCardItem.setNum(num);
                        memberCardItem.setOriginalNum(originalNum);
                        memberCardItem.setFirstCategoryName(firstCategoryName);
                        memberCardItem.setSecondCategoryName(secondCategoryName);
                        memberCardItem.setValidTime(validTime);
                        memberCardItem.setIsValidForever(isValidForever);
                        memberCardItems.add(memberCardItem);

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(itemName);
                        product.setItemType("服务项");
                        product.setPrice(price.replace("￥", ""));
                        product.setFirstCategoryName(firstCategoryName);
                        product.setSecondCategoryName(secondCategoryName);
                        products.add(product);
                    }
                }
            }
        }

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("结果为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\掌上车店卡内项目.xls";
        String pathname2 = "C:\\exportExcel\\掌上车店卡内项目商品.xls";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, workbook, pathname);
        ExportUtil.exportProductDataInLocal(products, workbook, pathname2);
    }

    /**
     * 会员卡-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        Set<String> cardIds = getMemberCardId();
        if (cardIds.size() > 0) {
            for (String cardId : cardIds) {
                Response res = ConnectionUtil.doGetWithLeastParams(MEMBERCARDDETAIL_URL + cardId, COOKIE);
                String html = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                String cardCodeRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(3) > div:nth-child(2)";
                String memberCardNameRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(4) > div:nth-child(2)";
                String carNumberRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(4) > div:nth-child(4)";
                String nameRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(5) > div:nth-child(2)";
                String phoneRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(5) > div:nth-child(6)";
                String balanceRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(6) > div:nth-child(2) > b";
                String dateCreatedRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(7) > div:nth-child(2)";
                String validTimeRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(7) > div:nth-child(4)";

                String cardCode = doc.select(cardCodeRegEx).text();
                String memberCardName = doc.select(memberCardNameRegEx).text();
                String carNumber = doc.select(carNumberRegEx).text();
                String name = doc.select(nameRegEx).text();
                String phone = doc.select(phoneRegEx).text();
                String balance = doc.select(balanceRegEx).text();
                String dateCreated = doc.select(dateCreatedRegEx).text();
                String validTime = doc.select(validTimeRegEx).text();

                if (!"-".equals(dateCreated))
                    dateCreated = DateUtil.formatDate2DateTime(dateCreated);

                MemberCard memberCard = new MemberCard();
                memberCard.setCardCode(cardCode);
                memberCard.setMemberCardName(memberCardName);
                memberCard.setCarNumber(carNumber);
                memberCard.setName(name);
                memberCard.setPhone(phone);
                memberCard.setBalance(balance.replace("￥", ""));
                memberCard.setDateCreated(dateCreated);
                memberCard.setCompanyName(companyName);
                memberCards.add(memberCard);
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("结果为" + memberCards.size());

        String pathname = "C:\\exportExcel\\掌上车店会员卡.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, workbook, pathname);
    }

    /**
     * 供应商-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams(supplierMethod, getPageValue("0", "30")), COOKIE);
        int totalPage = getSupplierTotalPage(response, 30);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams(supplierMethod, getPageValue(String.valueOf(i), "30")), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").elements();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("id") != null ? element.get("id").asText() : "";
                    ids.add(id);
                }
            }
        }

        if (ids.size() > 0) {
            for (String id : ids) {
                String value = "{" + "\"id\":" + "\"" + id + "\"" +
                        "," + "\"op\":" + "\"" + "get" + "\"" + "}";
                Response res = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams(supplierDetailMethod, value), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                JsonNode data = result.get("data");
                String name = data.get("supplierName") != null ? data.get("supplierName").asText() : "";
                String companyName = data.get("storeName") != null ? data.get("storeName").asText() : "";
                String contactName = data.get("linkManName") != null ? data.get("linkManName").asText() : "";
                String contactPhone = data.get("mobilePhone") != null ? data.get("mobilePhone").asText() : "";
                String fax = data.get("officePhone") != null ? data.get("officePhone").asText() : "";//业务电话
                String address = data.get("address") != null ? data.get("address").asText() : "";
                String accountNumber = data.get("bankAccount") != null ? data.get("bankAccount").asText() : "";
                String depositBank = data.get("bankName") != null ? data.get("bankName").asText() : "";
                String accountName = data.get("receiveManName") != null ? data.get("receiveManName").asText() : "";
                String remark = data.get("memoInfo") != null ? data.get("memoInfo").asText() : "";
                String type = data.get("supplyType") != null ? data.get("supplyType").asText() : "";//供应商类别


                Supplier supplier = new Supplier();
                supplier.setName(name);
                supplier.setCompanyName(companyName);
                supplier.setContactName(contactName);
                supplier.setContactPhone(contactPhone);
                supplier.setFax(fax);
                supplier.setAddress(address);
                supplier.setAccountNumber(accountNumber);
                supplier.setDepositBank(depositBank);
                supplier.setAccountName(accountName);
                supplier.setRemark(remark + " " + type);
                suppliers.add(supplier);
            }
        }

        System.out.println("结果为" + suppliers.toString());
        System.out.println("结果为" + suppliers.size());

        String pathname = "C:\\exportExcel\\掌上车店供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, workbook, pathname);

    }

    /**
     * 车辆信息-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Set<String> carInfoIds = new HashSet<>();

        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(carInfoMethod, getPageValue("0", "15")), COOKIE);
        int totalPage = getTotalPage(response, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(carInfoMethod, getPageValue(String.valueOf(i), "15")), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").elements();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String dataId = element.get("memberId") != null ? element.get("memberId").asText() : "";
                    carInfoIds.add(dataId);
                }
            }
        }

        if (carInfoIds.size() > 0) {
            for (String carId : carInfoIds) {
                String value = "{" + "\"dataId\":" + "\"" + carId + "\"" + "}";
                Response res = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(carInfoDetailMethod, value), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                JsonNode data = result.get("data");
                String companyName = data.get("storeName") != null ? data.get("storeName").asText() : "";
                String carNumber = data.get("carPlateNo") != null ? data.get("carPlateNo").asText() : "";
                String name = data.get("userName") != null ? data.get("userName").asText() : "";
                String phone = data.get("telephone") != null ? data.get("telephone").asText() : "";

                JsonNode userCarInfos = data.get("userCarInfos");
                if (userCarInfos != null) {
                    Iterator<JsonNode> userCarInfo = userCarInfos.elements();
                    while (userCarInfo.hasNext()) {
                        JsonNode element = userCarInfo.next();

                        String contactName = element.get("contactName") != null ? element.get("contactName").asText() : "";
                        if (StringUtils.isBlank(contactName))
                            contactName = name;

                        String contactTelephone = element.get("contactTelephone") != null ? element.get("contactTelephone").asText() : "";
                        if (StringUtils.isBlank(contactTelephone))
                            contactTelephone = phone;

                        String myCarPlateNo = element.get("myCarPlateNo") != null ? element.get("myCarPlateNo").asText() : "";
                        if (StringUtils.isBlank(myCarPlateNo))
                            myCarPlateNo = carNumber;

                        String engineNumber = element.get("engineNumber") != null ? element.get("engineNumber").asText() : "";
                        String carModel = element.get("carModelName") != null ? element.get("carModelName").asText() : "";
                        String vcInsuranceCompany = element.get("companyName") != null ? element.get("companyName").asText() : "";
                        String vcInsuranceValidDate = element.get("insuranceDate") != null ? element.get("insuranceDate").asText() : "";
                        String VINCode = element.get("vehicleIdNumber") != null ? element.get("vehicleIdNumber").asText() : "";

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCompanyName(companyName);
                        carInfo.setCarNumber(myCarPlateNo);
                        carInfo.setPhone(contactTelephone);
                        carInfo.setName(contactName);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setCarModel(carModel);
                        carInfo.setVINcode(VINCode);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("结果为" + carInfos.size());

        String pathname = "C:\\exportExcel\\掌上车店车辆信息.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);

    }

    private Set<String> getMemberCardId() throws IOException {
        Set<String> cardIds = new HashSet<>();

        Response response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getTotalParams("1"), COOKIE);
        int totalPage = getMemberCardTotalPage(response, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getTotalParams(String.valueOf(i)), COOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String getMemberCardIdRegEx = "body > div.wrapper > div.contents > div > div.main > table > tbody > tr:nth-child({no}) > td:nth-child(14) > p:nth-child(1) > a";
                for (int j = 1; j <= 15; j++) {
                    String cardIdRegEx = StringUtils.replace(getMemberCardIdRegEx, "{no}", j + "");
                    String cardId = document.select(cardIdRegEx).attr("data-id");

                    if (StringUtils.isNotBlank(cardId))
                        cardIds.add(cardId);
                }
            }
        }

        return cardIds;
    }

    private List<BasicNameValuePair> getTotalParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("page.no", pageNo));
        return params;
    }

    private List<BasicNameValuePair> getParams(String method, String value) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("method", method));
        params.add(new BasicNameValuePair("data", value));
        return params;
    }

    private String getPageValue(String pageNo, String pageSize) {
        String value = "{" + "\"pageSize\":" + pageSize + "," +
                "\"pageNo\":" + pageNo + "}";

        return value;
    }


    private int getMemberCardTotalPage(Response response, int num) throws IOException {
        int totalPage = 0;

        if (response != null) {
            String html = response.returnContent().asString();
            Document doc = Jsoup.parseBodyFragment(html);

            String totalPageRegEx = "var pageCount=.*";
            String totalStr = CommonUtil.fetchString(doc.toString(), totalPageRegEx);
            String getTotalPageRegEx = "(?<=').*(?=')";
            String total = CommonUtil.fetchString(totalStr, getTotalPageRegEx);
            int count = Integer.parseInt(total);

            if (count % num == 0) {
                totalPage = count / num;
            } else
                totalPage = count / num + 1;

        }
        return totalPage;
    }

    private int getSupplierTotalPage(Response response, int num) throws IOException {
        int totalPage = 0;

        if (response != null) {
            JsonNode result = MAPPER.readTree(response.returnContent().asString());
            String countStr = result.get("data").get(0).asText();
            int count = Integer.parseInt(countStr);

            if (count % num == 0) {
                totalPage = count / num;
            } else
                totalPage = count / num + 1;
        }
        return totalPage;
    }

    private int getTotalPage(Response response, int num) throws IOException {
        int totalPage = 0;

        if (response != null) {
            JsonNode result = MAPPER.readTree(response.returnContent().asString());
            String countStr = result.get("data").get(0).get("count").asText();
            int count = Integer.parseInt(countStr);

            if (count % num == 0) {
                totalPage = count / num;
            } else
                totalPage = count / num + 1;
        }
        return totalPage;
    }


}
