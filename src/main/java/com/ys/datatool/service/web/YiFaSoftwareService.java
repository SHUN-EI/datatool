package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.Supplier;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
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


    private String COOKIE = "username=13305722816; JSESSIONID=E438349CB1F192CF4626A04669E03A6D.s1; keepCurrentStep=YES";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String SUPPLIER_URL = "https://shop.bcgogo.com/web/supplier.do?method=searchSupplierDataAction";

    private String companyName = "一发软件";


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


    private String getSupplierParam(int pageNo) {
        String param = "customerOrSupplier=supplier&maxRows=15&startPageNo=" + pageNo;

        return param;
    }

    private int getTotalPage(Response response) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());

        String totalStr = result.get(1).get("totalPage").asText();
        int total = Integer.parseInt(totalStr);

        return total;
    }

}
