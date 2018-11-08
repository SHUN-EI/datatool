package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mo on @date  2018/7/24.
 * <p>
 * 众途系统
 */
@Service
public class ZhongTuService {

    private String BILLITEM_URL = "http://crm.xmheigu.com/Boss/Finance/Service/GetPartsDetailed.ashx";

    private String BILL_URL = "http://crm.xmheigu.com/Boss/Finance/Service/GetBillsTable.ashx";

    private String MEMBERCARDITEM_URL = "http://crm.xmheigu.com/Boss/Customer/CustomerPackageList.aspx";

    private String MEMBERCARD_URL = "http://crm.xmheigu.com/Boss/Customer/CustomerCardListMem.aspx?action=GetList&groupId={no}&keyword=&rows=20&sort=ID&order=desc&page=";

    private String STOCK_URL = "http://crm.xmheigu.com/Boss/Stock/Stockservice/StockSearch.ashx";

    private String CARINFODETAIL_URL = "http://crm.xmheigu.com/Boss/Customer/ashx/GetData.ashx";

    private String CARINFO_URL = "http://crm.xmheigu.com/Boss/Customer/ashx/GetCustomerData.ashx";

    private String SERVICE_URL = "http://crm.xmheigu.com/Boss/Stock/XN_ShopList.aspx";

    private String LOGIN_URL = "http://crm.xmheigu.com/Boss/Index.aspx";

    private String SUPPLIER_URL = "http://crm.xmheigu.com/Boss/Stock/SupplierList.aspx";

    private String SUPPLIERDETAIL_URL = "http://crm.xmheigu.com/Boss/Stock/SupplierEdit.aspx?action=edit&id=";

    private String ITEM_URL = "http://crm.xmheigu.com/Boss/Stock/Stockservice/StockShop.ashx";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String USERID = "txtUserId";

    private String PWDID = "txtUserPwd";

    private String BTNLOGIN = "btnLogin";

    private String USERNAME = "zhongtu";

    private String PASSWORD = "080808";

    private String fieldName = "total";

    private int count = 0;

    private List<HtmlPage> pages = new ArrayList();

    private Map<String, Product> productMap = new HashMap<>();

    private List<Product> products = new ArrayList<>();

    private String trRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr";

    //车店编号
    private String groupId = "1";

    private String btime = "2001-01-01";

    private String companyName = "众途";

    private String COOKIE = "ASP.NET_SessionId=qqlpvkda02kf5owagjknxc1e; ztrjnew@4db97b96-12af-45b0-b232-fd1e9b7a672e=UserId=YJWelm6Yb9U=&CSID=YJWelm6Yb9U=&UserName=3uLp7ZTjgZW6OssgsPootw==&SID=TVo+7r+xtys=&RoleId=VBdEVOSspJM=&GroupId=VBdEVOSspJM=";


    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        Map<String, Bill> billMap = new HashMap<>();
        Map<String, Bill> billItemMap = getBillItemData();

