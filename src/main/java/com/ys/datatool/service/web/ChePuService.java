package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on 2019/4/14
 * 车仆系统
 */
@Service
public class ChePuService {

    private String MEMBERCARD_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-mcard-findmemberCard.do";

    private String CARINFODETAIL_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-member-findMember.do";

    private String CARINFO_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-members-findStoreMembers.do";

    private String SERVICE_URL = "https://dm.chiefchain.cn/mnt/CRUD/CRUD-Q-service-findServiceList.do";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Charset charset = Charset.forName("UTF-8");

    private String companyName = "车仆系统";

    private String fieldName = "result";

    private String COOKIE = "JSESSIONID=C751D33E6785FCA0B47C2BD6C53B7E79-n1";


    /**
     * 车辆信息、会员卡、卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        List<MemberCard> memberCards = new ArrayList<>();
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithJson(CARINFO_URL, getParam(0), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithJson(CARINFO_URL, getParam(i), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString(charset));

                JsonNode dataNode = result.get("result");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String carNumber = e.get("plateNumbers") != null ? e.get("plateNumbers").asText() : "";
                        String name = e.get("memberName") != null ? e.get("memberName").asText() : "";
                        String phone = e.get("mobileNumber") != null ? e.get("mobileNumber").asText() : "";
                        String carId = e.get("memberId").asText();

                        CarInfo carInfo = new CarInfo();
                        carInfo.setCarNumber(carNumber);
                        carInfo.setCarId(carId);
                        carInfo.setName(CommonUtil.formatString(name));
                        carInfo.setPhone(CommonUtil.formatString(phone));
                        carInfo.setCompanyName(companyName);
                        carInfos.add(carInfo);
                    }
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                String carId = carInfo.getCarId();

                //车辆信息
                Response res = ConnectionUtil.doPostWithJson(CARINFODETAIL_URL, "memberId=" + carId, COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString(charset));

                JsonNode resulNode = result.get("result");
                JsonNode node = resulNode.get("autos");

                if (node != null && node.size() > 0) {
                    JsonNode carNode = node.get(0);

                    String vin = carNode.get("vinCode").asText();
                    String engineNumber = carNode.get("engineNo").asText();
                    String vcInsuranceValidDate = carNode.get("biEdate").asText();
                    String carModel = carNode.get("autotypeName").asText();
                    String brand = carNode.get("automodelName").asText();
                    String tcInsuranceValidDate = carNode.get("ciEdate").asText();

                    carInfo.setVINcode(CommonUtil.formatString(vin));
                    carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                    carInfo.setVcInsuranceValidDate(vcInsuranceValidDate);
                    carInfo.setTcInsuranceValidDate(tcInsuranceValidDate);
                    carInfo.setCarModel(CommonUtil.formatString(carModel));
                    carInfo.setBrand(CommonUtil.formatString(brand));
                }

                //会员卡信息
                Response res2 = ConnectionUtil.doPostWithJson(MEMBERCARD_URL, "memberId=" + carId, COOKIE);
                JsonNode content = MAPPER.readTree(res2.returnContent().asString(charset));

                JsonNode dataNode = content.get("result");
                if (dataNode != null && dataNode.size() > 0) {
                    JsonNode memberNode = dataNode.get(0);

                    String cardCode = memberNode.get("mcardNo").asText();
                    String memberCardName = memberNode.get("mcardMame").asText();
                    String dateCreated = memberNode.get("startDate").asText();
                    String validTime = memberNode.get("stopDate").asText();
                    String balance = memberNode.get("mcardBalance").asText();
                    String remark = memberNode.get("cardStateName").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCompanyName(companyName);
                    memberCard.setCardCode(cardCode);
                    memberCard.setMemberCardName(memberCardName);
                    memberCard.setCarNumber(carInfo.getCarNumber());
                    memberCard.setDateCreated(dateCreated);
                    memberCard.setName(carInfo.getName());
                    memberCard.setBalance(balance);
                    memberCard.setValidTime(validTime);
                    memberCard.setPhone(carInfo.getPhone());
                    memberCard.setRemark(remark);
                    memberCards.add(memberCard);

                    //卡内项目
                    JsonNode itemNode = memberNode.get("svcs");
                    if (itemNode != null && itemNode.size() > 0) {
                        Iterator<JsonNode> it = itemNode.iterator();

                        while (it.hasNext()) {
                            JsonNode e = it.next();

                            String itemName = e.get("svcName").asText();
                            String num = e.get("svcNum").asText();
                            String price = e.get("svcPrice").asText();

                            MemberCardItem memberCardItem = new MemberCardItem();
                            memberCardItem.setCompanyName(companyName);
                            memberCardItem.setCardCode(cardCode);
                            memberCardItem.setItemName(itemName);
                            memberCardItem.setNum(num);
                            memberCardItem.setPrice(price);
                            memberCardItems.add(memberCardItem);
                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\车仆车辆.xls";
        String pathname2 = "C:\\exportExcel\\车仆会员.xls";
        String pathname3 = "C:\\exportExcel\\车仆卡内项目.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname2);
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname3);

    }


    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithJson(SERVICE_URL, getServiceParam(0), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, 15);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithJson(SERVICE_URL, getServiceParam(i), COOKIE);

                JsonNode result = MAPPER.readTree(response.returnContent().asString(charset));

                JsonNode dataNode = result.get("result");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String firstCategoryName = e.get("svctypePName").asText();
                        String secondCategoryName = e.get("svctypeName").asText();
                        String productName = e.get("serviceName").asText();
                        String price = e.get("goodsPrice").asText();
                        String code = e.get("serviceId").asText();
                        String brandName = e.get("servicePrice").asText();
                        String remark = e.get("memo").asText();

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setPrice(price);
                        product.setCode(code);
                        product.setBrandName(brandName);
                        product.setProductName(productName);
                        product.setItemType("服务项");
                        product.setFirstCategoryName(firstCategoryName);
                        product.setSecondCategoryName(secondCategoryName);
                        product.setRemark(remark);
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\车仆服务项目.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    private String getServiceParam(int pageNo) {
        String param = "row=15&stdsvcTag=1&page=" + pageNo;

        return param;
    }

    private String getParam(int pageNo) {
        String param = "pageSize=15&pageNo=" + pageNo;

        return param;
    }
}
