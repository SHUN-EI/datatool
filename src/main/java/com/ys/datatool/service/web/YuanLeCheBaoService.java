package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.HtmlTag;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by mo on @date  2018/5/14.
 * 元乐车宝系统
 */
@Service
public class YuanLeCheBaoService {

    ////////////////////////////工具使用前，请先填写companyId、shopBranchId、staffId、COOKIE等数据////////////////////////////////////////////////////////////////////////


    /**
     * 车店编号-shopId:
     * 215(冠军养护)、183(迅驰)、208(稳中快)、
     * 77(石家庄丽雷行)、140(天骐汽车)、132(路胜通汽车)、
     * 288(良匠汽车)、70(黑妞汽车)、82(国瑞汽修厂)、284(车来车旺美车会所)
     * 79(广州市花都区明杰)、113(新蔡爱卡汽车)、283(摩范汽车)、86(宜章财君)
     * 118(T-9养车汇)
     */
    private String companyId = "271 ";

    /**
     * 分店编号-shopBranchId：
     * 146(路胜通汽车)、298(摩范汽车)、100(宜章财君)、132(T-9养车汇)
     */
    private String shopBranchId = "285";

    /**
     * staffId：
     * 2422(T-9养车汇)
     */
    private String staffId = "4285";

    private String COOKIE = "JSESSIONID=E2F59671ADDD2EFC763037783C88A2A5; usfl=5WZMVa2aFk12FlCElq0; lk=8a83584a4316d3ab05d5b2a793cd2c7b";



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    private String BILL_URL = "http://wh.youchepi.cn/Home/shopFinance/orderTable";

    private String CARDRIVINGLICENSE_URL = "http://wh.youchepi.cn/Home/car/drivingLicense";

    private String CLIENTLIST_URL = "http://wh.youchepi.cn/Home/receptionService/customerTable";

    private String CLIENTDETAIL_URL = "http://wh.youchepi.cn/Home/receptionService/customerDetail";

    private String BILLINSERVICE_URL = "http://wh.youchepi.cn/Home/workbench/ajaxGetInServiceList";

    private String MEMBERCARD_URL = "http://wh.youchepi.cn/Home/memberManagement/gerMemberUserLists";

    private String STOCKINSEARCH_URL = "http://wh.youchepi.cn/Home/storage/storageInSearchByPartsGuid";

    private String STOCKINDIV_URL = "http://wh.youchepi.cn/Home/partsinfo/storageInDiv";

    private String STOCKDETAIL_URL = "http://wh.youchepi.cn/Home/partsinfo/partsInfo";

    private String STOCK_URL = "http://wh.youchepi.cn/Home/cbstoragepartsinventory/invenManagementTable";

    private String SERVICE_URL = "http://wh.youchepi.cn/Home/service/serviceTable";

    private String SUPPLIER_URL = "http://wh.youchepi.cn/Home/cbpartssupplier/supplierTable";

    private String CARINFOPAGE_URL = "http://wh.youchepi.cn/Home/car/carTable";

    private String CARINFODETAIL_URL = "http://wh.youchepi.cn/Home/car/carDetail";

    //会员管理-会员等级
    private String MEMBERCARDOVERVIEW_URL = "http://wh.youchepi.cn/Home/memberManagement/memberList?";

    private String trItemRegEx = "#content-tbody > tr";

    private String trStockRegEx = "#set-tbody > tr";

    private String divRegEx = "div[class='userCars']  > div[class='row'] > div";

    String optionRegEx = "#specification_search_in > option";

    private String totalPageRegEx = "totalPage =.*";

    private String totalPageReplaceRegEx = "totalPage = ";

    private String totalRegEx = "totalPage=.*";

    private String totalReplaceRegEx = "totalPage=";

    private String fieldName = "totalCount";

    private String divName = "div";

    private String optionName = "option";

    private int num = 10;

    private String companyName = "元乐车宝";

    private Random random = new Random();




