package com.ys.datatool.service.web;

import com.ys.datatool.domain.Bill;
import com.ys.datatool.util.DateUtil;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/11/16.
 * <p>
 * 车车汇系统
 */
@Service
public class CheCheHuiService {


    private String startTime="2013-01-01";

    private String BILL_URL = "http://shanghu.che01.cn/cch/cCHBusinessConnector?ac=orderMain&type=processed" +
            "&startTime=" +
            startTime +
            "&endTime=" +
            DateUtil.formatCurrentDate() +
            "&searchSid=&connector=true&dhx_no_header=1&count=100&posStart=";

    private String COOKIE = "JSESSIONID=358501228F582C7BFF1D39E915D2E263-n1; UM_distinctid=1671ba0b5f64f0-06f6a0bf115af9-4313362-144000-1671ba0b5f793";


    /**
     * 历史消费记录和消费记录相关车辆
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException {
        List<Bill> bills = new ArrayList<>();


    }


}
