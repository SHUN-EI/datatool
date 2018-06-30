package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/6/30.
 * 典典养车
 */
@Service
public class DianDianYangCheService {
    private static final String MEMBERCARD_URL = "https://ndsm.ddyc.com/ndsm/member/list";

    private static final String AUTHORITY = "ndsm.ddyc.com";

    private static final String ACCEPT = "application/json, text/plain, */*";

    private static final String ACCEPT_ENCODING = "gzip, deflate, br";

    private static final String ACCEPT_lANGUAGE = "zh-CN,zh;q=0.8";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final String ORIGIN = "https://ndsm.ddyc.com";

    private static final String REFERER = "https://ndsm.ddyc.com/ndsm/member/memberList/index";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Workbook workbook;

    private String fileName = "典典养车";

    private int num = 10;

    private static final String COOKIE = "gr_user_id=d22a54e1-15bf-48e5-81d4-2d5e965b8b4b; JSESSIONID=E4B478B7925A9370B5CF5D0D9DAE03E9; gr_session_id_e2f213a5f5164248817464925de8c1af=15ae9b21-ef1a-4e11-a0b2-22ca55cb5275";

    /**
     * 会员卡
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, getParam(1), AUTHORITY, ACCEPT, ACCEPT_ENCODING, ACCEPT_lANGUAGE, CONTENT_TYPE, COOKIE, ORIGIN, REFERER, USER_AGENT);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get("total");
        int totalPage = WebClientUtil.getTotalPage(totalNode, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, getParam(i), AUTHORITY, ACCEPT, ACCEPT_ENCODING, ACCEPT_lANGUAGE, CONTENT_TYPE, COOKIE, ORIGIN, REFERER, USER_AGENT);
                result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardName(element.get("memberId").asText());
                    memberCard.setName(element.get("name").asText());
                    memberCard.setPhone(element.get("phone").asText());
                    memberCard.setBalance(element.get("asset").get("balance").asText());

                    JsonNode carNode = element.get("carList");
                    if ("null" != carNode.asText())
                        memberCard.setCarNumber(carNode.get(0).get("plateNumber").asText());

                    memberCards.add(memberCard);
                }
            }
        }

        System.out.println("总页数为" + totalPage);
        System.out.println("总数为" + memberCards.size());
        System.out.println("结果为" + memberCards.toString());

    }

    private String getParam(int index) {
        String page = String.valueOf(index);
        String param = "{" + "\"page\":" + page + "," + "\"pageSize\":" + "10" + "}";

        return param;
    }
}