        String act = "GetData";
        Response response = ConnectionUtil.doPostWithLeastParams(BILL_URL, getBillParams("1", "100", act), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 100);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(BILL_URL, getBillParams(String.valueOf(i), "100", act), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("OrderID").asText();
                    String carNumber = element.get("CarCode").asText();
                    String mileage = element.get("Themileage").asText();
                    String remark = element.get("ItemRemark").asText();
                    String receptionistName = element.get("Adviser").asText();
                    String num = element.get("ProNum").asText();
                    String unitPrice = element.get("ProPrice").asText();

                    String dateEnd = element.get("SettlementTime").asText();
                    dateEnd = DateUtil.formatDateTime2Date(dateEnd);

                    String totalAmountStr = element.get("ProSum").asText();
                    String serviceItemNames = element.get("ProductName").asText();
                    serviceItemNames = serviceItemNames + "*" + totalAmountStr;

                    //汇总项目
                    if (billMap.size() > 0 && null != billMap.get(billNo)) {
                        Bill b = billMap.get(billNo);

                        if (null != b.getServiceItemNames() && !"".equals(serviceItemNames)) {
                            serviceItemNames = b.getServiceItemNames() + "," + serviceItemNames;
                        }
                    }

                    //计算单据总价
                    BigDecimal sum = element.get("ProSum").decimalValue();
                    if (billMap.size() > 0 && null != billMap.get(billNo)) {
                        Bill b = billMap.get(billNo);
                        String totalPriceStr = b.getTotalAmount();
                        BigDecimal totalPrice = new BigDecimal(totalPriceStr);
                        sum = sum.add(totalPrice);
                        sum.setScale(2, BigDecimal.ROUND_HALF_UP);
                        totalAmountStr = sum.toString();
                    }

                    //获取配件
                    String goodsNames = "";
                    if (null != billItemMap.get(billNo)) {
                        Bill b = billItemMap.get(billNo);
                        goodsNames = b.getGoodsNames();
                    }

                    Bill bill = new Bill();
                    bill.setBillNo(billNo);
                    bill.setCompanyName(companyName);
                    bill.setCarNumber(CommonUtil.formatString(carNumber));
                    bill.setMileage(CommonUtil.formatString(mileage));
                    bill.setRemark(CommonUtil.formatString(remark));
                    bill.setReceptionistName(CommonUtil.formatString(receptionistName));
                    bill.setTotalAmount(totalAmountStr);
                    bill.setDateEnd(dateEnd);
                    bill.setServiceItemNames(serviceItemNames);
                    bill.setGoodsNames(goodsNames);
                    billMap.put(billNo, bill);
                }
            }
        }

        if (billMap.size() > 0) {
            billMap.entrySet().forEach(b -> {
                bills.add(b.getValue());
            });
        }


        System.out.println("页数为" + totalPage);

        String pathname = "C:\\exportExcel\\众途历史消费记录.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);

    }

    private Map<String, Bill> getBillItemData() throws IOException {
        Map<String, Bill> billItemMap = new HashMap<>();

        String act = "GetData";
        Response response = ConnectionUtil.doPostWithLeastParams(BILLITEM_URL, getBillItemParams("1", "100", act), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 100);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(BILLITEM_URL, getBillItemParams(String.valueOf(i), "100", act), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("OrderID").asText();
                    String num = element.get("Num").asText();
                    String totalAmountStr = element.get("Amount").asText();
                    totalAmountStr = totalAmountStr.replace("-", "");
                    String unitPrice = element.get("Cost").asText();

                    String goodsNames = element.get("ProductName").asText();
                    goodsNames = goodsNames + "*" + totalAmountStr;

                    //汇总配件
                    if (billItemMap.size() > 0 && null != billItemMap.get(billNo)) {
                        Bill b = billItemMap.get(billNo);

                        if (null != b.getGoodsNames() && !"".equals(goodsNames)) {
                            goodsNames = b.getGoodsNames() + "," + goodsNames;
                        }
                    }

                    Bill bill = new Bill();
                    bill.setBillNo(billNo);
                    bill.setGoodsNames(goodsNames);
                    billItemMap.put(billNo, bill);
                }
            }
        }

        return billItemMap;
    }

    /**
     * 卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        getServiceData(products, productMap);

        WebClient webClient = WebClientUtil.getWebClient();
        getAllPages(webClient, MEMBERCARDITEM_URL);

        if (pages.size() > 0) {
            for (HtmlPage page : pages) {
                Document doc = Jsoup.parseBodyFragment(page.asXml());

                String getTRRegEx = "#form1 > div.form_div > div > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, getTRRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int i = 2; i <= trSize; i++) {
                        String cardCodeRegEx = "#form1 > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(5)";
                        String itemNameRegEx = "#form1 > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(7)";
                        String originalNumRegEx = "#form1 > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(8)";
                        String numRegEx = "#form1 > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(10)";
                        String validTimeRegEx = "#form1 > div.form_div > div > table > tbody > tr:nth-child(2) > td:nth-child(12)";

                        String cardCode = doc.select(StringUtils.replace(cardCodeRegEx, "{no}", String.valueOf(i))).text();
                        String itemName = doc.select(StringUtils.replace(itemNameRegEx, "{no}", String.valueOf(i))).text();
                        String originalNum = doc.select(StringUtils.replace(originalNumRegEx, "{no}", String.valueOf(i))).text();
                        String num = doc.select(StringUtils.replace(numRegEx, "{no}", String.valueOf(i))).text();
                        String validTime = doc.select(StringUtils.replace(validTimeRegEx, "{no}", String.valueOf(i))).text();

                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setCardCode(cardCode);
                        memberCardItem.setItemName(itemName);
                        memberCardItem.setOriginalNum(originalNum);
                        memberCardItem.setNum(num);
                        memberCardItem.setValidTime(DateUtil.formatDateTime(validTime));
                        memberCardItem.setCompanyName(companyName);
                        memberCardItem.setIsValidForever(CommonUtil.getIsValidForever(validTime));

                        Product product = productMap.get(itemName);
                        if (product != null) {
                            memberCardItem.setPrice(product.getPrice());
                            memberCardItem.setFirstCategoryName(product.getFirstCategoryName());
                            memberCardItem.setSecondCategoryName(product.getSecondCategoryName());
                            memberCardItem.setCode(product.getCode());
                        }

                        memberCardItems.add(memberCardItem);
                    }
                }
            }
        }

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("结果为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\众途卡内项目.xls";
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

        Response res = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARD_URL, "{no}", groupId) + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARD_URL, "{no}", groupId) + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());


                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String company = element.get("GroupName").asText();
                    String cardCode = element.get("Code").asText();
                    String name = element.get("Name").asText();
                    String phone = element.get("Mobile").asText();
                    String carNumber = element.get("CarCode").asText();
                    String memberCardName = element.get("CardName").asText();
                    String balance = element.get("PackagePrice").asText();
                    String dateCreated = element.get("StartTime").asText();//"CreateTime"

                    if (StringUtils.isNoneBlank(dateCreated))
                        dateCreated = dateCreated.replace("T", " ");

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCompanyName(company);
                    memberCard.setCardCode(cardCode);
                    memberCard.setCarNumber(carNumber);
                    memberCard.setMemberCardName(memberCardName);
                    memberCard.setBalance(balance);
                    memberCard.setDateCreated(dateCreated.replace("-", "/"));
                    memberCard.setName(name);
                    memberCard.setPhone(phone);
                    memberCards.add(memberCard);
                }
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("结果为" + memberCards.size());

        String pathname = "C:\\exportExcel\\众途会员卡.xls";
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
        Set<StoreRoom> storeRooms = new HashSet<>();
        Map<String, String> storeRoomMap = new HashMap<>();

        String act = "GetStockList";
        Response res = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams("1", "20", act), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams(String.valueOf(i), "20", act), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                JsonNode companyNode = result.get("columns0").get(0);
                String company = companyNode.get("title").asText();

                Iterator<JsonNode> storeRoomNode = result.get("columns1").iterator();
                while (storeRoomNode.hasNext()) {
                    JsonNode element = storeRoomNode.next();

                    String id = element.get("field").asText();
                    String name = element.get("title").asText();

                    StoreRoom storeRoom = new StoreRoom();
                    storeRoom.setCompanyName(company);
                    storeRoom.setId(id);
                    storeRoom.setName(name);
                    storeRooms.add(storeRoom);
                    storeRoomMap.put(id, name);
                }

                Iterator<JsonNode> stockNode = result.get("rows").iterator();
                while (stockNode.hasNext()) {
                    JsonNode element = stockNode.next();

                    String goodsName = element.get("Name").asText();
                    String price = element.get("Cost").asText();
                    String productCode = element.get("ShopCode").asText();

                    for (String id : storeRoomMap.keySet()) {

                        String storeRoom = storeRoomMap.get(id);
                        String num = element.get(id).asText();

                        if ("null".equals(num))
                            continue;

                        Stock stock = new Stock();
                        stock.setCompanyName(company);
                        stock.setStoreRoomName(storeRoom);
                        stock.setPrice(price);
                        stock.setGoodsName(goodsName);
                        stock.setProductCode(productCode);
                        stock.setInventoryNum(num);
                        stocks.add(stock);
                    }
                }
            }
        }


        System.out.println("结果为" + stocks.toString());
        System.out.println("结果为" + stocks.size());

        String storeRoomPathname = "C:\\exportExcel\\众途仓库.xls";
        String stockPathname = "C:\\exportExcel\\众途库存.xls";
        ExportUtil.exportStoreRoomDataInLocal(storeRooms, ExcelDatas.workbook, storeRoomPathname);
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, stockPathname);

    }


    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        String act = "";
        Response res = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams("1", "20", act), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(String.valueOf(i), "20", act), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carId = element.get("ID").asText();
                    String companyName = element.get("GroupName").asText();
                    String carNumber = element.get("CarCode").asText();
                    String name = element.get("Name").asText();
                    String phone = element.get("Mobile").asText();
                    String brand = element.get("CarName").asText();
                    String carModel = element.get("CarModelName").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCompanyName(companyName);
                    carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                    carInfo.setName(CommonUtil.formatString(name));
                    carInfo.setPhone(CommonUtil.formatString(phone));
                    carInfo.setBrand(CommonUtil.formatString(brand));
                    carInfo.setCarModel(CommonUtil.formatString(carModel));

                    carInfo.setCarId(carId);
                    List<BasicNameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("Type", "Customer"));
                    params.add(new BasicNameValuePair("ID", carId));

                    Response response = ConnectionUtil.doPostWithLeastParams(CARINFODETAIL_URL, params, COOKIE);
                    JsonNode content = MAPPER.readTree(response.returnContent().asString());

                    JsonNode data = content.get(0);
                    String VINCode = data.get("CarFrame").asText();
                    String engineNumber = data.get("CarEngineNo").asText();
                    String vcInsuranceCompany = data.get("BaoXianGs").asText();

                    String vcInsuranceValidDateStr = data.get("InsuranceDate").asText();
                    String vcInsuranceValidDate = CommonUtil.formatString(vcInsuranceValidDateStr);

                    carInfo.setVINcode(CommonUtil.formatString(VINCode));
                    carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                    carInfo.setVcInsuranceCompany(CommonUtil.formatString(vcInsuranceCompany));
                    carInfo.setVcInsuranceValidDate(DateUtil.formatSQLDate(vcInsuranceValidDate));
                    carInfos.add(carInfo);
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("结果为" + carInfos.size());

        String pathname = "C:\\exportExcel\\众途车辆.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }


    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        getServiceData(products, productMap);

        System.out.println("结果为" + products.toString());
        System.out.println("结果为" + products.size());

        String pathname = "C:\\exportExcel\\众途服务.xls";
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
        Set<String> ids = new HashSet<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllPages(webClient, SUPPLIER_URL);

        if (pages.size() > 0) {
            for (HtmlPage page : pages) {
                Document doc = Jsoup.parseBodyFragment(page.asXml());

                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int i = 2; i <= trSize; i++) {
                        String idRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(3) > a";
                        String getIdRegEx = "id=.*";
                        String idStr = doc.select(StringUtils.replace(idRegEx, "{no}", String.valueOf(i))).attr("href");
                        String id = CommonUtil.fetchString(idStr, getIdRegEx).replace("id=", "");
                        ids.add(id);
                    }
                }
            }
        }

        if (ids.size() > 0) {
            for (String id : ids) {

                Response res = ConnectionUtil.doGetWithLeastParams(SUPPLIERDETAIL_URL + id, COOKIE);
                String html = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                String nameRegEx = "#txt_Name";
                String contactNameRegEx = "#txt_LinkMan";
                String contactPhoneRegEx = "#txt_Mobile";
                String addressRegEx = "#txt_Address";
                String remarkRegEx = "#txt_Remark";
                String accountNameRegEx = "#txt_RealName";
                String depositBankRegEx = "#txt_BankName";
                String accountNumberRegEx = "#txt_BankCode";
                String faxRegEx = "#txt_Fax";
                String phoneRegEx = "#txt_Tel";

                String name = doc.select(nameRegEx).attr("value");
                String contactName = doc.select(contactNameRegEx).attr("value");
                String contactPhone = doc.select(contactPhoneRegEx).attr("value");
                String address = doc.select(addressRegEx).attr("value");
                String remark = doc.select(remarkRegEx).attr("value");
                String accountName = doc.select(accountNameRegEx).attr("value");
                String depositBank = doc.select(depositBankRegEx).attr("value");
                String accountNumber = doc.select(accountNumberRegEx).attr("value");
                String fax = doc.select(faxRegEx).attr("value");
                String phone = doc.select(phoneRegEx).attr("value");

                Supplier supplier = new Supplier();
                supplier.setName(name);
                supplier.setContactName(contactName);
                supplier.setContactPhone(contactPhone);
                supplier.setAddress(address);
                supplier.setAccountName(accountName);
                supplier.setDepositBank(depositBank);
                supplier.setAccountNumber(accountNumber);
                supplier.setRemark(remark);
                supplier.setPhone(phone);
                supplier.setFax(fax);
                supplier.setCompanyName(companyName);
                suppliers.add(supplier);
            }
        }

        System.out.println("结果为" + suppliers.toString());

        String pathname = "C:\\exportExcel\\众途供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }


    /**
     * 商品
     *
     * @throws IOException
     */
    @Test
    public void fetchItemDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        String act = "GetShopList";
        Response res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams("1", "15", act), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams(String.valueOf(i), "15", act), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("Name").asText();
                    String companyName = element.get("GroupName").asText();
                    String code = element.get("ShopCode").asText();
                    String firstCategoryName = element.get("ShopTypeName").asText();
                    String price = element.get("Price").asText();
                    String barCode = element.get("NameCode").asText();
                    String unit = element.get("UnitName").asText();
                    String remark = element.get("GuiGe").asText();//产品规格

                    Product product = new Product();
                    product.setCode(code);
                    product.setProductName(productName);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setUnit(unit);
                    product.setPrice(price);
                    product.setCompanyName(companyName);
                    product.setBarCode(barCode);
                    product.setItemType("配件");
                    product.setRemark(remark);
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());
        System.out.println("结果为" + products.size());

        String pathname = "C:\\exportExcel\\众途商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    private void getServiceData(List<Product> products, Map<String, Product> productMap) throws IOException {
        WebClient webClient = WebClientUtil.getWebClient();
        getAllPages(webClient, SERVICE_URL);

        if (pages.size() > 0) {
            for (HtmlPage page : pages) {
                Document doc = Jsoup.parseBodyFragment(page.asXml());

                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int i = 2; i <= trSize; i++) {

                        String productNameRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(3) > a";
                        String codeRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(5)";
                        String priceRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(8)";
                        String unitRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(10)";
                        String firstCategoryNameRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(11)";
                        String remarkRegEx = "#form1 > div.ctn.h > div > div.form_div > div > table > tbody > tr:nth-child({no}) > td:nth-child(4)";

                        String productName = doc.select(StringUtils.replace(productNameRegEx, "{no}", String.valueOf(i))).text();
                        String code = doc.select(StringUtils.replace(codeRegEx, "{no}", String.valueOf(i))).text();
                        String unit = doc.select(StringUtils.replace(unitRegEx, "{no}", String.valueOf(i))).text();
                        String firstCategoryName = doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(i))).text();
                        String price = doc.select(StringUtils.replace(priceRegEx, "{no}", String.valueOf(i))).text();
                        String remark = doc.select(StringUtils.replace(remarkRegEx, "{no}", String.valueOf(i))).text();

                        Product product = new Product();
                        product.setProductName(productName);
                        product.setCompanyName(companyName);
                        product.setCode(code);
                        product.setUnit(unit);
                        product.setFirstCategoryName(firstCategoryName);
                        product.setPrice(price);
                        product.setItemType("配件");
                        product.setRemark(remark);//规格
                        products.add(product);
                        productMap.put(productName, product);
                    }
                }
            }
        }


    }

    private List<BasicNameValuePair> getBillItemParams(String page, String rows, String act) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("act", act));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("keyword", ""));
        params.add(new BasicNameValuePair("type", "0"));
        params.add(new BasicNameValuePair("btime", btime));
        params.add(new BasicNameValuePair("etime", DateUtil.formatCurrentDate()));
        params.add(new BasicNameValuePair("rows", rows));
        params.add(new BasicNameValuePair("groupId", groupId));
        return params;
    }

    private List<BasicNameValuePair> getBillParams(String page, String rows, String act) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("act", act));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("btime", btime));
        params.add(new BasicNameValuePair("etime", DateUtil.formatCurrentDate()));
        params.add(new BasicNameValuePair("rows", rows));
        params.add(new BasicNameValuePair("groupId", groupId));
        params.add(new BasicNameValuePair("paymentId", "-1"));
        params.add(new BasicNameValuePair("businessId", "-1"));
        return params;
    }


    private List<BasicNameValuePair> getParams(String page, String rows, String act) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("act", act));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("rows", rows));
        params.add(new BasicNameValuePair("GroupID", groupId));
        return params;
    }

    private void getAllPages(WebClient webClient, String url) throws IOException {
        login(webClient);

        HtmlPage page = webClient.getPage(url);
        pages.add(page);

        String total = WebClientUtil.getTotalPage(page);
        nextPage(page, Integer.parseInt(total));
    }


    private void login(WebClient webClient) throws IOException {
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);

        HtmlInput user = loginPage.getHtmlElementById(USERID);
        HtmlInput pwd = loginPage.getHtmlElementById(PWDID);
        HtmlInput btnLogin = loginPage.getHtmlElementById(BTNLOGIN);//登录按钮

        user.setAttribute("value", USERNAME);
        pwd.setAttribute("value", PASSWORD);

        //登录操作
        btnLogin.click();
    }

    private void nextPage(HtmlPage page, int num) throws IOException {
        ++count;
        if (count == num)
            return;

        String anchorXPath = "//*[@id=\"AspNetPager\"]/div/a[3]";
        HtmlAnchor nextPage = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = nextPage.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num);//num为总页数
    }

}
