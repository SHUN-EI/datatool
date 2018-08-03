package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on  2018/8/2.
 * 车车云系统
 */
@Service
public class CheCheYunService {

    private String CARINFODETAIL_URL = "https://www.checheweike.com/crm/index.php?route=member/car/get&car_id=";

    private String CARINFO_URL = "https://www.checheweike.com/crm/index.php?route=member/car/gets&limit=20&order=DESC&sort=c.date_added&page=";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "count";

    private String COOKIE = "_bl_uid=U9jhCk23c20dCO8mwqRgavCnavav; PHPSESSID=u7ce3mahn04uu7grrmkhas0j83; ccwk_backend_tracking=u7ce3mahn04uu7grrmkhas0j83-10535; Hm_lvt_42a5df5a489c79568202aaf0b6c21801=1533202596,1533288090; Hm_lpvt_42a5df5a489c79568202aaf0b6c21801=1533288092; SERVERID=03485b53178f0de6cfb6b08218d57da6|1533288454|1533288038";

    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Map<String, CarInfo> carInfoMap = new HashMap<>();

        Response res = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("cars").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String carId = element.get("car_id").asText();
                    String carNumber = element.get("license").asText();
                    String name = element.get("name").asText();
                    String phone = element.get("mobile").asText();
                    String companyName = element.get("substore").asText();
                    String carModel = element.get("model").asText();
                    String VINCode = element.get("vin").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarNumber(carNumber);
                    carInfo.setName(name);
                    carInfo.setPhone(phone);
                    carInfo.setCompanyName(companyName);
                    carInfo.setCarModel(carModel);
                    carInfo.setBrand(carModel);
                    carInfo.setVINcode(VINCode);
                    carInfoMap.put(carId, carInfo);
                }
            }
        }

        if (carInfoMap.size()>0){
            for (String carId:carInfoMap.keySet()){
                res = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + carId + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                JsonNode data=result.get("car");

                String engineNumber=data.get("engine_number").asText();
                String vcInsuranceCompany=data.get("commmercial_insurance_company").asText();
                String tcInsuranceCompany=data.get("compulsory_insurance_company").asText();
                String vcInsuranceValidDate=data.get("date_commmercial_insurance_end").asText();
                String tcInsuranceValidDate=data.get("date_compulsory_insurance_end").asText();
                String registerDate=data.get("date_registered").asText();

                CarInfo carInfo=carInfoMap.get(carId);
                carInfo.setEngineNumber(engineNumber);
                carInfo.setVcInsuranceCompany(vcInsuranceCompany);
                carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                carInfo.setTcInsuranceCompany(tcInsuranceCompany);
                carInfo.setTcInsuranceValidDate(tcInsuranceValidDate);
                carInfo.setRegisterDate(registerDate);
                carInfos.add(carInfo);
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("结果为" + carInfos.size());

        String pathname = "C:\\exportExcel\\车车云车辆信息.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);

    }


}
