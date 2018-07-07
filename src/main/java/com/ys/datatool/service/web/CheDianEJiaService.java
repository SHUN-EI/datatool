package com.ys.datatool.service.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.MemberCardItem;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mo on @date  2018-06-06.
 * 车店E家系统
 */

@Service
public class CheDianEJiaService {

    private String MEMBERCARDITEMDETAIL_URL = "http://s.66ejia.com/ShopMembers/ShopMemberPackageEdit.aspx?id=";

    private String MEMBERCARDITEM_URL = "http://s.66ejia.com/ShopMembers/ShopMemberPackages.aspx";

    private String LOGIN_URL = "http://s.66ejia.com/Login.aspx";

    private List<HtmlPage> pages = new ArrayList();

    private int count = 0;

    private Workbook workbook;

    private String trName = "tr";

    //获取数据的令牌
    private String COOKIE = "ASP.NET_SessionId=mgou4uidksuc2qzghgcmsswp; CarSaasShopAdmin=eyJSb2xlQ29udGVudHMiOiIiLCJTaG9wU3RhdGUiOjAsIkNhck51bWJlckhlYWQiOiJcdTdDQTRBIiwiSUQiOiJmNWNjZjllMS1iMjQyLTQxOWUtYTA4MS05NDdiNWQ0MWZlNWIiLCJTaG9wSUQiOiIzZWUzYWM0Ny05ZGNlLTQ4NjctOGRmYS1jZGRiNTVlMTNhNzgiLCJMb2dpbk5hbWUiOiJnZWx1bmJ1IiwiTG9naW5Qd2QiOiIiLCJXWE9wZW5JRCI6IiIsIlRydWVOYW1lIjoiXHU3MzhCIiwiVXNlclBob25lIjoiMTgxMjcwNzc1NzMiLCJSb2xlSUQiOiIwIiwiUm9sZU5hbWUiOiJcdTdCQTFcdTc0MDZcdTU0NTgiLCJBZG1pblN0YXRlIjowLCJBZGRUaW1lIjoiMDkvMTgvMjAxNyAxOToyOTo0NyIsIlNob3BOYW1lIjoiXHU3QzczXHU1MTc2XHU2Nzk3Llx1OUE3MFx1NTJBMFx1NkM3RFx1OEY2Nlx1NjcwRFx1NTJBMVx1NUU5NyIsIk9yZ2FuSUQiOiJHREdaIiwiU2hvcExvZ28iOiIvVXBsb2FkL3B1YmxpYy9iNGYzMGNmZjI1NGE0NDc1YTA0YjJmZDgzNmE1NWZlNS5qcGciLCJTaG9wUGhvbmUiOiIwNzYwLTg2MzYzMDMzIiwiU2hvcE1hc3RlciI6Ilx1OTBFRFx1NUMwRlx1NTlEMCIsIlNob3BNYXN0ZXJQaG9uZSI6IjE4MDIyMTA4NDAwIiwiU2hvcFByb3ZpbmNlIjoiXHU1RTdGXHU0RTFDXHU3NzAxIiwiU2hvcENpdHkiOiJcdTRFMkRcdTVDNzFcdTVFMDIiLCJTaG9wQXJlYSI6Ilx1NTc2Nlx1NkQzMlx1OTU0NyIsIlNob3BBZGRyZXNzIjoiXHU3OEE3XHU1Qjg5XHU4REVGNFx1NTNGN1x1OTUyNlx1N0VFM1x1OTZDNVx1ODJEMTlcdTY3MUZcdUZGMDhcdTczQUZcdTZEMzJcdTUzMTdcdThERUZcdTRFMEVcdTc4QTdcdTVCODlcdThERUZcdTRFQTRcdTYzQTVcdTU5MDRcdUZGMDkiLCJTaG9wUGFyZW50SUQiOiIiLCJTaG9wQWRtaW5UeXBlIjoxMH0=";


    @Test
    public void test() throws IOException {

    }


