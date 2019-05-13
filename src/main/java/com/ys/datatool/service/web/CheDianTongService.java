package com.ys.datatool.service.web;

import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.domain.entity.BillDetail;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.HtmlTag;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 2019/3/26
 * 车店通系统
 */
@Service
public class CheDianTongService {


    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private String COOKIE = "__RequestVerificationToken=tpqg8Or-W_2jqrTtDQWmEi7SyRD5Q2tVWFkbWlj8_BjFInqk210Z8vL3HuHFOdLx96jyZv1WCzFpipHn9Dkl5eFQQevnbq7KmnGM4SA1RZg1; Hm_lvt_b13246fd95aec7bfc57d9c415dfefe18=1553504899; LoginInfo=4dd8661c6fda4c3da0005c3f7bb3cb10; SERVERID=9ca4382df214465ec42a2ada7c3d713d|1553568654|1553504898; Hm_lpvt_b13246fd95aec7bfc57d9c415dfefe18=1553568653";



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private String BIll_URL = "https://auto.51autoshop.com/Order/OrderList";

    private String BILLDETAIL_URL = "https://auto.51autoshop.com/Order/RepairOrderRead?orderId=";

    private String companyName = "车店通";

    private Charset charset = Charset.forName("utf-8");

    private String trRegEx = "#ListTB > tbody > tr";



