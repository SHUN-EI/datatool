package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by mo on 2019/4/14
 * 车仆系统
 */
@Service
public class ChePuService {

    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private String COOKIE = "JSESSIONID=EFB9A09BD47E9710CFB6074E35776163-n1";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String BILL_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-asorder-findAsorders4Web.do";

    private String MEMBERCARD_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-mcard-findmemberCard.do";

    private String CARINFODETAIL_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-member-findMember.do";

    private String CARINFO_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-members-findStoreMembers.do";

    private String SERVICE_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-service-findServiceList.do";

    private String companyName = "车仆系统";

    private String fieldName = "result";

    private String beginDate = "2017-01-01";


    /**
     * 历史消费记录
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        //进行中的订单
        Response res1 = ConnectionUtil.doPostWithForm(BILL_URL, getBillWorkingParam(1), COOKIE);

        //已挂起的订单
        Response res2 = ConnectionUtil.doPostWithForm(BILL_URL, getBillHoldParam(1), COOKIE);

        //已挂账的订单
        Response res3 = ConnectionUtil.doPostWithForm(BILL_URL, getBillUnPaidParam(1), COOKIE);

        //已完成的订单
        Response res4 = ConnectionUtil.doPostWithForm(BILL_URL, getBillFinishedParam(1), COOKIE);

        //已失效的订单
        Response res5 = ConnectionUtil.doPostWithForm(BILL_URL, getBillFailedParam(1), COOKIE);

        int total1 = getBillTotalPage(res1);
        int total2 = getBillTotalPage(res2);
        int total3 = getBillTotalPage(res3);
        int total4 = getBillTotalPage(res4);
        int total5 = getBillTotalPage(res5);

        fetchBillWorkingData(bills, total1);
        fetchBillHoldData(bills, total2);
        fetchBillUnPaidData(bills, total3);
        fetchBillFinishedData(bills, total4);
        fetchBillFailedData(bills, total5);

        String pathname = "C:\\exportExcel\\车仆系统消费记录.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
    }


    //进行中
    private void fetchBillWorkingData(List<Bill> bills, int total) throws IOException {
        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                Response res = ConnectionUtil.doPostWithForm(BILL_URL, getBillWorkingParam(i), COOKIE);
                analysisBillData(res, bills, "进行中");
            }
        }
    }

    //已挂起
    private void fetchBillHoldData(List<Bill> bills, int total) throws IOException {
        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                Response res = ConnectionUtil.doPostWithForm(BILL_URL, getBillHoldParam(i), COOKIE);
                analysisBillData(res, bills, "已挂起");
            }
        }
    }

    //已挂账
    private void fetchBillUnPaidData(List<Bill> bills, int total) throws IOException {
        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                Response res = ConnectionUtil.doPostWithForm(BILL_URL, getBillUnPaidParam(i), COOKIE);
                analysisBillData(res, bills, "已挂账");
            }
        }
    }

    //已完成
    private void fetchBillFinishedData(List<Bill> bills, int total) throws IOException {
        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                Response res = ConnectionUtil.doPostWithForm(BILL_URL, getBillFinishedParam(i), COOKIE);
                analysisBillData(res, bills, "已完成");
            }
        }
    }

    //已失效
    private void fetchBillFailedData(List<Bill> bills, int total) throws IOException {
        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                Response res = ConnectionUtil.doPostWithForm(BILL_URL, getBillFailedParam(i), COOKIE);
                analysisBillData(res, bills, "已失效");
            }
        }
    }


    private void analysisBillData(Response res, List<Bill> bills, String remark) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString());

        Iterator<JsonNode> it = result.get("result").get("rows").elements();
        while (it.hasNext()) {
            JsonNode element = it.next();

            String billNo = element.get("orderNo").asText();
            String carNumber = element.get("plateNumber").asText();
            String receptionistName = element.get("pickupUname").asText();
            String totalAmount = element.get("orderTotalAmount").asText();
            String dateEnd = element.get("fillDate").asText();
            dateEnd = DateUtil.formatSQLDate(dateEnd);

            String serviceItemNames = "";
            Optional<JsonNode> snode = Optional.ofNullable(element.get("services"));
            if (snode.isPresent() && snode.get().size() > 0)
                serviceItemNames =  snode.get().get(0).get("serviceNames").asText();

            String goodsNames = "";
            Optional<JsonNode> gnode = Optional.ofNullable(element.get("goods"));
            if (gnode.isPresent() && gnode.get().size() > 0)
                goodsNames = gnode.get().get(0).get("goodsNames").asText();

            Bill bill = new Bill();
            bill.setCarNumber(carNumber);
            bill.setBillNo(billNo);
            bill.setCompanyName(companyName);
            bill.setServiceItemNames(serviceItemNames);
            bill.setGoodsNames(goodsNames);
            bill.setTotalAmount(totalAmount);
            bill.setReceptionistName(receptionistName);
            bill.setDateEnd(dateEnd);
            bill.setRemark(remark);

            bills.add(bill);
        }
    }


    /**
     * 车辆信息、会员卡、卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        List<MemberCard> memberCards = new ArrayList<>();
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithForm(CARINFO_URL, getParam(0), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithForm(CARINFO_URL, getParam(i), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode dataNode = result.get("result");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String carNumber = e.get("plateNumbers") != null ? e.get("plateNumbers").asText() : "";
                        String name = e.get("memberName") != null ? e.get("memberName").asText() : "";
                        String phone = e.get("mobileNumber") != null ? e.get("mobileNumber").asText() : "";
                        String carId = e.get("memberId").asText();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setCarId(carId);
                        carInfo.setName(CommonUtil.formatString(name));
                        carInfo.setPhone(CommonUtil.formatString(phone));
                        carInfo.setCompanyName(companyName);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String carId = carInfo.getCarId();

                //车辆信息
                Response res = ConnectionUtil.doPostWithForm(CARINFODETAIL_URL, "memberId=" + carId, COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode resulNode = result.get("result");
                JsonNode node = resulNode.get("autos");

                if (node != null && node.size() > 0) {
                    JsonNode carNode = node.get(0);

                    String vin = carNode.get("vinCode").asText();
                    String engineNumber = carNode.get("engineNo").asText();
                    String vcInsuranceValidDate = carNode.get("biEdate").asText();
                    String carModel = carNode.get("autotypeName").asText();
                    String brand = carNode.get("automodelName").asText();
                    String tcInsuranceValidDate = carNode.get("ciEdate").asText();

                    carInfo.setVINcode(CommonUtil.formatString(vin));
                    carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                    carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                    carInfo.setTcInsuranceValidDate(tcInsuranceValidDate);
                    carInfo.setCarModel(CommonUtil.formatString(carModel));
                    carInfo.setBrand(CommonUtil.formatString(brand));
                }

                //会员卡信息
                Response res2 = ConnectionUtil.doPostWithForm(MEMBERCARD_URL, "memberId=" + carId, COOKIE);
                JsonNode content = JsonObject.MAPPER.readTree(res2.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode dataNode = content.get("result");
                if (dataNode != null && dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode memberNode = it.next();

                        String cardCode = memberNode.get("mcardNo").asText();
                        String memberCardName = memberNode.get("mcardMame").asText();
                        String dateCreated = memberNode.get("startDate").asText();
                        String validTime = memberNode.get("stopDate").asText();
                        String balance = memberNode.get("mcardBalance").asText();
                        String remark = memberNode.get("cardStateName").asText();

                        MemberCard memberCard = new MemberCard();
                        memberCard.setCompanyName(companyName);
                        memberCard.setCardCode(cardCode);
                        memberCard.setMemberCardName(memberCardName);
                        memberCard.setCarNumber(carInfo.getCarNumber());
                        memberCard.setDateCreated(dateCreated);
                        memberCard.setName(carInfo.getName());
                        memberCard.setBalance(balance);
                        memberCard.setValidTime(validTime);
                        memberCard.setPhone(carInfo.getPhone());
                        memberCard.setRemark(remark);
                        memberCards.add(memberCard);

                        //卡内项目
                        JsonNode itemNode = memberNode.get("svcs");
                        if (itemNode != null && itemNode.size() > 0) {
                            Iterator<JsonNode> iterator = itemNode.iterator();

                            while (iterator.hasNext()) {
                                JsonNode e = iterator.next();

                                String itemName = e.get("svcName").asText();
                                String num = e.get("svcNum").asText();
                                String price = e.get("svcPrice").asText();
                                String isValidForever = e.get("isInfinite").asText();

                                if ("1".equals(isValidForever)) {
                                    num = "无限次";
                                }

                                MemberCardItem memberCardItem = new MemberCardItem();
                                memberCardItem.setCompanyName(companyName);
                                memberCardItem.setCardCode(cardCode);
                                memberCardItem.setItemName(itemName);
                                memberCardItem.setNum(num);
                                memberCardItem.setOriginalNum(num);
                                memberCardItem.setPrice(price);
                                memberCardItems.add(memberCardItem);
                            }
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\车仆车辆.xls";
        String pathname2 = "C:\\exportExcel\\车仆会员.xls";
        String pathname3 = "C:\\exportExcel\\车仆卡内项目.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname2);
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname3);

    }


    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithForm(SERVICE_URL, getServiceParam(0), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithForm(SERVICE_URL, getServiceParam(i), COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode dataNode = result.get("result");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String firstCategoryName = e.get("svctypePName").asText();
                        String secondCategoryName = e.get("svctypeName").asText();
                        String productName = e.get("serviceName").asText();
                        String price = e.get("goodsPrice").asText();
                        String code = e.get("serviceId").asText();
                        String brandName = e.get("servicePrice").asText();
                        String remark = e.get("memo").asText();

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setPrice(price);
                        product.setCode(code);
                        product.setBrandName(brandName);
                        product.setProductName(productName);
                        product.setItemType("服务项");
                        product.setFirstCategoryName(firstCategoryName);
                        product.setSecondCategoryName(secondCategoryName);
                        product.setRemark(remark);
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\车仆服务项目.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }


    private String getServiceParam(int pageNo) {
        String param = "row=15&stdsvcTag=1&page=" + pageNo;

        return param;
    }

    private String getParam(int pageNo) {
        String param = "pageSize=15&pageNo=" + pageNo;

        return param;
    }

    private String getBillWorkingParam(int pageNo) {
        String param = "_os=1&beginDate4Do=" +
                beginDate +
                "&endDate4Do=" +
                DateUtil.formatCurrentDate() +
                "&orderStateId=5001,6001" +
                "&rows=15&sidx=fillDate&sord=ASC" +
                "&page=" + pageNo;

        return param;
    }

    private String getBillHoldParam(int pageNo) {
        String param = "_os=1&beginDate4Do=" +
                beginDate +
                "&endDate4Do=" +
                DateUtil.formatCurrentDate() +
                "&orderStateId=5002,6002" +
                "&rows=15&sidx=fillDate&sord=ASC" +
                "&page=" + pageNo;

        return param;
    }


    private String getBillUnPaidParam(int pageNo) {
        String param = "_os=1&beginDate4Done=" +
                beginDate +
                "&endDate4Done=" +
                DateUtil.formatCurrentDate() +
                "&orderStateId=5005,6005" +
                "&rows=15&sidx=settlementTime&sord=DESC" +
                "&page=" + pageNo;

        return param;
    }

    private String getBillFailedParam(int pageNo) {
        String param = "_os=1&beginDate4Update=" +
                beginDate +
                "&endDate4Update=" +
                DateUtil.formatCurrentDate() +
                "&orderStateId=5004,6004" +
                "&rows=15&sidx=updateTime&sord=DESC" +
                "&page=" + pageNo;

        return param;
    }

    private String getBillFinishedParam(int pageNo) {
        String param = "_os=1&beginDate4Done=" +
                beginDate +
                "&endDate4Done=" +
                DateUtil.formatCurrentDate() +
                "&orderStateId=5003,6003" +
                "&rows=15&sidx=settlementTime&sord=DESC" +
                "&page=" + pageNo;

        return param;
    }

    private int getBillTotalPage(Response response) throws IOException {
        int totalPage = 0;

        if (response != null) {
            JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
            String countStr = result.get("result").get("total").asText();
            int count = Integer.parseInt(countStr);

            if (count > 0)
                totalPage = count;
        }
        return totalPage;
    }
}