    /**
     * 历史消费记录和消费记录相关车辆
     * <p>
     * 打开路径:门店财务-门店订单
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithLeastParams(BILL_URL, getPageInfoParams("1"), COOKIE);
        String content = res.returnContent().asString();
        Document document = Jsoup.parseBodyFragment(content);

        String startRegEx = "\"data\"";
        String endRegEx = "};";
        JsonNode node = getDataNode(document, startRegEx, endRegEx);
        JsonNode totalCount = node.get("totalCount");
        int totalPage = WebClientUtil.getTotalPage(totalCount, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(BILL_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                JsonNode result = getDataNode(doc, startRegEx, endRegEx);
                Iterator<JsonNode> it = result.get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("orderCode").asText();
                    String carNumber = element.get("carNumber").asText();

                    String remark = element.get("tagName").asText();//订单类型
                    String totalAmount = element.get("dealAmount").asText();//totalAmount
                    String dateEnd = element.get("orderTime").asText();
                    dateEnd = DateUtil.formatSQLDate(dateEnd);

                    String name = "";
                    if (element.get("userName") != null)
                        name = element.get("userName").asText();

                    String phone = "";
                    if (element.get("mobile") != null)
                        phone = element.get("mobile").asText();

                    Bill bill = new Bill();
                    bill.setBillNo(billNo);
                    bill.setCarNumber(carNumber);
                    bill.setCompanyName(companyName);
                    bill.setTotalAmount(totalAmount);
                    bill.setDateEnd(dateEnd);

                    JsonNode orderItemNode = element.get("orderItemList");
                    if (orderItemNode.size() > 0) {
                        Iterator<JsonNode> items = orderItemNode.iterator();
                        while (items.hasNext()) {
                            JsonNode e = items.next();
                            String serviceItemName = e.get("itemName").asText();

                            if (null != bill.getServiceItemNames()) {
                                String service = bill.getServiceItemNames() + "," + serviceItemName;
                                bill.setServiceItemNames(service);
                            }

                            if (null == bill.getServiceItemNames()) {
                                bill.setServiceItemNames(serviceItemName);
                            }
                        }
                    }

                    bills.add(bill);

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCompanyName(companyName);
                    carInfo.setName(name);
                    carInfo.setPhone(phone);
                    carInfo.setCarNumber(carNumber);
                    carInfos.add(carInfo);
                }
            }
        }

        String pathname = "C:\\exportExcel\\元乐车宝消费记录.xls";
        String pathname2 = "C:\\exportExcel\\元乐车宝记录-车辆.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname2);
    }

    /**
     * 门店订单-单据和单据明细
     * <p>
     * 打开路径:门店财务-门店订单
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithLeastParams(BILL_URL, getPageInfoParams("1"), COOKIE);
        String content = res.returnContent().asString();
        Document document = Jsoup.parseBodyFragment(content);

        String startRegEx = "\"data\"";
        String endRegEx = "};";
        JsonNode node = getDataNode(document, startRegEx, endRegEx);
        JsonNode totalCount = node.get("totalCount");
        int totalPage = WebClientUtil.getTotalPage(totalCount, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(BILL_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                JsonNode result = getDataNode(doc, startRegEx, endRegEx);
                Iterator<JsonNode> it = result.get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String billNo = element.get("orderCode").asText();
                    String carNumber = element.get("carNumber").asText();
                    String phone = element.get("mobile").asText();
                    String actualAmount = element.get("dealAmount").asText();
                    String totalAmount = element.get("totalAmount").asText();
                    String name = element.get("userName").asText();

                    String dateEnd = element.get("payTime").asText();//需要转换日期格式

                    String dateAdded = element.get("orderTime").asText();
                    dateAdded = DateUtil.formatSQLDateTime(dateAdded);

                    Bill bill = new Bill();
                    bill.setCompanyName(companyName);
                    bill.setBillNo(billNo);
                    bill.setCarNumber(carNumber);
                    bill.setPhone(phone);
                    bill.setActualAmount(actualAmount);
                    bill.setTotalAmount(totalAmount);
                    bill.setName(name);
                    bill.setDateAdded(dateAdded);
                    bill.setDateExpect(dateAdded);
                    bill.setDateEnd(dateAdded);
                    bills.add(bill);

                    if (element.get("orderItemList") != null) {
                        Iterator<JsonNode> items = element.get("orderItemList").iterator();
                        while (items.hasNext()) {
                            JsonNode e = items.next();
                            String itemName = e.get("itemName").asText();
                            String num = e.get("quantity").asText();
                            String price = e.get("itemPrice").asText();
                            String itemCode = e.get("orderItemGuid").asText();

                            BillDetail billDetail = new BillDetail();
                            billDetail.setCompanyName(companyName);
                            billDetail.setBillNo(billNo);
                            billDetail.setItemName(itemName);
                            billDetail.setNum(num);
                            billDetail.setItemType("服务项");
                            billDetail.setPrice(price);
                            billDetail.setItemCode(itemCode);
                            billDetails.add(billDetail);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\元乐车宝单据.xls";
        String pathname2 = "C:\\exportExcel\\元乐车宝单据明细.xls";
        ExportUtil.exportBillDataInLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportBillDetailDataInLocal(billDetails, ExcelDatas.workbook, pathname2);

    }

    /**
     * 库存-标准模版导出
     * <p>
     * 打开路径:商品管理-配件编辑-配件信息和配件设置-入库记录
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        Map<String, Product> productMap = new HashMap<>();
        Map<String, Product> productWithSpec = new HashMap<>();
        Map<String, Stock> stockMap = new HashMap<>();

        //商品管理
        int totalPage = getTotalPage(STOCK_URL, getPageInfoParams("1"), totalRegEx, totalReplaceRegEx);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String priceRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(5)";
                        String productNameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(4) > span";
                        String brandNameRegEx = "#content-tbody > tr:nth-child(" + j + ")  > td:nth-child(3)";
                        String firstCategoryNameRegEx = "#content-tbody > tr:nth-child(" + j + ")  > td:nth-child(2)";

                        String price = doc.select(priceRegEx).text();
                        String productName = doc.select(productNameRegEx).text();
                        String brandName = doc.select(brandNameRegEx).text();
                        String firstCategoryName = doc.select(firstCategoryNameRegEx).text();

                        String partsGuidRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(9) > div > span.common-font.edit-act";
                        String partsGuid = doc.select(partsGuidRegEx).attr("onclick");
                        String getGUIDRegEx = "(?<=').*(?=')";
                        String guid = CommonUtil.fetchString(partsGuid, getGUIDRegEx);

                        Product product = new Product();
                        product.setPrice(price);
                        product.setBrandName(brandName);
                        product.setProductName(productName);
                        product.setFirstCategoryName(firstCategoryName);
                        productMap.put(guid, product);
                    }
                }
            }
        }

        //入库记录-请选择规格
        if (productMap.size() > 0) {
            for (String guid : productMap.keySet()) {
                Product p = productMap.get(guid);
                Response response = ConnectionUtil.doPostWithLeastParams(STOCKINDIV_URL, getDetailParams("partsGuid", guid), COOKIE);
                String content = response.returnContent().asString();
                Document doc = Jsoup.parse(content);

                int optionSize = WebClientUtil.getTagSize(doc, optionRegEx, optionName);
                if (optionSize > 0) {
                    for (int i = 2; i <= optionSize; i++) {
                        String specRegEx = "#specification_search_in > option:nth-child(" + i + ")";
                        String spec = doc.select(specRegEx).text();
                        String specValue = doc.select(specRegEx).attr("value");

                        Product product = new Product();
                        product.setSpecification(spec);
                        product.setSpecificationValue(specValue);
                        product.setPrice(p.getPrice());
                        product.setBrandName(p.getBrandName());
                        product.setProductName(p.getProductName());
                        product.setFirstCategoryName(p.getFirstCategoryName());
                        product.setId(guid);
                        productWithSpec.put(guid + spec, product);
                    }
                }
            }
        }

        //配件信息和配件设置
        if (productWithSpec.size() > 0) {
            for (Map.Entry<String, Product> entry : productWithSpec.entrySet()) {
                Product p = entry.getValue();
                Response response = ConnectionUtil.doPostWithLeastParams(STOCKDETAIL_URL, getDetailParams("partsGuid", p.getId()), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(doc, trStockRegEx, HtmlTag.trName);
                for (int i = 1; i <= trSize; i++) {

                    String specRegEx = "#set-tbody > tr:nth-child(" + i + ") > td:nth-child(1)";//规格
                    String inventoryNumRegEx = "#set-tbody > tr:nth-child(" + i + ") > td:nth-child(5)";//库存
                    String salePriceRegEx = "#set-tbody > tr:nth-child(" + i + ") > td:nth-child(2)";//价格

                    String spec = doc.select(specRegEx).text();
                    String inventoryNum = doc.select(inventoryNumRegEx).text();
                    String salePrice = doc.select(salePriceRegEx).text();

                    Stock stock = new Stock();
                    stock.setCompanyName(companyName);
                    stock.setStoreRoomName("仓库");
                    stock.setProductCode(spec);
                    stock.setPartsGuid(p.getId());
                    stock.setGoodsName(p.getProductName());
                    stock.setInventoryNum(inventoryNum);
                    stock.setSalePrice(salePrice.replace("￥", ""));
                    stock.setBrand(p.getBrandName());
                    stock.setFirstCategoryName(p.getFirstCategoryName());

                    stock.setSpec(spec);//规格
                    stock.setSpecification(spec);

                    if (spec.equals(p.getSpecification())) {
                        stock.setSpecificationValue(p.getSpecificationValue());
                    } else {
                        String specValue = productWithSpec.get(p.getId() + spec).getSpecificationValue();
                        stock.setSpecificationValue(specValue);
                    }

                    stockMap.put(stock.getSpecificationValue(), stock);

                }
            }
        }

        //入库记录-根据规格筛选
        if (stockMap.size() > 0) {
            for (String value : stockMap.keySet()) {
                Stock stock = stockMap.get(value);
                String partsGuid = stock.getPartsGuid();
                String specificationGuid = stock.getSpecificationValue();

                if (specificationGuid != null) {
                    Response response = ConnectionUtil.doPostWithLeastParams(STOCKINSEARCH_URL, getStockInPriceParams(partsGuid, specificationGuid), COOKIE);
                    String html = response.returnContent().asString();
                    Document doc = Jsoup.parse(html);

                    //取第一条入库记录中的成本价
                    String priceRegEx = "#content-tbody > tr:nth-child(1) > td:nth-child(7)";
                    String price = doc.select(priceRegEx).text().replace("￥", "");

                    if (StringUtils.isBlank(price))
                        price = "0";

                    Stock s = new Stock();
                    s.setCompanyName(companyName);
                    s.setStoreRoomName("仓库");
                    s.setProductCode(stock.getProductCode());
                    s.setPartsGuid(stock.getPartsGuid());
                    s.setGoodsName(stock.getGoodsName());
                    s.setInventoryNum(stock.getInventoryNum());
                    s.setSalePrice(stock.getSalePrice());
                    s.setBrand(stock.getBrand());
                    s.setFirstCategoryName(stock.getFirstCategoryName());
                    s.setSpec(stock.getSpec());//规格
                    s.setSpecification(stock.getSpecification());
                    s.setSpecificationValue(stock.getSpecificationValue());
                    s.setPrice(price);
                    stocks.add(s);
                }
            }
        }

        System.out.println("结果为" + stocks.toString());
        System.out.println("大小为" + stocks.size());

        String pathname = "C:\\exportExcel\\元乐车宝库存.xls";
        String pathname2 = "C:\\exportExcel\\元乐车宝库存（含分类）.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
        ExportUtil.exportYuanLeCheBaoStockDataInLocal(stocks, ExcelDatas.workbook, pathname2);
    }

    /**
     * 商品-标准模版导出
     * 打开路径：库存管理-商品管理
     *
     * @throws IOException
     */
    @Test
    public void fetchItemDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();
        Map<String, Product> productMap = new HashMap<>();

