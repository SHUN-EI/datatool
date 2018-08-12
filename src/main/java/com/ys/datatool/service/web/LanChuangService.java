package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.Bill;
import com.ys.datatool.domain.BillDetail;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.util.ConnectionUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/6/30.
 * 蓝创系统
 */
@Service
public class LanChuangService {

    private static final String MEMBERCARD_URL = "http://113.128.194.58:8090/main/MemSwiping.aspx?cardid=";

    private static final String MEMLIST_URL = "http://113.128.194.58:8090/Member/MemList.aspx";

    private static final String EXPENSEHISTORY_URL = "http://113.128.194.58:8090/Report/Expense_History.aspx";

    private static final String COOKIE = "pageSize=50; ASP.NET_SessionId=w04yex550dyt2jbgop3w12ac; ZhiLuoAPP.APSXAUTH=6E59BB6C6A5DCB96497A5D5F5897C4D01D94CF0232D9E43DACAE8EED5A22C1F6707AD9CA9A87EFEA1165F5DFEBE20CBE4200B61E82B112CA3D54F5FE407879B01CD27F49231DEB97B3A6E4C650565A397D87783B3D0C8A4CC770AF08E6A352A6BC5D7ED9555362DEA67297A1FA16AB3A59B6510369F2DC820DA2ECE48BAE5F6E075EE5903CAE87B13B1F222AEEFBDEE7B0C57581C712C94B1E17F138453199BEB368BA02AD25ACAD0986A5D4BBD8C320483C56123FF1A0597A45C3E53455D2BEE4FBF42B3A4CA6DB76A6219E27B33DC3AE374E4E04382884BFC6608B460D745304B228E8EAF6676D9D8CECBC1FE6266DE48C1356770AB4BC9F2BC543FA22467CF17138A890231B94D1FC3B3737A61CE4C9C3D189B34A9E86B292D9E328F8D07AE078D0644C3AECD94B51C2ED3D3E91136647E29BFB91B663AF64EF3C6A82238AC61B3A7729A66056D1ADC9904AB6D829E4FE1E04BDBA4ABF5B6C696C781267D4AFCD00E30953B16EE54F846EE6EA1BAA941A30A3C4BCCC9B75C2FA57D7474AA1BD560BF7B9F61B129B5549F3741EC05FEAC4C366DD7EA18D4F48C954B07AF37D2C61A8B22B9F43AFDE3CA185EDB93EC304002840C94DC572F5CB9EAAB5B7214FAFE2BCDD8250FE8F2FD441A16954527E600F95695617D20045AE51B5BAEAE616EF4A441A81CBE9107FB7A97EE81B7503DFDF17F2CC0C7FBCC19CBFBF6C2CD8ECB635238E0E464E073937A4F2A21B1BC506304E12AB96A3EFAC1F3D281457FB3FD5D4526982C0E5A5C1254DB035CBC7F533F1CDD54D65EC5251E4026246EC722DCBC24932; indexact=3; menuone=%u4F1A%u5458%u6D88%u8D39; hisurl=../Report/Expense_History.aspx; menuid=m22; guid0=0706150313641";

    private static final String ACCEPT = "application/json, text/javascript, */*; q=0.01";

    private static final String ACCEPT_ENCODING = "gzip, deflate";

    private static final String ACCEPT_LANGUAGE = "zh-CN,zh;q=0.8";

    private static final String ORIGIN = "http://113.128.194.58:8090";

    private static final String CONNECTION = "keep-alive";

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final String HOST = "113.128.194.58:8090";

    private static final String REFERER = "http://113.128.194.58:8090/Report/Expense_History.aspx";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    private static final String UPGRADE_INSECURE_REQUESTS = "1";

    private static final String X_REQUESTED_WITH = "XMLHttpRequest";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String prefixURL = "http://113.128.194.58:8090/ajax/ajax.aspx?ajaxGuid=";

    private List<HtmlPage> pages = new ArrayList();

    private String fileName = "蓝创会员管理系统";


    /**
     * 会员卡
     *
     * @throws IOException
     * @throws ScriptException
     */
    public void fetchMemberCardData() throws IOException, ScriptException {
        List<MemberCard> memberCards = new ArrayList<>();
        List<String> cardIds = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("todo", "GetMemListPage"));
            params.add(new BasicNameValuePair("size", "50"));
            params.add(new BasicNameValuePair("index", i + ""));

            String membercardURL = prefixURL + getGUID() + "&ajaxIndex=" + (i - 1) + "&url=" + MEMLIST_URL;
            Response response = ConnectionUtil.doPostWithLeastParams(membercardURL, params, COOKIE);
            JsonNode result = MAPPER.readTree(response.returnContent().asString());
            JsonNode node = result.get("msg");
            Iterator<JsonNode> it = node.get("List").iterator();

