package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018-06-12.
 * 4C系统-后市场管理系统
 */

@Service
public class FourCService {

    private String CARINFO_URL = "http://www.car-cloud.cn/Basic/Car/LoadData";

    private String CLIENT_URL = "http://www.car-cloud.cn/Wy/WyCarOwner/LoadData";

    private String SERVICE_URL = "http://www.car-cloud.cn/Wy/WyArticle/LoadData";

    private String RECHARGERECORD_URL = "http://www.car-cloud.cn/Wy/WyBilling/LoadCarOwnerRechargeRecord";

    private String BUYPACKAGES_URL = "http://www.car-cloud.cn/Wy/WyBilling/LoadCarOwnerBuyPackages";

    private String MEMBERCARDITEM_URL = "http://www.car-cloud.cn/Wy/WyBilling/LoadCarOwnerPackages";

    private String CAR_URL = "http://www.car-cloud.cn/Wy/WyCarOwner/LoadData";

    private String STOCK_URL = "http://www.car-cloud.cn/Wy/wyInventory/LoadInventoryData";

    private String ITEM_URL = "http://www.car-cloud.cn/Wy/wyProduct/LoadData";

    private String SUPPLIER_URL = "http://www.car-cloud.cn/Wy/WySupplier/LoadData";

    private String fieldName = "total";

    private String companyName = "4C系统-后市场管理系统";

    private int num = 200;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String COOKIE = "UM_distinctid=1658e1e37d6d6-009474bfab936c-37664109-144000-1658e1e37d85b; CNZZDATA1263794507=1764275064-1535683701-%7C1535683701; ASP.NET_SessionId=rccbi5lv25gu2r44an1a3kpd; .ASPXAUTH=E8344216FECEE0AA295355B1F1B99FFC241F58979BF5262390E9F2562C399898CB27963C92BBEFA88AA0662B46FA89F90E35E51B8694B5775518CD4750BE00607C3C5A473F837E6002372181BB8B6587BDB32884FAE3B8CFF7842A7C9E317F21AE7496953AE95853AA89B81AFE28F4BED4C352FF1183A6DBBEE8DBBD0F1E080D798596C74F79FE1249C72AC23AF20DA4D19243A5C9228AE3AE9371A934AFE9A4D50BB4743ACD257321FD27C9AAA397F25F2972F705254BE9783B22600C94674AD945A92EA64E3512E8AFB871C14CB0CC81041C9DD62EC875A71F353CD32269A24673BA8B4D46108520C389D230625D5CE025D62F0C5FB328ABC211E9B023E9EB448C434BBE02BF5E64CEDD85EDFFC46AEE8DA5967A853300E55EEE12141A7F5741D4957D95B1E03AA57FB8B2B489453339A7A3D6751A167C5B73B3433707DEBBA8FC631D8F3CAEA0D20EB7044B79FEA18C3FB571317045AF8CFE41B003672123F6B99B47333AD1BF2065E03418B866ED6A49DF6B227F7A8AB8860F569EF4E8DD7F9C42EC545E0293A2CDC11B85F01BA78A34B2BDDBF1E15C3D1C7051C5A7D8A49CE23070033A6ADE78544540EA93DFD9FB2B932A99E6FF80D64FF4E202DA4F74A7F02A9ECB06C9B888959376D18F30BEB436597C0136CF47E33645EB4C8F68C8BAEB5B12C231CE1DF23066DE774F41A5C597973C776E53C4016D2DF5DCFF8EF286EED6B2148B93631D799E9DA9841885AB8A957D379DE7481F7FCA355E9792EFA61E8E1940D5E6776F1D4A23D8AA4E7020FBBAF4C8866DBE3ECA8F739A2E4441F106F1A1EF5D19FEA2CBC89A38520BCCB94AD824B839EE98DACF2C68BA6715FE7CD9912BDC2C7FD7082E3D1CA215181A22888525D1D905BDDEE8B8CBFC670420C66487FEF55EDA8CFE55E9B8C410E1F68E70D516EBE20A650E323D5C49C092A632E729BAA8EACF278EB38CF16AA887D0";


