package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.ys.datatool.util.WebClientUtil;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by mo on @date  2018/7/4.
 * 快修哥系统(好快省)
 */
@Service
public class KuaiXiuGeService {

    private String PART_URL="http://saas.hks360.com/PartsSet.aspx?page=PartsSet&cid=5188&username=fumeijing";

    private String LOGIN_URL ="http://saas.hks360.com/default.aspx?id=0";

    private String COMPANYID = "ucode";

    private String USERID = "uname";

    private String PWDID = "upwd";

    private String LOGINBUTTON = "Button1";

    private String COMPANYNAME = "5188";

    private String USERNAME = "fumeijing";

    private String PASSWORD = "hks123";

    @Test
    public void  test() throws IOException{
        WebClient webClient = WebClientUtil.getWebClient();
        login(webClient);

        HtmlPage partPage=webClient.getPage(PART_URL);
        System.out.println("结果为"+partPage.asXml());
    }

    private void login(WebClient webClient) throws IOException {
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);
        HtmlInput company= loginPage.getHtmlElementById(COMPANYID);
        HtmlInput user = loginPage.getHtmlElementById(USERID);
        HtmlInput pwd = loginPage.getHtmlElementById(PWDID);
        HtmlSpan btnLogin = loginPage.getHtmlElementById(LOGINBUTTON);

        company.setValueAttribute(COMPANYNAME);
        user.setValueAttribute(USERNAME);
        pwd.setValueAttribute(PASSWORD);
        btnLogin.click();
    }
}
