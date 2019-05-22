package com.ys.datatool.service.web;

import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.HtmlTag;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 2019/5/21
 * 惠车邦智慧门店
 */
@Service
public class HuiCheBangService {

    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private String COOKIE = "HCBSMART_LOGIN_KEY=A50ADD70CC4E82166C3D4AB1EADC1CB6064F1B4278EAB8759382A6496FBC33186F2BB6A20383BC053EA3146D12ABFFE0D4C3D78980EA37EE65E48737924FBF47AD5A2435A768475634A01CA5D980DAC69D4EC9436A0CBC3C3217716BE12742840A6B78B43075889277B3A128C030E63CFFA6C4BAA06603D1D97C5E2F3262B9F0462B3CDE09C487C8840DCCA9FD5DC7EC2F9D5390B05A9ADFF5CB5C11F9A138E0A9F92BF3CB1D85993A593B2E83996CCA6928113DEB68BEFB3705C061F7C7A5D62EA783BB83A40D976C650813FD401180F4E73D63C27B95F60C8AB4B134E0B1CB805B3AA39D3A18CD258E3D3048B3C3804547266034BE1E98D631600749FA6E6CBAB9A464B32FFD1B8BF1303DDE105BF62FE3AC793C9CBCC333BE660F05D29829D2FDFF7EA80CCED361738A83BFDC59D0E558366075ACF7D1E9E2137A5BE8DFC2B7EEE90C0D083597FD823BCFDD77E4AE3D2FF8AD9D8608D7B7321988E2B4995F20F107E1CF567EE699833DD17C7B9856B32CEE25E9F4FAE137DD1503B328DA2F7991E59ABCAAEEBD0DF73F91658927DA44B4DE6987E78EFA78FB4632D67C939DF901571842FC6F9E58FDB0A6A4D7A9C00352594AE90915999A45644480FFE49960BDC9F4AE7D8E0FDD26E9CFA449A8C38AF623B5347F63A5601A02D662CE3D8BD96B4837349B8FC13D80093058E7D33EDEC5A8A8C4FE07A336AF7D215AB630D9A14EB26C32C2459C305C0B8C18B480638A84DFC7EF368823FF0A2DDFB47B33C36562B2127557DC85F66EA1AC1464A33D9D24AB15F6BD8D77685AA9A685DC26514071D2FB7186B26D8E05D3541E14698CB9459098E0D7E260C5A2DF25F6FF96C25B70B1BB40C39EC14684053B90DC0AC61DEAB4A08B01A0932A3732CB6764416511853DBFA323AD93F21A3B232FF6826248B87D1DC7B327BCEF0DDA0E8269AF2C8C1A02ACACAAAA073055DFD421D4C94E4EC2947DBC3FEB575038219D9AE33C8C4F59F759A1657F83683121800117A3794F603D35D846C8F3ED94439CE74D21731B685B7423061611BFCD296A247ED6AE9DFFCCDFBAA4817B8958E85968450631A07AB64E478257BBB5D980F33A57516CA7A565111DB25E21065619FB65F6B3A621F6562A7BB258DC37D27BE098732EE1B68CAA8F6B9385EA4670F252CDA41300A59EBBE80CCE0D0FD3772541FF0C9D77FF883D3ABD7E684AE9D5C83DBCAAEA381514D784CF0792DF5D032386F6E1BE676628275CDF695739D579B39866FE0C3FED9E7631BA21A259580B5743EBCFC5359DD20B682F015458163AE869D17C74D2C4B625EECF4A1FE4A4A179F5F025C98EA3B7F3198A4FF5134CE90264DA5F04A4EE64B09F56B106C3E1BFFECB4F05FD257E7008C4DBE81651E17F3A8AEBE714A0E98D44D7FF1992F576432E6790387FA45B900F9C04C73EECD4844C9C60B47389; HCBSMART_CID=487; TVIP=http%3A%2F%2F192.168.1.104%3A23021; ASP.NET_SessionId=dmvx3tlnntdjlioqce55axll";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String SERVICE_URL = "http://crm.huichebang.cn/Config/ProjectsConfig?v=3.3";

