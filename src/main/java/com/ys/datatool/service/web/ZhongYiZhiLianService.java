package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.*;
import com.ys.datatool.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mo on @date  2018/4/27.
 * 中易智联系统
 */
@Service
public class ZhongYiZhiLianService {

    private String UPDATECARDVALIDTIME_URL = "http://boss.xmzyzl.com/Customer/MemberManage/SaveDate?";

    private String MEMBERCARDEXPIRE_URL = "http://boss.xmzyzl.com/Customer/MemberManage/GetSearchResult?limit=20&offset={offset}&StartTime=&EndTime=&CARDTYPEID=&EMPLOYEEID=&shop=&SHOPID=&keyword=&TYPE=3";

    private String MEMBERCARD_URL = "http://boss.xmzyzl.com/Customer/MemberManage/GetSearchResult?limit=20&offset={offset}&StartTime=&EndTime=&CARDTYPEID=&EMPLOYEEID=&SHOPID=&keyword=&TYPE=&shop=";

    private String MEMBERCARDITEM_URL = "http://boss.xmzyzl.com/Customer/MemberManage/PackageQuery";

    private String STOCKCOST_URL = "http://boss.xmzyzl.com/Store/StoreSearch/CostQuery";

    private String STOCKINSHOP_URL = "http://boss.xmzyzl.com/Store/StoreSearch/Query?limit=50&cbtnZero=1&SHOPID={no}&offset=";

    private String CARINFODETAIL_URL = "http://boss.xmzyzl.com/Customer/CustomerAdd/QueryIndex";

    private String CARINFO_URL = "http://boss.xmzyzl.com/Customer/CustomerCar/Query?limit=20&firstShopStartTime=&firstShopEndTime=&memberFlag=&shop=&keyword=&offset={offset}";

    private String SERVICE_URL = "http://boss.xmzyzl.com/Basic/Item/Query?limit=20&startDate=&endDate=&name=&state=&shop=&offset={offset}";

    private String ITEM_URL = "http://boss.xmzyzl.com/Basic/ProductManage/Query?limit=20&startDate=&endDate=&shop=&name=&offset={offset}";

    private String SUPPLIERDETAIL_URL = "http://boss.xmzyzl.com/Basic/Supplier/SuppQuery?SuppID={id}";

    private String SUPPLIER_URL = "http://boss.xmzyzl.com/Basic/Supplier/Query";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fieldName = "total";

    private int num = 20;//分页参数为10、15、20、25

    private String begintime = "2011-01-01";

    private String endtime = "2018-05-31";

    /**
     * 家喻汽车集团各店(12间)
     * 兴隆店 07a21bd0061747418418cb54299402fajl
     * 碧海店 1548c2f4d8854c4f9e1abffd0fc0a175
     * 中铁逸都店 206819be63c240e69cdd49cbd08c76c1
     * 世纪城店 30a5a584ba7c46248c8c5276b3731b82
     * 广场店  32d97ecd5c2c4e569e05cb15b00fff66
     * 蟠桃宫店 708ae79e2bdd48a591ad2af6c91d0a32
     * 平桥店  cb780f5eb03a48f7ba93c0c9e3ad54d4
     * 贵州家喻集团汽车服务有限公司 c4554d26854f47ab8d089aed29fd0c1f
     * 北衙路店 6956a41acd464d81aee8f9f2e534beba
     * 朝阳店  8f2a276046b74ee29bc5001988a757a2
     * 都匀店  aa2d1ea43fbf4b47ad4dc8e6a7cd95cb
     * 北新区店 d50149edd5b742a1adf9dfaf1ad1e94a
     */
    private String shopId = "07a21bd0061747418418cb54299402fajl";//车店编号

    private String companyName = "中易智联";

