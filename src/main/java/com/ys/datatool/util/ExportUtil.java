package com.ys.datatool.util;

import com.ys.datatool.domain.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by mo on @date  2018/4/20.
 */
public class ExportUtil {

    public static void exportMemberCardSomeFieldDataInLocal(List<MemberCard> memberCards, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createMemberCardList(memberCards);
        String[] keys = new String[]{"companyName", "cardCode", "memberCardName", "carNumber",
                "cardType", "cardSort", "dateCreated", "balance",
                "name", "phone", "grade","discount","remark"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.memberCardSomeFields);
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


    public static void exportMemberCardItemSomeFieldDataInLocal(List<MemberCardItem> memberCardItems, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createMemberCardItemList(memberCardItems);
        String[] keys = new String[]{"companyName", "cardCode", "itemName", "price",
                "discount", "num", "originalNum", "itemType",
                "specialType", "firstCategoryName", "secondCategoryName",
                "validTime", "isValidForever", "code", "dateCreated",
                "memberCardName", "name", "phone"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.memberCardItemSomeFields);
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


    public static void exportYuanLeCheBaoStockDataInLocal(List<Stock> stocks, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createStockList(stocks);
        String[] keys = new String[]{"companyName", "storeRoomName", "goodsName", "firstCategoryName",
                "brand", "remark", "spec", "salePrice", "inventoryNum",
                "price", "locationName", "productCode"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, ExcelDatas.YuanLeCheBaoStockDatas);
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
        String[] keys = new String[]{"companyName", "name", "carNumber", "brand",
                "carModel", "mobile", "registerDate", "engineNumber",
                "VINcode", "vcInsuranceValidDate", "vcInsuranceCompany",
                "tcInsuranceValidDate", "tcInsuranceCompany", "remark"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.carInfoDatas);
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

    public static void exportStockDataInLocal(List<Stock> stocks, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createStockList(stocks);
        String[] keys = new String[]{"companyName", "storeRoomName", "goodsName", "inventoryNum",
                "price", "locationName", "productCode"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, ExcelDatas.stockDatas);
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

    public static void exportProductDataInLocal(List<Product> products, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createProductList(products);
        String[] keys = new String[]{"companyName", "productName", "itemType", "barCode",
                "price", "firstCategoryName", "secondCategoryName", "brandName",
                "", "isShare", "", "code", "carModel",
                "unit", "origin", "", "isActive", "remark"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, ExcelDatas.itemDatas);
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

    public static void exportSupplierDataInLocal(List<Supplier> suppliers, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createSupplierList(suppliers);
        String[] keys = new String[]{"companyName", "name", "fax", "address",
                "contactName", "contactPhone", "",
                "", "", "remark", "code"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createWorkBook(list, keys, ExcelDatas.supplierDatas);
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
        String[] keys = new String[]{"companyName", "cardCode", "itemName", "price",
                "discount", "num", "originalNum", "itemType",
                "specialType", "firstCategoryName", "secondCategoryName",
                "validTime", "isValidForever", "code"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.memberCardItemDatas);
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
        String[] keys = new String[]{"companyName", "cardCode", "memberCardName", "carNumber",
                "cardType", "memberCardId", "dateCreated", "balance",
                "name", "phone", "remark"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createWorkBook(list, keys, ExcelDatas.memberCardDatas);
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
}
