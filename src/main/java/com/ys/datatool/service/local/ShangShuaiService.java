package com.ys.datatool.service.local;

import com.ys.datatool.domain.*;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.FBDBConnUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on @date  2018/4/15.
 * 商帅系统
 */

@Service
public class ShangShuaiService {

    private Workbook workbook = new HSSFWorkbook();

    private String fileName = "商帅软件";

       /* FBWrappingDataSource dataSource = new FBWrappingDataSource();
        dataSource.setDatabase(URL);
        dataSource.setUserName(USER);
        dataSource.setPassword(PASSWORD);
        JDBCTEMPLATE = new JdbcTemplate(dataSource);*/

    @Test
    public void fetchServiceData() throws Exception{
        List<Product> products = new ArrayList<>();

        String serviceQuery = "select s.SERVICECODE,s.SERVICENAME,s.STANDPRICE,s.SERVICESORT from TB_SERVICE s ";

        Connection connection = FBDBConnUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(serviceQuery);

    }

    @Test
    public void fetchItemData() throws Exception {
        List<Product> products = new ArrayList<>();

        String itemQuery = "select p.PRODUCTNUMBER,p.PRODUCTNAME,p.BARCODE,p.SELLPRICE,P.PRODUCTSORT, " +
                " P.PRODUCTMODEL,p.PRODUCTUNIT,p.PACTUNIT from TB_PRODUCT p ";

        Connection connection = FBDBConnUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(itemQuery);
    }

    @Test
    public void fetchSupplierData() throws Exception {
        List<Supplier> suppliers = new ArrayList<>();

        String supplierQuery = "select p.CLIENTNAME,p.ADDRESS,p.CLIENTLINKER,p.MOBILEPHONE,p.CLIENTNUMBER from VI_PROVIDER p ";

        Connection connection = FBDBConnUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(supplierQuery);
    }

    @Test
    public void fetchStockData() throws Exception {
        List<Stock> stocks = new ArrayList<>();

        String stockQuery = "select s.DEPOTNAME,p.PRODUCTNUMBER,p.PRODUCTNAME, " +
                " p.STOCKPRICE,s.AMOUNT,p.PRODUCTUNIT, " +
                " p.PACTUNIT,p.PRODUCTMODEL, " +
                " p.BARCODE,p.SELLPRICE, " +
                " p.PRODUCTSORT from  TB_STORAGE s " +
                " inner join TB_PRODUCT p " +
                "on p.PRODUCTID=S.PRODUCTID";

        Connection connection = FBDBConnUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(stockQuery);
    }

    @Test
    public void fetchCarInfoData() throws Exception {
        List<CarInfo> carInfos = new ArrayList<>();
        String carInfoQuery = "select c.CLIENTNAME,v.VEHICLENUMBER,v.VEHICLETRADEMARK," +
                "v.VEHICLESTYLE,c.MOBILEPHONE,v.ENGINNUMBER, " +
                "v.VINNUMBER from TB_VEHICLE v" +
                " inner join TB_CLIENT c" +
                " on c.CLIENTID=v.CLIENTID;";

        Connection connection = FBDBConnUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(carInfoQuery);
    }

    @Test
    public void fetchMemberCardItemData() throws Exception {
        List<MemberCardItem> memberCardItems = new ArrayList<>();
        String memberCardItemQuery = "select c.CARDNUMBER,c.CARDNAME," +
                "item.SERVICENAME,item.SERVICECODE,item.REMAINCONSUMECOUNT,item.ENDDATE," +
                "item.ISERASE,s.STANDPRICE,s.SERVICESORT" +
                " from TB_RACARDITEM item " +
                "inner join  TB_RACARD c " +
                "on c.CARDID=item.CARDID " +
                "inner join TB_SERVICE s" +
                " on s.SERVICECODE=item.SERVICECODE;";

        Connection connection = FBDBConnUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(memberCardItemQuery);
        while (rs.next()) {
            MemberCardItem memberCardItem = new MemberCardItem();

            String cardCode = rs.getString("CARDNUMBER");
            memberCardItem.setCardCode(cardCode);

            String itemName = new String(rs.getBytes("SERVICENAME"), "gb2312");
            memberCardItem.setItemName(itemName);

            String price = rs.getString("STANDPRICE");
            memberCardItem.setPrice(price);

            String num = rs.getString("REMAINCONSUMECOUNT");
            memberCardItem.setNum(num);

            String firstCategoryName = new String(rs.getBytes("SERVICESORT"), "gb2312");
            memberCardItem.setFirstCategoryName(firstCategoryName);

            String code = rs.getString("SERVICECODE");
            memberCardItem.setCode(code);

            String validTime = rs.getString("ENDDATE");
            memberCardItem.setValidTime(validTime);

            String remark = new String(rs.getBytes("ISERASE"), "gb2312");
            if ("是".equals(remark))
                continue;
            memberCardItem.setRemark(remark);

            memberCardItems.add(memberCardItem);
        }

        System.out.print("结果为" + memberCardItems.toString());
        System.out.print("大小为" + memberCardItems.size());

        String pathname = "D:\\商帅卡内项目导出.xls";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, workbook, pathname);

    }

    @Test
    public void fetchMemberCardData() throws Exception {

        List<MemberCard> memberCards = new ArrayList<>();
        String memberCardQuery = " select CARDNUMBER,CARDNAME,VEHICLENUMBER ," +
                "CARDTYPE,CARDDATE,REMAINTOTALSUM,ISSTOP, " +
                "CLIENTLINKER,MOBILEPHONE " +
                " from TB_RACARD card " +
                "inner join TB_CLIENT client " +
                " on client.CLIENTID=card.CLIENTID;";

        Connection connection = FBDBConnUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(memberCardQuery);
        while (rs.next()) {
            MemberCard memberCard = new MemberCard();

            String cardCode = rs.getString("CARDNUMBER");
            memberCard.setCardCode(cardCode);

            String memberCardName = new String(rs.getBytes("CARDNAME"), "gb2312");
            memberCard.setMemberCardName(memberCardName);

            String carNumber = new String(rs.getBytes("VEHICLENUMBER"), "gb2312");
            memberCard.setCarNumber(carNumber);

            String cardType = new String(rs.getBytes("CARDTYPE"), "gb2312");
            memberCard.setCardType(cardType);

            String dateCreated = rs.getString("CARDDATE");
            memberCard.setDateCreated(dateCreated);

            String balance = rs.getString("REMAINTOTALSUM");
            memberCard.setBalance(balance);

            String name = new String(rs.getBytes("CLIENTLINKER"), "gb2312");
            memberCard.setName(name);

            String phone = rs.getString("MOBILEPHONE");
            memberCard.setPhone(phone);

            String remark = new String(rs.getBytes("ISSTOP"), "gb2312");
            memberCard.setRemark(remark);

            memberCards.add(memberCard);
        }

        System.out.print("结果为" + memberCards.toString());
        System.out.print("大小为" + memberCards.size());

        String pathname = "D:\\商帅会员卡导出.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, workbook, pathname);
    }

}