    private String COOKIE = "_uab_collina=153205459358668378190893; acw_tc=AQAAAPzfYiHJTwYA2blvcVnldZ1d8ZiS; spellName=; ASP.NET_SessionId=4g2x31hvhk5lq0kalq4m1iy3; SysType=0; u_asec=099%23KAFEhEEKE74EJETLEEEEEpEQz0yFD6DFDc3qC60TZcLEW6gTSXiEG60TZXL5E7EFlllbrmQTEE7EERpCjYFET%2FdosyaStqMTEhdEvRIu%2FqYWcTZB95esCR4B3R07Fps1HshnPvZ1UfIWPs2CivZ0lRMAbL4H1OOybspBpEep%2FtWabe7nCGrbAwUWcTDt9yy0ry4B3ED7OfoZHshnwBZBQO4WPsMRi%2FrZbOMAb4QHPfrGbspBTFZGDiabE7EUlllP%2F3iSllllluLSt37FX9llWsaStEgtlllO%2F3iS16allurdt37InHGTEELlluaMIHGkKcQTEEMFluutG%2FBUE7TxE1rWEFy8B1Aa3mvkqUYlqaqQiMQuVi5ZRJ9yk8lDqwP%2BM6A0puEWoZjDB0ANtkj2qHA3kmwNB1b%2B0IYRyUQGBwD4DYAkl%2BKSI%2BCoiZWcqHGl69QTEEjtBKlV";


