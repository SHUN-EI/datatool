package com.ys.datatool.service.web;

import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/6/15.
 * 车拍拍系统
 */
@Service
public class ChePaiPaiService {

    private String TEMPCLIENT_URL = "http://vip.chepaipai.com.cn/index.php?m=client&a=temporary&pno=";
 
    private Charset charset = Charset.forName("UTF-8");

    private String totalRegEx = "totalPage = .*";

    private String trName = "tr";

    private String COOKIE = "CARPP_STORENO_C=13318336333; CARPP_USERNAME_C=%E6%A2%81%E8%95%B4%E7%91%9C; UM_distinctid=16402abc71f998-03ecab8f5d6da6-5e452019-144000-16402abc72058a; PHPSESSID=aelcva0p14h7s3tg1i98q4nj41; CNZZDATA1262768147=1641429679-1529052741-http%253A%252F%252Fvip.chepaipai.com.cn%252F%7C1530354693; SERVERID=ddb645d5daaa3821e0564fcaa077d0d0|1530355911|1530355897";

    /**
     * 临时客户
     * @throws IOException
     */
    @Test
    public void fetchTempClientData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Response res = ConnectionUtil.doGetWithLeastParams(TEMPCLIENT_URL + "1", COOKIE);
        String html = res.returnContent().asString(charset);
        Document doc = Jsoup.parse(html);

        String totalPageStr = CommonUtil.fetchString(doc.toString(), totalRegEx).replace("totalPage = ", "");
        int totalPage = Integer.parseInt(totalPageStr.replace(";", "").trim());

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(TEMPCLIENT_URL + String.valueOf(i), COOKIE);
                html = res.returnContent().asString();
                doc = Jsoup.parse(html);

                String trItemRegEx = "body > div.sys_main.clearfix > div.rightBox > div > div.dengji > table > tbody > tr";
                int trSize = WebClientUtil.getTagSize(doc, trItemRegEx,trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {

                        String nameRegEx = "body > div.sys_main.clearfix > div.rightBox > div > div.dengji > table > tbody > tr:nth-child({no}) > td:nth-child(1)";
                        String phoneRegEx = "body > div.sys_main.clearfix > div.rightBox > div > div.dengji > table > tbody > tr:nth-child({no}) > td:nth-child(2)";
                        String carNumberRegEx = "body > div.sys_main.clearfix > div.rightBox > div > div.dengji > table > tbody > tr:nth-child({no}) > td:nth-child(3) > a";

                        String name = doc.select(StringUtils.replace(nameRegEx, "{no}", String.valueOf(j))).text();
                        String phone = doc.select(StringUtils.replace(phoneRegEx, "{no}", String.valueOf(j))).text();
                        String carNumber = doc.select(StringUtils.replace(carNumberRegEx, "{no}", String.valueOf(j))).text();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setCarNumber(carNumber);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("carInfos大小为" + carInfos.size());

        String pathname = "C:\\exportExcel\\车拍拍临时客户.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }
}
