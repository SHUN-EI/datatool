package com.ys.datatool.service.web;

import com.ys.datatool.domain.HtmlTag;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

/**
 * Created by mo on @date  2018/12/5.
 * <p>
 * 云店易系统2.2
 */

@Service
public class YunDianYiService {

    private String PACKAGECAR_URL = "https://vip.yundianyi.com/promotion/ajaxpackageshow/id/";

    private String PACKAGEPAGE_URL = "https://vip.yundianyi.com/promotion/package/";

    private String PACKAGELIST_URL = "https://vip.yundianyi.com/promotion/getpackagepage/type/enable/page/";

    private String COOKIE = "PHPSESSID=q04a28a91hbf8rciq0jusmp6g0; ace_settings=%7B%22sidebar-collapsed%22%3A1%7D; SERVERID=0bb7dc3a32edb4645acc8c025bd1047b|1544007351|1544007346";


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

        Response response = ConnectionUtil.doGetWithLeastParams(PACKAGEPAGE_URL, COOKIE);
        String html = response.returnContent().asString();
        Document document = Jsoup.parse(html);

        String totalRegEx = "#enablePackage > div.text-center > ul > li:nth-child(3) > input";
        String totalPageStr = document.select(totalRegEx).attr("data-max-page");
        int total = Integer.parseInt(totalPageStr);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {

                String aaa=PACKAGELIST_URL + i+"";
                Response res = ConnectionUtil.doGetWithLeastParams(PACKAGELIST_URL + i + "", COOKIE);

                html = res.returnContent().asString();
                Document doc = Jsoup.parse(html);

                String trRegEx = "#enable_table > div > div > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trRegEx, HtmlTag.trName);

                String ssss = "";

            }
        }

        String sss = "";


    }
}
