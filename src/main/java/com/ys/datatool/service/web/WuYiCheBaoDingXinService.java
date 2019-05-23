package com.ys.datatool.service.web;

import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.HtmlTag;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.domain.entity.CarInfo;
import com.ys.datatool.domain.entity.Product;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.DateUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/11/27.
 * <p>
 * 51车宝系统-鼎鑫
 */
@Service
public class WuYiCheBaoDingXinService {

    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private String COOKIE = "hidden=value; hidden=value; store-dingxinqixiu=%5B%7B%22account%22%3A%2215978811110%22%2C%22pwd%22%3A%2215978811110%22%2C%22cur%22%3Atrue%7D%5D; PHPSESSID=p391qnoahq04g3vk3r5h5533a1; language=cn";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String BILLDETAIL_URL = "http://saas.51chebao.com/store/dingxinqixiu/order/view?order_number=";

    private String BILL_URL = "http://saas.51chebao.com/store/dingxinqixiu/order?store_id=870&page=";

    private String ACCOUNTING_URL = "http://saas.51chebao.com/store/dingxinqixiu/accounting/index?t=&wk_sys_type=store&wk_sys_directory=dingxinqixiu&t=&page=";

    private String SERVICE_URL = "http://saas.51chebao.com/store/dingxinqixiu/service?page=";

    private String companyName = "鼎鑫名车";


