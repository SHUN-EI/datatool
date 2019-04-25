package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018/6/21.
 * 车酷客系统
 */
@Service
public class CheKuKeService {

    private String CONSURECORDDETAIL_URL = "http://sa.chekuke.com/Shop/ShopProjectConsuRecordList.aspx?No=";

    private String CONSURECORD_URL = "http://sa.chekuke.com/StatisticalCenter/ShopUserConsuRecordList.aspx";

    private String MEMBER_URL = "http://sa.chekuke.com/MemberManage/MemberList.aspx";

    private String BILL_URL = "http://sa.chekuke.com/Shop/ShopProjectOrderList.aspx";

    private String CARINFODETAIL_URL = "http://sa.chekuke.com/MemberManage/ShopUserDriverEdit.aspx?t=1&&id=";

    private String CARINFO_URL = "http://sa.chekuke.com/MemberManage/ShopUserDriverList.aspx";

    private String MEMBERCARDINFO_URL = "http://sa.chekuke.com/MemberManage/ShopUserCardInfo.aspx?id={id}";

    private String MEMBERCARDITEM_URL = "http://sa.chekuke.com/MemberManage/ShopUserCardEdit.aspx?id={id}";

    private String MEMBERCARDCLIENT_URL = "http://sa.chekuke.com/MemberManage/ShopUserCardList.aspx";

    private String LOGIN_URL = "http://sa.chekuke.com/login.aspx?f=e";

    private String USERID = "txtLoginName";

    private String PWDID = "txtLoginPwd";

    private String VCODE = "txtVcode";

    private String VALICODEIMG = "img_code";

    private String BTNLOGIN = "btnLogin";

    private String STATESELECT = "ddl_state";

    private String trMemberCardRegEx = "#card_tab > tbody > tr";

    private String trCarInfoRegEx = "#form1 > table.list_tbl > tbody > tr";

    private String getmemberCardIdRegEx = "(?<=\\().*(?=\\))";

    private String getCarInfoIdRegEx = "(?<=,).*(?=\\))";

    private List<HtmlPage> pages = new ArrayList();

    private String nameStr = "";

    private String phoneStr = "";

    private String companyName = "车酷客";

    private int count = 0;

    //会员卡对应的行数
    private int sign = 0;

    ////////////////////////工具使用前，请先填写用户名、密码、Cookie等数据/////////////////////////////////////////////////////////////////////

    //用户名
    private String USERNAME = "S8121";
    //密码
    private String PASSWORD = "a150500.";

    private String COOKIE = "ASP.NET_SessionId=5vfsjcyug3lhywj2q1wvcn2e; LOGINKEY=3941dd19f97c4e75aae825648ce72f9e; LOGINNAME=S8121; Hm_lvt_104dd4c34f58725547e88d600d6c28ed=1547524379,1547617751; Hm_lpvt_104dd4c34f58725547e88d600d6c28ed=1547617751";


