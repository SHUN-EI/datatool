package com.ys.datatool.service.tool;

import com.ys.datatool.domain.Bill;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.util.DateUtil;
import com.ys.datatool.util.ExportUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mo on @date  2018/4/27.
 */
@Service
public class TransformTool {


    private String companyName = "车店";

    /**
     * 根据单据和单据明细表合并成历史消费记录表
     *
     * @throws IOException
     */
    @Test
    public void formatBillExcelData() throws IOException {
        List<Bill> bills = new ArrayList<>();
        Map<String, Bill> billMap = new HashMap<>();

        File billFile = new File("C:\\exportExcel\\单据.xls");
        File billDetailFile = new File("C:\\exportExcel\\单据明细.xls");
        FileInputStream billStream = new FileInputStream(billFile);
        HSSFWorkbook billWorkbook = new HSSFWorkbook(billStream);
        HSSFSheet billSheet = billWorkbook.getSheetAt(0);

        FileInputStream billDetailStream = new FileInputStream(billDetailFile);
        HSSFWorkbook billDetailWorkbook = new HSSFWorkbook(billDetailStream);
        HSSFSheet billDetailSheet = billDetailWorkbook.getSheetAt(0);

        int billNoNum = 0;
        int dateEndNum = 0;
        int carNumberNum = 0;
        int mileageNum = 0;
        int serviceItemNameNum = 0;
        int goodsNameNum = 0;
        int totalAmountNum = 0;
        int receptionistNum = 0;
        int remarkNum = 0;


        //获取指定表头所在位置
        for (Cell cell : billSheet.getRow(0)) {
            //cell.getCellType()==Cell.CELL_TYPE_STRING
            if (cell.getCellTypeEnum() == CellType.STRING) {
                String content = cell.getRichStringCellValue().getString().trim();

                if (ExcelDatas.billNoName.equals(content))
                    billNoNum = cell.getColumnIndex();

                if (ExcelDatas.dateEndName.equals(content))
                    dateEndNum = cell.getColumnIndex();

                if (ExcelDatas.carNumberName.equals(content))
                    carNumberNum = cell.getColumnIndex();

                if (ExcelDatas.mileageName.equals(content))
                    mileageNum = cell.getColumnIndex();

                if (ExcelDatas.serviceItemName.equals(content))
                    serviceItemNameNum = cell.getColumnIndex();

                if (ExcelDatas.goodsName.equals(content))
                    goodsNameNum = cell.getColumnIndex();

                if (ExcelDatas.totalAmountName.equals(content))
                    totalAmountNum = cell.getColumnIndex();

                if (ExcelDatas.receptionistName.equals(content))
                    receptionistNum = cell.getColumnIndex();

                if (ExcelDatas.remarkName.equals(content))
                    remarkNum = cell.getColumnIndex();

            }
        }


        for (int i = 1; i <= billSheet.getLastRowNum(); i++) {
            HSSFRow row = billSheet.getRow(i);

            String billNo = "";
            String carNumber = "";
            String mileage = "";
            String totalAmount = "";
            String receptionistName = "";
            String remark = "";
            String dateEndStr = "";

            billNo = row.getCell(billNoNum).toString();

            if (carNumberNum != 0)
                carNumber = getCell(row, carNumberNum, carNumber);

            if (mileageNum != 0)
                mileage = getCell(row, mileageNum, mileage);

            if (totalAmountNum != 0)
                totalAmount = getCell(row, totalAmountNum, totalAmount);

            if (receptionistNum != 0)
                receptionistName = getCell(row, receptionistNum, receptionistName);

            if (remarkNum != 0)
                remark = getCell(row, remarkNum, remark);


            if (dateEndNum != 0) {
                dateEndStr = getCell(row, dateEndNum, dateEndStr);

                if (dateEndStr.contains("/"))
                    dateEndStr = DateUtil.formatDateTime2Date(dateEndStr);

                if (dateEndStr.contains("-"))
                    dateEndStr = DateUtil.formatDateTime2Date(dateEndStr.replace("-", "/"));

            }

            Bill bill = new Bill();
            bill.setCompanyName(companyName);
            bill.setBillNo(billNo);
            bill.setDateEnd(dateEndStr);
            bill.setCarNumber(carNumber);
            bill.setMileage(mileage);
            bill.setTotalAmount(totalAmount);
            bill.setReceptionistName(receptionistName);
            bill.setRemark(remark);
            bills.add(bill);
            billMap.put(billNo, bill);
        }

        for (Cell cell : billDetailSheet.getRow(0)) {
            if (cell.getCellTypeEnum() == CellType.STRING) {
                String content = cell.getRichStringCellValue().getString().trim();

                if (ExcelDatas.billNoName.equals(content))
                    billNoNum = cell.getColumnIndex();

                if (ExcelDatas.serviceItemName.equals(content))
                    serviceItemNameNum = cell.getColumnIndex();

                if (ExcelDatas.goodsName.equals(content))
                    goodsNameNum = cell.getColumnIndex();
            }
        }

        for (int i = 1; i <= billDetailSheet.getLastRowNum(); i++) {
            HSSFRow row = billDetailSheet.getRow(i);

            System.out.println("----------------------------------第" + i + "行----------------------------------------------");

            String billNo = "";
            String serviceItemName = "";
            String goodsName = "";

            billNo = row.getCell(billNoNum).toString();
            Bill bill = billMap.get(billNo);

            if (bill != null) {
                if (serviceItemNameNum != 0) {
                    serviceItemName = getCell(row, serviceItemNameNum, serviceItemName);
                }

                if (null != bill.getServiceItemNames() && !"".equals(serviceItemName)) {
                    String service = bill.getServiceItemNames() + "," + serviceItemName;
                    bill.setServiceItemNames(service);
                }

                if (null == bill.getServiceItemNames()) {
                    bill.setServiceItemNames(serviceItemName);
                }

                if (goodsNameNum != 0) {
                    goodsName = getCell(row, goodsNameNum, goodsName);
                }

                if (null != bill.getGoodsNames() && !"".equals(goodsName)) {
                    String goods = bill.getGoodsNames() + "," + goodsName;
                    bill.setGoodsNames(goods);

                }

                if (null == bill.getGoodsNames()) {
                    bill.setGoodsNames(goodsName);
                }
            }
        }

        System.out.println("bills结果为" + bills.toString());
        System.out.println(" bills大小为" + bills.size());

        String pathname = "C:\\exportExcel\\历史消费记录.xls";
        ExportUtil.exportConsumptionRecordDataInLocal(bills, ExcelDatas.workbook, pathname);
        System.out.println("--------------------------------------历史消费记录解析成功---------------------------------------------------");

    }


