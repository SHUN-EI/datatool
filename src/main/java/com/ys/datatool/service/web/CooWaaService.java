package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Bill;
import com.ys.datatool.util.ConnectionUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/10/15.
 * 酷蛙快修
 */
@Service
public class CooWaaService {

    private String BILL_URL = "https://shops.coowaa.cn/Modules/SalesOrder/SalesOrderMethods.aspx/SearchWorkOrderList";

    private String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String companyName = "酷蛙快修";

    private String COOKIE = "rememberServicePad=userid=18218754669; ASP.NET_SessionId=y5gujmd3hpokmqbrvf4ciwjs; userid=10423; .democoowaashops=56EF0B422C3BC61902E4BC6E574486E17757B6C0BB5AB9CDA2E069838B8CB5921DF274BD5346040775FD30547DEBC5BE5667351CABD9D641BFF72C8F55E93DC584FA8C23BD81ADF0EEE7B11BA9232BC9C5D5C6A44AEDB48FB7A514FBCE918A1E4B28B538E85D4E1CF51A22305C605661BAB0A03A21622B08AF1DD0BCE70621968E32D73F230E1F774B00569909F483D1";

    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(0), COOKIE, CONTENT_TYPE);
        int totalPage = getTotalPage(response, 50);

        if (totalPage > 0) {

        }

        String abc = "";


    }

    private int getTotalPage(Response response, int num) throws IOException {
        int totalPage = 0;

        String returnContent = response.returnContent().asString();
        String startRegEx = "total";
        String endRegEx = "rows";
        int start = returnContent.indexOf(startRegEx);
        int end = returnContent.indexOf(endRegEx);

        String totalStr = returnContent.substring(start + 9, end - 3);
        int total = Integer.parseInt(totalStr);

        if (total % num == 0) {
            totalPage = total / num;
        } else
            totalPage = total / num + 1;

        return totalPage;

    }

    private String getBillParam(int pageNo) {

        String param = "{" +
                "\"paraname\":[\"is_delete\",\"work_order_no\",\"plate_number\",\"customer_name\",\"reception_date\",\"reception_date\",\"settlement_time\",\"settlement_time\"]," +
                "\"expression\":[\"=\",\"~*\",\"~*\",\"~*\",\">=\",\"<\",\">=\",\"<=\"]," +
                "\"paravalue\":" + "[\"0\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]," +
                "\"pagesize\":" + 50 + "," +
                "\"pageindex\":" + pageNo +
                "}";

        return param;
    }
}
