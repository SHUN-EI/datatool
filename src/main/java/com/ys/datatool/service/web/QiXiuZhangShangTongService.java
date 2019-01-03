package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.Product;
import com.ys.datatool.domain.Stock;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2019/1/2.
 * 汽修掌上通
 */
@Service
public class QiXiuZhangShangTongService {

    private String STOCKDETAIL_URL = "http://xlc.qxgs.net/api/pc/def/sp/shop_parts/";

    private String STOCK_URL = "http://xlc.qxgs.net/api/pc/def/sp/shop_parts/overview/page?pageSize=10&itemsCount={num}&pageNo=";

    private String SUPPLIER_URL = "http://xlc.qxgs.net/api/pc/def/sp/spsuppliers/find";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String CONTENT_TYPE = "application/json;charset=UTF-8";

    private String companyName = "汽修掌上通";

    private String COOKIE = "Hm_lvt_c86a6dea8a77cec426302f12c57466e0=1546399091; shop=%22%E5%AE%89%E7%B4%A2%E6%B1%BD%E8%BD%A6%E5%85%BB%E6%8A%A4%E6%80%BB%E5%BA%97%22; Hm_lpvt_c86a6dea8a77cec426302f12c57466e0=1546490520; sid=7da50a60-55dc-4b82-9f19-fc70eaf73164";


    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(STOCK_URL, "{num}", "0") + 1, COOKIE);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get(0).get("len");
        int totalPage = WebClientUtil.getTotalPage(totalNode, 10);
        String countNum = totalNode.asText();

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(STOCK_URL, "{num}", countNum) + i, COOKIE);
                JsonNode content = MAPPER.readTree(response.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String goodsName = element.get("partName").asText();
                        String productCode = element.get("partCode").asText();
                        String price = element.get("price").asText();
                        String inventoryNum = element.get("amount").asText();
                        String brand = element.get("partBrand").asText();
                        String carModel = element.get("appModels").asText();
                        String unit = element.get("spec").asText();

                        Stock stock = new Stock();
                        stock.setCompanyName(companyName);
                        stock.setGoodsName(goodsName);
                        stock.setProductCode(productCode);
                        stock.setPrice(price);
                        stock.setInventoryNum(inventoryNum);

                        String partId = element.get("partId").asText();
                        Response res = ConnectionUtil.doGetWithLeastParams(STOCKDETAIL_URL + partId + "/stockDetl", COOKIE);
                        JsonNode body = MAPPER.readTree(res.returnContent().asString());
                        JsonNode data = body.get("data");
                        if (data.size() > 0) {
                            JsonNode stockNode = data.get(0);
                            String storeRoomName = stockNode.get("warehouse").asText();
                            String locationName = stockNode.get("warehouseInfo").asText();

                            stock.setStoreRoomName(storeRoomName);
                            stock.setLocationName(locationName);
                        }

                        stocks.add(stock);

                        Product product=new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(goodsName);
                        product.setCode(productCode);
                        product.setPrice(price);
                        product.setBrandName(brand);
                        product.setCarModel(CommonUtil.formatString(carModel));
                        product.setUnit(unit);
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\汽修掌上通库存.xls";
        String pathname2 = "C:\\exportExcel\\汽修掌上通库存商品.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname2);

    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(SUPPLIER_URL, getParam(1), COOKIE, CONTENT_TYPE);
        int totalPage = getTotalPage(response);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(SUPPLIER_URL, getParam(i), COOKIE, CONTENT_TYPE);
                JsonNode content = MAPPER.readTree(response.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String name = element.get("name").asText();
                        String contactPhone = element.get("phone").asText();
                        String contactName = element.get("contact").asText();
                        String address = element.get("addr").asText();
                        String remark = element.get("remark").asText();
                        String depositBank = element.get("bankName").asText();
                        String accountNumber = element.get("bankNo").asText();

                        Supplier supplier = new Supplier();
                        supplier.setCompanyName(companyName);
                        supplier.setName(CommonUtil.formatString(name));
                        supplier.setContactPhone(CommonUtil.formatString(contactPhone));
                        supplier.setContactName(CommonUtil.formatString(contactName));
                        supplier.setAddress(CommonUtil.formatString(address));
                        supplier.setRemark(CommonUtil.formatString(remark));
                        supplier.setDepositBank(CommonUtil.formatString(depositBank));
                        supplier.setAccountNumber(CommonUtil.formatString(accountNumber));
                        suppliers.add(supplier);
                    }
                }
            }
        }


        String pathname = "C:\\exportExcel\\汽修掌上通供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }

    private int getTotalPage(Response response) throws IOException {
        JsonNode result = MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get(0).get("len");
        int totalPage = WebClientUtil.getTotalPage(totalNode, 10);

        return totalPage > 0 ? totalPage : 0;
    }

    private String getParam(int pageNo) {
        String param = "{" +
                "\"all\":1," +
                "\"pageSize\":10," +
                "\"pageIndex\":" + pageNo +
                "}";

        return param;
    }
}
