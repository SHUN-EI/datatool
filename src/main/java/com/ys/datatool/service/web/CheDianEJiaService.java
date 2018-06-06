package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.MemberCardItem;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mo on @date  2018-06-06.
 */

@Service
public class CheDianEJiaService {

    private String MEMBERCARDITEM_URL = "http://s.66ejia.com/ShopMembers/ShopMemberPackages.aspx";

    private String LOGIN_URL = "http://s.66ejia.com/Login.aspx";

    private List<HtmlPage> pages = new ArrayList();

    private int count = 0;

    @Test
    public void test() throws IOException {
        Set<String> ids = new HashSet<>();
        WebClient webClient = WebClientUtil.getWebClient();
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);

        HtmlInput user = loginPage.getHtmlElementById("txtLoginName");
        HtmlInput pwd = loginPage.getHtmlElementById("txtLoginPwd");
        HtmlInput btnLogin = loginPage.getHtmlElementById("btnLogin");

        user.setAttribute("value", "gelunbu");
        pwd.setAttribute("value", "123456");
        btnLogin.click();

        HtmlPage memberCardItemPage = webClient.getPage(MEMBERCARDITEM_URL);
        Document doc = Jsoup.parseBodyFragment(memberCardItemPage.asXml());

        String totalRegEx = "#AspNetPager1 > a:nth-child(10)";
        String totalPageStr = doc.select(totalRegEx).attr("href");
        String getTotalRegEx = "(?<=,').*(?=')";
        String totalStr = CommonUtil.fetchString(totalPageStr, getTotalRegEx);
        int total = Integer.parseInt(totalStr);

        String trRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr";
        int trSize = WebClientUtil.getTRSize(doc, trRegEx);

        pages.add(memberCardItemPage);
        nextPage(memberCardItemPage, total);

        if (trSize > 0) {
            for (int i = 0; i < pages.size(); i++) {
                for (int j = 2; j <= trSize; j++) {
                    doc = Jsoup.parseBodyFragment(pages.get(i).asXml());

                    String idRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(9) > a:nth-child(1)";
                    String idStr = doc.select(StringUtils.replace(idRegEx, "{no}", String.valueOf(j))).attr("href");
                    String getIdRegEx = "(?<=').*(?=')";
                    String id = CommonUtil.fetchString(idStr, getIdRegEx);
                    ids.add(id);
                }
            }
        }


        System.out.println("主页为" + memberCardItemPage.asXml());
        System.out.println("totalPage 为" + total);
        System.out.println("pages为" + pages.size());
        System.out.println("trSize为" + trSize);
        System.out.println("trSize为" + ids.toString());
    }

    @Test
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();


    }

    private void nextPage(HtmlPage page, int num) throws IOException {
        ++count;
        if (count == num)
            return;

        String anchorXPath = "//*[@id=\"AspNetPager1\"]/a[8]";
        HtmlAnchor anchor = page.getFirstByXPath(anchorXPath);

        HtmlPage htmlPage = anchor.click();
        pages.add(htmlPage);
        nextPage(htmlPage, num);
    }

}