    /**
     * 历史消费记录和消费记录相关车辆
     * 营销管理-订单管理
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        int totalPage = WebClientUtil.getHtmlTotalPage(BILL_URL, COOKIE);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWith(BILL_URL + String.valueOf(i), COOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String trRegEx = "#tbl_list > tbody > tr";
                int trSize = WebClientUtil.getTagSize(document, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int j = 3; j <= trSize; j++) {
                        String billNoRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(3) > a";
                        String billNo = document.select(billNoRegEx).text();

                        String dateEndRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(9) ";
                        String dateEnd = document.select(dateEndRegEx).text();
                        dateEnd = DateUtil.formatSQLDate(dateEnd);

                        Response resp = ConnectionUtil.doGetWith(BILLDETAIL_URL + billNo, COOKIE);
                        String content = resp.returnContent().asString();
                        Document doc = Jsoup.parseBodyFragment(content);


                        String carNumberRegEx = "#content > div > div.content > table > tbody > tr:nth-child(1) > td:nth-child(4)";
                        String mileageRegEx = "#content > div > div.content > table > tbody > tr:nth-child(1) > td:nth-child(6)";
                        String totalAmountRegEx = "#content > div > div.content > table > tbody > tr:nth-child(3) > td:nth-child(4)";
                        String receptionistNameRegEx = "#content > div > div.content > table > tbody > tr:nth-child(3) > td:nth-child(6)";
                        String billTypeRegEx = "#content > div > div.content > table > tbody > tr:nth-child(3) > td:nth-child(2)";
                        String remarkRegEx = "#content > div > div.content > table > tbody > tr:nth-child(5) > td:nth-child(4)";
                        String nameRegEx = "#content > div > div.content > table > tbody > tr:nth-child(2) > td:nth-child(2)";
                        String phoneRegEx = "#content > div > div.content > table > tbody > tr:nth-child(2) > td:nth-child(4)";
                        String vinRegEx = "#content > div > div.content > table > tbody > tr:nth-child(5) > td:nth-child(2)";
                        String brandRegEx = "#content > div > div.content > table > tbody > tr:nth-child(1) > td:nth-child(2)";

                        String carNumber = doc.select(carNumberRegEx).text();
                        String mileage = doc.select(mileageRegEx).text();
                        String totalAmount = doc.select(totalAmountRegEx).text();
                        String receptionistName = doc.select(receptionistNameRegEx).text();
                        String billType = doc.select(billTypeRegEx).text();
                        String remark = doc.select(remarkRegEx).text();
                        String name = doc.select(nameRegEx).text();
                        String phone = doc.select(phoneRegEx).text();
                        String vin = doc.select(vinRegEx).text();
                        String brand = doc.select(brandRegEx).text();

                        String itemTrRegEx = "#content > div > div.content > div:nth-child(3) > table > tbody > tr";
                        int itemTrSize = WebClientUtil.getTagSize(doc, itemTrRegEx, HtmlTag.trName);

                        String serviceNames = "";
                        String itemNames = "";
                        if (itemTrSize > 0) {
                            for (int z = 1; z < trSize; z++) {
                                String serviceItemNamesRegEx = trRegEx + ":nth-child(" + z + ") " + "> td:nth-child(1)";
                                String goodsNamesRegEx = trRegEx + ":nth-child(" + z + ") " + "> td:nth-child(2)";
                                String numRegEx = trRegEx + ":nth-child(" + z + ") " + "> td:nth-child(3)";
                                String priceRegEx = trRegEx + ":nth-child(" + z + ") " + "> td:nth-child(5)";

                                String serviceItemNames = doc.select(serviceItemNamesRegEx).text();
                                String goodsNames = doc.select(goodsNamesRegEx).text();
                                String num = doc.select(numRegEx).text();
                                String price = doc.select(priceRegEx).text();
                                price = price.replace("￥", "");

                                serviceItemNames = serviceItemNames + "*" + num + "(" + price + ")";
                                if (!"".equals(serviceNames))
                                    serviceNames = serviceNames + "," + serviceItemNames;

                                if ("".equals(serviceNames))
                                    serviceNames = serviceItemNames;

                                if ("".equals(goodsNames))
                                    continue;

                                if (!"".equals(itemNames))
                                    itemNames = itemNames + "," + goodsNames;

                                if ("".equals(itemNames))
                                    itemNames = goodsNames;

                            }
                        }

                        Bill bill = new Bill();
                        bill.setBillNo(billNo);
                        bill.setCarNumber(carNumber);
                        bill.setMileage(mileage);
                        bill.setDateEnd(dateEnd);
                        bill.setCompanyName(companyName);
                        bill.setTotalAmount(totalAmount.replace("￥", ""));
                        bill.setReceptionistName(receptionistName);
                        bill.setRemark(billType + " " + remark);
                        bill.setGoodsNames(itemNames);
                        bill.setServiceItemNames(serviceNames);
                        bills.add(bill);

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCompanyName(companyName);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setVINcode(vin);
                        carInfo.setBrand(brand);
                        carInfos.add(carInfo);

                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\鼎鑫名车消费记录.xls";
        String pathname2 = "C:\\exportExcel\\鼎鑫名车消费记录-车辆.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname2);
    }


    /**
     * 服务项目
     * 系统设置-服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();

        int totalPage = WebClientUtil.getHtmlTotalPage(SERVICE_URL, COOKIE);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWith(SERVICE_URL + String.valueOf(i), COOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String trRegEx = "#form > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(document, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int j = 2; j <= trSize; j++) {
                        String productNameRegEx = trRegEx + ":nth-child(" + j + ") " + "> td.left.serviceTR";
                        String firstCategoryNameRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(3)";
                        String priceRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(5) > input";

                        String productName = document.select(productNameRegEx).text();
                        String firstCategoryName = document.select(firstCategoryNameRegEx).text();
                        String price = document.select(priceRegEx).attr("value");

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(productName);
                        product.setFirstCategoryName(firstCategoryName);
                        product.setPrice(price);
                        product.setItemType("服务项");
                        products.add(product);
                    }
                }
            }
        }

        System.out.println("结果为" + totalPage);

        String pathname = "C:\\exportExcel\\鼎鑫名车服务项目.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 财务记账数据
     * 财务管理-财务记账
     *
     * @throws IOException
     */
    @Test
    public void fetchAccountData() throws IOException {
        List<Bill> bills = new ArrayList<>();

        int totalPage = WebClientUtil.getHtmlTotalPage(ACCOUNTING_URL, COOKIE);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWith(ACCOUNTING_URL + String.valueOf(i), COOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String trRegEx = "#table_list > tbody:nth-child(3) > tr";
                int trSize = WebClientUtil.getTagSize(document, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int j = 2; j <= trSize; j++) {

                        String billNoRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(2)";
                        String stockOutNumberRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(3)";
                        String payTypeRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(4)";
                        String accountTypeRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(5)";
                        String totalAmountRegEx = trRegEx + ":nth-child(" + j + ") " + "> td.right";
                        String dateAddedRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(7)";
                        String receptionistNameRegEx = trRegEx + ":nth-child(" + j + ") " + "> td:nth-child(8)";
                        String remarkRegEx = trRegEx + ":nth-child(" + j + ") " + "> td.left";

                        String billNo = document.select(billNoRegEx).text();
                        String stockOutNumber = document.select(stockOutNumberRegEx).text();
                        String payType = document.select(payTypeRegEx).text();
                        String accountType = document.select(accountTypeRegEx).text();
                        String totalAmount = document.select(totalAmountRegEx).text();
                        String dateAdded = document.select(dateAddedRegEx).text();
                        String receptionistName = document.select(receptionistNameRegEx).text();
                        String remark = document.select(remarkRegEx).text();

                        Bill bill = new Bill();
                        bill.setCompanyName(companyName);
                        bill.setBillNo(billNo);
                        bill.setStockOutNumber(stockOutNumber);
                        bill.setPayType(payType);
                        bill.setAccountType(accountType);
                        bill.setTotalAmount(totalAmount);
                        bill.setDateAdded(dateAdded);
                        bill.setReceptionistName(receptionistName);
                        bill.setRemark(remark);
                        bills.add(bill);
                    }
                }
            }
        }

        System.out.println("结果为" + bills.toString());

        String pathname = "C:\\exportExcel\\鼎鑫名车记账数据.xls";
        ExportUtil.exportWuYiCheBaoAccountDataInLocal(bills, ExcelDatas.workbook, pathname);
    }
}
