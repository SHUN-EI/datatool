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
import java.util.Set;

/**
 * Created by mo on @date  2018/4/20.
 */
public class ExportUtil {

    /**
     * 元乐车宝-会员卡导出
     *
     * @param memberCards
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportYuanLeCheBaoMemberCardDataInLocal(List<MemberCard> memberCards, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createMemberCardList(memberCards);
        String[] keys = new String[]{"companyName", "cardCode", "memberCardName", "carNumber",
                "cardType", "cardSort", "dateCreated", "balance",
                "name", "phone"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.memberCardDatas);
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

    /**
     * 车酷客-车辆信息详情相关数据
     *
     * @param carInfos
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportCheKuKeCarInfoDataInLocal(List<CarInfo> carInfos, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createCarInfoList(carInfos);
        String[] keys = new String[]{"companyName", "name", "carNumber", "brand",
                "carModel", "mobile", "registerDate", "engineNumber",
                "VINcode", "vcInsuranceValidDate", "vcInsuranceCompany",
                "tcInsuranceValidDate", "tcInsuranceCompany", "remark",
                "brandSelect", "brandInput", "carSeriesSelect", "carSeriesInput", "carModelSelect", "carModelInput"
        };

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.CheKuKeCarInfoDatas);
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

    public static void exportBillSomeFieldDataInLocal(List<Bill> bills, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createBillList(bills);
        String[] keys = new String[]{"companyName", "billNo", "carNumber", "mileage",
                "clientPhone", "clientName", "totalAmount", "discount",
                "actualAmount", "waitInStore", "dateExpect", "payType", "remark",
                "dateAdded", "dateEnd"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.billSomeFields);
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

    public static void exportBillDetailSomeFieldDataInLocal(List<BillDetail> billDetails, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createBillDetailList(billDetails);
        String[] keys = new String[]{"companyName", "billNo", "itemName", "quantity",
                "price", "discount", "itemType", "firstCategoryName",
                "secondCategoryName", "itemCode", "salePrice", "carNumber", "dateAdded"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.billDetailSomeFields);
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

    public static void exportMemberCardSomeFieldDataInLocal(List<MemberCard> memberCards, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createMemberCardList(memberCards);
        String[] keys = new String[]{"companyName", "cardCode", "memberCardName", "carNumber",
                "cardType", "cardSort", "dateCreated", "balance",
                "name", "phone", "grade", "discount", "remark", "memberCardId", "validTime"};

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
                "memberCardName", "name", "phone", "memberCardItemId"};

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
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.YuanLeCheBaoStockDatas);
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
     * 仓库-标准模版导出
     * @param storeRooms
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportStoreRoomDataInLocal(Set<StoreRoom> storeRooms, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createStoreRoomList(storeRooms);
        String[] keys = new String[]{"companyName", "name", "remark", "locationName"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.storeRoomDatas);
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
     * 库存-标准模版导出
     *
     * @param stocks
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportStockDataInLocal(List<Stock> stocks, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createStockList(stocks);
        String[] keys = new String[]{"companyName", "storeRoomName", "goodsName", "inventoryNum",
                "price", "locationName", "productCode"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.stockDatas);
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
     * 商品或服务项-标准模版导出
     *
     * @param products
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportProductDataInLocal(List<Product> products, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createProductList(products);
        String[] keys = new String[]{"companyName", "productName", "itemType", "barCode",
                "price", "firstCategoryName", "secondCategoryName", "brandName",
                "manufactory", "isShare", "alias", "code", "carModel",
                "unit", "origin", "manufactoryType", "isActive", "remark", "cloudGoodsCode"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.itemDatas);
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
     * 供应商-标准模版导出
     *
     * @param suppliers
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportSupplierDataInLocal(List<Supplier> suppliers, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createSupplierList(suppliers);
        String[] keys = new String[]{"companyName", "name", "fax", "address",
                "contactName", "contactPhone", "accountName",
                "depositBank", "accountNumber", "remark", "code", "isShare"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.supplierDatas);
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
     * 车辆信息-标准模版导出
     *
     * @param carInfos
     * @param workbook
     * @param pathname
     * @throws IOException
     */
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

    /**
     * 卡内项目-标准模版导出
     *
     * @param memberCardItems
     * @param workbook
     * @param pathname
     * @throws IOException
     */
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

    /**
     * 会员卡-标准模版导出
     *
     * @param memberCards
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportMemberCardDataInLocal(List<MemberCard> memberCards, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createMemberCardList(memberCards);
        String[] keys = new String[]{"companyName", "cardCode", "memberCardName", "carNumber",
                "cardType", "cardSort", "dateCreated", "balance",
                "name", "phone"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.memberCardDatas);
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
