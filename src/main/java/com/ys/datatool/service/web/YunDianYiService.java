package com.ys.datatool.service.web;

import com.ys.datatool.domain.HtmlTag;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mo on @date  2018/12/5.
 * <p>
 * 云店易系统2.2
 */

@Service
public class YunDianYiService {

    private String PACKAGECAR_URL = "https://vip.yundianyi.com/promotion/ajaxpackageshow/id/";

    private String PACKAGEPAGE_URL = "https://vip.yundianyi.com/promotion/getpackagepage/type/enable/classid/1/page/";

    private String PAGE_COOKIE = "PHPSESSID=5dgjhd202dv6qo0l0oq08o96u4; ace_settings=%7B%22sidebar-collapsed%22%3A-1%7D; SERVERID=02f09cca2cc8a5aa65c27d4c679ad17a|1544098808|1544098808";

    private String serverId = "SERVERID=02f09cca2cc8a5aa65c27d4c679ad17a|1544098982|1544098982";

    private String COOKIE = "PHPSESSID=5dgjhd202dv6qo0l0oq08o96u4; ace_settings=%7B%22sidebar-collapsed%22%3A-1%7D; " + serverId;


    /**
     * 促销套餐数据
     * <p>
     * 营销管理-促销套餐列表-获取每个套餐中所有购买车辆
     * 客户管理-客户搜索，搜索所有车牌号，获取每个车辆的套餐详情
     *
     * @throws Exception
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws Exception {
        Set<String> serverIds = new HashSet<>();
        List<MemberCard> memberCards = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(PACKAGEPAGE_URL + 1, PAGE_COOKIE);
        String html = response.returnContent().asString();
        Document document = Jsoup.parse(html);

        String totalRegEx = "#package_page_table_enable_1 > table > tbody > tr > td > div > ul > li:nth-child(3) > input";
        String totalPageStr = document.select(totalRegEx).attr("data-max-page");
        int total = Integer.parseInt(totalPageStr);

        if (total > 0) {
            //每页为10行，取多一页
            for (int i = 1; i <= total * 11; i++) {
                Response res = ConnectionUtil.doGetWithLeastParams(PACKAGEPAGE_URL + i, COOKIE);
                String cookie = res.returnResponse().getFirstHeader("Set-Cookie").getValue();
                cookie = StringUtils.replace(cookie, ";Path=/", "");
                serverIds.add(cookie);
                serverId = cookie;
            }

            for (int j = 1; j <= total; j++) {
                for (String sid : serverIds) {

                    serverId = sid;
                    String url = PACKAGEPAGE_URL + j;
                    Response res2 = ConnectionUtil.doGetWithLeastParams(url, COOKIE);
                    String content = res2.returnContent().asString();
                    Document doc = Jsoup.parse(content);

                    String trRegEx = "#package_data_table_enable_1 > div > table > tbody > tr";
                    int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);

                    if (trSize > 0) {
                        for (int i = 2; i <= trSize; i++) {

                            String memberCardNameRegEx = trRegEx + ":nth-child(" + i + ") > td:nth-child(1)";
                            String cardIdRegEx = trRegEx + ":nth-child(" + i + ") > td:nth-child(6) > a.btn.btn-primary.btn-minier";
                            String cardIdStr = doc.select(cardIdRegEx).attr("onclick");
                            String getIdRegEx = "(?<=')(.*)(?=')";
                            String cardId = CommonUtil.fetchString(cardIdStr, getIdRegEx);

                            String memberCardName = doc.select(memberCardNameRegEx).text();

                            MemberCard memberCard = new MemberCard();
                            memberCard.setMemberCardName(memberCardName);
                            memberCard.setMemberCardId(cardId);
                            memberCards.add(memberCard);

                        }
                    }
                }

                String sss = "";

            }
        }


    }
}
