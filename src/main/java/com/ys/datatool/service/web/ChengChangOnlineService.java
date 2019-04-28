package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mo on 2019/4/28
 */
@Service
public class ChengChangOnlineService {

    private String CATEGORY_URL = "http://guanjia.cz888.com/Api/Common/GetProductCate";

    private String SERVICE_URL = "http://guanjia.cz888.com/Api/Product/ProductSearch?Pcid=0&cid=0&Type=2";

    private String fieldName = "Count";

    private String companyName = "橙长在线";

    private String COOKIE = "ASP.NET_SessionId=jpg5agnfnmqxiqshltvbnhdg; ShopAdminUser=uId=4713DC16B7C0D7E3&uType=&uName=B95EA3713D5D4337D61C03D0B14A72F0&uPhone=B63E7262FDF19C2B3079616824B1C652&uStag=A1050A9334DD35BC34C6D80EBF37313750062C62D2A4DDB565B869777C6E6AB39A4CE2530BA354E0&uLastLogin=2019/4/28 14:01:58&uLoginIp=61.144.100.160&uLoginCount=749&uLoginNowTime=1556453987096&uIp=ED6B9AA7C71BFC6482003D463DF51B5A&Expires=1556885987096";


    /**
     * 服务项目
     * <p>
     * 打开路径:店铺管理-服务列表
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        List<FirstCategory> firstCategories = fetchFirstCategoryData();
        List<SecondCategory> secondCategories = fetchSecondCategoryData();

        Response response = ConnectionUtil.doPostWithLeastParamJson(SERVICE_URL, getParam(1), COOKIE, WebConfig.CONTENT_TYPE);

        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(SERVICE_URL, getParam(i), COOKIE, WebConfig.CONTENT_TYPE);

                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET));

                JsonNode dataNode = result.get("List");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String productName = e.get("Name").asText();
                        String barCode = e.get("VenderSku").asText();
                        String price = e.get("Price").asText();
                        String categoryName = e.get("CatName").asText();
                        String unit = e.get("Unit").asText();

                        System.out.println("服务名称为" + productName);
                        String cid = e.get("Cid").asText();

                        String firstCategoryName = "";
                        List<SecondCategory> secondCategoryList = secondCategories.stream()
                                .filter(secondCategory -> cid.equals(secondCategory.getSid()))
                                .collect(Collectors.toList());

                        if (secondCategoryList.size() > 0) {
                            firstCategoryName = secondCategoryList.get(0).getFirstCategory().getName();
                        } else {
                            FirstCategory fCategory = firstCategories.stream().filter(firstCategory -> cid.equals(firstCategory.getFid())).collect(Collectors.toList()).get(0);
                            firstCategoryName = fCategory.getName();
                        }

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(productName);
                        product.setItemType("服务项");
                        product.setBarCode(barCode);
                        product.setCode(barCode);
                        product.setFirstCategoryName(firstCategoryName);
                        product.setSecondCategoryName(categoryName);
                        product.setPrice(price);
                        product.setUnit(unit);
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\橙长在线服务.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);

    }


    /**
     * 服务一级分类
     *
     * @throws IOException
     */
    private List<FirstCategory> fetchFirstCategoryData() throws IOException {
        List<FirstCategory> firstCategories = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithoutParam(CATEGORY_URL, COOKIE);

        JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET));
        if (content.size() > 0) {
            Iterator<JsonNode> it = content.iterator();

            while (it.hasNext()) {
                JsonNode e = it.next();

                String id = e.get("Id").asText();
                String name = e.get("Name").asText();

                FirstCategory firstCategory = new FirstCategory();
                firstCategory.setFid(id);
                firstCategory.setName(name);
                firstCategories.add(firstCategory);
            }
        }
        return firstCategories;
    }

    /**
     * 服务二级分类
     *
     * @throws IOException
     */
    private List<SecondCategory> fetchSecondCategoryData() throws IOException {
        List<SecondCategory> secondCategories = new ArrayList<>();

        List<FirstCategory> firstCategories = fetchFirstCategoryData();
        if (firstCategories.size() > 0) {
            for (FirstCategory firstCategory : firstCategories) {

                String param = "{" + "id:" + firstCategory.getFid() + "}";
                Response response = ConnectionUtil.doPostWithLeastParamJson(CATEGORY_URL, param, COOKIE, WebConfig.CONTENT_TYPE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET));

                if (result.size() > 0) {
                    Iterator<JsonNode> it = result.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String id = e.get("Id").asText();
                        String name = e.get("Name").asText();

                        SecondCategory secondCategory = new SecondCategory();
                        secondCategory.setSid(id);
                        secondCategory.setName(name);
                        secondCategory.setFirstCategory(firstCategory);
                        secondCategories.add(secondCategory);
                    }
                }
            }
        }
        return secondCategories;
    }

    private String getParam(int pageNo) {
        String param = "{pn:" +
                pageNo +
                ",PageSize: 20" +
                ",DeptId:12 }";

        return param;
    }

}
