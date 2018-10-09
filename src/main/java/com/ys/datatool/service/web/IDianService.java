package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.DateUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by mo on  2018/8/11.
 * I店系统
 */

@Service
public class IDianService {

    private String CARINFO_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=member_customer_query&page=0&pageSize=50&option=";

    private String MEMBERCARDITEM_URL = "http://app.idianchina.com:8082/api/vip/member/get-member-detail";

    private String MEMBERCARD_URL = "http://app.idianchina.com:8082/api/vip/member/query";

    private String fromDate = "2003-01-01";

    private String toDate = "2018-09-28";

    private String BILL_URL = "http://www.idsz.xin:7070/posapi_invoke" +
            "?apiname=saleorder_queryallfilter_new&" +
            "fromDate=" +
            fromDate +
            "&toDate=" +
            toDate +
            "&licensePlate=&userPhone=&billStatus=0&tpyes=0&orderTypes=0&rows=50&page=";

    private String CONSUMPTIONRECORD_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=sale_opensale_vieworderinfo_new";

    private String ACCEPT_ENCODING = "gzip, deflate, sdch";

    private String ACCEPT = "application/json, text/javascript, */*; q=0.01";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Charset charset = Charset.forName("utf-8");

    private String companyName = "I店";

    private String COOKIE = "JSESSIONID=42FC878D2B5EBD31B99C8523EFD2573D";


    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        Response res = ConnectionUtil.doGetEncode(BILL_URL + "1", COOKIE, ACCEPT_ENCODING, ACCEPT);
        JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
        String totalStr = result.get("total").asText();
        int total = Integer.parseInt(totalStr);

        /**
         * 单据状态tpyes:全部状态-0,已开单-1,已结算-2,已提车-3,已取消-4
         * billStatus  已开单-10,已结算-20,已提车-30,已取消-40
         *
         */

        if (total > 0) {
            for (int i = 1; i <= 10; i++) {
                res = ConnectionUtil.doGetEncode(BILL_URL + String.valueOf(i), COOKIE, ACCEPT_ENCODING, ACCEPT);
                JsonNode content = MAPPER.readTree(res.returnContent().asString(charset));

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

                Response response = ConnectionUtil.doPostEncode(CONSUMPTIONRECORD_URL, params, COOKIE, ACCEPT_ENCODING, ACCEPT);
                JsonNode content = MAPPER.readTree(response.returnContent().asString(charset));

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
        ExportUtil.exportConsumptionRecordDataInLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname2);

    }

