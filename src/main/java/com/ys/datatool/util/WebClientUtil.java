package com.ys.datatool.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ys.datatool.domain.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
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

    public static void exportDataExcel(HttpServletResponse response, Workbook workbook, String fileName) throws IOException {
        OutputStream outputStream = null;
        try {
            workbook = new HSSFWorkbook();
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "数据导出.xls";
            outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(outFile, "utf-8"));
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

    public static void exportCarModelItemInLocal(List<CloudCarModelEntity> carModelEntities, String pathname, Workbook workbook) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createCloudCarModelList(carModelEntities);
        String[] keys = new String[]{"num", "itemCode", "levelId", "manufacturers", "models", "year", "produced_year", "idling_year", "displacement", "induction"};
        String[] columnNames = new String[]{"序号", "WIX产品型号", "levelId", "厂家", "车型", "年款", "生产年份", "停产年份", "排量(升)", "进气形式"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            File file = new File(pathname);

            outputStream = new FileOutputStream(file);
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

    public static void exportSupplierDataInLocal(List<Supplier> suppliers, String pathname, Workbook workbook) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createSupplierList(suppliers);
        String[] keys = new String[]{"name", "phone", "fax", "address", "contactName", "contactPhone", "remark"};
        String[] columnNames = new String[]{"供应商名称", "供应商联系号码", "传真", "地址", "其他联系人名称", "其他联系人号码", "备注"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            File file = new File(pathname);

            outputStream = new FileOutputStream(file);
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

    /**
     * 元乐车宝入库详情导出excel
     *
     * @param products
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportStockInDetailDataInLocal(List<Product> products, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createProductList(products);
        String[] keys = new String[]{"productName", "code", "barCode", "productCode", "itemType", "unit", "brandName", "price", "quantity"};
        String[] columnNames = new String[]{"配件类型", "品牌", "型号", "规格", "渠道属性", "产地信息", "入库数量", "成本价", "成本小计"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            File file = new File(pathname);

            outputStream = new FileOutputStream(file);
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

    public static void exportItemDataInLocal(List<Product> products, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createProductList(products);
        String[] keys = new String[]{"productName", "price", "itemType", "firstCategoryName", "secondCategoryName", "productCode", "barCode", "brandName", "code", "unit", "unitCost", "quantity", "storeRoomName", "origin", "carModel"};
        String[] columnNames = new String[]{"商品名称", "售价", "商品类别", "一级分类名称", "二级分类名称", "商品编码", "条形码", "品牌", "自编码", "单位", "入库单价", "库存数量", "仓库", "产地", "适用车型"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            File file = new File(pathname);

            outputStream = new FileOutputStream(file);
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


    public static void exportCarInfoDataInLocal(List<CarInfo> carInfos, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createCarInfoList(carInfos);
        String[] keys = new String[]{"name", "mobile", "carNumber", "mileage", "registerDate", "brand", "carModel", "colors", "engineNumber", "VINcode", "vcInsuranceValidDate", "vcInsuranceCompany", "tcInsuranceValidDate", "tcInsuranceCompany"};
        String[] columnNames = new String[]{"客户名称", "客户电话", "车牌号", "里程", "注册时间", "品牌", "车型", "颜色", "发动机号码", "车架号", "保险日期", "承保公司", "交强险日期", "交强险承保公司"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            File file = new File(pathname);

            outputStream = new FileOutputStream(file);
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

    public static void exportMemberCardDataInLocal(List<MemberCard> memberCards, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createMemberCardList(memberCards);
        String[] keys = new String[]{"cardCode", "memberCardId", "memberCardName", "carNumber", "cardType", "dateCreated", "validTime", "balance", "firstCharge", "firstGift", "num", "name", "phone"};
        String[] columnNames = new String[]{"会员卡卡号", "会员卡id", "会员卡名称", "车牌号", "卡类型", "开卡日期", "到期时间", "卡内余额", "充值金额", "赠送金额", "卡剩余次数", "联系人姓名", "联系人手机"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            File file = new File(pathname);

            outputStream = new FileOutputStream(file);
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

    public static void exportMemberCardItemDataInLocal(List<MemberCardItem> memberCardItems, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createMemberCardItemList(memberCardItems);
        String[] keys = new String[]{"memberCardItemId", "cardCode", "memberCardName", "carNumber", "name", "phone", "itemName", "price", "num", "usedNum", "originalNum", "firstCategoryName", "secondCategoryName", "balance", "validTime", "dateCreated"};
        String[] columnNames = new String[]{"会员卡id", "会员卡卡号", "会员卡名称", "车牌号", "持卡人姓名", "持卡人手机", "商品名称", "售价", "剩余数量", "使用数量", "初始数量", "一级分类名称", "二级分类名称", "卡内余额", "到期时间", "开卡时间"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            File file = new File(pathname);
            outputStream = new FileOutputStream(file);
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

    public static void exportStockData(HttpServletResponse httpServletResponse, List<Stock> stocks, Workbook workbook, String fileName) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createStockList(stocks);
        String[] keys = new String[]{"storeRoomName", "goodsName", "inventoryNum", "price", "remark", "locationName", "productCode"};
        String[] columnNames = new String[]{"仓库名称", "商品名称", "库存数量", "入库单价", "备注", "仓位", "商品编码"};
        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "库存数据导出.xls";
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

    public static void exportItemData(HttpServletResponse httpServletResponse, List<Product> products, Workbook workbook, String fileName) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createProductList(products);
        String[] keys = new String[]{"productName", "price", "itemType", "firstCategoryName", "secondCategoryName", "productCode", "barCode", "brandName", "code", "unit", "unitCost", "quantity", "storeRoomName", "origin", "carModel"};
        String[] columnNames = new String[]{"商品名称", "售价", "商品类别", "一级分类名称", "二级分类名称", "商品编码", "条形码", "品牌", "自编码", "单位", "入库单价", "库存数量", "仓库", "产地", "适用车型"};
        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "库存商品数据导出.xls";
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

    public static void exportSupplierData(HttpServletResponse httpServletResponse, List<Supplier> suppliers, Workbook workbook, String fileName) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createSupplierList(suppliers);
        String[] keys = new String[]{"name", "phone", "fax", "address", "contactName", "contactPhone", "remark"};
        String[] columnNames = new String[]{"供应商名称", "供应商联系号码", "传真", "地址", "其他联系人名称", "其他联系人号码", "备注"};
        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "库存供应商数据导出.xls";
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

    public static void exportBillDetailData(HttpServletResponse httpServletResponse, List<BillDetail> billDetails, Workbook workbook, String fileName) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createBillDetailList(billDetails);
        String[] keys = new String[]{"no", "name", "itemCode", "itemType", "quantity", "price", "totalAmount", "workingHour", "salePrice", "payment", "carLicense", "clientName", "firstCategoryName", "secondCategoryName", "mileage", "dateAdded", "dateExpect", "dateEnd"};
        String[] columnNames = new String[]{"单据号", "商品名称", "商品编码", "商品类别", "数量", "单价", "金额", "工时", "工时费", "支付方式", "车牌", "客户名称", "一级分类名称", "二级分类名称", "里程", "开单日期", "预计完工日期", "完工日期"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "单据明细数据导出.xls";
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

    public static void exportBillData(HttpServletResponse httpServletResponse, List<Bill> bills, Workbook workbook, String fileName) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createBillList(bills);
        String[] keys = new String[]{"no", "car_license", "cardCode", "mileage", "automodel", "clientName", "clientPhone", "company", "totalAmount", "discount", "actualAmount", "waitInStore", "payType", "dateAdded", "dateExpect", "dateEnd", "remark"};
        String[] columnNames = new String[]{"单据号", "车牌号", "会员卡卡号", "里程", "车型", "联系人", "联系人电话", "联系人单位", "总计", "单据折扣", "实收", "是否在店等", "支付类型", "开单日期", "预计完工", "完工日期", "备注"};
        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "单据数据导出.xls";
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

    public static void exportMemberCardItemData(HttpServletResponse httpServletResponse, List<MemberCardItem> memberCardItems, Workbook workbook, String fileName) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createMemberCardItemList(memberCardItems);
        String[] keys = new String[]{"memberCardItemId", "cardCode", "memberCardName", "carNumber", "name", "phone", "itemName", "price", "num", "usedNum", "originalNum", "firstCategoryName", "secondCategoryName", "balance", "validTime", "dateCreated"};
        String[] columnNames = new String[]{"会员卡id", "会员卡卡号", "会员卡名称", "车牌号", "持卡人姓名", "持卡人手机", "商品名称", "售价", "剩余数量", "使用数量", "初始数量", "一级分类名称", "二级分类名称", "卡内余额", "到期时间", "开卡时间"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "卡内项目数据导出.xls";
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

    public static void exportMemberCardData(HttpServletResponse httpServletResponse, List<MemberCard> memberCards, Workbook workbook, String fileName) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createMemberCardList(memberCards);
        String[] keys = new String[]{"cardCode", "memberCardId", "memberCardName", "carNumber", "cardType", "dateCreated", "validTime", "balance", "firstCharge", "firstGift", "num", "name", "phone"};
        String[] columnNames = new String[]{"会员卡卡号", "会员卡id", "会员卡名称", "车牌号", "卡类型", "开卡日期", "到期时间", "卡内余额", "充值金额", "赠送金额", "卡剩余次数", "联系人姓名", "联系人手机"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, columnNames);
            CellStyle cellStyle = workbook.createCellStyle(); //换行样式
            cellStyle.setWrapText(true);

            String outFile = fileName + "会员卡数据导出.xls";
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

    public static HtmlPage login(WebClient webClient, String url, String username, String password, String userId, String pwdId, String btnXPath) throws IOException {

        HtmlPage page = webClient.getPage(url);
        HtmlInput user = page.getHtmlElementById(userId);
        HtmlInput pwd = page.getHtmlElementById(pwdId);
        //HtmlButton btn = page.getFirstByXPath(btnXPath);
        HtmlInput input = page.getFirstByXPath(btnXPath);
        user.setAttribute("value", username);
        pwd.setAttribute("value", password);

        return input.click();
    }


    public static HtmlPage loginWithVerificationCode(WebClient webClient, String url, String username, String password, String valCode, String userId, String pwdId, String valCodeId, String btnXPath) throws IOException {

        HtmlPage page = webClient.getPage(url);
        HtmlInput user = page.getHtmlElementById(userId);
        HtmlInput pwd = page.getHtmlElementById(pwdId);
        HtmlInput code = page.getHtmlElementById(valCodeId);
        //HtmlButton btn = page.getFirstByXPath(btnXPath);
        HtmlInput input = page.getFirstByXPath(btnXPath);
        user.setAttribute("value", username);
        pwd.setAttribute("value", password);
        code.setAttribute("value", valCode);

        return input.click();
    }

}
