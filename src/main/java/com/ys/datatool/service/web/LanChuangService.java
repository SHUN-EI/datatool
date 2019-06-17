package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.entity.Bill;
import com.ys.datatool.domain.entity.BillDetail;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.MemberCard;
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

    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private static final String COOKIE = "ASP.NET_SessionId=n53msi55ge5yt155n240ygv5; ZhiLuoAPP.APSXAUTH=1487EF28A35A71F8680138314601CE3AD045836263FDEF5067C28408880551E8E0F1B9FEE049E89F76D5DE760FCCF0AF0AD0C7E6BEC64B3B65D670155FD3DD3B1990A8E58CCA1913F725FB49BFE0F966FFCAE7617A50955D8211EA6BD94BE4BA5BEAA0D87A23E7E107AE21C5DF4E8C812EE73E714BDB788FC99615DE9A77F0FF849827A9BDE779FD04697ECBDFC853646BD85D1BE6D1CAACF3BF68B272B07EB88D74079A9B8832E2254E966093C3FB521E9AD428B49D5CA0B74C6A05C551FD8308467BB0AAE5481E138946BEC7C2239E4A651D1DC2EDDA50645E4D620A5CA093FCB9188ABCA73A0EDC194F5F4836AD9776C919651884CD58388092A0C35890EF1FC4663A75C69B064E0051F5C1CBD542522F38C79563F0621676D84357DDEF199AAC2EF9BCA3C1373E9D4B7F2813CA1329296641E3DD746FBA80AE62507E86F167E49C700E55321274669941F99BFE9641C094D6B8810AF5C193DD7C7FC9372CE44F1955670B8FAC6790854A9AC0217A4A872CDB9B1126131957E75D9130BEFEA0FA86E77315CF217DE190A9ECB4B8E2E708F935E5B58D7B267AF5530A3B1904995CE0CD5C36977F25DF58E1E92E4E057615A49AA42F0D1FD9AFFDFE59C0FD48F8628001F657D61086E1D59BF308FF2550AA76DD3ABB15D4D6314F7284CC9EE5720302587D7C06F00F1E1B51337031EB4F1DFAC011AECF18BC081A64A8A7BC0F08F400DD1247084AF615BDB0083244DB40879AF5B465B88B96D05C4117EDC9807F98C0EC44A0BA674EE5B5DA7ECDFD83BA79FC881B6246B515AB41A1133B97FB73C9B207DBA549753C019C61605197DF689C498C; indexact=0; menuone=q00; hisurl=../Member/MemList.aspx; menuid=q01; guid1=0611111134995";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private static final String MEMBERCARD_URL = "http://113.128.194.58:8090/main/MemSwiping.aspx?cardid=";

    private static final String MEMLIST_URL = "http://113.128.194.58:8090/Member/MemList.aspx";

    private static final String EXPENSEHISTORY_URL = "http://113.128.194.58:8090/Report/Expense_History.aspx";

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
            JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
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
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
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
            JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
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
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
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
            JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
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
