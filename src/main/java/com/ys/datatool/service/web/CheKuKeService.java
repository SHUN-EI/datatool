package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/6/21.
 * 车酷客系统
 */
public class CheKuKeService {

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

    private String getIdRegEx = "(?<=\\().*(?=\\))";

    private List<HtmlPage> pages = new ArrayList();

    private int count = 0;

    private int sign = 0;//会员卡对应的行数

    private Workbook workbook;

    @Test
    public void test() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

    }

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

        String editTD4XPath = "//*[@id=\"card_tab\"]/tbody/tr[{no}]/td[4]/input[2]";
        String editTD3XPath = "//*[@id=\"card_tab\"]/tbody/tr[{no}]/td[3]/input[2]";

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
                        String id = CommonUtil.fetchString(idStr, getIdRegEx);

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
                    } else {

                        //根据会员id获取会员卡卡内项目,跳转到会员卡编辑页面
                        String idStr = doc.select(StringUtils.replace(idTD3RegEx, "{no}", j + "")).attr("onclick");
                        String id = CommonUtil.fetchString(idStr, getIdRegEx);

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
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards,workbook,pathname);
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

        Document doc = Jsoup.parseBodyFragment(allMemberCardPage.asXml());
        String lastLabelRegEx = "(?<=\\<a href=).*(?= 尾页)";
        String lastRegEx = "(?<=,').*(?=')";
        String lastLabel = CommonUtil.fetchString(doc.toString(), lastLabelRegEx);
        String total = CommonUtil.fetchString(lastLabel, lastRegEx);
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

        String anchorXPath = "//*[@id=\"AspNetPager1\"]/a[13]";
        if (count > 10)
            anchorXPath = "//*[@id=\"AspNetPager1\"]/a[14]";
        if (count > 20)
            anchorXPath = "//*[@id=\"AspNetPager1\"]/a[6]";//change

        HtmlAnchor nextPage = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = nextPage.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num);//num为总页数
    }
}
