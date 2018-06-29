package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.TimeUtil;
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

    //客户信息页面方法传参
    private String clientInfoMethod = "1239";

    //车辆信息页面方法传参
    private String carInfoMethod = "1912";

    //供应商页面总页数
    private int supplierPageNum = 3;

    //车辆信息页面总页数
    private int carPageNum = 145;

    //会员卡页面总页数
    private int memberCardPageNum = 6;

    private String COOKIE = "JSESSIONID=4AC7D9310FC4C688C08F57EDCB1993CF; Authorization=eyJhbGciOiJIUzUxMiJ9.eyJpZCI6IjZhNTRmYjQwLWVlZjEtNDAxZS04ZThiLWE0NGY5OWI3MjNlZSIsImV4cCI6MTUyODAxMTk2MywibmJmIjoxNTI3OTI1NTYzLCJzdG9yZUlkIjoiOWU2NTA3MmEtNjIyMy00Y2U0LWI1MjAtMGMwZGQzN2IwMzU0IiwidXNlclR5cGUiOiIwIn0.iqF2_o22HnynLtTyu3f60cnUjrmnKkHFfXfoxuebwGR9Xvu5JhDE1PfV0fs6cobo92U4h1Kr15HihP3z5Vei3w; Hm_lvt_678c2a986264dd9650b6a59042718858=1527655868,1527925565; SERVERID=b810ac6d9315e3be005b170045c65755|1527926716|1527925561; Hm_lpvt_678c2a986264dd9650b6a59042718858=1527926716";

    @Test
    public void test() throws IOException {
        Response response = ConnectionUtil.doGetWithLeastParams(MEMBERCARDDETAIL_URL + "bf91c55d-42a0-4814-bdad-b511603c5f8f", COOKIE);
        String html = response.returnContent().asString();
        Document document = Jsoup.parse(html);
        System.out.println("结果为" + document.html());
    }

    /**
     * 会员卡
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

        String pathname = "D:\\掌上车店会员卡信息.xls";
         ExportUtil.exportMemberCardDataInLocal(memberCards, workbook, pathname);

    }

    /**
     * 车辆信息
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        for (int i = 1; i <= carPageNum; i++) {
            Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getParams(carInfoMethod, String.valueOf(i), String.valueOf(30)), COOKIE);

            JsonNode result = MAPPER.readTree(response.returnContent().asString());
            Iterator<JsonNode> it = result.get("data").elements();
            while (it.hasNext()) {
                JsonNode element = it.next();

                CarInfo carInfo = new CarInfo();
                carInfo.setCarNumber(element.get("myCarPlateNo") != null ? element.get("myCarPlateNo").asText() : "");
                carInfo.setCarModel(element.get("carModelName") != null ? element.get("carModelName").asText() : "");
                carInfo.setName(element.get("contactName") != null ? element.get("contactName").asText() : "");
                carInfo.setPhone(element.get("contactTelephone") != null ? element.get("contactTelephone").asText() : "");
                carInfo.setRegisterDate(element.get("registerDate") != null ? TimeUtil.formatMillisecond2DateTime(element.get("registerDate").asText()) : "");
                carInfo.setVINcode(element.get("vehicleIdNumber") != null ? element.get("vehicleIdNumber").asText() : "");
                carInfo.setEngineNumber(element.get("engineNumber") != null ? element.get("engineNumber").asText() : "");
                carInfo.setVcInsuranceValidDate(element.get("insuranceDate") != null ? TimeUtil.formatMillisecond2DateTime(element.get("insuranceDate").asText()) : "");
                carInfos.add(carInfo);
            }
        }
        String pathname = "D:\\掌上车店车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);
    }

    /**
     * 供应商
     * @throws IOException
     */
    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        for (int i = 1; i <= supplierPageNum; i++) {
            Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams(supplierMethod, String.valueOf(i), String.valueOf(30)), COOKIE);

            JsonNode result = MAPPER.readTree(response.returnContent().asString());
            Iterator<JsonNode> it = result.get("data").elements();
            while (it.hasNext()) {
                JsonNode element = it.next();

                Supplier supplier = new Supplier();
                supplier.setName(element.get("supplierName") != null ? element.get("supplierName").asText() : "");
                supplier.setContactName(element.get("linkManName") != null ? element.get("linkManName").asText() : "");
                supplier.setContactPhone(element.get("mobilePhone") != null ? element.get("mobilePhone").asText() : "");
                supplier.setRemark(element.get("supplyType") != null ? element.get("supplyType").asText() : "");
                supplier.setAddress(element.get("address") != null ? element.get("address").asText() : "");
                supplier.setFax(element.get("officePhone") != null ? element.get("officePhone").asText() : "");//"officePhone"
                suppliers.add(supplier);
            }
        }

        String pathname = "D:\\掌上车店供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, workbook, pathname);
    }

    private List<BasicNameValuePair> getMemberCardParams(String method, String cardId) {
        List<BasicNameValuePair> params = new ArrayList<>();

        String value = "{" + "\"memberId\":" + "\"" + cardId + "\"" + "}";

        params.add(new BasicNameValuePair("data", value));
        params.add(new BasicNameValuePair("method", method));
        return params;
    }

    private List<BasicNameValuePair> getParams(String method, String pageNo, String pageSize) {
        List<BasicNameValuePair> params = new ArrayList<>();

        String value = "{" + "\"pageSize\":" + pageSize + "," +
                "\"pageNo\":" + pageNo + "}";

        params.add(new BasicNameValuePair("data", value));
        params.add(new BasicNameValuePair("method", method));
        return params;
    }


}
