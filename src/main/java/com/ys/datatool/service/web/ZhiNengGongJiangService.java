package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018/11/12.
 * 智能工匠系统
 */
@Service
public class ZhiNengGongJiangService {

    private String dc = "1542003069386";

    private String CARINFO_URL = "http://z1001.cn/member/queryMemberPage.atc?_dc=" +
            dc +
            "&page={no}&limit=500&start=";

    private String CARINFODETAIL_URL = "http://z1001.cn/member/queryCarByMemberId.atc?_dc=" +
            dc +
            "&page=1&start=0&limit=25&memberId=";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private int num = 500;

    private String fieldName = "totalCount";

    private String companyName = "智能工匠系统";

    private String COOKIE = "JSESSIONID=B58B2EDE378DF0A30A56C96B5BFCA4E0; login.userName=%u97E9%u5609%u7EA2; Hm_lvt_1342037efbd12977a0de3d64429d52ed=1541998699; Hm_lpvt_1342037efbd12977a0de3d64429d52ed=1542005119";


    /**
     * 车辆信息-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Map<String, CarInfo> carInfoMap = new HashMap<>();

        Response response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(CARINFO_URL, "{no}", "1") + 0, COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int start = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(CARINFO_URL, "{no}", String.valueOf(i)) + start, COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                start = start + num;
                Iterator<JsonNode> it = result.get("memberList").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String memberId = element.get("memId").asText();
                    String source = element.get("source").asText();
                    String name = element.get("name").asText();
                    String phone = element.get("mobile").asText();

                    JsonNode carList = element.get("carList");
                    if (carList != null && carList.size() > 0) {
                        Iterator<JsonNode> content = carList.iterator();
                        while (content.hasNext()) {
                            JsonNode e = content.next();

                            String carShort = e.get("carShort").asText();
                            String carCode = e.get("carCode").asText();
                            String carNum = e.get("carNumber").asText();
                            String carNumber = carShort + carCode + carNum;

                            String carId = e.get("id").asText();
                            String model = e.get("model").asText();
                            String carBrand = e.get("brandName").asText();
                            String vin = e.get("carFrame").asText();
                            String engineNumber = e.get("carEngine").asText();
                            String vcInsuranceCompany = e.get("carInsuCompany").asText();
                            String vcInsuranceValidDate = e.get("carInsurance").asText();

                            CarInfo carInfo = new CarInfo();
                            carInfo.setCompanyName(companyName);
                            carInfo.setCarNumber(carNumber);
                            carInfo.setName(CommonUtil.formatString(name));
                            carInfo.setPhone(CommonUtil.formatString(phone));
                            carInfo.setBrand(CommonUtil.formatString(carBrand));
                            carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                            carInfo.setCarModel(CommonUtil.formatString(model));
                            carInfo.setVINcode(CommonUtil.formatString(vin));
                            carInfo.setVcInsuranceCompany(CommonUtil.formatString(vcInsuranceCompany));
                            carInfo.setVcInsuranceValidDate(CommonUtil.formatString(vcInsuranceValidDate));
                            carInfos.add(carInfo);
                            carInfoMap.put(carId, carInfo);

                            Response res = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + memberId, COOKIE);
                            JsonNode data = MAPPER.readTree(res.returnContent().asString());

                            Iterator<JsonNode> iterator = data.get("carList").iterator();
                            while (iterator.hasNext()) {
                                JsonNode node = iterator.next();
                                String id = node.get("id").asText();
                                String brand = node.get("brandName").asText();

                                if (carInfoMap.get(id) != null) {
                                    CarInfo car = carInfoMap.get(id);
                                    car.setBrand(CommonUtil.formatString(brand));
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());

        String pathname = "C:\\exportExcel\\智能工匠车辆信息.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }
}
