package com.ys.datatool.service.local;

import com.ys.datatool.domain.entity.MemberCard;
import com.ys.datatool.domain.entity.MemberCardItem;
import com.ys.datatool.util.ExportUtil;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mo on @date  2018/4/21.
 * 超越系统
 */

@Service
public class SurpassService {

    static JdbcTemplate JDBC_TEMPLATE;

    private static final String DRIVER = "net.sourceforge.jtds.jdbc.Driver";

    private static final String URL = "jdbc:jtds:sqlserver://localhost:1433/surpass;instance=TEST2K";

    private static final String USER = "sa";

    private static final String PASSWORD = "root";

    private String fileName = "超越大典软件";

    private Workbook workbook = new HSSFWorkbook();

    static {

        JtdsDataSource dataSource = new JtdsDataSource();
        dataSource.setServerName("127.0.0.1");
        //dataSource.setInstance("TEST2K");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("surpass");
        dataSource.setUser("sa");
        dataSource.setPassword("root");
        JDBC_TEMPLATE = new JdbcTemplate(dataSource);
    }

    /**
     * 会员卡
     * @throws Exception
     */
    @Test
    public void fetchMemberCardData() throws Exception {
        List<MemberCard> memberCards = new ArrayList<>();

        String memberCardQuery = "select m.MemberNo,m.MemberType,m.BuyDate," +
                "m.MemberLimit,v.RegisterNo,v.GuestName," +
                "v.ContactTel,b.PreBalance " +
                " from dbo.C_MemberBuy m " +
                "inner join dbo.C_Vehicle v " +
                "on v.VIN=m.VIN " +
                "inner join dbo.T_ReceivableBalance b " +
                "on b.GuestCode=v.GuestCode";

        JDBC_TEMPLATE.query(memberCardQuery,
                rs -> {
                    MemberCard memberCard = new MemberCard();
                    memberCard.setCardCode(rs.getString("MemberNo"));
                    memberCard.setMemberCardName(rs.getString("MemberType"));
                    memberCard.setDateCreated(rs.getString("BuyDate"));
                    memberCard.setCarNumber(rs.getString("RegisterNo"));
                    memberCard.setName(rs.getString("GuestName"));
                    memberCard.setPhone(rs.getString("ContactTel"));
                    memberCard.setValidTime(rs.getString("MemberLimit"));

                    String balance = rs.getString("PreBalance");
                    memberCard.setBalance(balance == null ? "0" : balance);
                    memberCards.add(memberCard);
                });

        System.out.print("结果为" + memberCards.toString());
        System.out.print("大小为" + memberCards.size());

        String pathname = "D:\\超越会员卡导出.xls";
        ExportUtil.exportMemberCardDataInLocal(memberCards, workbook, pathname);

    }


    /**
     * 卡内项目
     * @throws Exception
     */
    @Test
    public void fetchMemberCardItemData() throws Exception {

        List<MemberCardItem> memberCardItems = new ArrayList<>();
        Set<String> itemSet = new HashSet<>();

        String memberCardItemQuery = "select sale.MemberNo,detai.JobName," +
                "detai.TotalCount,detai.HasUsedCount," +
                "job.TypeDesc,m.MemberLimit,v.RegisterNo  " +
                "from dbo.C_SetServiceSales sale " +
                "inner join dbo.C_SetServiceSalesDetai detai " +
                "on detai.SetIDNo=sale.SetIDNo " +
                "inner join dbo.C_JobCode job " +
                "on job.JobCode=detai.JobCode " +
                "inner join C_MemberBuy m " +
                "on m.MemberNo=sale.MemberNo " +
                " inner join dbo.C_Vehicle v " +
                " on v.VIN=m.VIN  " +
                "where sale.MemberNo=sale.MemberNo " +
                "order by m.MemberLimit desc";

        JDBC_TEMPLATE.query(memberCardItemQuery,
                rs -> {
                    String cardCode = rs.getString("MemberNo");
                    String itemName = rs.getString("JobName");
                    String validTime = rs.getString("MemberLimit");
                    String firstCategoryName = rs.getString("TypeDesc");
                    String carNumber = rs.getString("RegisterNo");

                    String originalNumStr = rs.getString("TotalCount");
                    if (originalNumStr == null)
                        originalNumStr = "0";
                    int originalNum = Integer.parseInt(originalNumStr);

                    String usedNumStr = rs.getString("HasUsedCount");
                    if (usedNumStr == null)
                        usedNumStr = "0";

                    int usedNum = Integer.parseInt(usedNumStr);

                    int num = originalNum - usedNum;

                    MemberCardItem memberCardItem = new MemberCardItem();
                    memberCardItem.setCardCode(cardCode);
                    memberCardItem.setItemName(itemName);
                    memberCardItem.setOriginalNum(originalNumStr);
                    memberCardItem.setUsedNum(usedNumStr);
                    memberCardItem.setNum(String.valueOf(num));
                    memberCardItem.setFirstCategoryName(firstCategoryName);
                    memberCardItem.setValidTime(validTime);
                    memberCardItem.setCarNumber(carNumber);

                    memberCardItems.add(memberCardItem);
                });

        System.out.print("结果为" + memberCardItems.toString());
        System.out.print("大小为" + memberCardItems.size());

        String pathname = "D:\\超越卡内项目导出.xls";
        ExportUtil.exportMemberCardItemDataInLocal(memberCardItems, workbook, pathname);

    }

}
