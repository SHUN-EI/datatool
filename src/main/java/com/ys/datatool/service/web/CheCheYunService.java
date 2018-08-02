package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private String COOKIE = "_bl_uid=U9jhCk23c20dCO8mwqRgavCnavav; PHPSESSID=djqm08dvff13purp94qn9m9b56; ccwk_backend_tracking=djqm08dvff13purp94qn9m9b56-10535; Hm_lvt_42a5df5a489c79568202aaf0b6c21801=1533202596; Hm_lpvt_42a5df5a489c79568202aaf0b6c21801=1533202601; SERVERID=44fa044763f68345a9d119d26c10de1c|1533204331|1533202531";


    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response res = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(CARINFO_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("cars").iterator();
            }
        }

        System.out.println("结果为" + totalPage);

    }


}
