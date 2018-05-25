package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.util.WebClientUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by mo on @date  2018/4/27.
 * 中易智联
 */
@Service
public class ZhongYiZhiLian2Service {

    private static final String MEMBERCARD_URL = "http://boss.xmzyzl.com/Customer/MemberManage/GetSearchResult?limit=20&offset=0&StartTime=&EndTime=&CARDTYPEID=&EMPLOYEEID=&TYPE=3&shop=&SHOPID=&keyword=";

    private static final String LOGIN_URL = "http://boss.xmzyzl.com/home/Login";

    private static final String USERID = "txtLoginName";

    private static final String PWDID = "txtPassword";

    private static final String BTNLOGIN = "btnLogin";

    private static final String USERNAME = "13885197265";

    private static final String PASSWORD = "seagull";

    private String fieldName = "total";

    private Workbook workbook;


    @Test
    public void fetchMemberCardItemData() throws IOException {
        WebClient webClient = WebClientUtil.getWebClient();
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);
        HtmlInput user = loginPage.getHtmlElementById(USERID);
        HtmlInput pwd = loginPage.getHtmlElementById(PWDID);
        HtmlAnchor btnLogin = loginPage.getHtmlElementById(BTNLOGIN);//登录按钮

        user.setAttribute("value", USERNAME);
        pwd.setAttribute("value", PASSWORD);

         String url="http://boss.xmzyzl.com/Customer/MemberDetailed/Index?CardId=3ce189289271486391a81a9ec42be579&CUSTOMERID=000428f3387e4b75beceae0cfd52d0d0";

        //登录操作
        btnLogin.click();

        HtmlPage index=webClient.getPage(url);


        System.out.println(index.asXml());
    }


}
