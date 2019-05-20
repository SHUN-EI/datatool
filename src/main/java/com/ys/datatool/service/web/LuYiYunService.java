package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.domain.entity.BillDetail;
import com.ys.datatool.domain.entity.CarInfo;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.DateUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on 2019/5/16
 * 路易云店-门店管理系统
 */
@Service
public class LuYiYunService {


    /////////////////////////////////工具使用前，请先填写COOKIE数据（注意：这里的COOKIE需要取Response Headers->Set-Cookie的值）/////////////////////////////////////


    private String COOKIE = "laravel_session=eyJpdiI6InR5RFprMENXMENlVFhPdEZKM3psMmc9PSIsInZhbHVlIjoiZ2FrQ2RLcDVcL0hrT2xQN3VSdmdKUHJhdDh5RndYUmpwYnpGN3lVK010NWsybzYzaVJ4K0I5SjBsRWFzY2NKSDllYUdsSzJmUXhQa3hyU0pjXC93R1JlZz09IiwibWFjIjoiNmJiYjY0ODIzYzQ5MmRmZjRkMTY5MTI4ZDVkY2Q3ZmFlYzRmOGI2OGQ1YmQwMTMyZWRhM2U0ZDJiYjczOTQ1MCJ9; expires=Thu, 16-May-2019 11:42:09 GMT; Max-Age=7200; path=/; HttpOnly";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String BILL_URL = "http://mendian.luyiyun.cn/worklist?clerk_id=all&status=all&type=all&perpage=60&page=";

    private String BILLDETAIL_URL = "http://mendian.luyiyun.cn/worklist/";

    private String CARINFO_URL = "http://mendian.luyiyun.cn/customer?customer_type=&label_id=0&perpage=60&page=";

    private String CARINFODETAIL_URL = "http://mendian.luyiyun.cn/customer/";

    private String companyName = "路易云店系统";

    private String fieldName = "total_num";


