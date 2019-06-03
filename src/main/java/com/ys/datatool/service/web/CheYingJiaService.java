package com.ys.datatool.service.web;


import com.ys.datatool.domain.config.ExcelDatas;
import com.ys.datatool.domain.entity.*;
import com.ys.datatool.util.CommonUtil;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.DateUtil;
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


    /**
     * 使用历史消费记录工具需要填写的参数
     * userName、passWord、cyjToken、companyId
     */
    private String userName = "297edeb35a231435015a31ebc5521551";

    private String passWord = "D11626809E127CD55CB1BFEBFB0BC39F";

    private String cyjToken = "2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585";

    private String companyId = "297edeb3569c18dc01569cf836cd1a22";


    /**
     * 工具使用前
     * 1.填写每个页面的总页数的值
     * 2.填写每个方法要求的传参的值
     * 3.填写解析返回数据传参 element的值
     */
    //解析返回数据传参(这参数非常重要！！！使用方法前需要修改这个值----------卡内项目方法不用改)
    private String element = "_x0031_380";

    //车辆信息总页数
    private int carInfoNum = 93;

    //供应商总页数
    private int supplierNum = 8;

    //服务项目总页数
    private int serviceNum = 3;

    //商品总页数
    private int itemNum = 155;

    //会员卡总页数
    private int memberCardNum = 69;

    //订单开始时间
    private String billStartDate = "2005/01/01";


    //////////////////////////////车辆信息传参(找到当前页数的值，并修改为{no})///////////////////////////////////////////////////////////////////////////////////////


    private String carInfoParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><MySoapHeader xmlns=\"http://tempuri.org/\"><UserName>297edeb3576b347701576fc49df54f12</UserName><PassWord>3794B30A3ECE4999685E6144367CB911</PassWord><CyjToken>2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585</CyjToken><CompanyId>297edeb3574bb64b0157512a6d082f72</CompanyId></MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
            "&lt;ArrayOfDictionaryEntry xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\n" +
            "  &lt;DictionaryEntry&gt;\n" +
            "    &lt;Key xsi:type=\"xsd:string\"&gt;p_curPage&lt;/Key&gt;\n" +
            "    &lt;Value xsi:type=\"xsd:int\"&gt;4&lt;/Value&gt;\n" +
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
            "    &lt;Value xsi:type=\"xsd:string\"&gt; merid='297edeb3574bb64b0157512a6d082f72'  and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
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


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////供应商传参(找到当前页数的值，并修改为{no})///////////////////////////////////////////////////////////////////////////////////////

    private String supplierParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><MySoapHeader xmlns=\"http://tempuri.org/\"><UserName>297edeb35a231435015a31ebc5521551</UserName><PassWord>D89CFE423C0F28DE35D3F2ED9AA2CF09</PassWord><CyjToken>2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585</CyjToken><CompanyId>297edeb3569c18dc01569cf836cd1a22</CompanyId></MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
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
            "    &lt;Value xsi:type=\"xsd:string\"&gt;tenantID='297edeb3569c18dc01569cf836cd1a22'  and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
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


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////服务项目传参(找到当前页数的值，并修改为{no})///////////////////////////////////////////////////////////////////////////////////////

    private String serviceParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><MySoapHeader xmlns=\"http://tempuri.org/\"><UserName>297edeb3576b347701576fc49df54f12</UserName><PassWord>7141F1540257FD4FF6B377388870EF11</PassWord><CyjToken>2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585</CyjToken><CompanyId>297edeb3574bb64b0157512a6d082f72</CompanyId></MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
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
            "    &lt;Value xsi:type=\"xsd:string\"&gt;merid='297edeb3574bb64b0157512a6d082f72' and (bakFive ='A' or bakFive is null or bakFive = '') and (storesid like '%297edeb3576b347701576fa84c7445b7%'or storesid='ALL')  and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
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


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////商品传参(找到当前页数的值，并修改为{no})///////////////////////////////////////////////////////////////////////////////////////

    private String itemParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><MySoapHeader xmlns=\"http://tempuri.org/\"><UserName>297edeb35a231435015a31ebc5521551</UserName><PassWord>D89CFE423C0F28DE35D3F2ED9AA2CF09</PassWord><CyjToken>2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585</CyjToken><CompanyId>297edeb3569c18dc01569cf836cd1a22</CompanyId></MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
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
            "    &lt;Value xsi:type=\"xsd:string\"&gt;TenantID= '297edeb3569c18dc01569cf836cd1a22' and states &amp;lt;&amp;gt; 2 and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
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


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////会员卡传参(找到当前页数的值，并修改为{no})///////////////////////////////////////////////////////////////////////////////////////

    private String memberCardParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><MySoapHeader xmlns=\"http://tempuri.org/\"><UserName>297edeb3576b347701576fc49df54f12</UserName><PassWord>3794B30A3ECE4999685E6144367CB911</PassWord><CyjToken>2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585</CyjToken><CompanyId>297edeb3574bb64b0157512a6d082f72</CompanyId></MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
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
            "    &lt;Value xsi:type=\"xsd:string\"&gt;(merId='297edeb3574bb64b0157512a6d082f72' and (stats is null or stats &amp;lt;&amp;gt;'6')) and (attribute is null or attribute='N')&lt;/Value&gt;\n" +
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////卡内项目传参(找到 cardInfoId=，并修改为 cardInfoId='{no}' )///////////////////////////////////////////////////////////////////////////////////////


    private String memberCardItemParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><MySoapHeader xmlns=\"http://tempuri.org/\"><UserName>297edeb3576b347701576fc49df54f12</UserName><PassWord>3794B30A3ECE4999685E6144367CB911</PassWord><CyjToken>2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585</CyjToken><CompanyId>297edeb3574bb64b0157512a6d082f72</CompanyId></MySoapHeader></soap:Header><soap:Body><Query xmlns=\"http://tempuri.org/\"><SQLString>select id,cardInfoId,itemId,itemCode,itemName,price,tolPrice,costprice,costtolprice,num,settleType,case when dayNum=''OR dayNum IS NULL THEN '0'ELSE dayNum END dayNum,surplusNum,useNum FROM yck_cardinfodetail  where cardInfoId='{no}'</SQLString></Query></soap:Body></soap:Envelope>";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////单据传参///////////////////////////////////////////////////////////////////////////////////////


    private String billParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
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
            "</MySoapHeader>" +
            "</soap:Header>" +
            "<soap:Body>" +
            "<Query xmlns=\"http://tempuri.org/\">" +
            "<SQLString>select '0' as IsSelect,ID,MentNo,MemberID,MemberName,MemberCard,StoreID," +
            "Reimbursement,cashier,FrameNum,MotoType,LicenNum,RoadHaul,ServiceConsultant," +
            "ServiceConsultantID,cashierID,BillingDate,EntranceTime,StartTime,completeTime," +
            "deliverTime,SettTime,InsDueDate,ServiceType,ProjectPrice,AccessPrice,TotalPay," +
            "ReceivePrice,Discount,FreePrice,OtherPrice,ReducePrice,ReceivablePrice,ConsumPoints," +
            "PresentPoints,case Status when 0 then '未结算' when 1 then '已结算' when 2 then '作废' end as Statu,Status,Remark,MemberCardID," +
            "MemberCardMoney,IsDelete,IsUpLoad,[OrderName],[CarColor],[EngineModel],[EngineNum],[Transmissionmodel]," +
            "[TelPerson],[TelNumber],[TrappedFuel],[CloseTime],[NmaintenanceTime],[RemainAmont]," +
            "[AppointmentTime] ,StoreName,SERVICETAX ,TOTALMANHOUR ,NOTAXTOTALSUM ,TAXTOTALSUM ,TAXSUM " +
            ",TOTALSUMCAPS,FACTTOTALSUM,COSTSUM ,PROFITSUM ,StatusName , ORDERSTYLE,ORDERSTATE , BALANCETYPE " +
            ",ACCOUNTORDERID,KSERVICENOTAXTOTALSUM,KSERVICETAXTOTALSUM,Abstract,CarBrand,Invoiced,invoiceType," +
            "invoiceTitle,invoiceNumber,taxRate,invoiceDate,case when Invoiced='0' then null else invoiceDate end as invoiceActualDate, " +
            "case when Invoiced='1' then '待审核' when Invoiced='2' then '已审核' when Invoiced='3' then '已入账' else '未开发票' end as InvoicedStatus," +
            "Source,(case Source when '1' then 'APP' when '2' then '快捷开单' else '' end) as SourceName,SettUserName " +
            "from bcs_ConSettlement a  where  a.status!=-1 and  " +
            "a.StoreID='" +
            "297edeb35a231435015a25e1070d0b1b" +
            "' and ordername&lt;&gt;'积分换购单' and a.ordername&lt;&gt;'返修单' and a.ordername&lt;&gt;'结算调整单'  " +
            "and a.BillingDate &gt;= '" +
            billStartDate +
            "' and a.BillingDate &lt;= '" +
            fetchEndDate() +
            "'</SQLString>" +
            "</Query>" +
            "</soap:Body>" +
            "</soap:Envelope>";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////单据服务传参///////////////////////////////////////////////////////////////////////////////////////

    private String billServiceParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
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
            "</MySoapHeader>" +
            "</soap:Header>" +
            "<soap:Body>" +
            "<Query xmlns=\"http://tempuri.org/\">" +
            "<SQLString>select ProjectCode Code,ROW_NUMBER() OVER(ORDER BY showIndex) as cNo,case WorkStatus when '10' then '质检通过' when '11' then '质检失败' end ZJStatus,WorkStatus,ID,MentID,ProjectID," +
            "PROJECTNAME,minSalePrice,costPrice,salePrice,workTime,workTypeId,workType,majorName,minorName,shopName," +
            "royaltyRate,Discount,TotalPay,IsDelete,IsUpLoad,NUM,SURPLUSNUM,USENUM,THISNUM,ProductTax," +
            "TaxSalePrice,TaxTotalSum,TaxSum,CoStSum,Profitsum,ProjectCode,WorkTimePrice,1.0 as ISSELECT,costObjectID," +
            "costObjectName,discountRate,showIndex,remark,projectNum,shopNameId,sourcePrice,TirePositCode,editPrice," +
            "TirePositCode,TirePositName,Tirekm,Treadpattern,TireOutTime,brakepad,'1' as isprint,WorkTimePrice,RebatesAccount FROM bcs_ConSettlProject  " +
            "where MentID='" +
            "{mentID}" +
            "' and ProjectType=0 order by showIndex" +
            "</SQLString>" +
            "</Query>" +
            "</soap:Body>" +
            "</soap:Envelope>";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////单据配件传参///////////////////////////////////////////////////////////////////////////////////////

    private String billItemParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
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
            "</MySoapHeader>" +
            "</soap:Header>" +
            "<soap:Body>" +
            "<Query xmlns=\"http://tempuri.org/\">" +
            "<SQLString>" +
            "select IsInStore,(case IsInStore when '1' then '已确认' else '' end) as IsInStoreText,remark,ROW_NUMBER() OVER(ORDER BY id desc) as cNo,ID,MentProID,ProAccessID,Brand,ProjectID," +
            "AccessName,AccessCode,lowSalePrice,salePrice,Discount,Num,TotalPay,IsDelete,IsUpLoad,price,SaleTime," +
            "ProductTax,TaxSalePrice,TaxTotalSum,TaxSum,CoStSum,Profitsum,ProuductNo,specModel,Isgive,Issetmeal," +
            "Ispromotion,inSotreType,supplierID,supplier,batchNumber,DiscountPrice,warehouseName,warehouseCODE," +
            "warehouseID,costObjectID,costObjectName,discountRate,showIndex,unit,inStoreID,shopName,shopNameId," +
            "IsConfirm,(case IsConfirm when 1 then '已确认' else '' end) as IsConfirmText," +
            "sourcePrice,editPrice,'1' as ISSELECT  from bcs_ConSettlProjectAccess where " +
            "MentID ='" +
            "{mentID}" +
            "' order by showIndex" +
            "</SQLString>" +
            "</Query>" +
            "</soap:Body>" +
            "</soap:Envelope>";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////卡结算单明细传参///////////////////////////////////////////////////////////////////////////////////////


    private String billSettleTypeParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
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
            "</MySoapHeader>" +
            "</soap:Header>" +
            "<soap:Body>" +
            "<Query xmlns=\"http://tempuri.org/\">" +
            "<SQLString>select ProjectCode Code,ROW_NUMBER() OVER(ORDER BY showIndex) as cNo," +
            "case WorkStatus when '10' then '质检通过' when '11' then '质检失败' end ZJStatus,WorkStatus,ID,MentID," +
            "ProjectID,PROJECTNAME,minSalePrice,costPrice,salePrice,workTime,workTypeId,workType,majorName," +
            "minorName,shopName,royaltyRate,Discount,TotalPay,IsDelete,IsUpLoad,NUM,SURPLUSNUM,USENUM,THISNUM," +
            "ProductTax,TaxSalePrice,TaxTotalSum,TaxSum,CoStSum,Profitsum,ProjectCode,WorkTimePrice,1.0 as ISSELECT," +
            "costObjectID,costObjectName,discountRate,showIndex,remark,projectNum,shopNameId,sourcePrice," +
            "TirePositCode,editPrice,TirePositCode,TirePositName,Tirekm,Treadpattern,TireOutTime," +
            "brakepad,'1' as isprint,WorkTimePrice,RebatesAccount " +
            "FROM bcs_ConSettlProject  where " +
            "MentID='" +
            "{mentID}" +
            "' and ProjectType=" +
            "2 " +
            "</SQLString>" +
            "</Query>" +
            "</soap:Body>" +
            "</soap:Envelope>";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////商品零售单明细传参///////////////////////////////////////////////////////////////////////////////////////


    private String billSaleParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
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
            "</MySoapHeader>" +
            "</soap:Header>" +
            "<soap:Body>" +
            "<Query xmlns=\"http://tempuri.org/\">" +
            "<SQLString>select IsInStore,(case IsInStore when '1' then '已确认' else '' end) as IsInStoreText," +
            "row_number() over (order by showIndex) as rn,ID,SaleID,AccountSet,ProductID,StorageID," +
            "ProductName,ProductSpecification,ProductPrice,Discount,Amount,TotalPay,IsConfirmed," +
            "ModifyTime,Comment,Status,batchNumber,Reserved1,Reserved2,Reserved3,Reserved4,Reserved5," +
            "Reserved6,Reserved7,Reserved8,Reserved9,Reserved10,Reserved11,Reserved12,Reserved13," +
            "Reserved14,Reserved15,Reserved16,ProductTax,TaxSalePrice,TaxTotalSum,TaxSum,CostSum," +
            "ProfitSum,ProuductNo,DiscountPrice,warehouseName,warehouseCODE,warehouseID,costObjectID," +
            "costObjectName,discountRate,showIndex,unit,inStoreID," +
            "(case IsConfirmed when '1' then '已确认' else '' end) as IsConfirmText,sourcePrice," +
            "editPrice,'1' as ISGOODSELECT  " +
            "FROM bcs_SaleItem  where  " +
            "SaleID='" +
            "{saleId}" +
            "' order by showIndex" +
            "</SQLString>" +
            "</Query>" +
            "</soap:Body>" +
            "</soap:Envelope>";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////项目结算单明细传参///////////////////////////////////////////////////////////////////////////////////////


    private String serviceSettleTypeParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
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
            "</MySoapHeader>" +
            "</soap:Header>" +
            "<soap:Body>" +
            "<Query xmlns=\"http://tempuri.org/\">" +
            "<SQLString>select ProjectCode Code,ROW_NUMBER() OVER(ORDER BY showIndex) as cNo," +
            "case WorkStatus when '10' then '质检通过' when '11' then '质检失败' end ZJStatus," +
            "WorkStatus,ID,MentID,ProjectID,PROJECTNAME,minSalePrice,costPrice,salePrice," +
            "workTime,workTypeId,workType,majorName,minorName,shopName,royaltyRate,Discount," +
            "TotalPay,IsDelete,IsUpLoad,NUM,SURPLUSNUM,USENUM,THISNUM,ProductTax,TaxSalePrice," +
            "TaxTotalSum,TaxSum,CoStSum,Profitsum,ProjectCode,WorkTimePrice,1.0 " +
            "as ISSELECT,costObjectID,costObjectName,discountRate,showIndex,remark,projectNum," +
            "shopNameId,sourcePrice,TirePositCode,editPrice,TirePositCode,TirePositName,Tirekm," +
            "Treadpattern,TireOutTime,brakepad,'1' as isprint,WorkTimePrice,RebatesAccount " +
            "FROM bcs_ConSettlProject  where " +
            "MentID='" +
            "{mentID}" +
            "' and ProjectType=" +
            "0 " +
            "order by showIndex</SQLString>" +
            "</Query>" +
            "</soap:Body>" +
            "</soap:Envelope>";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////洗车单明细传参///////////////////////////////////////////////////////////////////////////////////////


    private String carWashParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
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
            "</MySoapHeader>" +
            "</soap:Header>" +
            "<soap:Body>" +
            "<Query xmlns=\"http://tempuri.org/\">" +
            "<SQLString>select ProjectCode Code,ROW_NUMBER() OVER(ORDER BY showIndex) as cNo," +
            "case WorkStatus when '10' then '质检通过' when '11' then '质检失败' end ZJStatus," +
            "WorkStatus,ID,MentID,ProjectID,PROJECTNAME,minSalePrice,costPrice,salePrice," +
            "workTime,workTypeId,workType,majorName,minorName,shopName,royaltyRate," +
            "Discount,TotalPay,IsDelete,IsUpLoad,NUM,SURPLUSNUM,USENUM,THISNUM,ProductTax," +
            "TaxSalePrice,TaxTotalSum,TaxSum,CoStSum,Profitsum,ProjectCode," +
            "WorkTimePrice,1.0 as ISSELECT,costObjectID,costObjectName,discountRate," +
            "showIndex,remark,projectNum,shopNameId,sourcePrice,TirePositCode,editPrice," +
            "TirePositCode,TirePositName,Tirekm,Treadpattern,TireOutTime," +
            "brakepad,'1' as isprint,WorkTimePrice,RebatesAccount " +
            "FROM bcs_ConSettlProject  where " +
            "MentID='" +
            "{mentID}" +
            "' and ProjectType=" +
            "0 " +
            "</SQLString>" +
            "</Query>" +
            "</soap:Body>" +
            "</soap:Envelope>";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String companyName = "车赢家";

    private String url = "http://61.186.130.102:803/YCKService.asmx";

    private String BillURL = "http://61.186.130.102:803/BCSService.asmx";

    private String SOAPAction = "http://tempuri.org/RunProcedureAndGetTotalRecord";

    private String QUERYSOAPAction = "http://tempuri.org/Query";

    private Charset charset = Charset.forName("UTF-8");


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 历史消费记录和消费记录相关车辆
     * <p>
     * 消费记录相关传参:
     * 1.单据传参: billParam
     * 2.单据配件传参:billItemParam
     * 3.单据服务传参:billServiceParam
     * 4.卡结算单明细传参:billServiceTypeParam
     * 5.商品零售单:billSaleParam
     * <p>
     * 打开路径:前台收银-全部销售订单
     * 服务项目-服务项目与配件
     * 商品-商品信息
     *
     * @throws IOException
     */
    @Test
    public void fetchConsumptionRecordDataStandard() throws IOException, DocumentException {
        List<Bill> bills = new ArrayList<>();
        List<BillDetail> billDetails = new ArrayList<>();

        Response res = ConnectionUtil.doPostWithSOAP(BillURL, QUERYSOAPAction, billParam);
        String billHtml = res.returnContent().asString(charset);

        if (billHtml.contains("</NewDataSet>")) {
            String target = "ds";

            List<Element> dataList = getQueryDataList(billHtml, target);

            if (dataList.size() > 0) {
                for (Element node : dataList) {

                    String billNo = "";
                    Element billNoElement = node.element("MentNo");
                    if (billNoElement != null)
                        billNo = billNoElement.getText();

                    String carNumber = "";
                    Element carNumberElement = node.element("LicenNum");
                    if (carNumberElement != null)
                        carNumber = carNumberElement.getText();

                    String mileage = "";
                    Element mileageElement = node.element("RoadHaul");
                    if (mileageElement != null)
                        mileage = mileageElement.getText();

                    String phone = "";
                    Element phoneElement = node.element("TelNumber");
                    if (phoneElement != null)
                        phone = phoneElement.getText();


                    String name = "";
                    Element nameElement = node.element("TelPerson");
                    if (nameElement != null)
                        name = nameElement.getText();

                    String totalAmount = "";
                    Element totalAmountElement = node.element("TotalPay");
                    if (totalAmountElement != null)
                        totalAmount = totalAmountElement.getText();


                    String dateEnd = "";
                    Element dateEndElement = node.element("CloseTime");
                    if (dateEndElement != null)
                        dateEnd = dateEndElement.getText();

                    String receptionistName = "";
                    Element receptionistNameElement = node.element("cashier");
                    if (receptionistNameElement != null)
                        receptionistName = receptionistNameElement.getText();

                    String id = "";
                    Element idElement = node.element("ID");
                    if (idElement != null)
                        id = idElement.getText();


                    String remark = "";
                    Element remarkElement = node.element("Remark");
                    if (remarkElement != null)
                        remark = remarkElement.getText();

                    String billCode = "";
                    Element billCodeElement = node.element("OrderName");
                    if (billCodeElement != null)
                        billCode = billCodeElement.getText();

                    String billId = "";
                    Element billIdElement = node.element("ID");
                    if (billIdElement != null)
                        billId = billIdElement.getText();


                    Bill bill = new Bill();
                    bill.setBillId(billId);
                    bill.setId(id);
                    bill.setMileage(mileage);
                    bill.setName(name);
                    bill.setPhone(phone);
                    bill.setBillNo(billNo);
                    bill.setCompanyName(companyName);
                    bill.setReceptionistName(receptionistName);
                    bill.setCarNumber(carNumber);
                    bill.setTotalAmount(totalAmount);
                    bill.setDateAdded(dateEnd);
                    bill.setDateEnd(dateEnd);
                    bill.setRemark(remark);
                    bill.setBillCode(billCode);
                    bill.setActualAmount(totalAmount);
                    bills.add(bill);

                }
            }
        }

        if (bills.size() > 0) {
            for (Bill bill : bills) {
                String id = bill.getId();
                String billId = bill.getBillId();

                /////////////////////////////单据配件////////////////////////////////////////////////////////////////////////////////

                String itemParam = StringUtils.replace(billItemParam, "{mentID}", id);
                Response res2 = ConnectionUtil.doPostWithSOAP(BillURL, QUERYSOAPAction, itemParam);
                String itemHtml = res2.returnContent().asString(charset);

                if (itemHtml.contains("</NewDataSet>")) {
                    String target = "ds";

                    List<Element> dataList = getQueryDataList(itemHtml, target);
                    if (dataList.size() > 0) {
                        for (Element node : dataList) {

                            String itemName = "";
                            Element itemNameElement = node.element("AccessName");
                            if (itemNameElement != null)
                                itemName = itemNameElement.getText();

                            String itemCode = "";
                            Element itemCodeElement = node.element("AccessCode");
                            if (itemCodeElement != null)
                                itemCode = itemCodeElement.getText();


                            String num = "";
                            Element numElement = node.element("Num");
                            if (numElement != null)
                                num = numElement.getText();

                            String price = "";
                            Element priceElement = node.element("TotalPay");
                            if (priceElement != null)
                                price = priceElement.getText();

                            String firstCategoryName = "";
                            Element firstCategoryNameElement = node.element("costObjectName");
                            if (firstCategoryNameElement != null)
                                firstCategoryName = firstCategoryNameElement.getText();

                            BillDetail billDetail = new BillDetail();
                            billDetail.setCompanyName(companyName);
                            billDetail.setBillNo(bill.getBillNo());
                            billDetail.setNum(num);
                            billDetail.setItemCode(itemCode);
                            billDetail.setPrice(price);
                            billDetail.setFirstCategoryName(firstCategoryName);
                            billDetail.setItemName(itemName);
                            billDetail.setItemType("配件");
                            billDetails.add(billDetail);
                        }
                    }
                }


                /////////////////////////////单据服务////////////////////////////////////////////////////////////////////////////////

                String serviceParam = StringUtils.replace(billServiceParam, "{mentID}", id);
                Response res3 = ConnectionUtil.doPostWithSOAP(BillURL, QUERYSOAPAction, serviceParam);
                String serviceHtml = res3.returnContent().asString(charset);

                if (serviceHtml.contains("</NewDataSet>")) {
                    String target = "ds";
                    List<Element> dataList = getQueryDataList(serviceHtml, target);

                    if (dataList.size() > 0) {
                        for (Element node : dataList) {

                            String itemName = "";
                            Element itemNameElement = node.element("PROJECTNAME");
                            if (itemNameElement != null)
                                itemName = itemNameElement.getText();

                            String itemCode = "";
                            Element itemCodeElement = node.element("ProjectCode");
                            if (itemCodeElement != null)
                                itemCode = itemCodeElement.getText();

                            String num = "";
                            Element numElement = node.element("projectNum");
                            if (numElement != null)
                                num = numElement.getText();

                            String price = "";
                            Element priceElement = node.element("TotalPay");
                            if (priceElement != null)
                                price = priceElement.getText();

                            String firstCategoryName = "";
                            Element firstCategoryNameElement = node.element("costObjectName");
                            if (firstCategoryNameElement != null)
                                firstCategoryName = firstCategoryNameElement.getText();


                            BillDetail billDetail = new BillDetail();
                            billDetail.setCompanyName(companyName);
                            billDetail.setNum(num);
                            billDetail.setBillNo(bill.getBillNo());
                            billDetail.setItemCode(itemCode);
                            billDetail.setPrice(price);
                            billDetail.setFirstCategoryName(firstCategoryName);
                            billDetail.setItemName(itemName);
                            billDetail.setItemType("服务项");
                            billDetails.add(billDetail);
                        }
                    }
                }


                /////////////////////////////卡结算单明细////////////////////////////////////////////////////////////////////////////////

                String billSettleParam = StringUtils.replace(billSettleTypeParam, "{mentID}", id);
                Response res4 = ConnectionUtil.doPostWithSOAP(BillURL, QUERYSOAPAction, billSettleParam);
                String serviceTypeHtml = res4.returnContent().asString(charset);

                if (serviceTypeHtml.contains("</NewDataSet>")) {
                    String target = "ds";

                    List<Element> dataList = getQueryDataList(serviceTypeHtml, target);
                    if (dataList.size() > 0) {
                        for (Element node : dataList) {


                            String itemName = "";
                            Element itemNameElement = node.element("PROJECTNAME");
                            if (itemNameElement != null)
                                itemName = itemNameElement.getText();

                            String itemCode = "";
                            Element itemCodeElement = node.element("ProjectID");
                            if (itemCodeElement != null)
                                itemCode = itemCodeElement.getText();

                            String thisUsedNum = "";
                            Element thisUsedNumElement = node.element("THISNUM");
                            if (thisUsedNumElement != null)
                                thisUsedNum = thisUsedNumElement.getText();

                            String originalNum = "";
                            Element originalNumElement = node.element("NUM");
                            if (originalNumElement != null)
                                originalNum = originalNumElement.getText();

                            String num = "";
                            Element numElement = node.element("SURPLUSNUM");
                            if (numElement != null)
                                num = numElement.getText();

                            String usedNum = "";
                            Element usedNumElement = node.element("USENUM");
                            if (usedNumElement != null)
                                usedNum = usedNumElement.getText();


                            String price = "";
                            Element priceElement = node.element("salePrice");
                            if (priceElement != null)
                                price = priceElement.getText();


                            BillDetail billDetail = new BillDetail();
                            billDetail.setCompanyName(companyName);
                            billDetail.setNum(num);
                            billDetail.setThisUsedNum(thisUsedNum);
                            billDetail.setUsedNum(usedNum);
                            billDetail.setOriginalNum(originalNum);
                            billDetail.setBillNo(bill.getBillNo());
                            billDetail.setItemCode(itemCode);
                            billDetail.setPrice(price);
                            billDetail.setItemName(itemName);
                            billDetail.setItemType("服务项");
                            billDetails.add(billDetail);


                        }
                    }
                }


                /////////////////////////////商品零售单明细////////////////////////////////////////////////////////////////////////////////

                String billSaleItemParam = StringUtils.replace(billSaleParam, "{saleId}",id);
                Response res5 = ConnectionUtil.doPostWithSOAP(BillURL, QUERYSOAPAction, billSaleItemParam);
                String billSaleHtml = res5.returnContent().asString(charset);

                if (billSaleHtml.contains("</NewDataSet>")) {
                    String target = "ds";

                    List<Element> dataList = getQueryDataList(billSaleHtml, target);
                    if (dataList.size() > 0) {

                        for (Element node : dataList) {

                            String itemName = "";
                            Element itemNameElement = node.element("ProductName");
                            if (itemNameElement != null)
                                itemName = itemNameElement.getText();

                            String firstCategoryName = "";
                            Element firstCategoryNameElement = node.element("costObjectName");
                            if (firstCategoryNameElement != null)
                                firstCategoryName = firstCategoryNameElement.getText();

                            String price = "";
                            Element priceElement = node.element("TotalPay");
                            if (priceElement != null)
                                price = priceElement.getText();

                            String num = "";
                            Element numElement = node.element("Amount");
                            if (numElement != null)
                                num = numElement.getText();

                            String itemCode = "";
                            Element itemCodeElement = node.element("ProductID");
                            if (itemCodeElement != null)
                                itemCode = itemCodeElement.getText();


                            BillDetail billDetail = new BillDetail();
                            billDetail.setCompanyName(companyName);
                            billDetail.setBillNo(bill.getBillNo());
                            billDetail.setNum(num);
                            billDetail.setItemCode(itemCode);
                            billDetail.setPrice(price);
                            billDetail.setFirstCategoryName(firstCategoryName);
                            billDetail.setItemName(itemName);
                            billDetail.setItemType("配件");
                            billDetails.add(billDetail);

                        }
                    }
                }

                /////////////////////////////项目结算单明细////////////////////////////////////////////////////////////////////////////////

                String serviceSettleParam = StringUtils.replace(serviceSettleTypeParam, "{mentID}", id);
                Response res6 = ConnectionUtil.doPostWithSOAP(BillURL, QUERYSOAPAction, serviceSettleParam);
                String serviceSettleHtml = res6.returnContent().asString(charset);

                if (serviceSettleHtml.contains("</NewDataSet>")) {
                    String target = "ds";

                    List<Element> serviceSettleElements = getQueryDataList(serviceSettleHtml, target);
                    if (serviceSettleElements.size() > 0) {
                        for (Element node : serviceSettleElements) {

                            String itemCode = "";
                            Element itemCodeElement = node.element("ProjectID");
                            if (itemCodeElement != null)
                                itemCode = itemCodeElement.getText();

                            String itemName = "";
                            Element itemNameElement = node.element("PROJECTNAME");
                            if (itemNameElement != null)
                                itemName = itemNameElement.getText();


                            String thisUsedNum = "";
                            Element thisUsedNumElement = node.element("THISNUM");
                            if (thisUsedNumElement != null)
                                thisUsedNum = thisUsedNumElement.getText();

                            String originalNum = "";
                            Element originalNumElement = node.element("NUM");
                            if (originalNumElement != null)
                                originalNum = originalNumElement.getText();

                            String num = "";
                            Element numElement = node.element("SURPLUSNUM");
                            if (numElement != null)
                                num = numElement.getText();

                            String usedNum = "";
                            Element usedNumElement = node.element("USENUM");
                            if (usedNumElement != null)
                                usedNum = usedNumElement.getText();


                            String price = "";
                            Element priceElement = node.element("salePrice");
                            if (priceElement != null)
                                price = priceElement.getText();


                            BillDetail billDetail = new BillDetail();
                            billDetail.setCompanyName(companyName);
                            billDetail.setNum(num);
                            billDetail.setThisUsedNum(thisUsedNum);
                            billDetail.setUsedNum(usedNum);
                            billDetail.setOriginalNum(originalNum);
                            billDetail.setBillNo(bill.getBillNo());
                            billDetail.setItemCode(itemCode);
                            billDetail.setPrice(price);
                            billDetail.setItemName(itemName);
                            billDetail.setItemType("服务项");
                            billDetails.add(billDetail);


                        }
                    }
                }

                /////////////////////////////洗车单明细////////////////////////////////////////////////////////////////////////////////


                String washParam = StringUtils.replace(carWashParam, "{mentID}", id);
                Response res7 = ConnectionUtil.doPostWithSOAP(BillURL, QUERYSOAPAction, washParam);
                String carWashHtml = res7.returnContent().asString(charset);

                if (carWashHtml.contains("</NewDataSet>")) {
                    String target = "ds";

                    List<Element> carWashElements = getQueryDataList(carWashHtml, target);
                    if (carWashElements.size() > 0) {
                        for (Element node : carWashElements) {

                            String price = "";
                            Element priceElement = node.element("salePrice");
                            if (priceElement != null)
                                price = priceElement.getText();

                            String itemName = "";
                            Element itemNameElement = node.element("PROJECTNAME");
                            if (itemNameElement != null)
                                itemName = itemNameElement.getText();

                            String itemCode = "";
                            Element itemCodeElement = node.element("ProjectID");
                            if (itemCodeElement != null)
                                itemCode = itemCodeElement.getText();

                            String thisUsedNum = "";
                            Element thisUsedNumElement = node.element("THISNUM");
                            if (thisUsedNumElement != null)
                                thisUsedNum = thisUsedNumElement.getText();

                            String originalNum = "";
                            Element originalNumElement = node.element("NUM");
                            if (originalNumElement != null)
                                originalNum = originalNumElement.getText();

                            String num = "";
                            Element numElement = node.element("SURPLUSNUM");
                            if (numElement != null)
                                num = numElement.getText();

                            String usedNum = "";
                            Element usedNumElement = node.element("USENUM");
                            if (usedNumElement != null)
                                usedNum = usedNumElement.getText();


                            BillDetail billDetail = new BillDetail();
                            billDetail.setCompanyName(companyName);
                            billDetail.setNum(num);
                            billDetail.setThisUsedNum(thisUsedNum);
                            billDetail.setUsedNum(usedNum);
                            billDetail.setOriginalNum(originalNum);
                            billDetail.setBillNo(bill.getBillNo());
                            billDetail.setItemCode(itemCode);
                            billDetail.setPrice(price);
                            billDetail.setItemName(itemName);
                            billDetail.setItemType("服务项");
                            billDetails.add(billDetail);

                        }
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\车赢家单据.xls";
        String pathname2 = "C:\\exportExcel\\车赢家单据明细.xls";
        ExportUtil.exportBillSomeFieldDataInLocal(bills, ExcelDatas.workbook, pathname);
        ExportUtil.exportBillDetailSomeFieldDataInLocal(billDetails, ExcelDatas.workbook, pathname2);
    }


    /**
     * 卡内项目-标准模版导出
     * 只需要修改卡内项目传参:memberCardItemParam
     * <p>
     * 打开路径:客户管理-客户卡信息-详情
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchMemberCardItemData() throws IOException, DocumentException {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Map<String, MemberCard> memberCardMap = new HashMap<>();

        //先获取所有会员卡
        for (int i = 1; i <= memberCardNum; i++) {
            String params = StringUtils.replace(memberCardParam, "{no}", String.valueOf(i));
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
                    memberCard.setCardCode(cardId);
                    memberCard.setCompanyName(companyName);
                    memberCard.setValidTime(validTime);
                    memberCardMap.put(cardId, memberCard);
                }
            }
        }

        if (memberCardMap.size() > 0) {
            for (String id : memberCardMap.keySet()) {
                String params = StringUtils.replace(memberCardItemParam, "{no}", id);
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

        String pathname = "C:\\exportExcel\\车赢家卡内项目.xlsx";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, ExcelDatas.workbook, pathname);
    }

    /**
     * 会员卡-标准模版导出
     * 会员卡总页数:memberCardNum
     * 会员卡传参:memberCardParam
     * <p>
     * 打开路径:客户管理-客户卡信息
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchMemberCardData() throws IOException, DocumentException {
        List<MemberCard> memberCards = new ArrayList<>();

        for (int i = 1; i <= memberCardNum; i++) {
            String params = StringUtils.replace(memberCardParam, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction, params);

            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html, element);

            if (dataList.size() > 0) {
                for (Element node : dataList) {
                    String memberCardName = "";
                    Element memberCardNameElement = node.element("CARDCLASSNAME");
                    if (memberCardNameElement != null)
                        memberCardName = memberCardNameElement.getText();

                    String cardId = "";
                    Element cardIdElement = node.element("ID");
                    if (cardIdElement != null)
                        cardId = cardIdElement.getText();

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

                    String state = "";
                    Element stateElement = node.element("STATS");
                    if (stateElement != null) {

                        switch (stateElement.getText()) {
                            case "0":
                                state = "未激活";
                                break;
                            case "1":
                                state = "正常";
                                break;
                            case "2":
                                state = "冻结";
                                break;
                            case "3":
                                state = "挂失";
                                break;
                            case "4":
                                state = "停用";
                                break;
                        }

                    }


                    MemberCard memberCard = new MemberCard();
                    memberCard.setMemberCardName(memberCardName);
                    memberCard.setCardCode(cardId);
                    memberCard.setName(name);
                    memberCard.setPhone(phone);
                    memberCard.setDateCreated(dateCreated);
                    memberCard.setValidTime(validTime);
                    memberCard.setCompanyName(companyName);
                    memberCard.setCarNumber(carNumber);
                    memberCard.setBalance(balance);
                    memberCard.setRemark(state);
                    memberCards.add(memberCard);
                }
            }
        }

        System.out.println("结果为" + memberCards.toString());
        System.out.println("大小为" + memberCards.size());

        String pathname = "C:\\exportExcel\\车赢家会员卡.xlsx";
        ExportUtil.exportMemberCardSomeFieldDataInLocal(memberCards, ExcelDatas.workbook, pathname);
    }

    /**
     * 商品-标准模版导出
     * 商品总页数:itemNum
     * 商品传参:itemParam
     * <p>
     * 打开路径:资料查询-商品库
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchItemData() throws IOException, DocumentException {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i <= itemNum; i++) {
            String params = StringUtils.replace(itemParam, "{no}", String.valueOf(i));
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

        String pathname = "C:\\exportExcel\\车赢家商品.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);

    }

    /**
     * 服务项目-标准模版导出
     * 服务项目总页数:serviceNum
     * 服务项目传参:serviceParam
     * <p>
     * 打开路径:资料查询-项目库
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchServiceData() throws IOException, DocumentException {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i <= serviceNum; i++) {
            String params = StringUtils.replace(serviceParam, "{no}", String.valueOf(i));
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

        String pathname = "C:\\exportExcel\\车赢家服务项目.xls";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);
    }

    /**
     * 供应商-标准模版导出
     * 供应商总页数:supplierNum
     * 供应商传参:supplierParam
     * <p>
     * 打开路径:资料查询-供应商库
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchSupplierData() throws IOException, DocumentException {
        List<Supplier> suppliers = new ArrayList<>();

        for (int i = 1; i <= supplierNum; i++) {

            String params = StringUtils.replace(supplierParam, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction, params);

            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html, element);
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

        String pathname = "C:\\exportExcel\\车赢家供应商.xls";
        ExportUtil.exportSupplierDataInLocal(suppliers, ExcelDatas.workbook, pathname);
    }

    /**
     * 车辆信息-标准模版导出
     * 车辆信息总页数: carInfoNum
     * 车辆信息传参:carInfoParam
     * <p>
     * 打开路径:客户管理-客户资料
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void fetchCarInfoData() throws IOException, DocumentException {
        List<CarInfo> carInfos = new ArrayList<>();

        for (int i = 1; i <= carInfoNum; i++) {

            String params = StringUtils.replace(carInfoParam, "{no}", String.valueOf(i));
            Response response = ConnectionUtil.doPostWithSOAP(url, SOAPAction, params);

            String html = response.returnContent().asString(charset);
            List<Element> dataList = getDataList(html, element);
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

        String pathname = "C:\\exportExcel\\车赢家车辆信息.xls";
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


    private String fetchEndDate() {
        String currentDate = DateUtil.formatCurrentDate();
        String endDate = currentDate.replace("-", "/") + " 23:59:59";

        return endDate;
    }
}
