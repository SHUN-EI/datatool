package com.ys.datatool.service.web;

import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.HtmlTag;
import com.ys.datatool.domain.entity.CarInfo;
import com.ys.datatool.domain.entity.Supplier;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 2019/5/21
 * 惠车邦智慧门店
 */
@Service
public class HuiCheBangService {

    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private String COOKIE = "HCBSMART_LOGIN_KEY=A50ADD70CC4E82166C3D4AB1EADC1CB6064F1B4278EAB8759382A6496FBC33186F2BB6A20383BC053EA3146D12ABFFE0D4C3D78980EA37EE65E48737924FBF47AD5A2435A768475634A01CA5D980DAC69D4EC9436A0CBC3C3217716BE12742840A6B78B43075889277B3A128C030E63CFFA6C4BAA06603D1D97C5E2F3262B9F0462B3CDE09C487C8840DCCA9FD5DC7EC2F9D5390B05A9ADFF5CB5C11F9A138E0A9F92BF3CB1D85993A593B2E83996CCA6928113DEB68BEFB3705C061F7C7A5D62EA783BB83A40D976C650813FD401180F4E73D63C27B95F60C8AB4B134E0B1CB805B3AA39D3A18CD258E3D3048B3C3804547266034BE1E98D631600749FA6E6CBAB9A464B32FFD1B8BF1303DDE105BF62FE3AC793C9CBCC333BE660F05D29829D2FDFF7EA80CCED361738A83BFDC59D0E558366075ACF7D1E9E2137A5BE8DFC2B7EEE90C0D083597FD823BCFDD77E4AE3D2FF8AD9D8608D7B7321988E2B4995F20F107E1CF567EE699833DD17C7B9856B32CEE25E9F4FAE137DD1503B328DA2F7991E59ABCAAEEBD0DF73F91658927DA44B4DE6987E78EFA78FB4632D67C939DF901571842FC6F9E58FDB0A6A4D7A9C00352594AE90915999A45644480FFE49960BDC9F4AE7D8E0FDD26E9CFA449A8C38AF623B5347F63A5601A02D662CE3D8BD96B4837349B8FC13D80093058E7D33EDEC5A8A8C4FE07A336AF7D215AB630D9A14EB26C32C2459C305C0B8C18B480638A84DFC7EF368823FF0A2DDFB47B33C36562B2127557DC85F66EA1AC1464A33D9D24AB15F6BD8D77685AA9A685DC26514071D2FB7186B26D8E05D3541E14698CB9459098E0D7E260C5A2DF25F6FF96C25B70B1BB40C39EC14684053B90DC0AC61DEAB4A08B01A0932A3732CB6764416511853DBFA323AD93F21A3B232FF6826248B87D1DC7B327BCEF0DDA0E8269AF2C8C1A02ACACAAAA073055DFD421D4C94E4EC2947DBC3FEB575038219D9AE33C8C4F59F759A1657F83683121800117A3794F603D35D846C8F3ED94439CE74D21731B685B7423061611BFCD296A247ED6AE9DFFCCDFBAA4817B8958E85968450631A07AB64E478257BBB5D980F33A57516CA7A565111DB25E21065619FB65F6B3A621F6562A7BB258DC37D27BE098732EE1B68CAA8F6B9385EA4670F252CDA41300A59EBBE80CCE0D0FD3772541FF0C9D77FF883D3ABD7E684AE9D5C83DBCAAEA381514D784CF0792DF5D032386F6E1BE676628275CDF695739D579B39866FE0C3FED9E7631BA21A259580B5743EBCFC5359DD20B682F015458163AE869D17C74D2C4B625EECF4A1FE4A4A179F5F025C98EA3B7F3198A4FF5134CE90264DA5F04A4EE64B09F56B106C3E1BFFECB4F05FD257E7008C4DBE81651E17F3A8AEBE714A0E98D44D7FF1992F576432E6790387FA45B900F9C04C73EECD4844C9C60B47389; HCBSMART_CID=487; TVIP=http%3A%2F%2F192.168.1.104%3A23021; ASP.NET_SessionId=dmvx3tlnntdjlioqce55axll";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String CARINFODETAIL_URL = "http://crm.huichebang.cn/CarCenter/OCarInfoEdit?id=";

    private String CARINFO_URL = "http://crm.huichebang.cn/CarCenter/AllIndex?v=3.3&pageIndex=";

    private String SUPPLIER_URL = "http://crm.huichebang.cn/Stock/ShopSupplierList?DeptState=0&RoleText1=All&pageIndex=";

    private String companyName = "惠车邦智慧门店";