    /**
     * 车辆信息
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(CLIENT_URL, getParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CLIENT_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String name = element.get("Name").asText();
                    String phone = element.get("Phone").asText();
                    String carNumber = element.get("CarCard").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setName(CommonUtil.formatString(name));
                    carInfo.setCompanyName(companyName);
                    carInfo.setPhone(CommonUtil.formatString(phone));
                    carInfo.setCarNumber(CommonUtil.formatString(carNumber));
                    carInfos.add(carInfo);
                }
            }
        }

        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {

                response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getDetailParams("CarNum", carInfo.getCarNumber()), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                System.out.println("车牌为" + carInfo.getCarNumber());

                if (result.get("rows").size() > 0) {
                    JsonNode data = result.get("rows").get(0);
                    String brand = data.get("CarBrand").asText();
                    String carModel = data.get("CarStyle").asText();
                    String vinCode = data.get("VIN").asText();
                    String engineNumber = data.get("EngineNumber").asText();
                    String registerDate = data.get("RegisterDate").asText();
                    String remark = data.get("Remark").asText();
                    String vcInsuranceCompany = data.get("InsuranceCompany").asText();
                    String vcInsuranceValidDate = data.get("InsuranceDate").asText();

                    carInfo.setBrand(CommonUtil.formatString(brand));
                    carInfo.setCarModel(CommonUtil.formatString(carModel));
                    carInfo.setVINcode(CommonUtil.formatString(vinCode));
                    carInfo.setEngineNumber(CommonUtil.formatString(engineNumber));
                    carInfo.setRegisterDate(CommonUtil.formatString(registerDate));
                    carInfo.setRemark(CommonUtil.formatString(remark));
                    carInfo.setVcInsuranceCompany(CommonUtil.formatString(vcInsuranceCompany));
                    carInfo.setVcInsuranceValidDate(CommonUtil.formatString(vcInsuranceValidDate));
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());

        String pathname = "C:\\exportExcel\\4C车辆导出.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    /**
     * 服务
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(SERVICE_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("Name").asText();
                    String price = element.get("Price").asText();
                    String firstCategoryName = element.get("Stype").asText();
                    String id = element.get("Guid").asText();

                    Product product = new Product();
                    product.setCompanyName(companyName);
                    product.setItemType("服务项");
                    product.setProductName(CommonUtil.formatString(productName));
                    product.setPrice(CommonUtil.formatString(price));
                    product.setFirstCategoryName(CommonUtil.formatString(firstCategoryName));
                    products.add(product);
                }
            }
        }
        System.out.println("结果为" + totalPage);

        String pathname = "C:\\exportExcel\\4C服务导出.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Map<String, String> memberCardMap = new HashMap<>();
        Response response = ConnectionUtil.doPostWithLeastParams(CAR_URL, getParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CAR_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String cardCode = element.get("ClubCard").asText();

                    if ("null".equals(cardCode))
                        continue;

                    String id = element.get("ID").asText();
                    String memberCardName = element.get("ClubGrade").asText();
                    String balance = element.get("Amount").asText();
                    String name = element.get("Name").asText();
                    String phone = element.get("Phone").asText();
                    String carNumber = element.get("CarCard").asText();
                    String dateCreated = element.get("DataTime").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardId(id);
                    memberCard.setName(CommonUtil.formatString(name));
                    memberCard.setPhone(CommonUtil.formatString(phone));
                    memberCard.setBalance(balance);
                    memberCard.setMemberCardName(CommonUtil.formatString(memberCardName));
                    memberCard.setCardCode(CommonUtil.formatString(cardCode));
                    memberCard.setCarNumber(CommonUtil.formatString(carNumber));
                    memberCard.setDateCreated(dateCreated);
                    memberCard.setCompanyName(companyName);
                    memberCards.add(memberCard);
                }
            }
        }

        for (MemberCard m : memberCards) {
            Response res = ConnectionUtil.doPostWithLeastParams(BUYPACKAGES_URL, getDetailParams("CarOwnerID", m.getMemberCardId()), COOKIE);
            JsonNode result = MAPPER.readTree(res.returnContent().asString());

            int size = result.get("rows").size();
            if (size > 0) {
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String dateCreated = element.get("PurchaseDateTime").asText();
                    String carOwnerID = element.get("CarOwnerID").asText();
                    memberCardMap.put(carOwnerID, dateCreated);
                }
            } else {
                Response respon = ConnectionUtil.doPostWithLeastParams(RECHARGERECORD_URL, getDetailParams("CarOwnerID", m.getMemberCardId()), COOKIE);
                JsonNode resultNode = MAPPER.readTree(respon.returnContent().asString());

                int resultSize = resultNode.get("rows").size();
                if (resultSize > 0) {
                    Iterator<JsonNode> it = resultNode.get("rows").iterator();
                    while (it.hasNext()) {
                        JsonNode element = it.next();

                        String carOwnerID = element.get("CarOwnerID").asText();
                        String dateCreated = element.get("RechargeTime").asText();
                        memberCardMap.put(carOwnerID, dateCreated);
                    }
                }
            }
        }

        for (MemberCard memberCard : memberCards) {
            memberCard.setDateCreated(memberCardMap.get(memberCard.getMemberCardId()));
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("大小为" + memberCards.size());

        String pathname = "C:\\exportExcel\\4C会员卡导出.xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }


    /**
     * 卡内项目
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, MemberCardItem> memberCardItemMap = new HashMap<>();

        Response response = ConnectionUtil.doPostWithLeastParams(CAR_URL, getParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CAR_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("ID").asText();
                    String cardCode = element.get("ClubCard").asText();
                    String memberCardName = element.get("ClubGrade").asText();
                    String balance = element.get("Amount").asText();
                    String name = element.get("Name").asText();
                    String phone = element.get("Phone").asText();
                    String dateCreated = element.get("DataTime").asText();

                    MemberCardItem memberCardItem = new MemberCardItem();
                    memberCardItem.setCardCode(CommonUtil.formatString(cardCode));
                    memberCardItem.setMemberCardName(memberCardName);
                    memberCardItem.setBalance(balance);
                    memberCardItem.setName(name);
                    memberCardItem.setPhone(phone);
                    memberCardItem.setDateCreated(dateCreated);
                    memberCardItemMap.put(id, memberCardItem);
                }
            }
        }

        for (String id : memberCardItemMap.keySet()) {
            Response res = ConnectionUtil.doPostWithLeastParams(MEMBERCARDITEM_URL, getDetailParams("CarOwnerID", id), COOKIE);
            JsonNode result = MAPPER.readTree(res.returnContent().asString());

            int size = result.get("rows").size();
            if (size > 0) {
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String itemName = element.get("ObjectName").asText();
                    String num = element.get("Qty").asText();
                    String price = element.get("UnitPrice").asText();
                    String validTime = element.get("ExpireDateTime").asText();
                    String code = element.get("ObjectID").asText();

                    MemberCardItem m = memberCardItemMap.get(id);
                    m.setItemName(itemName);
                    m.setNum(num);
                    m.setOriginalNum(num);
                    m.setPrice(price);
                    m.setValidTime(validTime);
                    m.setCode(code);
                    m.setCompanyName(companyName);
                    memberCardItems.add(m);
                }
            }
        }

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("大小为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\4C卡内项目导出.xls";
        ExportUtil.exportMemberCardItemSomeFieldDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
    }

    /**
     * 商品
     *
     * @throws IOException
     */
    @Test
    public void fetchItemData() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("Name").asText();
                    String code = element.get("PartCode").asText();
                    String barCode = element.get("BarCode").asText();
                    String price = element.get("Price").asText();
                    String firstCategoryName = element.get("Stype").asText();
                    String brandName = element.get("Brand").asText();
                    String carModel = element.get("Fit").asText();

