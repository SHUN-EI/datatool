package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Bill;
import com.ys.datatool.domain.BillDetail;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
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


    private String fromDate = "2003-01-01";

    private String CARINFO_URL = "http://new.mrrjvip.com/MemberInfo/Query";

    private String BILL_URL = "http://new.mrrjvip.com/Consume/QueryBill";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Charset charset = Charset.forName("utf-8");

    private String CONTENT_TYPE = "application/json;charset=UTF-8";

    private String fieldName = "total";

    private String companyName = "茂日软件";

    private int num = 100;

    private String COOKIE = "aliyungf_tc=AQAAAPNnQHnFaAEAmghDcfyQ0m4IPf/4; ASP.NET_SessionId=nc01fqyltq5oxlbsbkydpdk5; __RequestVerificationToken=vKdwHUys2H5hgGMEE627LOoyRYDc3b9DZfYaTdCaLbqKRibbuLPN-UbGQoXlCjJkPeqZ6hHqPfOTN-Fb5If4HhcpfEYJUVy3UDP6Dyn1bWM1; .ASPXAUTH=F8B7E12F764F2F14B0445A5D09E423F79338E79C7647E408648B2FA6B4914FDB8B7687C296F873190D359DBEB1EB099FA0F0E801CFAA82422BAB6DBBDD8DDFE4649D0E4A89B6A4BBC25197259144B1A91EFBC6D62EFCF982A6D43C2998ADA9D6; SERVERID=7ec4522f7498fadd6917fbc71337ba6d|1552026094|1552011440\n";


    /**
     * 单据明细
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDetailDataStandard() throws IOException {
        List<BillDetail> billDetails = new ArrayList<>();
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(CARINFO_URL, getCarInfoParam(0), COOKIE, CONTENT_TYPE);
        int carTotalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 100);


        if (carTotalPage > 0) {
            int offset = 0;

            for (int i = 1; i <= carTotalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(CARINFO_URL, getBillParam(offset), COOKIE, CONTENT_TYPE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString(charset));

                offset += num;
                JsonNode dataNode = result.get("rows");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.elements();


                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String carId = e.get("Id").asText();
                    }
                }
            }
        }
        String aa = "";


    }


    /**
     * 单据
     *
     * @throws IOException
     */
    @Test
    public void fetchBillDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(0), COOKIE, CONTENT_TYPE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 100);


        if (totalPage > 0) {
            int offset = 0;

            for (int i = 1; i <= totalPage; i++) {

                response = ConnectionUtil.doPostWithLeastParamJson(BILL_URL, getBillParam(offset), COOKIE, CONTENT_TYPE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString(charset));

                offset += num;

                JsonNode dataNode = result.get("rows");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.elements();

                    while (it.hasNext()) {
                        JsonNode e = it.next();


                        String billNo = e.get("BillCode").asText();
                        System.out.println("单号为" + billNo);
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
