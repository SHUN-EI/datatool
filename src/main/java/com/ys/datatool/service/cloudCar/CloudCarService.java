package com.ys.datatool.service.cloudCar;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.ys.datatool.domain.CloudCarModelEntity;
import org.bson.Document;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on  2018/8/14.
 */
@Service
public class CloudCarService {

    static JdbcTemplate JDBC_TEMPLATE;

    private String DRIVER = "com.mysql.jdbc.Driver";

    private String URL = "jdbc:mysql://192.168.1.253:3308/super_manager_v2";

    private String USER = "root";

    private String PASSWORD = "root";

    static {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("192.168.1.253");
        dataSource.setPortNumber(3308);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("super_manager_v2");
        JDBC_TEMPLATE = new JdbcTemplate(dataSource);
    }



    @Test
    public void fetchData() throws Exception {
        List<CloudCarModelEntity> cloudCarModelEntities = new ArrayList<>();

        String query = "select level_id,manufacturers,brand, series from  sm_cloud_car_model_all " +
                "where Manufacturers='上汽大众' or Manufacturers='上海大众';";

        /** Class.forName(DRIVER);
         Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement statement = con.createStatement();
         ResultSet rs = statement.executeQuery(query);


         while (rs.next()) {
         String levelId = rs.getString("level_id");
         String manufacturers = rs.getString("manufacturers");
         String brand = rs.getString("brand");
         String series = rs.getString("series");

         CloudCarModelEntity cloudCarModelEntity = new CloudCarModelEntity();
         cloudCarModelEntity.setLevelId(levelId);
         cloudCarModelEntity.setManufacturers(manufacturers);
         cloudCarModelEntity.setBrand(brand);
         cloudCarModelEntity.setSeries(series);
         cloudCarModelEntities.add(cloudCarModelEntity);
         }*/


      /**JDBC_TEMPLATE.query(query, rs -> {
            while (rs.next()) {
                String levelId = rs.getString("level_id");
                String manufacturers = rs.getString("manufacturers");
                String brand = rs.getString("brand");
                String series = rs.getString("series");

                CloudCarModelEntity cloudCarModelEntity = new CloudCarModelEntity();
                cloudCarModelEntity.setLevelId(levelId);
                cloudCarModelEntity.setManufacturers(manufacturers);
                cloudCarModelEntity.setBrand(brand);
                cloudCarModelEntity.setSeries(series);
                cloudCarModelEntities.add(cloudCarModelEntity);
            }
        });*/


        MongoClient mongoClient=new MongoClient("192.168.1.251",29017);
        MongoDatabase mongoDatabase=mongoClient.getDatabase("SuperManagerV2");
        MongoCollection<Document> collection = mongoDatabase.getCollection("NotMatchVINLevelIds");

        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while(mongoCursor.hasNext()){

            Document doc= mongoCursor.next();


            String s="";
        }

        System.out.println("集合 NotMatchVINLevelIds 选择成功");

        //System.out.println("结果为" + cloudCarModelEntities.size());
        String pathname = "C:\\exportExcel\\云车型.xlsx";
       // ExportUtil.exportCloudCarModelDataInLocal(cloudCarModelEntities, ExcelDatas.workbook, pathname);

    }


}
