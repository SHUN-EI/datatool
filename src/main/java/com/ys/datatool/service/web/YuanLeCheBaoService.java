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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by mo on @date  2018/5/14.
 * 元乐车宝系统
 */
@Service
public class YuanLeCheBaoService {

    private String CARDRIVINGLICENSE_URL = "http://www.carbao.vip/Home/car/drivingLicense";

    private String CLIENTLIST_URL = "http://www.carbao.vip/Home/receptionService/customerTable";

    private String CLIENTDETAIL_URL = "http://www.carbao.vip/Home/receptionService/customerDetail";

    private String CLIENT_URL = "http://www.carbao.vip/Home/receptionService/sreenCustomerTable";

    private String BILL_URL = "http://www.carbao.vip/Home/workbench/ajaxGetInServiceList";

    private String MEMBERCARD_URL = "http://www.carbao.vip/Home/memberManagement/gerMemberUserLists";

    private String MEMBERCARDOVERVIEW_URL = "http://www.carbao.vip/Home/memberManagement/memberList?shopBranchId=127&staffId=2341&shopId=";

    private String STOCKINSEARCH_URL = "http://www.carbao.vip/Home/storage/storageInSearchByPartsGuid";

    private String STOCKINDIV_URL = "http://www.carbao.vip/Home/partsinfo/storageInDiv";

    private String STOCKDETAIL_URL = "http://www.carbao.vip/Home/partsinfo/partsInfo";

    private String STOCK_URL = "http://www.carbao.vip/Home/cbstoragepartsinventory/invenManagementTable";

    private String SERVICE_URL = "http://www.carbao.vip/Home/service/serviceTable";

    private String SUPPLIER_URL = "http://www.carbao.vip/Home/cbpartssupplier/supplierTable";

    private String CARINFOPAGE_URL = "http://www.carbao.vip/Home/car/carTable";

    private String CARINFODETAIL_URL = "http://www.carbao.vip/Home/car/carDetail";

    private String trItemRegEx = "#content-tbody > tr";

    private String divRegEx = "div[class='userCars']  > div[class='row'] > div";

    private String totalPageRegEx = "totalPage =.*";

    private String totalRegEx = "totalPage=.*";

    private String fieldName = "totalCount";

    private String trName = "tr";

    private String divName = "div";

    private Charset charset = Charset.forName("UTF-8");

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private int num = 10;

    private String companyName = "元乐车宝";

    private Workbook workbook;

    private Random random = new Random();

    /**
     * 车店编号-shopId:
     * 215(冠军养护)、183(迅驰)、208(稳中快)、
     * 77(石家庄丽雷行)、140(天骐汽车)、132(路胜通汽车)、
     * 288(良匠汽车)、70(黑妞汽车)、82(国瑞汽修厂)、284(车来车旺美车会所)
     * 79(广州市花都区明杰)、113(新蔡爱卡汽车)、283(摩范汽车)
     */
    private String companyId = "132";

    /**
     * 分店编号-shopBranchId：
     * 146(路胜通汽车)、298(摩范汽车)
     */
    private String shopBranchId = "146";

    private String COOKIE = "JSESSIONID=41913339DDC6EA22DCE09B4CD4FEC352; usfl=watXJrLQTgNV1wqr4jX; lk=e357a31c0f5056771bcde96d5d1c401d";

    @Test
    public void test() throws Exception {

        String a = "02082192238，13928706998";
        int index = a.indexOf("，");

        String a1 = a.substring(0, index);
        String a2 = a.substring(index + 1, a.length());

        System.out.println("结果为" + a.length());
        System.out.println("结果为" + a1);
        System.out.println("结果为" + a2);

    }

