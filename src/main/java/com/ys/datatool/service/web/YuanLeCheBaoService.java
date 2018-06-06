package com.ys.datatool.service.web;

import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.Product;
import com.ys.datatool.domain.Stock;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018/5/14.
 * 元乐车宝
 */
@Service
public class YuanLeCheBaoService {

    private static final String STOCKINSEARCH_URL = "http://www.carbao.vip/Home/storage/storageInSearchByPartsGuid";

    private static final String STOCKINDIV_URL = "http://www.carbao.vip/Home/partsinfo/storageInDiv";

    private static final String STOCKDETAIL_URL = "http://www.carbao.vip/Home/partsinfo/partsInfo";

    private static final String STOCK_URL = "http://www.carbao.vip/Home/cbstoragepartsinventory/invenManagementTable";

    private static final String SERVICE_URL = "http://www.carbao.vip/Home/service/serviceTable";

    private static final String SUPPLIER_URL = "http://www.carbao.vip/Home/cbpartssupplier/supplierTable";

    private static final String CARINFOPAGE_URL = "http://www.carbao.vip/Home/car/carTable";

    private static final String CARINFODETAIL_URL = "http://www.carbao.vip/Home/car/carDetail";

    private String trItemRegEx = "#content-tbody > tr";

    private String totalPageRegEx = "totalPage =.*";

    private String totalRegEx = "totalPage=.*";

    private Workbook workbook;

    /**
     * 车店编号-shopId:
     * 215(冠军养护)、183(迅驰)、208(稳中快)、
     * 77(石家庄丽雷行)、140(天骐汽车)、132(路胜通汽车)、
     * 288(良匠汽车)、70(黑妞汽车)、82(国瑞汽修厂)、284(车来车旺美车会所)
     * 79(广州市花都区明杰)
     */
    private String companyId = "284";

    private static final String COOKIE = "JSESSIONID=AFFAC63705DE1E1E3FED991A9D337C69; usfl=FxnbV6HgdGzEhcgHWdE; lk=f47446288e43e1cf9d797b7d1749b653";