    /**
     * 卡内项目
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, MemberCardItem> memberCardItemMap = new HashMap<>();

        WebClient webClient = getLoginWebClient();
        HtmlPage memberCardItemPage = webClient.getPage(MEMBERCARDITEM_URL);
        Document doc = Jsoup.parseBodyFragment(memberCardItemPage.asXml());

        String totalRegEx = "#AspNetPager1 > a:nth-child(10)";
        String totalPageStr = doc.select(totalRegEx).attr("href");
        String getTotalRegEx = "(?<=,').*(?=')";
        String totalStr = CommonUtil.fetchString(totalPageStr, getTotalRegEx);
        int total = Integer.parseInt(totalStr);

        pages.add(memberCardItemPage);
        nextPage(memberCardItemPage, total);

        for (int i = 0; i < pages.size(); i++) {
            doc = Jsoup.parseBodyFragment(pages.get(i).asXml());
            String trRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr";
            int trSize = WebClientUtil.getTagSize(doc, trRegEx,trName);

            for (int j = 2; j <= trSize; j++) {
                String idRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(9) > a:nth-child(1)";
                String idStr = doc.select(StringUtils.replace(idRegEx, "{no}", String.valueOf(j))).attr("href");
                String getIdRegEx = "(?<=').*(?=')";
                String id = CommonUtil.fetchString(idStr, getIdRegEx);

                String nameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(1)";
                String cardCodeRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(2)";
                String memberCardNameRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(3)";
                String dateCreatedRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(8)";
                String validTimeRegEx = "#form1 > div.rightinfo > table.tablelist > tbody > tr:nth-child({no}) > td:nth-child(6)";

                String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                String cardCode=doc.select(StringUtils.replace(cardCodeRegEx, "{no}", String.valueOf(j))).text();
                String memberCardName=doc.select(StringUtils.replace(memberCardNameRegEx, "{no}", String.valueOf(j))).text();
                String dateCreated=doc.select(StringUtils.replace(dateCreatedRegEx, "{no}", String.valueOf(j))).text();
                String validTime=doc.select(StringUtils.replace(validTimeRegEx, "{no}", String.valueOf(j))).text();

                MemberCardItem memberCardItem = new MemberCardItem();
                memberCardItem.setName(name);
                memberCardItem.setPhone(cardCode);
                memberCardItem.setCardCode(cardCode);
                memberCardItem.setMemberCardName(memberCardName);
                memberCardItem.setDateCreated(dateCreated);
                memberCardItem.setValidTime(validTime);

                memberCardItemMap.put(id, memberCardItem);
            }
        }

        if (memberCardItemMap.size() > 0) {
            for (String id : memberCardItemMap.keySet()) {
                Response response = ConnectionUtil.doGetWithLeastParams(MEMBERCARDITEMDETAIL_URL + id, COOKIE);
                String html = response.returnContent().asString();
                doc = Jsoup.parse(html);

                String getTRRegEx = "#form1 > div.formbody > table > tbody > tr";
                int tRSize = WebClientUtil.getTagSize(doc, getTRRegEx,trName);

                for (int i = 2; i <= tRSize; i++) {

                    String itemNameRegEx = "#form1 > div.formbody > table > tbody > tr:nth-child({no}) > td:nth-child(1) > input[type=\"hidden\"]:nth-child(2)";
                    String originalNumRegEx = "#form1 > div.formbody > table > tbody > tr:nth-child({no}) > td:nth-child(2)";
                    String usedNumRegEx = "#form1 > div.formbody > table > tbody > tr:nth-child(2) > td:nth-child(3) > input";
                    String firstCategoryNameRegEx = "#form1 > div.formbody > table > tbody > tr:nth-child({no}) > td:nth-child(1) > input[type=\"hidden\"]:nth-child(1)";

                    String originalNumStr = doc.select(StringUtils.replace(originalNumRegEx, "{no}", String.valueOf(i))).text();
                    String usedNumStr = doc.select(StringUtils.replace(usedNumRegEx, "{no}", String.valueOf(i))).attr("value");
                    String firstCategoryName = doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(i))).attr("value");
                    String itemName = doc.select(StringUtils.replace(itemNameRegEx, "{no}", String.valueOf(i))).attr("value");

                    int originalNum = Integer.parseInt(originalNumStr);
                    int usedNum = Integer.parseInt(usedNumStr);
                    int num = originalNum - usedNum;

                    MemberCardItem m = memberCardItemMap.get(id);
                    MemberCardItem memberCardItem = new MemberCardItem();
                    memberCardItem.setItemName(itemName);
                    memberCardItem.setOriginalNum(originalNumStr);
                    memberCardItem.setNum(String.valueOf(num));
                    memberCardItem.setFirstCategoryName(firstCategoryName);
                    memberCardItem.setName(m.getName());
                    memberCardItem.setPhone(m.getPhone());
                    memberCardItem.setCardCode(m.getCardCode());
                    memberCardItem.setMemberCardName(m.getMemberCardName());
                    memberCardItem.setDateCreated(m.getDateCreated());
                    memberCardItem.setValidTime(m.getValidTime());
                    memberCardItems.add(memberCardItem);
                }
            }
        }


        String pathname = "C:\\exportExcel\\车店E家卡内项目导出.xlsx";
        ExportUtil.exportMemberCardItemSomeFieldDataInLocal(memberCardItems, workbook, pathname);

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("大小为" + memberCardItems.size());

    }

    private WebClient getLoginWebClient() throws IOException {
        WebClient webClient = WebClientUtil.getWebClient();
        HtmlPage loginPage = webClient.getPage(LOGIN_URL);

        HtmlInput user = loginPage.getHtmlElementById("txtLoginName");
        HtmlInput pwd = loginPage.getHtmlElementById("txtLoginPwd");
        HtmlInput btnLogin = loginPage.getHtmlElementById("btnLogin");

        user.setAttribute("value", "gelunbu");
        pwd.setAttribute("value", "123456");
        btnLogin.click();

        return webClient;
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
