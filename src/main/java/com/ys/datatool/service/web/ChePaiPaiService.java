package com.ys.datatool.service.web;

import com.ys.datatool.util.ConnectionUtil;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by mo on @date  2018/6/15.
 */
public class ChePaiPaiService {

    private String TEMPCLIENT_URL="http://vip.chepaipai.com.cn/index.php?m=client&a=temporary&pno=";

    private Charset charset = Charset.forName("UTF-8");

    private String COOKIE = "CARPP_STORENO_C=13318336333; CARPP_USERNAME_C=%E6%A2%81%E8%95%B4%E7%91%9C; UM_distinctid=16402abc71f998-03ecab8f5d6da6-5e452019-144000-16402abc72058a; CNZZDATA1262768147=1641429679-1529052741-http%253A%252F%252Fvip.chepaipai.com.cn%252F%7C1529052741; PHPSESSID=6anmdjd55fkobqvqouv32m8c06; SERVERID=fb46de91a7276047e33f515298cae466|1529054530|1529054405";

    @Test
    public void fetchTempClientData() throws IOException{

        Response res = ConnectionUtil.doGetWithLeastParams(TEMPCLIENT_URL+"1",COOKIE);
        String html = res.returnContent().asString(charset);
        Document doc = Jsoup.parse(html);

        System.out.println("结果为"+doc.html());

    }
}
