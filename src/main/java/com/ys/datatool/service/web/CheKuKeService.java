package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.poi.ss.usermodel.Workbook;

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

    private String MEMBERCARDCLIENT_URL = "http://s a.chekuke.com/MemberManage/ShopUserCardList.aspx";

    private String LOGIN_URL = "http://sa.chekuke.com/login.aspx?f=e";

    private String USERID = "txtLoginName";

    private String PWDID = "txtLoginPwd";

    private String VCODE = "txtVcode";

    private String VALICODEIMG = "img_code";

    private String BTNLOGIN = "btnLogin";

    private String USERNAME = "s8161";

    private String PASSWORD = "4721198";

    private String STATESELECT = "ddl_state";

    private List<HtmlPage> pages = new ArrayList();

    private Workbook workbook;

    private void getMemberCardPages(WebClient webClient) throws IOException {

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
        HtmlPage memberCardPage = webClient.getPage(MEMBERCARDCLIENT_URL);

        //下拉选择状态-请选择 value=0,获取所有状态的会员卡数据
        HtmlSelect select = (HtmlSelect) memberCardPage.getElementById(STATESELECT);
        HtmlOption option = select.getOptionByValue("0");
        option.setSelected(true);

        String queryXPath = "//*[@id=\"btnQuery\"]";
        HtmlInput allMemberCard = memberCardPage.getFirstByXPath(queryXPath);
        HtmlPage allMemberCardPage = allMemberCard.click();
        pages.add(allMemberCardPage);
        //nextPage(allMemberCardPage, 43);//会员卡总页数

    }
}