    /**
     * 卡内项目
     *
     * @throws Exception
     */
    @Test
    public void fetchMemberCardItemDataStandard() throws Exception {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, Product> productMap = new HashMap<>();

        Response response = null;
        int totalPage = 0;
        //服务项目
        response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(SERVICE_URL, "{offset}", "0"), COOKIE);
        totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);
        if (totalPage > 0) {
            int offSet = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(SERVICE_URL, "{offset}", String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String productName = element.get("NAME").asText();
                    String code = element.get("CODE").asText();
                    String firstCategoryName = element.get("ITEMCLASSNAME").asText();
                    String price = element.get("SELLPRICE").asText();
                    String id = element.get("ID").asText();

                    Product product = new Product();
                    product.setProductName(formatString(productName));
                    product.setCode(formatString(code));
                    product.setItemType("服务项");
                    product.setFirstCategoryName(formatString(firstCategoryName));
                    product.setPrice(price);
                    productMap.put(id, product);
                }
            }
        }

        //商品
        response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(ITEM_URL, "{offset}", "0"), COOKIE);
        totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);
        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(ITEM_URL, "{offset}", String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String code = element.get("CODE").asText();
                    String productName = element.get("NAME").asText();
                    String firstCategoryName = element.get("PRODUCTCLASSNAME").asText();
                    String price = element.get("SELLPRICE").asText();
                    String barCode = element.get("BARCODE").asText();
                    String id = element.get("ID").asText();

                    Product product = new Product();
                    product.setCode(formatString(code));
                    product.setProductName(formatString(productName));
                    product.setFirstCategoryName(formatString(firstCategoryName));
                    product.setPrice(price);
                    product.setItemType("配件");
                    product.setBarCode(formatString(barCode));
                    productMap.put(id, product);
                }
            }
        }

        //会员卡内项目
        response = ConnectionUtil.doPostWithLeastParams(MEMBERCARDITEM_URL, getMemberCardItemParams(shopId), COOKIE);
        JsonNode result = MAPPER.readTree(response.returnContent().asString());

        Iterator<JsonNode> it = result.iterator();
        while (it.hasNext()) {
            JsonNode element = it.next();

            String id = element.get("PRODUCTID").asText();
            String companyName = element.get("SHOPNAME").asText();
            String cardCode = element.get("CARDNUMBER").asText();
            String itemName = element.get("PNAME").asText();
            String originalNum = element.get("SUMACOUNT").asText();
            String num = element.get("NUM").asText();
            String validTime = element.get("EXPIRYTIME").asText();

            Product product = productMap.get(id);
            MemberCardItem memberCardItem = new MemberCardItem();
            memberCardItem.setCompanyName(companyName);
            memberCardItem.setCardCode(cardCode);
            memberCardItem.setItemName(itemName);
            memberCardItem.setDiscount("0");
            memberCardItem.setNum(num);
            memberCardItem.setOriginalNum(originalNum);
            memberCardItem.setValidTime(validTime);
            memberCardItem.setIsValidForever(CommonUtil.getIsValidForever(validTime));
            memberCardItem.setPrice(product.getPrice());
            memberCardItem.setFirstCategoryName(product.getFirstCategoryName());
            memberCardItem.setCode(product.getCode());
            memberCardItem.setItemType(product.getItemType());
            memberCardItems.add(memberCardItem);
        }

        System.out.println("结果为" + memberCardItems.toString());
        System.out.println("memberCardItems大小为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\中易智联卡内项目.xlsx";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
    }

    /**
     * 批量更新会员卡到期时间
     * 系统服务器每次只允许调用接口100次
     *
     * @throws IOException
     */
    @Test
    public void batchUpdateCardValidTime() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Response response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARDEXPIRE_URL, "{offset}", "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARDEXPIRE_URL, "{offset}", String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String cardCode = element.get("CARDNUMBER").asText();
                    String cuId = element.get("CUID").asText();
                    String id = element.get("ID").asText();
                    String ctId = element.get("CTID").asText();
                    String txtendtime = element.get("MATURITY").asText();
                    String txtchangetime = "2018-05-25";//延期时间

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardId(id);
                    memberCard.setValidTime(txtendtime);
                    memberCard.setChangeTime(txtchangetime);
                    memberCard.setCtId(ctId);
                    memberCard.setCardCode(cardCode);
                    memberCard.setCuId(cuId);
                    memberCards.add(memberCard);
                }
            }
        }

        if (memberCards.size() > 0) {

            int index = 1;
            for (MemberCard memberCard : memberCards) {
                ++index;
                System.out.println("卡号为" + memberCard.getCardCode() + "正在延期.....");
                ConnectionUtil.doPostWithLeastParams(UPDATECARDVALIDTIME_URL,
                        getCardValidTimeParams(memberCard.getMemberCardId(), memberCard.getCuId(),
                                memberCard.getValidTime(), memberCard.getChangeTime(),
                                memberCard.getCtId()), COOKIE);
                System.out.println("卡号为" + memberCard.getCardCode() + "延期成功");
                System.out.println("共延期了" + index + "张会员卡");
            }
        }

        System.out.println("过期会员卡分别为" + memberCards.toString());
        System.out.println("过期会员卡总数为" + memberCards.size());
        System.out.println("所有过期会员卡延期完成");

    }

    /**
     * 过期会员卡
     *
     * @throws IOException
     */
    @Test
    public void fetchExpireMemberCardData() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Response response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARDEXPIRE_URL, "{offset}", "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        // 2018/5/22 过期会员卡卡内项目详情页面的cookies会变，需要获取上次访问的cookie
        if (totalPage > 0) {
            int offSet = 0;

            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARDEXPIRE_URL, "{offset}", String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String id = element.get("ID").asText();
                    String clientId = element.get("CUSTOMERID").asText();
                    String cardCode = element.get("CARDNUMBER").asText();
                    String companyName = element.get("SHOPNAME").asText();
                    String memberCardName = element.get("CTNAME").asText();
                    String name = element.get("CUNAME").asText();
                    String phone = element.get("MOBILE").asText();
                    String carNumber = element.get("CARNUMBER").asText();
                    String dateCreated = element.get("SORTTIME").asText();//"CREATETIME"
                    String balance = element.get("CARDBALANCE").asText();
                    String validTime = element.get("MATURITY").asText();
                    String state = element.get("STATE").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCompanyName(companyName);
                    memberCard.setCardCode(cardCode);
                    memberCard.setMemberCardName(memberCardName);
                    memberCard.setName(formatString(name));
                    memberCard.setPhone(formatString(phone));
                    memberCard.setCarNumber(carNumber);
                    memberCard.setDateCreated(DateUtil.formatDateTime(dateCreated));
                    memberCard.setBalance(balance);
                    memberCard.setRemark(validTime);
                    memberCard.setMemberCardId(id);
                    memberCard.setCardType(clientId);
                    memberCards.add(memberCard);
                }
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("memberCards大小为" + memberCards.size());

        String pathname = "C:\\exportExcel\\过期会员卡导出.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    /**
     * 会员卡-标准模版导出
     * <p>
     * 支持连锁店，分别导出各店的会员卡，需要传入各店的shopId
     * 如果不传shopId(shopId=""),则默认导出所有店的会员卡
     *
     * @throws Exception
     */
    @Test
    public void fetchMemberCardDataStandard() throws IOException {
        List<MemberCard> memberCards = new ArrayList<>();
        Response response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARD_URL, "{offset}", "0") + shopId, COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;

            //因为家喻总店会员卡数据太多，要分状态抓取，修改地址MEMBERCARD_URL中TYPE=0为正常，1为挂失，3为过期
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(MEMBERCARD_URL, "{offset}", String.valueOf(offSet)) + shopId, COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                System.out.println("offSet大小为" + offSet);
                System.out.println("页数为" + i);
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String cardCode = element.get("CARDNUMBER").asText();
                    String companyName = element.get("SHOPNAME").asText();
                    String memberCardName = element.get("CTNAME").asText();
                    String name = element.get("CUNAME").asText();
                    String phone = element.get("MOBILE").asText();
                    String carNumber = element.get("CARNUMBER").asText();
                    String dateCreated = element.get("SORTTIME").asText();//"CREATETIME"
                    String balance = element.get("CARDBALANCE").asText();
                    String state = element.get("STATE").asText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setCompanyName(companyName);
                    memberCard.setCardCode(cardCode);
                    memberCard.setMemberCardName(memberCardName);
                    memberCard.setName(formatString(name));
                    memberCard.setPhone(formatString(phone));
                    memberCard.setCarNumber(formatString(carNumber));
                    memberCard.setDateCreated(DateUtil.formatDateTime(dateCreated));
                    memberCard.setBalance(balance);
                    memberCard.setRemark(state);
                    memberCards.add(memberCard);
                }
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("memberCards大小为" + memberCards.size());

        String pathname = "C:\\exportExcel\\中易智联会员卡.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, ExcelDatas.workbook, pathname);

    }

    /**
     * 库存-标准模版导出
     * 支持连锁店，分别导出各店的库存，需要传入各店的shopId
     * 如果不传shopId(shopId=""),则默认导出所有店的库存
     *
     * @throws IOException
     */
    @Test
    public void fetchStockInShopDataStandard() throws IOException {
        List<Stock> stocks = new ArrayList<>();

        Response res = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(STOCKINSHOP_URL, "{no}", shopId) + "0", COOKIE);
        int totalPage = WebClientUtil.getTotalPage(res, MAPPER, fieldName, 50);

        if (totalPage > 0) {
            int offSet = 0;
            for (int i = 1; i <= totalPage; i++) {
                res = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(STOCKINSHOP_URL, "{no}", shopId) + String.valueOf(offSet), COOKIE);
                JsonNode result = MAPPER.readTree(res.returnContent().asString());

                offSet = offSet + 50;
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String goodsName = element.get("NAME").asText();
                    String code = element.get("CODE").asText();
                    String storeRoomName = element.get("WAREHOUSENAME").asText();
                    String barcode = element.get("BARCODE").asText();//家喻要求条形码为商品编码
                    String companyName = element.get("SHOPNAME").asText();

                    //"ALLOWNUM"-可用库存,"NUM"-实际库存
                    String num = element.get("NUM").asText();
                    String allowNum = element.get("ALLOWNUM").asText();
                    String stockId = element.get("PRODUCTSKUID").asText();

                    Stock stock = new Stock();
                    stock.setCompanyName(formatString(companyName));
                    stock.setStoreRoomName(formatString(storeRoomName));
                    stock.setGoodsName(formatString(goodsName));
                    stock.setInventoryNum(num);
                    stock.setProductCode(formatString(code));
                    stock.setBarCode(formatString(barcode));
                    stocks.add(stock);
                }
            }
        }

        if (stocks.size() > 0) {
            for (Stock stock : stocks) {
                Response response = ConnectionUtil.doPostWithLeastParams(STOCKCOST_URL, getStockCostParams(shopId, begintime, endtime, stock.getBarCode()), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                String cost = result.get(0).get("COSTPRICE").asText();
                stock.setPrice(cost);
            }
        }


        System.out.println("结果为" + stocks.toString());

        String pathname = "C:\\exportExcel\\中易智联库存.xls";
        ExportUtil.exportStockDataInLocal(stocks, ExcelDatas.workbook, pathname);

    }

    /**
     * 服务项目-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchServiceDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(SERVICE_URL, "{offset}", "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(SERVICE_URL, "{offset}", String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String productName = element.get("NAME").asText();
                    String code = element.get("CODE").asText();
                    String firstCategoryName = element.get("ITEMCLASSNAME").asText();
                    String companyName = element.get("SHOPNAME").asText();
                    String price = element.get("SELLPRICE").asText();
                    String remark = element.get("REMARK").asText();

                    Product product = new Product();
                    product.setProductName(formatString(productName));
                    product.setCode(formatString(code));
                    product.setFirstCategoryName(formatString(firstCategoryName));
                    product.setCompanyName(formatString(companyName));
                    product.setPrice(formatString(price));
                    product.setRemark(formatString(remark));
                    product.setItemType("服务项");
                    product.setIsShare("是");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());

        String pathname = "C:\\exportExcel\\中易智联服务项目.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 商品-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchItemDataStandard() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(ITEM_URL, "{offset}", "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(ITEM_URL, "{offset}", String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String code = element.get("CODE").asText();
                    String productName = element.get("NAME").asText();
                    String firstCategoryName = element.get("PRODUCTCLASSNAME").asText();
                    String brandName = element.get("PRODUCTBRANDNAME").asText();
                    String unit = element.get("PRODUCTUNITNAME").asText();
                    String price = element.get("SELLPRICE").asText();
                    String companyName = element.get("SHOPNAME").asText();
                    String barCode = element.get("BARCODE").asText();
                    String remark = element.get("REMARK").asText();

                    Product product = new Product();
                    product.setCode(formatString(code));
                    product.setProductName(formatString(productName));
                    product.setFirstCategoryName(formatString(firstCategoryName));
                    product.setBrandName(formatString(brandName));
                    product.setUnit(formatString(unit));
                    product.setPrice(formatString(price));
                    product.setCompanyName(formatString(companyName));
                    product.setBarCode(formatString(barCode));
                    product.setItemType("配件");
                    product.setIsShare("是");
                    product.setRemark(formatString(remark));
                    products.add(product);
                }
            }
        }

        System.out.println("大小为" + products.size());
        System.out.println("结果为" + products.toString());

        String pathname = "C:\\exportExcel\\中易智联商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);

    }

    /**
     * 供应商-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchSupplierDataStandard() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Map<String, Supplier> supplierMap = new HashMap<>();

        Response response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getSupplierParams(String.valueOf(num), "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doPostWithLeastParams(SUPPLIER_URL, getSupplierParams(String.valueOf(num), String.valueOf(offSet)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String supplierId = element.get("ID").asText();
                    String code = element.get("CODE").asText();
                    String className = element.get("CLASSNAME").asText();
                    String name = element.get("NAME").asText();
                    String remark = element.get("REMARK").asText();
                    String companyName = element.get("SHOPNAME").asText();

                    Supplier supplier = new Supplier();
                    supplier.setCompanyName(formatString(companyName));
                    supplier.setCode(formatString(code));
                    supplier.setName(formatString(name));
                    supplier.setRemark(formatString(className) + " " + formatString(remark));
                    supplierMap.put(supplierId, supplier);
                }
            }
        }

        if (supplierMap.size() > 0) {
            for (String id : supplierMap.keySet()) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(SUPPLIERDETAIL_URL, "{id}", id), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());
                Iterator<JsonNode> it = result.iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    String fax = element.get("FAX").asText();
                    String address = element.get("ADDRESS").asText();
                    String phone = element.get("PHONE").asText();
                    String contactPhone = element.get("MOBILE").asText();
                    String contactName = element.get("CONTACT").asText();

                    Supplier s = supplierMap.get(id);
                    Supplier supplier = new Supplier();
                    supplier.setCompanyName(s.getCompanyName());
                    supplier.setCode(s.getCode());
                    supplier.setName(s.getName());
                    supplier.setRemark(s.getRemark());
                    supplier.setContactName(formatString(contactName));
                    supplier.setAddress(formatString(address));
                    supplier.setFax(formatString(fax));
                    supplier.setContactPhone(formatString(contactPhone));
                    suppliers.add(supplier);
                }
            }
        }


        System.out.println("大小为" + suppliers.size());
        System.out.println("结果为" + suppliers.toString());

        String pathname = "C:\\exportExcel\\中易智联供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }

    /**
     * 车辆信息-标准模版导出
     *
     * @throws IOException
     */
    @Test
    public void fetchCarInfoDataStandard() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();
        Map<String, CarInfo> carInfoMap = new HashMap<>();

        Response response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(CARINFO_URL, "{offset}", "0"), COOKIE);
        int totalPage = WebClientUtil.getTotalPage(response, MAPPER, fieldName, num);

        if (totalPage > 0) {
            int offSet = 0;
            for (int i = 1; i <= totalPage; i++) {
                response = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(CARINFO_URL, "{offset}", String.valueOf(offSet)), COOKIE);
                System.out.println("页数为" + i);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                offSet = offSet + num;
                Iterator<JsonNode> it = result.get("rows").iterator();

                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String carId = element.get("ID").asText();
                    String carNumber = element.get("CARNUMBER").asText();
                    String name = element.get("CNAME").asText();
                    String phone = element.get("MOBILE").asText();
                    String companyName = element.get("SHOPNAME").asText();
                    String brand = element.get("BRANDNAME").asText();
                    String carModel = element.get("CARTYPENAME").asText();
                    String remark = element.get("REMARK").asText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarNumber(carNumber == "null" ? "" : carNumber);
                    carInfo.setName(formatString(name));
                    carInfo.setPhone(formatString(phone));
                    carInfo.setCompanyName(formatString(companyName));
                    carInfo.setBrand(formatString(brand));
                    carInfo.setCarModel(formatString(carModel));
                    carInfo.setRemark(formatString(remark));
                    carInfoMap.put(carId, carInfo);
                }
            }
        }

        if (carInfoMap.size() > 0) {
            for (String id : carInfoMap.keySet()) {
                response = ConnectionUtil.doPostWithLeastParams(CARINFODETAIL_URL, getCarInfoDetailParams(id), COOKIE);

                JsonNode result = MAPPER.readTree(response.returnContent().asString());
                Iterator<JsonNode> it = result.iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();
                    String VINcode = element.get("VINNUMBER").asText();
                    String engineNumber = element.get("ENGINENUMBER").asText();
                    String vcInsuranceCompany = element.get("INSURERCOMPANY").asText();
                    String tcInsuranceValidDate = element.get("COMPULSORYTIME").asText();
                    String vcInsuranceValidDate = element.get("INSURANCETIME").asText();

                    CarInfo carInfo = carInfoMap.get(id);
                    carInfo.setVINcode(formatString(VINcode));
                    carInfo.setEngineNumber(formatString(engineNumber));
                    carInfo.setVcInsuranceCompany(formatString(vcInsuranceCompany));
                    carInfo.setVcInsuranceValidDate(formatString(vcInsuranceValidDate));
                    carInfo.setTcInsuranceValidDate(formatString(tcInsuranceValidDate));
                    carInfos.add(carInfo);
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());

        String pathname = "C:\\exportExcel\\中易智联车辆.xlsx";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }


    private List<BasicNameValuePair> getMemberCardItemParams(String shopId) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("keyword", ""));
        params.add(new BasicNameValuePair("item", ""));
        params.add(new BasicNameValuePair("StartTime", ""));
        params.add(new BasicNameValuePair("EndTime", ""));
        params.add(new BasicNameValuePair("CardType", ""));
        params.add(new BasicNameValuePair("Type", ""));
        params.add(new BasicNameValuePair("shop", shopId));
        return params;
    }

    private List<BasicNameValuePair> getCardValidTimeParams(String id, String cuId, String txtendtime, String txtchangetime, String ctId) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("ShopId", ""));
        params.add(new BasicNameValuePair("ID", id));
        params.add(new BasicNameValuePair("cuId", cuId));
        params.add(new BasicNameValuePair("txtendtime", txtendtime));
        params.add(new BasicNameValuePair("txtchangetime", txtchangetime));
        params.add(new BasicNameValuePair("ctId", ctId));
        params.add(new BasicNameValuePair("delayRemark", ""));
        return params;
    }


    private List<BasicNameValuePair> getStockCostParams(String shopId, String begintime, String endtime, String keywords) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("WAREHOUSEID", ""));
        params.add(new BasicNameValuePair("keywords", keywords));
        params.add(new BasicNameValuePair("SHOPID", shopId));
        params.add(new BasicNameValuePair("treeId", ""));
        params.add(new BasicNameValuePair("begintime", begintime));
        params.add(new BasicNameValuePair("endtime", endtime));
        params.add(new BasicNameValuePair("billtype", ""));
        return params;
    }

    private List<BasicNameValuePair> getCarInfoDetailParams(String carId) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("carId", carId));
        return params;
    }

    private List<BasicNameValuePair> getSupplierParams(String num, String offset) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("limit", num));
        params.add(new BasicNameValuePair("offset", offset));
        params.add(new BasicNameValuePair("shop", ""));
        return params;
    }

    private String formatString(String target) {
        return target == "null" ? "" : target;
    }
}
