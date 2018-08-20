package com.ys.datatool.service.web;


import com.ys.datatool.domain.*;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mo on @date  2018/6/20.
 * 车赢家系统
 */
@Service
public class CheYingJiaService {


    String url = "http://61.186.130.102:803/YCKService.asmx";

    String HOST = "61.186.130.102:803";

    String SOAPAction = "http://tempuri.org/RunProcedureAndGetTotalRecord";

    String QUERYSOAPAction = "http://tempuri.org/Query";

    String CONTENT_TYPE = "text/xml; charset=utf-8";

    private Charset charset = Charset.forName("UTF-8");

    /**
     * 车辆页面总页数
     */
    private int carInfoNum = 431;

    /**
     * 供应商页面总页数
     */
    private int supplierNum = 6;

    /**
     * 服务页面总页数
     */
    private int serviceNum = 20;

    /**
     * 商品页面总页数
     */
    private int itemNum = 108;

    /**
     * 会员卡页面总页数
     */
    private int memberCardNum = 323;//323

    private String userName = "297edeb35a0eb000015a1baf767104da";

    private String passWord = "3844FFFB48E49782625F10D54E4AACCD";

    private String cyjToken = "2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585";

    private String companyId = "297edeb357fb144a01580046ab245d37";

    /**
     * 解析返回数据传参
     */
    private String element = "_x0035_816";