    /**
     * 车辆信息
     * 打开路径:客户信息-详情
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(CARINFO_URL + 1, COOKIE);
        int totalPage = WebClientUtil.getTotalPageNo(response, fieldName, 60);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {

                Response res = ConnectionUtil.doGetWith(CARINFO_URL + i, COOKIE);
                JsonNode body = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode data = body.get("data").get("data");
                if (data.size() > 0) {
                    Iterator<JsonNode> iterator = data.elements();

                    while (iterator.hasNext()) {
                        JsonNode element = iterator.next();
                        String customerId = element.get("customer_id").asText();

                        res = ConnectionUtil.doGetWith(CARINFODETAIL_URL + customerId, COOKIE);
                        JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                        String name = "";
                        String phone = "";

                        JsonNode dataNode = result.get("data");

                        JsonNode customer = dataNode.get("customer_info");
                        if (customer != null) {
                            name = customer.get("name").asText();
                            phone = customer.get("phone").asText();
                        }

                        JsonNode carNode = dataNode.get("car_info");
                        if (carNode.size() > 0) {
                            Iterator<JsonNode> it = carNode.elements();

                            while (it.hasNext()) {
                                JsonNode e = it.next();

                                String carNumber = e.get("car_number").asText();
                                String brand = e.get("model_name").asText();
                                String vin = e.get("frame_number").asText();
                                String engineNumber = e.get("engine_model").asText();
                                String tcInsuranceCompany = e.get("insurance_company").asText();
                                String tcInsuranceValidDate = e.get("insurance_out").asText();


                                CarInfo carInfo = new CarInfo();
                                carInfo.setName(name);
                                carInfo.setPhone(phone);
                                carInfo.setCompanyName(companyName);
                                carInfo.setCarNumber(carNumber);
                                carInfo.setBrand(brand);
                                carInfo.setVINcode(vin);
                                carInfo.setEngineNumber(engineNumber);
                                carInfo.setTcInsuranceCompany(tcInsuranceCompany);
                                carInfo.setTcInsuranceValidDate(tcInsuranceValidDate);
                                carInfos.add(carInfo);
                            }
                        }


                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\路易云店系统车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }


    /**
     * 历史消费记录和消费记录相关车辆
     * <p>
     * 打开路径:工单-工单详情
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(BILL_URL + 1, COOKIE);
        int totalPage = WebClientUtil.getTotalPageNo(response, fieldName, 60);

        if (totalPage > 0) {

            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWith(BILL_URL + i, COOKIE);
                JsonNode body = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode data = body.get("data").get("data");

                if (data.size() > 0) {
                    Iterator<JsonNode> iterator = data.elements();

                    while (iterator.hasNext()) {
                        JsonNode element = iterator.next();

                        String billNo = element.get("work_list_no").asText();
                        String carNo = element.get("car_number").asText();
                        String bid = element.get("work_list_id").asText();
                        String totalAmount = element.get("total_price").asText();

                        String cname = element.get("name").asText();
                        String mobile = element.get("phone").asText();

                        String dateEnd = element.get("created_at").asText();
                        dateEnd = DateUtil.formatSQLDate(dateEnd);


                        //工单类型  all -全部  1-标准工单   2-卡券核销工单
                        String type = element.get("type").asText();

                        //工单状态  all -全部   1-已结算   2-已退单  3-进行中  4-整单挂账  5-部分挂账
                        String status = element.get("status").asText();


                        switch (status) {
                            case "1":
                                status = "已结算";
                                break;
                            case "2":
                                status = "已退单";
                                break;
                            case "3":
                                status = "进行中";
                                break;
                            case "4":
                                status = "整单挂账";
                                break;
                            case "5":
                                status = "部分挂账";
                                break;
                        }


                        switch (type) {
                            case "1":
                                type = "标准工单";
                                break;
                            case "2":
                                type = "卡券核销工单";
                                break;
                        }


                        Bill bill = new Bill();
                        bill.setCompanyName(companyName);
                        bill.setBillId(bid);
                        bill.setBillNo(billNo);
                        bill.setDateEnd(dateEnd);
                        bill.setCarNumber(carNo);
                        bill.setTotalAmount(totalAmount);
                        bill.setRemark(type + "," + status);
                        bill.setName(cname);
                        bill.setPhone(mobile);
                        bills.add(bill);


                        res = ConnectionUtil.doGetWith(BILLDETAIL_URL + bill.getBillId(), COOKIE);
                        JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                        JsonNode dataNode = result.get("data");


                        if (dataNode != null) {

                            JsonNode worker = dataNode.get("worklist_info");
                            if (worker != null) {
                                String receptionistName = worker.get("clerk_name").asText();
                                bill.setReceptionistName(receptionistName);
                            }

                            JsonNode customer = dataNode.get("customer_info");
                            if (customer != null) {

                                String mileage = customer.get("mileage").asText();
                                bill.setMileage(mileage);


                                String name = customer.get("name").asText();
                                String phone = customer.get("phone").asText();
                                String carNumber = customer.get("car_number").asText();
                                String model = customer.get("model_name").asText();
                                String vin = customer.get("frame_number").asText();

                                if ("".equals(carNumber)) {
                                    bill.setCarNumber(name);
                                }

                                CarInfo carInfo = new CarInfo();
                                carInfo.setName(name);
                                carInfo.setCompanyName(companyName);
                                carInfo.setPhone(phone);
                                carInfo.setCarNumber(carNumber);
                                carInfo.setVINcode(vin);
                                carInfo.setCarModel(model);
                                carInfos.add(carInfo);
                            }


                            JsonNode service = dataNode.get("service_info");
                            if (service.size() > 0) {
                                Iterator<JsonNode> it = service.elements();

                                while (it.hasNext()) {
                                    JsonNode e = it.next();

                                    String itemName = e.get("service_name").asText();
                                    String num = e.get("quantity").asText();
                                    String sid = e.get("service_id").asText();
                                    String originalPrice = e.get("original_price").asText();//原始单价
                                    String actualPrice = e.get("actual_price").asText();//实际支付价格
                                    String totalPrice = e.get("total_price").asText();//单项总价

                                    itemName = itemName + "*" + num + "(" + originalPrice + ")";
                                    if (null != bill.getServiceItemNames() && !"".equals(itemName)) {
                                        String s = bill.getServiceItemNames() + "," + itemName;
                                        bill.setServiceItemNames(s);
                                    }

                                    if (null == bill.getServiceItemNames()) {
                                        bill.setServiceItemNames(itemName);
                                    }


                                    BillDetail billDetail = new BillDetail();
                                    billDetail.setCompanyName(companyName);
                                    billDetail.setBillNo(bill.getBillNo());
                                    billDetail.setItemName(itemName);
                                    billDetail.setNum(num);
                                    billDetail.setPrice(originalPrice);
                                    billDetail.setItemType("服务项");
                                    billDetail.setItemCode(sid);
                                    billDetails.add(billDetail);
                                }
                            }


                            JsonNode product = dataNode.get("product_info");
                            if (product.size() > 0) {
                                Iterator<JsonNode> it = product.elements();

                                while (it.hasNext()) {
                                    JsonNode e = it.next();

                                    String itemName = e.get("product_name").asText();
                                    String num = e.get("quantity").asText();
                                    String pid = e.get("product_id").asText();
                                    String originalPrice = e.get("original_price").asText();//原始单价
                                    String actualPrice = e.get("actual_price").asText();//实际支付价格
                                    String totalPrice = e.get("total_price").asText();//单项总价

                                    itemName = itemName + "*" + num + "(" + originalPrice + ")";
                                    if (null != bill.getGoodsNames() && !"".equals(itemName)) {
                                        String s = bill.getGoodsNames() + "," + itemName;
                                        bill.setGoodsNames(s);
                                    }

                                    if (null == bill.getGoodsNames()) {
                                        bill.setGoodsNames(itemName);
                                    }

                                    BillDetail billDetail = new BillDetail();
                                    billDetail.setCompanyName(companyName);
                                    billDetail.setBillNo(bill.getBillNo());
                                    billDetail.setItemName(itemName);
                                    billDetail.setNum(num);
                                    billDetail.setPrice(originalPrice);
                                    billDetail.setItemType("商品");
                                    billDetail.setItemCode(pid);
                                    billDetails.add(billDetail);
                                }
                            }
                        }
                    }
                }
            }
        }


        String pathname = "C:\\exportExcel\\路易云店系统消费记录.xls";
        String pathname2 = "C:\\exportExcel\\路易云店系统消费记录-车辆.xls";
        String pathname3 = "C:\\exportExcel\\路易云店系统单据.xls";
        String pathname4 = "C:\\exportExcel\\路易云店系统单据明细.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname2);
        ExportUtil.exportBillDataInLocal(bills, ExcelDatas.workbook, pathname3);
        ExportUtil.exportBillDetailDataInLocal(billDetails, ExcelDatas.workbook, pathname4);

    }


}
