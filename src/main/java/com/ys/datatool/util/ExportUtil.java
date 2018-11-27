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

    public static void exportConsumptionRecordDataToExcel07InLocal(List<Bill> bills, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createBillList(bills);
        String[] keys = new String[]{"companyName", "billNo", "dateEnd", "carNumber", "mileage",
                "serviceItemNames", "goodsNames", "totalAmount", "receptionistName",
                "remark"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.consumptionRecordDatas);
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

    public static void exportConsumptionRecordDataToExcel03InLocal(List<Bill> bills, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createBillList(bills);
        String[] keys = new String[]{"companyName", "billNo", "dateEnd", "carNumber", "mileage",
                "serviceItemNames", "goodsNames", "totalAmount", "receptionistName",
                "remark"};

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.consumptionRecordDatas);
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

    /**
     * 元乐车宝-导出库存相关数据
     *
     * @param stocks
     * @param workbook
     * @param pathname
     * @throws IOException
     */
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
     * 配件
     *
     * @param parts
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportPartDataInLocal(List<Part> parts, Workbook workbook, String pathname) throws IOException {

        List<Map<String, Object>> list = ExcelUtil.createPartsList(parts);
        String[] keys = new String[]{"companyName", "code", "name", "replacePartCode", "partsCode", "origin", "carModel",
                "specification", "specificationCapacity", "costPrice", "salePrice", "unit", "remark"
        };

        OutputStream outputStream = null;
        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.partDatas);
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
     *
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
                "carModel", "phone", "registerDate", "engineNumber",
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

    /**
     * 单据-标准模版导出
     *
     * @param bills
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportBillDataInLocal(List<Bill> bills, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createBillList(bills);
        String[] keys = new String[]{"companyName", "billNo", "carNumber", "mileage",
                "phone", "name", "totalAmount", "discount", "actualAmount", "waitInStore",
                "dateExpect", "payType", "remark", "dateAdded", "dateEnd"};
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.billDatas);
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
     * 单据明细-标准模版导出
     *
     * @param billDetails
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportBillDetailDataInLocal(List<BillDetail> billDetails, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createBillDetailList(billDetails);
        String[] keys = new String[]{"companyName", "billNo", "itemName", "num",
                "price", "discount", "itemType", "firstCategoryName", "secondCategoryName", "itemCode"
        };
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.billItemDatas);
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
     * @param cloudCarModelEntities
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportCloudCarModelDataInLocal(List<CloudCarModelEntity> cloudCarModelEntities, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createCloudCarModelList(cloudCarModelEntities);
        String[] keys = new String[]{"vin", "levelId", "manufacturers", "brand", "brand_no", "series",
                "models", "year", "produced_year", "sales_name", "vehicle_type", "vehicle_size", "emission_standard",
                "induction", "engine_description", "displacement", "transmission_type", "transmission_description",
                "vinOnetoThree", "vinFour", "vinFive", "vinSix", "vinSeventoEight", "vinNine", "vinTen", "vinEleven", "vinTwelvetoSeventeen"
        };
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.cloudCarModelDatas);
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
     * 力洋数据所有字段
     *
     * @param cloudCarModelEntities
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportCloudCarModelSomeFieldsInLocal(List<CloudCarModelEntity> cloudCarModelEntities, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createCloudCarModelList(cloudCarModelEntities);
        String[] keys = new String[]{"vin", "levelId", "manufacturers", "brand", "brand_no", "series",
                "models", "year", "produced_year", "sales_name", "vehicle_type", "vehicle_size", "emission_standard",
                "induction", "engine_description", "displacement", "transmission_type", "transmission_description",
                "name", "series_no", "generation", "chassis_code", "sales_version", "guiding_price", "listing_year", "listing_month",
                "idling_year", "production_status", "sales_status", "country", "vehicle_attributes", "models_code", "engine_model",
                "cylinder_volume", "fuel_type", "fuel_grade", "horsepower", "power_kw", "power_rpm", "torque_nm", "torque_rpm",
                "cylinder_arrangement", "cylinders", "valves_per_cylinder", "compression_ratio", "fuel_injection",
                "combined_fuel_consumption", "urban_fuel_consumption", "suburb_fuel_consumption", "acceleration",
                "max_speed", "engine_knowhow", "catalyst", "cooling_method", "bore", "stroke", "gear_number",
                "front_brake", "rear_brake", "front_suspension", "rear_suspension", "steering", "power_steering",
                "min_ground_clearance", "min_turning_radius", "access_angle", "departure_angle", "engine_location",
                "drive_mode", "drive_model", "body_type", "length", "width", "height", "wheelbase", "front_track", "rear_track",
                "curb_weight", "max_loading", "fuel_tank_capacity", "luggage_place", "roof_type", "calash", "doors", "seats",
                "front_tyre", "rear_tyre", "front_rim", "rear_rim", "rims_material", "spare_wheel", "driver_airbag", "passenger_airbag",
                "front_side_airbag", "rear_side_airbag", "front_curtain_airbag", "rear_curtain_airbag", "knee_airbag", "tire_pressure_monitor",
                "run_flat_tyre", "seatbelt_warning_lamp", "isofix", "latch", "engine_antitheft", "central_locking", "remote_control",
                "keyless_entry", "keyless_go", "abs", "ebd", "eba", "asr", "esp", "epb", "hdc", "variable_suspension", "air_suspension",
                "variable_steering_ratio", "blis", "active_brake", "active_steering", "leather_steering_wheel", "height_adjustable_steering_wheel",
                "length_adjustable_steering_wheel", "electric_adjustable_steering_wheel", "multifunction_steering_wheel", "steering_wheel_with_shift",
                "leather_seat", "sport_seat", "height_adjustable_seat", "lumber_support_adjustable", "shoulder_support_adjustable",
                "driver_seat_power_adjustable", "passenger_seat_power_adjustable", "second_row_backrest_adjustable", "second_row_seat_position_adjustable",
                "rear_seat_power_adjustable", "memory_seat", "front_seat_heater", "rear_seat_heater", "seat_ventilation", "seat_massage",
                "overall_rear_seats_fold_down", "rear_seats_proportion_fold_down", "third_row_seat", "front_center_armrest", "rear_center_armrest",
                "rear_cup_holder", "ambientes_lamp", "rear_back_window_glass_blind", "rear_side_window_glass_blind", "sunvisor_mirror",
                "power_tailgate", "sport_body_dress_up_kits", "electric_suction_door", "sunroof", "panoramic_sunroof", "hid_headlamp",
                "led_headlamp", "daytime_running_lamp", "adaptive_headlamp", "corner_headlamp", "front_fog_lamp", "height_adjustable_headlamp",
                "headlamp_washer", "front_power_window", "rear_power_window", "anti_pinch_glass", "insulated_glass", "electrically_adjustable_outside_mirror",
                "heated_outside_mirror", "auto_dimming_inside_mirror", "power_fold_outside_mirror", "inside_mirror_with_memory", "rear_wiper",
                "rain_sensing_wipers", "cruise_control", "parking_assist", "rear_view_camera", "trip_computer", "hud", "gps", "interactive_information_services",
                "lcd_screen", "man_machine_interactive_system", "internal_hard_disk", "bluetooth", "vehicle_tv", "rear_entertainment_screen",
                "entertainment_connector", "MP3", "single_disc_cd", "multi_disc_cd", "virtual_multi_disc_cd", "single_disc_dvd", "multi_disc_dvd", "speaker_number",
                "ac", "autoac", "rearac", "rear_vent", "aczone_control", "pollen_filter", "refrigerator", "automatic_parking", "night_vision", "splitview",
                "acc", "panoramic_camera", "rear_parking_aid", "telematics", "valve_system", "cylinder_head", "cylinder_block", "body_structure", "parking_brake",
                "warranty_period", "vehicle_color", "intelligent_stop_start", "dipped_lights", "high_beam", "maintenance_remark",
                "vinOnetoThree", "vinFour", "vinFive", "vinSix", "vinSeventoEight", "vinNine", "vinTen", "vinEleven", "vinTwelvetoSeventeen"
        };
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createXSSFWorkbook(list, keys, ExcelDatas.cloudCarModelSomeFields);
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
     * 51车宝-记账数据导出
     *
     * @param bills
     * @param workbook
     * @param pathname
     * @throws IOException
     */
    public static void exportWuYiCheBaoAccountDataInLocal(List<Bill> bills, Workbook workbook, String pathname) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.createBillList(bills);
        String[] keys = new String[]{"companyName", "billNo", "stockOutNumber", "payType",
                "accountType", "totalAmount", "dateAdded", "receptionistName", "remark"
        };
        OutputStream outputStream = null;

        try {
            workbook = ExcelUtil.createHSSFWorkbook(list, keys, ExcelDatas.WuYiCheBaoAccountDatas);
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
