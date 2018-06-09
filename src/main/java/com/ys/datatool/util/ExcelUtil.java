package com.ys.datatool.util;

import com.ys.datatool.domain.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

/**
 * Created by mo on  2017/5/30.
 */

public class ExcelUtil {

    public static Cell getCell(int col, Row row) {
        return row.getCell(col) != null ? row.getCell(col) : row.createCell(col, Cell.CELL_TYPE_STRING);
    }

    public static XSSFCell getCell(int col, XSSFRow row) {
        return row.getCell(col) != null ? row.getCell(col) : row.createCell(col, Cell.CELL_TYPE_STRING);
    }

    public static Row getRow(int row, Sheet sheet) {
        return sheet.getRow(row) != null ? sheet.getRow(row) : sheet.createRow(row);
    }

    public static XSSFRow getRow(int row, XSSFSheet sheet) {
        return sheet.getRow(row) != null ? sheet.getRow(row) : sheet.createRow(row);
    }

    public static Sheet createSheetAndHead(Workbook workbook, String sheetName, String[] datas) {
        Sheet sheet = workbook.createSheet(sheetName);
        Row rowHead = sheet.createRow(0);
        for (int i = 0; i < datas.length; i++) {
            rowHead.createCell(i).setCellValue(datas[i]);
        }
        return sheet;
    }

