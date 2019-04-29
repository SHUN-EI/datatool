package com.ys.datatool.service.cloudCar;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.entity.Part;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/9/3.
 * <p>
 * 广汽三菱-经销商协同管理系统
 */
@Service
public class DCMSGuangQiSanLingService {

    private String ITEM_URL = "http://dcms.gmmc.com.cn/DCMS/part/partbase/DlrPartBaseInfoAction/queryBaseInfoList.json";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String companyName="广汽三菱";

    private String COOKIE = "JSESSIONID=0Y6tBKeptquMEE_YXE9M6jWzaIhAxexKUqxBfC2tCvtkLJ6QFSmn!1897524995";


    @Test
    public void fetchItemData() throws IOException {
        List<Part> parts = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getItemParams("1"), COOKIE);
        String content = res.returnContent().asString();

        String temp = JSONObject.parse(content).toString();
        JsonNode node = MAPPER.readTree(temp);
        String totalStr = node.get("ps").get("totalPages").asText();
        int total = Integer.parseInt(totalStr);


        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getItemParams(String.valueOf(i)), COOKIE);
                String returnContent = res.returnContent().asString();

                String formatJson = JSONObject.parse(returnContent).toString();
                JsonNode data = MAPPER.readTree(formatJson);

                Iterator<JsonNode> it = data.get("ps").get("records").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String code = element.get("PART_CODE") != null ? element.get("PART_CODE").asText() : "";
                    String name = element.get("PART_NAME") != null ? element.get("PART_NAME").asText() : "";
                    String partsCode = element.get("PARTS_CODE") != null ? element.get("PARTS_CODE").asText() : "";
                    String remark = element.get("REMARK") != null ? element.get("REMARK").asText() : "";
                    String salePrice = element.get("SALE_PRICE") != null ? element.get("SALE_PRICE").asText() : "";
                    String costPrice = element.get("COST_PRICE") != null ? element.get("COST_PRICE").asText() : "";
                    String unit = element.get("UNITS") != null ? element.get("UNITS").asText() : "";
                    String specification = element.get("SPECIFICATIONS") != null ? element.get("SPECIFICATIONS").asText() : "";
                    String specificationCapacity = element.get("SPECIFICATION_CAPACITY") != null ? element.get("SPECIFICATION_CAPACITY").asText() : "";
                    String origin = element.get("ORIGIN") != null ? element.get("ORIGIN").asText() : "";
                    String carModel = element.get("MODEL_NAME") != null ? element.get("MODEL_NAME").asText() : "";
                    String replacePartCode = element.get("REPLACE_PART_CODE") != null ? element.get("REPLACE_PART_CODE").asText() : "";

                    Part part = new Part();
                    part.setCompanyName(companyName);
                    part.setCode(code);
                    part.setName(name);
                    part.setPartsCode(partsCode);
                    part.setRemark(remark);
                    part.setSalePrice(salePrice);
                    part.setCostPrice(costPrice);
                    part.setUnit(unit);
                    part.setSpecification(specification);
                    part.setSpecificationCapacity(specificationCapacity);
                    part.setOrigin(origin);
                    part.setCarModel(carModel);
                    part.setReplacePartCode(replacePartCode);
                    parts.add(part);

                    //暂停11秒
                /*    try {
                        Thread.sleep(11000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        }

        String pathname = "C:\\exportExcel\\广汽三菱系统配件.json";
        File file = new File(pathname);
        MAPPER.writeValue(file, parts);

        String pathname2 = "C:\\exportExcel\\广汽三菱配件.xls";
        ExportUtil.exportPartDataInLocal(parts, ExcelDatas.workbook, pathname2);

        System.out.println("结果为" + parts.toString());
        System.out.println("结果为" + parts.size());
    }

    private List<BasicNameValuePair> getItemParams(String page) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("except", "0"));
        params.add(new BasicNameValuePair("_search", "false"));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("rows", "50"));
        params.add(new BasicNameValuePair("sord", "desc"));
        params.add(new BasicNameValuePair("sidx", "PART_CODE"));
        return params;
    }
}