    /**
     * 历史消费记录和消费记录相关车辆
     * 网页-服务记录-总消费记录
     * 打开路径:服务记录-总消费记录
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();
        Set<String> billNoSet = new HashSet<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllPages(webClient, CONSURECORD_URL);

        String tableBillRegEx = "#form1 > table";
        for (HtmlPage page : pages) {
            Document doc = Jsoup.parseBodyFragment(page.asXml());
            String trBillRegEx = "table > tbody > tr:nth-child(1) > td[colspan='7']";

            Elements elements = doc.select(tableBillRegEx).attr("class", "list_tb1");
            for (Element element : elements) {

                String tdStr = element.select(trBillRegEx).text();
                String billNo = tdStr.replace("【项目消费】单号：", "");
                if (StringUtils.isNotBlank(billNo))
                    billNoSet.add(billNo);
            }
        }

        if (billNoSet.size() > 0) {
            for (String billNo : billNoSet) {
                String bno = billNo;

                System.out.println("正在获取的单号为" + bno);

                if ("128078".equals(bno) || "133316".equals(bno))
                    continue;

                Response response = ConnectionUtil.doGetWithLeastParams(CONSURECORDDETAIL_URL + bno, COOKIE);

                String html = response.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(html);

                Elements elements = doc.select(tableBillRegEx).attr("class", "list_tb1");
                Element e1 = elements.get(0);
                Element e2 = elements.get(1);

                String detail = e1.getElementsByTag("td").get(0).toString();
                Elements tbodys = e2.getElementsByTag("tbody");
                Elements trs = tbodys.get(0).children();

                String totalAmount = trs.get(trs.size() - 1).getElementsByTag("td").get(7).text();
                totalAmount = totalAmount.replace(" 元", "");

                String getBillNoRegEx = "(?<=单号：)(.*)(?=<br> 消费会员)";
                String no = CommonUtil.fetchString(detail, getBillNoRegEx);

                String getCarNumberRegEx = "(?<=消费车辆：)(.*)(?=<br> 接单人)";
                String carNumber = CommonUtil.fetchString(detail, getCarNumberRegEx);

                String getReceptionistNameRegEx = "(?<=接单人：)(.*)(?=<br> 进厂时间)";
                String receptionistName = CommonUtil.fetchString(detail, getReceptionistNameRegEx);

                String getDateEndRegEx = "(?<=进厂时间：)(.*)(?=<br> 进厂公里)";
                String dateEnd = CommonUtil.fetchString(detail, getDateEndRegEx);
                dateEnd = DateUtil.formatSQLDate(dateEnd);

                String getMileageRegEx = "(?<=进厂公里：)(.*)(?=<br> </td>)";
                String mileage = CommonUtil.fetchString(detail, getMileageRegEx);

                String serviceNames = "";
                String itemNames = "";
                if (trs.size() > 0) {
                    for (int i = 2; i < trs.size() - 2; i++) {
                        Element e = trs.get(i);

                        if (StringUtils.isBlank(e.text()))
                            continue;

                        if (e.toString().contains("table")) {

                            Elements itemTbodys = e.getElementsByTag("tbody");
                            Elements itemTrs = itemTbodys.get(0).children();
                            int itemTrsSize = itemTrs.size();
                            if (itemTrsSize > 0) {
                                for (int j = 3; j < itemTrsSize; j++) {
                                    Element data = itemTrs.get(j);
                                    String num = data.getElementsByTag("td").get(2).text();
                                    String totalPrice = data.getElementsByTag("td").get(4).text();//总价
                                    String price = data.getElementsByTag("td").get(1).text();//原价

                                    String goodsNames = data.getElementsByTag("td").get(0).text();
                                    goodsNames = goodsNames + "*" + num + "(" + price + ")";

                                    if (!"".equals(itemNames))
                                        itemNames = itemNames + "," + goodsNames;

                                    if ("".equals(itemNames))
                                        itemNames = goodsNames;
                                }
                            }
                        } else {
                            String serviceItemNames = e.getElementsByTag("td").get(0).text();
                            String price = e.getElementsByTag("td").get(7).text();
                            price = price.replace(" 元", "");
                            serviceItemNames = serviceItemNames + "(" + price + ")";

                            if (!"".equals(serviceNames))
                                serviceNames = serviceNames + "," + serviceItemNames;

                            if ("".equals(serviceNames))
                                serviceNames = serviceItemNames;

                        }
                    }
                }

                Bill bill = new Bill();
                bill.setCompanyName(companyName);
                bill.setBillNo(no);
                bill.setCarNumber(carNumber);
                bill.setReceptionistName(receptionistName);
                bill.setDateEnd(dateEnd);
                bill.setMileage(mileage);
                bill.setServiceItemNames(serviceNames);
                bill.setGoodsNames(itemNames);
                bill.setTotalAmount(totalAmount);
                bills.add(bill);
            }
        }

        String pathname = "C:\\exportExcel\\车酷客消费记录.xls";
        ExportUtil.exportConsumptionRecordDataToExcel03InLocal(bills, ExcelDatas.workbook, pathname);
    }


    /**
     * 车辆信息
     * 网页-车主列表(首页-点击总车主)
     * 打开路径:首页-点击总车主-车主列表-车辆信息详情
     *
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataFormMember() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Set<String> cardNoSet = new HashSet<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllPages(webClient, MEMBER_URL);

        for (HtmlPage page : pages) {
            Document doc = Jsoup.parseBodyFragment(page.asXml());
            int trSize = WebClientUtil.getTagSize(doc, trCarInfoRegEx, HtmlTag.trName);

            if (trSize > 0) {
                for (int i = 2; i <= trSize; i++) {
                    String cardNoRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(3)";
                    String cardNo = doc.select(StringUtils.replace(cardNoRegEx, "{no}", String.valueOf(i))).text();
                    cardNoSet.add(cardNo.substring(0, 6));
                }
            }
        }

        //把获得的会员卡号遍历,到会员车辆列表中查询获取车辆详情
        for (String cardNo : cardNoSet) {
            HtmlPage carInfoPage = webClient.getPage(CARINFO_URL);
            HtmlInput cardNoInput = (HtmlInput) carInfoPage.getElementById("txt_number");
            cardNoInput.setValueAttribute(cardNo);

            HtmlInput query = (HtmlInput) carInfoPage.getElementById("btnQuery");
            HtmlPage carPage = query.click();

            String idTD2RegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(2) > input:nth-child(1)";
            String idTD3RegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(3) > input:nth-child(1)";

            Document doc = Jsoup.parseBodyFragment(carPage.asXml());
            int trSize = WebClientUtil.getTagSize(doc, trCarInfoRegEx, HtmlTag.trName);
            if (trSize > 0) {
                for (int j = 2; j <= trSize; j++) {
                    String nameRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();

                    String tdCarInfoRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td";
                    //判断有多少个td
                    int tdSize = WebClientUtil.getTagSize(doc, StringUtils.replace(tdCarInfoRegEx, "{no}", String.valueOf(j)), HtmlTag.tdName);

                    if (tdSize == 3) {
                        String idStr = doc.select(StringUtils.replace(idTD3RegEx, "{no}", String.valueOf(j))).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getCarInfoIdRegEx);

                        //车辆修改页面
                        HtmlPage cardDetailPage = webClient.getPage(CARINFODETAIL_URL + id);
                        Document document = Jsoup.parseBodyFragment(cardDetailPage.asXml());

                        String carNumberProRegEx = "#selPro > option[selected]";
                        String carNumberLetRegEx = "#selLet > option[selected]";
                        String carNumberValueRegEx = "#tb_number";//value

                        String VINCodeRegEx = "#tb_fdj";
                        String engineNumberRegEx = "#tb_cjh";
                        String vcInsuranceCompanyRegEx = "#txtBXGS";
                        String vcInsuranceValidDateRegEx = "#txtBXTime";

                        String brandSelectRegEx = "#sel_brand > option[selected]";
                        String brandInputRegEx = "#txtBrand";

                        String carSeriesSelectRegEx = "#sel_model > option[selected]";
                        String carSeriesInputRegEx = "#txtModel";

                        String carModelSelectRegEx = "#sel_style > option[selected]";
                        String carModelInputRegEx = "#txtStyle";

                        String carNumberPro = document.select(carNumberProRegEx).text();
                        String carNumberLet = document.select(carNumberLetRegEx).text();
                        String carNumberValue = document.select(carNumberValueRegEx).attr("value");
                        String carNumber = carNumberPro + carNumberLet + carNumberValue;

                        String VINCode = document.select(VINCodeRegEx).attr("value");
                        String engineNumber = document.select(engineNumberRegEx).attr("value");
                        String vcInsuranceCompany = document.select(vcInsuranceCompanyRegEx).attr("value");
                        String vcInsuranceValidDate = document.select(vcInsuranceValidDateRegEx).attr("value");

                        String brandSelect = document.select(brandSelectRegEx).text();
                        String brandInput = document.select(brandInputRegEx).attr("value");

                        String carSeriesSelect = document.select(carSeriesSelectRegEx).text();
                        String carSeriesInput = document.select(carSeriesInputRegEx).attr("value");

                        String carModelSelect = document.select(carModelSelectRegEx).text();
                        String carModelInput = document.select(carModelInputRegEx).attr("value");

                        nameStr = name;
                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setCompanyName(companyName);
                        carInfo.setName(name);
                        carInfo.setPhone(name);
                        carInfo.setVINcode(VINCode);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setBrand(brandSelect);
                        carInfo.setCarModel(carModelSelect);
                        carInfo.setBrandSelect(brandSelect);
                        carInfo.setBrandInput(brandInput);
                        carInfo.setCarSeriesSelect(carSeriesSelect);
                        carInfo.setCarSeriesInput(carSeriesInput);
                        carInfo.setCarModelSelect(carModelSelect);
                        carInfo.setCarModelInput(carModelInput);
                        carInfos.add(carInfo);
                    }

                    if (tdSize == 2) {
                        String idStr = doc.select(StringUtils.replace(idTD2RegEx, "{no}", String.valueOf(j))).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getCarInfoIdRegEx);

                        //车辆修改页面
                        HtmlPage cardDetailPage = webClient.getPage(CARINFODETAIL_URL + id);
                        Document document = Jsoup.parseBodyFragment(cardDetailPage.asXml());

                        String carNumberProRegEx = "#selPro > option[selected]";
                        String carNumberLetRegEx = "#selLet > option[selected]";
                        String carNumberValueRegEx = "#tb_number";//value

                        String brandSelectRegEx = "#sel_brand > option[selected]";
                        String brandInputRegEx = "#txtBrand";

                        String carSeriesSelectRegEx = "#sel_model > option[selected]";
                        String carSeriesInputRegEx = "#txtModel";

                        String carModelSelectRegEx = "#sel_style > option[selected]";
                        String carModelInputRegEx = "#txtStyle";

                        String VINCodeRegEx = "#tb_fdj";
                        String engineNumberRegEx = "#tb_cjh";
                        String vcInsuranceCompanyRegEx = "#txtBXGS";
                        String vcInsuranceValidDateRegEx = "#txtBXTime";

                        String carNumberPro = document.select(carNumberProRegEx).text();
                        String carNumberLet = document.select(carNumberLetRegEx).text();
                        String carNumberValue = document.select(carNumberValueRegEx).attr("value");
                        String carNumber = carNumberPro + carNumberLet + carNumberValue;

                        String VINCode = document.select(VINCodeRegEx).attr("value");
                        String engineNumber = document.select(engineNumberRegEx).attr("value");
                        String vcInsuranceCompany = document.select(vcInsuranceCompanyRegEx).attr("value");
                        String vcInsuranceValidDate = document.select(vcInsuranceValidDateRegEx).attr("value");

                        String brandSelect = document.select(brandSelectRegEx).text();
                        String brandInput = document.select(brandInputRegEx).attr("value");

                        String carSeriesSelect = document.select(carSeriesSelectRegEx).text();
                        String carSeriesInput = document.select(carSeriesInputRegEx).attr("value");

                        String carModelSelect = document.select(carModelSelectRegEx).text();
                        String carModelInput = document.select(carModelInputRegEx).attr("value");

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setCompanyName(companyName);
                        carInfo.setPhone(nameStr);
                        carInfo.setName(nameStr);
                        carInfo.setVINcode(VINCode);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setBrand(brandSelect);
                        carInfo.setCarModel(carModelSelect);
                        carInfo.setBrandSelect(brandSelect);
                        carInfo.setBrandInput(brandInput);
                        carInfo.setCarSeriesSelect(carSeriesSelect);
                        carInfo.setCarSeriesInput(carSeriesInput);
                        carInfo.setCarModelSelect(carModelSelect);
                        carInfo.setCarModelInput(carModelInput);
                        carInfos.add(carInfo);
                    }
                }
            }
        }
        System.out.println("carInfos为" + carInfos.toString());
        System.out.println("carInfos为" + carInfos.size());

        String pathname = "C:\\exportExcel\\车酷客车主列表车辆信息.xlsx";
        ExportUtil.exportCheKuKeCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    /**
     * 车辆信息
     * 网页-店铺管理-开单管理-车辆信息详情
     * 打开路径:店铺管理-开单管理-车辆信息详情
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataFormBill() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Set<String> carNumberSet = new HashSet<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllPages(webClient, BILL_URL);

        for (int i = 0; i < pages.size(); i++) {
            Document doc = Jsoup.parseBodyFragment(pages.get(i).asXml());
            int trSize = WebClientUtil.getTagSize(doc, trCarInfoRegEx, HtmlTag.trName);

            if (trSize > 0) {
                for (int j = 2; j <= trSize; j++) {

                    String carNumberRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(7)";
                    String carNumber = doc.select(StringUtils.replace(carNumberRegEx, "{no}", String.valueOf(j))).text();
                    carNumberSet.add(carNumber);
                }
            }
        }

        //把获得的会员卡号遍历,到会员车辆列表中查询获取车辆详情
        for (String carNumber : carNumberSet) {
            HtmlPage carInfoPage = webClient.getPage(CARINFO_URL);
            HtmlInput carNumberInput = (HtmlInput) carInfoPage.getElementById("txt_number");
            carNumberInput.setValueAttribute(carNumber);

            HtmlInput query = (HtmlInput) carInfoPage.getElementById("btnQuery");
            HtmlPage carPage = query.click();

            Document doc = Jsoup.parseBodyFragment(carPage.asXml());
            String nameRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child(2) > td:nth-child(1)";
            String name = doc.select(nameRegEx).text();

            String idRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child(2) > td:nth-child(3) > input:nth-child(1)";
            String idStr = doc.select(idRegEx).attr("onclick");
            String id = CommonUtil.fetchString(idStr, getCarInfoIdRegEx);

            //车辆修改页面
            HtmlPage cardDetailPage = webClient.getPage(CARINFODETAIL_URL + id);
            Document document = Jsoup.parseBodyFragment(cardDetailPage.asXml());

            String carNumberProRegEx = "#selPro > option[selected]";
            String carNumberLetRegEx = "#selLet > option[selected]";
            String carNumberValueRegEx = "#tb_number";//value

            String engineNumberRegEx = "#tb_cjh";
            String VINCodeRegEx = "#tb_fdj";
            String vcInsuranceCompanyRegEx = "#txtBXGS";
            String vcInsuranceValidDateRegEx = "#txtBXTime";

            String brandSelectRegEx = "#sel_brand > option[selected]";
            String brandInputRegEx = "#txtBrand";

            String carSeriesSelectRegEx = "#sel_model > option[selected]";
            String carSeriesInputRegEx = "#txtModel";

            String carModelSelectRegEx = "#sel_style > option[selected]";
            String carModelInputRegEx = "#txtStyle";

            String carNumberPro = document.select(carNumberProRegEx).text();
            String carNumberLet = document.select(carNumberLetRegEx).text();
            String carNumberValue = document.select(carNumberValueRegEx).attr("value");
            String carNo = carNumberPro + carNumberLet + carNumberValue;

            String engineNumber = document.select(engineNumberRegEx).attr("value");
            String VINCode = document.select(VINCodeRegEx).attr("value");
            String vcInsuranceCompany = document.select(vcInsuranceCompanyRegEx).attr("value");
            String vcInsuranceValidDate = document.select(vcInsuranceValidDateRegEx).attr("value");

            String brandSelect = document.select(brandSelectRegEx).text();
            String brandInput = document.select(brandInputRegEx).attr("value");

            String carSeriesSelect = document.select(carSeriesSelectRegEx).text();
            String carSeriesInput = document.select(carSeriesInputRegEx).attr("value");

            String carModelSelect = document.select(carModelSelectRegEx).text();
            String carModelInput = document.select(carModelInputRegEx).attr("value");


            CarInfo carInfo = new CarInfo();
            carInfo.setCarNumber(carNo);
            carInfo.setCompanyName(companyName);
            carInfo.setName(name);
            carInfo.setPhone(name);
            carInfo.setVINcode(VINCode);
            carInfo.setEngineNumber(engineNumber);
            carInfo.setVcInsuranceCompany(vcInsuranceCompany);
            carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
            carInfo.setBrand(brandSelect);
            carInfo.setCarModel(carModelSelect);
            carInfo.setBrandSelect(brandSelect);
            carInfo.setBrandInput(brandInput);
            carInfo.setCarSeriesSelect(carSeriesSelect);
            carInfo.setCarSeriesInput(carSeriesInput);
            carInfo.setCarModelSelect(carModelSelect);
            carInfo.setCarModelInput(carModelInput);
            carInfos.add(carInfo);
        }

        System.out.println("carInfos为" + carInfos.toString());
        System.out.println("carInfos为" + carInfos.size());
        System.out.println("carNumberSet为" + carNumberSet.size());

        String pathname = "C:\\exportExcel\\车酷客单据车辆信息.xlsx";
        ExportUtil.exportCheKuKeCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    /**
     * 车辆信息
     * 网页-店铺管理-会员车辆列表
     * <p>
     * 车酷客系统后台，每页返回的数据都不一样
     *
     * @throws IOException
     */
    @Deprecated
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllPages(webClient, CARINFO_URL);