    public static XSSFSheet createSheetAndHead(XSSFWorkbook workbook, String sheetName, String[] datas) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow rowHead = sheet.createRow(0);
        for (int i = 0; i < datas.length; i++) {
            rowHead.createCell(i).setCellValue(datas[i]);
        }
        return sheet;
    }

    public static List<Map<String, Object>> createCarModelList(List<CarModelEntity> carModelEntities) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "车型商品关系表");
        listMap.add(map);

        CarModelEntity carModelEntity = null;
        for (int i = 0; i < carModelEntities.size(); i++) {
            carModelEntity = carModelEntities.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("levelId", carModelEntity.getLevelId());
            mapValue.put("manufacturers", carModelEntity.getManufacturers());
            mapValue.put("models", carModelEntity.getModels());
            mapValue.put("year", carModelEntity.getYear());
            mapValue.put("produced_year", carModelEntity.getProducedYear());
            mapValue.put("idling_year", carModelEntity.getIdlingYear());
            mapValue.put("displacement", carModelEntity.getDisplacement());
            mapValue.put("induction", carModelEntity.getInduction());
            mapValue.put("num", carModelEntity.getNum());
            mapValue.put("itemCode", carModelEntity.getItemCode());
            listMap.add(mapValue);
        }
        return listMap;
    }

    public static List<Map<String, Object>> createStockList(List<Stock> stocks) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "库存");
        listMap.add(map);

        Stock stock = null;
        for (int i = 0; i < stocks.size(); i++) {
            stock = stocks.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("storeRoomName", stock.getStoreRoomName());
            mapValue.put("locationName", stock.getLocationName());
            mapValue.put("goodsName", stock.getGoodsName());
            mapValue.put("inventoryNum", stock.getInventoryNum());
            mapValue.put("price", stock.getPrice());
            mapValue.put("productCode", stock.getProductCode());
            mapValue.put("companyName", stock.getCompanyName());
            mapValue.put("firstCategoryName", stock.getFirstCategoryName());
            mapValue.put("brand", stock.getBrand());
            mapValue.put("remark", stock.getRemark());
            mapValue.put("spec", stock.getSpec());
            mapValue.put("salePrice", stock.getSalePrice());
            listMap.add(mapValue);
        }
        return listMap;
    }

    public static List<Map<String, Object>> createBillList(List<Bill> bills) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "单据");
        listMap.add(map);

        Bill bill = null;
        for (int i = 0; i < bills.size(); i++) {
            bill = bills.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("no", bill.getBillNo());
            mapValue.put("car_license", bill.getCarLicense());
            mapValue.put("cardCode", bill.getCardCode());
            mapValue.put("automodel", bill.getAutomodel());
            mapValue.put("clientName", bill.getClientName());
            mapValue.put("clientPhone", bill.getClientPhone());
            mapValue.put("company", bill.getCompany());
            mapValue.put("totalAmount", bill.getTotalAmount());
            mapValue.put("actualAmount", bill.getActualAmount());
            mapValue.put("discount", bill.getDiscount());
            mapValue.put("dateAdded", bill.getDateAdded());
            mapValue.put("dateExpect", bill.getDateExpect());
            mapValue.put("dateEnd", bill.getDateEnd());
            mapValue.put("remark", bill.getRemark());
            mapValue.put("mileage", bill.getMileage());
            mapValue.put("waitInStore", bill.getWaitInStore());
            mapValue.put("payType", bill.getPayType());
            listMap.add(mapValue);
        }
        return listMap;
    }

    public static List<Map<String, Object>> createBillDetailList(List<BillDetail> billDetails) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "单据明细");
        listMap.add(map);

        BillDetail billDetail = null;
        for (int i = 0; i < billDetails.size(); i++) {
            billDetail = billDetails.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("name", billDetail.getItemName());
            mapValue.put("salePrice", billDetail.getSalePrice());
            mapValue.put("workingHour", billDetail.getWorkingHour());
            mapValue.put("quantity", billDetail.getQuantity());
            mapValue.put("itemType", billDetail.getItemType());
            mapValue.put("itemCode", billDetail.getItemCode());
            mapValue.put("no", billDetail.getNo());
            mapValue.put("discountRate", billDetail.getDiscountRate());
            mapValue.put("deduction", billDetail.getDeduction());
            mapValue.put("totalAmount", billDetail.getTotalAmount());
            mapValue.put("price", billDetail.getPrice());
            mapValue.put("mileage", billDetail.getMileage());
            mapValue.put("payment", billDetail.getPayment());
            mapValue.put("dateAdded", billDetail.getDateAdded());
            mapValue.put("dateExpect", billDetail.getDateExpect());
            mapValue.put("dateEnd", billDetail.getDateEnd());
            mapValue.put("carLicense", billDetail.getCarLicense());
            mapValue.put("clientName", billDetail.getClientName());
            mapValue.put("firstCategoryName", billDetail.getFirstCategoryName());
            mapValue.put("secondCategoryName", billDetail.getSecondCategoryName());
            listMap.add(mapValue);
        }
        return listMap;
    }

    public static List<Map<String, Object>> createCarInfoList(List<CarInfo> carInfos) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "车辆信息");
        listMap.add(map);

        CarInfo carInfo = null;
        for (int i = 0; i < carInfos.size(); i++) {
            carInfo = carInfos.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("name", carInfo.getName());
            mapValue.put("mobile", carInfo.getPhone());
            mapValue.put("carNumber", carInfo.getCarNumber());
            mapValue.put("brand", carInfo.getBrand());
            mapValue.put("carModel", carInfo.getCarModel());
            mapValue.put("mileage", carInfo.getMileage());
            mapValue.put("engineNumber", carInfo.getEngineNumber());
            mapValue.put("registerDate", carInfo.getRegisterDate());
            mapValue.put("colors", carInfo.getColors());
            mapValue.put("VINcode", carInfo.getVINcode());
            mapValue.put("vcInsuranceValidDate", carInfo.getVcInsuranceValidDate());
            mapValue.put("vcInsuranceCompany", carInfo.getVcInsuranceCompany());
            mapValue.put("tcInsuranceValidDate", carInfo.getTcInsuranceValidDate());
            mapValue.put("tcInsuranceCompany", carInfo.getTcInsuranceCompany());
            mapValue.put("remark", carInfo.getRemark());
            mapValue.put("companyName", carInfo.getCompanyName());
            listMap.add(mapValue);

        }
        return listMap;
    }

    public static List<Map<String, Object>> createSupplierList(List<Supplier> suppliers) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "库存供应商");
        listMap.add(map);

        Supplier supplier = null;
        for (int i = 0; i < suppliers.size(); i++) {
            supplier = suppliers.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("name", supplier.getName());
            mapValue.put("phone", supplier.getPhone());
            mapValue.put("fax", supplier.getFax());
            mapValue.put("address", supplier.getAddress());
            mapValue.put("contactName", supplier.getContactName());
            mapValue.put("contactPhone", supplier.getContactPhone());
            mapValue.put("remark", supplier.getRemark());
            mapValue.put("code", supplier.getCode());
            mapValue.put("companyName", supplier.getCompanyName());
            listMap.add(mapValue);
        }
        return listMap;
    }

    public static List<Map<String, Object>> createMemberCardList(List<MemberCard> memberCards) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "会员卡");
        listMap.add(map);

        MemberCard memberCard = null;
        for (int i = 0; i < memberCards.size(); i++) {
            memberCard = memberCards.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("cardCode", memberCard.getCardCode());
            mapValue.put("memberCardName", memberCard.getMemberCardName());
            mapValue.put("carNumber", memberCard.getCarNumber());
            mapValue.put("dateCreated", memberCard.getDateCreated());
            mapValue.put("balance", memberCard.getBalance());
            mapValue.put("firstCharge", memberCard.getFirstCharge());
            mapValue.put("firstGift", memberCard.getFirstGift());
            mapValue.put("num", memberCard.getNum());
            mapValue.put("name", memberCard.getName());
            mapValue.put("phone", memberCard.getPhone());
            mapValue.put("cardType", memberCard.getCardType());
            mapValue.put("validTime", memberCard.getValidTime());
            mapValue.put("memberCardId", memberCard.getMemberCardId());
            mapValue.put("remark", memberCard.getRemark());
            mapValue.put("companyName", memberCard.getCompanyName());
            mapValue.put("state", memberCard.getState());
            mapValue.put("memberCardId", memberCard.getMemberCardId());
            mapValue.put("grade", memberCard.getGrade());
            mapValue.put("discount", memberCard.getDiscount());
            mapValue.put("cardSort", memberCard.getCardSort());
            listMap.add(mapValue);
        }
        return listMap;
    }

    public static List<Map<String, Object>> createMemberCardItemList(List<MemberCardItem> memberCardItems) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "卡内项目");
        listMap.add(map);

        MemberCardItem memberCardItem = null;
        for (int i = 0; i < memberCardItems.size(); i++) {
            memberCardItem = memberCardItems.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("cardCode", memberCardItem.getCardCode());
            mapValue.put("itemName", memberCardItem.getItemName());
            mapValue.put("balance", memberCardItem.getBalance());
            mapValue.put("price", memberCardItem.getPrice());
            mapValue.put("num", memberCardItem.getNum());
            mapValue.put("originalNum", memberCardItem.getOriginalNum());
            mapValue.put("firstCategoryName", memberCardItem.getFirstCategoryName());
            mapValue.put("secondCategoryName", memberCardItem.getSecondCategoryName());
            mapValue.put("name", memberCardItem.getName());
            mapValue.put("carNumber", memberCardItem.getCarNumber());
            mapValue.put("discount", memberCardItem.getDiscount());
            mapValue.put("validTime", memberCardItem.getValidTime());
            mapValue.put("usedNum", memberCardItem.getUsedNum());
            mapValue.put("phone", memberCardItem.getPhone());
            mapValue.put("dateCreated", memberCardItem.getDateCreated());
            mapValue.put("memberCardName", memberCardItem.getMemberCardName());
            mapValue.put("memberCardItemId", memberCardItem.getMemberCardItemId());
            mapValue.put("remark", memberCardItem.getRemark());
            mapValue.put("code", memberCardItem.getCode());
            mapValue.put("itemType", memberCardItem.getItemType());
            mapValue.put("specialType", memberCardItem.getSpecialType());
            mapValue.put("isValidForever", memberCardItem.getIsValidForever());
            mapValue.put("companyName", memberCardItem.getCompanyName());
            listMap.add(mapValue);
        }
        return listMap;
    }

    public static List<Map<String, Object>> createProductList(List<Product> products) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "库存商品");
        listMap.add(map);

        Product product = null;
        for (int i = 0; i < products.size(); i++) {
            product = products.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("productName", product.getProductName());
            mapValue.put("price", product.getPrice());
            mapValue.put("firstCategoryName", product.getFirstCategoryName());
            mapValue.put("secondCategoryName", product.getSecondCategoryName());
            mapValue.put("productCode", product.getProductCode());
            mapValue.put("brandName", product.getBrandName());
            mapValue.put("unit", product.getUnit());
            mapValue.put("quantity", product.getQuantity());
            mapValue.put("code", product.getCode());
            mapValue.put("unitCost", product.getUnitCost());
            mapValue.put("itemType", product.getItemType());
            mapValue.put("barCode", product.getBarCode());
            mapValue.put("storeRoomName", product.getStoreRoomName());
            mapValue.put("origin", product.getOrigin());
            mapValue.put("carModel", product.getCarModel());
            mapValue.put("companyName", product.getCompanyName());
            mapValue.put("isShare", product.getIsShare());
            mapValue.put("isActive", product.getIsActive());
            mapValue.put("remark", product.getRemark());

            listMap.add(mapValue);
        }
        return listMap;
    }

    /**
     * excel-xlsx格式
     *
     * @return
     */
    public static XSSFWorkbook createXSSFWorkbook(List<Map<String, Object>> list, String[] keys, String[] columnNames) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(list.get(0).get("sheetName").toString());
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth(i, 15 * 256);
        }

        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
        }

        for (int i = 1; i < list.size(); i++) {
            XSSFRow r = sheet.createRow(i);
            for (int j = 0; j < keys.length; j++) {
                XSSFCell cell = r.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
            }
        }

        return workbook;
    }

    /**
     * @param keys        list中map的key数组集合
     * @param columnNames excel的列名
     */
    public static Workbook createWorkBook(List<Map<String, Object>> list, String[] keys, String[] columnNames) {

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        //f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

//        Font f3=wb.createFont();
//        f3.setFontHeightInPoints((short) 10);
//        f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
       /* cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);*/

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        /*cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);*/

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            // 在row行上创建一个方格
            for (short j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }
}