    /**
     * 供应商-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Set<String> supplierDetails = new HashSet<>();

        int totalPage = getTotalPage(SUPPLIER_URL, getPageInfoParams("1"));
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = response.returnContent().asString(charset);
                Document doc = Jsoup.parse(html);

                for (int j = 1; j <= 10; j++) {
                    String supplierDetailRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(5) > a.supplierDetail";
                    String detailUrl = doc.select(StringUtils.replace(supplierDetailRegEx, "{no}", String.valueOf(j))).attr("content-url");

                    if (StringUtils.isNotBlank(detailUrl))
                        supplierDetails.add(detailUrl);
                }
            }
        }

        if (supplierDetails.size() > 0) {
            for (String supplierDetail : supplierDetails) {
                String preUrl = "http://www.carbao.vip";
                Response response = ConnectionUtil.doGetWithLeastParams(preUrl + supplierDetail, COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String nameRegEx = "#content > div > div.row.row-d > div:nth-child(1) > div:nth-child(1) > div.col-md-7";
                String contactPhoneRegEx = "#content > div > div.row.row-d > div:nth-child(2) > div:nth-child(2) > div.col-md-7";
                String contactNameRegEx = "#content > div > div.row.row-d > div:nth-child(2) > div:nth-child(1) > div.col-md-7";
                String remarkRegEx = "#content > div > div.row.row-d > div:nth-child(3) > div > div.col-md-7";

                String name = doc.select(nameRegEx).text();
                String contactPhone = doc.select(contactPhoneRegEx).text();
                String contactName = doc.select(contactNameRegEx).text();
                String remark = doc.select(remarkRegEx).text();
                String fax = "";

                if (contactPhone.contains("，")) {
                    int index = contactPhone.indexOf("，");
                    fax = contactPhone.substring(index + 1, contactPhone.length());
                    contactPhone = contactPhone.substring(0, index);
                    int a=contactPhone.length();

                }

                Supplier supplier = new Supplier();
                supplier.setCompanyName(companyName);
                supplier.setName(name);
                supplier.setContactName(contactName);
                supplier.setContactPhone(contactPhone);
                supplier.setRemark(remark);
                supplier.setFax(fax);
                suppliers.add(supplier);
            }
        }
        System.out.println("结果为" + suppliers.toString());
        System.out.println("大小为" + suppliers.size());

        String pathname = "C:\\exportExcel\\元乐车宝供应商导出.xls";
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

        int totalPage = getTotalPage(CARINFOPAGE_URL, getPageInfoParams("1"));
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doPostWithLeastParams(CARINFOPAGE_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = response.returnContent().asString(charset);
                Document doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String clientRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(9) > a:nth-child(1)";

                        String carId = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("userid");
                        String phone = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("mobile");
                        String name = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("username");
                        String carnum = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("carnum");
                        String cararea = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("cararea");
                        String carNumber = cararea + carnum;

                        CarInfo carInfo = new CarInfo();
                        carInfo.setPhone(phone);
                        carInfo.setCompanyName(companyName);
                        carInfo.setName(name);
                        carInfo.setCarNum(carnum);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setCarArea(cararea);
                        carInfo.setCarId(carId);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String userName = carInfo.getName();
                String userId = carInfo.getCarId();
                String carNum = carInfo.getCarNum();
                String carArea = carInfo.getCarArea();
                String mobile = carInfo.getPhone();

                Response response = ConnectionUtil.doPostWithLeastParams(CARINFODETAIL_URL, getCarInfoDetailParams(userName, userId, carArea, carNum, mobile), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String carNumberRegEx = "#is_bang > div > div:nth-child(1) > span:nth-child(3)";
                String VINRegEx = "#isReset > table > tbody > tr > td:nth-child(1) > span";
                String brandRegEx = "#isReset > table > tbody > tr > td:nth-child(2)";
                String carModelRegEx = "#isReset > table > tbody > tr > td:nth-child(3)";

                String userCarIdRegEx = "[\\s\\S]*var userId[\\s\\S]*var userCarId = (\\d+)[\\s\\S]*";
                String userCarId = CommonUtil.filterString(doc.toString(), userCarIdRegEx);

                String VINcode = doc.select(VINRegEx).text().replace("VIN：", "");
                String carModel = doc.select(carModelRegEx).text().replace("车型：", "");
                String brand = doc.select(brandRegEx).text().replace("品牌：", "");

                carInfo.setUserCarId(userCarId);
                carInfo.setVINcode(VINcode.replace("未完善", ""));
                carInfo.setCarModel(carModel.replace("-", ""));
                carInfo.setBrand(brand.replace("-", ""));
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String userCarId = carInfo.getUserCarId();

                Response response = ConnectionUtil.doPostWithLeastParams(CARDRIVINGLICENSE_URL, getDetailParams("userCarId", userCarId), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String engineNumberRegEx = "#engineCode";
                String registerDateRegEx = "#registerDate";
                String vcInsuranceValidDateRegEx = "#insuranceTimeEnd";
                String VINcodeRegEx = "#vin";

                String engineNumber = doc.select(engineNumberRegEx).text();
                String registerDate = doc.select(registerDateRegEx).text();
                String vcInsuranceValidDate = doc.select(vcInsuranceValidDateRegEx).text();
                String VINcode = doc.select(VINcodeRegEx).text();

                carInfo.setEngineNumber(engineNumber.replace("未完善", ""));
                carInfo.setRegisterDate(registerDate.replace("未完善", ""));
                carInfo.setVcInsuranceValidDate(vcInsuranceValidDate.replace("未完善", ""));
                carInfo.setVINcode(VINcode.replace("未完善", ""));
            }
        }

        System.out.println("车辆分别为" + carInfos.toString());
        System.out.println("车辆大小为" + carInfos.size());


        String pathname = "C:\\exportExcel\\元乐车宝车辆信息导出.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);
    }

    /**
     * 卡内项目-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, MemberCard> packageMap = new HashMap<>();

        Map<String, MemberCard> memberCardMap = getMemberHasPackage();
        if (memberCardMap.size() > 0) {
            for (String userId : memberCardMap.keySet()) {
                Response res = ConnectionUtil.doPostWithLeastParams(CLIENTDETAIL_URL, getDetailParams("userId", userId), COOKIE);
                String content = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(content);

                String trRegEx = "#staleDated-content-tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {

                        String packageIdRegEx = "#staleDated-content-tbody > tr:nth-child({no}) > td:nth-child(9) > a";
                        String packageId = doc.select(StringUtils.replace(packageIdRegEx, "{no}", String.valueOf(i))).attr("packageId");

                        MemberCard memberCard = memberCardMap.get(userId);
                        packageMap.put(packageId, memberCard);
                    }
                }
            }
        }

        if (packageMap.size() > 0) {
            for (String packageId : packageMap.keySet()) {

                MemberCard memberCard = packageMap.get(packageId);
                String url = getMemberCardItemURL(memberCard.getUserId(), packageId);
                Response res = ConnectionUtil.doGetWithLeastParams(url, COOKIE);
                String content = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(content);

                String divRowRegEx = "div[class='recordCardInfo']  > div[class=row] > div[class=col-sm-4]";
                Elements elements = doc.select(divRowRegEx).tagName("div");
                String validTime = elements.get(4).getElementsByTag("span").get(1).text();

                String isValidForever = CommonUtil.getIsValidForever(validTime);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {
                        String itemNameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(2)";
                        String originalNumRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(3)";
                        String numRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(5)";
                        String remarkRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(6)";

                        String itemName = doc.select(StringUtils.replace(itemNameRegEx, "{no}", String.valueOf(i))).text();
                        String originalNum = doc.select(StringUtils.replace(originalNumRegEx, "{no}", String.valueOf(i))).text();
                        String num = doc.select(StringUtils.replace(numRegEx, "{no}", String.valueOf(i))).text();
                        String remark = doc.select(StringUtils.replace(remarkRegEx, "{no}", String.valueOf(i))).text();

                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setCompanyName(companyName);
                        memberCardItem.setDiscount(memberCard.getCardSort());
                        memberCardItem.setCardCode(memberCard.getCardCode());
                        memberCardItem.setItemName(itemName);
                        memberCardItem.setOriginalNum(originalNum.replace("次数", ""));
                        memberCardItem.setNum(num.replace("次数", ""));
                        memberCardItem.setRemark(remark);
                        memberCardItem.setIsValidForever(isValidForever);
                        memberCardItem.setValidTime(DateUtil.formatDateTime(validTime));
                        memberCardItems.add(memberCardItem);
                    }
                }
            }
        }

        if (memberCardItems.size() > 0) {
            for (MemberCardItem memberCardItem : memberCardItems) {
                String itemName = memberCardItem.getItemName();

                Response res = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getServiceParams("1", itemName), COOKIE);
                String content = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(content);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {
                        String codeRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(1)";
                        String priceRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(3)";
                        String firstCategoryNameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(4)";
                        String itemRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(2)";

                        String name = doc.select(StringUtils.replace(itemRegEx, "{no}", String.valueOf(i))).text();
                        String firstCategoryName = doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(i))).text();
                        String price = doc.select(StringUtils.replace(priceRegEx, "{no}", String.valueOf(i))).text();
                        String code = doc.select(StringUtils.replace(codeRegEx, "{no}", String.valueOf(i))).text();

                        if (!name.equals(itemName))
                            continue;

                        memberCardItem.setCode(code);
                        memberCardItem.setPrice(price.replace("￥", ""));
                        memberCardItem.setFirstCategoryName(firstCategoryName);
                    }
                }
            }
        }

        System.out.println("memberCardItems结果为" + memberCardItems.toString());
        System.out.println("memberCardItems大小为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\元乐车宝卡内项目导出.xlsx";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, workbook, pathname);

    }

    /**
     * 会员卡-标准模版导出
     * 支持一卡多车
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        Map<String, MemberCard> memberCardMap = getMemberHasPackage();

        if (memberCardMap.size() > 0) {
            for (String userId : memberCardMap.keySet()) {
                Response res = ConnectionUtil.doPostWithLeastParams(CLIENTDETAIL_URL, getDetailParams("userId", userId), COOKIE);
                String content = res.returnContent().asString();
                Document document = Jsoup.parseBodyFragment(content);

                //int divSize = WebClientUtil.getTagSize(document, divRegEx, divName); 此方法无法判断divSize
                String divRowRegEx = "div[class='userCars']  > div[class=row] > div[class=col-sm-2]";
                Elements elements = document.select(divRowRegEx).tagName("div");
                if (elements.size() > 0) {
                    for (Element e : elements) {
                        Element carSpanElement = e.getElementsByTag("a").get(1).tagName("span");
                        String carNumberRegEx = "span[onclick=findCarDetail(this)]";
                        String cararea = carSpanElement.select(carNumberRegEx).attr("cararea");
                        String carnum = carSpanElement.select(carNumberRegEx).attr("carnum");
                        String carNumber = cararea + carnum;
                        //String carNumberRegEx = "div[class='userCars'] > div[class='row'] > div:nth-child({no}) > a:nth-child(2)";

                        String dateCreated = "";
                        String dateRegEx = "#staleDated-content-tbody";
                        String dateStr = document.select(dateRegEx).text();
                        if (StringUtils.isBlank(dateStr)) {
                            dateCreated = "1900-01-01";
                        } else {
                            String trRegEx = "#staleDated-content-tbody > tr";
                            int trSize = WebClientUtil.getTagSize(document, trRegEx, trName);
                            if (trSize > 0) {
                                for (int i = 1; i <= trSize; i++) {

                                    String dateCreatedRegEx = "#staleDated-content-tbody > tr:nth-child({no}) > td:nth-child(6)";
                                    dateCreated = document.select(StringUtils.replace(dateCreatedRegEx, "{no}", String.valueOf(i))).text();
                                    dateList.add(dateCreated);
                                }
                            }

                            if (dateList.size() > 0)
                                dateCreated = DateUtil.getEarliestDate(dateList);
                        }


                        MemberCard m = memberCardMap.get(userId);
                        MemberCard memberCard = new MemberCard();
                        memberCard.setCarNumber(carNumber);
                        memberCard.setName(m.getName());
                        memberCard.setPhone(m.getPhone());
                        memberCard.setBalance(m.getBalance());
                        memberCard.setMemberCardName(m.getMemberCardName());
                        memberCard.setCardCode(m.getCardCode());
                        memberCard.setCardSort(m.getCardSort());
                        memberCard.setDateCreated(DateUtil.formatDateTime(dateCreated));
                        memberCard.setCompanyName(companyName);
                        memberCards.add(memberCard);
                    }
                }
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("结果为" + memberCards.size());

        String pathname = "C:\\exportExcel\\元乐车宝会员卡导出.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, workbook, pathname);

    }

    /**
     * 单据
     *
     * @throws IOException
     */
    @Test
    public void fetchBillData() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(BILL_URL, getPageInfoParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(BILL_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("orderCode").asText();
                    String dateAdded = element.get("orderDate").asText();
                    String totalAmount = element.get("dealAmount").asText();//totalAmount
                    String carNumber = element.get("carNumber").asText();

                    String clientName = "";
                    String clientPhone = "";
                    if (element.get("userInfo") != null) {
                        clientName = element.get("userInfo").get("name").asText();
                        clientPhone = element.get("userInfo").get("mobile").asText();
                    }

                    String brand = "";
                    String carModel = "";
                    String mileage = "";
                    if (element.get("userCarInfo") != null) {
                        brand = element.get("userCarInfo").get("brand").asText();
                        carModel = element.get("userCarInfo").get("modelName").asText();
                        mileage = element.get("userCarInfo").get("lastMaintainMiles").asText();
                    }

                    Bill bill = new Bill();
                    bill.setBillNo(billNo);
                    bill.setDateAdded(dateAdded);
                    bill.setDateEnd(dateAdded);
                    bill.setDateExpect(dateAdded);
                    bill.setTotalAmount(totalAmount);
                    bill.setActualAmount(totalAmount);
                    bill.setCarNumber(carNumber);
                    bill.setClientName(clientName);
                    bill.setClientPhone(clientPhone);
                    bill.setBrand(brand);
                    bill.setCarModel(carModel);
                    bill.setMileage(mileage);
                    bills.add(bill);
                }
            }
        }


        System.out.println("结果为" + bills.toString());
        System.out.println("大小为" + bills.size());

        String pathname = "C:\\exportExcel\\单据导出.xls";
        ExportUtil.exportBillSomeFieldDataInLocal(bills, workbook, pathname);
    }

    /**
     * 单据明细
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDetailData() throws IOException {
        List<BillDetail> billDetails = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(BILL_URL, getPageInfoParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(BILL_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carNumber = element.get("carNumber").asText();
                    String totalAmount = element.get("totalAmount").asText();
                    String dateAdded = element.get("orderDate").asText();
                    String billNo = element.get("orderCode").asText();

                    Iterator<JsonNode> details = element.get("orderItemInfo").iterator();
                    while (details.hasNext()) {
                        JsonNode e = details.next();

                        String itemName = e.get("itemName").asText();
                        String salePrice = e.get("itemPrice").asText();//原价
                        String price = e.get("dealPrice").asText();//折扣价
                        String quantity = e.get("quantity").asText();
                        String itemType = "";

                        if (e.get("serviceInfo") != null)
                            itemType = "服务项";

                        if (e.get("partsInfo") != null) {
                            itemType = "配件";

                            String categoryName = e.get("partsInfo").get("categoryName").asText();
                            String brandName = e.get("partsInfo").get("brandName").asText();
                            String partsName = e.get("partsInfo").get("partsName").asText();

                            itemName = categoryName + "-" + brandName + "-" + partsName;
                        }


                        BillDetail billDetail = new BillDetail();
                        billDetail.setBillNo(billNo);
                        billDetail.setCarNumber(carNumber);
                        billDetail.setDateAdded(dateAdded);
                        billDetail.setTotalAmount(totalAmount);
                        billDetail.setItemName(itemName);
                        billDetail.setPrice(price);
                        billDetail.setSalePrice(salePrice);
                        billDetail.setQuantity(quantity);
                        billDetail.setItemType(itemType);
                        billDetails.add(billDetail);
                    }
                }
            }
        }

        System.out.println("结果为" + billDetails.toString());
        System.out.println("大小为" + billDetails.size());

        String pathname = "C:\\exportExcel\\单据明细导出.xls";
        ExportUtil.exportBillDetailSomeFieldDataInLocal(billDetails, workbook, pathname);
    }


    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Map<String, MemberCard> memberCardMap = new HashMap<>();

        Response response = ConnectionUtil.doGetWithLeastParams(MEMBERCARDOVERVIEW_URL + companyId, COOKIE);
        String html = response.returnContent().asString(charset);
        Document doc = Jsoup.parse(html);

        int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, trName);
        if (trSize > 0) {
            for (int i = 1; i <= trSize; i++) {

                String gradeIdRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(6) > a";
                String gradeRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(2)";
                String discountRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(3)";
                String remarkRegEx = "#content-tbody > tr:nth-child({no}) > td.ruleContent";

                String gradeId = doc.select(StringUtils.replace(gradeIdRegEx, "{no}", String.valueOf(i))).attr("gradeid");
                String grade = doc.select(StringUtils.replace(gradeRegEx, "{no}", String.valueOf(i))).text();
                String discount = doc.select(StringUtils.replace(discountRegEx, "{no}", String.valueOf(i))).text();
                String remark = doc.select(StringUtils.replace(remarkRegEx, "{no}", String.valueOf(i))).text();

                MemberCard memberCard = new MemberCard();
                memberCard.setGrade(grade);
                memberCard.setDiscount(discount);
                memberCard.setRemark(remark);
                memberCardMap.put(gradeId, memberCard);
            }
        }

        for (String gradeId : memberCardMap.keySet()) {
            MemberCard memberCard = memberCardMap.get(gradeId);

            Response r = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getMemberCardParams("1", gradeId), COOKIE);
            String content = r.returnContent().asString(charset);
            doc = Jsoup.parse(content);

            String regEx = "[\\s\\S]*var pageNo[\\s\\S]*var totalPage = (\\d+)[\\s\\S]*";
            String totalStr = CommonUtil.filterString(doc.toString(), regEx);
            int total = Integer.parseInt(totalStr);

            if (total > 0) {
                for (int i = 1; i <= total; i++) {
                    Response res = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getMemberCardParams(String.valueOf(i), gradeId), COOKIE);
                    String page = res.returnContent().asString(charset);
                    doc = Jsoup.parse(page);

                    trSize = WebClientUtil.getTagSize(doc, trItemRegEx, trName);
                    if (trSize > 0) {

                        for (int j = 1; j <= trSize; j++) {

                            String cardCodeRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(3)";
                            String nameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(2)";
                            String balanceRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(4)";

                            String cardCode = doc.select(StringUtils.replace(cardCodeRegEx, "{no}", String.valueOf(j))).text();
                            String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                            String balance = doc.select(StringUtils.replace(balanceRegEx, "{no}", String.valueOf(j))).text();

                            MemberCard m = new MemberCard();
                            m.setCardCode(cardCode);
                            m.setName(name);
                            m.setPhone(cardCode);
                            m.setBalance(balance);
                            m.setGrade(memberCard.getGrade());
                            m.setDiscount(memberCard.getDiscount());
                            m.setRemark(memberCard.getRemark());
                            memberCards.add(m);
                        }
                    }
                }
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("大小为" + memberCards.size());

        String pathname = "C:\\exportExcel\\会员卡导出.xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, workbook, pathname);
    }

    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockData() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        Set<String> guids = new HashSet<>();
        Map<String, String> specificationGuids = new HashMap<>();

        Response response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getStockParams("1"), COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalRegEx).replace("totalPage=", "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getStockParams(String.valueOf(i)), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, trName);
                for (int j = 1; j <= trSize; j++) {

                    String getGUIDRegEx = "(?<=').*(?=')";
                    String partsGuidRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(9) > div > span.common-font.edit-act";
                    String partsGuid = doc.select(StringUtils.replace(partsGuidRegEx, "{no}", String.valueOf(i))).attr("onclick");
                    String guid = CommonUtil.fetchString(partsGuid, getGUIDRegEx);
                    guids.add(guid);
                }
            }
        }

        if (guids.size() > 0) {
            for (String guid : guids) {
                response = ConnectionUtil.doPostWithLeastParams(STOCKINDIV_URL, getStockDetailParams(guid), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String optionRegEx = "#specification_search_in > option";
                int optionSize = getOptionSize(doc, optionRegEx);

                if (optionSize > 0) {
                    for (int i = 2; i <= optionSize; i++) {
                        String specRegEx = "#specification_search_in > option:nth-child({no})";
                        String spec = doc.select(StringUtils.replace(specRegEx, "{no}", String.valueOf(i))).text();
                        String specValue = doc.select(StringUtils.replace(specRegEx, "{no}", String.valueOf(i))).attr("value");

                        specificationGuids.put(spec, specValue);
                    }
                }
            }
        }

        if (guids.size() > 0) {
            for (String guid : guids) {
                response = ConnectionUtil.doPostWithLeastParams(STOCKDETAIL_URL, getStockDetailParams(guid), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String firstCategoryNameRegEx = "#mainDiv > div:nth-child(9) > div:nth-child(2) > table > tbody > tr:nth-child(1) > td:nth-child(1) > div:nth-child(2)";
                String brandRegEx = "#mainDiv > div:nth-child(9) > div:nth-child(2) > table > tbody > tr:nth-child(1) > td:nth-child(2) > div:nth-child(2)";
                String remarkRegEx = "#mainDiv > div:nth-child(9) > div:nth-child(2) > table > tbody > tr:nth-child(2) > td:nth-child(1) > div:nth-child(2)";

                String firstCategoryName = doc.select(firstCategoryNameRegEx).text();
                String brand = doc.select(brandRegEx).text();
                String remark = doc.select(remarkRegEx).text();//配件型号

                String trRegEx = "#set-tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, trName);
                for (int j = 1; j <= trSize; j++) {

                    String specRegEx = "#set-tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String inventoryNumRegEx = "#set-tbody > tr:nth-child({no}) > td:nth-child(5)";
                    String salePriceRegEx = "#set-tbody > tr:nth-child({no}) > td:nth-child(2)";

                    String spec = doc.select(StringUtils.replace(specRegEx, "{no}", String.valueOf(j))).text();
                    String inventoryNum = doc.select(StringUtils.replace(inventoryNumRegEx, "{no}", String.valueOf(j))).text();
                    String salePrice = doc.select(StringUtils.replace(salePriceRegEx, "{no}", String.valueOf(j))).text();

                    //拼接商品名称
                    String goodsName = firstCategoryName + brand + remark + spec;

                    Stock stock = new Stock();
                    stock.setGoodsName(goodsName);
                    stock.setFirstCategoryName(firstCategoryName);
                    stock.setBrand(brand);
                    stock.setRemark(remark);
                    stock.setSpec(spec);
                    stock.setInventoryNum(inventoryNum);
                    stock.setSalePrice(salePrice.replace("￥", ""));
                    stock.setPartsGuid(guid);
                    stocks.add(stock);
                }
            }
        }

        if (stocks.size() > 0) {
            for (Stock stock : stocks) {
                String spec = stock.getSpec();
                String partsGuid = stock.getPartsGuid();
                String specificationGuid = specificationGuids.get(spec);

                if (specificationGuid != null) {
                    response = ConnectionUtil.doPostWithLeastParams(STOCKINSEARCH_URL, getStockInPriceParams(partsGuid, specificationGuid), COOKIE);
                    html = response.returnContent().asString();
                    doc = Jsoup.parse(html);

                    //取第一条入库记录中的成本价
                    String priceRegEx = "#content-tbody > tr:nth-child(1) > td:nth-child(7)";
                    String price = doc.select(priceRegEx).text().replace("￥", "");
                    stock.setPrice(price);
                }
            }
        }

        System.out.println("结果为" + stocks.toString());
        System.out.println("大小为" + stocks.size());

        String pathname = "C:\\exportExcel\\元乐车宝库存导出.xls";
        ExportUtil.exportYuanLeCheBaoStockDataInLocal(stocks, workbook, pathname);
    }

    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getServiceParams("1", ""), COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalRegEx).replace("totalPage=", "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, trName);
                for (int j = 1; j <= trSize; j++) {
                    String codeRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String productNameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(2)";
                    String priceRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(3)";
                    String firstCategoryNameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(4)";

                    Product product = new Product();
                    product.setCode(doc.select(StringUtils.replace(codeRegEx, "{no}", String.valueOf(j))).text());
                    product.setProductName(doc.select(StringUtils.replace(productNameRegEx, "{no}", String.valueOf(j))).text());
                    product.setPrice(doc.select(StringUtils.replace(priceRegEx, "{no}", String.valueOf(j))).text().replace("￥", ""));
                    product.setFirstCategoryName(doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(j))).text());
                    products.add(product);
                }
            }
        }
        System.out.println("结果为" + totalPage);
        System.out.println("结果为" + products.toString());
        System.out.println("大小为" + products.size());


        String pathname = "C:\\exportExcel\\元乐车宝服务项目导出.xls";
        ExportUtil.exportProductDataInLocal(products, workbook, pathname);
    }

    private List<BasicNameValuePair> getDetailParams(String name, String value) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(name, value));
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("shopBranchId", shopBranchId));

        return params;
    }

    private List<BasicNameValuePair> getMemberCardParams(String pageNo, String gradeId) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("shopGradeId", gradeId));
        params.add(new BasicNameValuePair("keyWord", ""));
        params.add(new BasicNameValuePair("shopBranchId", shopBranchId));
        params.add(new BasicNameValuePair("staffId", "2341"));

        return params;
    }

    private List<BasicNameValuePair> getStockInPriceParams(String partsGuid, String specificationGuid) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("branchId", "299"));
        params.add(new BasicNameValuePair("staffId", "4574"));
        params.add(new BasicNameValuePair("beginTimeStr", ""));
        params.add(new BasicNameValuePair("endTimeStr", ""));
        params.add(new BasicNameValuePair("partsProperty", ""));
        params.add(new BasicNameValuePair("pageNo", "1"));
        params.add(new BasicNameValuePair("pageSize", "5"));
        params.add(new BasicNameValuePair("orderBy", "-1"));
        params.add(new BasicNameValuePair("specificationGuid", specificationGuid));
        params.add(new BasicNameValuePair("partsGuid", partsGuid));

        return params;
    }

    private List<BasicNameValuePair> getStockDetailParams(String partsGuid) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("partsGuid", partsGuid));

        return params;
    }

    private List<BasicNameValuePair> getStockParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));

        return params;
    }

    private List<BasicNameValuePair> getServiceParams(String pageNo, String keyword) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("keyword", keyword));
        params.add(new BasicNameValuePair("categoryId", ""));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        return params;
    }

    private List<BasicNameValuePair> getCarInfoDetailParams(String userName, String userId, String carArea, String carNum, String mobile) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("staffId", "1649"));
        params.add(new BasicNameValuePair("shopBranchId", shopBranchId));
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("carArea", carArea));
        params.add(new BasicNameValuePair("carNum", carNum));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("pageType", "2"));
        return params;
    }

    private List<BasicNameValuePair> getPageInfoParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopBranchId", shopBranchId));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        params.add(new BasicNameValuePair("staffId", "4478"));
        return params;
    }

    private String getDiscount(String salePrice, String price) {
        int sp = Integer.parseInt(salePrice);
        int p = Integer.parseInt(price);
        DecimalFormat df = new DecimalFormat("0.00");

        String res = df.format((float) p / (float) sp);

        return res;
    }

    private int getOptionSize(Document document, String optionRegEx) {
        int optionSize = document.select(optionRegEx).tagName("option").size();

        return optionSize > 0 ? optionSize : 0;
    }

    private String getMemberCardItemURL(String userId, String packageId) {
        String url = "http://www.carbao.vip/Home/receptionService/recordCardDetail?" +
                "userId=" + userId +
                "&packageId=" + packageId +
                "&shopBranchId=" + shopBranchId +
                "&shopId=" + companyId;
        return url;
    }


    private int getTotalPage(String url, List<BasicNameValuePair> params) throws IOException {
        Response response = ConnectionUtil.doPostWithLeastParams(url, params, COOKIE);
        String html = response.returnContent().asString(charset);
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalPageRegEx).replace("totalPage = ", "");
        String a = "";
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        return totalPage;
    }

    /**
     * 获取可用套卡不为0的会员数量
     *
     * @return
     * @throws IOException
     */
    private Map<String, MemberCard> getMemberHasPackage() throws IOException {
        Map<String, MemberCard> memberCardMap = new HashMap<>();

        int totalPage = getTotalPage(CLIENTLIST_URL, getPageInfoParams("1"));
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(CLIENTLIST_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, trName);
                if (trSize > 0) {
                    for (int j = 1; j <= 10; j++) {
                        String nameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(2)";
                        String phoneRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(3)";
                        String memberCardNameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(6)";
                        String clientRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(9) > a:nth-child(1)";
                        String balanceRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(4)";
                        String hasPackageRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(5)";

                        String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                        String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", String.valueOf(j))).text();
                        String memberCardName = doc.select(StringUtils.replace(memberCardNameRegEx, "{no}", String.valueOf(j))).text();
                        String balance = doc.select(StringUtils.replace(balanceRegEx, "{no}", String.valueOf(j))).text();
                        String userId = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("userid");
                        String hasPackage = doc.select(StringUtils.replace(hasPackageRegEx, "{no}", String.valueOf(j))).text();
                        String cardSort = String.valueOf(random.nextInt());

                        if ("0".equals(hasPackage) && "0.0".equals(balance))
                            continue;

                        if ("-".equals(memberCardName) || StringUtils.isBlank(memberCardName))
                            memberCardName = "普通会员卡";

                        MemberCard memberCard = new MemberCard();
                        memberCard.setCardCode(phone);//手机号作为卡号
                        memberCard.setName(name);
                        memberCard.setPhone(phone);
                        memberCard.setMemberCardName(memberCardName);
                        memberCard.setBalance(balance);
                        memberCard.setUserId(userId);
                        memberCard.setCardSort(cardSort.replace("-", ""));
                        memberCardMap.put(userId, memberCard);
                    }
                }
            }
        }

        return memberCardMap;
    }

}
