package com.ys.datatool.service.web;

import com.ys.datatool.domain.config.HtmlTag;
import com.ys.datatool.domain.entity.CarInfo;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mo on @date  2018/6/30.
 * 途虎养车系统
 */
@Service
public class TuHuService {


    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////


    private static final String COOKIE = "_fmdata_token=cbcb184b-ab01-44ef-91ea-dd223fb5417f; ASP.NET_SessionId=htk2bkg5xpe4tqi01qggq3ej; .TUHU.SHOP=274272E80D169E5C038918137E0124CC48089DFDB44382E99C480CF3C60F4FA9732EA10DCF0ABBA4E9ADCA2BBA16059CA0CD147BA818AD72E637A97BE242DB558757EF0BF8727D42419E8AFF; ShopsManageSystem=UserName=dm-23057&InstallShopID=23057&InstallShopName=%e9%80%94%e8%99%8e%e5%85%bb%e8%bd%a6%e5%b7%a5%e5%9c%ba%e5%ba%97%ef%bc%88%e5%b9%bf%e5%b7%9e%e5%a4%a7%e7%9f%b3%e8%b7%af%e5%ba%97%ef%bc%89; _fmdata=A0458BDF1E9624CE9D36E53C8B600E384B5683295C41D2680D4EA97831EBCE99C3EC8713F00D9B6574C987283237DFD93B70E408DF8C37AD; NTKF_T2D_CLIENTID=guest17047CA4-8154-95B1-1408-C07549B85A9B; nTalk_CACHE_DATA={uid:kf_9739_ISME9754_dm-23057-ba7fa2f6-5ff6-4e96-a2be-5579493ce802,tid:1515335019940585}";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static final String CARINFODETAIL_URL = "https://s.tuhu.cn{index}";

    private static final String CARINFO_URL = "https://s.tuhu.cn/Customer/CustomerList/?UserName=&UserTel=&PageIndex={page}";

    private String fileName = "途虎养车";

    private String trCarInfoRegEx = " table > tbody > tr";

    private String trCarInfoDetailRegEx = "#customer-dat-info > table > tbody > tr";



    /**
     * 车辆信息
     *
     * @throws IOException
     */
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Set<String> indexSet = new HashSet<>();

        Response response = ConnectionUtil.doGetWith(StringUtils.replace(CARINFO_URL, "{page}", "1"), COOKIE);
        String html = response.returnContent().asString();
        Document document = Jsoup.parse(html);

        String totalStr = document.getElementsByClass("last-child").prev("a").text();
        int total = Integer.parseInt(totalStr);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                response = ConnectionUtil.doGetWith(StringUtils.replace(CARINFO_URL, "{page}", String.valueOf(i)), COOKIE);
                html = response.returnContent().asString();
                document = Jsoup.parse(html);

                int trSize = WebClientUtil.getTagSize(document, trCarInfoRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int j = 1; j <= trSize; j++) {
                        String carInfoNoRegEx = "table > tbody > tr:nth-child(" + j + ") > td:nth-child(8) > a";
                        String index = document.select(carInfoNoRegEx).attr("href");
                        indexSet.add(index);
                    }
                }
            }
        }

        if (indexSet.size() > 0) {
            for (String index : indexSet) {
                //String index = "/Customer/CustomerDetails?userGuid={2416559e-1191-4abf-bcc7-3a286bcd4cba}";
                //String a = StringUtils.replace(CARINFODETAIL_URL, "{index}", index);

                Response r = ConnectionUtil.doGetWith(enCodeURL(StringUtils.replace(CARINFODETAIL_URL, "{index}", index)), COOKIE);
                html = r.returnContent().asString();
                document = Jsoup.parse(html);

                String nameRegEx = "#customer-info-table > tbody > tr:nth-child(1) > td:nth-child(2)";
                String name = document.select(nameRegEx).text();

                String phoneRegEx = "#customer-info-table > tbody > tr:nth-child(2) > td:nth-child(2)";
                String phone = document.select(phoneRegEx).text();

                int trSize = WebClientUtil.getTagSize(document, trCarInfoDetailRegEx, HtmlTag.trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {

                        String carNumberRegEx = "#customer-dat-info > table > tbody > tr:nth-child(" + i + ") > td:nth-child(1)";
                        String brandRegEx = "#customer-dat-info > table > tbody > tr:nth-child(" + i + ") > td:nth-child(2)";
                        String carModelRegEx = "#customer-dat-info > table > tbody > tr:nth-child(" + i + ") > td:nth-child(3)";
                        String VINcodeRegEx = "#customer-dat-info > table > tbody > tr:nth-child(" + i + ") > td:nth-child(8)";
                        String engineNumberRegEx = "#customer-dat-info > table > tbody > tr:nth-child(" + i + ") > td:nth-child(10)";
                        String mileageRegEx = "#customer-dat-info > table > tbody > tr:nth-child(" + i + ") > td:nth-child(9)";   //网页车架号

                        String carNumber = document.select(carNumberRegEx).text();
                        String brand = document.select(brandRegEx).text();
                        String carModel = document.select(carModelRegEx).text();
                        String VINcode = document.select(VINcodeRegEx).text();
                        String engineNumber = document.select(engineNumberRegEx).text();
                        String mileage = document.select(mileageRegEx).text();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setBrand(brand);
                        carInfo.setCarModel(carModel);
                        carInfo.setVINcode(VINcode);
                        carInfo.setEngineNumber(engineNumber);
                        carInfo.setName(name);
                        carInfo.setPhone(phone);
                        carInfo.setMileage(mileage);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

    }

    //encodeURL
    private String enCodeURL(String url) {
        String tempURL = url.replace("{", "%7B");
        String realURL = tempURL.replace("}", "%7D");
        return realURL;
    }
}
