package com.ys.datatool.service.batch;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.DateUtil;
import org.apache.http.client.fluent.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by mo on 2019/5/15
 */
public class BatchDianDianYangCheConsumptionRecordService implements Runnable {


    private static final Logger log = Logger.getLogger(BatchDianDianYangCheConsumptionRecordService.class);

    private List<Bill> bills;

    private List<String> list;

    private CountDownLatch countDownLatch;

    private String BILL_URL;

    private String BILLDETAIL_URL;

    private String companyName;

    private String COOKIE;

    public BatchDianDianYangCheConsumptionRecordService(List<Bill> bills, List<String> list,
                                                        String cookie, String url,
                                                        String detailUrl, String companyName,
                                                        CountDownLatch countDownLatch) {

        this.bills = bills;
        this.list = list;
        this.COOKIE = cookie;
        this.BILL_URL = url;
        this.BILLDETAIL_URL = detailUrl;
        this.companyName = companyName;
        this.countDownLatch = countDownLatch;

    }


    public void fetchConsumptionRecordDatas(String index) throws IOException {
        Response res = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(Integer.parseInt(index)), COOKIE);
        JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

        JsonNode dataNode = result.get("data").get("data");

        if (dataNode.size() > 0) {
            Iterator<JsonNode> it = dataNode.elements();
            while (it.hasNext()) {
                JsonNode element = it.next();

                String billNo = element.get("workId").asText();
                String carNumber = element.get("carNumber").asText();
                String dateEnd = element.get("businessStarDate").asText();
                dateEnd = DateUtil.formatSQLDate(dateEnd);

                String id = element.get("orderId").asText();
                String totalAmount = element.get("receivableAccount").asText();//应收
                String actualAmount = element.get("shopGetMoney").asText();//实收

                Bill bill = new Bill();
                bill.setCompanyName(companyName);
                bill.setBillNo(billNo);
                bill.setCarNumber(carNumber);
                bill.setDateEnd(dateEnd);
                bill.setTotalAmount(totalAmount);


                System.out.println("车牌号为" + carNumber);
                System.out.println("网址为" + BILLDETAIL_URL + id);

                if (!"null".equals(id)) {
                    Response res2 = ConnectionUtil.doGetWith(BILLDETAIL_URL + id, COOKIE);
                    JsonNode content = JsonObject.MAPPER.readTree(res2.returnContent().asString());

                    JsonNode serviceNode = content.get("data").get("carServicePackageList");

                    if (serviceNode != null && serviceNode.size() > 0) {
                        Iterator<JsonNode> iterator = serviceNode.elements();

                        while (iterator.hasNext()) {
                            JsonNode e = iterator.next();

                            String num = e.get("amount").asText();
                            String serviceItemNames = e.get("packageName").asText();
                            String price = e.get("unitPrice").asText();

                            if (null != bill.getServiceItemNames() && !"".equals(serviceItemNames)) {
                                serviceItemNames = serviceItemNames + "*" + price;
                                String s = bill.getServiceItemNames() + "," + serviceItemNames;
                                bill.setServiceItemNames(s);
                            }

                            if (null == bill.getServiceItemNames()) {
                                bill.setServiceItemNames(serviceItemNames + "*" + price);
                            }

                            JsonNode workers = e.get("workerList");
                            if (workers.size() > 0) {
                                String receptionistName = workers.get(0).get("workerName").asText();
                                bill.setReceptionistName(receptionistName);
                            }
                        }
                    }
                }

                bills.add(bill);
            }
        }

    }

    private String getBillParam(int pageNo) {
        String param = "{" +
                "\"pageNumber\":" +
                pageNo + "," +
                "\"pageSize\":10," +
                "\"queryParam\":" +
                "{\"carNumber\":\"\"," +
                "\"userPhone\":\"\"," +
                "\"workEndDate\":null," +
                "\"workStarDate\":null," +
                "\"workStatus\":0," +
                "\"workCode\":null," +
                "\"dateRange\":\"\"," +
                "\"workOrderId\":\"\"," +
                "\"salesmanId\":\"\"," +
                "\"workOrderType\":\"\"," +
                "\"orderCategory\":\"0\"," +
                "\"workStatusId\":\"\"}" +
                "}";

        return param;
    }

    @Override
    public void run() {

        if (null != list) {
            list.forEach(b -> {
                try {

                    System.out.println("--------------------------------正在处理第" + b + "页的数据-------------------------------------------------------");
                    fetchConsumptionRecordDatas(b);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }

        countDownLatch.countDown();
    }
}
