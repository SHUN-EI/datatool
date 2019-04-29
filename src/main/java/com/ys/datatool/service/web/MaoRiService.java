package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.domain.entity.BillDetail;
import com.ys.datatool.domain.entity.CarInfo;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on 2019/3/7
 * <p>
 * 茂日软件
 */
@Service
public class MaoRiService {


    private String BILLDETAIL_URL = "http://new.mrrjvip.com/MemberCard/ConsumeLogs";

    private String CARINFO_URL = "http://new.mrrjvip.com/MemberInfo/Query";

    private String BILL_URL = "http://new.mrrjvip.com/Consume/QueryBill";

    private String fromDate = "2003-01-01";

    private String fieldName = "total";

    private String companyName = "茂日软件";

    private int num = 100;

    private String COOKIE = "aliyungf_tc=AQAAAPNnQHnFaAEAmghDcfyQ0m4IPf/4; ASP.NET_SessionId=nc01fqyltq5oxlbsbkydpdk5; __RequestVerificationToken=vKdwHUys2H5hgGMEE627LOoyRYDc3b9DZfYaTdCaLbqKRibbuLPN-UbGQoXlCjJkPeqZ6hHqPfOTN-Fb5If4HhcpfEYJUVy3UDP6Dyn1bWM1; .ASPXAUTH=6675E931CA2BE1005A55ADA7F4A2A306667B4B2207D77F26E3E5FBFCB8891E078D5EFE49DF5357695F70837208FF5496A5DF6019B68D9DDEC0870D7144C5D70FE02E4D3938AE004217885CA8CAB52F48F200BB44B3D0D7B4D41C8B2545B7C2C7; SERVERID=7ec4522f7498fadd6917fbc71337ba6d|1552034099|1552011440";


    /**
     * 单据明细
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDetailDataStandard() throws IOException {
        List<BillDetail> billDetails = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(CARINFO_URL, getCarInfoParam(0), COOKIE, WebConfig.CONTENT_TYPE);
        int carTotalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 100);


        if (carTotalPage > 0) {
            int offset = 0;

            for (int i = 1; i <= carTotalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(CARINFO_URL, getCarInfoParam(offset), COOKIE, WebConfig.CONTENT_TYPE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                offset += num;
                JsonNode dataNode = result.get("rows");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.elements();


                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String carId = e.get("Id").asText();
                        String name = e.get("Name").asText();
                        String phone = e.get("Phone").asText();
                        String carNumber = e.get("CarNumber").asText();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarId(CommonUtil.formatString(carId));
                        carInfo.setName(CommonUtil.formatString(name));
                        carInfo.setPhone(CommonUtil.formatString(phone));
                        carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String id = carInfo.getCarId();

                Response res = ConnectionUtil.doPostWithLeastParamJson(BILLDETAIL_URL, getBillDetailParam(0, id), COOKIE, WebConfig.CONTENT_TYPE);
                int billDetailTotalPage = WebClientUtil.getTotalPage(res, JsonObject.MAPPER, fieldName, 100);

                if (billDetailTotalPage > 0) {
                    int offset = 0;
                    for (int i = 1; i <= billDetailTotalPage; i++) {

                        res = ConnectionUtil.doPostWithLeastParamJson(BILLDETAIL_URL, getBillDetailParam(offset, id), COOKIE, WebConfig.CONTENT_TYPE);
                        JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                        offset += num;
                        JsonNode dataNode = result.get("rows");
                        if (dataNode.size() > 0) {
                            Iterator<JsonNode> it = dataNode.elements();

                            while (it.hasNext()) {
                                JsonNode e = it.next();


                                String billNo = e.get("BillCode").asText();
                                String dateEnd = e.get("BillDate").asText();
                                String cardCode = e.get("MemberCardCode").asText();
                                String num = e.get("Amount").asText();
                                String price = e.get("DiscountMoney").asText();
                                String itemName = e.get("ItemName").asText();

                                BillDetail billDetail = new BillDetail();
                                billDetail.setCompanyName(companyName);
                                billDetail.setBillNo(CommonUtil.formatString(billNo));
                                billDetail.setDateEnd(CommonUtil.formatString(dateEnd));
                                billDetail.setNum(CommonUtil.formatString(num));
                                billDetail.setPrice(CommonUtil.formatString(price));
                                billDetail.setItemName(CommonUtil.formatString(itemName));
                                billDetails.add(billDetail);
                            }
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\茂日单据明细.xls";
        String pathname2 = "C:\\exportExcel\\茂日单据车辆信息.xls";
        ExportUtil.exportBillDetailSomeFieldDataInLocal(billDetails, ExcelDatas.workbook, pathname);
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname2);
    }


    /**
     * 单据
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(0), COOKIE, WebConfig.CONTENT_TYPE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 100);


        if (totalPage > 0) {
            int offset = 0;

            for (int i = 1; i <= totalPage; i++) {

                response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(offset), COOKIE, WebConfig.CONTENT_TYPE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                offset += num;

                JsonNode dataNode = result.get("rows");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.elements();

                    while (it.hasNext()) {
                        JsonNode e = it.next();


                        String billNo = e.get("BillCode").asText();
                        String dateEnd = e.get("BillDate").asText();
                        String clientName = e.get("CustomerName").asText();
                        String totalAmount = e.get("PaymentMoney").asText();
                        String ItemName = e.get("ItemNames").asText();
                        String receptionistName = e.get("EmpNames").asText();
                        String cardCode = e.get("MemberCardCode").asText();
                        String remark = e.get("Remark").asText();


                        Bill bill = new Bill();
                        bill.setCompanyName(companyName);
                        bill.setBillNo(CommonUtil.formatString(billNo));
                        bill.setDateEnd(CommonUtil.formatString(dateEnd));
                        bill.setName(CommonUtil.formatString(clientName));
                        bill.setTotalAmount(CommonUtil.formatString(totalAmount));
                        bill.setItemName(CommonUtil.formatString(ItemName));
                        bill.setReceptionistName(CommonUtil.formatString(receptionistName));
                        bill.setRemark(CommonUtil.formatString(remark));
                        bill.setCardCode(CommonUtil.formatString(cardCode));
                        bills.add(bill);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\茂日单据.xls";
        ExportUtil.exportBillSomeFieldDataInLocal(bills, ExcelDatas.workbook, pathname);
    }


    private String getBillDetailParam(int offset, String id) {
        String param = "{\"pageSize\":" +
                num +
                ",\"offset\":" +
                offset +
                ",\"startDate\":\"\",\"endDate\":\"\",\"memberId\":\"" +
                id +
                "\"}";


        return param;
    }


    private String getCarInfoParam(int offset) {
        String param = "{\"pageSize\":" +
                num +
                ",\"offset\":" +
                offset +
                ",\"order\":\"asc\",\"keyWord\":\"\"" +
                "}";

        return param;
    }

    private String getBillParam(int offset) {
        String param = "{\"pageSize\":" +
                num +
                "," +
                "\"offset\":" +
                offset +
                ",\"order\":\"desc\"," +
                "\"sortName\":\"BillCode\"," +
                "\"billQueryType\":\"BillDate\"," +
                "\"BillSource\":\"-1\"," +
                "\"startDate\":\"" +
                fromDate +
                "\"" +
                ",\"endDate\":\"" +
                DateUtil.formatCurrentDate() +
                "\"" +
                ",\"status\":\"-1\"," +
                "\"keyWord\":\"\"}";

        return param;
    }
}
