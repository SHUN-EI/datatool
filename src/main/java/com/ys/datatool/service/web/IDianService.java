package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Bill;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.http.client.fluent.Response;
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

    private String BILL_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=saleorder_queryallfilter_new&fromDate=2010-01-01&toDate=2018-08-12&licensePlate=&userPhone=&billStatus=0&tpyes=0&orderTypes=0&rows=50&page=";

    private String CARINFO_URL = "http://www.idsz.xin:7070/posapi_invoke?apiname=member_customer_query&option=&page=1&pageSize=50";

    private String COOKIE = "JSESSIONID=3B77227B11C658B464D19C35FA810B40";

    private String CONTENT_TYPE = "";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Charset charset = Charset.forName("utf-8");

    private String companyName = "I店";


    @Test
    public void fetchBillDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response res = ConnectionUtil.doGetWithLeastParams2(BILL_URL + "1",CONTENT_TYPE, COOKIE);
        JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
        String totalStr = result.get("total").asText();
        int total = Integer.parseInt(totalStr);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                res = ConnectionUtil.doGetWithLeastParams2(BILL_URL + String.valueOf(i),CONTENT_TYPE, COOKIE);
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


}
