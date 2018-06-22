package com.ys.datatool.service.web;


import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.util.ExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/6/20.
 * 车赢家系统
 */
public class CheYingJiaService {


    String url = "http://61.186.130.102:803/YCKService.asmx";

    String HOST = "61.186.130.102:803";

    String SOAPAction = "http://tempuri.org/RunProcedureAndGetTotalRecord";

    String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; MS Web Services Client Protocol 4.0.30319.42000)";

    String CONTENT_TYPE = "text/xml; charset=utf-8";

    private Workbook workbook;

    private Charset charset = Charset.forName("UTF-8");

    private int carInfoNum = 276;


    @Test
    public void testSOAP() throws IOException, DocumentException {
        List<CarInfo> carInfos = new ArrayList<>();

        String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><MySoapHeader xmlns=\"http://tempuri.org/\"><UserName>297ec67f6086c54001609ac4b8b81cdc</UserName><PassWord>8D51324FB76D92C19E625024B66AC76F</PassWord><CyjToken>2016-03-07T09:57:07.8402B59263D6E3FD3F07664C26E36637585</CyjToken><CompanyId>297edeb35d0b3080015d0ce0879e30af</CompanyId></MySoapHeader></soap:Header><soap:Body><RunProcedureAndGetTotalRecord xmlns=\"http://tempuri.org/\"><storedProcName>up_getrecordbypage</storedProcName><parameters>&lt;?xml version=\"1.0\" encoding=\"utf-16\"?&gt;\n" +
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

        for (int i = 1; i <= carInfoNum; i++) {

            String p = StringUtils.replace(param, "{no}", String.valueOf(i));
            Response response = Request.Post(url)
                    .setHeader("SOAPAction", SOAPAction)
                    .bodyString(p, ContentType.TEXT_XML)
                    .execute();

            String html = response.returnContent().asString(charset);
            Document doc = DocumentHelper.parseText(html);
            Element root = doc.getRootElement();
            Element body = root.element("Body");
            Element resp= body.element("RunProcedureAndGetTotalRecordResponse");
            Element result = resp.element("RunProcedureAndGetTotalRecordResult");
            Element diff = result.element("diffgram");
            Element dataSet = diff.element("NewDataSet");

            List<Element> dataList = dataSet.elements("_x0035_508");
            if (dataList.size() > 0) {
                for (Element node : dataList) {

                    String phone = "";
                    Element phoneElement = node.element("PHONE");
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

                    CarInfo carInfo = new CarInfo();
                    carInfo.setName(name);
                    carInfo.setPhone(phone);
                    carInfo.setCarNumber(carNumber);
                    carInfos.add(carInfo);
                }
            }
        }

        System.out.println("结果为" + carInfos.toString());
        System.out.println("结果为" + carInfos.size());

        String pathname = "D:\\车赢家车辆信息导出.xls";
        ExportUtil.exportCarInfoDataInLocal(carInfos, workbook, pathname);
    }


}
