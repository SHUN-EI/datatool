package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018-06-12.
 * 4C系统
 */

@Service
public class FourCService {

    private String SUPPLIER_URL = "http://www.car-cloud.cn/Wy/WySupplier/LoadData";

    private String COOKIE = "UM_distinctid=163f1c740ba313-099c2ef31601ab-4323461-144000-163f1c740bbb5a; ASP.NET_SessionId=ek2lhloypptuxmnqta35vxsf; CNZZDATA1263794507=1449725428-1528765268-%7C1528767908; .ASPXAUTH=EB177373C5D4D769B1AB0FE0FD84010A6B64DABC93C767830D01AE71DB4CB808DF972A8AA970F0A38E1C4AE5B92C24F4A7C0FC8BE5BD6E82B53B26F8DEDE3F37C324FC2CB84659D408C3607E56D7FDBDDE85AA483D8E8BCA876788D86D49AE67C6B02372A6953B4AC294BEF811F5E835DD4525685B6338AC371BFE4BB67844D30E974FFF39A57E132D270584DA95920DB9A5E02EBFE1BEDBBE42F333EEE033A90CFA37601CF76617ACEE4C4153C2B1034EA3B1C6B65ED4927DA949C06BE5702FFF49C70B9FC8CF3AF5D29D5E2305B5EA9606D6EEBDFE0C7934606F82B5717CE1F8EFC3B9E1A5B6FCB7890869CDE0119CE6B6253727B1F99EC74198A1FD86798B7238B2D2857251CDC19C120B9FE5E2D3F53340FB3CBFC61ACA33878B08F276436D41A7AA3C066ABC3D2A704A297149B61229DE3B8326FC7F1F643B975FAC20FB49DF61EB93C1D8645A9B12A4EFF08E92EED9773C4CBB2B66B8C5C2FB945197FABA5C21146B176A5CE6D6410DB0574D2DFCBE179E2119B288BCC20561884381C3A958FCCA09537BDA06CE62EA733B01FC9FDCBE6241CAAB8F52E7E54FD93C6A0D573D8AFE281B7E55CB1C04F0DFD597997F44CFE853AA20AAF19FC4853BC1D517363DF6E398CB6EC6CA2D653A690E1CC813C58E73E68989D4F4F4A87DFE4BBC7A532A3FB780E38E9B04A0CBCD7D06F7126B32856DEEA156E3112D04D4CDFD349549EACD2C2883D290AC67BE1C4EFE58DDBD753182E8486B14826BC84FB04A24865353D3C9347AEA2BB28793F46E5E5DE6ED7198BB7E007F1F6039A425EC8593782D304BFD4EF14427732D67137E2355039B591455A15497E9C9203B08A4C5DEF4B0CEA4B5B0C1A2FEA0D8753D587B69CC55F7C3F9C09179F6DD2D44C3607EF1FA6E46E864BE26101A35BAF28FA07488F670642C9AF4EA9C10BD644B427E14E1F292404A5795A0221E86BF7F22A30B7E02";

    private String fieldName = "total";

    private int num = 200;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Workbook workbook;

    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String contactName = element.get("Contact").asText();
                    String contactPhone = element.get("Phone").asText();

                    String name = element.get("Name").asText();
                    String address = element.get("Address").asText();
                    String remark = element.get("Stype").asText();
                    String fax = element.get("Companyphone").asText();//公司电话

                    String manager = element.get("BusinessName").asText();
                    String managerPhone = element.get("BusinessPhone").asText();

                    Supplier supplier = new Supplier();
                    supplier.setContactName(contactName == "null" ? "" : contactName);
                    supplier.setContactPhone(contactPhone == "null" ? "" : contactPhone);
                    supplier.setName(name == "null" ? "" : name);
                    supplier.setAddress(address == "null" ? "" : address);
                    supplier.setRemark(remark == "null" ? "" : remark);
                    supplier.setFax(fax == "null" ? "" : fax);
                    supplier.setManager(manager == "null" ? "" : manager);
                    supplier.setManagerPhone(managerPhone == "null" ? "" : managerPhone);
                    suppliers.add(supplier);
                }
            }

        }

        System.out.println("结果为" + suppliers.toString());
        System.out.println("大小为" + suppliers.size());

        String pathname = "D:\\4C供应商导出.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, workbook, pathname);

    }

    private List<BasicNameValuePair> getParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("page", pageNo));
        params.add(new BasicNameValuePair("rows", "200"));

        return params;
    }

}