    /**
     * 单据
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response res = ConnectionUtil.doGetEncode(BILL_URL + "1", COOKIE, ACCEPT_ENCODING, ACCEPT);
        JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
        String totalStr = result.get("total").asText();
        int total = Integer.parseInt(totalStr);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                res = ConnectionUtil.doGetEncode(BILL_URL + String.valueOf(i), COOKIE, ACCEPT_ENCODING, ACCEPT);
                JsonNode content = MAPPER.readTree(res.returnContent().asString(charset));

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

        System.out.println("结果为" + bills.toString());
        System.out.println("结果为" + bills.size());

        String pathname = "C:\\exportExcel\\i店单据.xlsx";
        ExportUtil.exportBillDataInLocal(bills, ExcelDatas.workbook, pathname);

    }

    /**
     * 车辆信息
     * 需要获取全部车牌号码，此方法需要读取所有车牌的excel
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        List<String> carNumbers = new ArrayList<>();

        File file = new File("C:\\exportExcel\\icard.xls");
        FileInputStream in = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(in);

        HSSFSheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();

        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = firstRowNum; i <= lastRowNum; i++) {
            row = sheet.getRow(i);//取得第i行
            cell = row.getCell(0);//取得i行的第一列
            String cellValue = cell.getStringCellValue().trim();
            carNumbers.add(cellValue);
        }


        if (carNumbers.size() > 0) {
            for (String carNumber : carNumbers) {
                Response res = ConnectionUtil.doGetEncode(CARINFO_URL + URLEncoder.encode(carNumber, "utf-8"), COOKIE, ACCEPT_ENCODING, ACCEPT);
                JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));

                JsonNode userObject = result.get("userObject");
                Iterator<JsonNode> it = userObject.iterator();

                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String name = element.get("name").asText();
                    String phone = element.get("userPhone").asText();
                    String brand = element.get("carBrandName").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCompanyName(companyName);
                    carInfo.setName(name);
                    carInfo.setPhone(phone);
                    carInfo.setBrand(brand);
                    carInfo.setCarNumber(carNumber);
                    carInfos.add(carInfo);
                }
            }
        }

        String pathname = "C:\\exportExcel\\i店会员车辆.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);

    }

    /**
     * 卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        for (int i = 0; i <= 21; i++) {
            Response res = ConnectionUtil.doPostWithLeastParamJsonInPhone(MEMBERCARD_URL, getMemberCardParams(String.valueOf(i)), COOKIE);

            JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
            JsonNode userObject = result.get("userObject");
            JsonNode memberList = userObject.get("memberList");

            if (!"".equals(memberList.toString())) {
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
                Response res = ConnectionUtil.doPostWithLeastParamJsonInPhone(MEMBERCARDITEM_URL, getMemberCardItemParams(id), COOKIE);

                JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
                JsonNode userObject = result.get("userObject");

                Iterator<JsonNode> it = userObject.get("timesDetailList").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String validTime = element.get("validityTime").asText();
                    String isValidForever = CommonUtil.getIsValidForever(validTime);

                    JsonNode goodsList = element.get("goodsList");
                    if (!"".equals(goodsList.toString())) {
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

        System.out.println("结果为" + ids.size());

        String pathname = "C:\\exportExcel\\i店卡内项目.xlsx";
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

        //String params = "MEID=1F3F7042-675B-4BAD-BE11-448A267326F0&deviceType=2&format=json&keyword=&memberLevelId=&sign=1B0BC2BC981BF781DDB9D55FAA886D3E&token=A19873FDF327F6D7F14A8110513DB9F7&user_phone=18934388886&versionCode=507&versionName=5.0.7&currentPageIndex=";
        for (int i = 0; i <= 21; i++) {
            Response res = ConnectionUtil.doPostWithLeastParamJsonInPhone(MEMBERCARD_URL, getMemberCardParams(String.valueOf(i)), COOKIE);

            JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
            JsonNode userObject = result.get("userObject");

            Iterator<JsonNode> it = userObject.get("memberList").iterator();
            while (it.hasNext()) {
                JsonNode element = it.next();

                String cardCode = element.get("memberId").asText();
                String memberCardName = element.get("memberLevelName").asText();
                String carNumber = element.get("licensePlate").asText();
                String balance = element.get("amount").asText();
                String dateCreated = element.get("openCardTime").asText();

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

        String pathname = "C:\\exportExcel\\i店会员卡.xlsx";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }


    private List<BasicNameValuePair> getMemberCardItemParams(String id) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("MEID", "1F3F7042-675B-4BAD-BE11-448A267326F0"));
        params.add(new BasicNameValuePair("deviceType", "2"));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("sign", "832C4564B8250546FB74DA4E11365984"));
        params.add(new BasicNameValuePair("token", "A19873FDF327F6D7F14A8110513DB9F7"));
        params.add(new BasicNameValuePair("user_phone", "18934388886"));
        params.add(new BasicNameValuePair("versionCode", "507"));
        params.add(new BasicNameValuePair("versionName", "5.0.7"));
        params.add(new BasicNameValuePair("memberId", id));
        return params;
    }

    private List<BasicNameValuePair> getMemberCardParams(String index) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("MEID", "1F3F7042-675B-4BAD-BE11-448A267326F0"));
        params.add(new BasicNameValuePair("deviceType", "2"));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("keyword", ""));
        params.add(new BasicNameValuePair("memberLevelId", ""));
        params.add(new BasicNameValuePair("sign", "1B0BC2BC981BF781DDB9D55FAA886D3E"));
        params.add(new BasicNameValuePair("token", "A19873FDF327F6D7F14A8110513DB9F7"));
        params.add(new BasicNameValuePair("user_phone", "18934388886"));
        params.add(new BasicNameValuePair("versionCode", "507"));
        params.add(new BasicNameValuePair("versionName", "5.0.7"));
        params.add(new BasicNameValuePair("currentPageIndex", index));
        return params;
    }


}
