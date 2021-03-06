package com.ys.datatool.util;

import com.ys.datatool.domain.entity.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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

    public static List<Map<String, Object>> createStoreRoomList(Set<StoreRoom> storeRooms) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "仓库");
        listMap.add(map);

        for (StoreRoom storeRoom : storeRooms) {
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("name", storeRoom.getName());
            mapValue.put("companyName", storeRoom.getCompanyName());
            mapValue.put("locationName", storeRoom.getLocationName());
            mapValue.put("remark", storeRoom.getRemark());
            listMap.add(mapValue);
        }

        return listMap;
    }

    public static List<Map<String, Object>> createCloudCarModelList(List<CloudCarModelEntity> carModelEntities) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "车型表");
        listMap.add(map);

        CloudCarModelEntity cloudCarModelEntity = null;
        for (int i = 0; i < carModelEntities.size(); i++) {
            cloudCarModelEntity = carModelEntities.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("levelId", cloudCarModelEntity.getLevelId());
            mapValue.put("manufacturers", cloudCarModelEntity.getManufacturers());
            mapValue.put("brand", cloudCarModelEntity.getBrand());
            mapValue.put("brand_no", cloudCarModelEntity.getBrandNo());
            mapValue.put("series", cloudCarModelEntity.getSeries());
            mapValue.put("models", cloudCarModelEntity.getModels());
            mapValue.put("year", cloudCarModelEntity.getYear());
            mapValue.put("produced_year", cloudCarModelEntity.getProducedYear());
            mapValue.put("sales_name", cloudCarModelEntity.getSalesName());
            mapValue.put("vehicle_type", cloudCarModelEntity.getVehicleType());
            mapValue.put("vehicle_size", cloudCarModelEntity.getVehicleSize());
            mapValue.put("emission_standard", cloudCarModelEntity.getEmissionStandard());
            mapValue.put("induction", cloudCarModelEntity.getInduction());
            mapValue.put("engine_description", cloudCarModelEntity.getEngineDescription());
            mapValue.put("displacement", cloudCarModelEntity.getDisplacement());
            mapValue.put("transmission_type", cloudCarModelEntity.getTransmissionType());
            mapValue.put("transmission_description", cloudCarModelEntity.getTransmissionDescription());
            mapValue.put("num", cloudCarModelEntity.getNum());
            mapValue.put("itemCode", cloudCarModelEntity.getItemCode());
            mapValue.put("vin", cloudCarModelEntity.getVin());
            mapValue.put("vinOnetoThree", cloudCarModelEntity.getVinOnetoThree());
            mapValue.put("vinFour", cloudCarModelEntity.getVinFour());
            mapValue.put("vinFive", cloudCarModelEntity.getVinFive());
            mapValue.put("vinSix", cloudCarModelEntity.getVinSix());
            mapValue.put("vinSeventoEight", cloudCarModelEntity.getVinSeventoEight());
            mapValue.put("vinNine", cloudCarModelEntity.getVinNine());
            mapValue.put("vinTen", cloudCarModelEntity.getVinTen());
            mapValue.put("vinEleven", cloudCarModelEntity.getVinEleven());
            mapValue.put("vinTwelvetoSeventeen", cloudCarModelEntity.getVinTwelvetoSeventeen());
            mapValue.put("name", cloudCarModelEntity.getName());
            mapValue.put("series_no", cloudCarModelEntity.getSeriesNo());
            mapValue.put("generation", cloudCarModelEntity.getGeneration());
            mapValue.put("chassis_code", cloudCarModelEntity.getChassisCode());
            mapValue.put("sales_version", cloudCarModelEntity.getSalesVersion());
            mapValue.put("guiding_price", cloudCarModelEntity.getGuidingPrice());
            mapValue.put("listing_year", cloudCarModelEntity.getListingYear());
            mapValue.put("listing_month", cloudCarModelEntity.getListingMonth());
            mapValue.put("idling_year", cloudCarModelEntity.getIdlingYear());
            mapValue.put("production_status", cloudCarModelEntity.getProductionStatus());
            mapValue.put("sales_status", cloudCarModelEntity.getSalesStatus());
            mapValue.put("country", cloudCarModelEntity.getCountry());
            mapValue.put("vehicle_attributes", cloudCarModelEntity.getVehicleAttributes());
            mapValue.put("models_code", cloudCarModelEntity.getModelCode());
            mapValue.put("engine_model", cloudCarModelEntity.getEngineModel());
            mapValue.put("cylinder_volume", cloudCarModelEntity.getCylinderVolume());
            mapValue.put("fuel_type", cloudCarModelEntity.getFuelType());
            mapValue.put("fuel_grade", cloudCarModelEntity.getFuelGrade());
            mapValue.put("horsepower", cloudCarModelEntity.getHorsepower());
            mapValue.put("power_kw", cloudCarModelEntity.getPowerKw());
            mapValue.put("power_rpm", cloudCarModelEntity.getPowerRpm());
            mapValue.put("torque_nm", cloudCarModelEntity.getTorqueNm());
            mapValue.put("torque_rpm", cloudCarModelEntity.getTorqueRpm());
            mapValue.put("cylinder_arrangement", cloudCarModelEntity.getCylinderArrangement());
            mapValue.put("cylinders", cloudCarModelEntity.getCylinders());
            mapValue.put("valves_per_cylinder", cloudCarModelEntity.getValvesPerCylinder());
            mapValue.put("compression_ratio", cloudCarModelEntity.getCompressionRatio());
            mapValue.put("fuel_injection", cloudCarModelEntity.getFuelInjection());
            mapValue.put("combined_fuel_consumption", cloudCarModelEntity.getCombinedFuelConsumption());
            mapValue.put("urban_fuel_consumption", cloudCarModelEntity.getUrbanFuelConsumption());
            mapValue.put("suburb_fuel_consumption", cloudCarModelEntity.getSuburbFuelConsumption());
            mapValue.put("acceleration", cloudCarModelEntity.getAcceleration());
            mapValue.put("max_speed", cloudCarModelEntity.getMaxSpeed());
            mapValue.put("engine_knowhow", cloudCarModelEntity.getEngineKnowhow());
            mapValue.put("catalyst", cloudCarModelEntity.getCatalyst());
            mapValue.put("cooling_method", cloudCarModelEntity.getCoolingMethod());
            mapValue.put("bore", cloudCarModelEntity.getBore());
            mapValue.put("stroke", cloudCarModelEntity.getStroke());
            mapValue.put("gear_number", cloudCarModelEntity.getGearNumber());
            mapValue.put("front_brake", cloudCarModelEntity.getFrontBrake());
            mapValue.put("rear_brake", cloudCarModelEntity.getRearBrake());
            mapValue.put("front_suspension", cloudCarModelEntity.getFrontSuspension());
            mapValue.put("rear_suspension", cloudCarModelEntity.getRearSuspension());
            mapValue.put("steering", cloudCarModelEntity.getSteering());
            mapValue.put("power_steering", cloudCarModelEntity.getPowerSteering());
            mapValue.put("min_ground_clearance", cloudCarModelEntity.getMinGroundClearance());
            mapValue.put("min_turning_radius", cloudCarModelEntity.getMinTurningRadius());
            mapValue.put("access_angle", cloudCarModelEntity.getAccessAngle());
            mapValue.put("departure_angle", cloudCarModelEntity.getDepartureAngle());
            mapValue.put("engine_location", cloudCarModelEntity.getEngineLocation());
            mapValue.put("drive_mode", cloudCarModelEntity.getDriveMode());
            mapValue.put("drive_model", cloudCarModelEntity.getDriveModel());
            mapValue.put("body_type", cloudCarModelEntity.getBodyType());
            mapValue.put("length", cloudCarModelEntity.getLength());
            mapValue.put("width", cloudCarModelEntity.getWidth());
            mapValue.put("height", cloudCarModelEntity.getHeight());
            mapValue.put("wheelbase", cloudCarModelEntity.getWheelbase());
            mapValue.put("front_track", cloudCarModelEntity.getFrontTrack());
            mapValue.put("rear_track", cloudCarModelEntity.getRearTrack());
            mapValue.put("curb_weight", cloudCarModelEntity.getCurbWeight());
            mapValue.put("max_loading", cloudCarModelEntity.getMaxLoading());
            mapValue.put("fuel_tank_capacity", cloudCarModelEntity.getFuelTankCapacity());
            mapValue.put("luggage_place", cloudCarModelEntity.getLuggagePlace());
            mapValue.put("roof_type", cloudCarModelEntity.getRoofType());
            mapValue.put("calash", cloudCarModelEntity.getCalash());
            mapValue.put("doors", cloudCarModelEntity.getDoors());
            mapValue.put("seats", cloudCarModelEntity.getSeats());
            mapValue.put("front_tyre", cloudCarModelEntity.getFrontTyre());
            mapValue.put("rear_tyre", cloudCarModelEntity.getRearTyre());
            mapValue.put("front_rim", cloudCarModelEntity.getFrontRim());
            mapValue.put("rear_rim", cloudCarModelEntity.getRearRim());
            mapValue.put("rims_material", cloudCarModelEntity.getRimsMaterial());
            mapValue.put("spare_wheel", cloudCarModelEntity.getSpareWheel());
            mapValue.put("driver_airbag", cloudCarModelEntity.getDriverAirbag());
            mapValue.put("passenger_airbag", cloudCarModelEntity.getPassengerAirbag());
            mapValue.put("front_side_airbag", cloudCarModelEntity.getFrontSideAirbag());
            mapValue.put("rear_side_airbag", cloudCarModelEntity.getRearSideAirbag());
            mapValue.put("front_curtain_airbag", cloudCarModelEntity.getFrontCurtainAirbag());
            mapValue.put("rear_curtain_airbag", cloudCarModelEntity.getRearCurtainAirbag());
            mapValue.put("knee_airbag", cloudCarModelEntity.getKneeAirbag());
            mapValue.put("tire_pressure_monitor", cloudCarModelEntity.getTirePressureMonitor());
            mapValue.put("run_flat_tyre", cloudCarModelEntity.getRunFlatTyre());
            mapValue.put("seatbelt_warning_lamp", cloudCarModelEntity.getSeatbeltWarningLamp());
            mapValue.put("isofix", cloudCarModelEntity.getISOFIX());
            mapValue.put("latch", cloudCarModelEntity.getLATCH());
            mapValue.put("engine_antitheft", cloudCarModelEntity.getEngineAntitheft());
            mapValue.put("central_locking", cloudCarModelEntity.getCentralLocking());
            mapValue.put("remote_control", cloudCarModelEntity.getRemoteControl());
            mapValue.put("keyless_entry", cloudCarModelEntity.getKeylessEntry());
            mapValue.put("keyless_go", cloudCarModelEntity.getKeylessGo());
            mapValue.put("abs", cloudCarModelEntity.getABS());
            mapValue.put("ebd", cloudCarModelEntity.getEBD());
            mapValue.put("eba", cloudCarModelEntity.getEBA());
            mapValue.put("asr", cloudCarModelEntity.getASR());
            mapValue.put("esp", cloudCarModelEntity.getESP());
            mapValue.put("epb", cloudCarModelEntity.getEPB());
            mapValue.put("hdc", cloudCarModelEntity.getHDC());
            mapValue.put("variable_suspension", cloudCarModelEntity.getVariableSuspension());
            mapValue.put("air_suspension", cloudCarModelEntity.getAirSuspension());
            mapValue.put("variable_steering_ratio", cloudCarModelEntity.getVariableSteeringRatio());
            mapValue.put("blis", cloudCarModelEntity.getBLIS());
            mapValue.put("active_brake", cloudCarModelEntity.getActiveBrake());
            mapValue.put("active_steering", cloudCarModelEntity.getActiveSteering());
            mapValue.put("leather_steering_wheel", cloudCarModelEntity.getLeatherSteeringWheel());
            mapValue.put("height_adjustable_steering_wheel", cloudCarModelEntity.getHeightAdjustableSteeringWheel());
            mapValue.put("length_adjustable_steering_wheel", cloudCarModelEntity.getLengthAdjustableSteeringWheel());
            mapValue.put("electric_adjustable_steering_wheel", cloudCarModelEntity.getElectricAdjustableSteeringWheel());
            mapValue.put("multifunction_steering_wheel", cloudCarModelEntity.getMultifunctionSteeringWheel());
            mapValue.put("steering_wheel_with_shift", cloudCarModelEntity.getSteeringWheelWithShift());
            mapValue.put("leather_seat", cloudCarModelEntity.getLeatherSeat());
            mapValue.put("sport_seat", cloudCarModelEntity.getSportSeat());
            mapValue.put("height_adjustable_seat", cloudCarModelEntity.getHeightAdjustableSeat());
            mapValue.put("lumber_support_adjustable", cloudCarModelEntity.getLumberSupportAdjustable());
            mapValue.put("shoulder_support_adjustable", cloudCarModelEntity.getShoulderSupportAdjustable());
            mapValue.put("driver_seat_power_adjustable", cloudCarModelEntity.getDriverSeatPowerAdjustable());
            mapValue.put("passenger_seat_power_adjustable", cloudCarModelEntity.getPassengerSeatPowerAdjustable());
            mapValue.put("second_row_backrest_adjustable", cloudCarModelEntity.getSecondRowBackrestAdjustable());
            mapValue.put("second_row_seat_position_adjustable", cloudCarModelEntity.getSecondRowSeatPositionAdjustable());
            mapValue.put("rear_seat_power_adjustable", cloudCarModelEntity.getRearSeatPowerAdjustable());
            mapValue.put("memory_seat", cloudCarModelEntity.getMemorySeat());
            mapValue.put("front_seat_heater", cloudCarModelEntity.getFrontSeatHeater());
            mapValue.put("rear_seat_heater", cloudCarModelEntity.getRearSeatHeater());
            mapValue.put("seat_ventilation", cloudCarModelEntity.getSeatVentilation());
            mapValue.put("seat_massage", cloudCarModelEntity.getSeatMassage());
            mapValue.put("overall_rear_seats_fold_down", cloudCarModelEntity.getOverallRearSeatsFoldDown());
            mapValue.put("rear_seats_proportion_fold_down", cloudCarModelEntity.getRearSeatsProportionFoldDown());
            mapValue.put("third_row_seat", cloudCarModelEntity.getThirdRowSeat());
            mapValue.put("front_center_armrest", cloudCarModelEntity.getFrontCenterArmrest());
            mapValue.put("rear_center_armrest", cloudCarModelEntity.getRearCenterArmrest());
            mapValue.put("rear_cup_holder", cloudCarModelEntity.getRearCupHolder());
            mapValue.put("ambientes_lamp", cloudCarModelEntity.getAmbientesLamp());
            mapValue.put("rear_back_window_glass_blind", cloudCarModelEntity.getRearBackWindowGlassBlind());
            mapValue.put("rear_side_window_glass_blind", cloudCarModelEntity.getRearSideWindowGlassBlind());
            mapValue.put("sunvisor_mirror", cloudCarModelEntity.getSunvisorMirror());
            mapValue.put("power_tailgate", cloudCarModelEntity.getPowerTailgate());
            mapValue.put("sport_body_dress_up_kits", cloudCarModelEntity.getSportBodyDressUpKits());
            mapValue.put("electric_suction_door", cloudCarModelEntity.getElectricSuctionDoor());
            mapValue.put("sunroof", cloudCarModelEntity.getSunroof());
            mapValue.put("panoramic_sunroof", cloudCarModelEntity.getPanoramicSunroof());
            mapValue.put("hid_headlamp", cloudCarModelEntity.getHidHeadlamp());
            mapValue.put("led_headlamp", cloudCarModelEntity.getLedHeadlamp());
            mapValue.put("daytime_running_lamp", cloudCarModelEntity.getDaytimeRunningLamp());
            mapValue.put("adaptive_headlamp", cloudCarModelEntity.getAdaptiveHeadlamp());
            mapValue.put("corner_headlamp", cloudCarModelEntity.getCornerHeadlamp());
            mapValue.put("front_fog_lamp", cloudCarModelEntity.getFrontFogLamp());
            mapValue.put("height_adjustable_headlamp", cloudCarModelEntity.getHeightAdjustableHeadlamp());
            mapValue.put("headlamp_washer", cloudCarModelEntity.getHeadlampWasher());
            mapValue.put("front_power_window", cloudCarModelEntity.getFrontPowerWindow());
            mapValue.put("rear_power_window", cloudCarModelEntity.getRearPowerWindow());
            mapValue.put("anti_pinch_glass", cloudCarModelEntity.getAntiPinchGlass());
            mapValue.put("insulated_glass", cloudCarModelEntity.getInsulatedGlass());
            mapValue.put("electrically_adjustable_outside_mirror", cloudCarModelEntity.getElectricallyAdjustableOutsideMirror());
            mapValue.put("heated_outside_mirror", cloudCarModelEntity.getHeatedOutsideMirror());
            mapValue.put("auto_dimming_inside_mirror", cloudCarModelEntity.getAutoDimmingInsideMirror());
            mapValue.put("power_fold_outside_mirror", cloudCarModelEntity.getPowerFoldOutsideMirror());
            mapValue.put("inside_mirror_with_memory", cloudCarModelEntity.getInsideMirrorWithMemory());
            mapValue.put("rear_wiper", cloudCarModelEntity.getRearWiper());
            mapValue.put("rain_sensing_wipers", cloudCarModelEntity.getRainSensingWipers());
            mapValue.put("cruise_control", cloudCarModelEntity.getCruiseControl());
            mapValue.put("parking_assist", cloudCarModelEntity.getParkingAssist());
            mapValue.put("rear_view_camera", cloudCarModelEntity.getRearViewCamera());
            mapValue.put("trip_computer", cloudCarModelEntity.getTripComputer());
            mapValue.put("hud", cloudCarModelEntity.getHUD());
            mapValue.put("gps", cloudCarModelEntity.getGPS());
            mapValue.put("interactive_information_services", cloudCarModelEntity.getInteractiveInformationServices());
            mapValue.put("lcd_screen", cloudCarModelEntity.getLcdScreen());
            mapValue.put("man_machine_interactive_system", cloudCarModelEntity.getManMachineInteractiveSystem());
            mapValue.put("internal_hard_disk", cloudCarModelEntity.getInternalHardDisk());
            mapValue.put("bluetooth", cloudCarModelEntity.getBluetooth());
            mapValue.put("vehicle_tv", cloudCarModelEntity.getVehicleTv());
            mapValue.put("rear_entertainment_screen", cloudCarModelEntity.getRearEntertainmentScreen());
            mapValue.put("entertainment_connector", cloudCarModelEntity.getEntertainmentConnector());
            mapValue.put("MP3", cloudCarModelEntity.getMP3());
            mapValue.put("single_disc_cd", cloudCarModelEntity.getSingleDiscCd());
            mapValue.put("multi_disc_cd", cloudCarModelEntity.getMultiDiscCd());
            mapValue.put("virtual_multi_disc_cd", cloudCarModelEntity.getVirtualMultiDiscCd());
            mapValue.put("single_disc_dvd", cloudCarModelEntity.getSingleDiscDvd());
            mapValue.put("multi_disc_dvd", cloudCarModelEntity.getMultiDiscDvd());
            mapValue.put("speaker_number", cloudCarModelEntity.getSpeakerNumber());
            mapValue.put("ac", cloudCarModelEntity.getAC());
            mapValue.put("autoac", cloudCarModelEntity.getAutoAC());
            mapValue.put("rearac", cloudCarModelEntity.getRearAC());
            mapValue.put("rear_vent", cloudCarModelEntity.getRearVent());
            mapValue.put("aczone_control", cloudCarModelEntity.getACZoneControl());
            mapValue.put("pollen_filter", cloudCarModelEntity.getPollenFilter());
            mapValue.put("refrigerator", cloudCarModelEntity.getRefrigerator());
            mapValue.put("automatic_parking", cloudCarModelEntity.getAutomaticParking());
            mapValue.put("night_vision", cloudCarModelEntity.getNightVision());
            mapValue.put("splitview", cloudCarModelEntity.getSplitview());
            mapValue.put("acc", cloudCarModelEntity.getACC());
            mapValue.put("panoramic_camera", cloudCarModelEntity.getPanoramicCamera());
            mapValue.put("rear_parking_aid", cloudCarModelEntity.getRearParkingAid());
            mapValue.put("telematics", cloudCarModelEntity.getTelematics());
            mapValue.put("valve_system", cloudCarModelEntity.getValveSystem());
            mapValue.put("cylinder_head", cloudCarModelEntity.getCylinderHead());
            mapValue.put("cylinder_block", cloudCarModelEntity.getCylinderBlock());
            mapValue.put("body_structure", cloudCarModelEntity.getBodyStructure());
            mapValue.put("parking_brake", cloudCarModelEntity.getParkingBrake());
            mapValue.put("warranty_period", cloudCarModelEntity.getWarrantyPeriod());
            mapValue.put("vehicle_color", cloudCarModelEntity.getVehicleColor());
            mapValue.put("intelligent_stop_start", cloudCarModelEntity.getIntelligentStopStart());
            mapValue.put("dipped_lights", cloudCarModelEntity.getDippedLights());
            mapValue.put("high_beam", cloudCarModelEntity.getHighBeam());
            mapValue.put("maintenance_remark", cloudCarModelEntity.getMaintenanceRemark());
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
            mapValue.put("secondCategoryName", stock.getSecondCategoryName());
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
        map.put("sheetName", "历史消费记录");
        listMap.add(map);

        Bill bill = null;
        for (int i = 0; i < bills.size(); i++) {
            bill = bills.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("billNo", bill.getBillNo());
            mapValue.put("carNumber", bill.getCarNumber());
            mapValue.put("cardCode", bill.getCardCode());
            mapValue.put("automodel", bill.getAutomodel());
            mapValue.put("name", bill.getName());
            mapValue.put("phone", bill.getPhone());
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
            mapValue.put("companyName", bill.getCompanyName());
            mapValue.put("serviceItemNames", bill.getServiceItemNames());
            mapValue.put("goodsNames", bill.getGoodsNames());
            mapValue.put("receptionistName", bill.getReceptionistName());
            mapValue.put("stockOutNumber", bill.getStockOutNumber());
            mapValue.put("accountType", bill.getAccountType());
            mapValue.put("itemName", bill.getItemName());
            mapValue.put("debtAmount", bill.getDebtAmount());
            mapValue.put("receivedAmount", bill.getReceivedAmount());
            mapValue.put("amount", bill.getAmount());
            mapValue.put("payer", bill.getPayer());
            mapValue.put("billCode", bill.getBillCode());
            mapValue.put("content", bill.getContent());
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
            mapValue.put("itemName", billDetail.getItemName());
            mapValue.put("companyName", billDetail.getCompanyName());
            mapValue.put("salePrice", billDetail.getSalePrice());
            mapValue.put("workingHour", billDetail.getWorkingHour());
            mapValue.put("num", billDetail.getNum());
            mapValue.put("itemType", billDetail.getItemType());
            mapValue.put("itemCode", billDetail.getItemCode());
            mapValue.put("billNo", billDetail.getBillNo());
            mapValue.put("discountRate", billDetail.getDiscountRate());
            mapValue.put("deduction", billDetail.getDeduction());
            mapValue.put("totalAmount", billDetail.getTotalAmount());
            mapValue.put("price", billDetail.getPrice());
            mapValue.put("mileage", billDetail.getMileage());
            mapValue.put("payment", billDetail.getPayment());
            mapValue.put("dateAdded", billDetail.getDateAdded());
            mapValue.put("dateExpect", billDetail.getDateExpect());
            mapValue.put("dateEnd", billDetail.getDateEnd());
            mapValue.put("carNumber", billDetail.getCarNumber());
            mapValue.put("clientName", billDetail.getClientName());
            mapValue.put("firstCategoryName", billDetail.getFirstCategoryName());
            mapValue.put("secondCategoryName", billDetail.getSecondCategoryName());
            mapValue.put("discount", billDetail.getDiscount());
            mapValue.put("originalNum", billDetail.getOriginalNum());
            mapValue.put("usedNum", billDetail.getUsedNum());
            mapValue.put("thisUsedNum", billDetail.getThisUsedNum());
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
            mapValue.put("phone", carInfo.getPhone());
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
            mapValue.put("brandSelect", carInfo.getBrandSelect());
            mapValue.put("brandInput", carInfo.getBrandInput());
            mapValue.put("carSeriesSelect", carInfo.getCarSeriesSelect());
            mapValue.put("carSeriesInput", carInfo.getCarSeriesInput());
            mapValue.put("carModelSelect", carInfo.getCarModelSelect());
            mapValue.put("carModelInput", carInfo.getCarModelInput());
            mapValue.put("balance", carInfo.getBalance());
            listMap.add(mapValue);

        }
        return listMap;
    }

    public static List<Map<String, Object>> createPartsList(List<Part> parts) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "配件信息");
        listMap.add(map);

        Part part = null;
        for (int i = 0; i < parts.size(); i++) {
            part = parts.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("code", part.getCode());
            mapValue.put("name", part.getName());
            mapValue.put("companyName", part.getCompanyName());
            mapValue.put("replacePartCode", part.getReplacePartCode());
            mapValue.put("partsCode", part.getPartsCode());
            mapValue.put("origin", part.getOrigin());
            mapValue.put("carModel", part.getCarModel());
            mapValue.put("specification", part.getSpecification());
            mapValue.put("specificationCapacity", part.getSpecificationCapacity());
            mapValue.put("costPrice", part.getCostPrice());
            mapValue.put("salePrice", part.getSalePrice());
            mapValue.put("unit", part.getUnit());
            mapValue.put("remark", part.getRemark());
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
            mapValue.put("accountName", supplier.getAccountName());
            mapValue.put("depositBank", supplier.getDepositBank());
            mapValue.put("accountNumber", supplier.getAccountNumber());
            mapValue.put("isShare", supplier.getIsShare());
            mapValue.put("remark", supplier.getRemark());
            mapValue.put("code", supplier.getCode());
            mapValue.put("companyName", supplier.getCompanyName());
            mapValue.put("type", supplier.getType());
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
            mapValue.put("ctId", memberCard.getCtId());
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
            mapValue.put("productCode", product.getItemCode());
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
            mapValue.put("cloudGoodsCode", product.getCloudGoodsCode());
            mapValue.put("manufactory", product.getManufactory());
            mapValue.put("manufactoryType", product.getManufactoryType());
            mapValue.put("alias", product.getAlias());

            listMap.add(mapValue);
        }
        return listMap;
    }

    /**
     * excel-xls格式
     *
     * @return
     */
    public static HSSFWorkbook createHSSFWorkbook(List<Map<String, Object>> list, String[] keys, String[] columnNames) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(list.get(0).get("sheetName").toString());
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth(i, 15 * 256);
        }

        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
        }

        for (int i = 1; i < list.size(); i++) {
            HSSFRow r = sheet.createRow(i);
            for (int j = 0; j < keys.length; j++) {
                HSSFCell cell = r.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
            }
        }

        return workbook;
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

