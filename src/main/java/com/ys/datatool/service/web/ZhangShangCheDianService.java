package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018-05-30.
 */
@Service
public class ZhangShangCheDianService {


    private String SUPPLIER_URL = "http://czbbb.cn/mnt/czbbb/supplierMgmt/czbbbApi.action";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Workbook workbook;

    //供应商页面总页数
    private int supplierPageNum = 3;

    private String COOKIE = "JSESSIONID=3BCA8A95EE362F19FAD287422EC33977; Authorization=eyJhbGciOiJIUzUxMiJ9.eyJpZCI6IjZhNTRmYjQwLWVlZjEtNDAxZS04ZThiLWE0NGY5OWI3MjNlZSIsImV4cCI6MTUyNzc0MjI2NywibmJmIjoxNTI3NjU1ODY3LCJzdG9yZUlkIjoiOWU2NTA3MmEtNjIyMy00Y2U0LWI1MjAtMGMwZGQzN2IwMzU0IiwidXNlclR5cGUiOiIwIn0.UfyBOYLPwMIjbEySptxHWl1RmKhVBh2nqa5oeFsX3BG6tgBnVICRw3L1JghcPc2rAHvVQl3Sl7uKV0-GGuKoqQ; Hm_lvt_678c2a986264dd9650b6a59042718858=1527655868; Hm_lpvt_678c2a986264dd9650b6a59042718858=1527660767; SERVERID=9a4b1cc263e64137f343a05cba9021f1|1527661451|1527655856";

    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        for (int i = 1; i <= supplierPageNum; i++) {
            Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getSupplierParams(String.valueOf(i), String.valueOf(30)), COOKIE);

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
                //supplier.setFax(element.get("officePhone") != null ? element.get("officePhone").asText() : "");//"officePhone"
                suppliers.add(supplier);
            }
        }

        String pathname = "D:\\掌上车店供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, workbook, pathname);
    }

    private List<BasicNameValuePair> getSupplierParams(String pageNo, String pageSize) {
        List<BasicNameValuePair> params = new ArrayList<>();

        String value = "{" + "\"pageSize\":" + pageSize + "," +
                "\"pageNo\":" + pageNo + "}";

        params.add(new BasicNameValuePair("data", value));
        params.add(new BasicNameValuePair("method", "60701"));
        return params;
    }


}