    @Test
    public void test() throws IOException {

        Map<String, String> specificationGuids = new HashMap<>();
        Response response = ConnectionUtil.doPostWithLeastParams(STOCKINDIV_URL, getStockDetailParams("11d81f5b-6d17-40e8-ac36-b99837235616"), COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String optionRegEx = "#specification_search_in > option";
        int optionSize = getOptionSize(doc, optionRegEx);

        System.out.println("optionSize结果为" + optionSize);
        System.out.println("specificationGuids大小为" + specificationGuids.size());
        System.out.println("specificationGuids结果为" + specificationGuids.toString());

    }


    @Test
    public void fetchStockData() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        Set<String> guids = new HashSet<>();
        Map<String, String> specificationGuids = new HashMap<>();

        Response response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getStockParams("1"), COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalRegEx).replace("totalPage=", "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getStockParams(String.valueOf(i)), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTRSize(doc, trItemRegEx);
                for (int j = 1; j <= trSize; j++) {

                    String getGUIDRegEx = "(?<=').*(?=')";
                    String partsGuidRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(9) > div > span.common-font.edit-act";
                    String partsGuid = doc.select(StringUtils.replace(partsGuidRegEx, "{no}", String.valueOf(i))).attr("onclick");
                    String guid = CommonUtil.fetchString(partsGuid, getGUIDRegEx);
                    guids.add(guid);
                }
            }
        }

        if (guids.size() > 0) {
            for (String guid : guids) {
                response = ConnectionUtil.doPostWithLeastParams(STOCKINDIV_URL, getStockDetailParams(guid), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String optionRegEx = "#specification_search_in > option";
                int optionSize = getOptionSize(doc, optionRegEx);

                if (optionSize > 0) {
                    for (int i = 2; i <= optionSize; i++) {
                        String specRegEx = "#specification_search_in > option:nth-child({no})";
                        String spec = doc.select(StringUtils.replace(specRegEx, "{no}", String.valueOf(i))).text();
                        String specValue = doc.select(StringUtils.replace(specRegEx, "{no}", String.valueOf(i))).attr("value");

                        specificationGuids.put(spec, specValue);
                    }
                }
            }
        }

        if (guids.size() > 0) {
            for (String guid : guids) {
                response = ConnectionUtil.doPostWithLeastParams(STOCKDETAIL_URL, getStockDetailParams(guid), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String firstCategoryNameRegEx = "#mainDiv > div:nth-child(9) > div:nth-child(2) > table > tbody > tr:nth-child(1) > td:nth-child(1) > div:nth-child(2)";
                String brandRegEx = "#mainDiv > div:nth-child(9) > div:nth-child(2) > table > tbody > tr:nth-child(1) > td:nth-child(2) > div:nth-child(2)";
                String remarkRegEx = "#mainDiv > div:nth-child(9) > div:nth-child(2) > table > tbody > tr:nth-child(2) > td:nth-child(1) > div:nth-child(2)";

                String firstCategoryName = doc.select(firstCategoryNameRegEx).text();
                String brand = doc.select(brandRegEx).text();
                String remark = doc.select(remarkRegEx).text();//配件型号

                String trRegEx = "#set-tbody > tr";
                int trSize = WebClientUtil.getTRSize(doc, trRegEx);
                for (int j = 1; j <= trSize; j++) {

                    String specRegEx = "#set-tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String inventoryNumRegEx = "#set-tbody > tr:nth-child({no}) > td:nth-child(5)";
                    String salePriceRegEx = "#set-tbody > tr:nth-child({no}) > td:nth-child(2)";

                    String spec = doc.select(StringUtils.replace(specRegEx, "{no}", String.valueOf(j))).text();
                    String inventoryNum = doc.select(StringUtils.replace(inventoryNumRegEx, "{no}", String.valueOf(j))).text();
                    String salePrice = doc.select(StringUtils.replace(salePriceRegEx, "{no}", String.valueOf(j))).text();

                    //拼接商品名称
                    String goodsName = firstCategoryName + brand + remark + spec;

                    Stock stock = new Stock();
                    stock.setGoodsName(goodsName);
                    stock.setFirstCategoryName(firstCategoryName);
                    stock.setBrand(brand);
                    stock.setRemark(remark);
                    stock.setSpec(spec);
                    stock.setInventoryNum(inventoryNum);
                    stock.setSalePrice(salePrice.replace("￥", ""));
                    stock.setPartsGuid(guid);
                    stocks.add(stock);
                }
            }
        }

        if (stocks.size() > 0) {
            for (Stock stock : stocks) {
                String spec = stock.getSpec();
                String partsGuid = stock.getPartsGuid();
                String specificationGuid = specificationGuids.get(spec);

                if (specificationGuid != null) {
                    response = ConnectionUtil.doPostWithLeastParams(STOCKINSEARCH_URL, getStockInPriceParams(partsGuid, specificationGuid), COOKIE);
                    html = response.returnContent().asString();
                    doc = Jsoup.parse(html);

                    //取第一条入库记录中的成本价
                    String priceRegEx = "#content-tbody > tr:nth-child(1) > td:nth-child(7)";
                    String price = doc.select(priceRegEx).text().replace("￥", "");
                    stock.setPrice(price);
                }
            }
        }

        System.out.println("结果为" + stocks.toString());
        System.out.println("大小为" + stocks.size());

        String pathname = "D:\\元乐车宝库存导出.xls";
        ExportUtil.exportYuanLeCheBaoStockDataInLocal(stocks, workbook, pathname);
    }

    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getServiceParams("1"), COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalRegEx).replace("totalPage=", "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getSupplierParams(String.valueOf(i)), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTRSize(doc, trItemRegEx);
                for (int j = 1; j <= trSize; j++) {
                    String codeRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String productNameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(2)";
                    String priceRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(3)";
                    String firstCategoryNameRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(4)";

                    Product product = new Product();
                    product.setCode(doc.select(StringUtils.replace(codeRegEx, "{no}", String.valueOf(j))).text());
                    product.setProductName(doc.select(StringUtils.replace(productNameRegEx, "{no}", String.valueOf(j))).text());
                    product.setPrice(doc.select(StringUtils.replace(priceRegEx, "{no}", String.valueOf(j))).text().replace("￥", ""));
                    product.setFirstCategoryName(doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(j))).text());
                    products.add(product);
                }
            }
        }
        System.out.println("结果为" + totalPage);
        System.out.println("结果为" + products.toString());
        System.out.println("大小为" + products.size());


        String pathname = "D:\\元乐车宝服务项目导出.xls";
        ExportUtil.exportProductDataInLocal(products, workbook, pathname);
    }

    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Set<String> supplierDetails = new HashSet<>();

        Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getSupplierParams("1"), COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalPageRegEx).replace("totalPage = ", "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getSupplierParams(String.valueOf(i)), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                for (int j = 1; j <= 10; j++) {
                    String supplierDetailRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(5) > a.supplierDetail";
                    String detailUrl = doc.select(StringUtils.replace(supplierDetailRegEx, "{no}", String.valueOf(j))).attr("content-url");

                    if (StringUtils.isNotBlank(detailUrl))
                        supplierDetails.add(detailUrl);
                }
            }
        }

        if (supplierDetails.size() > 0) {
            for (String supplierDetail : supplierDetails) {
                String preUrl = "http://www.carbao.vip";
                response = ConnectionUtil.doGetWithLeastParams(preUrl + supplierDetail, COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String nameRegEx = "#content > div > div.row.row-d > div:nth-child(1) > div:nth-child(1) > div.col-md-7";
                String contactPhoneRegEx = "#content > div > div.row.row-d > div:nth-child(2) > div:nth-child(2) > div.col-md-7";
                String contactNameRegEx = "#content > div > div.row.row-d > div:nth-child(2) > div:nth-child(1) > div.col-md-7";
                String remarkRegEx = "#content > div > div.row.row-d > div:nth-child(3) > div > div.col-md-7";

                Supplier supplier = new Supplier();
                supplier.setName(doc.select(nameRegEx).text());
                supplier.setContactName(doc.select(contactNameRegEx).text());
                supplier.setContactPhone(doc.select(contactPhoneRegEx).text());
                supplier.setRemark(doc.select(remarkRegEx).text());
                suppliers.add(supplier);
            }
        }
        System.out.println("结果为" + totalPage);
        System.out.println("结果为" + supplierDetails.toString());
        System.out.println("大小为" + supplierDetails.size());
        System.out.println("结果为" + suppliers.toString());
        System.out.println("大小为" + suppliers.size());

        String pathname = "D:\\元乐车宝供应商导出.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, workbook, pathname);

    }

    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(CARINFOPAGE_URL, getCarInfoPageParams("1"), COOKIE);
        String html = response.returnContent().asString();
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalPageRegEx).replace("totalPage = ", "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CARINFOPAGE_URL, getCarInfoPageParams(String.valueOf(i)), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                int trSize = WebClientUtil.getTRSize(doc, trItemRegEx);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String clientRegEx = "#content-tbody > tr:nth-child({no}) > td:nth-child(9) > a:nth-child(1)";

                        String carId = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("userid");
                        String phone = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("mobile");
                        String name = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("username");
                        String carnum = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("carnum");
                        String cararea = doc.select(StringUtils.replace(clientRegEx, "{no}", String.valueOf(j))).attr("cararea");
                        String carNumber = cararea + carnum;

                        CarInfo carInfo = new CarInfo();
                        carInfo.setPhone(phone);
                        carInfo.setName(name);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setCarNum(carnum);
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
                String carArea = carInfo.getCarArea();
                String carNum = carInfo.getCarNum();
                String mobile = carInfo.getPhone();

                response = ConnectionUtil.doPostWithLeastParams(CARINFODETAIL_URL, getCarInfoDetailParams(userName, userId, carArea, carNum, mobile), COOKIE);
                html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String carNumberRegEx = "#is_bang > div > div:nth-child(1) > span:nth-child(3)";
                String VINRegEx = "#isReset > table > tbody > tr > td:nth-child(1) > span";
                String brandRegEx = "#isReset > table > tbody > tr > td:nth-child(2)";
                String carModelRegEx = "#isReset > table > tbody > tr > td:nth-child(3)";

                String VINcode = doc.select(VINRegEx).text().replace("VIN：", "");
                String carModel = doc.select(carModelRegEx).text().replace("车型：", "");
                String brand = doc.select(brandRegEx).text().replace("品牌：", "");

                carInfo.setVINcode(VINcode.replace("未完善", ""));
                carInfo.setCarModel(carModel.replace("-", ""));
                carInfo.setBrand(brand.replace("-", ""));
            }
        }

        System.out.println("结果为" + totalPageStr);
        System.out.println("结果为" + totalPage);
        System.out.println("车辆分别为" + carInfos.toString());
        System.out.println("车辆大小为" + carInfos.size());

        String pathname = "D:\\元乐车宝车辆信息导出.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);
    }

    private List<BasicNameValuePair> getStockInPriceParams(String partsGuid, String specificationGuid) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("branchId", "299"));
        params.add(new BasicNameValuePair("staffId", "4574"));
        params.add(new BasicNameValuePair("beginTimeStr", ""));
        params.add(new BasicNameValuePair("endTimeStr", ""));
        params.add(new BasicNameValuePair("partsProperty", ""));
        params.add(new BasicNameValuePair("pageNo", "1"));
        params.add(new BasicNameValuePair("pageSize", "5"));
        params.add(new BasicNameValuePair("orderBy", "-1"));
        params.add(new BasicNameValuePair("specificationGuid", specificationGuid));
        params.add(new BasicNameValuePair("partsGuid", partsGuid));

        return params;
    }

    private List<BasicNameValuePair> getStockDetailParams(String partsGuid) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("partsGuid", partsGuid));

        return params;
    }

    private List<BasicNameValuePair> getStockParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));

        return params;
    }

    private List<BasicNameValuePair> getSupplierParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("keyword", ""));
        params.add(new BasicNameValuePair("payType", ""));
        params.add(new BasicNameValuePair("shopBranchId", "299"));
        params.add(new BasicNameValuePair("staffId", "4574"));
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        return params;
    }

    private List<BasicNameValuePair> getServiceParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("keyword", ""));
        params.add(new BasicNameValuePair("categoryId", ""));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        return params;
    }

    private List<BasicNameValuePair> getCarInfoDetailParams(String userName, String userId, String carArea, String carNum, String mobile) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("staffId", "1649"));
        params.add(new BasicNameValuePair("shopBranchId", "95"));
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("carArea", carArea));
        params.add(new BasicNameValuePair("carNum", carNum));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("pageType", "2"));
        return params;
    }

    private List<BasicNameValuePair> getCarInfoPageParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("shopBranchId", "229"));
        params.add(new BasicNameValuePair("staffId", "3468"));
        params.add(new BasicNameValuePair("shopId", companyId));//车店编号
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNo", pageNo));
        return params;
    }

    private int getOptionSize(Document document, String optionRegEx) {
        int optionSize = document.select(optionRegEx).tagName("option").size();

        return optionSize > 0 ? optionSize : 0;
    }

}
