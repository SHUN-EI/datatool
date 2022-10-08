package com.ys.datatool.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.config.JsonObject;
import com.ys.datatool.domain.config.WebConfig;
import com.ys.datatool.domain.entity.CarInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mo on   2017/6/26.
 */
public class WebClientUtil {

    public static int getTotalPageNo(Response response, String fieldName, int num) throws IOException {
        JsonNode result = JsonObject.MAPPER.readTree(response.returnContent().asString(WebConfig.CHARSET_UTF_8));
        JsonNode totalNode = result.get(WebConfig.DATAFIELDNAME).get(fieldName);
        int totalPage = WebClientUtil.getTotalPage(totalNode, num);
        return totalPage;
    }

    //<a href="javascript:__doPostBack('AspNetPager1','2349')" style="margin-right:5px;">尾页</a>

    /**
     * 取尾页的值
     * @param htmlPage
     * @return
     */
    public static String getTotalPage(HtmlPage htmlPage) {
        Document doc = Jsoup.parseBodyFragment(htmlPage.asXml());
        String lastLabelRegEx = "(?<=\\<a href=).*(?= 尾页)";
        String lastRegEx = "(?<=,').*(?=')";
        String lastLabel = CommonUtil.fetchString(doc.toString(), lastLabelRegEx);
        String total = CommonUtil.fetchString(lastLabel, lastRegEx);

        return total;
    }

    //获取总页数
    public static int getTotalPage(Response response, ObjectMapper mapper, String fieldName) throws IOException {
        JsonNode result = mapper.readTree(response.returnContent().asString());
        String totalStr = result.get(fieldName).asText();
        return Integer.parseInt(totalStr);
    }


    /**
     * 获取总页数
     *
     * @param response
     * @param mapper
     * @param fieldName
     * @param num
     * @return
     * @throws IOException
     */
    public static int getTotalPage(Response response, ObjectMapper mapper, String fieldName, int num) throws IOException {
        int totalPage = 0;

        if (response != null) {
            JsonNode result = mapper.readTree(response.returnContent().asString());
            String countStr = result.get(fieldName).asText();
            int count = Integer.parseInt(countStr);

            if (count % num == 0) {
                totalPage = count / num;
            } else
                totalPage = count / num + 1;
        }
        return totalPage;
    }

    public static int getTotalPage(JsonNode node, int num) {
        int totalPage = 0;

        if (node != null) {
            String totalStr = node.asText();
            int total = Integer.parseInt(totalStr);

            if (total % num == 0) {
                totalPage = total / num;
            } else
                totalPage = total / num + 1;
        }
        return totalPage;
    }

    /**
     * 获取总页数
     * 适用于51车宝系统，车奇士，鼎鑫
     *
     * @param url
     * @param cookie
     * @return
     * @throws IOException
     */
    public static int getHtmlTotalPage(String url, String cookie) throws IOException {
        int totalPage = 0;

        Response response = ConnectionUtil.doGetWith(url + "1", cookie);
        String html = response.returnContent().asString();
        Document document = null;
        if (StringUtils.isNotBlank(html)) {
            document = Jsoup.parse(html);
            String strTotalPage = document.select("div[class=results]").text();
            String regEx = ".*共计\\s(\\d+)\\s.*";
            String total = CommonUtil.filterString(strTotalPage, regEx);

            totalPage = Integer.parseInt(total);
        }

        return totalPage;
    }

    /**
     * 网页抓取客户ID，适用于中易智联和众途的网页端
     *
     * @param ids
     * @param page
     * @param regEx
     */
    public static void addClientIds(Set<String> ids, HtmlPage page, String getIdRegEx, String regEx, int total) {

        for (int j = 2; j <= total; j++) {
            Document doc = Jsoup.parseBodyFragment(page.asXml());
            String idStr = doc.select(StringUtils.replace(regEx, "{no}", j + "")).attr("href");
            String id = CommonUtil.fetchString(idStr, getIdRegEx);

            if (StringUtils.isNotBlank(id))
                ids.add(id);
        }
    }

    public static int getTagSize(Document document, String regEx, String tagName) {
        int tagSize = document.select(regEx).tagName(tagName).size();
        return tagSize > 0 ? tagSize : 0;
    }

    public static WebClient getWebClient() throws IOException {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.setRefreshHandler(new ThreadedRefreshHandler());
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        return webClient;
    }


    public static void exportCarInfoData(HttpServletResponse httpServletResponse, List<CarInfo> carInfos, Workbook workbook, String fileName) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createCarInfoList(carInfos);
        String[] keys = new String[]{"name", "mobile", "carNumber", "mileage", "registerDate", "brand", "carModel", "colors", "engineNumber", "VINcode", "vcInsuranceValidDate", "vcInsuranceCompany", "tcInsuranceValidDate", "tcInsuranceCompany"};
        String[] columnNames = new String[]{"客户名称", "客户电话", "车牌号", "里程", "注册时间", "品牌", "车型", "颜色", "发动机号码", "车架号", "保险日期", "承保公司", "交强险日期", "交强险承保公司"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "车辆信息数据导出.xls";
            outputStream = httpServletResponse.getOutputStream();
            httpServletResponse.setContentType("application/x-download");
            httpServletResponse.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(outFile, "utf-8"));
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
