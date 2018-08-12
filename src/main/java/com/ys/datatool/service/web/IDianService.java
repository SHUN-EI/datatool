package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Bill;
import com.ys.datatool.util.ConnectionUtil;
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

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Charset charset = Charset.forName("UTF-8");


    @Test
    public void fetchBillDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();


        Response res = ConnectionUtil.doGetWithLeastParams(BILL_URL+"1", COOKIE);
        JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));
        String totalStr=result.get("total").asText();
        int total=Integer.parseInt(totalStr);

        if(total>0){
            for (int i=1;i<=2;i++){
                res= ConnectionUtil.doGetWithLeastParams(BILL_URL+String.valueOf(i), COOKIE);
                JsonNode content = MAPPER.readTree(res.returnContent().asString(charset));

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();


                }
            }
        }

        System.out.println("结果为"+total);



    }


}
