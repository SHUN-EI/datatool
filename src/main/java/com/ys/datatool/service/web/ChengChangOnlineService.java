package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.*;
import org.apache.http.client.fluent.Response;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mo on 2019/4/28
 * 橙长在线系统
 */
@Service
public class ChengChangOnlineService {


    /////////////////////////////////工具使用前，请先填写COOKIE数据////////////////////////////////////////////////////////////////////////



    private String COOKIE = "ASP.NET_SessionId=1nwarb1cukj0yevhowdygzhl; ShopAdminUser=uId=4713DC16B7C0D7E3&uType=&uName=B95EA3713D5D4337D61C03D0B14A72F0&uPhone=B63E7262FDF19C2B3079616824B1C652&uStag=A1050A9334DD35BC34C6D80EBF37313750062C62D2A4DDB565B869777C6E6AB39A4CE2530BA354E0&uLastLogin=2019/5/4 15:29:19&uLoginIp=61.140.95.110&uLoginCount=761&uLoginNowTime=1557025017074&uIp=ED6B9AA7C71BFC642EBCC9025DBD7136&Expires=1557457017074";



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private String MEMBERCARDITEMDETAIL_URL = "http://guanjia.cz888.com/Api/User/GetBatchEntity";

    private String MEMBERCARDITEM_URL = "http://guanjia.cz888.com/Api/User/BatchSearch";

    private String MEMBERCARD_URL = "http://guanjia.cz888.com/Api/Shop/UserInfoSearch";

    private String CATEGORY_URL = "http://guanjia.cz888.com/Api/Common/GetProductCate";

    private String SERVICE_URL = "http://guanjia.cz888.com/Api/Product/ProductSearch?Pcid=0&cid=0&Type=2";

    private String ITEM_URL = "http://guanjia.cz888.com/Api/Product/ProductSearch?Pcid=0&cid=0&Type=1";

    private String fieldName = "Count";

    private String companyName = "橙长在线";