    /**
     * 车辆信息
     * 打开路径:首页-车主-所有车主
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(CARINFO_URL + 1, COOKIE);
        int totalPage = getTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWith(CARINFO_URL + i, COOKIE);

                String html = res.returnContent().asString();
                Document body = Jsoup.parseBodyFragment(html);

                String trRegEx = "#gridContent > tbody > tr";

                int trSize = WebClientUtil.getTagSize(body, trRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {

                        String nameRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(2) > div:nth-child(1)";
                        String phoneRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(2) > div:nth-child(2)";
                        String carNumberRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(9)";
                        String balanceRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(3) > span";

                        String name = body.select(nameRegEx).text();
                        String phone = body.select(phoneRegEx).text();
                        String carNumber = body.select(carNumberRegEx).text();
                        String balance = body.select(balanceRegEx).text();

                        String tdRegEx = trRegEx + ":nth-child(" + j + ") > td";
                        int tdSize = WebClientUtil.getTagSize(body, tdRegEx, HtmlTag.tdName);
                        String detailRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(" + tdSize + ") > input:nth-child(1)";
                        String value = body.select(detailRegEx).attr("onclick");

                        String idRegEx = "(?<=')(.*)(?=',)";
                        String cidRegEx = "(?<=',)(.*)(?=\\))";

                        String id = CommonUtil.fetchString(value, idRegEx);
                        String cid = CommonUtil.fetchString(value, cidRegEx);

                        //车辆信息明细
                        String url = CARINFODETAIL_URL + id + "&cid=" + cid;
                        Response resp = ConnectionUtil.doGetWith(url, COOKIE);
                        String content = resp.returnContent().asString();
                        Document doc = Jsoup.parseBodyFragment(content);

                        String vinRegEx = "#txtFrameNo";
                        String engineNumberRegEx = "#txtEngineNo";
                        String brandRegEx = "#brand";
                        String carModelRegEx = "#model";
                        String vcInsuranceValidDateRegEx = "#txtInTime";
                        String vcInsuranceCompanyRegEx = "#txtInCompany";

                        String vin = doc.select(vinRegEx).attr("value");
                        String engineNumber = doc.select(engineNumberRegEx).attr("value");
                        String brand = doc.select(brandRegEx).attr("value");
                        String carModel = doc.select(carModelRegEx).attr("value");
                        String vcInsuranceValidDate = doc.select(vcInsuranceValidDateRegEx).attr("value");
                        String vcInsuranceCompany = doc.select(vcInsuranceCompanyRegEx).attr("value");

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCompanyName(companyName);
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setBalance(balance);
                        carInfo.setVINcode(vin);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setBrand(brand);
                        carInfo.setCarModel(carModel);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setRemark(balance);//余额
                        carInfos.add(carInfo);

                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\惠车邦车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);

    }


    /**
     * 供应商
     * 打开路径:首页-库存-供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doGetWith(SUPPLIER_URL + 1, COOKIE);
        int totalPage = getTotalPage(response);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doGetWith(SUPPLIER_URL + i, COOKIE);

                String html = res.returnContent().asString();
                Document body = Jsoup.parseBodyFragment(html);

                String trRegEx = "body > div.layui-form > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(body, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {

                        String nameRegEx = "body > div.layui-form > table > tbody > tr:nth-child(" + j + ") > td:nth-child(2)";
                        String contactNameRegEx = "body > div.layui-form > table > tbody > tr:nth-child(" + j + ") > td:nth-child(3)";
                        String contactPhoneRegEx = "body > div.layui-form > table > tbody > tr:nth-child(" + j + ") > td:nth-child(4)";
                        String addressRegEx = "body > div.layui-form > table > tbody > tr:nth-child(" + j + ") > td:nth-child(5)";

                        String name = body.select(nameRegEx).text();
                        String contactName = body.select(contactNameRegEx).text();
                        String contactPhone = body.select(contactPhoneRegEx).text();
                        String address = body.select(addressRegEx).text();

                        Supplier supplier = new Supplier();
                        supplier.setCompanyName(companyName);
                        supplier.setName(name);
                        supplier.setContactName(contactName);
                        supplier.setContactPhone(contactPhone);
                        supplier.setAddress(address);
                        suppliers.add(supplier);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\惠车邦供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }

    private int getTotalPage(Response response) throws IOException {
        int totalPage = 0;

        String html = response.returnContent().asString();
        Document body = Jsoup.parseBodyFragment(html);

        String totalRegEx = "body > div.dataTables_paginate.paging_full_numbers > div > span > a";
        Elements elements = body.select(totalRegEx);

        if (elements.size() > 0) {
            String totalStr = elements.get(elements.size() - 1).text();
            totalPage = Integer.parseInt(totalStr);
        }

        return totalPage;
    }

}