    /**
     * 根据单据明细表计算单据总金额得到单据表
     *
     * @throws IOException
     */
    @Test
    public void countBillTotalAmountExcelData() throws IOException {
        List<Bill> bills = new ArrayList<>();
        Map<String, Bill> billMap = new HashMap<>();

        File billDetailFile = new File("C:\\exportExcel\\单据明细.xls");

        FileInputStream billDetailStream = new FileInputStream(billDetailFile);
        HSSFWorkbook billDetailWorkbook = new HSSFWorkbook(billDetailStream);
        HSSFSheet billDetailSheet = billDetailWorkbook.getSheetAt(0);

        int billNoNum = 0;
        int totalAmountNum = 0;
        for (Cell cell : billDetailSheet.getRow(0)) {
            if (cell.getCellTypeEnum() == CellType.STRING) {
                String content = cell.getRichStringCellValue().getString().trim();

                if (ExcelDatas.billNoName.equals(content))
                    billNoNum = cell.getColumnIndex();

                if (ExcelDatas.totalAmountName.equals(content))
                    totalAmountNum = cell.getColumnIndex();
            }
        }

        for (int i = 1; i <= billDetailSheet.getLastRowNum(); i++) {
            HSSFRow row = billDetailSheet.getRow(i);

            System.out.println("----------------------------------第" + i + "行----------------------------------------------");

            String billNo = "";
            String totalAmount = "";

            billNo = row.getCell(billNoNum).toString();

            if (totalAmountNum != 0)
                totalAmount = getCell(row, totalAmountNum, totalAmount);

            //计算单据总价
            BigDecimal sum = new BigDecimal(totalAmount);
            if (billMap.size() > 0 && billMap.get(billNo) != null) {
                Bill b = billMap.get(billNo);
                String totalPriceStr = b.getTotalAmount();
                BigDecimal totalPrice = new BigDecimal(totalPriceStr);
                sum = sum.add(totalPrice);
                sum.setScale(2, BigDecimal.ROUND_HALF_UP);
                totalAmount = sum.toString();
            }

            Bill bill = new Bill();
            bill.setBillNo(billNo);
            bill.setCompanyName(companyName);
            bill.setTotalAmount(totalAmount);
            billMap.put(billNo, bill);
        }

        if (billMap.size() > 0) {
            billMap.entrySet().forEach(b -> {
                bills.add(b.getValue());
            });
        }

        String pathname = "C:\\exportExcel\\单据.xls";
        ExportUtil.exportConsumptionRecordDataInLocal(bills, ExcelDatas.workbook, pathname);
        System.out.println("--------------------------------------单据总金额计算完成---------------------------------------------------");

    }

    private String getCell(HSSFRow row, int num, String result) {

        Cell cell = row.getCell(num);
        if (cell != null) {
            result = row.getCell(num).toString();
        } else
            result = "";

        return result;
    }


}
