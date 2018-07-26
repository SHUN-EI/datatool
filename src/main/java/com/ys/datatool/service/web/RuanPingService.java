package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.domain.MemberCardItem;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018/6/30.
 * 软平汽修系统
 */
@Service
public class RuanPingService {
    private static final String MEMBERCARDITEM_URL = "http://rp.shruanping.com/cusManager/CusMember/GetDetailList";

    private static final String MEMBERCARD_URL = "http://rp.shruanping.com/cusManager/CusMember/GetList?dt=0.9859314750248469&Fileds=";

    private static final String COOKIE = "xclvkjl+dkjs=uGNfjKhXPkM=; SysPlatform-SAS2016=kLxZok/RVVBbl1J+Rrcaz5yoAVDiI2odfSYjTBG8L+AuXJhSKsrV9xpyMhX3PZohJ8jaRoe5EqsTM+i3v8RQWx5JSLrCsnP3swfpCGSam5vQQUC8ihz9OK8fcymncMMUnMzIAzT+0Qe8zLcGp5svFwWdKesI/SvLRRaBJyAT+c0nxpcT8ODEdhVdqQDLdmd7SqktN/wJfWdWwPzk1vb//IOlaDICQRNn5oPgafo0616amK2tCu2N05Jw0bXdQUIOuPK7GQz4A2CSr7RE8ti0gNr6DLvVr+H5r3rP48bZHdLGbLyhTcAmbYOHYZOB/a+4Lq1ODEi7V8qQp3yC9AYz4kzl7WoNSNWCK8NfMmh5LW6vH3Mpp3DDFPQTsja0eho0p81GheBQ3OyAZdD6IP/BjbR2yODUAfeEpg+Nbz4YrElWtlVpgD/XjMj3yjMbVYAiw2nTrNeBaCs=";

    private static final String ACCEPT = "*/*";

    private static final String CONNECTION = "keep-alive";

    private static final String HOST = "rp.shruanping.com";

    private static final String ORIGIN = "http://rp.shruanping.com";

    private static final String REFERER = "http://rp.shruanping.com/cusManager/CusMember/Index";

    private static final String UPGRADE_INSECURE_REQUESTS = "1";

    private static final String X_REQUESTED_WITH = "XMLHttpRequest";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "totalRows";

    private String fileName = "软平汽修系统";

    private String pageName = "records";


    /**
     * 卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        Response response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getPageParams("1"), COOKIE);
        int total = WebClientUtil.getTotalPage(response, MAPPER, pageName, 20);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getPageParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("dt").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String id = element.get("MemberID").asText();
                    ids.add(id);
                }
            }
        }

        if (ids.size() > 0) {
            for (String id : ids) {
                response = ConnectionUtil.doPostWithLeastParams(MEMBERCARDITEM_URL, getMemberCardItemParams(id), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> itemIterator = result.get("Item").iterator();
                while (itemIterator.hasNext()) {
                    JsonNode element = itemIterator.next();

                    if (StringUtils.isNotBlank(element.toString())) {
                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setMemberCardItemId(element.get("MemberID").asText());
                        memberCardItem.setItemName(element.get("ItemName").asText());
                        memberCardItem.setOriginalNum(element.get("Qty").asText());
                        memberCardItem.setNum(element.get("SurplusQty").asText());
                        memberCardItem.setPrice(element.get("STMoney").asText());
                        memberCardItems.add(memberCardItem);
                    }
                }

                Iterator<JsonNode> partsIterator = result.get("Parts").iterator();
                while (partsIterator.hasNext()) {
                    JsonNode element = partsIterator.next();

                    if (StringUtils.isNotBlank(element.toString())) {
                        MemberCardItem memberCardItem = new MemberCardItem();
                        memberCardItem.setMemberCardItemId(element.get("MemberID").asText());
                        memberCardItem.setItemName(element.get("PartsName").asText());
                        memberCardItem.setOriginalNum(element.get("Qty").asText());
                        memberCardItem.setNum(element.get("SurplusQty").asText());
                        memberCardItem.setPrice(element.get("STMoney").asText());
                        memberCardItems.add(memberCardItem);
                    }
                }
            }
        }
    }

    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getPageParams("1"), COOKIE);
        int total = WebClientUtil.getTotalPage(response, MAPPER, pageName, 20);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                response = ConnectionUtil.doPostWithLeastParams(MEMBERCARD_URL, getPageParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("dt").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCardCode(element.get("CardNo").asText());
                    memberCard.setMemberCardId(element.get("MemberID").asText());
                    memberCard.setName(element.get("CustomerName").asText());
                    memberCard.setMemberCardName(element.get("MemKind").asText());
                    memberCard.setDateCreated(element.get("CreateDate").asText());
                    memberCard.setBalance(element.get("SurplusPrice").asText());
                    memberCard.setCarNumber(element.get("Plate").asText());
                    memberCard.setValidTime(element.get("EndDate").asText());
                    memberCards.add(memberCard);
                }
            }
        }

    }

    private List<BasicNameValuePair> getMemberCardItemParams(String id) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", id));
        return params;
    }

    private List<BasicNameValuePair> getPageParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("pagesize", "20"));
        params.add(new BasicNameValuePair("page", pageNo));
        return params;
    }
}
