package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
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

    //供应商页面总页数
    private int supplierPageNum = 3;

    //会员卡页面总页数
    private int memberCardPageNum = 6;

    private String COOKIE = "JSESSIONID=1E3D86B974255068E579460A91579938; Hm_lvt_678c2a986264dd9650b6a59042718858=1531206994; Authorization=eyJhbGciOiJIUzUxMiJ9.eyJpZCI6IjZhNTRmYjQwLWVlZjEtNDAxZS04ZThiLWE0NGY5OWI3MjNlZSIsImV4cCI6MTUzMTYzOTk3MCwibmJmIjoxNTMxNTUzNTcwLCJzdG9yZUlkIjoiOWU2NTA3MmEtNjIyMy00Y2U0LWI1MjAtMGMwZGQzN2IwMzU0IiwidXNlclR5cGUiOiIwIn0.qR6zVPAdjj-eksxFWHKd50N24xhIooBllAGqLZ1CVR3kfU1c0FUlPu7DZAQwK40q-nHROZHemyoLav0u0Ta5Pw; SERVERID=fcc0e5fe0ca1ba074f3fd4818c894192|1531553773|1531553568; Hm_lpvt_678c2a986264dd9650b6a59042718858=1531553772";

    @Test
    public void test() throws IOException {

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

        String pathname = "C:\\exportExcel\\掌上车店供应商导出.xls";
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

        String pathname = "C:\\exportExcel\\掌上车店车辆导出.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);

    }

    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Set<String> cardIds = new HashSet<>();

        for (int i = 1; i <= memberCardPageNum; i++) {
            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("page.no", String.valueOf(i)));

            Response response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, params, COOKIE);
            String html = response.returnContent().asString();
            Document document = Jsoup.parse(html);

            String getMemberCardIdRegEx = "body > div.wrapper > div.contents > div > div.main > table > tbody > tr:nth-child({no}) > td:nth-child(14) > p:nth-child(1) > a";
            for (int j = 1; j <= 16; j++) {
                String cardIdRegEx = StringUtils.replace(getMemberCardIdRegEx, "{no}", j + "");
                String cardId = document.select(cardIdRegEx).attr("data-id");

                if (StringUtils.isNotBlank(cardId))
                    cardIds.add(cardId);
            }
        }

        if (cardIds.size() > 0) {
            for (String cardId : cardIds) {
                Response response = ConnectionUtil.doGetWithLeastParams(MEMBERCARDDETAIL_URL + cardId, COOKIE);

                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String cardCodeRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(3) > div:nth-child(2)";
                String memberCardNameRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(4) > div:nth-child(2)";
                String carNumberRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(4) > div:nth-child(4)";
                String nameRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(5) > div:nth-child(2)";
                String phoneRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(5) > div:nth-child(6)";
                String balanceRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(6) > div:nth-child(2) > b";
                String dateCreatedRegEx = "body > div.wrapper > div.contents > div > div.main > div.select-title > div.input-item > div:nth-child(7) > div:nth-child(2)";

                MemberCard memberCard = new MemberCard();
                memberCard.setCardCode(doc.select(cardCodeRegEx).text());
                memberCard.setMemberCardName(doc.select(memberCardNameRegEx).text());
                memberCard.setCarNumber(doc.select(carNumberRegEx).text());
                memberCard.setName(doc.select(nameRegEx).text());
                memberCard.setPhone(doc.select(phoneRegEx).text());
                memberCard.setBalance(doc.select(balanceRegEx).text());
                memberCard.setDateCreated(doc.select(dateCreatedRegEx).text());
                memberCards.add(memberCard);

            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("大小为" + memberCards.size());

        String pathname = "C:\\exportExcel\\掌上车店会员卡信息.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, workbook, pathname);

    }

    private List<BasicNameValuePair> getMemberCardParams(String method, String cardId) {
        List<BasicNameValuePair> params = new ArrayList<>();

        String value = "{" + "\"memberId\":" + "\"" + cardId + "\"" + "}";

        params.add(new BasicNameValuePair("data", value));
        params.add(new BasicNameValuePair("method", method));
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
