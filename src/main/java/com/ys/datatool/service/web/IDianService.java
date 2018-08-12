package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Bill;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on  2018/8/11.
 * I店系统
 */

@Service
public class IDianService {

    private String MEMBERCARD_URL = "http://app.idianchina.com:8082/api/vip/member/query";

    private String BILL_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=saleorder_queryallfilter_new&fromDate=2010-01-01&toDate=2018-08-12&licensePlate=&userPhone=&billStatus=0&tpyes=0&orderTypes=0&rows=50&page=";

    private String CARINFO_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=member_customer_query&option=&page=1&pageSize=50";

    private String COOKIE = "JSESSIONID=B85A189876127F17E5C799D0858E0B6F";

    private String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Charset charset = Charset.forName("utf-8");

    private String companyName = "I店";

    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        //String params = "MEID=1F3F7042-675B-4BAD-BE11-448A267326F0&deviceType=2&format=json&keyword=&memberLevelId=&sign=1B0BC2BC981BF781DDB9D55FAA886D3E&token=A19873FDF327F6D7F14A8110513DB9F7&user_phone=18934388886&versionCode=507&versionName=5.0.7&currentPageIndex=";
        for (int i = 0; i <= 21; i++) {
            Response res = ConnectionUtil.doPostWithLeastParamJsonInPhone(MEMBERCARD_URL,getMemberCardParams(String.valueOf(i)), COOKIE);

            JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
            JsonNode userObject=result.get("userObject");

            Iterator<JsonNode> it =userObject.get("memberList").iterator();
            while (it.hasNext()) {
                JsonNode element = it.next();

                String cardCode=element.get("memberId").asText();
                String carNumber=element.get("licensePlate").asText();
                String balance=element.get("amount").asText();
                String dateCreated=element.get("openCardTime").asText();

                MemberCard memberCard=new MemberCard();
                memberCard.setCardCode(cardCode);
                memberCard.setCarNumber(carNumber);
                memberCard.setBalance(balance);
                memberCard.setDateCreated(dateCreated);
                memberCards.add(memberCard);
            }
        }

        String pathname = "C:\\exportExcel\\i店会员卡.xlsx";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    @Test
    public void fetchBillDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response res = ConnectionUtil.doGetWithLeastParams(BILL_URL + "1", COOKIE);
        JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
        String totalStr = result.get("total").asText();
        int total = Integer.parseInt(totalStr);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                res = ConnectionUtil.doGetWithLeastParams(BILL_URL + String.valueOf(i), COOKIE);
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
