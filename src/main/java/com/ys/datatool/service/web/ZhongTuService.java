package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.Product;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/7/24.
 * <p>
 * 众途系统
 */
@Service
public class ZhongTuService {


    private String ITEM_URL = "http://crm.zhongtukj.com/Boss/Stock/Stockservice/StockShop.ashx";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "total";

    private String companyId = "7";

    private String COOKIE = "ASP.NET_SessionId=slyqlmsdtyatk3yvwfmuzjpy; ztrjnew@4db97b96-12af-45b0-b232-fd1e9b7a672e=UserId=9Os8NGpT0Ts=&CSID=XM5LZ3LKOMA=&UserName=WHZqGZajveNSaHb4auidfA==&SID=TLlau79yBDk=&RoleId=lg86MnveZaI=&GroupId=xrWQau6DcFU=";

    /**
     * 商品
     *
     * @throws IOException
     */
    @Test
    public void fetchItemDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        String act = "GetShopList";
        Response res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams("1", "15", companyId, act), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams(String.valueOf(i), "15", companyId, act), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("Name").asText();
                    String companyName = element.get("GroupName").asText();
                    String code = element.get("ShopCode").asText();
                    String firstCategoryName = element.get("ShopTypeName").asText();
                    String price = element.get("Price").asText();
                    String barCode = element.get("NameCode").asText();
                    String unit = element.get("UnitName").asText();
                    String remark = element.get("GuiGe").asText();//产品规格

                    Product product = new Product();
                    product.setCode(code);
                    product.setProductName(productName);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setUnit(unit);
                    product.setPrice(price);
                    product.setCompanyName(companyName);
                    product.setBarCode(barCode);
                    product.setItemType("配件");
                    product.setRemark(remark);
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());
        System.out.println("结果为" + products.size());

        String pathname = "C:\\exportExcel\\众途商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    private List<BasicNameValuePair> getParams(String page, String rows, String GroupID, String act) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("act", act));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("rows", rows));
        params.add(new BasicNameValuePair("GroupID", GroupID));
        return params;
    }

}