        for (int i = 0; i < pages.size(); i++) {

            String idTD2RegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(2) > input:nth-child(1)";
            String idTD3RegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(3) > input:nth-child(1)";

            Document doc = Jsoup.parseBodyFragment(pages.get(i).asXml());
            int trSize = WebClientUtil.getTagSize(doc, trCarInfoRegEx, HtmlTag.trName);
            if (trSize > 0) {
                for (int j = 2; j <= trSize; j++) {
                    String nameRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String phoneRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(1)";

                    String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                    String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", String.valueOf(j))).text();

                    String tdCarInfoRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td";
                    //判断有多少个td
                    int tdSize = WebClientUtil.getTagSize(doc, StringUtils.replace(tdCarInfoRegEx, "{no}", String.valueOf(j)), HtmlTag.tdName);
                    if (tdSize == 3) {
                        String idStr = doc.select(StringUtils.replace(idTD3RegEx, "{no}", String.valueOf(j))).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getCarInfoIdRegEx);

                        //车辆修改页面
                        HtmlPage cardDetailPage = webClient.getPage(CARINFODETAIL_URL + id);
                        Document document = Jsoup.parseBodyFragment(cardDetailPage.asXml());

                        String carNumberProRegEx = "#selPro > option[selected]";
                        String carNumberLetRegEx = "#selLet > option[selected]";
                        String carNumberValueRegEx = "#tb_number";//value

                        String VINCodeRegEx = "#tb_fdj";
                        String engineNumberRegEx = "#tb_cjh";
                        String vcInsuranceCompanyRegEx = "#txtBXGS";
                        String vcInsuranceValidDateRegEx = "#txtBXTime";

                        String brandSelectRegEx = "#sel_brand > option[selected]";
                        String brandInputRegEx = "#txtBrand";

                        String carSeriesSelectRegEx = "#sel_model > option[selected]";
                        String carSeriesInputRegEx = "#txtModel";

                        String carModelSelectRegEx = "#sel_style > option[selected]";
                        String carModelInputRegEx = "#txtStyle";

                        String carNumberPro = document.select(carNumberProRegEx).text();
                        String carNumberLet = document.select(carNumberLetRegEx).text();
                        String carNumberValue = document.select(carNumberValueRegEx).attr("value");
                        String carNumber = carNumberPro + carNumberLet + carNumberValue;

                        String VINCode = document.select(VINCodeRegEx).attr("value");
                        String engineNumber = document.select(engineNumberRegEx).attr("value");
                        String vcInsuranceCompany = document.select(vcInsuranceCompanyRegEx).attr("value");
                        String vcInsuranceValidDate = document.select(vcInsuranceValidDateRegEx).attr("value");

                        String brandSelect = document.select(brandSelectRegEx).text();
                        String brandInput = document.select(brandInputRegEx).attr("value");

                        String carSeriesSelect = document.select(carSeriesSelectRegEx).text();
                        String carSeriesInput = document.select(carSeriesInputRegEx).attr("value");

                        String carModelSelect = document.select(carModelSelectRegEx).text();
                        String carModelInput = document.select(carModelInputRegEx).attr("value");


                        nameStr = name;
                        phoneStr = phone;

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setVINcode(VINCode);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setBrand(brandSelect);
                        carInfo.setCarModel(carModelSelect);
                        carInfo.setBrandSelect(brandSelect);
                        carInfo.setBrandInput(brandInput);
                        carInfo.setCarSeriesSelect(carSeriesSelect);
                        carInfo.setCarSeriesInput(carSeriesInput);
                        carInfo.setCarModelSelect(carModelSelect);
                        carInfo.setCarModelInput(carModelInput);
                        carInfos.add(carInfo);
                    }

                    if (tdSize == 2) {
                        String idStr = doc.select(StringUtils.replace(idTD2RegEx, "{no}", String.valueOf(j))).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getCarInfoIdRegEx);

                        //车辆修改页面
                        HtmlPage cardDetailPage = webClient.getPage(CARINFODETAIL_URL + id);
                        Document document = Jsoup.parseBodyFragment(cardDetailPage.asXml());

                        String carNumberProRegEx = "#selPro > option[selected]";
                        String carNumberLetRegEx = "#selLet > option[selected]";
                        String carNumberValueRegEx = "#tb_number";//value

                        String brandSelectRegEx = "#sel_brand > option[selected]";
                        String brandInputRegEx = "#txtBrand";

                        String carSeriesSelectRegEx = "#sel_model > option[selected]";
                        String carSeriesInputRegEx = "#txtModel";

                        String carModelSelectRegEx = "#sel_style > option[selected]";
                        String carModelInputRegEx = "#txtStyle";

                        String VINCodeRegEx = "#tb_fdj";
                        String engineNumberRegEx = "#tb_cjh";
                        String vcInsuranceCompanyRegEx = "#txtBXGS";
                        String vcInsuranceValidDateRegEx = "#txtBXTime";

                        String carNumberPro = document.select(carNumberProRegEx).text();
                        String carNumberLet = document.select(carNumberLetRegEx).text();
                        String carNumberValue = document.select(carNumberValueRegEx).attr("value");
                        String carNumber = carNumberPro + carNumberLet + carNumberValue;

                        String VINCode = document.select(VINCodeRegEx).attr("value");
                        String engineNumber = document.select(engineNumberRegEx).attr("value");
                        String vcInsuranceCompany = document.select(vcInsuranceCompanyRegEx).attr("value");
                        String vcInsuranceValidDate = document.select(vcInsuranceValidDateRegEx).attr("value");

                        String brandSelect = document.select(brandSelectRegEx).text();
                        String brandInput = document.select(brandInputRegEx).attr("value");

                        String carSeriesSelect = document.select(carSeriesSelectRegEx).text();
                        String carSeriesInput = document.select(carSeriesInputRegEx).attr("value");

                        String carModelSelect = document.select(carModelSelectRegEx).text();
                        String carModelInput = document.select(carModelInputRegEx).attr("value");


                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setName(nameStr);
                        carInfo.setPhone(phoneStr);
                        carInfo.setVINcode(VINCode);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setBrand(brandSelect);
                        carInfo.setCarModel(carModelSelect);
                        carInfo.setBrandSelect(brandSelect);
                        carInfo.setBrandInput(brandInput);
                        carInfo.setCarSeriesSelect(carSeriesSelect);
                        carInfo.setCarSeriesInput(carSeriesInput);
                        carInfo.setCarModelSelect(carModelSelect);
                        carInfo.setCarModelInput(carModelInput);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        System.out.println("carInfos结果为" + carInfos.toString());
        System.out.println("carInfos大小为" + carInfos.size());

        String pathname = "C:\\exportExcel\\车酷客车辆信息.xlsx";
        ExportUtil.exportCheKuKeCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);

    }

