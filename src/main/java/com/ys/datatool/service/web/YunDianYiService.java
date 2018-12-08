package com.ys.datatool.service.web;

import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.HtmlTag;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.domain.MemberCardItem;
import com.ys.datatool.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/12/5.
 * <p>
 * 云店易系统2.2
 */

@Service
public class YunDianYiService {

    private String CARINFO_URL = "https://vip.yundianyi.com/custom/getcustom/id/";

    private String PACKAGECAR_URL = "https://vip.yundianyi.com/promotion/ajaxshowpromotionrecord/type/enable/id/";

    private String PACKAGECARPAGE_URL = "https://vip.yundianyi.com/promotion/ajaxpackageshow/id/";

    private String PACKAGEPAGE_URL = "https://vip.yundianyi.com/promotion/getpackagepage/type/enable/classid/1/page/";

    private String X_REQUESTED_WITH = "XMLHttpRequest";

    private String companyName = "云店易";

    private String COOKIE = "PHPSESSID=ag05ngpe5rkod40ep7jamd4166;";


    /**
     * 促销套餐数据(该客户只有洗车套餐数据)
     * <p>
     * 营销管理-促销套餐列表-获取每个套餐中所有购买车辆
     * 客户管理-客户搜索，搜索所有车牌号，获取每个车辆的套餐详情
     *
     * @throws Exception
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws Exception {
        List<MemberCard> memberCards = new ArrayList<>();
        List<MemberCard> cards = new ArrayList<>();
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(PACKAGEPAGE_URL + 1, COOKIE);
        String html = response.returnContent().asString();
        Document document = Jsoup.parse(html);

        String totalRegEx = "#package_page_table_enable_1 > table > tbody > tr > td > div > ul > li:nth-child(3) > input";
        String totalPageStr = document.select(totalRegEx).attr("data-max-page");
        int total = Integer.parseInt(totalPageStr);


        //获取所有套餐
        if (total > 0) {
            for (int i = 1; i <= total; i++) {

                String url = PACKAGEPAGE_URL + i;
                Response res = ConnectionUtil.doGetWithLeastParams(url, COOKIE);

                HttpResponse httpResponse = res.returnResponse();
                //String cookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
                HttpEntity httpEntity = httpResponse.getEntity();
                String body = EntityUtils.toString(httpEntity);
                Document doc = Jsoup.parseBodyFragment(body);

                String trRegEx = "#package_data_table_enable_1 > div > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    for (int j = 2; j <= trSize; j++) {

                        String memberCardNameRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(1)";
                        String cardIdRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(6) > a.btn.btn-primary.btn-minier";
                        String cardIdStr = doc.select(cardIdRegEx).attr("onclick");
                        String getIdRegEx = "(?<=id/)(.*)(?=/')";
                        String cardId = CommonUtil.fetchString(cardIdStr, getIdRegEx);

                        //套餐到期时间
                        String validTimeRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(5) > span:nth-child(2)";
                        String validTime = doc.select(validTimeRegEx).text();
                        validTime = DateUtil.formatSQLDateTime(validTime);

                        String memberCardName = doc.select(memberCardNameRegEx).text();
                        MemberCard memberCard = new MemberCard();
                        memberCard.setMemberCardName(memberCardName);
                        memberCard.setMemberCardId(cardId);
                        memberCard.setValidTime(validTime);
                        cards.add(memberCard);
                    }
                }
            }
        }

        //获取每个套餐对应的所有车辆
        if (cards.size() > 0) {
            for (MemberCard card : cards) {

                String cardName = card.getMemberCardName();
                String cardId = card.getMemberCardId();
                String pageUrl = PACKAGECARPAGE_URL + cardId + "/";
                Response res = ConnectionUtil.doGetWith(pageUrl, COOKIE, X_REQUESTED_WITH);
                String content = res.returnContent().asString();
                Document doc = Jsoup.parseBodyFragment(content);

                String totalPageRegEx = "#enablePromotionSale > div.text-center > ul > li:nth-child(3) > input";
                String totalStr = doc.select(totalPageRegEx).attr("data-max-page");
                System.out.println("结果为" + pageUrl);

                if (totalStr == "")
                    continue;

                int totalPage = Integer.parseInt(totalStr);

                if (totalPage > 0) {
                    for (int i = 1; i <= totalPage; i++) {
                        String url = PACKAGECAR_URL + cardId + "/page/" + i;
                        Response res2 = ConnectionUtil.doGetWith(url, COOKIE, X_REQUESTED_WITH);

                        String body = res2.returnContent().asString();
                        Document docu = Jsoup.parseBodyFragment(body);

                        String trRegEx = "body > table > tbody > tr";
                        int trSize = WebClientUtil.getTagSize(docu, trRegEx, HtmlTag.trName);

                        if (trSize > 0) {
                            for (int j = 2; j <= trSize; j++) {

                                String carNumberRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(2)";
                                String nameRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(1) > a";
                                String phoneRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(3)";
                                String dateCreatedRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(4)";
                                String balanceRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(5)";
                                String clientIdRegEx = trRegEx + ":nth-child(" + j + ") > td:nth-child(1) > a";
                                String clientIdStr = docu.select(clientIdRegEx).attr("href");
                                String getClientIdRegEx = "(?<=id/)(.*)(?=/)";
                                String clientId = CommonUtil.fetchString(clientIdStr, getClientIdRegEx);

                                String carNumber = docu.select(carNumberRegEx).text();
                                String name = docu.select(nameRegEx).text();
                                String phone = docu.select(phoneRegEx).text();
                                String balance = docu.select(balanceRegEx).text();
                                String dateCreated = docu.select(dateCreatedRegEx).text();
                                dateCreated = DateUtil.formatSQLDateTime(dateCreated);

                                MemberCard memberCard = new MemberCard();
                                memberCard.setClientId(clientId);
                                memberCard.setCardSort(clientId);
                                memberCard.setCompanyName(companyName);
                                memberCard.setCardCode(carNumber);
                                memberCard.setCarNumber(carNumber);
                                memberCard.setName(name);
                                memberCard.setPhone(phone);
                                memberCard.setDateCreated(dateCreated);
                                memberCard.setBalance(balance);
                                memberCard.setMemberCardName(cardName);
                                memberCard.setValidTime(card.getValidTime());
                                memberCards.add(memberCard);
                            }
                        }
                    }
                }
            }
        }

        if (memberCards.size() > 0) {
            for (MemberCard memberCard : memberCards) {
                String clientId = memberCard.getClientId();

                String url = CARINFO_URL + clientId + "/";
                Response res = ConnectionUtil.doGetWith(url, COOKIE, X_REQUESTED_WITH);
                String body = res.returnContent().asString();
                Document docu = Jsoup.parse(body);

                String trRegEx = "#sale_table > div > div.widget-body > div > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(docu, trRegEx, HtmlTag.trName);

                if (trSize > 0) {
                    Elements elements = docu.select(trRegEx).tagName(HtmlTag.trName);
                    for (Element e : elements) {

                        if (e.hasClass("info") || e.hasClass("green"))
                            continue;

                        String tableRegEx = "table[class=table table-hover] > tbody > tr";
                        Elements trElements = e.select(tableRegEx);

                        for (Element element : trElements) {

                            if (element.hasClass("info"))
                                continue;

                            String itemNameRegEx = "td:nth-child(1)";
                            String originalNumRegEx = "td:nth-child(2)";
                            String numRegEx = "td:nth-child(4)";

                            String itemName = element.select(itemNameRegEx).text();
                            String originalNum = element.select(originalNumRegEx).text();
                            String num = element.select(numRegEx).text();

                            String validTime = memberCard.getValidTime();
                            String isValidForever = CommonUtil.getIsValidForever(validTime);

                            MemberCardItem memberCardItem = new MemberCardItem();
                            memberCardItem.setItemName(itemName);
                            memberCardItem.setOriginalNum(originalNum);
                            memberCardItem.setNum(num);
                            memberCardItem.setCompanyName(companyName);
                            memberCardItem.setCardCode(memberCard.getCardCode());
                            memberCardItem.setValidTime(validTime);
                            memberCardItem.setIsValidForever(isValidForever);
                            memberCardItems.add(memberCardItem);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\云店易会员卡.xls";
        String pathname2 = "C:\\exportExcel\\云店易卡内项目.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname2);
    }
}
