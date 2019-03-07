package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Bill;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.DateUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 2019/3/7
 * <p>
 * 茂日软件
 */
@Service
public class MaoRiService {


    private String fromDate = "2003-01-01";

    private String BILL_URL = "http://new.mrrjvip.com/Consume/QueryBill";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Charset charset = Charset.forName("utf-8");

    private String CONTENT_TYPE = "application/json;charset=UTF-8";

    private String fieldName = "total";

    private String COOKIE = "aliyungf_tc=AQAAAPNnQHnFaAEAmghDcfyQ0m4IPf/4; __RequestVerificationToken=mlGPU5jGMQxN6GoYr1XTzTiNvP8kR8DGJGTe3Ccj_njrk3mJlNLGjf2pl1ZGxYXZmJ6bPCFaUbqxUIQpmiGVU8uU3b0VcjUJB6ouMu9UU9c1; ASP.NET_SessionId=nc01fqyltq5oxlbsbkydpdk5; .ASPXAUTH=DEF3A9C3D7C69FCC6B751D22A725B2E4B839964A171E51CED22A5582C94B5A4EC95056939913D03F4DC54C49FF3F52C61BE148A7F5BA68ECCCCFE2E390DE18595085E57ABA62273114BD9A5CD08341257E6091B75A96FDE81C065BB9168AAF23; SERVERID=6ff2789acc2c6db5bc66d9ec1d77499c|1551936781|1551926873";


    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        String param=getBillParam(0);
        Response response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(0), COOKIE,CONTENT_TYPE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 100);

        //String result = response.returnContent().asString(charset);


        String sss = "";


    }


    private String getBillParam(int offset) {
        String param = "{\"pageSize\":100," +
                "\"offset\":" +
                offset +
                ",\"order\":\"desc\"," +
                "\"sortName\":\"BillCode\"," +
                "\"billQueryType\":\"BillDate\"," +
                "\"BillSource\":\"-1\"," +
                "\"startDate\":\"" +
                fromDate +
                "\"" +
                ",\"endDate\":\"" +
                DateUtil.formatCurrentDate() +
                "\"" +
                ",\"status\":\"-1\"," +
                "\"keyWord\":\"\"}";

        return param;
    }
}
