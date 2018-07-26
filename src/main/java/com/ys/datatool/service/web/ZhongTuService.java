package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * Created by mo on @date  2018/7/24.
 * <p>
 * 众途系统
 */
@Service
public class ZhongTuService {


    private String ITEM_URL = "http://crm.zhongtukj.com/Boss/Stock/Stockservice/StockShop.ashx";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "total";

    private String COOKIE = "ASP.NET_SessionId=slyqlmsdtyatk3yvwfmuzjpy; ztrjnew@4db97b96-12af-45b0-b232-fd1e9b7a672e=UserId=9Os8NGpT0Ts=&CSID=XM5LZ3LKOMA=&UserName=WHZqGZajveNSaHb4auidfA==&SID=TLlau79yBDk=&RoleId=lg86MnveZaI=&GroupId=xrWQau6DcFU=";


}
