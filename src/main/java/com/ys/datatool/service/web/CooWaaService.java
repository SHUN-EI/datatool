package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.DateUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/10/15.
 * 酷蛙快修
 */
@Service
public class CooWaaService {

    private String BILLITEM_URL = "https://shops.coowaa.cn/Modules/SalesOrder/SalesOrderMethods.aspx/SearchWorkOrderPartsList";

    private String BILLSERVICE_URL = "https://shops.coowaa.cn/Modules/SalesOrder/SalesOrderMethods.aspx/SearchWorkOrderRepairList";

    private String BILL_URL = "https://shops.coowaa.cn/Modules/SalesOrder/SalesOrderMethods.aspx/SearchWorkOrderList";

    private String companyName = "酷蛙快修";

    private String COOKIE = "rememberServicePad=userid=18218754669; ASP.NET_SessionId=y5gujmd3hpokmqbrvf4ciwjs; userid=10423; .democoowaashops=56EF0B422C3BC61902E4BC6E574486E17757B6C0BB5AB9CDA2E069838B8CB5921DF274BD5346040775FD30547DEBC5BE5667351CABD9D641BFF72C8F55E93DC584FA8C23BD81ADF0EEE7B11BA9232BC9C5D5C6A44AEDB48FB7A514FBCE918A1E4B28B538E85D4E1CF51A22305C605661BAB0A03A21622B08AF1DD0BCE70621968E32D73F230E1F774B00569909F483D1";


    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(0), COOKIE);
        int totalPage = getTotalPage(response, 50);

        if (totalPage > 0) {
            for (int i = 0; i < totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(i), COOKIE);

                JsonNode result = formatDataToJson(res);
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String billNo = element.get("WorkOrderNo").asText();
                    String companyName = element.get("DealerFullName").asText();
                    String receptionistName = element.get("ReceptionEmpName").asText();
                    String carNumber = element.get("PlateNumber").asText();
                    String mileage = element.get("Mileage").asText();
                    String totalAmount = element.get("TotalAmount").asText();
                    String remark = element.get("TypeNameArr").asText();

                    String dateEnd = element.get("OrderTime").asText();
                    dateEnd = DateUtil.formatSQLDate(dateEnd);

                    Bill bill = new Bill();
                    bill.setBillNo(billNo);
                    bill.setCompanyName(companyName);
                    bill.setReceptionistName(receptionistName);
                    bill.setCarNumber(carNumber);
                    bill.setMileage(mileage);
                    bill.setTotalAmount(totalAmount);
                    bill.setRemark(remark);
                    bill.setDateEnd(dateEnd);

                    Response res2 = ConnectionUtil.doPostWithLeastParamJson(BILLSERVICE_URL, getBillDetailParam(billNo), COOKIE);
                    JsonNode serviceContent = formatDataToJson(res2);

                    JsonNode serviceData = serviceContent.get("rows");
                    if (serviceData.size() > 0) {
                        Iterator<JsonNode> services = serviceData.elements();

                        while (services.hasNext()) {
                            JsonNode e = services.next();

                            String serviceItemNames = e.get("RepairName").asText();

                            if (null != bill.getServiceItemNames() && !"".equals(serviceItemNames)) {
                                String s = bill.getServiceItemNames() + "," + serviceItemNames;
                                bill.setServiceItemNames(s);
                            }

                            if (null == bill.getServiceItemNames()) {
                                bill.setServiceItemNames(serviceItemNames);
                            }
                        }
                    }

                    Response res3 = ConnectionUtil.doPostWithLeastParamJson(BILLITEM_URL, getBillDetailParam(billNo), COOKIE);
                    JsonNode itemContent = formatDataToJson(res3);

                    JsonNode itemData = itemContent.get("rows");
                    if (itemData.size()>0){
                        Iterator<JsonNode> items = itemData.elements();

                        while (items.hasNext()){
                            JsonNode e = items.next();

                            String goodsNames = e.get("PartsName").asText();

                            if (null != bill.getGoodsNames() && !"".equals(goodsNames)) {
                                String goods = bill.getGoodsNames() + "," + goodsNames;
                                bill.setGoodsNames(goods);
                            }

                            if (null == bill.getGoodsNames()) {
                                bill.setGoodsNames(goodsNames);
                            }
                        }
                    }

                    bills.add(bill);
                }
            }
        }

        System.out.println("结果为" + bills.toString());

        String pathname = "C:\\exportExcel\\酷蛙快修消费记录.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);

    }

    private JsonNode formatDataToJson(Response response) throws IOException {

        String returnContent = response.returnContent().asString();
        returnContent = returnContent.replace("\\", "");

        String startRegEx = "rows";
        int start = returnContent.indexOf(startRegEx);

        String content = returnContent.substring(start - 1, returnContent.length() - 2);
        String formatJson = "{" + content;
        JsonNode result = JsonObject.MAPPER.readTree(formatJson);

        return result;
    }

    private int getTotalPage(Response response, int num) throws IOException {
        int totalPage = 0;

        String returnContent = response.returnContent().asString();
        String startRegEx = "total";
        String endRegEx = "rows";
        int start = returnContent.indexOf(startRegEx);
        int end = returnContent.indexOf(endRegEx);

        String totalStr = returnContent.substring(start + 9, end - 3);
        int total = Integer.parseInt(totalStr);

        if (total % num == 0) {
            totalPage = total / num;
        } else
            totalPage = total / num + 1;

        return totalPage;

    }

    private String getBillDetailParam(String billNo) {
        String param = "{" +
                "\"paraname\":[\"work_order_no\",\"is_delete\"]," +
                "\"expression\":[\"=\",\"=\"]," +
                "\"paravalue\":[\"" +
                billNo +
                "\",\"0\"]," +
                "\"pagesize\":\"\"," +
                "\"pageindex\":\"\"" +
                "}";

        return param;
    }

    private String getBillParam(int pageNo) {

        String param = "{" +
                "\"paraname\":[\"is_delete\",\"work_order_no\"" +
                ",\"plate_number\",\"customer_name\",\"reception_date\"" +
                ",\"reception_date\",\"settlement_time\",\"settlement_time\"]," +
                "\"expression\":[\"=\",\"~*\",\"~*\",\"~*\",\">=\",\"<\",\">=\",\"<=\"]," +
                "\"paravalue\":" + "[\"0\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]," +
                "\"pagesize\":" + 50 + "," +
                "\"pageindex\":" + pageNo +
                "}";

        return param;
    }
}