    /**
     * 历史消费记录和消费记录
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(BIll_URL, getBillParams("1"), COOKIE);
        String totalRegEx = "#xxf > div > div.col-sm-3.text-left > span:nth-child(2)";
        int total = getTotalPage(response, totalRegEx);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(BIll_URL, getBillParams(String.valueOf(i)), COOKIE);
                String html = res.returnContent().asString();
                Document body = Jsoup.parseBodyFragment(html);

                int trSize = WebClientUtil.getTagSize(body, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {

                        String trTagRegEx = "#ListTB > tbody > tr:nth-child(" + j + ")";
                        Element tr = body.select(trTagRegEx).get(0);

                        String billId = tr.attr("data-id");

                        String companyNameRegEx = "td[data-content=\"ShopName\"]";
                        String companyName = tr.select(companyNameRegEx).text();

                        String carNumberRegEx = "td[data-content=\"CarCode\"]";
                        String carNumber = tr.select(carNumberRegEx).text();

                        String mileageRegEx = "td[data-content=\"Mileage\"]";
                        String mileage = tr.select(mileageRegEx).text();

                        String nameRegEx = "td[data-content=\"CustomerName\"]";
                        String name = tr.select(nameRegEx).text();

                        String totalAmountRegEx = "td[data-content=\"PayAmount\"]";
                        String totalAmount = tr.select(totalAmountRegEx).text();

                        String actualAmountRegEx = "td[data-content=\"Amount\"]";
                        String actualAmount = tr.select(actualAmountRegEx).text();

                        String discountRegEx = "td[data-content=\"Fraction\"]";
                        String discount = tr.select(discountRegEx).text();

                        String payTypeRegEx = "td[data-content=\"PayStatus\"]";
                        String payType = tr.select(payTypeRegEx).text();

                        String receptionistNameRegEx = "td[data-content=\"CreateName\"]";
                        String receptionistName = tr.select(receptionistNameRegEx).text();

                        String remarkRegEx = "td[data-content=\"OrderRemark\"]";
                        String remark = tr.select(remarkRegEx).text();

                        String dateAddedRegEx = "td[data-content=\"CreateTime\"]";
                        String dateAdded = tr.select(dateAddedRegEx).text();

                        String billNoRegEx = "td[data-content=\"OrderCode\"]";
                        String billNo = tr.select(billNoRegEx).text();

                        Bill bill = new Bill();
                        bill.setBillId(billId);
                        bill.setCompanyName(companyName);
                        bill.setCarNumber(carNumber);
                        bill.setMileage(mileage);
                        bill.setName(name);
                        bill.setTotalAmount(totalAmount);
                        bill.setActualAmount(actualAmount);
                        bill.setDiscount(discount);
                        bill.setPayType(payType);
                        bill.setReceptionistName(receptionistName);
                        bill.setRemark(remark);
                        bill.setDateAdded(dateAdded);
                        bill.setDateEnd(dateAdded);
                        bill.setDateExpect(dateAdded);
                        bill.setBillNo(billNo);
                        bills.add(bill);
                    }
                }
            }
        }

        if (bills.size() > 0) {
            for (Bill bill : bills) {
                String billId = bill.getBillId();

                Response res = ConnectionUtil.doGetWith(BILLDETAIL_URL + billId, COOKIE);
                String html = res.returnContent().asString();
                Document body = Jsoup.parseBodyFragment(html);

                int trServiceSize = WebClientUtil.getTagSize(body, trRegEx, HtmlTag.trName);
                if (trServiceSize > 0) {
                    for (int i = 1; i <= trServiceSize; i++) {

                        String trTagRegEx = "#ListTB > tbody > tr:nth-child(" + i + ")";
                        Element tr = body.select(trTagRegEx).get(0);

                        String itemNameRegEx = "td:nth-child(2)";
                        String itemName = tr.select(itemNameRegEx).text();

                        String numRegEx = "td:nth-child(5)";
                        String num = tr.select(numRegEx).text();

                        String priceRegEx = "td:nth-child(7)";
                        String price = tr.select(priceRegEx).text();

                        BillDetail billDetail = new BillDetail();
                        billDetail.setCompanyName(bill.getCompanyName());
                        billDetail.setBillNo(bill.getBillNo());
                        billDetail.setItemName(itemName);
                        billDetail.setNum(num);
                        billDetail.setItemType("服务项");
                        billDetail.setPrice(price);
                        billDetails.add(billDetail);
                    }
                }


                String trItemRegEx = "body > div.wrapper.wrapper-content > div:nth-child(5) > div.list_box_content > table:nth-child(2) > tbody > tr";
                int trItemSize = WebClientUtil.getTagSize(body, trItemRegEx, HtmlTag.trName);
                if (trItemSize > 0) {
                    for (int i = 1; i <= trItemSize; i++) {

                        String trTagRegEx = "body > div.wrapper.wrapper-content > div:nth-child(5) > div.list_box_content > table:nth-child(2) > tbody > tr:nth-child(" + i + ")";
                        Element tr = body.select(trTagRegEx).get(0);

                        String itemNameRegEx = "td:nth-child(2)";
                        String itemName = tr.select(itemNameRegEx).text();

                        String numRegEx = "td:nth-child(5)";
                        String num = tr.select(numRegEx).text();

                        String priceRegEx = "td:nth-child(7)";
                        String price = tr.select(priceRegEx).text();

                        BillDetail billDetail = new BillDetail();
                        billDetail.setCompanyName(bill.getCompanyName());
                        billDetail.setBillNo(bill.getBillNo());
                        billDetail.setItemName(itemName);
                        billDetail.setNum(num);
                        billDetail.setItemType("配件");
                        billDetail.setPrice(price);
                        billDetails.add(billDetail);

                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\车店通单据.xls";
        String pathname2 = "C:\\exportExcel\\车店通单据明细.xls";
        ExportUtil.exportBillSomeFieldDataInLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportBillDetailSomeFieldDataInLocal(billDetails, ExcelDatas.workbook, pathname2);
    }

    private int getTotalPage(Response response, String regEx) throws IOException {
        String html = response.returnContent().asString();
        Document document = Jsoup.parse(html);

        String result = document.select(regEx).text();
        String totalStr = result.replace("1 / ", " ").trim();

        return Integer.parseInt(totalStr);

    }


    private List<BasicNameValuePair> getBillParams(String index) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("DetialUrl", "/Order/OrderDetial"));
        params.add(new BasicNameValuePair("PageIndex", index));
        params.add(new BasicNameValuePair("PageSize", "10"));
        params.add(new BasicNameValuePair("CustType", "2"));//用户类型
        params.add(new BasicNameValuePair("ConsumerId", ""));//客户id
        params.add(new BasicNameValuePair("PrimaryType", "0"));
        params.add(new BasicNameValuePair("LockProdItemId", ""));
        params.add(new BasicNameValuePair("StartTime", ""));
        params.add(new BasicNameValuePair("EndTime", ""));
        params.add(new BasicNameValuePair("Status", ""));
        params.add(new BasicNameValuePair("PayStatus", ""));
        params.add(new BasicNameValuePair("InvoiceStatus", "0"));//开票状态
        params.add(new BasicNameValuePair("WCompanyID", ""));
        params.add(new BasicNameValuePair("Keywords", ""));
        params.add(new BasicNameValuePair("Model", ""));
        params.add(new BasicNameValuePair("ItemKeywords", ""));
        params.add(new BasicNameValuePair("Type", ""));//开单类型
        params.add(new BasicNameValuePair("OrderLabel", ""));
        params.add(new BasicNameValuePair("CreateName", ""));
        params.add(new BasicNameValuePair("WorkOrderApproach", ""));
        params.add(new BasicNameValuePair("SortField", ""));
        params.add(new BasicNameValuePair("SortDir", ""));

        return params;
    }


}