                    Product product = new Product();
                    product.setProductName(CommonUtil.formatString(productName));
                    product.setCode(CommonUtil.formatString(code));
                    product.setBarCode(CommonUtil.formatString(barCode));
                    product.setPrice(CommonUtil.formatString(price));
                    product.setFirstCategoryName(CommonUtil.formatString(firstCategoryName));
                    product.setBrandName(CommonUtil.formatString(brandName));
                    product.setCarModel(CommonUtil.formatString(carModel));
                    product.setItemType("配件");
                    product.setCompanyName(companyName);
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());
        System.out.println("结果为" + products.size());

        String pathname = "C:\\exportExcel\\4C商品导出.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockData() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        Map<String, Stock> stockMap = new HashMap<>();
        Map<String, Stock> itemMap = new HashMap<>();

        Response response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(STOCK_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("ProductID").asText();
                    String goodsName = element.get("ProductName").asText();
                    String inventoryNum = element.get("FinalQty").asText();

                    Stock stock = new Stock();
                    stock.setGoodsName(CommonUtil.formatString(goodsName));
                    stock.setInventoryNum(CommonUtil.formatString(inventoryNum));
                    stockMap.put(id, stock);
                }
            }
        }

        Response res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams("1"), COOKIE);
        int itemTotalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, num);

        if (itemTotalPage > 0) {
            for (int i = 1; i <= itemTotalPage; i++) {
                res = ConnectionUtil.doPostWithLeastParams(ITEM_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("ID").asText();
                    String price = element.get("InPrice").asText();
                    String code = element.get("PartCode").asText();
                    String storeRoomName = element.get("StoreName").asText();
                    String locationName = element.get("StoreLocation").asText();

                    Stock stock = new Stock();
                    stock.setProductCode(CommonUtil.formatString(code));
                    stock.setPrice(CommonUtil.formatString(price));
                    stock.setStoreRoomName(CommonUtil.formatString(storeRoomName));
                    stock.setLocationName(CommonUtil.formatString(locationName));
                    itemMap.put(id, stock);
                }
            }
        }

        if (stockMap.size() > 0) {
            for (String id : stockMap.keySet()) {
                Stock stock = stockMap.get(id);
                Stock s = itemMap.get(id);

                stock.setPrice(s.getPrice());
                stock.setProductCode(s.getProductCode());
                stock.setStoreRoomName(s.getStoreRoomName());
                stock.setLocationName(s.getLocationName());
                stock.setCompanyName(companyName);
                stocks.add(stock);
            }
        }
        System.out.println("结果为" + stocks.toString());
        System.out.println("大小为" + stocks.size());

        String pathname = "C:\\exportExcel\\4C库存导出.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierData() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams("1"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String contactName = element.get("Contact").asText();
                    String contactPhone = element.get("Phone").asText();

                    String name = element.get("Name").asText();
                    String address = element.get("Address").asText();
                    String remark = element.get("Stype").asText();
                    String fax = element.get("Companyphone").asText();//公司电话

                    String manager = element.get("BusinessName").asText();
                    String managerPhone = element.get("BusinessPhone").asText();

                    Supplier supplier = new Supplier();
                    supplier.setContactName(CommonUtil.formatString(contactName));
                    supplier.setContactPhone(CommonUtil.formatString(contactPhone));
                    supplier.setName(CommonUtil.formatString(name));
                    supplier.setAddress(CommonUtil.formatString(address));
                    supplier.setRemark(CommonUtil.formatString(remark));
                    supplier.setFax(CommonUtil.formatString(fax));
                    supplier.setManager(CommonUtil.formatString(manager));
                    supplier.setManagerPhone(CommonUtil.formatString(managerPhone));
                    supplier.setCompanyName(companyName);
                    suppliers.add(supplier);
                }
            }

        }

        System.out.println("结果为" + suppliers.toString());
        System.out.println("大小为" + suppliers.size());

        String pathname = "C:\\exportExcel\\4C供应商导出.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);

    }

    private List<BasicNameValuePair> getCardDetailParams(String carOwnerID) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("CarOwnerID", carOwnerID));
        params.add(new BasicNameValuePair("page", "1"));
        params.add(new BasicNameValuePair("rows", "200"));

        return params;
    }

    private List<BasicNameValuePair> getDetailParams(String fieldName, String value) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(fieldName, value));
        params.add(new BasicNameValuePair("page", "1"));
        params.add(new BasicNameValuePair("rows", "200"));

        return params;
    }


    private List<BasicNameValuePair> getParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("page", pageNo));
        params.add(new BasicNameValuePair("rows", "200"));

        return params;
    }

}