    /**
     * 卡内项目-标准模版导出
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException, DocumentException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, MemberCard> memberCardMap = new HashMap<>();

        String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header>" +
                "<MySoapHeader xmlns=\"http://tempuri.org/\">" +
                "<UserName>" +
                userName +
                "</UserName>" +
                "<PassWord>" +
                passWord +
                "</PassWord>" +
                "<CyjToken>" +
                cyjToken +
                "</CyjToken>" +
                "<CompanyId>" +
                companyId +
                "</CompanyId>" +
                "</MySoapHeader></soap:Header>" +
                "<soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\">" +
                "<storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
                "&lt;ArrayOfDictionaryEntry xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_curPage&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;{no}&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_sort&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\" /&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_fields&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;'0' as IsSelect,SALESNAME,id,cardId,cardClassId,cardClassName,subCardFlag,subOrder,tranPassId,quryPassId,membId,membName,leaguerNum,membPhone,smsFlag,makeCardDate,validDate,activeDate,closeDate,lastActiDate,makeCardFileid,sellType,cardYesdBal,cardBal,cardSubAcctNub,track1,track2,track3,cardCVV,disCrank,merId,merName,branchCompId,agentCompId,projMangId,projAssitId,stats,lastOperId,lastOperName,createEmpName,lastOperDate,batchNo,storesId,storesName,memberCarId,memberCar,ciCarNumber,bakThree,bakFour,bakFive,Remark&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_filter&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;(merId='297edeb35d0b3080015d0ce0879e30af' and (stats is null or stats &amp;lt;&amp;gt;'6')) and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_pageSize&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;20&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_tableName&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;yck_cardInfo&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "&lt;/ArrayOfDictionaryEntry&gt;</parameters></RunProcedureAndGetTotalRecord></soap:Body></soap:Envelope>";

        String cardItemParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header>" +
                "<MySoapHeader xmlns=\"http://tempuri.org/\">" +
                "<UserName>" +
                userName +
                "</UserName>" +
                "<PassWord>" +
                passWord +
                "</PassWord>" +
                "<CyjToken>" +
                cyjToken +
                "</CyjToken>" +
                "<CompanyId>" +
                companyId +
                "</CompanyId>" +
                "</MySoapHeader></soap:Header><soap:Body><Query xmlns=\"http://tempuri.org/\">" +
                "<SQLString>select id,cardInfoId,itemId,itemCode,itemName,price,tolPrice,costprice,costtolprice,num,settleType,case when dayNum=''OR dayNum IS NULL THEN '0'ELSE dayNum END dayNum,surplusNum,useNum FROM yck_cardinfodetail  " +
                "where cardInfoId='{no}'</SQLString>" +
                "</Query></soap:Body></soap:Envelope>";

        for (int i = 1; i <= memberCardNum; i++) {
            String params = StringUtils.replace(param, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction, params);

            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html, element);

            if (dataList.size() > 0) {
                for (Element node : dataList) {
                    String cardId = "";
                    Element cardIdElement = node.element("ID");
                    if (cardIdElement != null)
                        cardId = cardIdElement.getText();

                    String cardCode = "";
                    Element cardCodeElement = node.element("CARDID");
                    if (cardCodeElement != null)
                        cardCode = cardCodeElement.getText();

                    String validTime = "";
                    Element validTimeElement = node.element("VALIDDATE");
                    if (validTimeElement != null)
                        validTime = validTimeElement.getText();

                    String companyName = "";
                    Element companyNameElement = node.element("STORESNAME");
                    if (companyNameElement != null)
                        companyName = companyNameElement.getText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardId(cardId);
                    memberCard.setCardCode(cardCode);
                    memberCard.setCompanyName(companyName);
                    memberCard.setValidTime(validTime);
                    memberCardMap.put(cardId, memberCard);
                }
            }
        }

        if (memberCardMap.size() > 0) {
            for (String id : memberCardMap.keySet()) {
                String params = StringUtils.replace(cardItemParam, "{no}", id);
                Response resp = ConnectionUtil.doPostWithSOAP(url, QUERYSOAPAction, params);

                String html = resp.returnContent().asString(charset);
                if (html.contains("</NewDataSet>")) {
                    String target = "ds";
                    List<Element> dataList = getQueryDataList(html, target);

                    if (dataList.size() > 0) {
                        for (Element node : dataList) {
                            String itemName = "";
                            Element itemNameElement = node.element("ITEMNAME");
                            if (itemNameElement != null)
                                itemName = itemNameElement.getText();

                            String code = "";
                            Element codeElement = node.element("ITEMCODE");
                            if (codeElement != null)
                                code = codeElement.getText();

                            String price = "";
                            Element priceElement = node.element("PRICE");
                            if (priceElement != null)
                                price = priceElement.getText();

                            String originalNum = "";
                            Element originalNumElement = node.element("NUM");
                            if (originalNumElement != null)
                                originalNum = originalNumElement.getText();

                            String num = "";
                            Element numElement = node.element("SURPLUSNUM");
                            if (numElement != null)
                                num = numElement.getText();

                            //有效期(月)
                            String validTime = "";
                            Element validTimeElement = node.element("DAYNUM");
                            if (validTimeElement != null)
                                validTime = validTimeElement.getText();

                            MemberCard memberCard = memberCardMap.get(id);
                            MemberCardItem memberCardItem = new MemberCardItem();
                            memberCardItem.setItemName(itemName);
                            memberCardItem.setCode(code);
                            memberCardItem.setPrice(price);
                            memberCardItem.setOriginalNum(originalNum);
                            memberCardItem.setNum(num);
                            memberCardItem.setCardCode(memberCard.getCardCode());
                            memberCardItem.setCompanyName(memberCard.getCompanyName());
                            memberCardItem.setValidTime(memberCard.getValidTime());
                            memberCardItem.setIsValidForever(CommonUtil.getIsValidForever(memberCard.getValidTime()));
                            memberCardItems.add(memberCardItem);
                        }
                    }
                }
            }
        }

        System.out.println("memberCardMap结果为" + memberCardMap.toString());
        System.out.println("memberCardMap大小为" + memberCardMap.size());
        System.out.println("memberCardItems结果为" + memberCardItems.toString());
        System.out.println("memberCardItems大小为" + memberCardItems.size());

        String pathname = "C:\\exportExcel\\车赢家卡内项目导出.xlsx";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
    }

    /**
     * 会员卡-标准模版导出
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchMemberCardData() throws IOException, DocumentException {
        List<MemberCard> memberCards = new ArrayList<>();

        String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header>" +
                "<MySoapHeader xmlns=\"http://tempuri.org/\">" +
                "<UserName>" +
                userName +
                "</UserName>" +
                "<PassWord>" +
                passWord +
                "</PassWord>" +
                "<CyjToken>" +
                cyjToken +
                "</CyjToken>" +
                "<CompanyId>" +
                companyId +
                "</CompanyId>" +
                "</MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
                "&lt;ArrayOfDictionaryEntry xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_curPage&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;{no}&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_sort&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\" /&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_fields&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;'0' as IsSelect,SALESNAME,id,cardId,cardClassId,cardClassName,subCardFlag,subOrder,tranPassId,quryPassId,membId,membName,leaguerNum,membPhone,smsFlag,makeCardDate,validDate,activeDate,closeDate,lastActiDate,makeCardFileid,sellType,cardYesdBal,cardBal,cardSubAcctNub,track1,track2,track3,cardCVV,disCrank,merId,merName,branchCompId,agentCompId,projMangId,projAssitId,stats,lastOperId,lastOperName,createEmpName,lastOperDate,batchNo,storesId,storesName,memberCarId,memberCar,ciCarNumber,bakThree,bakFour,bakFive,Remark&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_filter&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;(merId='297edeb35d0b3080015d0ce0879e30af' and (stats is null or stats &amp;lt;&amp;gt;'6')) and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_pageSize&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;20&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_tableName&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;yck_cardInfo&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "&lt;/ArrayOfDictionaryEntry&gt;</parameters></RunProcedureAndGetTotalRecord></soap:Body></soap:Envelope>";

        for (int i = 1; i <= memberCardNum; i++) {
            String params = StringUtils.replace(param, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction, params);

            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html, element);

            if (dataList.size() > 0) {
                for (Element node : dataList) {
                    String memberCardName = "";
                    Element memberCardNameElement = node.element("CARDCLASSNAME");
                    if (memberCardNameElement != null)
                        memberCardName = memberCardNameElement.getText();

                    String cardCode = "";
                    Element cardCodeElement = node.element("CARDID");
                    if (cardCodeElement != null)
                        cardCode = cardCodeElement.getText();

                    String name = "";
                    Element nameElement = node.element("MEMBNAME");
                    if (nameElement != null)
                        name = nameElement.getText();

                    String phone = "";
                    Element phoneElement = node.element("MEMBPHONE");
                    if (phoneElement != null)
                        phone = phoneElement.getText();

                    String dateCreated = "";
                    Element dateCreatedElement = node.element("ACTIVEDATE");
                    if (dateCreatedElement != null)
                        dateCreated = dateCreatedElement.getText();

                    String validTime = "";
                    Element validTimeElement = node.element("VALIDDATE");
                    if (validTimeElement != null)
                        validTime = validTimeElement.getText();

                    String companyName = "";
                    Element companyNameElement = node.element("STORESNAME");
                    if (companyNameElement != null)
                        companyName = companyNameElement.getText();

                    String carNumber = "";
                    Element carNumberElement = node.element("CICARNUMBER");
                    if (carNumberElement != null)
                        carNumber = carNumberElement.getText();

                    String balance = "";
                    Element balanceElement = node.element("CARDBAL");
                    if (balanceElement != null)
                        balance = balanceElement.getText();

                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardName(memberCardName);
                    memberCard.setCardCode(cardCode);
                    memberCard.setName(name);
                    memberCard.setPhone(phone);
                    memberCard.setDateCreated(dateCreated);
                    memberCard.setValidTime(validTime);
                    memberCard.setCompanyName(companyName);
                    memberCard.setCarNumber(carNumber);
                    memberCard.setBalance(balance);
                    memberCards.add(memberCard);
                }
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("大小为" + memberCards.size());

        String pathname = "C:\\exportExcel\\车赢家会员卡导出.xlsx";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    /**
     * 商品-标准模版导出
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchItemData() throws IOException, DocumentException {
        List<Product> products = new ArrayList<>();

        String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header>" +
                "<MySoapHeader xmlns=\"http://tempuri.org/\">" +
                "<UserName>" +
                userName +
                "</UserName>" +
                "<PassWord>" +
                passWord +
                "</PassWord>" +
                "<CyjToken>" +
                cyjToken +
                "</CyjToken>" +
                "<CompanyId>" +
                companyId +
                "</CompanyId>" +
                "</MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
                "&lt;ArrayOfDictionaryEntry xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_curPage&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;{no}&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_sort&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;stockno&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_fields&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt; id, states, productname,'0' IsPrintCode, productcode, stockno, pinyinno, brand, specmodel, unit, supplier, supplerid, storenum, appinnum, trueinnum, appoutnum, trueoutnum, customname, customcode, customid, warehousename, warehousecode, warehouseid, createper, createperid, createdates, productcage, productcageid, protypeid, protypecode, protypename, createdate, createemp, lastupdatedate, updateemp, bakone, baktwo, bakthree, bakfour, bakfive, baksix, bakseven, bakeight, baknine, bakten, productid, applymodel, picstore, applymodelid, tenantname, tenantid, picsrc, picstore1, picstore2, picstore3, picstore4, picstore5, picstore6, picstore7, viceunit, price, saleprice, lowsaleprice, wholesaleprice1, wholesaleprice2, memsaleprice1, memsaleprice2, inprice, outnumprice, storeprice, avgprice, wholesaleprice, storesaleprice, allotsaleprice, shopsaleprice, memsaleprice, promsaleprice,storeID,storeName&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_filter&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;TenantID= '297edeb35d0b3080015d0ce0879e30af' and states &amp;lt;&amp;gt; 2 and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_pageSize&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;20&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_tableName&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;yck_ProductInfo&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "&lt;/ArrayOfDictionaryEntry&gt;</parameters></RunProcedureAndGetTotalRecord></soap:Body></soap:Envelope>";


        for (int i = 1; i <= itemNum; i++) {
            String params = StringUtils.replace(param, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction, params);

            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html, element);

            if (dataList.size() > 0) {
                for (Element node : dataList) {
                    String productName = "";
                    Element productNameElement = node.element("PRODUCTNAME");
                    if (productNameElement != null)
                        productName = productNameElement.getText();

                    String code = "";
                    Element codeElement = node.element("STOCKNO");
                    if (codeElement != null)
                        code = codeElement.getText();

                    String unit = "";
                    Element unitElement = node.element("UNIT");
                    if (unitElement != null)
                        unit = unitElement.getText();

                    String price = "";
                    Element priceElement = node.element("SALEPRICE");
                    if (priceElement != null)
                        price = priceElement.getText();

                    String firstCategoryName = "";
                    Element firstCategoryNameElement = node.element("PROTYPENAME");
                    if (firstCategoryNameElement != null)
                        firstCategoryName = firstCategoryNameElement.getText();

                    String brandName = "";
                    Element brandNameElement = node.element("BRAND");
                    if (brandNameElement != null)
                        brandName = brandNameElement.getText();

                    String origin = "";
                    Element originElement = node.element("PRODUCTCAGE");
                    if (originElement != null)
                        origin = originElement.getText();

                    String carModel = "";
                    Element carModelElement = node.element("APPLYMODEL");
                    if (carModelElement != null)
                        carModel = carModelElement.getText();

                    Product product = new Product();
                    product.setProductName(productName);
                    product.setCode(code);
                    product.setUnit(unit);
                    product.setPrice(price);
                    product.setFirstCategoryName(firstCategoryName);
                    product.setBrandName(brandName);
                    product.setOrigin(origin);
                    product.setCarModel(carModel);
                    product.setItemType("商品");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());
        System.out.println("大小为" + products.size());

        String pathname = "C:\\exportExcel\\车赢家商品导出.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);

    }

    /**
     * 服务项目-标准模版导出
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchServiceData() throws IOException, DocumentException {
        List<Product> products = new ArrayList<>();

        String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header>" +
                "<MySoapHeader xmlns=\"http://tempuri.org/\">" +
                "<UserName>" +
                userName +
                "</UserName>" +
                "<PassWord>" +
                passWord +
                "</PassWord>" +
                "<CyjToken>" +
                cyjToken +
                "</CyjToken>" +
                "<CompanyId>" +
                companyId +
                "</CompanyId>" +
                "</MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
                "&lt;ArrayOfDictionaryEntry xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_curPage&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;{no}&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_sort&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;code&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_fields&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;id,code,name,description,costPrice,salePrice,minSalePrice,workTime,workTimePrice,workTypeId,workType,settleType,merId,merName,storesId,storesName,commission,constructionNum,constructionCost,proCostPrice,invCostPrice,isOnlyCar,mnemonicCode,ApplyModel,costObjectId,costObjectName &lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_filter&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;merid='297edeb35d0b3080015d0ce0879e30af' and (bakFive ='A' or bakFive is null or bakFive = '') and (storesid like '%297edeb35d1206b6015d169619a1254d%'or storesid='ALL')  and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_pageSize&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;20&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_tableName&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;YCK_SERVICEITEM&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "&lt;/ArrayOfDictionaryEntry&gt;</parameters></RunProcedureAndGetTotalRecord></soap:Body></soap:Envelope>";

        for (int i = 1; i <= serviceNum; i++) {
            String params = StringUtils.replace(param, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction, params);
            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html, element);

            if (dataList.size() > 0) {
                for (Element node : dataList) {
                    String productName = "";
                    Element productNameElement = node.element("NAME");
                    if (productNameElement != null)
                        productName = productNameElement.getText();

                    String code = "";
                    Element codeElement = node.element("CODE");
                    if (codeElement != null)
                        code = codeElement.getText();

                    String remark = "";
                    Element remarkElement = node.element("DESCRIPTION");
                    if (remarkElement != null)
                        remark = remarkElement.getText();

                    String price = "";
                    Element priceElement = node.element("SALEPRICE");
                    if (priceElement != null)
                        price = priceElement.getText();

                    Product product = new Product();
                    product.setProductName(productName);
                    product.setCode(code);
                    product.setRemark(remark);
                    product.setPrice(price);
                    product.setItemType("服务项");
                    products.add(product);
                }
            }
        }

        System.out.println("结果为" + products.toString());
        System.out.println("大小为" + products.size());

        String pathname = "C:\\exportExcel\\车赢家服务项目导出.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 供应商-标准模版导出
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchSupplierData() throws IOException, DocumentException {
        List<Supplier> suppliers = new ArrayList<>();

        String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header>" +
                "<MySoapHeader xmlns=\"http://tempuri.org/\">" +
                "<UserName>" +
                userName +
                "</UserName>" +
                "<PassWord>" +
                passWord +
                "</PassWord>" +
                "<CyjToken>" +
                cyjToken +
                "</CyjToken>" +
                "<CompanyId>" +
                companyId +
                "</CompanyId>" +
                "</MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
                "&lt;ArrayOfDictionaryEntry xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_curPage&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;{no}&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_sort&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;comCode&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_fields&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;payType,id,tenantID,tenantCode,tenantName,comCode,company,comGroup,comType,email,contacts,conTel,fax,mobile,comPage,county,province,city,post,address,openBank,bankAccount,taxNum,state,remark,createEmp,createEmpId,createDate,storeID,storeName&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_filter&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;tenantID='297edeb35d0b3080015d0ce0879e30af'  and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_pageSize&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;20&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_tableName&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;yck_supplierManager&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "&lt;/ArrayOfDictionaryEntry&gt;</parameters></RunProcedureAndGetTotalRecord></soap:Body></soap:Envelope>";

        for (int i = 1; i <= supplierNum; i++) {

            String params = StringUtils.replace(param, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction, params);

            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html,element);
            if (dataList.size() > 0) {
                for (Element node : dataList) {
                    String companyName = "";
                    Element companyNameElement = node.element("STORENAME");
                    if (companyNameElement != null)
                        companyName = companyNameElement.getText();

                    String address = "";
                    Element addressElement = node.element("ADDRESS");
                    if (addressElement != null)
                        address = addressElement.getText();

                    String contactName = "";
                    Element contactNameElement = node.element("CONTACTS");
                    if (contactNameElement != null)
                        contactName = contactNameElement.getText();

                    String contactPhone = "";
                    Element contactPhoneElement = node.element("MOBILE");
                    if (contactPhoneElement != null)
                        contactPhone = contactPhoneElement.getText();

                    String name = "";
                    Element nameElement = node.element("COMPANY");
                    if (nameElement != null)
                        name = nameElement.getText();

                    String phone = "";
                    Element phoneElement = node.element("CONTEL");
                    if (phoneElement != null)
                        phone = phoneElement.getText();

                    String code = "";
                    Element codeElement = node.element("COMCODE");
                    if (codeElement != null)
                        code = codeElement.getText();

                    String remark = "";
                    Element remarkElement = node.element("REMARK");
                    if (remarkElement != null)
                        remark = remarkElement.getText();

                    Supplier supplier = new Supplier();
                    supplier.setCompanyName(companyName);
                    supplier.setName(name);
                    supplier.setPhone(phone);
                    supplier.setContactName(contactName);
                    supplier.setContactPhone(contactPhone);
                    supplier.setCode(code);
                    supplier.setRemark(remark);
                    supplier.setAddress(address);
                    supplier.setFax(phone);//固话
                    suppliers.add(supplier);
                }
            }
        }

        System.out.println("结果为" + suppliers.toString());
        System.out.println("大小为" + suppliers.size());

        String pathname = "C:\\exportExcel\\车赢家供应商导出.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }

    /**
     * 车辆信息-标准模版导出
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchCarInfoData() throws IOException, DocumentException {
        List<CarInfo> carInfos = new ArrayList<>();

        String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                "<soap:Header>" +
                "<MySoapHeader xmlns=\"http://tempuri.org/\">" +
                "<UserName>" +
                userName +
                "</UserName>" +
                "<PassWord>" +
                passWord +
                "</PassWord>" +
                "<CyjToken>" +
                cyjToken +
                "</CyjToken>" +
                "<CompanyId>" +
                companyId +
                "</CompanyId>" +
                "</MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
                "&lt;ArrayOfDictionaryEntry xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_curPage&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;{no}&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_sort&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;LEAGUERNUM desc,LEAGUERNAME,MOBILE&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_fields&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;ID,LEAGUERNUM,MERID,LEAGUERNAME,ABBREVIATION,ZJTYPE,ZJNUM,SEX,BIRTHDAY,EMAIL,PHONE,MOBILE,ADDRESS,ZIPCODE,EDULEVEL,TRADETYPE,POST,COMTYPE,LEAGUERAREA,PIN,REGTIME,LOGOUTTIME,LEAGUERSTATE,C_SORTINDEX,CREATEDATE,CREATEEMP,LASTUPDATEDATE,UPDATEEMP,BAKONE,BAKTWO,BAKTHREE,BAKFOUR,BAKFIVE,BAKSIX,BAKSEVEN,BAKEIGHT,BAKNINE,BAKTEN,LEAGUERTYPEID,LEAGUERTYPE,MERNAME,STORESID,STORESNAME,ISONCREDIT,MAXCREDIT,ACCOUNTDAY,CLIENTMANAGERID,CLIENTMANAGER,CUSTOMERSOURCEID,CUSTOMERSOURCE,CARNUMBER,INTYPE,HYMONEY,INTEGRAL,MOBILEONE,case CUSTOMERSOURCE when '微信' then '微信' else '' end as ISWEIXIN, case leaguerState when '2' then '冻结客户' when '3' then '领养客户' else '正常客户' end as StateName,ADOPTTIME,LEVELNAME,LASTONSTORETIME,LASTVISITTIME,SumConsumptionAccount,SumConsumptionCount,FirstOnStoreDate &lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_filter&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt; merid='297edeb35d0b3080015d0ce0879e30af'  and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_pageSize&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;20&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_tableName&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;yck_leaguerInfo&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "&lt;/ArrayOfDictionaryEntry&gt;</parameters></RunProcedureAndGetTotalRecord></soap:Body></soap:Envelope>";


        String param2="<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><MySoapHeader xmlns=\"http://tempuri.org/\"><UserName>297edeb35a0eb000015a1baf767104da</UserName><PassWord>3844FFFB48E49782625F10D54E4AACCD</PassWord><CyjToken>2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585</CyjToken><CompanyId>297edeb357fb144a01580046ab245d37</CompanyId></MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
                "&lt;ArrayOfDictionaryEntry xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_curPage&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;{no}&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_sort&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;LEAGUERNUM desc,LEAGUERNAME,MOBILE&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_fields&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;ID,LEAGUERNUM,MERID,LEAGUERNAME,ABBREVIATION,ZJTYPE,ZJNUM,SEX,BIRTHDAY,EMAIL,PHONE,MOBILE,ADDRESS,ZIPCODE,EDULEVEL,TRADETYPE,POST,COMTYPE,LEAGUERAREA,PIN,REGTIME,LOGOUTTIME,LEAGUERSTATE,C_SORTINDEX,CREATEDATE,CREATEEMP,LASTUPDATEDATE,UPDATEEMP,BAKONE,BAKTWO,BAKTHREE,BAKFOUR,BAKFIVE,BAKSIX,BAKSEVEN,BAKEIGHT,BAKNINE,BAKTEN,LEAGUERTYPEID,LEAGUERTYPE,MERNAME,STORESID,STORESNAME,ISONCREDIT,MAXCREDIT,ACCOUNTDAY,CLIENTMANAGERID,CLIENTMANAGER,CUSTOMERSOURCEID,CUSTOMERSOURCE,CARNUMBER,INTYPE,HYMONEY,INTEGRAL,MOBILEONE,case CUSTOMERSOURCE when '微信' then '微信' else '' end as ISWEIXIN, case leaguerState when '2' then '冻结客户' when '3' then '领养客户' else '正常客户' end as StateName,ADOPTTIME,LEVELNAME,LASTONSTORETIME,LASTVISITTIME,SumConsumptionAccount,SumConsumptionCount,FirstOnStoreDate &lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_filter&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt; merid='297edeb357fb144a01580046ab245d37'  and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_pageSize&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:int\"&gt;20&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "  &lt;DictionaryEntry&gt;\n" +
                "    &lt;Key xsi:type=\"xsd:string\"&gt;p_tableName&lt;/Key&gt;\n" +
                "    &lt;Value xsi:type=\"xsd:string\"&gt;yck_leaguerInfo&lt;/Value&gt;\n" +
                "  &lt;/DictionaryEntry&gt;\n" +
                "&lt;/ArrayOfDictionaryEntry&gt;</parameters></RunProcedureAndGetTotalRecord></soap:Body></soap:Envelope>";


        for (int i = 1; i <= carInfoNum; i++) {

            String params = StringUtils.replace(param2, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction,params);

            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html,element);
            if (dataList.size() > 0) {
                for (Element node : dataList) {

                    String phone = "";
                    Element phoneElement = node.element("MOBILE");
                    if (phoneElement != null)
                        phone = phoneElement.getText();

                    String name = "";
                    Element nameElement = node.element("LEAGUERNAME");
                    if (nameElement != null)
                        name = nameElement.getText();

                    String carNumber = "";
                    Element carNumberElement = node.element("CARNUMBER");
                    if (carNumberElement != null)
                        carNumber = carNumberElement.getText();

                    String companyName = "";
                    Element companyNameElement = node.element("STORESNAME");
                    if (companyNameElement != null)
                        companyName = companyNameElement.getText();

                    String remark = "";
                    Element remarkElement = node.element("PHONE");
                    if (remarkElement != null)
                        remark = remarkElement.getText();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCompanyName(companyName);
                    carInfo.setName(name);
                    carInfo.setPhone(phone);
                    carInfo.setCarNumber(carNumber);
                    carInfo.setRemark(remark);
                    carInfos.add(carInfo);
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("结果为" + carInfos.size());

        String pathname = "C:\\exportExcel\\车赢家车辆信息导出.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, ExcelDatas.workbook, pathname);
    }

    private List<Element> getDataList(String response, String target) throws DocumentException {
        Document doc = DocumentHelper.parseText(response);
        Element root = doc.getRootElement();
        Element body = root.element("Body");
        Element resp = body.element("RunProcedureAndGetTotalRecordResponse");
        Element result = resp.element("RunProcedureAndGetTotalRecordResult");
        Element diff = result.element("diffgram");
        Element dataSet = diff.element("NewDataSet");
        List<Element> dataList = dataSet.elements(target);

        return dataList;
    }

    private List<Element> getQueryDataList(String response, String target) throws DocumentException {
        Document doc = DocumentHelper.parseText(response);
        Element root = doc.getRootElement();
        Element body = root.element("Body");
        Element resp = body.element("QueryResponse");
        Element result = resp.element("QueryResult");
        Element diff = result.element("diffgram");
        Element dataSet = diff.element("NewDataSet");
        List<Element> dataList = dataSet.elements(target);

        return dataList;
    }
}
