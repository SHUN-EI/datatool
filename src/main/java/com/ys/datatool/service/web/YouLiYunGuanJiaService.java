package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.CarInfo;
import com.ys.datatool.domain.entity.MemberCard;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on 2019/3/4
 * 有礼云管家系统
 */
@Service
public class YouLiYunGuanJiaService {


    private String MEMBERCARD_URL = "http://ls.4008778515.com/Report/dlmemberlist";

    private String CARINFODETAIL_URL = "http://ls.4008778515.com/Report/Carinfoedit?ShopID=undefined&ID=";

    private String CARINFO_URL = "http://ls.4008778515.com/Report/Carinfolist";

    private String companyName = "有礼云管家";

    private String fieldName = "recordsTotal";

    private int num = 10;

    private String COOKIE = "shopsernum=undefined; username=18022578558; password=huang888; yunsuo_session_verify=f6f70c3d3d9475acd4f40fd0a1b480b4; PHPSESSID=ndh3d86uiaffscvj8ufa0858f1";



    /**
     * 会员信息
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, getParam(0), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 10);

        if (totalPage > 0) {
            int start = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, getParam(start), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                start = start + num;

                JsonNode dataNode = result.get("data");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.elements();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String id = e.get("ID").asText();
                        String phone = e.get("Phone").asText();
                        String name = e.get("Name").asText();
                        String cardCode = e.get("MemberCard").asText();
                        String cardName = e.get("RankName").asText();
                        String balance = e.get("Balance").asText();
                        String give = e.get("Give").asText();

                        MemberCard memberCard = new MemberCard();
                        memberCard.setCompanyName(companyName);
                        memberCard.setCardCode(cardCode);
                        memberCard.setName(name);
                        memberCard.setPhone(phone);
                        memberCard.setMemberCardName(cardName);
                        memberCard.setBalance(balance);
                        memberCard.setCardSort(give);
                        memberCards.add(memberCard);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\有礼云管家会员卡.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithJson(CARINFO_URL, getParam(0), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 10);

        if (totalPage > 0) {
            int start = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithJson(CARINFO_URL, getParam(start), COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                start = start + num;
                JsonNode dataNode = result.get("data");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.elements();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String id = e.get("ID").asText();
                        String carNumber = e.get("RegistrationNum").asText();
                        String phone = e.get("Phone").asText();
                        String name = e.get("Name").asText();
                        String code = e.get("Codes").asText();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCompanyName(companyName);
                        carInfo.setCarId(id);
                        carInfo.setCarNumber(carNumber);
                        carInfo.setPhone(phone);
                        carInfo.setName(name);
                        carInfo.setCarCode(code);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String carId = carInfo.getCarId();

                Response res = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + carId, COOKIE);
                String content = res.returnContent().asString(WebConfig.CHARSET_UTF_8);

                Document doc = Jsoup.parseBodyFragment(content);

                String brandRegEx = "#CarBrand";
                String modelRegEx = "#CarBrandModel";
                String engineNumberRegEx = "#EngineNum";
                String vinRegEx = "#VINumber";
                String vcInsuranceValidDateRegEx = "#tab-2 > div > div > div > div > div:nth-child(1) > div:nth-child(2) > input";
                String tcInsuranceValidDateRegEx = "#tab-2 > div > div > div > div > div:nth-child(1) > div:nth-child(3) > input";

                String brand = doc.select(brandRegEx).attr("value");
                String model = doc.select(modelRegEx).attr("value");
                String engineNumber = doc.select(engineNumberRegEx).attr("value");
                String vin = doc.select(vinRegEx).attr("value");
                String vcInsuranceValidDate = doc.select(vcInsuranceValidDateRegEx).attr("value");
                String tcInsuranceValidDate = doc.select(tcInsuranceValidDateRegEx).attr("value");

                carInfo.setBrand(brand);
                carInfo.setCarModel(model);
                carInfo.setEngineNumber(engineNumber);
                carInfo.setVINcode(vin);
                carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                carInfo.setTcInsuranceValidDate(tcInsuranceValidDate);
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("大小为" + carInfos.size());

        String pathname = "C:\\exportExcel\\有礼云管家车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }


    private String getParam(int pageNo) {
        String param = "length=10&start=" + pageNo;

        return param;
    }

}