package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.Product;
import com.ys.datatool.domain.Supplier;
import com.ys.datatool.util.CommonUtil;
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

    private String ITEM_URL = "https://www.checheweike.com/web/index.php?route=catalog/product/gets&limit=50&order=DESC&sort=p.date_added&page=";

    private String SERVICE_URL = "https://www.checheweike.com/web/index.php?route=catalog/service/gets&limit=50&order=DESC&sort=s.date_added&page=";

    private String SUPPLIER_URL = "https://www.checheweike.com/web/index.php?route=supplier/supplier/gets&limit=50&order=DESC&sort=date_added&page=";

    private String CARINFODETAIL_URL = "https://www.checheweike.com/crm/index.php?route=member/car/get&car_id=";

    private String CARINFO_URL = "https://www.checheweike.com/crm/index.php?route=member/car/gets&limit=20&order=DESC&sort=c.date_added&page=";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "count";

    private String companyName = "车车云";

    private String COOKIE = "_bl_uid=U9jhCk23c20dCO8mwqRgavCnavav; PHPSESSID=u7ce3mahn04uu7grrmkhas0j83; ccwk_backend_tracking=u7ce3mahn04uu7grrmkhas0j83-10535; Hm_lvt_42a5df5a489c79568202aaf0b6c21801=1533202596,1533288090; Hm_lpvt_42a5df5a489c79568202aaf0b6c21801=1533288092; SERVERID=03485b53178f0de6cfb6b08218d57da6|1533288454|1533288038";

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
                while (it.hasNext()){
                    JsonNode element = it.next();

                    String productName = element.get("name").asText();
                    String code = element.get("product_no").asText();
                    String firstCategoryName = element.get("pcategory_name").asText();
                    String secondCategoryName = element.get("business_type_name").asText();
                    String price = element.get("price").asText();
                    String unit=element.get("unit").asText();
                    String origin=element.get("manufacturer_name").asText();

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
                    String firstCategoryName = element.get("scategory_name").asText();
                    String secondCategoryName = element.get("business_type_name").asText();
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
