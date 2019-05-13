package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.Supplier;
import com.ys.datatool.util.ConnectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/6/30.
 * 淘汽云修系统
 */
@Service
public class TaoQiYunXiuService {


    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private static final String COOKIE = "JSESSIONID=86E8954B7FCF51834B091C36A51E5524; VC_UUID=e3be3702-f1b3-4bec-b307-5cb0cb0e0e8aVC; SESSION_USER_NAME=%E6%9D%8E%E5%86%9B%E9%94%8B; UUID=5cef3fe3-7931-4654-a278-65d4c03ce060; Hm_lvt_177aadfd52500674827db74ca8f51989=1503281502,1503541165; Hm_lpvt_177aadfd52500674827db74ca8f51989=1503557466";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private static final String STOCK_URL = "http://www.51chegj.com:8089/scm/stroeInventory/inventoryStatistics/qryInventoryPage?store_id=100675&tenantId=10675&keys=&prod_cata_id=&limit=20";

    private static final String SUPPLIER_URL = "http://www.yunqixiu.com/legend/shop/setting/supplier/supplier-edit?id={id}&refer=supplier-list";

    private static final String SUPPLIERLIST_URL = "http://www.yunqixiu.com/legend/shop/setting/supplier/supplier-list/data?page={page}";

    private String fileName = "淘汽云修";



    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        List<String> supplierIds = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(StringUtils.replace(SUPPLIERLIST_URL, "{page}", "1"), COOKIE);
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

        JsonNode node = result.get("data");
        int totalPage = Integer.parseInt(node.get("totalPages").asText());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWith(StringUtils.replace(SUPPLIERLIST_URL, "{page}", i + ""), COOKIE);
                result = JsonObject.MAPPER.readTree(response.returnContent().asString());
                node = result.get("data");

                Iterator<JsonNode> it = node.get("content").elements();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("id").asText();
                    supplierIds.add(id);
                }
            }
        }

        for (int i = 0; i < supplierIds.size(); i++) {
            response = ConnectionUtil.doGetWith(StringUtils.replace(SUPPLIER_URL, "{id}", supplierIds.get(i)), COOKIE);
            String html = response.returnContent().asString();
            Document document = Jsoup.parse(html);

            String supplierNameRegEx = "body > div.yqx-wrapper.clearfix > div.order-right.fl > div.content.js-supplier > div.form-box > div:nth-child(1) > div:nth-child(1) > div.form-item > input";
            String supplierName = document.select(supplierNameRegEx).attr("value");

            String supplierContactNameRegEx = "body > div.yqx-wrapper.clearfix > div.order-right.fl > div.content.js-supplier > div.form-box > div:nth-child(2) > div:nth-child(2) > div.form-item > input";
            String supplierContactName = document.select(supplierContactNameRegEx).attr("value");

            String supplierContactPhoneRegEx = "body > div.yqx-wrapper.clearfix > div.order-right.fl > div.content.js-supplier > div.form-box > div:nth-child(3) > div:nth-child(1) > div.form-item > input";
            String supplierContactPhone = document.select(supplierContactPhoneRegEx).attr("value");

            Supplier supplier = new Supplier();
            supplier.setName(supplierName);
            supplier.setContactName(supplierContactName);
            supplier.setContactPhone(supplierContactPhone);
            supplier.setPhone(supplierContactPhone);
            suppliers.add(supplier);
        }

    }
}
