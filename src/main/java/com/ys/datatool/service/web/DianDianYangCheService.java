package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.MemberCard;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/6/30.
 * 典典养车
 */
@Service
public class DianDianYangCheService {

    private String BILL_URL = "https://ndsm.ddyc.com/ndsm/work/getWorkPageList";

    private String CARINFODETAIL_URL = "https://ndsm.ddyc.com/ndsm/car/carDetails?carInfoId=";

    private String CARINFO_URL = "https://ndsm.ddyc.com/ndsm/car/getShopCarList";

    private String MEMBERCARD_URL = "https://ndsm.ddyc.com/ndsm/member/list";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Workbook workbook;

    private String fileName = "典典养车";

    private int num = 10;

    private static final String COOKIE = "JSESSIONID=DE5229B3A6017A4A44326542941B12D8; gr_user_id=5b4ec60a-f3cd-4586-a294-73c73b41a61b; gr_session_id_e2f213a5f5164248817464925de8c1af=55746d30-1c66-4a59-bbc6-6a9ac03d7503; gr_session_id_e2f213a5f5164248817464925de8c1af_55746d30-1c66-4a59-bbc6-6a9ac03d7503=true";


    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getTotalParams("1"), COOKIE);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());
        String totalStr = result.get("data").get("totalPage").asText();
        int total = Integer.parseInt(totalStr);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                Response res = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getTotalParams(String.valueOf(i)), COOKIE);
                result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").get("data").elements();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carId = element.get("carInfoId").asText();
                    String carNumber = element.get("carNumber").asText();
                    String phone = element.get("phone").asText();
                    String name = element.get("carOwnerName").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                    carInfo.setName(CommonUtil.formatString(name));
                    carInfo.setPhone(CommonUtil.formatString(phone));

                    Response res2 = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + carId, COOKIE);
                    JsonNode content = MAPPER.readTree(res2.returnContent().asString());

                    if (content.hasNonNull("data") == true) {
                        JsonNode data = content.get("data");
                        String brand = data.get("carBrandName").asText();
                        String carModel = data.get("carModelName").asText();
                        String vin = data.get("vinCode").asText();

                        carInfo.setBrand(CommonUtil.formatString(brand));
                        carInfo.setCarModel(CommonUtil.formatString(carModel));
                        carInfo.setVINcode(CommonUtil.formatString(vin));
                    }
                    carInfos.add(carInfo);
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());

        String pathname = "C:\\exportExcel\\典典养车车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);


    }

    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, getParam(1), COOKIE);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());
        JsonNode totalNode = result.get("data").get("total");
        int totalPage = WebClientUtil.getTotalPage(totalNode, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, getParam(i), COOKIE);
                result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("data").get("data").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardName(element.get("memberId").asText());
                    memberCard.setName(element.get("name").asText());
                    memberCard.setPhone(element.get("phone").asText());
                    memberCard.setBalance(element.get("asset").get("balance").asText());

                    JsonNode carNode = element.get("carList");
                    if ("null" != carNode.asText())
                        memberCard.setCarNumber(carNode.get(0).get("plateNumber").asText());

                    memberCards.add(memberCard);
                }
            }
        }

        System.out.println("总页数为" + totalPage);
        System.out.println("总数为" + memberCards.size());
        System.out.println("结果为" + memberCards.toString());

    }

    private List<BasicNameValuePair> getTotalParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("pageSize", "10"));
        params.add(new BasicNameValuePair("pageNumber", pageNo));
        return params;
    }

    private String getParam(int index) {
        String page = String.valueOf(index);
        String param = "{" + "\"page\":" + page + "," + "\"pageSize\":" + "10" + "}";

        return param;
    }
}
