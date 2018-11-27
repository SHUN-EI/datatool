package com.ys.datatool.service.web;

import com.ys.datatool.domain.*;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/6/30.
 * 车奇士系统
 */
@Service
public class WuYiCheBaoCheQiShiService {

    private static final String MEMBERCARDITEM_URL = "http://cheqishi.51chebao.com/card/edit?card_number=";

    private static final String MEMBERCARD_URL = "http://cheqishi.51chebao.com/card?store_id=599&page=";

    private static final String CARINFO_URL = "http://cheqishi.51chebao.com/customer?page=";

    private static final String SUPPLIER_URL = "http://cheqishi.51chebao.com/dealer?page=";

    private static final String BILL_URL = "http://cheqishi.51chebao.com/order?store_id=599&page=";

    private static final String BILLDETAIL_URL = "http://cheqishi.51chebao.com/order/view?order_number=";

    private String fileName = "车保无忧-河南车奇士";

    private static final String COOKIE = "language=cn; PHPSESSID=p2igo423087kp4uvmu37uqmrs6; store-cheqishi=%5B%7B%22account%22%3A%2218530186018%22%2C%22pwd%22%3A%221234%22%2C%22cur%22%3Atrue%7D%5D";


    /**
     * 卡内项目
     *
     * @throws IOException
     */
    public void fetchMemberCardItemData() throws IOException {
        List<String> memberCardNos = new ArrayList<>();
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        int totalPage = WebClientUtil.getHtmlTotalPage(MEMBERCARD_URL, COOKIE);
        Document document = null;

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(MEMBERCARD_URL + String.valueOf(i), COOKIE);
                String html = response.returnContent().asString();
                document = Jsoup.parse(html);

                String regEx = "#tbl_list > tbody > tr:nth-child({no}) > td:nth-child(3)";
                for (int j = 2; j <= 51; j++) {

                    String memberCardNo = document.select(StringUtils.replace(regEx, "{no}", j + "")).text();
                    memberCardNos.add(memberCardNo);
                }
            }
        }

        for (int i = 0; i < memberCardNos.size(); i++) {
            Response response = ConnectionUtil.doGetWithLeastParams(MEMBERCARDITEM_URL + memberCardNos.get(i), COOKIE);
            String html = response.returnContent().asString();
            document = Jsoup.parse(html);

            String trRegEx = "#service_times > tbody > tr";
            int trSize = document.select(trRegEx).tagName("tr").size();

            if (trSize > 0) {
                for (int j = 1; j <= trSize; j++) {

                    String regEx = "#tab-edit > table.form > tbody > tr:nth-child(2) > td:nth-child(2)";

                    String memberCarItemId = document.select(regEx).text();

                    MemberCardItem memberCardItem = new MemberCardItem();
                    memberCardItem.setMemberCardItemId(memberCarItemId + "_" + memberCardNos.get(i));
                    memberCardItem.setCardCode(memberCardNos.get(i));
                    memberCardItem.setItemName(document.select(trRegEx + "> td:nth-child(1)").text());
                    memberCardItem.setNum(document.select(trRegEx + "> td:nth-child(2)").text());
                    memberCardItem.setOriginalNum(document.select(trRegEx + "> td:nth-child(2)").text());

                    memberCardItems.add(memberCardItem);
                }
            }
        }
    }

    /**
     * 车辆信息
     *
     * @throws IOException
     */
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        int totalPage = WebClientUtil.getHtmlTotalPage(CARINFO_URL, COOKIE);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + i + "", COOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String regEx = "#tbl_list > tbody > tr:nth-child({no})";
                for (int j = 2; j <= 51; j++) {
                    String tr = StringUtils.replace(regEx, "{no}", j + "");

                    CarInfo carInfo = new CarInfo();
                    carInfo.setName(document.select(tr + "> td:nth-child(2)").text());
                    carInfo.setPhone(document.select(tr + "> td:nth-child(3)").text());
                    carInfo.setCarNumber(document.select(tr + "> td:nth-child(4)> div > span").text());

                    carInfos.add(carInfo);
                }
            }
        }
    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        int totalPage = WebClientUtil.getHtmlTotalPage(SUPPLIER_URL, COOKIE);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(SUPPLIER_URL + i + "", COOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String regEx = "#form > table > tbody > tr:nth-child({no})";
                for (int j = 2; j <= 21; j++) {

                    String tr = StringUtils.replace(regEx, "{no}", j + "");
                    String td = document.select(tr + "> td:nth-child(3)").text();
                    String getPhoneRegEx = "(?<=\\().*(?=\\))";

                    Supplier supplier = new Supplier();
                    supplier.setName(document.select(tr + "> td.dealer_name").text());
                    supplier.setAddress(document.select(tr + "> td:nth-child(4)").text());
                    supplier.setContactName(td);
                    supplier.setContactPhone(CommonUtil.fetchString(td, getPhoneRegEx));

                    suppliers.add(supplier);
                }
            }
        }
    }

    /**
     * 单据明细
     *
     * @throws IOException
     */
    public void fetchBillDetailData() throws IOException {
        List<String> billNos = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();

        int totalPage = WebClientUtil.getHtmlTotalPage(BILL_URL, COOKIE);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(BILL_URL + i + "", COOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String regEx = "#tbl_list > tbody > tr:nth-child({no}) > td:nth-child(3)";
                for (int j = 2; j <= 51; j++) {

                    String billNo = document.select(StringUtils.replace(regEx, "{no}", j + "")).text();
                    billNos.add(billNo);
                }
            }
        }

        for (int i = 0; i < billNos.size(); i++) {
            Response response = ConnectionUtil.doGetWithLeastParams(BILLDETAIL_URL + billNos.get(i), COOKIE);
            String html = response.returnContent().asString();
            Document document = Jsoup.parse(html);

            String trRegEx = "#content > div > div.content > div:nth-child(3) > table > tbody > tr";
            int trSize = WebClientUtil.getTagSize(document,trRegEx,HtmlTag.trName);

            if (trSize > 0) {
                for (int j = 1; j <= trSize; j++) {

                    String itemNameRegEx = "#content > div > div.content > div:nth-child(3) > table > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String itemName = document.select(StringUtils.replace(itemNameRegEx, "{no}", j + "")).text();
                    if (itemName.contains("备注信息") || itemName.contains("合计"))
                        continue;

                    String detailIdRegEx = "#content > div > div.content > table > tbody > tr:nth-child(1) > td:nth-child(4)";
                    String detailId = document.select(detailIdRegEx).text();

                    String mileageRegEx = "#content > div > div.content > table > tbody > tr:nth-child(1) > td:nth-child(6)";
                    String mileage = document.select(mileageRegEx).text().replace("公里", "");

                    String dateAddedRegEx = "#content > div > div.content > table > tbody > tr:nth-child(2) > td:nth-child(6)";
                    String dateAdded = document.select(dateAddedRegEx).text();

                    String dateExpectRegEx = "#content > div > div.content > table > tbody > tr:nth-child(4) > td:nth-child(2)";
                    String dateExpect = document.select(dateExpectRegEx).text();

                    String dateEndRegEx = "#content > div > div.content > table > tbody > tr:nth-child(4) > td:nth-child(4)";
                    String dateEnd = document.select(dateEndRegEx).text();

                    String paymentRegEx = "#settlement > tbody > tr:nth-child(1) > td";
                    String payment = document.select(paymentRegEx).text();
                    if (payment.contains("现金") || payment.contains("其他"))
                        payment = "现金";

                    String productRegEx = "#content > div > div.content > div:nth-child(3) > table > tbody > tr:nth-child({no}) > td:nth-child(2)";
                    String product = document.select(StringUtils.replace(productRegEx, "{no}", j + "")).text();

                    String quantityRegEx = "#content > div > div.content > div:nth-child(3) > table > tbody > tr:nth-child({no}) > td:nth-child(3)";
                    String quantity = document.select(StringUtils.replace(quantityRegEx, "{no}", j + "")).text();

                    String priceRegEx = "#content > div > div.content > div:nth-child(3) > table > tbody > tr:nth-child({no}) > td:nth-child(4)";
                    String price = document.select(StringUtils.replace(priceRegEx, "{no}", j + "")).text().replace("￥", "");

                    String amountRegEx = "#content > div > div.content > div:nth-child(3) > table > tbody > tr:nth-child({no}) > td:nth-child(5)";
                    String amount = document.select(StringUtils.replace(amountRegEx, "{no}", j + "")).text().replace("￥", "");

                    String salePriceRegEx = "#content > div > div.content > div:nth-child(3) > table > tbody > tr:nth-child({no}) > td:nth-child(6)";
                    String salePrice = document.select(StringUtils.replace(salePriceRegEx, "{no}", j + "")).text().replace("￥", "");

                    BillDetail billDetail = new BillDetail();
                    billDetail.setDetailId(detailId + "_" + billNos.get(i));
                    billDetail.setBillNo(billNos.get(i));
                    billDetail.setItemName(itemName + "(" + product + ")");
                    billDetail.setNum(quantity);
                    billDetail.setPrice(price);
                    billDetail.setTotalAmount(amount);
                    billDetail.setSalePrice(salePrice);
                    billDetail.setMileage(mileage);
                    billDetail.setDateAdded(dateAdded);
                    billDetail.setDateExpect(dateExpect);
                    billDetail.setDateEnd(dateEnd);
                    billDetail.setPayment(payment);

                    billDetails.add(billDetail);
                }
            }
        }
    }

    /**
     * 单据
     *
     * @throws IOException
     */
    public void fetchBillData() throws IOException {
        List<Bill> bills = new ArrayList<>();

        int totalPage = WebClientUtil.getHtmlTotalPage(BILL_URL, COOKIE);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response response = ConnectionUtil.doGetWithLeastParams(BILL_URL + String.valueOf(i), COOKIE);
                String html = response.returnContent().asString();
                Document document = Jsoup.parse(html);

                String regEx = "#tbl_list > tbody > tr:nth-child({no})";
                for (int j = 2; j <= 51; j++) {

                    String tr = StringUtils.replace(regEx, "{no}", j + "");
                    // List<String> content = Arrays.asList(StringUtils.split(tr, " "));

                    Bill bill = new Bill();
                    bill.setBillNo(document.select(tr + " > td:nth-child(3) ").text());
                    bill.setActualAmount(document.select(tr + ">td.right ").text().replace("￥", ""));
                    bill.setName(document.select(tr + "> td:nth-child(5) ").text());
                    bill.setPhone(document.select(tr + " >td:nth-child(6) ").text());
                    bill.setCarNumber(document.select(tr + " > td:nth-child(7) ").text());
                    bill.setState(document.select(tr + ">td:nth-child(8) ").text());
                    bill.setDateExpect(document.select(tr + " > td:nth-child(9) ").text());
                    bill.setDateEnd(document.select(tr + " > td:nth-child(9) ").text());
                    bill.setAutomodel(document.select(tr + "> td:nth-child(11) ").text());

                    bills.add(bill);
                }
            }
        }
    }

}