    /**
     * 套餐卡及卡内项目
     * 打开路径:会员管理-会员套餐
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        List<MemberCardItem> memberCardItems = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(MEMBERCARDITEM_URL, getMemberCardItemParam(1), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 20);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(MEMBERCARDITEM_URL, getMemberCardItemParam(i), COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode dataNode = result.get("List");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String carNumber = e.get("CarNo").asText();
                        String userId = e.get("UserBatchId").asText();
                        String memberCardName = e.get("Name").asText();
                        String cardCode = e.get("Id").asText();

                        String effectEndDate = e.get("EffectEndDate").asText();
                        String regEx = "(?<=\\()[^\\)]+";
                        String validTimeStr = CommonUtil.fetchString(effectEndDate, regEx);
                        String validTime = DateUtil.formatMillisecond2DateTime(validTimeStr);

                        System.out.println("结果为" + carNumber);
                        String userName = e.get("UserName").asText();
                        int index = userName.indexOf("-");
                        String name = userName.substring(0, index);
                        String phone = userName.substring(index + 1, userName.length());

                        MemberCard memberCard = new MemberCard();
                        memberCard.setCardCode(userId);
                        memberCard.setCompanyName(companyName);
                        memberCard.setPhone(phone);
                        memberCard.setName(name);
                        memberCard.setCarNumber(carNumber);
                        memberCard.setMemberCardName(memberCardName);
                        memberCard.setValidTime(validTime);
                        memberCard.setMemberCardId(userId);
                        memberCards.add(memberCard);
                    }
                }
            }
        }

        if (memberCards.size() > 0) {
            for (MemberCard memberCard : memberCards) {

                String param = "{" + "ubid:" + memberCard.getMemberCardId() + "}";
                Response res = ConnectionUtil.doPostWithLeastParamJson(MEMBERCARDITEMDETAIL_URL, param, COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));
                JsonNode dataNode = result.get("data");

                if (dataNode != null) {

                    String effectBeginDate = dataNode.get("EffectBeginDate").asText();
                    String regEx = "(?<=\\()[^\\)]+";
                    String startTimeStr = CommonUtil.fetchString(effectBeginDate, regEx);
                    String dateCreated = DateUtil.formatMillisecond2DateTime(startTimeStr);
                    memberCard.setDateCreated(dateCreated);


                    JsonNode details = dataNode.get("Details");
                    if (details.size() > 0) {
                        Iterator<JsonNode> it = details.iterator();

                        while (it.hasNext()) {
                            JsonNode e = it.next();

                            String itemName = e.get("ProductName").asText();
                            String price = e.get("ServicePrice").asText();
                            String originalNumStr = e.get("Qty").asText();
                            String userNumStr = e.get("HasUsedQty").asText();

                            String isValidForever = CommonUtil.getIsValidForever(memberCard.getValidTime());

                            BigDecimal originalNum = new BigDecimal(originalNumStr);
                            BigDecimal userNum = new BigDecimal(userNumStr);
                            BigDecimal num = originalNum.subtract(userNum);

                            MemberCardItem memberCardItem = new MemberCardItem();
                            memberCardItem.setCardCode(memberCard.getMemberCardId());
                            memberCardItem.setItemName(itemName);
                            memberCardItem.setCompanyName(companyName);
                            memberCardItem.setPrice(price);
                            memberCardItem.setOriginalNum(originalNumStr);
                            memberCardItem.setNum(num.toString());
                            memberCardItem.setValidTime(memberCard.getValidTime());
                            memberCardItem.setIsValidForever(isValidForever);
                            memberCardItems.add(memberCardItem);
                        }
                    }
                }
            }
        }


        String pathname = "C:\\exportExcel\\橙长在线套餐卡.xls";
        String pathname2 = "C:\\exportExcel\\橙长在线卡内项目.xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);
        ExportUtil.exportMemberCardItemSomeFieldDataInLocal(memberCardItems, ExcelDatas.workbook, pathname2);

    }


    /**
     * 会员卡
     * 打开路径:会员管理-会员列表
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParamJson(MEMBERCARD_URL, getMemberCardParam(1), COOKIE);

        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 20);
        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(MEMBERCARD_URL, getMemberCardParam(i), COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode dataNode = result.get("List");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String id = e.get("Id").asText();
                        String name = e.get("ZhName").asText();
                        String phone = e.get("Phone").asText();
                        String carNumber = e.get("CarNos").asText();
                        String balance = e.get("Balance").asText();
                        String remark = e.get("CashCardInfoJson").asText();

                        MemberCard memberCard = new MemberCard();
                        memberCard.setName(name);
                        memberCard.setPhone(phone);
                        memberCard.setCarNumber(CommonUtil.formatString(carNumber));
                        memberCard.setBalance(balance);
                        memberCard.setCompanyName(companyName);
                        memberCard.setCardCode(id);
                        memberCard.setRemark(CommonUtil.formatString(remark));
                        memberCards.add(memberCard);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\橙长在线会员卡.xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);

    }


    /**
     * 商品
     * 打开路径:店铺管理-产品列表
     *
     * @throws IOException
     */
    @Test
    public void fetchItemData() throws IOException {
        List<Product> products = new ArrayList<>();

        List<FirstCategory> firstCategories = fetchFirstCategoryData();
        List<SecondCategory> secondCategories = fetchSecondCategoryData();

        Response response = ConnectionUtil.doPostWithLeastParamJson(ITEM_URL, getParam(1), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(ITEM_URL, getParam(i), COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode dataNode = result.get("List");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String productName = e.get("Name").asText();
                        String barCode = e.get("VenderSku").asText();
                        String price = e.get("Price").asText();
                        String categoryName = e.get("CatName").asText();
                        String unit = e.get("Unit").asText();

                        String cid = e.get("Cid").asText();

                        String firstCategoryName = "";
                        List<SecondCategory> secondCategoryList = secondCategories.stream()
                                .filter(secondCategory -> cid.equals(secondCategory.getSid()))
                                .collect(Collectors.toList());

                        if (secondCategoryList.size() > 0) {
                            firstCategoryName = secondCategoryList.get(0).getFirstCategory().getName();
                        } else {
                            FirstCategory fCategory = firstCategories.stream().filter(firstCategory -> cid.equals(firstCategory.getFid())).collect(Collectors.toList()).get(0);
                            firstCategoryName = fCategory.getName();
                        }

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(productName);
                        product.setItemType("服务项");
                        product.setBarCode(barCode);
                        product.setCode(barCode);
                        product.setFirstCategoryName(firstCategoryName);
                        product.setSecondCategoryName(categoryName);
                        product.setPrice(price);
                        product.setUnit(unit);
                        products.add(product);
                    }
                }
            }
        }


        String pathname = "C:\\exportExcel\\橙长在线商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);

    }

    /**
     * 服务项目
     * <p>
     * 打开路径:店铺管理-服务列表
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        List<FirstCategory> firstCategories = fetchFirstCategoryData();
        List<SecondCategory> secondCategories = fetchSecondCategoryData();

        Response response = ConnectionUtil.doPostWithLeastParamJson(SERVICE_URL, getParam(1), COOKIE);

        int totalPage = WebClientUtil.getTotalPage(response, JsonObject.MAPPER, fieldName, 20);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParamJson(SERVICE_URL, getParam(i), COOKIE);

                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                JsonNode dataNode = result.get("List");
                if (dataNode.size() > 0) {
                    Iterator<JsonNode> it = dataNode.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String productName = e.get("Name").asText();
                        String barCode = e.get("VenderSku").asText();
                        String price = e.get("Price").asText();
                        String categoryName = e.get("CatName").asText();
                        String unit = e.get("Unit").asText();

                        System.out.println("服务名称为" + productName);
                        String cid = e.get("Cid").asText();

                        String firstCategoryName = "";
                        List<SecondCategory> secondCategoryList = secondCategories.stream()
                                .filter(secondCategory -> cid.equals(secondCategory.getSid()))
                                .collect(Collectors.toList());

                        if (secondCategoryList.size() > 0) {
                            firstCategoryName = secondCategoryList.get(0).getFirstCategory().getName();
                        } else {
                            FirstCategory fCategory = firstCategories.stream().filter(firstCategory -> cid.equals(firstCategory.getFid())).collect(Collectors.toList()).get(0);
                            firstCategoryName = fCategory.getName();
                        }

                        Product product = new Product();
                        product.setCompanyName(companyName);
                        product.setProductName(productName);
                        product.setItemType("服务项");
                        product.setBarCode(barCode);
                        product.setCode(barCode);
                        product.setFirstCategoryName(firstCategoryName);
                        product.setSecondCategoryName(categoryName);
                        product.setPrice(price);
                        product.setUnit(unit);
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\橙长在线服务.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);

    }


    /**
     * 服务一级分类
     *
     * @throws IOException
     */
    private List<FirstCategory> fetchFirstCategoryData() throws IOException {
        List<FirstCategory> firstCategories = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithoutParam(CATEGORY_URL, COOKIE);

        JsonNode content = JsonObject.MAPPER.readTree(res.returnContent().asString(WebConfig.CHARSET_UTF_8));
        if (content.size() > 0) {
            Iterator<JsonNode> it = content.iterator();

            while (it.hasNext()) {
                JsonNode e = it.next();

                String id = e.get("Id").asText();
                String name = e.get("Name").asText();

                FirstCategory firstCategory = new FirstCategory();
                firstCategory.setFid(id);
                firstCategory.setName(name);
                firstCategories.add(firstCategory);
            }
        }
        return firstCategories;
    }

    /**
     * 服务二级分类
     *
     * @throws IOException
     */
    private List<SecondCategory> fetchSecondCategoryData() throws IOException {
        List<SecondCategory> secondCategories = new ArrayList<>();

        List<FirstCategory> firstCategories = fetchFirstCategoryData();
        if (firstCategories.size() > 0) {
            for (FirstCategory firstCategory : firstCategories) {

                String param = "{" + "id:" + firstCategory.getFid() + "}";
                Response response = ConnectionUtil.doPostWithLeastParamJson(CATEGORY_URL, param, COOKIE);
                JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));

                if (result.size() > 0) {
                    Iterator<JsonNode> it = result.iterator();

                    while (it.hasNext()) {
                        JsonNode e = it.next();

                        String id = e.get("Id").asText();
                        String name = e.get("Name").asText();

                        SecondCategory secondCategory = new SecondCategory();
                        secondCategory.setSid(id);
                        secondCategory.setName(name);
                        secondCategory.setFirstCategory(firstCategory);
                        secondCategories.add(secondCategory);
                    }
                }
            }
        }
        return secondCategories;
    }


    private String getMemberCardItemParam(int pageNo) {
        String param = "{pn:" +
                pageNo +
                ", Id:0, PageSize: 20 }";

        return param;
    }

    private String getMemberCardParam(int pageNo) {
        String param = "{pn:" +
                pageNo +
                ", PageSize: 20" +
                ", SearchShopId:12" +
                ", Type:0 }";

        return param;
    }

    private String getParam(int pageNo) {
        String param = "{pn:" +
                pageNo +
                ",PageSize: 20" +
                ",DeptId:12 }";

        return param;
    }

}
