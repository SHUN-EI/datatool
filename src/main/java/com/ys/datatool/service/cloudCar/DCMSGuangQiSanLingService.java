package com.ys.datatool.service.cloudCar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Part;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
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

    private String COOKIE = "JSESSIONID=4PaoyqEN3M4EyyADZWZfPZPOJiK97BldUh7WZvviGDWRBbUwQm4V!1897524995";


    @Test
    public void fetchItemData() throws IOException {
        List<Part> parts = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getItemParams("1"), COOKIE);
        String content = res.returnContent().asString();
        String totalPageRegEx = "totalPages:.*";
        String totalString = CommonUtil.fetchString(content, totalPageRegEx).replace("totalPages:", "");
        String totalStr = totalString.replace("}}", "");
        int total = Integer.parseInt(totalStr);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {

                res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getItemParams(String.valueOf(i)), COOKIE);
                String returnContent = res.returnContent().asString();

                //处理获取的字符串
                String con1 = returnContent.substring(returnContent.indexOf("ps"), returnContent.length());
                String con2 = "{" + con1;
                String con3 = con2.replaceAll("\\{", "\\{\"");
                String con4 = con3.replaceAll(":", "\":");
                String con5 = con4.replaceAll(",", ",\"");
                String result = con5.replaceAll(",\"\\{", ",\\{");

                System.out.println("返回结果为-------" + result);
                System.out.println("当前页数为-------" + i + "页");

                //特殊
                if (result.contains("\"DIAMETER\""))
                    result = result.replace("\"DIAMETER\"", "\"DIAMETER");

                if (result.contains("PNC\""))
                    result = result.replace("PNC\"", "PNC");

                if (result.contains("\"1\":"))
                    result = result.replaceAll("\"1\":", "\"1:");

                if (result.contains("EXT\""))
                    result = result.replaceAll("EXT\"", "EXT");

                if (result.contains("\"电池\":CR2032,\"3SW\""))
                    result = result.replaceAll("\"电池\":CR2032,\"3SW\"", "\"电池:CR2032,3SW\"");

                //解析
                JsonNode data = MAPPER.readTree(result);
                JsonNode record = data.get("ps");

                Iterator<JsonNode> it = record.get("records").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String code = element.get("PART_CODE").asText();
                    String name = element.get("PART_NAME").asText();
                    String partsCode = element.get("PARTS_CODE").asText();
                    String remark = element.get("REMARK").asText();
                    String salePrice = element.get("SALE_PRICE").asText();
                    String costPrice = element.get("COST_PRICE").asText();
                    String unit = element.get("UNITS").asText();
                    String specification = element.get("SPECIFICATIONS").asText();
                    String specificationCapacity = element.get("SPECIFICATION_CAPACITY").asText();
                    String origin = element.get("ORIGIN").asText();
                    String carModel = element.get("MODEL_NAME").asText();
                    String replacePartCode = element.get("REPLACE_PART_CODE").asText();

                    Part part = new Part();
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