            while (it.hasNext()) {
                JsonNode element = it.next();
                cardIds.add(element.get("CardID").asText());
            }
        }

        if (cardIds.size() > 0) {
            for (int i = 0; i < cardIds.size(); i++) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("todo", "GetMemModelAll"));
                params.add(new BasicNameValuePair("memKey", cardIds.get(i)));

                String memberCardURL = prefixURL + getGUID() + "&ajaxIndex=0" + "&url=" + MEMBERCARD_URL + cardIds.get(i);

                Response response = ConnectionUtil.doPostWithLeastParams(memberCardURL, params, COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());
                JsonNode node = result.get("msg");

                MemberCard memberCard = new MemberCard();
                memberCard.setCardCode(cardIds.get(i));
                memberCard.setName(node.get("Name").asText());
                memberCard.setPhone(node.get("Mobile").asText());
                memberCard.setCarNumber(node.get("C_clpz").asText());
                memberCard.setBalance(node.get("ShopMoney").asText());
                memberCard.setDateCreated(node.get("CreateTime").asText());
                memberCards.add(memberCard);
            }
        }
    }


    /**
     * 单据明细
     *
     * @throws IOException
     * @throws ScriptException
     */
    public void fetchBillDetailData() throws IOException, ScriptException {
        List<BillDetail> billDetails = new ArrayList<>();
        List<String> billNos = new ArrayList<>();

        for (int i = 1; i <= 14; i++) {
            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("todo", "GetExHistoryPage"));
            params.add(new BasicNameValuePair("size", "50"));
            params.add(new BasicNameValuePair("index", i + ""));
            params.add(new BasicNameValuePair("key", ""));
            params.add(new BasicNameValuePair("currentShop", "1042"));
            params.add(new BasicNameValuePair("ReportMem_startTime", ""));
            params.add(new BasicNameValuePair("ReportMem_endTime", ""));
            params.add(new BasicNameValuePair("OrderCode", ""));

            String billURL = prefixURL + getGUID() + "&ajaxIndex=" + (i - 1) + "&url=" + EXPENSEHISTORY_URL;

            Response response = ConnectionUtil.doPostWithLeastParams(billURL, params, COOKIE);
            JsonNode result = MAPPER.readTree(response.returnContent().asString());
            JsonNode node = result.get("msg");
            Iterator<JsonNode> it = node.get("orderPeprot").iterator();

            while (it.hasNext()) {
                JsonNode element = it.next();
                billNos.add(element.get("OrderCode").asText());
            }
        }

        if (billNos.size() > 0) {
            for (int i = 0; i < billNos.size(); i++) {
                List<BasicNameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("todo", "GetorderDetail"));
                params.add(new BasicNameValuePair("OrderCode", billNos.get(i)));

                String billDetailURL = prefixURL + getGUID() + "&ajaxIndex=" + (i) + "&url=" + EXPENSEHISTORY_URL;

                Response response = ConnectionUtil.doPostWithLeastParams(billDetailURL, params, COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());
                JsonNode node = result.get("msg");
                Iterator<JsonNode> it = node.get("orderDetailReport").iterator();

                while (it.hasNext()) {
                    JsonNode element = it.next();

                    BillDetail billDetail = new BillDetail();
                    billDetail.setBillNo(billNos.get(i));
                    billDetail.setItemName(element.get("GoodsName").asText());
                    billDetail.setPrice(element.get("Price").asText());
                    billDetail.setNum(element.get("Number").asText());

                    billDetails.add(billDetail);
                }
            }
        }
    }

    /**
     * 单据
     *
     * @throws IOException
     * @throws ScriptException
     */
    public void fetchBillData() throws IOException, ScriptException {
        List<Bill> bills = new ArrayList<>();

        for (int i = 1; i <= 14; i++) {
            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("todo", "GetExHistoryPage"));
            params.add(new BasicNameValuePair("size", "50"));
            params.add(new BasicNameValuePair("index", i + ""));
            params.add(new BasicNameValuePair("key", ""));
            params.add(new BasicNameValuePair("currentShop", "1042"));
            params.add(new BasicNameValuePair("ReportMem_startTime", ""));
            params.add(new BasicNameValuePair("ReportMem_endTime", ""));
            params.add(new BasicNameValuePair("OrderCode", ""));

            String billURL = prefixURL + getGUID() + "&ajaxIndex=" + (i - 1) + "&url=" + EXPENSEHISTORY_URL;

            Response response = ConnectionUtil.doPostWithLeastParams(billURL, params, COOKIE);
            JsonNode result = MAPPER.readTree(response.returnContent().asString());
            JsonNode node = result.get("msg");
            Iterator<JsonNode> it = node.get("orderPeprot").iterator();

            while (it.hasNext()) {
                JsonNode element = it.next();

                Bill bill = new Bill();
                bill.setBillNo(element.get("OrderCode").asText());
                bill.setCardCode(element.get("CardID").asText());
                bill.setName(element.get("MemName").asText());
                bill.setTotalAmount(element.get("TotalMoney").asText());
                bill.setActualAmount(element.get("DiscountMoney").asText());
                bill.setDateAdded(element.get("CreateTime").asText());
                bill.setDateEnd(element.get("CreateTime").asText());
                bill.setDateExpect(element.get("CreateTime").asText());
                bill.setRemark(element.get("Remark").asText());

                bills.add(bill);
            }
        }

    }


    private String getGUID() throws IOException, ScriptException {
        String guidScript = "(function"
                + " GetGuid()"
                + "{"
                + "   var now = new Date();"
                + "   var year = now.getFullYear();"
                + "   var month = now.getMonth();"
                + "   var date = now.getDate();"
                + "   var day = now.getDay();"
                + "   var hour = now.getHours();"
                + "   var minu = now.getMinutes();"
                + "   var sec = now.getSeconds();"
                + "   var mill = now.getMilliseconds();"
                + "   month = month + 1;"
                + "   if (month < 10) month = \"0\" + month;"
                + "   if (date < 10) date = \"0\" + date;"
                + "   if (hour < 10) hour = \"0\" + hour;"
                + "   if (minu < 10) minu = \"0\" + minu;"
                + "   if (sec < 10) sec = \"0\" + sec;"
                + "   var guid = month + date + hour + minu + sec + mill;"
                + "   return guid;"
                + "})()";

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        Object guid = engine.eval(guidScript);

        return guid.toString();
    }
}
