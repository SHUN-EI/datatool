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
import java.util.*;

/**
 * Created by mo on @date  2018/4/27.
 */
@Service
public class TransformTool {


    private String companyName = "车店";

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
                carNumber = row.getCell(carNumberNum).toString();

            if (mileageNum != 0)
                mileage = row.getCell(mileageNum).toString();

            if (totalAmountNum != 0) {
                totalAmount = row.getCell(totalAmountNum).toString();
            }

            if (receptionistNum != 0)
                receptionistName = row.getCell(receptionistNum).toString();

            if (remarkNum != 0)
                remark = row.getCell(remarkNum).toString();

            if (dateEndNum != 0) {
                String dataCell = row.getCell(dateEndNum).toString();

                if (dataCell.contains("/"))
                    dateEndStr = DateUtil.formatDateTime2Date(dataCell);

                if (dataCell.contains("-"))
                    dateEndStr = DateUtil.formatDateTime2Date(dataCell.replace("-","/"));

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

            String billNo = "";
            String serviceItemName = "";

            billNo = row.getCell(billNoNum).toString();

            if (serviceItemNameNum != 0)
                serviceItemName = row.getCell(serviceItemNameNum).toString();

            Bill bill = billMap.get(billNo);
            if (null != bill.getServiceItemNames()) {
                String service = bill.getServiceItemNames() + "," + serviceItemName;
                bill.setServiceItemNames(service);
            }


            if (null == bill.getServiceItemNames()) {
                bill.setServiceItemNames(serviceItemName);
            }

        }

        System.out.println("bills结果为" + bills.toString());
        System.out.println(" bills大小为" + bills.size());

        String pathname = "C:\\exportExcel\\历史消费记录.xls";
        ExportUtil.exportConsumptionRecordDataInLocal(bills, ExcelDatas.workbook, pathname);
        System.out.println("--------------------------------------历史消费记录解析成功---------------------------------------------------");

    }


}
