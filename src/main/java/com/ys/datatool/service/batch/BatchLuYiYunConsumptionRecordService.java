package com.ys.datatool.service.batch;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExecutorConfig;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.domain.entity.BillDetail;
import com.ys.datatool.domain.entity.CarInfo;
import com.ys.datatool.util.ConnectionUtil;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by mo on 2019/5/16
 */
public class BatchLuYiYunConsumptionRecordService implements Runnable {


    private List<Bill> bills;

    private List<BillDetail> billDetails;

    private List<CarInfo> carInfos;

    private List<String> list;

    private String BILL_URL;

    private String BILLDETAIL_URL;

    private CountDownLatch countDownLatch;

    private String COOKIE;

    private String companyName;


    public BatchLuYiYunConsumptionRecordService(List<Bill> bills,
                                                List<BillDetail> billDetails,
                                                List<CarInfo> carInfos,
                                                List<String> list,
                                                String url,
                                                String detailUrl,
                                                String cookie,
                                                String companyName) {

        this.bills = bills;
        this.billDetails = billDetails;
        this.carInfos = carInfos;
        this.list = list;
        this.BILL_URL = url;
        this.BILLDETAIL_URL = detailUrl;
        this.COOKIE = cookie;
        this.countDownLatch = ExecutorConfig.countDownLatch;
        this.companyName = companyName;
    }


    public void fetchConsumptionRecordDatas(String index) throws IOException {

        System.out.println("-----------------正在抓取第" + index + "页的数据-----------------------------------");

        Response response = ConnectionUtil.doGetWith(BILL_URL + index, COOKIE);
        JsonNode body = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

        JsonNode data = body.get("data").get("data");

       /* if (data.size() > 0) {
            Iterator<JsonNode> iterator = data.elements();

            while (iterator.hasNext()) {
                JsonNode element = iterator.next();

                String billNo = element.get("work_list_no").asText();
                String bid = element.get("work_list_id").asText();
                String carNo = element.get("car_number").asText();
                String totalAmount = element.get("total_price").asText();

                String cname = element.get("name").asText();
                String mobile = element.get("phone").asText();

                String dateEnd = element.get("created_at").asText();
                dateEnd = DateUtil.formatSQLDate(dateEnd);


                //工单类型  all -全部  1-标准工单   2-卡券核销工单
                String type = element.get("type").asText();

                //工单状态  all -全部   1-已结算   2-已退单  3-进行中  4-整单挂账  5-部分挂账
                String status = element.get("status").asText();


                switch (type) {
                    case "1":
                        type = "标准工单";
                        break;
                    case "2":
                        type = "卡券核销工单";
                        break;
                }

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


                Response res = ConnectionUtil.doGetWith(BILLDETAIL_URL + bill.getBillId(), COOKIE);
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

                            if (null != bill.getServiceItemNames() && !"".equals(itemName)) {
                                itemName = itemName + "*" + originalPrice;
                                String s = bill.getServiceItemNames() + "," + itemName;
                                bill.setServiceItemNames(s);
                            }

                            if (null == bill.getServiceItemNames()) {
                                bill.setServiceItemNames(itemName + "*" + originalPrice);
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


                            if (null != bill.getGoodsNames() && !"".equals(itemName)) {
                                itemName = itemName + "*" + originalPrice;
                                String s = bill.getGoodsNames() + "," + itemName;
                                bill.setGoodsNames(s);
                            }

                            if (null == bill.getGoodsNames()) {
                                bill.setGoodsNames(itemName + "*" + originalPrice);
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
        }*/
    }


    @Override
    public void run() {

        if (null != list) {
            list.forEach(b -> {
                try {
                    fetchConsumptionRecordDatas(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        countDownLatch.countDown();
    }


}
