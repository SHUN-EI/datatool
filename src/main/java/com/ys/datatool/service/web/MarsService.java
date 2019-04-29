package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.entity.Supplier;
import com.ys.datatool.util.ConnectionUtil;
import org.apache.http.client.fluent.Response;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018-06-08.
 * Mars系统
 */

@Service
public class MarsService {


    private String URL = "https://mars.tyreplus.com.cn/MARS/WebClient/cbs";

    private String CONTENT_TYPE = "application/json;charset=UTF-8";

    private String COOKIE = "SessionId=le4utoy4igkcx2puwk3q3y1e; .ASPXAUTH=3C565734F1952F3943E4F9D280AB398782F1DE0B8EDEB94B42A9DF6164818F4DE7523F1D899CC895C205829DEABF54AC4AEA386C47A76E016D75A1B31DE687C445280D46D3FC6CE54B27BA08F29C4ADB95A54A29B7B4CA2E68B0BC5F7ED3447DA21B4D47D2399BAE1731B5018AB41CEA2D9B81B10AB6BA344F32FFE422EE281A; 4C633A=1536759|-1|1156|0; 24ECBC=1536759|-1|1156|0; 24EFF1=1536759|-1|1243|0; 48EF0A=1536759|-1|1156|0; 49451C=1536759|-1|1156|0; 49457E=1536759|-1|1156|0; 4945E4=1536759|-1|1156|0; 4950A3=1536759|-1|1214|0; 495815=1536759|-1|1214|0; 3BA0CD=1536759|-1|1214|0; 1A4AA=1536759|-1|1156|0; 1A528=1536759|-1|1214|0; 60DFD=1536759|-1|1231|0; 61C56=1536759|-1|1231|0; 65527=1536759|-1|1156|0; 656CF=1536759|-1|1231|0; 665E9=1536759|-1|1231|0; .ASPXAUTH=9BE759848EC8C8B94F11538FBD9DE50E8C6C8F17C5CB33D9CAD54C81A94981A85974FE8A7CEF206A3F1E45C6ED41CAF14CF1EF18B6B1084C501362A1EC1FE62C1F631AE011C75C445D7A6EA96BA979E98A39FC6744FAFCD0718D2CEEFA92FA341EA7A44D08348D3C5AB05877AAE29B4CC2E83A4B1EFB81B39A5BF542E634EDE4";

    private Workbook workbook;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        String param = "{\"jsonrpc\":\"2.0\",\"method\":\"Invoke\",\"params\":[{\"openFormIds\":[\"318C79\",\"318AEC\",\"3180F4\"],\"interactionsToInvoke\":[{\"namedParameters\":\"{\\\"Delta\\\":1}\",\"interactionName\":\"ScrollRepeater\",\"controlId\":\"318C7D\",\"formId\":\"318C79\",\"callbackId\":\"7\"}],\"sessionId\":\"le4utoy4igkcx2puwk3q3y1eR5DEALER135812Tyreplus 135812User\",\"requestToken\":\"dab0rnzjciprm02twthlgocg\",\"sequenceNo\":\"jibeye9i#7\",\"navigationContext\":{\"location\":\"https://mars.tyreplus.com.cn/Mars/WebClient/?company=Tyreplus%20135812&bookmark=41%3bFwAAAAJ7%2f1YARAAxADMANQA4ADEAMgAvADEAMAAwADAAOAA3&node=e34ce138-75b7-4118-9255-92b02ca59213&mode=Edit&page=27&filter=Vendor.%27Service%20Center%27%20IS%20%27135812%27&spa=1&dc=0&inapp=false&i=318C79&tenant=r5dealer135812&IsDlg=1\",\"isDialog\":true,\"isSpa\":true,\"spaInstanceId\":\"jibeye17\",\"isInApp\":false,\"deviceCategory\":0,\"nativePageType\":\"ListApplicationPage\"}}],\"id\":1}";

        Response response = ConnectionUtil.doPostWithLeastParamJson(URL, param, COOKIE,CONTENT_TYPE);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());

        JsonNode res = result.get("result").get("parameters");
        Iterator<JsonNode> it =res.iterator();
        while (it.hasNext()) {

            JsonNode element = it.next();
        }

        System.out.println("结果为" + result);


    }


}
