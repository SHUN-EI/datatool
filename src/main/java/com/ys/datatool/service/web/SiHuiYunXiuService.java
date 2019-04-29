package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.entity.MemberCard;
import com.ys.datatool.domain.entity.MemberCardItem;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by mo on @date  2018/12/3.
 *
 * 驷惠云修系统
 */
@Service
public class SiHuiYunXiuService {

    private String URL = "http://www.sihuiyun.com/api/web?action=";

    private String memberCardAction = "HY02001";

    private String memberCardItemAction = "HY02007";

    private String carInfoAction = "JC16003";

    private String companyName = "驷惠云修";

    private String sessionid = "20180521-1819-5395-d5d7-a49d-72d69290";

    private String TOKEN = "18120333fa302e0bb044da820cc8499678fb7a";

    private String COOKIE = "loginName=zyn; ASP.NET_SessionId=lh2cxai411nxni4vp5jqmuq2; platform=%E4%BA%91%E4%BF%AE%E4%BC%81%E4%B8%9A%E7%89%88; HasLyData=1; Hm_lvt_05ad9204c97ed3f86ffa6aa6d0e0cdf0=1543822180; SsoToken=5F7655D323D64AA4B22334A3D18D1CBD9215DEE74AD91E79B432AD1C508D342AA1DBD813F1511BB500304A3E0B1838EE2517FBC289469BED176E16502A47B56EA0C2D8EBDF3C39BC4B0F991C85E900B51BE1DDE9B206C3A05526A521195E915DE17CE5CB3F92F791C0E4761BF528650DACFB183DBB3224857978F1D61B0B60B9; YxToken=18120333fa302e0bb044da820cc8499678fb7a; Hm_lpvt_05ad9204c97ed3f86ffa6aa6d0e0cdf0=1543826396";

    /**
     * 卡内项目
     * 会员-会员卡信息-卡内项目
     *
     * @throws Exception
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws Exception {
        List<MemberCard> memberCards = new ArrayList<>();
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, String> carNumberMap = new HashMap<>();

        //获取所有车牌
        Response resp = ConnectionUtil.doPostWithToken(URL + carInfoAction, getParam(carInfoAction, 1), COOKIE, TOKEN);
        JsonNode data = JsonObject.MAPPER.readTree(resp.returnContent().asString());
        JsonNode total = data.get("data").get("total");
        int carTotalPage = WebClientUtil.getTotalPage(total, 50);
        if (carTotalPage > 0) {
            for (int i = 1; i <= carTotalPage; i++) {
                resp = ConnectionUtil.doPostWithToken(URL + carInfoAction, getParam(carInfoAction, i), COOKIE, TOKEN);
                data = JsonObject.MAPPER.readTree(resp.returnContent().asString());
                JsonNode node = data.get("data").get("rows");

                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();
                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String carCode = element.get("CarCode").asText();
                        String clientID = element.get("ClientID").asText();
                        carNumberMap.put(clientID, carCode);
                    }
                }
            }
        }

        Response response = ConnectionUtil.doPostWithToken(URL + memberCardAction, getParam(memberCardAction, 1), COOKIE, TOKEN);
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get("total");
        int totalPage = WebClientUtil.getTotalPage(totalNode, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                Response res = ConnectionUtil.doPostWithToken(URL + memberCardAction, getParam(memberCardAction, i), COOKIE, TOKEN);
                result = JsonObject.MAPPER.readTree(res.returnContent().asString());
                JsonNode node = result.get("data").get("rows");

                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();
                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String cardId = element.get("CardID").asText();
                        String company = element.get("EntName").asText();
                        String cardCode = element.get("CardCode").asText();
                        String memberCardName = element.get("CardTypeName").asText();
                        String name = element.get("ClientName").asText();
                        String phone = element.get("ClientMobile").asText();
                        String balance = element.get("CurrMoney").asText();
                        String remark = element.get("Remark").asText();
                        String dateCreated = element.get("OpenCardDate").asText();
                        dateCreated = DateUtil.formatSQLDateTime(dateCreated);

                        String clientId = element.get("ClientID").asText();
                        String carNumber=carNumberMap.get(clientId);

                        String state = element.get("FlagState").asText();
                        switch (state) {
                            case "0":
                                state = "正常";
                                break;
                            case "10":
                                state = "禁用";
                                break;
                            case "2":
                                state = "退卡";
                                break;
                        }


                        MemberCard memberCard = new MemberCard();
                        memberCard.setCompanyName(company);
                        memberCard.setMemberCardId(cardId);
                        memberCard.setCardCode(cardCode);
                        memberCard.setMemberCardName(memberCardName);
                        memberCard.setName(name);
                        memberCard.setPhone(phone);
                        memberCard.setBalance(balance);
                        memberCard.setDateCreated(dateCreated);
                        memberCard.setUserId(clientId);//持卡人
                        memberCard.setCarNumber(carNumber);
                        memberCard.setCardSort(state + " " + remark);
                        memberCards.add(memberCard);
                    }
                }

            }
        }

        if (memberCards.size() > 0) {
            for (MemberCard memberCard : memberCards) {
                String cardId = memberCard.getMemberCardId();

                Response res = ConnectionUtil.doPostWithToken(URL + memberCardItemAction, getMemberCardItemParam(cardId), COOKIE, TOKEN);
                result = JsonObject.MAPPER.readTree(res.returnContent().asString());
                JsonNode node = result.get("data").get("cardItems");

                if (node.size() > 0) {
                    Iterator<JsonNode> it = node.iterator();
                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String itemName = element.get("ItemName").asText();
                        String code = element.get("ItemCode").asText();
                        String price = element.get("TaxInPrice").asText();
                        String num = element.get("Amount").asText();
                        String validTime = element.get("EndDate").asText();
                        String isValidForever = CommonUtil.getIsValidForever(validTime);

                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setCompanyName(memberCard.getCompanyName());
                        memberCardItem.setCardCode(memberCard.getCardCode());
                        memberCardItem.setItemName(itemName);
                        memberCardItem.setCode(code);
                        memberCardItem.setPrice(price);
                        memberCardItem.setNum(num);
                        memberCardItem.setOriginalNum(num);
                        memberCardItem.setValidTime(validTime);
                        memberCardItem.setIsValidForever(isValidForever);
                        memberCardItems.add(memberCardItem);
                    }
                }
            }
        }

        System.out.println("结果为" + totalPage);

        String pathname = "C:\\exportExcel\\驷惠云修会员卡.xls";
        String pathname2 = "C:\\exportExcel\\驷惠云修卡内项目.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname2);
    }


    private String getMemberCardItemParam(String id) {
        String param = "{" +
                "\"action\":" + "\"" + memberCardItemAction + "\"" +
                ",\"clienttype\":\"web\"," +
                "\"sessionid\":" +
                "\"" + sessionid + "\"" + "," +
                "\"data\":{" +
                "\"ID\":" + "\"" + id + "\"" +
                "}" +
                "}";
        return param;

    }

    private String getParam(String action, int num) {
        String param = "{" +
                "\"action\":" + "\"" + action + "\"" +
                ",\"clienttype\":\"web\"," +
                "\"sessionid\":" +
                "\"" + sessionid + "\"" + "," +
                "\"data\":{\"pageSize\":50," +
                "\"pageNumber\":" +
                num + "," +
                "\"pageIndex\":" +
                num + "," +
                "\"flag\":1}" +
                "}";
        return param;

    }

}
