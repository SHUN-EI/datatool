package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
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
 * Created by mo on  2018/8/2.
 * 车车云系统
 */
@Service
public class CheCheYunService {

    private String MEMBERCARDCAR_URL = "https://www.checheweike.com/crm/index.php?route=member/api/ext_info&exdata=car&vip_user_id=";

    private String MEMBERCARDCLIENT_URL = "https://www.checheweike.com/crm/index.php?route=member/customer/get&id=";

    private String MEMBERCARD_URL = "https://www.checheweike.com/crm/index.php?route=member/customer/gets&limit=100&order=DESC&page={no}&search_key=&sort=vu.date_added&vip_level_id=";

    private String MEMBERCARDLEVEL_URL = "https://www.checheweike.com/web/index.php?route=member/vip_level/gets";

    private String STOCKDETAIL_URL = "https://www.checheweike.com/erp/index.php?route=stock/balance/get_warehouse_detail&substore_id=1&product_id=";

    private String STOCK_URL = "https://www.checheweike.com/erp/index.php?route=stock/balance/gets&limit=50&order=DESC&query_type=product&sort=ps.date_added&substore_id=1&zero_stock_show_enabled=1&page=";

    private String ITEM_URL = "https://www.checheweike.com/web/index.php?route=catalog/product/gets&limit=50&order=DESC&sort=p.date_added&page=";

    private String SERVICE_URL = "https://www.checheweike.com/web/index.php?route=catalog/service/gets&limit=50&order=DESC&sort=s.date_added&page=";

    private String SUPPLIER_URL = "https://www.checheweike.com/web/index.php?route=supplier/supplier/gets&limit=50&order=DESC&sort=date_added&page=";

    private String CARINFODETAIL_URL = "https://www.checheweike.com/crm/index.php?route=member/car/get&car_id=";

    private String CARINFO_URL = "https://www.checheweike.com/crm/index.php?route=member/car/gets&limit=20&order=DESC&sort=c.date_added&page=";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Random random = new Random();

    private String fieldName = "count";

    private String companyName = "车车云";

    private String COOKIE = "_bl_uid=4gj1gkz9dsjtbXpmkc73wL4tywF0; PHPSESSID=u7ce3mahn04uu7grrmkhas0j83; ccwk_backend_tracking=u7ce3mahn04uu7grrmkhas0j83-10535; Hm_lvt_42a5df5a489c79568202aaf0b6c21801=1533202596,1533288090; Hm_lpvt_42a5df5a489c79568202aaf0b6c21801=1533521699; SERVERID=ba8d33d7fbdf881c0f02ef10dce9e063|1533521725|1533521423";


