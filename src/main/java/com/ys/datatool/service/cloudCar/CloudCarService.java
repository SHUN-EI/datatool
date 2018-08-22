package com.ys.datatool.service.cloudCar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.ys.datatool.domain.CloudCarModelEntity;
import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.util.ExportUtil;
import org.bson.Document;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

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

    private static final ObjectMapper MAPPER = new ObjectMapper();

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

        String sak = "WVWZZZ33ZWW226668";
        System.out.println("结果为" + sak.substring(11, sak.length()));
    }

    /**
     * 根据指定的win码去mongodb找出对应的levelid
     * 再去mysql里面找出levelid其他数据
     *
     * @throws Exception
     */
    @Test
    public void fetchVINLevelFromMongo() throws Exception {
        List<CloudCarModelEntity> cloudCarModelEntities = new ArrayList<>();

        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("SuperManagerV2");
        MongoCollection<Document> collection = mongoDatabase.getCollection("NotMatchVINLevelIds");

        BasicDBObject queryByCondition = new BasicDBObject();
        Pattern pattern = Pattern.compile("^WVW.*$", Pattern.CASE_INSENSITIVE);//左匹配
        queryByCondition.put("vin", pattern);

        FindIterable<Document> find = collection.find(queryByCondition);
        MongoCursor<Document> mongoCur = find.iterator();
        while (mongoCur.hasNext()) {
            Document doc = mongoCur.next();

            Object ids = doc.get("levelIds");
            if (ids != null) {
                JsonNode node = MAPPER.readTree(doc.toJson());
                String vin = node.get("vin").asText();

                JsonNode levelIds = node.get("levelIds");
                int size = node.get("levelIds").size();
                for (int i = 0; i < size; i++) {
                    String levelId = levelIds.get(i).asText();

                    CloudCarModelEntity cloudCarModelEntity = new CloudCarModelEntity();
                    cloudCarModelEntity.setLevelId(levelId);
                    cloudCarModelEntity.setVin(vin);
                    cloudCarModelEntities.add(cloudCarModelEntity);
                }
            }
        }


        String querySomeFieldsByLevelId = "select level_id,manufacturers,brand,brand_no,series,models," +
                "  year,produced_year,sales_name,vehicle_type,vehicle_size, " +
                "  emission_standard,induction,engine_description,displacement," +
                "  transmission_type,transmission_description " +
                " from sm_cloud_car_model_all where level_id=";

        String queryByLevelId = "select  *  from sm_cloud_car_model_all where level_id=";

        if (cloudCarModelEntities.size() > 0) {
            for (CloudCarModelEntity cloudCarModelEntity : cloudCarModelEntities) {
                String levelId = cloudCarModelEntity.getLevelId();
                String condition = queryByLevelId + "'" + levelId + "';";

                JDBC_TEMPLATE.query(condition, rs -> {
                    String manufacturers = rs.getString("manufacturers");
                    String brand = rs.getString("brand");
                    String brandNo = rs.getString("brand_no");
                    String series = rs.getString("series");
                    String models = rs.getString("models");
                    String producedYear = rs.getString("produced_year");
                    String year = rs.getString("year");
                    String salesName = rs.getString("sales_name");
                    String vehicleType = rs.getString("vehicle_type");
                    String vehicleSize = rs.getString("vehicle_size");
                    String emissionStandard = rs.getString("emission_standard");
                    String induction = rs.getString("induction");
                    String engineDescription = rs.getString("engine_description");
                    String displacement = rs.getString("displacement");
                    String transmissionType = rs.getString("transmission_type");
                    String transmissionDescription = rs.getString("transmission_description");

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
                });
            }
        }

        System.out.println("mongoDataJson数据为" + cloudCarModelEntities.toString());
        System.out.println("mongoDataJson为" + cloudCarModelEntities.size());

        String pathname = "C:\\exportExcel\\vin对应levelId(WVW开头).xlsx";
        ExportUtil.exportCloudCarModelDataInLocal(cloudCarModelEntities, ExcelDatas.workbook, pathname);
    }

    /**
     * 根据指定的条件去mysql查询levelid相关的车型数据
     * 再去mongodb找出levelid对应的win码
     *
     * @throws Exception
     */
    @Test
    public void fetchMatchVINLevelIdData() throws Exception {
        List<CloudCarModelEntity> cloudCarModelEntities = new ArrayList<>();
        List<Map<String, Object>> mongoDatas = new ArrayList<Map<String, Object>>();

        String querySomeFields = "select level_id,manufacturers,brand,brand_no,series,models," +
                "   year,produced_year,sales_name,vehicle_type,vehicle_size, " +
                "  emission_standard,induction,engine_description,displacement," +
                "  transmission_type,transmission_description " +
                " from  sm_cloud_car_model_all where Manufacturers='上汽大众' or Manufacturers='上海大众';";

        /** Class.forName(DRIVER);
         Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement statement = con.createStatement();
         ResultSet rs = statement.executeQuery(query);*/

        String query = "select * from  sm_cloud_car_model_all where Manufacturers='上汽大众' or Manufacturers='上海大众';";


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
                String name = rs.getString("name");
                String seriesNo = rs.getString("series_no");
                String generation = rs.getString("generation");
                String chassisCode = rs.getString("chassis_code");
                String salesVersion = rs.getString("sales_version");
                String guidingPrice = rs.getString("guiding_price");
                String listingYear = rs.getString("listing_year");
                String listingMonth = rs.getString("listing_month");
                String idlingYear = rs.getString("idling_year");
                String productionStatus = rs.getString("production_status");
                String salesStatus = rs.getString("sales_status");
                String country = rs.getString("country");
                String vehicleAttributes = rs.getString("vehicle_attributes");
                String modelsCode = rs.getString("models_code");
                String engineModel = rs.getString("engine_model");
                String cylinderVolume = rs.getString("cylinder_volume");
                String fuelType = rs.getString("fuel_type");
                String fuelGrade = rs.getString("fuel_grade");
                String horsepower = rs.getString("horsepower");
                String powerKw = rs.getString("power_kw");
                String powerRpm = rs.getString("power_rpm");
                String torqueNm = rs.getString("torque_nm");
                String torqueRpm = rs.getString("torque_rpm");
                String cylinderArrangement = rs.getString("cylinder_arrangement");
                String cylinders = rs.getString("cylinders");
                String valvesPerCylinder = rs.getString("valves_per_cylinder");
                String compressionRatio = rs.getString("compression_ratio");
                String fuelInjection = rs.getString("fuel_injection");
                String combinedFuelConsumption = rs.getString("combined_fuel_consumption");
                String urbanFuelConsumption = rs.getString("urban_fuel_consumption");
                String suburbFuelConsumption = rs.getString("suburb_fuel_consumption");
                String acceleration = rs.getString("acceleration");
                String maxSpeed = rs.getString("max_speed");
                String engineKnowhow = rs.getString("engine_knowhow");
                String catalyst = rs.getString("catalyst");
                String coolingMethod = rs.getString("cooling_method");
                String bore = rs.getString("bore");
                String stroke = rs.getString("stroke");
                String gearNumber = rs.getString("gear_number");
                String frontBrake = rs.getString("front_brake");
                String rearBrake = rs.getString("rear_brake");
                String frontSuspension = rs.getString("front_suspension");
                String rearSuspension = rs.getString("rear_suspension");
                String steering = rs.getString("steering");
                String powerSteering = rs.getString("power_steering");
                String minGroundClearance = rs.getString("min_ground_clearance");
                String minTurningRadius = rs.getString("min_turning_radius");
                String accessAngle = rs.getString("access_angle");
                String departureAngle = rs.getString("departure_angle");
                String engineLocation = rs.getString("engine_location");
                String driveMode = rs.getString("drive_mode");
                String driveModel = rs.getString("drive_model");
                String bodyType = rs.getString("body_type");
                String length = rs.getString("length");
                String width = rs.getString("width");
                String height = rs.getString("height");
                String wheelbase = rs.getString("wheelbase");
                String frontTrack = rs.getString("front_track");
                String rearTrack = rs.getString("rear_track");
                String curbWeight = rs.getString("curb_weight");
                String maxLoading = rs.getString("max_loading");
                String fuelTankCapacity = rs.getString("fuel_tank_capacity");
                String luggagePlace = rs.getString("luggage_place");
                String roofType = rs.getString("roof_type");
                String calash = rs.getString("calash");
                String doors = rs.getString("doors");
                String seats = rs.getString("seats");
                String frontTyre = rs.getString("front_tyre");
                String rearTyre = rs.getString("rear_tyre");
                String frontRim = rs.getString("front_rim");
                String rearRim = rs.getString("rear_rim");
                String rimsMaterial = rs.getString("rims_material");
                String spareWheel = rs.getString("spare_wheel");
                String driverAirbag = rs.getString("driver_airbag");
                String passengerAirbag = rs.getString("passenger_airbag");
                String frontSideAirbag = rs.getString("front_side_airbag");
                String rearSideAirbag = rs.getString("rear_side_airbag");
                String frontCurtainAirbag = rs.getString("front_curtain_airbag");
                String rearCurtainAirbag = rs.getString("rear_curtain_airbag");
                String kneeAirbag = rs.getString("knee_airbag");
                String tirePressureMonitor = rs.getString("tire_pressure_monitor");
                String runFlatTyre = rs.getString("run_flat_tyre");
                String seatbeltWarningLamp = rs.getString("seatbelt_warning_lamp");
                String isofix = rs.getString("isofix");
                String latch = rs.getString("latch");
                String engineAntitheft = rs.getString("engine_antitheft");
                String centralLocking = rs.getString("central_locking");
                String remoteControl = rs.getString("remote_control");
                String keylessEntry = rs.getString("keyless_entry");
                String keylessGo = rs.getString("keyless_go");
                String abs= rs.getString("abs");
                String ebd= rs.getString("ebd");
                String eba= rs.getString("eba");
                String asr= rs.getString("asr");
                String esp= rs.getString("esp");
                String epb= rs.getString("epb");
                String hdc= rs.getString("hdc");
                String variableSuspension= rs.getString("variable_suspension");
                String airSuspension= rs.getString("air_suspension");
                String variableSteeringRatio= rs.getString("variable_steering_ratio");
                String blis= rs.getString("blis");
                String activeBrake= rs.getString("active_brake");
                String activeSteering= rs.getString("active_steering");
                String leatherSteeringWheel= rs.getString("leather_steering_wheel");
                String heightAdjustableSteeringWheel= rs.getString("height_adjustable_steering_wheel");
                String lengthAdjustableSteeringWheel= rs.getString("length_adjustable_steering_wheel");
                String electricAdjustableSteeringWheel= rs.getString("electric_adjustable_steering_wheel");
                String multifunctionSteeringWheel= rs.getString("multifunction_steering_wheel");
                String steeringWheelWithShift= rs.getString("steering_wheel_with_shift");
                String leatherSeat= rs.getString("leather_seat");
                String sportSeat= rs.getString("sport_seat");
                String heightAdjustableSeat= rs.getString("height_adjustable_seat");
                String lumberSupportAdjustable= rs.getString("lumber_support_adjustable");
                String shoulderSupportAdjustable= rs.getString("shoulder_support_adjustable");




                CloudCarModelEntity cloudCarModelEntity = new CloudCarModelEntity();
                cloudCarModelEntity.setLevelId(levelId);
                cloudCarModelEntity.setVariableSteeringRatio(variableSteeringRatio);
                cloudCarModelEntity.setBLIS(blis);
                cloudCarModelEntity.setActiveBrake(activeBrake);
                cloudCarModelEntity.setActiveSteering(activeSteering);
                cloudCarModelEntity.setLeatherSteeringWheel(leatherSteeringWheel);
                cloudCarModelEntity.setHeightAdjustableSteeringWheel(heightAdjustableSteeringWheel);
                cloudCarModelEntity.setLengthAdjustableSteeringWheel(lengthAdjustableSteeringWheel);
                cloudCarModelEntity.setElectricAdjustableSteeringWheel(electricAdjustableSteeringWheel);
                cloudCarModelEntity.setMultifunctionSteeringWheel(multifunctionSteeringWheel);
                cloudCarModelEntity.setSteeringWheelWithShift(steeringWheelWithShift);
                cloudCarModelEntity.setLeatherSeat(leatherSeat);
                cloudCarModelEntity.setSportSeat(sportSeat);
                cloudCarModelEntity.setHeightAdjustableSeat(heightAdjustableSeat);
                cloudCarModelEntity.setLumberSupportAdjustable(lumberSupportAdjustable);
                cloudCarModelEntity.setShoulderSupportAdjustable(shoulderSupportAdjustable);
                cloudCarModelEntity.setRunFlatTyre(runFlatTyre);
                cloudCarModelEntity.setSeatbeltWarningLamp(seatbeltWarningLamp);
                cloudCarModelEntity.setISOFIX(isofix);
                cloudCarModelEntity.setLATCH(latch);
                cloudCarModelEntity.setEngineAntitheft(engineAntitheft);
                cloudCarModelEntity.setCentralLocking(centralLocking);
                cloudCarModelEntity.setRemoteControl(remoteControl);
                cloudCarModelEntity.setKeylessEntry(keylessEntry);
                cloudCarModelEntity.setKeylessGo(keylessGo);
                cloudCarModelEntity.setABS(abs);
                cloudCarModelEntity.setEBD(ebd);
                cloudCarModelEntity.setEBA(eba);
                cloudCarModelEntity.setASR(asr);
                cloudCarModelEntity.setESP(esp);
                cloudCarModelEntity.setEPB(epb);
                cloudCarModelEntity.setHDC(hdc);
                cloudCarModelEntity.setVariableSuspension(variableSuspension);
                cloudCarModelEntity.setAirSuspension(airSuspension);
                cloudCarModelEntity.setRearRim(rearRim);
                cloudCarModelEntity.setRimsMaterial(rimsMaterial);
                cloudCarModelEntity.setSpareWheel(spareWheel);
                cloudCarModelEntity.setDriverAirbag(driverAirbag);
                cloudCarModelEntity.setPassengerAirbag(passengerAirbag);
                cloudCarModelEntity.setFrontSideAirbag(frontSideAirbag);
                cloudCarModelEntity.setRearSideAirbag(rearSideAirbag);
                cloudCarModelEntity.setFrontCurtainAirbag(frontCurtainAirbag);
                cloudCarModelEntity.setRearCurtainAirbag(rearCurtainAirbag);
                cloudCarModelEntity.setKneeAirbag(kneeAirbag);
                cloudCarModelEntity.setTirePressureMonitor(tirePressureMonitor);
                cloudCarModelEntity.setRearTrack(rearTrack);
                cloudCarModelEntity.setCurbWeight(curbWeight);
                cloudCarModelEntity.setMaxLoading(maxLoading);
                cloudCarModelEntity.setFuelTankCapacity(fuelTankCapacity);
                cloudCarModelEntity.setLuggagePlace(luggagePlace);
                cloudCarModelEntity.setRoofType(roofType);
                cloudCarModelEntity.setCalash(calash);
                cloudCarModelEntity.setDoors(doors);
                cloudCarModelEntity.setSeats(seats);
                cloudCarModelEntity.setFrontTyre(frontTyre);
                cloudCarModelEntity.setRearTyre(rearTyre);
                cloudCarModelEntity.setFrontRim(frontRim);
                cloudCarModelEntity.setFrontSuspension(frontSuspension);
                cloudCarModelEntity.setRearSuspension(rearSuspension);
                cloudCarModelEntity.setSteering(steering);
                cloudCarModelEntity.setPowerSteering(powerSteering);
                cloudCarModelEntity.setMinGroundClearance(minGroundClearance);
                cloudCarModelEntity.setMinTurningRadius(minTurningRadius);
                cloudCarModelEntity.setAccessAngle(accessAngle);
                cloudCarModelEntity.setDepartureAngle(departureAngle);
                cloudCarModelEntity.setEngineLocation(engineLocation);
                cloudCarModelEntity.setDriveMode(driveMode);
                cloudCarModelEntity.setDriveModel(driveModel);
                cloudCarModelEntity.setBodyType(bodyType);
                cloudCarModelEntity.setLength(length);
                cloudCarModelEntity.setWidth(width);
                cloudCarModelEntity.setHeight(height);
                cloudCarModelEntity.setWheelbase(wheelbase);
                cloudCarModelEntity.setFrontTrack(frontTrack);
                cloudCarModelEntity.setCylinderArrangement(cylinderArrangement);
                cloudCarModelEntity.setCylinders(cylinders);
                cloudCarModelEntity.setValvesPerCylinder(valvesPerCylinder);
                cloudCarModelEntity.setCompressionRatio(compressionRatio);
                cloudCarModelEntity.setFuelInjection(fuelInjection);
                cloudCarModelEntity.setCombinedFuelConsumption(combinedFuelConsumption);
                cloudCarModelEntity.setUrbanFuelConsumption(urbanFuelConsumption);
                cloudCarModelEntity.setSuburbFuelConsumption(suburbFuelConsumption);
                cloudCarModelEntity.setAcceleration(acceleration);
                cloudCarModelEntity.setMaxSpeed(maxSpeed);
                cloudCarModelEntity.setEngineKnowhow(engineKnowhow);
                cloudCarModelEntity.setCatalyst(catalyst);
                cloudCarModelEntity.setCoolingMethod(coolingMethod);
                cloudCarModelEntity.setBore(bore);
                cloudCarModelEntity.setStroke(stroke);
                cloudCarModelEntity.setGearNumber(gearNumber);
                cloudCarModelEntity.setFrontBrake(frontBrake);
                cloudCarModelEntity.setRearBrake(rearBrake);
                cloudCarModelEntity.setIdlingYear(idlingYear);
                cloudCarModelEntity.setProductionStatus(productionStatus);
                cloudCarModelEntity.setSalesStatus(salesStatus);
                cloudCarModelEntity.setCountry(country);
                cloudCarModelEntity.setVehicleAttributes(vehicleAttributes);
                cloudCarModelEntity.setModelCode(modelsCode);
                cloudCarModelEntity.setEngineModel(engineModel);
                cloudCarModelEntity.setCylinderVolume(cylinderVolume);
                cloudCarModelEntity.setFuelType(fuelType);
                cloudCarModelEntity.setFuelGrade(fuelGrade);
                cloudCarModelEntity.setHorsepower(horsepower);
                cloudCarModelEntity.setPowerKw(powerKw);
                cloudCarModelEntity.setPowerRpm(powerRpm);
                cloudCarModelEntity.setTorqueNm(torqueNm);
                cloudCarModelEntity.setTorqueRpm(torqueRpm);
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
                cloudCarModelEntity.setName(name);
                cloudCarModelEntity.setSeriesNo(seriesNo);
                cloudCarModelEntity.setGeneration(generation);
                cloudCarModelEntity.setChassisCode(chassisCode);
                cloudCarModelEntity.setSalesVersion(salesVersion);
                cloudCarModelEntity.setGuidingPrice(guidingPrice);
                cloudCarModelEntity.setListingYear(listingYear);
                cloudCarModelEntity.setListingMonth(listingMonth);
                cloudCarModelEntities.add(cloudCarModelEntity);
            }
        });


        //预投产环境mongodb地址:192.168.1.251、pdcmongodb地址:192.168.1.222
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
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
                        cloudCarModelEntity.setVinOnetoThree(vin.substring(0, 3));
                        cloudCarModelEntity.setVinFour(vin.substring(3, 4));
                        cloudCarModelEntity.setVinFive(vin.substring(4, 5));
                        cloudCarModelEntity.setVinSix(vin.substring(5, 6));
                        cloudCarModelEntity.setVinSeventoEight(vin.substring(6, 8));
                        cloudCarModelEntity.setVinNine(vin.substring(8, 9));
                        cloudCarModelEntity.setVinTen(vin.substring(9, 10));
                        cloudCarModelEntity.setVinEleven(vin.substring(10, 11));
                        cloudCarModelEntity.setVinTwelvetoSeventeen(vin.substring(11, vin.length()));
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
