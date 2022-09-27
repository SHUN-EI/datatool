package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.MemberCard;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 京东云修系统
 */
@Service
public class JDYunXiuService {

    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private String COOKIE = "__jdv=135409966|direct|-|none|-|1663300134323; thor=05755E80072BF2B819AEF6ECC4AA33D32F1F77891AE5528FCF19F110612D201368C5C76D05330B911CD1C13894A9C37A7639E6C9A6977954BD6D2C2D1A0B24A3BC50A7D01658FD727D576CEC72A2C2C4137C8BC929F082289C353EFDC93F21CFF33FF2978B7ED9FE86B0674EA57740D256006E09188C3F6E9F7491628BC98F6857D70649F351CFE5B7F709004060406E1C09B01B64981135D754419578D6287D; pin=jd_4b2f1d770020a; unick=jd_137261fwj; JD_UUID=3bc74a26-65ac-48ca-97b1-fed4da834386; yunxiupin=jd_4b2f1d770020a; UUID=84632614-56eb-4e85-bd02-9ee5301f4c2c; SESSION_USER_NAME=%E8%B0%88%E7%A4%BE%E5%9F%B9; __jda=154799550.16633001343231436663376.1663300134.1663300134.1663300134.1; __jdc=154799550; PUBLIC_NOTICE_TIME37318=\"2022-09-09 10:34:40\"; __jdb=154799550.5.16633001343231436663376|1.1663300134";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String CARINFO_URL = "https://www.yunxiu.com/legend/account/search?page=";


    @Test
    public void fetchCarInfoDataStandard() throws IOException {

        List<MemberCard> memberCards = new ArrayList<>();

        Response response = Request.Get(CARINFO_URL + 1 + "")
                .setHeader("Cookie", COOKIE)
                .execute();

        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));
        JsonNode dataNode = result.get("data");

        JsonNode totalPageNode = dataNode.get("totalPages");

        if (totalPageNode != null) {

            for (int i = 1; i <= totalPageNode.asInt(); i++) {
                Response res = Request.Get(CARINFO_URL + i + "")
                        .setHeader("Cookie", COOKIE)
                        .execute();

                JsonNode resultNodes = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode dataListNode = resultNodes.get("data").get("content");

                if (dataListNode != null && dataListNode.size() > 0) {
                    Iterator<JsonNode> it = dataListNode.iterator();

                    while (it.hasNext()) {
                        JsonNode memberNode = it.next();

                        String companyName = "京东云修";
                        String name = memberNode.get("customerName").asText();
                        String phone = memberNode.get("mobile").asText();
                        JsonNode carNumberNode = memberNode.get("licenseList");

                        StringBuffer carNumberString = new StringBuffer();
                        if (carNumberNode != null && carNumberNode.size() > 0) {
                            Iterator<JsonNode> carNumbers = carNumberNode.iterator();

                            while (carNumbers.hasNext()) {
                                JsonNode carNumber = carNumbers.next();
                                carNumberString.append(carNumber).append(",");
                            }
                        }


                        JsonNode cardNode = memberNode.get("memberCards");
                        if (cardNode != null && cardNode.size() > 0) {
                            Iterator<JsonNode> cards = cardNode.iterator();

                            while (cards.hasNext()) {
                                JsonNode card = cards.next();
                                String memberCardId = card.get("memberCardId").asText();
                                String memberCardName = card.get("typeName").asText();
                                String dateCreated = card.get("cardGmtCreate").asText();
                                String balance = card.get("balance").asText();

                                MemberCard memberCard = new MemberCard();
                                memberCard.setCompanyName(companyName);
                                memberCard.setName(name);
                                memberCard.setPhone(phone);
                                memberCard.setMemberCardId(memberCardId);
                                memberCard.setCardCode(memberCardId);//会员卡卡号
                                memberCard.setMemberCardName(memberCardName);
                                memberCard.setCardType(memberCardName);//卡类型
                                memberCard.setCardSort(memberCardName);//卡品种
                                memberCard.setDateCreated(dateCreated);
                                memberCard.setBalance(balance);
                                memberCard.setCarNumber(carNumberString.toString());//车牌号

                                memberCards.add(memberCard);
                            }
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\京东云修会员.xls";

        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);

    }
}