    /**
     * 会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Map<String, String> memberCardLevelMap = new HashMap<>();
        Map<String, MemberCard> memberCardIDMap = new HashMap<>();

        Response res1 = ConnectionUtil.doGetWithLeastParams(MEMBERCARDLEVEL_URL, COOKIE);
        JsonNode result = MAPPER.readTree(res1.returnContent().asString());

        Iterator<JsonNode> it = result.get("vip_levels").iterator();
        while (it.hasNext()) {
            JsonNode element = it.next();

            String id = element.get("vip_level_id").asText();
            String levelName = element.get("name").asText();
            memberCardLevelMap.put(id, levelName);
        }

        if (memberCardLevelMap.size() > 0) {
            for (String id : memberCardLevelMap.keySet()) {
                Response res2 = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARD_URL, "{no}", "1") + id, COOKIE);
                int totalPage = WebClientUtil.getTotalPage(res2, MAPPER, fieldName, 100);

                String memberCardName = memberCardLevelMap.get(id);
                if (totalPage > 0) {
                    for (int i = 1; i <= totalPage; i++) {
                        res2 = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARD_URL, "{no}", String.valueOf(i)) + id, COOKIE);
                        JsonNode content = MAPPER.readTree(res2.returnContent().asString());

                        Iterator<JsonNode> customers = content.get("customers").iterator();
                        while (customers.hasNext()) {
                            JsonNode element = customers.next();

                            String vipUserId = element.get("vip_user_id").asText();
                            String dateCreated = element.get("date_added").asText();
                            String companyName = element.get("substore").asText();
                            String balance = element.get("balance").asText();

                            String cardCode = element.get("card_number").asText();
                            if ("".equals(cardCode))
                                cardCode = String.valueOf(random.nextInt()).replace("-", "");

                            MemberCard memberCard = new MemberCard();
                            memberCard.setCardCode(cardCode);
                            memberCard.setBalance(balance);
                            memberCard.setDateCreated(dateCreated.replace("-", "/"));
                            memberCard.setCompanyName(companyName);
                            memberCard.setMemberCardName(memberCardName);
                            memberCardIDMap.put(vipUserId, memberCard);
                        }
                    }
                }
            }
        }

        if (memberCardIDMap.size() > 0) {
            for (String id : memberCardIDMap.keySet()) {

                Response res3 = ConnectionUtil.doGetWithLeastParams(MEMBERCARDCLIENT_URL + id, COOKIE);
                JsonNode content = MAPPER.readTree(res3.returnContent().asString());

                JsonNode customer = content.get("customer");
                String name = customer.get("name").asText();
                String phone = customer.get("mobile").asText();

                MemberCard memberCard = memberCardIDMap.get(id);
                memberCard.setName(name);
                memberCard.setPhone(phone);
            }
        }

        if (memberCardIDMap.size() > 0) {
            for (String id : memberCardIDMap.keySet()) {
                Response res4 = ConnectionUtil.doGetWithLeastParams(MEMBERCARDCAR_URL + id, COOKIE);
                JsonNode carData = MAPPER.readTree(res4.returnContent().asString());

                Iterator<JsonNode> cars = carData.get("cars").iterator();
                while (cars.hasNext()) {
                    JsonNode e = cars.next();
                    String carNumber = e.get("license").asText();

                    MemberCard m = memberCardIDMap.get(id);
                    MemberCard memberCard = new MemberCard();
                    memberCard.setCarNumber(carNumber);
                    memberCard.setCompanyName(m.getCompanyName());
                    memberCard.setCardCode(m.getCardCode());
                    memberCard.setMemberCardName(m.getMemberCardName());
                    memberCard.setDateCreated(m.getDateCreated());
                    memberCard.setBalance(m.getBalance());
                    memberCard.setName(m.getName());
                    memberCard.setPhone(m.getPhone());
                    memberCards.add(memberCard);
                }
            }
        }

        System.out.print("结果为" +memberCards.toString());

        String pathname = "C:\\exportExcel\\车车云会员卡.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }


    /**
     * 库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();
        Map<String, Stock> stockMap = new HashMap<>();

        Response res = ConnectionUtil.doGetWithLeastParams(STOCK_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= 3; i++) {
                res = ConnectionUtil.doGetWithLeastParams(STOCK_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("products").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String goodsName = element.get("product_name").asText();
                    String productCode = element.get("product_no").asText();
                    String inventoryNum = element.get("left_quantity").asText();
                    String id = element.get("product_id").asText();
                    String price = element.get("unit_cost").asText();
                    String locationName = element.get("position").asText();

                    Stock stock = new Stock();
                    stock.setGoodsName(goodsName);
                    stock.setProductCode(productCode);
                    stock.setInventoryNum(inventoryNum);
                    stock.setPrice(price);
                    stock.setLocationName(locationName);
                    stockMap.put(id, stock);
                }
            }
        }

        if (stockMap.size() > 0) {
            for (String id : stockMap.keySet()) {
                res = ConnectionUtil.doGetWithLeastParams(STOCKDETAIL_URL + id + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                String storeRoomName = "";
                String substoreStr = result.get("substores").toString();
                if (substoreStr.contains("batches")) {
                    JsonNode substores = result.get("substores").get(0);
                    JsonNode batches = substores.get("batches").get(0);

                    storeRoomName = batches.get("warehouse_name").asText();
                }

                Stock stock = stockMap.get(id);
                stock.setCompanyName(companyName);
                stock.setStoreRoomName(storeRoomName);
                stocks.add(stock);
            }
        }

        System.out.print("结果为" + stocks.toString());

        String pathname = "C:\\exportExcel\\车车云库存.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);
    }

    /**
     * 商品
     *
     * @throws IOException
     */
    @Test
    public void fetchItemDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response res = ConnectionUtil.doGetWithLeastParams(ITEM_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(ITEM_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("products").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("name").asText();
                    String code = element.get("product_no").asText();
                    String firstCategoryName = element.get("pcategory_name").asText();//配件分类
                    String secondCategoryName = element.get("business_type_name").asText();//业务类别
                    String price = element.get("price").asText();
                    String unit = element.get("unit").asText();
                    String origin = element.get("manufacturer_name").asText();

                    //启用(上架)-1，禁用(下架)-0
                    String isActive = element.get("status").asText();
                    if ("1".equals(isActive))
                        isActive = "启用";

                    if ("0".equals(isActive))
                        isActive = "禁用";

                    Product product = new Product();
                    product.setProductName(productName);
                    product.setCode(code);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setSecondCategoryName(secondCategoryName);
                    product.setPrice(price);
                    product.setIsActive(isActive);
                    product.setCompanyName(companyName);
                    product.setUnit(unit);
                    product.setOrigin(origin);
                    product.setItemType("配件");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());

        String pathname = "C:\\exportExcel\\车车云商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 服务项目
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response res = ConnectionUtil.doGetWithLeastParams(SERVICE_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(SERVICE_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("services").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("name").asText();
                    String code = element.get("service_no").asText();
                    String firstCategoryName = element.get("scategory_name").asText();//项目分类
                    String secondCategoryName = element.get("business_type_name").asText();//业务类别
                    String price = element.get("price").asText();

                    //启用-1，禁用-0
                    String isActive = element.get("status").asText();
                    if ("1".equals(isActive))
                        isActive = "启用";

                    if ("0".equals(isActive))
                        isActive = "禁用";

                    Product product = new Product();
                    product.setProductName(productName);
                    product.setCode(code);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setSecondCategoryName(secondCategoryName);
                    product.setPrice(price);
                    product.setIsActive(isActive);
                    product.setCompanyName(companyName);
                    product.setItemType("服务项");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());

        String pathname = "C:\\exportExcel\\车车云服务.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 供应商
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        Response res = ConnectionUtil.doGetWithLeastParams(SUPPLIER_URL + "1", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(SUPPLIER_URL + i + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                Iterator<JsonNode> it = result.get("suppliers").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("supplier_id").asText();
                    String name = element.get("name").asText();
                    String contactName = element.get("contact_person").asText();
                    String contactPhone = element.get("mobile").asText();
                    String address = element.get("address").asText();
                    String type = element.get("supplier_type_name").asText();
                    String remark = element.get("comment").asText();
                    String depositBank = element.get("bank").asText();
                    String accountNumber = element.get("bank_account").asText();
                    String fax = element.get("fax").asText();
                    String mainBusiness = element.get("main_business").asText();

                    Supplier supplier = new Supplier();
                    supplier.setName(name);
                    supplier.setCompanyName(companyName);
                    supplier.setContactName(contactName);
                    supplier.setContactPhone(contactPhone);
                    supplier.setAddress(address);
                    supplier.setDepositBank(depositBank);
                    supplier.setAccountNumber(accountNumber);
                    supplier.setFax(fax);
                    supplier.setRemark(CommonUtil.formatString(type) + " " + CommonUtil.formatString(remark));
                    suppliers.add(supplier);
                }
            }
        }

        System.out.println("结果为" + suppliers.toString());

        String pathname = "C:\\exportExcel\\车车云供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }


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

        if (carInfoMap.size() > 0) {
            for (String carId : carInfoMap.keySet()) {
                res = ConnectionUtil.doGetWithLeastParams(CARINFODETAIL_URL + carId + "", COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                JsonNode data = result.get("car");

                String engineNumber = data.get("engine_number").asText();
                String vcInsuranceCompany = data.get("commmercial_insurance_company").asText();
                String tcInsuranceCompany = data.get("compulsory_insurance_company").asText();
                String vcInsuranceValidDate = data.get("date_commmercial_insurance_end").asText();
                String tcInsuranceValidDate = data.get("date_compulsory_insurance_end").asText();
                String registerDate = data.get("date_registered").asText();

                CarInfo carInfo = carInfoMap.get(carId);
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
