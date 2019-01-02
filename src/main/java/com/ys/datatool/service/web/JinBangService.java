package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Bill;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2019/1/2.
 * <p>
 * 金邦会员管理系统
 */

@Service
public class JinBangService {

    private String MEMBERCARD_URL = "http://www.600vip.cn/Member/Member/GetMemberList";

    private String BILL_URL = "http://www.600vip.cn/Member/Member/OrderLogList";

    private String BILLDETAIL_URL = "http://www.600vip.cn/Member/Member/OrderDetailList";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "total";

    private String companyName = "金邦会员管理系统";

    private String COOKIE = "luckcode=158143; luckchain=uid=10312; rememberPassword=1; ucompcode=AuPGKdid1NM=; uaccount=qZZOA1XVj9A=; upwd=grxW9Nj3JK7IIx62nh+UlQ==; Hm_lvt_eb92647b72da97bebb9f81b44b7581a2=1545722861,1546398448; sid=6540e33a9880486e9419c091cae66390; Hm_lpvt_eb92647b72da97bebb9f81b44b7581a2=1546422614";


    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        Response response = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, getParam(1), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 40);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, getParam(i), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                JsonNode node = result.get("rows");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String id = element.get("Id").asText();
                        ids.add(id);
                    }
                }
            }
        }

        if (ids.size() > 0) {
            for (String id : ids) {
                Response res = ConnectionUtil.doPostWithJson(BILL_URL, getBillParam(id, 1), COOKIE);
                int billTotalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 40);

                if (billTotalPage > 0) {
                    for (int i = 1; i <= billTotalPage; i++) {
                        res = ConnectionUtil.doPostWithJson(BILL_URL, getBillParam(id, i), COOKIE);
                        JsonNode result = MAPPER.readTree(res.returnContent().asString());

                        JsonNode node = result.get("rows");
                        if (node.size() > 0) {
                            Iterator<JsonNode> it = node.iterator();

                            while (it.hasNext()) {
                                JsonNode element = it.next();

                                String billNo = element.get("BillCode").asText();
                                String carNumber = element.get("CardID").asText();
                                String totalAmount = element.get("PayMoney").asText();
                                String remark = element.get("Remark").asText();
                                String dateEnd = element.get("CreateTime").asText();
                                dateEnd = getCreateTime(dateEnd);

                                Bill bill = new Bill();
                                bill.setCompanyName(companyName);
                                bill.setDateEnd(dateEnd);
                                bill.setCarNumber(carNumber);
                                bill.setBillNo(billNo);
                                bill.setTotalAmount(totalAmount);
                                bill.setServiceItemNames(CommonUtil.formatString(remark));

                                //汇总配件
                                Response res2 = ConnectionUtil.doPostWithJson(BILLDETAIL_URL, getBillDetailParam(billNo), COOKIE);
                                JsonNode content = MAPPER.readTree(res2.returnContent().asString());
                                JsonNode data = content.get("rows");
                                if (data.size() > 0) {
                                    Iterator<JsonNode> iterator = data.iterator();

                                    while (iterator.hasNext()) {
                                        JsonNode e = iterator.next();

                                        String goodName = e.get("GoodsName").asText();
                                        String goodCode = e.get("GoodsCode").asText();
                                        String totalMoney = e.get("TotalMoney").asText();
                                        String num = e.get("Number").asText();

                                        String goodsNames = goodName + "[" + goodCode + "]" + "*" + num + "(" + totalMoney + ")";

                                        if (bill.getGoodsNames() != null && !"".equals(bill.getGoodsNames())) {
                                            goodsNames = bill.getGoodsNames() + "," + goodsNames;
                                            bill.setGoodsNames(goodsNames);
                                        }

                                        if (bill.getGoodsNames() == null) {
                                            bill.setGoodsNames(goodsNames);
                                        }
                                    }
                                }

                                bills.add(bill);
                            }
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\金邦会员管理系统消费记录.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
    }

    private String getCreateTime(String date) {
        if (!"".equals(date)) {
            date = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
        }

        return date;
    }

    private String getParam(int pageNo) {
        String param = "page=" + pageNo + "&rows=40";

        return param;
    }

    private String getBillParam(String id, int pageNo) {
        String param = "rows=40&sort=CreateTime&order=desc&page=" +
                pageNo +
                "&memIDByOrder=" + id;

        return param;
    }

    private String getBillDetailParam(String billCode) {
        String param = "billCode=" + billCode;

        return param;
    }
}