    /**
     * 卡内项目
     * 网页-店铺管理-会员套餐列表
     * <p>
     * 打开路径:店铺管理-会员套餐列表
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        WebClient webClient = WebClientUtil.getWebClient();
        getAllMemberCardPages(webClient);

        String trMemberCardInfoRegEx = "#form1 > table > tbody > tr:nth-child(3) > td > table > tbody > tr";
        String idTD4RegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(4) > input:nth-child(1)";
        String idTD3RegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(3) > input:nth-child(1)";
        String tdMemberCardRegEx = "#card_tab > tbody > tr:nth-child({no}) > td";

        for (int i = 0; i < pages.size(); i++) {

            HtmlPage cardPage = pages.get(i);
            Document doc = Jsoup.parseBodyFragment(cardPage.asXml());
            int trSize = WebClientUtil.getTagSize(doc, trMemberCardRegEx, HtmlTag.trName);

            if (trSize > 0) {
                for (int j = 2; j <= trSize; j++) {

                    //判断有多少个td
                    int tdSize = WebClientUtil.getTagSize(doc, StringUtils.replace(tdMemberCardRegEx, "{no}", String.valueOf(j)), HtmlTag.tdName);
                    if (tdSize == 4) {

                        String idStr = doc.select(StringUtils.replace(idTD4RegEx, "{no}", j + "")).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getmemberCardIdRegEx);

                        //会员卡详情页面
                        HtmlPage cardItemPage = webClient.getPage(StringUtils.replace(MEMBERCARDINFO_URL, "{id}", id));
                        Document document = Jsoup.parseBodyFragment(cardItemPage.asXml());

                        int trMemberCardInfoSize = WebClientUtil.getTagSize(document, trMemberCardInfoRegEx, HtmlTag.trName);
                        if (trMemberCardInfoSize > 0) {
                            for (int k = 2; k <= trMemberCardInfoSize; k++) {

                                String itemNameRegEx = "#form1 > table > tbody > tr:nth-child(3) > td > table > tbody > tr:nth-child({no}) > td:nth-child(1)";
                                String itemName = document.select(StringUtils.replace(itemNameRegEx, "{no}", k + "")).text();

                                String numRegEx = "#form1 > table > tbody > tr:nth-child(3) > td > table > tbody > tr:nth-child({no}) > td:nth-child(2)";
                                String numStr = document.select(StringUtils.replace(numRegEx, "{no}", k + "")).text();
                                String num = numStr.replaceAll("次", "");

                                MemberCardItem memberCardItem = new MemberCardItem();
                                memberCardItem.setMemberCardItemId(id);
                                memberCardItem.setItemName(itemName);
                                memberCardItem.setCompanyName(companyName);
                                memberCardItem.setNum(num);
                                memberCardItem.setOriginalNum(num);
                                memberCardItems.add(memberCardItem);
                            }
                        }
                    }

                    if (tdSize == 3) {
                        String idStr = doc.select(StringUtils.replace(idTD3RegEx, "{no}", j + "")).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getmemberCardIdRegEx);

                        HtmlPage cardItemPage = webClient.getPage(StringUtils.replace(MEMBERCARDINFO_URL, "{id}", id));
                        Document document = Jsoup.parseBodyFragment(cardItemPage.asXml());

                        int trMemberCardInfoSize = WebClientUtil.getTagSize(document, trMemberCardInfoRegEx, HtmlTag.trName);
                        if (trMemberCardInfoSize > 0) {
                            for (int k = 2; k <= trMemberCardInfoSize; k++) {

                                String itemNameRegEx = "#form1 > table > tbody > tr:nth-child(3) > td > table > tbody > tr:nth-child({no}) > td:nth-child(1)";
                                String itemName = document.select(StringUtils.replace(itemNameRegEx, "{no}", k + "")).text();

                                String numRegEx = "#form1 > table > tbody > tr:nth-child(3) > td > table > tbody > tr:nth-child({no}) > td:nth-child(2)";
                                String numStr = document.select(StringUtils.replace(numRegEx, "{no}", k + "")).text();
                                String num = numStr.replaceAll("次", "");

                                MemberCardItem memberCardItem = new MemberCardItem();
                                memberCardItem.setItemName(itemName);
                                memberCardItem.setMemberCardItemId(id);
                                memberCardItem.setNum(num);
                                memberCardItem.setCompanyName(companyName);
                                memberCardItem.setOriginalNum(num);
                                memberCardItems.add(memberCardItem);
                            }
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\卡内项目.xls";
        ExportUtil.exportMemberCardItemSomeFieldDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
    }


    /**
     * 会员卡
     * 网页-店铺管理-会员卡片列表
     * 打开路径:店铺管理-会员卡片列表
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        WebClient webClient = WebClientUtil.getWebClient();
        getAllMemberCardPages(webClient);

        String tdMemberCardRegEx = "#card_tab > tbody > tr:nth-child({no}) > td";
        String idTD4RegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(4) > input:nth-child(1)";
        String idTD3RegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(3) > input:nth-child(1)";

        String cardNameTD4RegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(2) > div";
        String cardNameTD3RegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1) > div";

        String dateCreatedTD4RegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(3)";
        String dateCreatedTD3RegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(2)";

        for (int i = 0; i < pages.size(); i++) {
            HtmlPage cardPage = pages.get(i);
            Document doc = Jsoup.parseBodyFragment(cardPage.asXml());
            int trSize = WebClientUtil.getTagSize(doc, trMemberCardRegEx, HtmlTag.trName);

            if (trSize > 0) {
                for (int j = 2; j <= trSize; j++) {

                    String cardCodeRegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String nameRegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String phoneRegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String carNumberRegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1)";

                    //判断有多少个td
                    int tdSize = WebClientUtil.getTagSize(doc, StringUtils.replace(tdMemberCardRegEx, "{no}", String.valueOf(j)), HtmlTag.tdName);
                    if (tdSize == 4) {
                        sign = j;

                        //根据会员id获取会员卡卡内项目,跳转到会员卡编辑页面
                        String idStr = doc.select(StringUtils.replace(idTD4RegEx, "{no}", j + "")).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getmemberCardIdRegEx);

                        HtmlPage cardItemPage = webClient.getPage(StringUtils.replace(MEMBERCARDITEM_URL, "{id}", id));
                        Document document = Jsoup.parseBodyFragment(cardItemPage.asXml());

                        String validTimeRegEx = "#tb_date";
                        String validTime = document.select(validTimeRegEx).attr("value");

                        String dateCreated = doc.select(StringUtils.replace(dateCreatedTD4RegEx, "{no}", j + "")).text();
                        String cardName = doc.select(StringUtils.replace(cardNameTD4RegEx, "{no}", j + "")).text();
                        String cardCode = doc.select(StringUtils.replace(cardCodeRegEx, "{no}", j + "")).text();
                        String name = doc.select(StringUtils.replace(nameRegEx, "{no}", j + "")).text();
                        String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", j + "")).text();
                        String carNumber = doc.select(StringUtils.replace(carNumberRegEx, "{no}", j + "")).text();

                        MemberCard memberCard = new MemberCard();
                        memberCard.setMemberCardId(id);
                        memberCard.setCompanyName(companyName);
                        memberCard.setName(name);
                        memberCard.setPhone(phone);
                        memberCard.setCarNumber(carNumber);
                        memberCard.setCardCode(cardCode);
                        memberCard.setMemberCardName(cardName);
                        memberCard.setDateCreated(dateCreated);
                        memberCard.setValidTime(validTime);
                        memberCards.add(memberCard);

                     /* 跳转到会员卡编辑页面
                        HtmlInput memberCardDetail = cardPage.getFirstByXPath(StringUtils.replace(editTD4XPath, "{no}", j + ""));
                        HtmlPage memberCardDetailPage = memberCardDetail.click();*/
                    }

                    if (tdSize == 3) {

                        //根据会员id获取会员卡卡内项目,跳转到会员卡编辑页面
                        String idStr = doc.select(StringUtils.replace(idTD3RegEx, "{no}", j + "")).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getmemberCardIdRegEx);

                        HtmlPage cardItemPage = webClient.getPage(StringUtils.replace(MEMBERCARDITEM_URL, "{id}", id));
                        Document document = Jsoup.parseBodyFragment(cardItemPage.asXml());

                        String validTimeRegEx = "#tb_date";
                        String validTime = document.select(validTimeRegEx).attr("value");

                        String dateCreated = doc.select(StringUtils.replace(dateCreatedTD3RegEx, "{no}", j + "")).text();
                        String cardName = doc.select(StringUtils.replace(cardNameTD3RegEx, "{no}", j + "")).text();
                        String cardCode = doc.select(StringUtils.replace(cardCodeRegEx, "{no}", sign + "")).text();
                        String name = doc.select(StringUtils.replace(nameRegEx, "{no}", sign + "")).text();
                        String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", sign + "")).text();
                        String carNumber = doc.select(StringUtils.replace(carNumberRegEx, "{no}", sign + "")).text();

                        MemberCard memberCard = new MemberCard();
                        memberCard.setMemberCardId(id);
                        memberCard.setCompanyName(companyName);
                        memberCard.setName(name);
                        memberCard.setPhone(phone);
                        memberCard.setCarNumber(carNumber);
                        memberCard.setCardCode(cardCode);
                        memberCard.setMemberCardName(cardName);
                        memberCard.setDateCreated(dateCreated);
                        memberCard.setValidTime(validTime);
                        memberCards.add(memberCard);
                    }
                }
            }
        }

        System.out.println("会员卡分别为" + memberCards.toString());
        String pathname = "C:\\exportExcel\\会员卡信息.xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    private void getAllPages(WebClient webClient, String url) throws IOException {
        login(webClient);

        HtmlPage page = webClient.getPage(url);
        pages.add(page);

        String total = WebClientUtil.getTotalPage(page);

        //根据总页数开始递归（Integer.parseInt(total)）
        nextPage(page, Integer.parseInt(total));
    }

    private void getAllMemberCardPages(WebClient webClient) throws IOException {
        login(webClient);

        HtmlPage memberCardPage = webClient.getPage(MEMBERCARDCLIENT_URL);

        //下拉选择状态-请选择 value=0,获取所有状态的会员卡数据
        HtmlSelect select = (HtmlSelect) memberCardPage.getElementById(STATESELECT);
        HtmlOption option = select.getOptionByValue("0");
        option.setSelected(true);

        //首页
        String queryXPath = "//*[@id=\"btnQuery\"]";
        HtmlInput allMemberCard = memberCardPage.getFirstByXPath(queryXPath);
        HtmlPage allMemberCardPage = allMemberCard.click();

        String total = WebClientUtil.getTotalPage(allMemberCardPage);
        pages.add(allMemberCardPage);
        nextPage(allMemberCardPage, Integer.parseInt(total));//会员卡总页数
    }

    private void login(WebClient webClient) throws IOException {

        HtmlPage loginPage = webClient.getPage(LOGIN_URL);
        HtmlInput user = loginPage.getHtmlElementById(USERID);
        HtmlInput pwd = loginPage.getHtmlElementById(PWDID);
        HtmlInput vcode = loginPage.getHtmlElementById(VCODE);
        HtmlInput btnLogin = loginPage.getHtmlElementById(BTNLOGIN);//登录按钮

        HtmlImage valiCodeImg = (HtmlImage) loginPage.getElementById(VALICODEIMG);//验证码图片
        ImageReader imageReader = valiCodeImg.getImageReader();
        BufferedImage bufferedImage = imageReader.read(0);

        JFrame frame = new JFrame();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(bufferedImage));
        frame.getContentPane().add(label);
        frame.setSize(800, 800);
        frame.setTitle("网页验证码");
        frame.setVisible(true);

        String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");
        frame.setVisible(false);

        user.setAttribute("value", USERNAME);
        pwd.setAttribute("value", PASSWORD);
        vcode.click();
        vcode.type(valicodeStr);

        //登录操作
        btnLogin.click();
    }

    private void nextPage(HtmlPage page, int num) throws IOException {
        ++count;
        if (count == num)
            return;

        int value = 0;
        int end = 0;
        if (num < 10) {
            end = num;
            value = num - end;
        } else {
            end = num / 10 * 10;
            value = num - end;
        }

        String anchorXPath = "";
        if (end < 10) {
            anchorXPath = "//*[@id=\"AspNetPager1\"]/a[" + (end + 2) + "]";
        } else {
            anchorXPath = "//*[@id=\"AspNetPager1\"]/a[13]";
        }

        //超过10页
        if (count > 10 && count <= end)
            anchorXPath = "//*[@id=\"AspNetPager1\"]/a[14]";

        //最后几页的下一页按钮
        if (count > end && count <= num)
            anchorXPath = "//*[@id=\"AspNetPager1\"]/a[" + (value + 3) + "]";

        HtmlAnchor nextPage = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = nextPage.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num);//num为总页数
    }

}