        int totalPage = getTotalPage(STOCK_URL, getPageInfoParams("1"), totalRegEx, totalReplaceRegEx);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String productNameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(4) > span";
                        String brandNameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(3)";
                        String firstCategoryNameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(2)";
                        String priceRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(5)";

                        String partsGuidRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(9) > div > span.common-font.edit-act";
                        String partsGuid = doc.select(partsGuidRegEx).attr("onclick");
                        String getGUIDRegEx = "(?<=').*(?=')";
                        String guid = CommonUtil.fetchString(partsGuid, getGUIDRegEx);

                        String price = doc.select(priceRegEx).text();
                        String firstCategoryName = doc.select(firstCategoryNameRegEx).text();
                        String brandName = doc.select(brandNameRegEx).text();
                        String productName = doc.select(productNameRegEx).text();

                        Product product = new Product();
                        product.setPrice(price);
                        product.setFirstCategoryName(firstCategoryName);
                        product.setBrandName(brandName);
                        product.setProductName(productName);
                        productMap.put(guid, product);
                    }
                }
            }
        }

        if (productMap.size() > 0) {
            for (String guid : productMap.keySet()) {

                Response response = ConnectionUtil.doPostWithLeastParams(STOCKDETAIL_URL, getDetailParams("partsGuid", guid), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String carModelRegEx = "#mainDiv > div:nth-child(9) > div:nth-child(2) > table > tbody > tr:nth-child(2) > td:nth-child(2) > div:nth-child(2)";
                String carModel = doc.select(carModelRegEx).text();

                int trSize = WebClientUtil.getTagSize(doc, trStockRegEx, HtmlTag.trName);
                for (int i = 1; i <= trSize; i++) {
                    //规格作为商品编码
                    String specRegEx = "#set-tbody > tr:nth-child(" + i + ") > td:nth-child(1)";
                    String salePriceRegEx = "#set-tbody > tr:nth-child(" + i + ") > td:nth-child(2)";

                    String spec = doc.select(specRegEx).text();
                    String salePrice = doc.select(salePriceRegEx).text();

                    Product p = productMap.get(guid);
                    Product product = new Product();
                    product.setCode(spec);
                    product.setPrice(salePrice.replace("￥", ""));
                    product.setFirstCategoryName(p.getFirstCategoryName());
                    product.setBrandName(p.getBrandName());
                    product.setProductName(p.getProductName());
                    product.setCarModel(carModel);
                    product.setItemType("商品");
                    product.setCompanyName(companyName);
                    products.add(product);
                }
            }
        }

        String pathname = "C:\\exportExcel\\元乐车宝商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 服务项目-标准模版导出
     * 打开路径：库存管理-服务工时设置
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        int totalPage = getTotalPage(SERVICE_URL, getPageInfoParams("1"), totalRegEx, totalReplaceRegEx);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String codeRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(1)";
                        String productNameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(2)";
                        String priceRegEx = "#content-tbody > tr:nth-child(" + j + ")> td:nth-child(3)";
                        String firstCategoryNameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(4)";

                        String code = doc.select(codeRegEx).text();
                        String productName = doc.select(productNameRegEx).text();
                        String price = doc.select(priceRegEx).text().replace("￥", "");
                        String firstCategoryName = doc.select(firstCategoryNameRegEx).text();

                        Product product = new Product();
                        product.setCode(code);
                        product.setCompanyName(companyName);
                        product.setProductName(productName);
                        product.setPrice(price);
                        product.setItemType("服务项");
                        product.setFirstCategoryName(firstCategoryName);
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\元乐车宝服务项目.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 供应商-标准模版导出
     * 打开路径：库存管理-供应商管理
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Set<String> supplierDetails = new HashSet<>();

        int totalPage = getTotalPage(SUPPLIER_URL, getPageInfoParams("1"), totalPageRegEx, totalPageReplaceRegEx);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = response.returnContent().asString(WebConfig.CHARSET_UTF_8);
                Document doc = Jsoup.parse(html);

                for (int j = 1; j <= 10; j++) {
                    String supplierDetailRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(5) > a.supplierDetail";
                    String detailUrl = doc.select(supplierDetailRegEx).attr("content-url");

                    if (StringUtils.isNotBlank(detailUrl))
                        supplierDetails.add(detailUrl);
                }
            }
        }

        if (supplierDetails.size() > 0) {
            for (String supplierDetail : supplierDetails) {
                String preUrl = "http://wh.youchepi.cn";
                Response response = ConnectionUtil.doGetWith(preUrl + supplierDetail, COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String nameRegEx = "#content > div > div.row.row-d > div:nth-child(1) > div:nth-child(1) > div.col-md-7";
                String contactPhoneRegEx = "#content > div > div.row.row-d > div:nth-child(2) > div:nth-child(2) > div.col-md-7";
                String contactNameRegEx = "#content > div > div.row.row-d > div:nth-child(2) > div:nth-child(1) > div.col-md-7";
                String remarkRegEx = "#content > div > div.row.row-d > div:nth-child(3) > div > div.col-md-7";

                String name = doc.select(nameRegEx).text();
                String contactPhone = doc.select(contactPhoneRegEx).text();
                String contactName = doc.select(contactNameRegEx).text();
                String remark = doc.select(remarkRegEx).text();
                String fax = "";

                if (contactPhone.contains("，")) {
                    int index = contactPhone.indexOf("，");
                    fax = contactPhone.substring(index + 1, contactPhone.length());
                    contactPhone = contactPhone.substring(0, index);
                    int a = contactPhone.length();

                }

                Supplier supplier = new Supplier();
                supplier.setCompanyName(companyName);
                supplier.setName(name);
                supplier.setContactName(contactName);
                supplier.setContactPhone(contactPhone);
                supplier.setRemark(remark);
                supplier.setFax(fax);
                suppliers.add(supplier);
            }
        }

        String pathname = "C:\\exportExcel\\元乐车宝供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }


    /**
     * 车辆信息-标准模版导出
     * 打开路径:客户管理-车辆查询-详细-行驶证
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        int totalPage = getTotalPage(CARINFOPAGE_URL, getPageInfoParams("1"), totalPageRegEx, totalPageReplaceRegEx);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doPostWithLeastParams(CARINFOPAGE_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = response.returnContent().asString(WebConfig.CHARSET_UTF_8);
                Document doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String clientRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(9) > a:nth-child(1)";

                        String carId = doc.select(clientRegEx).attr("userid");
                        String phone = doc.select(clientRegEx).attr("mobile");
                        String name = doc.select(clientRegEx).attr("username");
                        String carnum = doc.select(clientRegEx).attr("carnum");
                        String cararea = doc.select(clientRegEx).attr("cararea");
                        String carNumber = cararea + carnum;

                        CarInfo carInfo = new CarInfo();
                        carInfo.setPhone(phone);
                        carInfo.setCompanyName(companyName);
                        carInfo.setName(name);
                        carInfo.setCarNum(carnum);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setCarArea(cararea);
                        carInfo.setCarId(carId);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String userName = carInfo.getName();
                String userId = carInfo.getCarId();
                String carNum = carInfo.getCarNum();
                String carArea = carInfo.getCarArea();
                String mobile = carInfo.getPhone();

                Response response = ConnectionUtil.doPostWithLeastParams(CARINFODETAIL_URL, getCarInfoDetailParams(userName, userId, carArea, carNum, mobile), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String carNumberRegEx = "#is_bang > div > div:nth-child(1) > span:nth-child(3)";
                String VINRegEx = "#isReset > table > tbody > tr > td:nth-child(1) > span";
                String brandRegEx = "#isReset > table > tbody > tr > td:nth-child(2)";
                String carModelRegEx = "#isReset > table > tbody > tr > td:nth-child(3)";

                String userCarIdRegEx = "[\\s\\S]*var userId[\\s\\S]*var userCarId = (\\d+)[\\s\\S]*";
                String userCarId = CommonUtil.filterString(doc.toString(), userCarIdRegEx);

                String VINcode = doc.select(VINRegEx).text().replace("VIN：", "");
                String carModel = doc.select(carModelRegEx).text().replace("车型：", "");
                String brand = doc.select(brandRegEx).text().replace("品牌：", "");

                carInfo.setUserCarId(userCarId);
                carInfo.setVINcode(VINcode.replace("未完善", ""));
                carInfo.setCarModel(carModel.replace("-", ""));
                carInfo.setBrand(brand.replace("-", ""));
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String userCarId = carInfo.getUserCarId();

                Response response = ConnectionUtil.doPostWithLeastParams(CARDRIVINGLICENSE_URL, getDetailParams("userCarId", userCarId), COOKIE);
                String html = response.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String engineNumberRegEx = "#engineCode";
                String registerDateRegEx = "#registerDate";
                String vcInsuranceValidDateRegEx = "#insuranceTimeEnd";
                String VINcodeRegEx = "#vin";

                String engineNumber = doc.select(engineNumberRegEx).text();
                String registerDate = doc.select(registerDateRegEx).text();
                String vcInsuranceValidDate = doc.select(vcInsuranceValidDateRegEx).text();
                String VINcode = doc.select(VINcodeRegEx).text();

                carInfo.setEngineNumber(engineNumber.replace("未完善", ""));
                carInfo.setRegisterDate(registerDate.replace("未完善", ""));
                carInfo.setVcInsuranceValidDate(vcInsuranceValidDate.replace("未完善", ""));
                carInfo.setVINcode(VINcode.replace("未完善", ""));
            }
        }

        String pathname = "C:\\exportExcel\\元乐车宝车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }


    /**
     * 工作台-单据(服务中)
     * <p>
     * 打开路径:工作台-服务中
     *
     * @throws IOException
     */
    @Test
    public void fetchBillInServiceData() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(BILLINSERVICE_URL, getPageInfoParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(BILLINSERVICE_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("orderCode").asText();
                    String dateAdded = element.get("orderDate").asText();
                    String totalAmount = element.get("dealAmount").asText();//totalAmount
                    String carNumber = element.get("carNumber").asText();

                    String clientName = "";
                    String clientPhone = "";
                    if (element.get("userInfo") != null) {
                        clientName = element.get("userInfo").get("name").asText();
                        clientPhone = element.get("userInfo").get("mobile").asText();
                    }

                    String brand = "";
                    String carModel = "";
                    String mileage = "";
                    if (element.get("userCarInfo") != null) {
                        brand = element.get("userCarInfo").get("brand").asText();
                        carModel = element.get("userCarInfo").get("modelName").asText();
                        mileage = element.get("userCarInfo").get("lastMaintainMiles").asText();
                    }

                    Bill bill = new Bill();
                    bill.setBillNo(billNo);
                    bill.setDateAdded(dateAdded);
                    bill.setDateEnd(dateAdded);
                    bill.setDateExpect(dateAdded);
                    bill.setTotalAmount(totalAmount);
                    bill.setActualAmount(totalAmount);
                    bill.setCarNumber(carNumber);
                    bill.setName(clientName);
                    bill.setPhone(clientPhone);
                    bill.setBrand(brand);
                    bill.setCarModel(carModel);
                    bill.setMileage(mileage);
                    bills.add(bill);
                }
            }
        }


        System.out.println("结果为" + bills.toString());
        System.out.println("大小为" + bills.size());

        String pathname = "C:\\exportExcel\\元乐车宝单据.xls";
        ExportUtil.exportBillSomeFieldDataInLocal(bills, ExcelDatas.workbook, pathname);
    }

    /**
     * 工作台-单据明细(服务中)
     * <p>
     * 打开路径:工作台-服务中-加开服务
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDetailInServiceData() throws IOException {
        List<BillDetail> billDetails = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(BILLINSERVICE_URL, getPageInfoParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(BILLINSERVICE_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carNumber = element.get("carNumber").asText();
                    String totalAmount = element.get("totalAmount").asText();
                    String dateAdded = element.get("orderDate").asText();
                    String billNo = element.get("orderCode").asText();

                    Iterator<JsonNode> details = element.get("orderItemInfo").iterator();
                    while (details.hasNext()) {
                        JsonNode e = details.next();

                        String itemName = e.get("itemName").asText();
                        String salePrice = e.get("itemPrice").asText();//原价
                        String price = e.get("dealPrice").asText();//折扣价
                        String num = e.get("quantity").asText();
                        String itemType = "";

                        if (e.get("serviceInfo") != null)
                            itemType = "服务项";

                        if (e.get("partsInfo") != null) {
                            itemType = "配件";

                            String categoryName = e.get("partsInfo").get("categoryName").asText();
                            String brandName = e.get("partsInfo").get("brandName").asText();
                            String partsName = e.get("partsInfo").get("partsName").asText();

                            itemName = categoryName + "-" + brandName + "-" + partsName;
                        }


                        BillDetail billDetail = new BillDetail();
                        billDetail.setBillNo(billNo);
                        billDetail.setCarNumber(carNumber);
                        billDetail.setDateAdded(dateAdded);
                        billDetail.setTotalAmount(totalAmount);
                        billDetail.setItemName(itemName);
                        billDetail.setPrice(price);
                        billDetail.setSalePrice(salePrice);
                        billDetail.setNum(num);
                        billDetail.setItemType(itemType);
                        billDetails.add(billDetail);
                    }
                }
            }
        }

        System.out.println("结果为" + billDetails.toString());
        System.out.println("大小为" + billDetails.size());

        String pathname = "C:\\exportExcel\\元乐车宝单据明细.xls";
        ExportUtil.exportBillDetailSomeFieldDataInLocal(billDetails, ExcelDatas.workbook, pathname);
    }


    /**
     * 卡内项目-标准模版导出
     * （可用套卡不为0）
     * <p>
     * 打开路径:前台服务-客户服务-详细
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, MemberCard> packageMap = new HashMap<>();

        Map<String, MemberCard> memberCardMap = getMemberHasPackage();
        if (memberCardMap.size() > 0) {
            for (String userId : memberCardMap.keySet()) {

                MemberCard card= memberCardMap.get(userId);
                System.out.println("--------名字为" + card.getName() + "---" + "---------手机号码为" + card.getPhone() + "---");
                Response res = ConnectionUtil.doPostWithLeastParams(CLIENTDETAIL_URL, getDetailParams("userId", userId), COOKIE);
                String content = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(content);

                String trRegEx = "#staleDated-content-tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {

                        String packageIdRegEx = "#staleDated-content-tbody > tr:nth-child(" + i + ") > td:nth-child(9) > a";
                        String packageId = doc.select(packageIdRegEx).attr("packageId");

                        MemberCard memberCard = memberCardMap.get(userId);
                        packageMap.put(packageId, memberCard);
                    }
                }
            }
        }

        if (packageMap.size() > 0) {
            for (String packageId : packageMap.keySet()) {

                MemberCard memberCard = packageMap.get(packageId);

                String url = getMemberCardItemURL(memberCard.getUserId(), packageId);
                Response res = ConnectionUtil.doGetWith(url, COOKIE);
                String content = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(content);

                String divRowRegEx = "div[class='recordCardInfo']  > div[class=row] > div[class=col-sm-4]";
                Elements elements = doc.select(divRowRegEx).tagName("div");
                String validTime = elements.get(4).getElementsByTag("span").get(1).text();

                String isValidForever = CommonUtil.getIsValidForever(validTime);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {
                        String itemNameRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(2)";
                        String originalNumRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(3)";
                        String numRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(5)";
                        String remarkRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(6)";

                        String itemName = doc.select(itemNameRegEx).text();
                        String originalNum = doc.select(originalNumRegEx).text();
                        String num = doc.select(numRegEx).text();
                        String remark = doc.select(remarkRegEx).text();

                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setCompanyName(companyName);
                        memberCardItem.setDiscount(memberCard.getCardSort());
                        memberCardItem.setCardCode(memberCard.getCardCode());
                        memberCardItem.setItemName(itemName);
                        memberCardItem.setOriginalNum(originalNum.replace("次数", ""));
                        memberCardItem.setNum(num.replace("次数", ""));
                        memberCardItem.setRemark(remark);
                        memberCardItem.setIsValidForever(isValidForever);
                        memberCardItem.setValidTime(DateUtil.formatSQLDateTime(validTime));
                        memberCardItems.add(memberCardItem);
                    }
                }
            }
        }

        if (memberCardItems.size() > 0) {
            for (MemberCardItem memberCardItem : memberCardItems) {
                String itemName = memberCardItem.getItemName();

                Response res = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getServiceParams("1", itemName), COOKIE);
                String content = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(content);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {
                        String codeRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(1)";
                        String priceRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(3)";
                        String firstCategoryNameRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(4)";
                        String itemRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(2)";

                        String name = doc.select(itemRegEx).text();
                        String firstCategoryName = doc.select(firstCategoryNameRegEx).text();
                        String price = doc.select(priceRegEx).text();
                        String code = doc.select(codeRegEx).text();

                        if (!name.equals(itemName))
                            continue;

                        memberCardItem.setCode(code);
                        memberCardItem.setPrice(price.replace("￥", ""));
                        memberCardItem.setFirstCategoryName(firstCategoryName);
                    }
                }
            }
        }

        System.out.println("memberCardItems结果为" + memberCardItems.toString());
        System.out.println("memberCardItems大小为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\元乐车宝卡内项目.xlsx";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);

    }


    /**
     * 会员卡-标准模版导出
     * 支持一卡多车（会员卡余额不为0.0或者可用套卡不为0）
     * <p>
     * 打开路径:前台服务-客户服务-详细
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        Map<String, MemberCard> memberCardMap = getMemberHasPackage();

        if (memberCardMap.size() > 0) {
            for (String userId : memberCardMap.keySet()) {
                Response res = ConnectionUtil.doPostWithLeastParams(CLIENTDETAIL_URL, getDetailParams("userId", userId), COOKIE);
                String content = res.returnContent().asString();
                Document document = Jsoup.parseBodyFragment(content);

                //int divSize = WebClientUtil.getTagSize(document, divRegEx, divName); 此方法无法判断divSize
                String divRowRegEx = "div[class='userCars']  > div[class=row] > div[class=col-sm-2]";
                Elements elements = document.select(divRowRegEx).tagName("div");
                if (elements.size() > 0) {
                    for (Element e : elements) {
                        Element carSpanElement = e.getElementsByTag("a").get(1).tagName("span");
                        String carNumberRegEx = "span[onclick=findCarDetail(this)]";
                        String cararea = carSpanElement.select(carNumberRegEx).attr("cararea");
                        String carnum = carSpanElement.select(carNumberRegEx).attr("carnum");
                        String carNumber = cararea + carnum;
                        //String carNumberRegEx = "div[class='userCars'] > div[class='row'] > div:nth-child({no}) > a:nth-child(2)";

                        String dateCreated = "";
                        String dateRegEx = "#staleDated-content-tbody";
                        String dateStr = document.select(dateRegEx).text();
                        if (StringUtils.isBlank(dateStr)) {
                            dateCreated = "1900-01-01";
                        } else {
                            String trRegEx = "#staleDated-content-tbody > tr";
                            int trSize = WebClientUtil.getTagSize(document, trRegEx, HtmlTag.trName);
                            if (trSize > 0) {
                                for (int i = 1; i <= trSize; i++) {

                                    String dateCreatedRegEx = "#staleDated-content-tbody > tr:nth-child({no}) > td:nth-child(6)";
                                    dateCreated = document.select(StringUtils.replace(dateCreatedRegEx, "{no}", String.valueOf(i))).text();
                                    dateList.add(dateCreated);
                                }
                            }

                            if (dateList.size() > 0)
                                dateCreated = DateUtil.getEarliestDate(dateList);
                        }


                        MemberCard m = memberCardMap.get(userId);
                        MemberCard memberCard = new MemberCard();
                        memberCard.setCarNumber(carNumber);
                        memberCard.setName(m.getName());
                        memberCard.setPhone(m.getPhone());
                        memberCard.setBalance(m.getBalance());
                        memberCard.setMemberCardName(m.getMemberCardName());
                        memberCard.setCardCode(m.getCardCode());
                        memberCard.setCardSort(m.getCardSort());
                        memberCard.setDateCreated(DateUtil.formatSQLDateTime(dateCreated));
                        memberCard.setCompanyName(companyName);
                        memberCards.add(memberCard);
                    }
                }
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("结果为" + memberCards.size());

        String pathname = "C:\\exportExcel\\元乐车宝会员卡.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);

    }


    /**
     * 会员卡
     * <p>
     * 打开路径:会员管理-详细
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Map<String, MemberCard> memberCardMap = new HashMap<>();

        String url = MEMBERCARDOVERVIEW_URL + "shopBranchId=" + shopBranchId + "&shopId=" + companyId + "&staffId=" + staffId;
        Response response = ConnectionUtil.doGetWith(url, COOKIE);
        String html = response.returnContent().asString(WebConfig.CHARSET_UTF_8);
        Document doc = Jsoup.parse(html);

        //会员管理获取所有会员等级
        int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
        if (trSize > 0) {
            for (int i = 1; i <= trSize; i++) {

                String gradeIdRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(6) > a";
                String gradeRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(2)";
                String discountRegEx = "#content-tbody > tr:nth-child(" + i + ") > td:nth-child(3)";
                String remarkRegEx = "#content-tbody > tr:nth-child(" + i + ") > td.ruleContent";

                String gradeId = doc.select(gradeIdRegEx).attr("gradeid");
                String grade = doc.select(gradeRegEx).text();
                String discount = doc.select(discountRegEx).text();
                String remark = doc.select(remarkRegEx).text();

                MemberCard memberCard = new MemberCard();
                memberCard.setGrade(grade);
                memberCard.setDiscount(discount);
                memberCard.setRemark(remark);
                memberCardMap.put(gradeId, memberCard);
            }
        }

        for (String gradeId : memberCardMap.keySet()) {
            MemberCard memberCard = memberCardMap.get(gradeId);

            Response r = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getMemberCardParams("1", gradeId), COOKIE);
            String content = r.returnContent().asString(WebConfig.CHARSET_UTF_8);
            doc = Jsoup.parse(content);

            String regEx = "[\\s\\S]*var pageNo[\\s\\S]*var totalPage = (\\d+)[\\s\\S]*";
            String totalStr = CommonUtil.filterString(doc.toString(), regEx);
            int total = Integer.parseInt(totalStr);

            if (total > 0) {
                for (int i = 1; i <= total; i++) {
                    Response res = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getMemberCardParams(String.valueOf(i), gradeId), COOKIE);
                    String page = res.returnContent().asString(WebConfig.CHARSET_UTF_8);
                    doc = Jsoup.parse(page);

                    trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
                    if (trSize > 0) {

                        for (int j = 1; j <= trSize; j++) {

                            String cardCodeRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(3)";
                            String nameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(2)";
                            String balanceRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(4)";

                            String cardCode = doc.select(cardCodeRegEx).text();
                            String name = doc.select(nameRegEx).text();
                            String balance = doc.select(balanceRegEx).text();

                            MemberCard m = new MemberCard();
                            m.setCompanyName(companyName);
                            m.setCardCode(cardCode);
                            m.setName(name);
                            m.setPhone(cardCode);
                            m.setBalance(balance);
                            m.setGrade(memberCard.getGrade());
                            m.setDiscount(memberCard.getDiscount());
                            m.setRemark(memberCard.getRemark());
                            memberCards.add(m);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\元乐车宝会员卡导出(会员等级).xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    /**
     * 获取会员数量
     * <p>
     * 打开路径:前台服务-客户服务
     *
     * @return
     * @throws IOException
     */
    private Map<String, MemberCard> getMemberHasPackage() throws IOException {
        Map<String, MemberCard> memberCardMap = new HashMap<>();

        int totalPage = getTotalPage(CLIENTLIST_URL, getPageInfoParams("1"), totalPageRegEx, totalPageReplaceRegEx);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(CLIENTLIST_URL, getPageInfoParams(String.valueOf(i)), COOKIE);
                String html = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= 10; j++) {
                        String nameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(2)";
                        String phoneRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(3)";
                        String memberCardNameRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(6)";//会员等级
                        String clientRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(9) > a:nth-child(1)";//序号
                        String balanceRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(4)";
                        String hasPackageRegEx = "#content-tbody > tr:nth-child(" + j + ") > td:nth-child(5)";

                        String name = doc.select(nameRegEx).text();
                        String phone = doc.select(phoneRegEx).text();
                        String memberCardName = doc.select(memberCardNameRegEx).text();
                        String balance = doc.select(balanceRegEx).text();
                        String userId = doc.select(clientRegEx).attr("userid");
                        String hasPackage = doc.select(hasPackageRegEx).text();
                        String cardSort = String.valueOf(random.nextInt());//随机数用于卡号

                        //会员卡余额为0.0&&可用套卡为0
                        if ("0".equals(hasPackage) && "0.0".equals(balance))
                            continue;

                        if ("-".equals(memberCardName) || StringUtils.isBlank(memberCardName))
                            memberCardName = "普通会员卡";

                        MemberCard memberCard = new MemberCard();
                        memberCard.setCardCode(phone);//手机号作为卡号
                        memberCard.setName(name);
                        memberCard.setPhone(phone);
                        memberCard.setMemberCardName(memberCardName);
                        memberCard.setBalance(balance);
                        memberCard.setUserId(userId);
                        memberCard.setCardSort(cardSort.replace("-", ""));
                        memberCardMap.put(userId, memberCard);
                    }
                }
            }
        }

        return memberCardMap;
    }


    private List<BasicNameValuePair> getMemberCardParams(String pageNo, String gradeId) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("shopGradeId", gradeId));
        params.add(new BasicNameValuePair("keyWord", ""));
        params.add(new BasicNameValuePair("shopBranchId", shopBranchId));
        params.add(new BasicNameValuePair("staffId", "2341"));

        return params;
    }

    private List<BasicNameValuePair> getStockInPriceParams(String partsGuid, String specificationGuid) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("branchId", shopBranchId));
        params.add(new BasicNameValuePair("staffId", "4574"));
        params.add(new BasicNameValuePair("beginTimeStr", ""));
        params.add(new BasicNameValuePair("endTimeStr", ""));
        params.add(new BasicNameValuePair("pageNo", "1"));
        params.add(new BasicNameValuePair("pageSize", "5"));
        params.add(new BasicNameValuePair("orderBy", "-1"));
        params.add(new BasicNameValuePair("specificationGuid", specificationGuid));
        params.add(new BasicNameValuePair("partsGuid", partsGuid));

        return params;
    }


    private List<BasicNameValuePair> getServiceParams(String pageNo, String keyword) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("keyword", keyword));
        params.add(new BasicNameValuePair("categoryId", ""));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        return params;
    }

    private List<BasicNameValuePair> getDetailParams(String name, String value) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(name, value));
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("shopBranchId", shopBranchId));

        return params;
    }

    private List<BasicNameValuePair> getCarInfoDetailParams(String userName, String userId, String carArea, String
            carNum, String mobile) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("staffId", "1649"));
        params.add(new BasicNameValuePair("shopBranchId", shopBranchId));
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("carArea", carArea));
        params.add(new BasicNameValuePair("carNum", carNum));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("pageType", "2"));
        return params;
    }

    private List<BasicNameValuePair> getPageInfoParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopBranchId", shopBranchId));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        params.add(new BasicNameValuePair("staffId", "4478"));
        return params;
    }

    private String getDiscount(String salePrice, String price) {
        int sp = Integer.parseInt(salePrice);
        int p = Integer.parseInt(price);
        DecimalFormat df = new DecimalFormat("0.00");

        String res = df.format((float) p / (float) sp);

        return res;
    }

    private String getMemberCardItemURL(String userId, String packageId) {
        String url = "http://wh.youchepi.cn/Home/receptionService/recordCardDetail?" +
                "userId=" + userId +
                "&packageId=" + packageId +
                "&shopBranchId=" + shopBranchId +
                "&shopId=" + companyId +
                "&staffId=" + staffId;
        return url;
    }

    private int getTotalPage(String url, List<BasicNameValuePair> params, String regEx, String replaceRegEx) throws
            IOException {
        Response response = ConnectionUtil.doPostWithLeastParams(url, params, COOKIE);
        String html = response.returnContent().asString(WebConfig.CHARSET_UTF_8);
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), regEx).replace(replaceRegEx, "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        return totalPage;
    }


    private JsonNode getDataNode(Document document, String startRegEx, String endRegEx) throws IOException {
        String docString = document.toString();
        int start = docString.indexOf(startRegEx);
        int end = docString.indexOf(endRegEx);

        String dataStr = docString.substring(start, end);
        String data = "{" + dataStr + "}";
        JsonNode node = JsonObject.MAPPER.readTree(data);

        return node;
    }

}
