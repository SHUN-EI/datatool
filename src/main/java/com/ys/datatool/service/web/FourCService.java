package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018-06-12.
 * 4C系统
 */

@Service
public class FourCService {

    private String RECHARGERECORD_URL = "http://www.car-cloud.cn/Wy/WyBilling/LoadCarOwnerRechargeRecord";

    private String BUYPACKAGES_URL = "http://www.car-cloud.cn/Wy/WyBilling/LoadCarOwnerBuyPackages";

    private String MEMBERCARDITEM_URL = "http://www.car-cloud.cn/Wy/WyBilling/LoadCarOwnerPackages";

    private String CAR_URL = "http://www.car-cloud.cn/Wy/WyCarOwner/LoadData";

    private String STOCK_URL = "http://www.car-cloud.cn/Wy/wyInventory/LoadInventoryData";

    private String ITEM_URL = "http://www.car-cloud.cn/Wy/wyProduct/LoadData";

    private String SUPPLIER_URL = "http://www.car-cloud.cn/Wy/WySupplier/LoadData";

    private String fieldName = "total";

    private int num = 200;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Workbook workbook;

    private String COOKIE = "UM_distinctid=163f1c740ba313-099c2ef31601ab-4323461-144000-163f1c740bbb5a; ASP.NET_SessionId=ek2lhloypptuxmnqta35vxsf; CNZZDATA1263794507=1449725428-1528765268-%7C1528956347; .ASPXAUTH=61856F7FB14B6076604F1D47D38FBFE2F891FC40F29387D7D9C1C020BDE2452445A4CE63F8E998DD91690AE3370C93B245C8BCB71BE2942673FBEA8B22AE792CED4F289E79268918FA7145F9BDEC0386CA98157D37BA0D1F46A1510FC451023B38231F0E02DEFD16C612C9DE21649E778E531957265C969DF15C28847085D76D972F01321E8E6D57A6142520E92790F7D09D2938B5112FEE45A733D61286EF29272F5BB561DA60746C7C8E376D22EBC4AA8618142F60F6C3807504666CCCEB13E03EFDF236F1BD60C6D8A5C2098A4C1380F22FAD0CB903A0EC4CF5A00DA63A400B910A9CE605F3953333319019FA3A34EC5DB342E403AC03B6841B0AA2E6DA9AB356F9729B43AD7FFEC1A871F979ACAD7D7C967B94A077D408B6A50D9E8684167C9DBE52AA70A6E490FEFD8E72D9192FCBC53A33F9EA10031DB4827D310D2B62884E24496E6B024C455C73BD1587BC19A76126E61D7577F97C6B9DA1C2330BADF32D50E6CFDBE826193DB1258615BBF5E20E70619D00994C40F4866B542D065EE5327A02EF0D5893AF8C00C2C12D089573970613E332B1F8FAC85F24806616DF61C83C39990446A09888C6939BFE0982DD6EE1959261FD5B73F0774ECB9161CA1EB09F4038AADB2DC6DB350A147EF6D15740CB6E411919DB9048938BA171B60B6F43C39AE2ED4559DB043AB74AF48EB8A58E307E5B0986F9424F2B8B8F1A974B4E2C7FF84300139C3A565BD6F450B0EE6C65C0494277B4421DE406B9652EA1B3B755848D30499339825525BFC74097B0741075FD2302B295684760FB8CF516B4F75A546DA11BA2259204FA928E162A3A7EFFFD7F1BE6DD0CCCA3FF71B40B8E06E8EE8EEADA7A683BA736B3C341EACE9DC19697E8F233A4154B4731CE9F0B5C403C2325DDB79BC8E14BA27086CC14A36891AAFB41CEE37A267C1CDF31607C00C9EBD5540E63184D251BB90B2BC7D33A6D";


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
                    memberCard.setName(name == "null" ? "" : name);
                    memberCard.setPhone(phone == "null" ? "" : phone);
                    memberCard.setBalance(balance);
                    memberCard.setMemberCardName(memberCardName == "null" ? "" : memberCardName);
                    memberCard.setCardCode(cardCode == "null" ? "" : cardCode);
                    memberCard.setCarNumber(carNumber == "null" ? "" : carNumber);
                    memberCard.setDateCreated(dateCreated);
                    memberCards.add(memberCard);
                }
            }
        }

        for (MemberCard m : memberCards) {
            Response res = ConnectionUtil.doPostWithLeastParams(BUYPACKAGES_URL, getCardDetailParams(m.getMemberCardId()), COOKIE);
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
                Response respon = ConnectionUtil.doPostWithLeastParams(RECHARGERECORD_URL, getCardDetailParams(m.getMemberCardId()), COOKIE);
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

        String pathname = "D:\\4C会员卡导出.xls";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, workbook, pathname);
    }

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
                    memberCardItem.setCardCode(cardCode == "null" ? "" : cardCode);
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
            Response res = ConnectionUtil.doPostWithLeastParams(MEMBERCARDITEM_URL, getCardDetailParams(id), COOKIE);
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

                    MemberCardItem memberCardItem = memberCardItemMap.get(id);
                    MemberCardItem m = new MemberCardItem();
                    m.setItemName(itemName);
                    m.setNum(num);
                    m.setOriginalNum(num);
                    m.setPrice(price);
                    m.setValidTime(validTime);
                    m.setCode(code);
                    m.setCardCode(memberCardItem.getCardCode());
                    m.setName(memberCardItem.getName());
                    m.setPhone(memberCardItem.getPhone());
                    m.setPrice(memberCardItem.getPrice());
                    m.setDateCreated(memberCardItem.getDateCreated());
                    m.setMemberCardName(memberCardItem.getMemberCardName());
                    memberCardItems.add(m);
                }
            }
        }

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("大小为" + memberCardItems.size());

        String pathname = "D:\\4C卡内项目导出.xls";
        ExportUtil.exportMemberCardItemSomeFieldDataInLocal(memberCardItems, workbook, pathname);
    }

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
                    product.setProductName(productName == "null" ? "" : productName);
                    product.setCode(code == "null" ? "" : code);
                    product.setBarCode(barCode == "null" ? "" : barCode);
                    product.setPrice(price == "null" ? "" : price);
                    product.setFirstCategoryName(firstCategoryName == "null" ? "" : firstCategoryName);
                    product.setBrandName(brandName == "null" ? "" : brandName);
                    product.setCarModel(carModel == "null" ? "" : carModel);
                    product.setItemType("配件");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());
        System.out.println("结果为" + products.size());

        String pathname = "D:\\4C商品导出.xls";
        ExportUtil.exportProductDataInLocal(products, workbook, pathname);
    }

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
                    stock.setGoodsName(goodsName == "null" ? "" : goodsName);
                    stock.setInventoryNum(inventoryNum == "null" ? "" : inventoryNum);
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
                    stock.setProductCode(code == "null" ? "" : code);
                    stock.setPrice(price == "null" ? "" : price);
                    stock.setStoreRoomName(storeRoomName == "null" ? "" : storeRoomName);
                    stock.setLocationName(locationName == "null" ? "" : locationName);
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
                stocks.add(stock);
            }
        }
        System.out.println("结果为" + stocks.toString());
        System.out.println("大小为" + stocks.size());

        String pathname = "D:\\4C库存导出.xls";
        ExportUtil.exportStockDataInLocal(stocks, workbook, pathname);
    }

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
                    supplier.setContactName(contactName == "null" ? "" : contactName);
                    supplier.setContactPhone(contactPhone == "null" ? "" : contactPhone);
                    supplier.setName(name == "null" ? "" : name);
                    supplier.setAddress(address == "null" ? "" : address);
                    supplier.setRemark(remark == "null" ? "" : remark);
                    supplier.setFax(fax == "null" ? "" : fax);
                    supplier.setManager(manager == "null" ? "" : manager);
                    supplier.setManagerPhone(managerPhone == "null" ? "" : managerPhone);
                    suppliers.add(supplier);
                }
            }

        }

        System.out.println("结果为" + suppliers.toString());
        System.out.println("大小为" + suppliers.size());

        String pathname = "D:\\4C供应商导出.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, workbook, pathname);

    }

    private List<BasicNameValuePair> getCardDetailParams(String carOwnerID) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("CarOwnerID", carOwnerID));
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