    private String STOCK_URL = "http://crm.huichebang.cn/Stock/ProductSKUList?ShowWay=-1&Sort=0&IsShow=1&pageIndex=";

    private String BILLDETAIL_URL = "http://crm.huichebang.cn/Config/ConsumeDetail?crid=";

    private String BILL_URL = "http://crm.huichebang.cn/Config/ConsumeRecordList?RoleText1=All&pageIndex=";

    private String CARINFODETAIL_URL = "http://crm.huichebang.cn/CarCenter/OCarInfoEdit?id=";

    private String CARINFO_URL = "http://crm.huichebang.cn/CarCenter/AllIndex?v=3.3&pageIndex=";

    private String SUPPLIER_URL = "http://crm.huichebang.cn/Stock/ShopSupplierList?DeptState=0&RoleText1=All&pageIndex=";

    private String totalRegEx = "body > div.dataTables_paginate.paging_full_numbers > div > span > a";

    private String companyName = "惠车邦智慧门店";


    /**
     * 服务项目
     * 打开路径:首页-管理-常用项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();

        String divRegEx = "#div_contents > div";
        Response response = ConnectionUtil.doGetWith(SERVICE_URL, COOKIE);
        String html = response.returnContent().asString();
        Document body = Jsoup.parseBodyFragment(html);

        Elements divs = body.select(divRegEx);
        if (divs.size() > 0) {
            for (Element div : divs) {

                String firstCategoryNameRegEx = "table > thead > tr > td > input[name='pclass']";
                String firstCategoryName = div.select(firstCategoryNameRegEx).attr("value");

                String trRegEx = "table > tbody > tr";
                Elements trs = div.select(trRegEx);

                if (trs.size() > 0) {
                    for (Element tr : trs) {
                        Elements tds = tr.getElementsByTag("td");

                        if (tds.size()>0){
                            String productName =  tds.get(0).select("input").attr("value");
                            String price =  tds.get(1).select("input").attr("value");

                            Product product = new Product();
                            product.setCompanyName(companyName);
                            product.setPrice(price);
                            product.setProductName(productName);
                            product.setFirstCategoryName(firstCategoryName);
                            product.setItemType("服务项");
                            products.add(product);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\惠车邦服务.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }


    /**
     * 库存及商品
     * 打开路径:首页-库存-库存管理
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        String totalRegEx = "#form_cr > div.dataTables_paginate.paging_full_numbers > div > span > a";
        Response response = ConnectionUtil.doGetWith(STOCK_URL + 1, COOKIE);
        int totalPage = getTotalPage(response, totalRegEx);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {

                Response res = ConnectionUtil.doGetWith(STOCK_URL + i, COOKIE);
                String html = res.returnContent().asString();
                Document body = Jsoup.parseBodyFragment(html);

                String trRegEx = "#form_cr > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(body, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {

                        String locationNameRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(2)";
                        String goodsNameRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(3) > pre";
                        String productCodeRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(3) > span";
                        String inventoryNumRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(7)";
                        String priceRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(10)";
                        String brandRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(5)";
                        String carModelRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(6)";
                        String salePriceRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(8)";


                        String locationName = body.select(locationNameRegEx).text();
                        String goodsName = body.select(goodsNameRegEx).text();
                        String productCode = body.select(productCodeRegEx).text();
                        String inventoryNum = body.select(inventoryNumRegEx).text();
                        String price = body.select(priceRegEx).text();
                        String brand = body.select(brandRegEx).text();
                        String carModel = body.select(carModelRegEx).text();
                        String salePrice = body.select(salePriceRegEx).text();

                        Stock stock = new Stock();
                        stock.setCompanyName(companyName);
                        stock.setGoodsName(goodsName);
                        stock.setInventoryNum(inventoryNum);
                        stock.setPrice(price);
                        stock.setLocationName(locationName);
                        stock.setProductCode(productCode);
                        stocks.add(stock);

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(goodsName);
                        product.setItemType("商品");
                        product.setPrice(salePrice);
                        product.setBrandName(brand);
                        product.setCarModel(carModel);
                        products.add(product);

                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\惠车邦库存.xls";
        String pathname2 = "C:\\exportExcel\\惠车邦商品.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname2);

    }


    /**
     * 历史消费记录和消费记录相关车辆
     * 打开路径:首页-报表-营业流水-操作-详情
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        String totalRegEx = "body > div > div[class='dataTables_paginate paging_full_numbers'] > div > span > a";

        Response response = ConnectionUtil.doGetWith(BILL_URL + 1, COOKIE);
        int totalPage = getTotalPage(response, totalRegEx);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {

                Response res = ConnectionUtil.doGetWith(BILL_URL + i, COOKIE);
                String html = res.returnContent().asString();
                Document body = Jsoup.parseBodyFragment(html);

                String trRegEx = "#table > tbody > tr";

                int trSize = WebClientUtil.getTagSize(body, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {

                        String tdRegEx = trRegEx + ":nth-child(" + j + ") > td";
                        int tdSize = WebClientUtil.getTagSize(body, tdRegEx, HtmlTag.tdName);
                        String detailRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(" + tdSize + ") > input:nth-child(2)";

                        String value = body.select(detailRegEx).attr("onclick");
                        String cridRegEx = "(?<=')(.*)(?=')";
                        String crid = CommonUtil.fetchString(value, cridRegEx);

                        //单据明细
                        Response resp = ConnectionUtil.doGetWith(BILLDETAIL_URL + crid, COOKIE);
                        String content = resp.returnContent().asString();
                        Document doc = Jsoup.parseBodyFragment(content);

                        String billNoRegEx = "body > div > table > tbody:nth-child(2) > tr > td > div:nth-child(1)";
                        String carNumberRegEx = "body > div > table > tbody:nth-child(2) > tr > td > div:nth-child(3)";
                        String totalAmountRegEx = "body > div > table > tbody:nth-child(2) > tr > td > div:nth-child(7) > span";
                        String receptionistNameRegEx = "body > div > table > tbody:nth-child(2) > tr > td > div:nth-child(8)";
                        String dateEndRegEx = "body > div > table > tbody:nth-child(2) > tr > td > div:nth-child(6)";


                        String billNo = doc.select(billNoRegEx).text().replace("单号：", "");
                        String carNumber = doc.select(carNumberRegEx).text().replace("车牌号码：", "");
                        String totalAmount = doc.select(totalAmountRegEx).text();
                        String receptionistName = doc.select(receptionistNameRegEx).text().replace("录单人：", "");
                        String dateEnd = doc.select(dateEndRegEx).text().replace("结算时间：", "");
                        dateEnd = DateUtil.formatSQLDate(dateEnd);


                        Bill bill = new Bill();
                        bill.setBillNo(billNo);
                        bill.setCompanyName(companyName);
                        bill.setCarNumber(carNumber);
                        bill.setTotalAmount(totalAmount);
                        bill.setReceptionistName(receptionistName);
                        bill.setDateEnd(dateEnd);


                        //商品及服务

                        String aaa = "";
                        bills.add(bill);

                    }
                }
            }
        }

    }


    /**
     * 车辆信息
     * 打开路径:首页-车主-所有车主
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(CARINFO_URL + 1, COOKIE);
        int totalPage = getTotalPage(response, totalRegEx);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWith(CARINFO_URL + i, COOKIE);

                String html = res.returnContent().asString();
                Document body = Jsoup.parseBodyFragment(html);

                String trRegEx = "#gridContent > tbody > tr";

                int trSize = WebClientUtil.getTagSize(body, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {

                        String nameRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(2) > div:nth-child(1)";
                        String phoneRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(2) > div:nth-child(2)";
                        String carNumberRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(9)";
                        String balanceRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(3) > span";

                        String name = body.select(nameRegEx).text();
                        String phone = body.select(phoneRegEx).text();
                        String carNumber = body.select(carNumberRegEx).text();
                        String balance = body.select(balanceRegEx).text();

                        String tdRegEx = trRegEx + ":nth-child(" + j + ") > td";
                        int tdSize = WebClientUtil.getTagSize(body, tdRegEx, HtmlTag.tdName);
                        String detailRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(" + tdSize + ") > input:nth-child(1)";
                        String value = body.select(detailRegEx).attr("onclick");

                        String idRegEx = "(?<=')(.*)(?=',)";
                        String cidRegEx = "(?<=',)(.*)(?=\\))";

                        String id = CommonUtil.fetchString(value, idRegEx);
                        String cid = CommonUtil.fetchString(value, cidRegEx);

                        //车辆信息明细
                        String url = CARINFODETAIL_URL + id + "&cid=" + cid;
                        Response resp = ConnectionUtil.doGetWith(url, COOKIE);
                        String content = resp.returnContent().asString();
                        Document doc = Jsoup.parseBodyFragment(content);

                        String vinRegEx = "#txtFrameNo";
                        String engineNumberRegEx = "#txtEngineNo";
                        String brandRegEx = "#brand";
                        String carModelRegEx = "#model";
                        String vcInsuranceValidDateRegEx = "#txtInTime";
                        String vcInsuranceCompanyRegEx = "#txtInCompany";

                        String vin = doc.select(vinRegEx).attr("value");
                        String engineNumber = doc.select(engineNumberRegEx).attr("value");
                        String brand = doc.select(brandRegEx).attr("value");
                        String carModel = doc.select(carModelRegEx).attr("value");
                        String vcInsuranceValidDate = doc.select(vcInsuranceValidDateRegEx).attr("value");
                        String vcInsuranceCompany = doc.select(vcInsuranceCompanyRegEx).attr("value");

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCompanyName(companyName);
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setBalance(balance);
                        carInfo.setVINcode(vin);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setBrand(brand);
                        carInfo.setCarModel(carModel);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setRemark(balance);//余额
                        carInfos.add(carInfo);

                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\惠车邦车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);

    }


    /**
     * 供应商
     * 打开路径:首页-库存-供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(SUPPLIER_URL + 1, COOKIE);
        int totalPage = getTotalPage(response, totalRegEx);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWith(SUPPLIER_URL + i, COOKIE);

                String html = res.returnContent().asString();
                Document body = Jsoup.parseBodyFragment(html);

                String trRegEx = "body > div.layui-form > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(body, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {

                        String nameRegEx = "body > div.layui-form > table > tbody > tr:nth-child(" + j + ") > td:nth-child(2)";
                        String contactNameRegEx = "body > div.layui-form > table > tbody > tr:nth-child(" + j + ") > td:nth-child(3)";
                        String contactPhoneRegEx = "body > div.layui-form > table > tbody > tr:nth-child(" + j + ") > td:nth-child(4)";
                        String addressRegEx = "body > div.layui-form > table > tbody > tr:nth-child(" + j + ") > td:nth-child(5)";

                        String name = body.select(nameRegEx).text();
                        String contactName = body.select(contactNameRegEx).text();
                        String contactPhone = body.select(contactPhoneRegEx).text();
                        String address = body.select(addressRegEx).text();

                        Supplier supplier = new Supplier();
                        supplier.setCompanyName(companyName);
                        supplier.setName(name);
                        supplier.setContactName(contactName);
                        supplier.setContactPhone(contactPhone);
                        supplier.setAddress(address);
                        suppliers.add(supplier);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\惠车邦供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }

    private int getTotalPage(Response response, String regEx) throws IOException {
        int totalPage = 0;

        String html = response.returnContent().asString();
        Document body = Jsoup.parseBodyFragment(html);

        Elements elements = body.select(regEx);

        if (elements.size() > 0) {
            String totalStr = elements.get(elements.size() - 1).text();
            totalPage = Integer.parseInt(totalStr);
        }

        return totalPage;
    }

}
