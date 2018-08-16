package com.ys.datatool.service.cloudCar;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.ys.datatool.domain.CloudCarModelEntity;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.NotMatchVINLevelIds;
import com.ys.datatool.util.ExportUtil;
import org.bson.Document;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void testData() {

        String sak="WVWZZZ33ZWW226668";
        System.out.println("结果为"+sak.substring(11,sak.length()));
    }

    @Test
    public void fetchVINLevelFromMongo() throws Exception{
        List<Map<String, Object>> mongoDatas = new ArrayList<Map<String, Object>>();
        Map<String,String> vinLevelIds=new HashMap<>();
        MongoClient mongoClient = new MongoClient("192.168.1.222", 29017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("SuperManagerV2");
        MongoCollection<Document> collection = mongoDatabase.getCollection("NotMatchVINLevelIds");

        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            Map<String, Object> map = new HashMap<>();
            map.putAll(doc);
            mongoDatas.add(map);
        }

        for (Map mongoData : mongoDatas){
            Object levelIds = mongoData.get("levelIds");
            if (levelIds != null) {
                String vin = mongoData.get("vin").toString();
                String mongoLevelId = mongoData.get("levelIds").toString();

                if(mongoLevelId.contains("WVW")){
                    vinLevelIds.put(vin,mongoLevelId);
                }
            }
        }

        System.out.println("vinLevelIds数据为" +vinLevelIds.toString());
        System.out.println("vinLevelIds为" + vinLevelIds.size());

    }


    @Test
    public void fetchMatchVINLevelIdData() throws Exception {
        List<CloudCarModelEntity> cloudCarModelEntities = new ArrayList<>();
        List<NotMatchVINLevelIds> notMatchVINLevelIds = new ArrayList<>();
        List<Map<String, Object>> mongoDatas = new ArrayList<Map<String, Object>>();

        String query="select level_id,manufacturers,brand,brand_no,series,models," +
                "   year,produced_year,sales_name,vehicle_type,vehicle_size, " +
                "  emission_standard,induction,engine_description,displacement," +
                "  transmission_type,transmission_description " +
                " from  sm_cloud_car_model_all where Manufacturers='上汽大众' or Manufacturers='上海大众';";

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


        JDBC_TEMPLATE.query(query, rs -> {
            while (rs.next()) {
                String levelId = rs.getString("level_id");
                String manufacturers = rs.getString("manufacturers");
                String brand = rs.getString("brand");
                String brandNo = rs.getString("brand_no");
                String series = rs.getString("series");
                String models = rs.getString("models");
                String year = rs.getString("year");
                String producedYear = rs.getString("produced_year");
                String salesName = rs.getString("sales_name");
                String vehicleType = rs.getString("vehicle_type");
                String vehicleSize = rs.getString("vehicle_size");
                String emissionStandard = rs.getString("emission_standard");
                String induction = rs.getString("induction");
                String engineDescription = rs.getString("engine_description");
                String displacement = rs.getString("displacement");
                String transmissionType = rs.getString("transmission_type");
                String transmissionDescription = rs.getString("transmission_description");

                CloudCarModelEntity cloudCarModelEntity = new CloudCarModelEntity();
                cloudCarModelEntity.setLevelId(levelId);
                cloudCarModelEntity.setManufacturers(manufacturers);
                cloudCarModelEntity.setBrand(brand);
                cloudCarModelEntity.setBrandNo(brandNo);
                cloudCarModelEntity.setSeries(series);
                cloudCarModelEntity.setModels(models);
                cloudCarModelEntity.setYear(year);
                cloudCarModelEntity.setProducedYear(producedYear);
                cloudCarModelEntity.setSalesName(salesName);
                cloudCarModelEntity.setVehicleType(vehicleType);
                cloudCarModelEntity.setVehicleSize(vehicleSize);
                cloudCarModelEntity.setEmissionStandard(emissionStandard);
                cloudCarModelEntity.setInduction(induction);
                cloudCarModelEntity.setEngineDescription(engineDescription);
                cloudCarModelEntity.setDisplacement(displacement);
                cloudCarModelEntity.setTransmissionType(transmissionType);
                cloudCarModelEntity.setTransmissionDescription(transmissionDescription);
                cloudCarModelEntities.add(cloudCarModelEntity);
            }
        });


        //预投产环境mongodb地址:192.168.1.251、pdcmongodb地址:192.168.1.222
        MongoClient mongoClient = new MongoClient("192.168.1.222", 29017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("SuperManagerV2");
        MongoCollection<Document> collection = mongoDatabase.getCollection("NotMatchVINLevelIds");

        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();

        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            Map<String, Object> map = new HashMap<>();
            map.putAll(doc);
            mongoDatas.add(map);
        }

        for (Map mongoData : mongoDatas) {
            for (CloudCarModelEntity cloudCarModelEntity : cloudCarModelEntities) {
                String levelId = cloudCarModelEntity.getLevelId();

                Object levelIds = mongoData.get("levelIds");
                if (levelIds != null) {
                    String mongoLevelId = mongoData.get("levelIds").toString();
                    String vin = mongoData.get("vin").toString();

                    if (mongoLevelId.contains(levelId)) {
                        cloudCarModelEntity.setVin(vin);
                        cloudCarModelEntity.setVinOnetoThree(vin.substring(0,3));
                        cloudCarModelEntity.setVinFour(vin.substring(3,4));
                        cloudCarModelEntity.setVinFive(vin.substring(4,5));
                        cloudCarModelEntity.setVinSix(vin.substring(5,6));
                        cloudCarModelEntity.setVinSeventoEight(vin.substring(6,8));
                        cloudCarModelEntity.setVinNine(vin.substring(8,9));
                        cloudCarModelEntity.setVinTen(vin.substring(9,10));
                        cloudCarModelEntity.setVinEleven(vin.substring(10,11));
                        cloudCarModelEntity.setVinTwelvetoSeventeen(vin.substring(11,vin.length()));
                    }
                }
            }
        }

        System.out.println("集合 NotMatchVINLevelIds 选择成功");
        System.out.println("mongoDatas数据为" + mongoDatas.toString());
        System.out.println("cloudCarModelEntities数据为" + cloudCarModelEntities.size());

        String pathname = "C:\\exportExcel\\vin对应levelId车型详情(生产).xlsx";
        ExportUtil.exportCloudCarModelDataInLocal(cloudCarModelEntities, ExcelDatas.workbook, pathname);

    }


}
