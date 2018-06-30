package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.domain.MemberCardItem;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    private String USERNAME = "s8161";

    private String PASSWORD = "4721198";

    private String STATESELECT = "ddl_state";

    private String trMemberCardRegEx = "#card_tab > tbody > tr";

    private String trCarInfoRegEx = "#form1 > table.list_tbl > tbody > tr";

    private String getmemberCardIdRegEx = "(?<=\\().*(?=\\))";

    private String getCarInfoIdRegEx = "(?<=,).*(?=\\))";

    private List<HtmlPage> pages = new ArrayList();

    private int count = 0;

    private int sign = 0;//会员卡对应的行数

    private int memberCardEnd = 20;//会员卡最后几页

    private int carEnd = 170;//车辆最后几页

    private Workbook workbook;

    /**
     * 车辆信息
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        WebClient webClient = WebClientUtil.getWebClient();
        getAllCarInfoPages(webClient);

        for (int i = 0; i < pages.size(); i++) {

            String idTD2RegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(2) > input:nth-child(1)";
            String idTD3RegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(3) > input:nth-child(1)";

            Document doc = Jsoup.parseBodyFragment(pages.get(i).asXml());
            int trSize = WebClientUtil.getTRSize(doc, trCarInfoRegEx);

            String tdCarInfoRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td";
            if (trSize > 0) {
                for (int j = 2; j <= trSize; j++) {
                    String nameRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String phoneRegEx = "#form1 > table.list_tbl > tbody > tr:nth-child({no}) > td:nth-child(1)";

                    String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                    String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", String.valueOf(j))).text();

                    //判断有多少个td
                    int tdSize = doc.select(StringUtils.replace(tdCarInfoRegEx, "{no}", String.valueOf(j))).tagName("td").size();
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

                        String brandRegEx = "#sel_brand > option[selected]";
                        String brandOutsideRegEx = "#txtBrand";//text

                        String carSeriesRegEx = "#sel_model > option[selected]";
                        String carSeriesOutsideRegEx = "#txtModel";//text

                        String carModelRegEx = "#sel_style > option[selected]";
                        String carModelOutsideRegEx = "#txtStyle";

                        String carNumberPro = document.select(carNumberProRegEx).text();
                        String carNumberLet = document.select(carNumberLetRegEx).text();
                        String carNumberValue = document.select(carNumberValueRegEx).attr("value");
                        String carNumber = carNumberPro + carNumberLet + carNumberValue;

                        String VINCode = document.select(VINCodeRegEx).attr("value");
                        String engineNumber = document.select(engineNumberRegEx).attr("value");
                        String vcInsuranceCompany = document.select(vcInsuranceCompanyRegEx).attr("value");
                        String vcInsuranceValidDate = document.select(vcInsuranceValidDateRegEx).attr("value");

                        String brand = document.select(brandRegEx).text();
                        String carModel = document.select(carModelRegEx).text();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setVINcode(VINCode);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setBrand(brand);
                        carInfo.setCarModel(carModel);
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

                        String brandRegEx = "#sel_brand > option[selected]";
                        String brandOutsideRegEx = "#txtBrand";//text

                        String carSeriesRegEx = "#sel_model > option[selected]";
                        String carSeriesOutsideRegEx = "#txtModel";//text

                        String carModelRegEx = "#sel_style > option[selected]";
                        String carModelOutsideRegEx = "#txtStyle";

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

                        String brand = document.select(brandRegEx).text();
                        String carModel = document.select(carModelRegEx).text();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setVINcode(VINCode);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                        carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                        carInfo.setBrand(brand);
                        carInfo.setCarModel(carModel);
                        carInfos.add(carInfo);
                    }
                }
            }
        }


        System.out.println("carInfos结果为" + carInfos.toString());
        System.out.println("carInfos大小为" + carInfos.size());

        String pathname = "C:\\exportExcel\\车酷客车辆信息.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);

    }

    /**
     * 卡内项目
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
            int trSize = WebClientUtil.getTRSize(doc, trMemberCardRegEx);

            if (trSize > 0) {
                for (int j = 2; j <= trSize; j++) {

                    //判断有多少个td
                    int tdSize = doc.select(StringUtils.replace(tdMemberCardRegEx, "{no}", j + "")).tagName("td").size();
                    if (tdSize == 4) {

                        String idStr = doc.select(StringUtils.replace(idTD4RegEx, "{no}", j + "")).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getmemberCardIdRegEx);

                        //会员卡详情页面
                        HtmlPage cardItemPage = webClient.getPage(StringUtils.replace(MEMBERCARDINFO_URL, "{id}", id));
                        Document document = Jsoup.parseBodyFragment(cardItemPage.asXml());

                        int trMemberCardInfoSize = WebClientUtil.getTRSize(document, trMemberCardInfoRegEx);
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

                        int trMemberCardInfoSize = WebClientUtil.getTRSize(document, trMemberCardInfoRegEx);
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
                                memberCardItem.setOriginalNum(num);
                                memberCardItems.add(memberCardItem);
                            }
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\卡内项目.xls";
        ExportUtil.exportMemberCardItemSomeFieldDataInLocal(memberCardItems, workbook, pathname);
    }


    /**
     * 会员卡
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
            int trSize = WebClientUtil.getTRSize(doc, trMemberCardRegEx);

            if (trSize > 0) {
                for (int j = 2; j <= trSize; j++) {

                    String cardCodeRegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String nameRegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String phoneRegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1)";
                    String carNumberRegEx = "#card_tab > tbody > tr:nth-child({no}) > td:nth-child(1)";

                    //判断有多少个td
                    int tdSize = doc.select(StringUtils.replace(tdMemberCardRegEx, "{no}", j + "")).tagName("td").size();
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

        String pathname = "C:\\exportExcel\\会员卡信息.xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, workbook, pathname);
    }

    private void getAllCarInfoPages(WebClient webClient) throws IOException {
        login(webClient);
        HtmlPage carInfoPage = webClient.getPage(CARINFO_URL);
        String total = getTotalPage(carInfoPage);

        pages.add(carInfoPage);
        nextPage(carInfoPage, Integer.parseInt(total), carEnd);//车辆信息总页数
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

        String total = getTotalPage(allMemberCardPage);
        pages.add(allMemberCardPage);
        nextPage(allMemberCardPage, Integer.parseInt(total), memberCardEnd);//会员卡总页数
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

    private String getTotalPage(HtmlPage htmlPage) {
        Document doc = Jsoup.parseBodyFragment(htmlPage.asXml());
        String lastLabelRegEx = "(?<=\\<a href=).*(?= 尾页)";
        String lastRegEx = "(?<=,').*(?=')";
        String lastLabel = CommonUtil.fetchString(doc.toString(), lastLabelRegEx);
        String total = CommonUtil.fetchString(lastLabel, lastRegEx);

        return total;
    }

    private void nextPage(HtmlPage page, int num, int end) throws IOException {
        ++count;
        if (count == num)
            return;

        String anchorXPath = "//*[@id=\"AspNetPager1\"]/a[13]";
        if (count > 10)
            anchorXPath = "//*[@id=\"AspNetPager1\"]/a[14]";

        //最后几页的下一页按钮
        if (count > end)
            anchorXPath = "//*[@id=\"AspNetPager1\"]/a[6]";

        HtmlAnchor nextPage = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = nextPage.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num, end);//num为总页数
    }
}
