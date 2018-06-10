package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.ConnectionUtil;
import org.apache.http.client.fluent.Response;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018-06-08.
 * Mars系统
 */

@Service
public class MarsService {


    private String URL = "https://mars.tyreplus.com.cn/MARS/WebClient/cbs";

    private String COOKIE = "SessionId=lugygb1app4a2rxyhhczw1jz; .ASPXAUTH=3960B2499FA184F3FD5FC67A4FD21BBFEFF071B56ED257B249E536D9ED929AD20321A0E09B468BFF889690A82C63C516D69C4969A3511D474B201DE3701959502524B5F40062DCC41F515476A767DAF4D0A805B0946EDAB21EFB3CA243A79D1757676492273935514EB27CD41D1CC330CE111DA55CEA3730CFD40BCDF6D3C5BD; 4C633A=1536759|-1|1156|0; 24ECBC=1536759|-1|1156|0; 24EFF1=1536759|-1|1243|0; .ASPXAUTH=9362661E3D3D6C07EA6B1C3D0F225DBAC520B9DED49CD7279A3DE16022AA76BAF76AE752FA3EA3AA7C2385A4F5EB07384F240E4B8D6F6FD0166854178229AD770966004B0F39D1E7FBC9D325EECFA43763F6E680AA58368BC473283B69D72DAA4578F062C5D02721C26C146CF37ACB06D6938DC91F0789C94FEF91CC40549AF5; 48EF0A=1536759|-1|1156|0; 49451C=1536759|-1|1156|0; 49457E=1536759|-1|1156|0; 4945E4=1536759|-1|1156|0; 4950A3=1536759|-1|1214|0; 495815=1536759|-1|1214|0; 3BA0CD=1536759|-1|1214|0; 1A4AA=1536759|-1|1156|0; 1A528=1536759|-1|1214|0; 60DFD=1536759|-1|1231|0; 61C56=1536759|-1|1231|0; 65527=1536759|-1|1156|0; 656CF=1536759|-1|1231|0; 665E9=1536759|-1|1231|0";

    private Workbook workbook;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        String param = "{\"jsonrpc\":\"2.0\",\"method\":\"Invoke\",\"params\":[{\"openFormIds\":[\"104E29\"],\"interactionsToInvoke\":[{\"namedParameters\":\"{\\\"NodeId\\\":\\\"8c433257-a286-4e5f-bdc4-3fde5b0ba3c4\\\"}\",\"interactionName\":\"Navigate\",\"controlId\":\"104E2A\",\"formId\":\"104E29\",\"callbackId\":\"6\"}],\"sessionId\":\"lugygb1app4a2rxyhhczw1jzR5DEALER135812Tyreplus 135812User\",\"requestToken\":\"rba0n0vdsm5pekapw0hxhyhj\",\"sequenceNo\":\"ji5tdzl2#6\",\"navigationContext\":{\"location\":\"https://mars.tyreplus.com.cn/MARS/WebClient/?company=Tyreplus%20135812&bookmark=17%3bDQAAAAJ7AzUANwAx&node=956d575b-4c5e-4bba-bcca-7fd69052857d&mode=View&page=14&spa=1&dc=0&inapp=false&i=104E29&tenant=r5dealer135812&ni=2EE\",\"isDialog\":false,\"isSpa\":true,\"spaInstanceId\":\"ji5tdza9\",\"isInApp\":false,\"deviceCategory\":0,\"nativePageType\":\"ListApplicationPage\"}}],\"id\":1}";

        Response response = ConnectionUtil.doPostWithLeastParamJson(URL, param, COOKIE);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());

        JsonNode res = result.get("result");
        System.out.println("结果为" + result);

        if (res != null)
            System.out.println("结果为" + res);


    }


}
