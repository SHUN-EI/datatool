package com.ys.datatool.service.web;

import com.ys.datatool.domain.Bill;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.HtmlTag;
import com.ys.datatool.util.ConnectionUtil;
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

    private String ACCOUNTING_URL = "http://saas.51chebao.com/store/dingxinqixiu/accounting/index?t=&wk_sys_type=store&wk_sys_directory=dingxinqixiu&t=&page=";

    private String companyName = "鼎鑫名车";

    private String COOKIE = "hidden=value; hidden=value; store-dingxinqixiu=%5B%5D; PHPSESSID=ffc1933qqd626j55vchfaf1sl2; language=cn";

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
                Response response = ConnectionUtil.doGetWithLeastParams(ACCOUNTING_URL + String.valueOf(i), COOKIE);
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
