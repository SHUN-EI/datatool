package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
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
 * Created by mo on @date  2019/1/2.
 * 汽修掌上通
 */
@Service
public class QiXiuZhangShangTongService {

    private String SUPPLIER_URL = "http://xlc.qxgs.net/api/pc/def/sp/spsuppliers/find";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String CONTENT_TYPE = "application/json;charset=UTF-8";

    private String companyName = "汽修掌上通";

    private String COOKIE = "Hm_lvt_c86a6dea8a77cec426302f12c57466e0=1546399091; Hm_lpvt_c86a6dea8a77cec426302f12c57466e0=1546430546; sid=63e16a65-cdf5-461e-82dd-60097fbf2f47; shop=%22%E5%AE%89%E7%B4%A2%E6%B1%BD%E8%BD%A6%E5%85%BB%E6%8A%A4%E6%80%BB%E5%BA%97%22";


    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(SUPPLIER_URL, getParam(1), COOKIE, CONTENT_TYPE);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get(0).get("len");
        int totalPage = WebClientUtil.getTotalPage(totalNode, 10);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(SUPPLIER_URL, getParam(i), COOKIE, CONTENT_TYPE);
                JsonNode content = MAPPER.readTree(response.returnContent().asString());

                JsonNode node = content.get("data").get(0).get("results");
                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();

                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String name = element.get("name").asText();
                        String contactPhone = element.get("phone").asText();
                        String contactName = element.get("contact").asText();
                        String address = element.get("addr").asText();
                        String remark = element.get("remark").asText();
                        String depositBank = element.get("bankName").asText();
                        String accountNumber = element.get("bankNo").asText();

                        Supplier supplier = new Supplier();
                        supplier.setCompanyName(companyName);
                        supplier.setName(CommonUtil.formatString(name));
                        supplier.setContactPhone(CommonUtil.formatString(contactPhone));
                        supplier.setContactName(CommonUtil.formatString(contactName));
                        supplier.setAddress(CommonUtil.formatString(address));
                        supplier.setRemark(CommonUtil.formatString(remark));
                        supplier.setDepositBank(CommonUtil.formatString(depositBank));
                        supplier.setAccountNumber(CommonUtil.formatString(accountNumber));
                        suppliers.add(supplier);
                    }
                }
            }
        }


        String pathname = "C:\\exportExcel\\汽修掌上通供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }

    private String getParam(int pageNo) {
        String param = "{" +
                "\"all\":1," +
                "\"pageSize\":10," +
                "\"pageIndex\":" + pageNo +
                "}";

        return param;
    }
}
