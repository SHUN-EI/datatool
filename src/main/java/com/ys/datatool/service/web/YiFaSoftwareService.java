package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.domain.entity.BillDetail;
import com.ys.datatool.domain.entity.Supplier;
import com.ys.datatool.util.CommonUtil;
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
 * Created by mo on 2019/5/17
 */
@Service
public class YiFaSoftwareService {

    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private String COOKIE = "username=13305722816; JSESSIONID=0A21FA3598C610DABB3930B75AB47270.s1";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String BILL_URL = "https://shop.bcgogo.com/web/inquiryCenter.do?method=inquiryCenterSearchOrderAction";

    private String SUPPLIER_URL = "https://shop.bcgogo.com/web/supplier.do?method=searchSupplierDataAction";

    private String companyName = "一发软件";


    /**
     * 历史消费记录和消费记录相关车辆
     * 打开路径:首页-待办事项-查询中心-单据查询
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(1), COOKIE);
        int totalPage = getBillTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= 3; i++) {
                Response res = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(i), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                if (result != null) {
                    JsonNode orderNode = result.get("data").get("orders");

                    if (orderNode.size() > 0) {
                        Iterator<JsonNode> it = orderNode.elements();

                        while (it.hasNext()) {
                            JsonNode e = it.next();

                            String billNo = e.get("receiptNo").asText();
                            String receptionistName = e.get("serviceWorkers").asText();
                            String totalAmount = e.get("amount").asText();//总计
                            String actualAmount = e.get("settled").asText();//实收
                            String state = e.get("orderStatusValue").asText();//状态
                            String type = e.get("orderTypeValue").asText();
                            String carNumber = e.get("vehicle").asText();

                            String content = e.get("orderContent").asText();
                            String company = e.get("shopName").asText();


                            String dateEnd = e.get("endDateStr").asText();
                            dateEnd = DateUtil.formatSQLDate(dateEnd);

                            Bill bill = new Bill();
                            bill.setCompanyName(company);
                            bill.setDateEnd(dateEnd);
                            bill.setCarNumber(carNumber);
                            bill.setBillNo(billNo);
                            bill.setReceptionistName(receptionistName);
                            bill.setTotalAmount(totalAmount);
                            bill.setRemark(type + "," + state);
                            bill.setContent(content);

                            JsonNode itemNode = e.get("itemIndexDTOs");
                            if (itemNode.size() > 0) {
                                Iterator<JsonNode> iterator = itemNode.elements();

                                while (iterator.hasNext()) {
                                    JsonNode node = iterator.next();

                                    String itemType = node.get("itemType").asText();

                                    String name = "";
                                    String price = "";
                                    String num = "";
                                    if ("SERVICE".equals(itemType)) {

                                        name = node.get("services").asText();
                                        price = node.get("itemPrice").asText();
                                        num = node.get("itemCount").asText();

                                        name = name + "*" + num + "(" + price + ")";
                                        if (null != bill.getServiceItemNames() && !"".equals(name)) {
                                            String s = bill.getServiceItemNames() + "," + name;
                                            bill.setServiceItemNames(s);
                                        }

                                        if (null == bill.getServiceItemNames()) {
                                            bill.setServiceItemNames(name);
                                        }

                                    }

                                    if ("MATERIAL".equals(itemType)) {
                                        name = node.get("itemName").asText();
                                        price = node.get("itemPrice").asText();
                                        num = node.get("itemCount").asText();

                                        name = name + "*" + num + "(" + price + ")";
                                        if (null != bill.getGoodsNames() && !"".equals(name)) {
                                            String s = bill.getGoodsNames() + "," + name;
                                            bill.setGoodsNames(s);
                                        }

                                        if (null == bill.getGoodsNames()) {
                                            bill.setGoodsNames(name);
                                        }
                                    }

                                }
                            }
                            bills.add(bill);
                        }
                    }
                }
            }
        }


        String pathname = "C:\\exportExcel\\一发软件消费记录.xls";
        String pathname2 = "C:\\exportExcel\\一发软件单据.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportBillSomeFieldDataInLocal(bills, ExcelDatas.workbook, pathname2);

    }

    /**
     * 供应商
     * 打开路径:首页-供应商管理
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(SUPPLIER_URL, getSupplierParam(1), COOKIE);
        int totalPage = getTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithLeastParamJson(SUPPLIER_URL, getSupplierParam(i), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

                if (result != null) {
                    JsonNode supplierNode = result.get(0).get("customerSuppliers");

                    if (supplierNode.size() > 0) {
                        Iterator<JsonNode> it = supplierNode.elements();

                        while (it.hasNext()) {
                            JsonNode e = it.next();

                            String name = e.get("name").asText();
                            String address = e.get("address").asText();
                            String code = e.get("idStr").asText();

                            String contactPhone = "";
                            String contactName = "";
                            JsonNode contact = e.get("contactDTOList");
                            if (contact.size() > 0) {
                                contactPhone = contact.get(0).get("mobile").asText();
                                contactName = contact.get(0).get("name").asText();
                            }

                            Supplier supplier = new Supplier();
                            supplier.setCompanyName(companyName);
                            supplier.setName(name);
                            supplier.setAddress(CommonUtil.formatString(address));
                            supplier.setCode(code);
                            supplier.setContactPhone(contactPhone);
                            supplier.setContactName(contactName);
                            suppliers.add(supplier);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\一发软件供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }


    private String getBillParam(int pageNo) {
        String param = "sort=created_time+desc&maxRows=20&orderStatusRepeal=YES&requestType=AJAX&startPageNo=" + pageNo;

        return param;
    }

    private String getSupplierParam(int pageNo) {
        String param = "customerOrSupplier=supplier&maxRows=15&startPageNo=" + pageNo;

        return param;
    }

    private int getBillTotalPage(Response response) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

        String totalStr = result.get("pager").get("totalPage").asText();
        int total = Integer.parseInt(totalStr);

        return total;
    }

    private int getTotalPage(Response response) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

        String totalStr = result.get(1).get("totalPage").asText();
        int total = Integer.parseInt(totalStr);

        return total;
    }

}
