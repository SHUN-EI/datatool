package com.ys.datatool.domain;

/**
 * Created by mo on @date  2018-03-22.
 *
 * 云车型
 */
public class CarModelEntity {

    private Long id;

    /**
     * 序号
     */
    private String num;

    /**
     * WIX产品型号
     */
    private String itemCode;

    private String name;

    /**
     * 更新类型, C:创建 U:更新 D:删除
     */
    private String ControlType;

    /**
     * 力洋编号(LevelId)
     */
    private String LevelId;

    /**
     * 保养手册备注
     */
    private String maintenanceRemark;

    /**
     * 厂家
     */
    private String Manufacturers;

    /**
     * 品牌
     */
    private String Brand;

    /**
     * 厂家品牌编号
     */
    private String BrandNo;

    /**
     * 车系
     */
    private String Series;

    /**
     * 车系编号
     */
    private String SeriesNo;

    /**
     * 车型
     */
    private String Models;

    /**
     * 代数
     */
    private String Generation;

    /**
     * 底盘号
     */
    private String ChassisCode;

    /**
     * 销售名称
     */
    private String SalesName;

    /**
     * 销售版本
     */
    private String SalesVersion;

    /**
     * 年款
     */
    private String Year;

    /**
     * 排放标准
     */
    private String EmissionStandard;

    /**
     * 车辆类型
     */
    private String VehicleType;

    /**
     * 车辆级别
     */
    private String VehicleSize;

    /**
     * 指导价格
     */
    private String GuidingPrice;

    /**
     * 上市年份
     */
    private String ListingYear;

    /**
     * 上市月份
     */
    private String ListingMonth;

    /**
     * 生产年份
     */
    private String ProducedYear;

    /**
     * 停产年份
     */
    private String IdlingYear;

    /**
     * 生产状态
     */
    private String ProductionStatus;

    /**
     * 销售状态
     */
    private String SalesStatus;

    /**
     * 国别
     */
    private String Country;

    /**
     * 国产合资进口
     * 制造方式
     */
    private String VehicleAttributes;

    /**
     * 车型代码
     */
    private String ModelCode;

    /**
     * 发动机型号
     */
    private String EngineModel;

    /**
     * 气缸容量
     */
    private String CylinderVolume;

    /**
     * 排量
     */
    private String Displacement;

    /**
     * 进气形式
     */
    private String Induction;

    /**
     * 燃油类型
     */
    private String FuelType;

    /**
     * 燃油标号
     */
    private String FuelGrade;

    /**
     * 最大马力(PS)
     */
    private String Horsepower;

    /**
     * 最大功率(kW)
     */
    private String PowerKw;

    /**
     * 最大功率转速(rpm)
     */
    private String PowerRpm;

    /**
     * 最大扭矩(N·m)
     */
    private String TorqueNm;

    /**
     * 最大功率转速(rpm)
     */
    private String TorqueRpm;

    /**
     * 气缸排列形式
     */
    private String CylinderArrangement;

    /**
     * 气缸数(个)
     */
    private String Cylinders;

    /**
     * 每缸气门数(个)
     */
    private String ValvesPerCylinder;

    /**
     * 压缩比
     */
    private String CompressionRatio;

    /**
     * 供油方式
     */
    private String FuelInjection;

    /**
     * 综合工况油耗
     */
    private String CombinedFuelConsumption;

    /**
     * 市区工况油耗
     */
    private String UrbanFuelConsumption;

    /**
     * 郊区工况油耗
     */
    private String SuburbFuelConsumption;

    /**
     * 加速时间(s)
     */
    private String Acceleration;

    /**
     * 最高车速(km/h)
     */
    private String MaxSpeed;

    /**
     * 发动机特有技术
     */
    private String EngineKnowhow;

    /**
     * 三元催化器
     */
    private String Catalyst;

    /**
     * 冷却方式
     */
    private String CoolingMethod;

    /**
     * 缸径
     */
    private String Bore;

    /**
     * 冲程
     */
    private String Stroke;

    /**
     * 发动机描述
     */
    private String EngineDescription;

    /**
     * 变速箱类型
     */
    private String TransmissionType;

    /**
     * 变速器描述
     */
    private String TransmissionDescription;

    /**
     * 档位数
     */
    private String GearNumber;

    /**
     * 前制动器类型
     */
    private String FrontBrake;

    /**
     * 后制动器类型
     */
    private String RearBrake;

    /**
     * 前悬挂类型
     */
    private String FrontSuspension;

    /**
     * 后悬挂类型
     */
    private String RearSuspension;

    /**
     * 转向机形式
     */
    private String Steering;

    /**
     * 助力类型
     */
    private String PowerSteering;

    /**
     * 最小离地间隙(mm)
     */
    private String MinGroundClearance;

    /**
     * 最小转弯半径
     */
    private String MinTurningRadius;

    /**
     * 离去角
     */
    private String AccessAngle;

    /**
     * 接近角
     */
    private String DepartureAngle;

    /**
     * 发动机位置
     */
    private String EngineLocation;

    /**
     * 驱动方式
     */
    private String DriveMode;

    /**
     * 驱动形式
     */
    private String DriveModel;

    /**
     * 车身型式
     */
    private String BodyType;

    /**
     * 长(mm)
     */
    private String Length;

    /**
     * 宽(mm)
     */
    private String Width;

    /**
     * 高(mm)
     */
    private String Height;

    /**
     * 轴距(mm)
     */
    private String Wheelbase;

    /**
     * 前轮距(mm)
     */
    private String FrontTrack;

    /**
     * 后轮距(mm)
     */
    private String RearTrack;

    /**
     * 整备质量(kg)
     */
    private String CurbWeight;

    /**
     * 最大载重质量(kg)
     */
    private String MaxLoading;

    /**
     * 油箱容积(L)
     */
    private String FuelTankCapacity;

    /**
     * 行李厢容积(L)
     */
    private String LuggagePlace;

    /**
     * 车顶形式
     */
    private String RoofType;

    /**
     * 车篷型式
     */
    private String Calash;

    /**
     * 车门数
     */
    private String Doors;

    /**
     * 座位数(个)
     */
    private String Seats;

    /**
     * 前轮胎规格
     */
    private String FrontTyre;

    /**
     * 后轮胎规格
     */
    private String RearTyre;

    /**
     * 前轮毂规格
     */
    private String FrontRim;

    /**
     * 后轮毂规格
     */
    private String RearRim;

    /**
     * 轮毂材料
     */
    private String RimsMaterial;

    /**
     * 备胎规格
     */
    private String SpareWheel;

    /**
     * 驾驶座安全气囊
     */
    private String DriverAirbag;

    /**
     * 副驾驶安全气囊
     */
    private String PassengerAirbag;

    /**
     * 前排侧气囊;
     */
    private String FrontSideAirbag;

    /**
     * 后排侧气囊;
     */
    private String RearSideAirbag;

    /**
     * 前排头部气囊(气帘);
     */
    private String FrontCurtainAirbag;

    /**
     * 后排头部气囊(气帘);
     */
    private String RearCurtainAirbag;

    /**
     * 膝部气囊;
     */
    private String KneeAirbag;

    /**
     * 胎压监测装置;
     */
    private String TirePressureMonitor;

    /**
     * 零胎压继续行驶;
     */
    private String RunFlatTyre;

    /**
     * 安全带未系提示;
     */
    private String SeatbeltWarningLamp;

    /**
     * ISOFIX儿童座椅接口;
     */
    private String ISOFIX;

    /**
     * LATCH座椅接口;
     */
    private String LATCH;

    /**
     * 发动机电子防盗;
     */
    private String EngineAntitheft;

    /**
     * 中控锁;
     */
    private String CentralLocking;

    /**
     * 遥控钥匙;
     */
    private String RemoteControl;

    /**
     * 无钥匙进入系统;
     */
    private String KeylessEntry;

    /**
     * 无钥匙启动系统;
     */
    private String KeylessGo;

    /**
     * ABS防抱死;
     */
    private String ABS;

    /**
     * 制动力分配(EBD/CBC等);
     */
    private String EBD;

    /**
     * 刹车辅助(EBA/BAS/BA等);
     */
    private String EBA;

    /**
     * 牵引力控制(ASR/TCS/TRC等);
     */
    private String ASR;

    /**
     * 车身稳定控制(ESP/DSC/VSC等);
     */
    private String ESP;

    /**
     * 自动驻车/上坡辅助;
     */
    private String EPB;

    /**
     * 陡坡缓降;
     */
    private String HDC;

    /**
     * 可变悬挂;
     */
    private String VariableSuspension;

    /**
     * 空气悬挂;
     */
    private String AirSuspension;

    /**
     * 可变转向比;
     */
    private String VariableSteeringRatio;

    /**
     * 并线辅助;
     */
    private String BLIS;

    /**
     * 主动刹车;
     */
    private String ActiveBrake;

    /**
     * 主动转向系统;
     */
    private String ActiveSteering;

    /**
     * 真皮方向盘;
     */
    private String LeatherSteeringWheel;

    /**
     * 方向盘上下调节;
     */
    private String HeightAdjustableSteeringWheel;

    /**
     * 方向盘前后调节;
     */
    private String LengthAdjustableSteeringWheel;

    /**
     * 方向盘电动调节;
     */
    private String ElectricAdjustableSteeringWheel;

    /**
     * 多功能方向盘;
     */
    private String MultifunctionSteeringWheel;

    /**
     * 方向盘换挡;
     */
    private String SteeringWheelWithShift;

    /**
     * 真皮座椅;
     */
    private String LeatherSeat;

    /**
     * 运动座椅;
     */
    private String SportSeat;

    /**
     * 座椅高低调节;
     */
    private String HeightAdjustableSeat;

    /**
     * 腰部支撑调节;
     */
    private String LumberSupportAdjustable;

    /**
     * 肩部支撑调节;
     */
    private String ShoulderSupportAdjustable;

    /**
     * 驾驶座座椅电动调节;
     */
    private String DriverSeatPowerAdjustable;

    /**
     * 副驾驶座座椅电动调节;
     */
    private String PassengerSeatPowerAdjustable;

    /**
     * 第二排靠背角度调节;
     */
    private String SecondRowBackrestAdjustable;

    /**
     * 第二排座椅移动;
     */
    private String SecondRowSeatPositionAdjustable;

    /**
     * 后排座椅电动调节;
     */
    private String RearSeatPowerAdjustable;

    /**
     * 电动座椅记忆;
     */
    private String MemorySeat;

    /**
     * 前排座椅加热;
     */
    private String FrontSeatHeater;

    /**
     * 后排座椅加热;
     */
    private String RearSeatHeater;

    /**
     * 座椅通风;
     */
    private String SeatVentilation;

    /**
     * 座椅按摩;
     */
    private String SeatMassage;

    /**
     * 后排座椅整体放倒;
     */
    private String OverallRearSeatsFoldDown;

    /**
     * 后排座椅比例放倒;
     */
    private String RearSeatsProportionFoldDown;

    /**
     * 第三排座椅;
     */
    private String ThirdRowSeat;

    /**
     * 前座中央扶手;
     */
    private String FrontCenterArmrest;

    /**
     * 后座中央扶手;
     */
    private String RearCenterArmrest;

    /**
     * 后排杯架;
     */
    private String RearCupHolder;

    /**
     * 车内氛围灯;
     */
    private String AmbientesLamp;

    /**
     * 后风挡遮阳帘;
     */
    private String RearBackWindowGlassBlind;

    /**
     * 后排侧遮阳帘;
     */
    private String RearSideWindowGlassBlind;

    /**
     * 遮阳板化妆镜;
     */
    private String SunvisorMirror;

    /**
     * 电动后备箱;
     */
    private String PowerTailgate;

    /**
     * 运动外观套件;
     */
    private String SportBodyDressUpKits;

    /**
     * 电动吸合门;
     */
    private String ElectricSuctionDoor;

    /**
     * 电动天窗;
     */
    private String Sunroof;

    /**
     * 全景天窗;
     */
    private String PanoramicSunroof;

    /**
     * 氙气大灯;
     */
    private String HidHeadlamp;

    /**
     * LED大灯;
     */
    private String LedHeadlamp;

    /**
     * 日间行车灯;
     */
    private String DaytimeRunningLamp;

    /**
     * 自动头灯;
     */
    private String AdaptiveHeadlamp;

    /**
     * 转向头灯;
     */
    private String CornerHeadlamp;

    /**
     * 前雾灯;
     */
    private String FrontFogLamp;

    /**
     * 大灯高度可调;
     */
    private String HeightAdjustableHeadlamp;

    /**
     * 大灯清洗装置;
     */
    private String HeadlampWasher;

    /**
     * 前电动车窗;
     */
    private String FrontPowerWindow;

    /**
     * 后电动车窗;
     */
    private String RearPowerWindow;

    /**
     * 车窗防夹手功能;
     */
    private String AntiPinchGlass;

    /**
     * 隔热玻璃;
     */
    private String InsulatedGlass;

    /**
     * 后视镜电动调节;
     */
    private String ElectricallyAdjustableOutsideMirror;

    /**
     * 后视镜加热;
     */
    private String HeatedOutsideMirror;

    /**
     * 后视镜自动防眩目;
     */
    private String AutoDimmingInsideMirror;

    /**
     * 后视镜电动折叠;
     */
    private String PowerFoldOutsideMirror;

    /**
     * 后视镜记忆;
     */
    private String InsideMirrorWithMemory;

    /**
     * 后雨刷;
     */
    private String RearWiper;

    /**
     * 感应雨刷;
     */
    private String RainSensingWipers;

    /**
     * 定速巡航;
     */
    private String CruiseControl;

    /**
     * 泊车辅助;
     */
    private String ParkingAssist;

    /**
     * 倒车视频影像;
     */
    private String RearViewCamera;

    /**
     * 行车电脑显示屏;
     */
    private String TripComputer;

    /**
     * HUD抬头数字显示;
     */
    private String HUD;

    /**
     * GPS导航;
     */
    private String GPS;

    /**
     * 定位互动服务;
     */
    private String InteractiveInformationServices;

    /**
     * 中控台彩色大屏;
     */
    private String LcdScreen;

    /**
     * 人机交互系统;
     */
    private String ManMachineInteractiveSystem;

    /**
     * 内置硬盘;
     */
    private String InternalHardDisk;

    /**
     * 蓝牙/车载电话;
     */
    private String Bluetooth;

    /**
     * 车载电视;
     */
    private String VehicleTv;

    /**
     * 后排液晶屏;
     */
    private String RearEntertainmentScreen;

    /**
     * 外接音源接口(AUX/USB/iPod等);
     */
    private String EntertainmentConnector;

    /**
     * 音频支持MP3;
     */
    private String MP3;

    /**
     * 单碟CD;
     */
    private String SingleDiscCd;

    /**
     * 多碟CD;
     */
    private String MultiDiscCd;

    /**
     * 虚拟多碟CD;
     */
    private String VirtualMultiDiscCd;

    /**
     * 单碟DVD;
     */
    private String SingleDiscDvd;

    /**
     * 多碟DVD;
     */
    private String MultiDiscDvd;

    /**
     * 扬声器数量
     */
    private String SpeakerNumber;

    /**
     * 空调;
     */
    private String AC;

    /**
     * 自动空调;
     */
    private String AutoAC;

    /**
     * 后排独立空调;
     */
    private String RearAC;

    /**
     * 后座出风口;
     */
    private String RearVent;
    ;

    /**
     * 温度分区控制;
     */
    private String ACZoneControl;

    /**
     * 空气调节/花粉过滤;
     */
    private String PollenFilter;

    /**
     * 车载冰箱;
     */
    private String Refrigerator;

    /**
     * 自动泊车入位;
     */
    private String AutomaticParking;

    /**
     * 夜视系统;
     */
    private String NightVision;

    /**
     * 中控液晶屏分屏显示;
     */
    private String Splitview;

    /**
     * 自适应巡航;
     */
    private String ACC;

    /**
     * 全景摄像头;
     */
    private String PanoramicCamera;

    /**
     * 倒车雷达;
     */
    private String RearParkingAid;

    /**
     * 车载信息服务;
     */
    private String Telematics;

    /**
     * 配气机构
     */
    private String ValveSystem;

    /**
     * 缸盖材料
     */
    private String CylinderHead;

    /**
     * 缸体材料
     */
    private String CylinderBlock;

    /**
     * 车体结构
     */
    private String BodyStructure;

    /**
     * 驻车制动类型
     */
    private String ParkingBrake;

    /**
     * 整车质保
     */
    private String WarrantyPeriod;

    /**
     * 车身颜色
     */
    private String VehicleColor;

    /**
     * 发动机启停技术
     */
    private String IntelligentStopStart;

    /**
     * 近光类型
     */
    private String DippedLights;

    /**
     * 远光类型
     */
    private String HighBeam;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getControlType() {
        return ControlType;
    }

    public void setControlType(String controlType) {
        ControlType = controlType;
    }

    public String getLevelId() {
        return LevelId;
    }

    public void setLevelId(String levelId) {
        LevelId = levelId;
    }

    public String getMaintenanceRemark() {
        return maintenanceRemark;
    }

    public void setMaintenanceRemark(String maintenanceRemark) {
        this.maintenanceRemark = maintenanceRemark;
    }

    public String getManufacturers() {
        return Manufacturers;
    }

    public void setManufacturers(String manufacturers) {
        Manufacturers = manufacturers;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getBrandNo() {
        return BrandNo;
    }

    public void setBrandNo(String brandNo) {
        BrandNo = brandNo;
    }

    public String getSeries() {
        return Series;
    }

    public void setSeries(String series) {
        Series = series;
    }

    public String getSeriesNo() {
        return SeriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        SeriesNo = seriesNo;
    }

    public String getModels() {
        return Models;
    }

    public void setModels(String models) {
        Models = models;
    }

    public String getGeneration() {
        return Generation;
    }

    public void setGeneration(String generation) {
        Generation = generation;
    }

    public String getChassisCode() {
        return ChassisCode;
    }

    public void setChassisCode(String chassisCode) {
        ChassisCode = chassisCode;
    }

    public String getSalesName() {
        return SalesName;
    }

    public void setSalesName(String salesName) {
        SalesName = salesName;
    }

    public String getSalesVersion() {
        return SalesVersion;
    }

    public void setSalesVersion(String salesVersion) {
        SalesVersion = salesVersion;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getEmissionStandard() {
        return EmissionStandard;
    }

    public void setEmissionStandard(String emissionStandard) {
        EmissionStandard = emissionStandard;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getVehicleSize() {
        return VehicleSize;
    }

    public void setVehicleSize(String vehicleSize) {
        VehicleSize = vehicleSize;
    }

    public String getGuidingPrice() {
        return GuidingPrice;
    }

    public void setGuidingPrice(String guidingPrice) {
        GuidingPrice = guidingPrice;
    }

    public String getListingYear() {
        return ListingYear;
    }

    public void setListingYear(String listingYear) {
        ListingYear = listingYear;
    }

    public String getListingMonth() {
        return ListingMonth;
    }

    public void setListingMonth(String listingMonth) {
        ListingMonth = listingMonth;
    }

    public String getProducedYear() {
        return ProducedYear;
    }

    public void setProducedYear(String producedYear) {
        ProducedYear = producedYear;
    }

    public String getIdlingYear() {
        return IdlingYear;
    }

    public void setIdlingYear(String idlingYear) {
        IdlingYear = idlingYear;
    }

    public String getProductionStatus() {
        return ProductionStatus;
    }

    public void setProductionStatus(String productionStatus) {
        ProductionStatus = productionStatus;
    }

    public String getSalesStatus() {
        return SalesStatus;
    }

    public void setSalesStatus(String salesStatus) {
        SalesStatus = salesStatus;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getVehicleAttributes() {
        return VehicleAttributes;
    }

    public void setVehicleAttributes(String vehicleAttributes) {
        VehicleAttributes = vehicleAttributes;
    }

    public String getModelCode() {
        return ModelCode;
    }

    public void setModelCode(String modelCode) {
        ModelCode = modelCode;
    }

    public String getEngineModel() {
        return EngineModel;
    }

    public void setEngineModel(String engineModel) {
        EngineModel = engineModel;
    }

    public String getCylinderVolume() {
        return CylinderVolume;
    }

    public void setCylinderVolume(String cylinderVolume) {
        CylinderVolume = cylinderVolume;
    }

    public String getDisplacement() {
        return Displacement;
    }

    public void setDisplacement(String displacement) {
        Displacement = displacement;
    }

    public String getInduction() {
        return Induction;
    }

    public void setInduction(String induction) {
        Induction = induction;
    }

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String fuelType) {
        FuelType = fuelType;
    }

    public String getFuelGrade() {
        return FuelGrade;
    }

    public void setFuelGrade(String fuelGrade) {
        FuelGrade = fuelGrade;
    }

    public String getHorsepower() {
        return Horsepower;
    }

    public void setHorsepower(String horsepower) {
        Horsepower = horsepower;
    }

    public String getPowerKw() {
        return PowerKw;
    }

    public void setPowerKw(String powerKw) {
        PowerKw = powerKw;
    }

    public String getPowerRpm() {
        return PowerRpm;
    }

    public void setPowerRpm(String powerRpm) {
        PowerRpm = powerRpm;
    }

    public String getTorqueNm() {
        return TorqueNm;
    }

    public void setTorqueNm(String torqueNm) {
        TorqueNm = torqueNm;
    }

    public String getTorqueRpm() {
        return TorqueRpm;
    }

    public void setTorqueRpm(String torqueRpm) {
        TorqueRpm = torqueRpm;
    }

    public String getCylinderArrangement() {
        return CylinderArrangement;
    }

    public void setCylinderArrangement(String cylinderArrangement) {
        CylinderArrangement = cylinderArrangement;
    }

    public String getCylinders() {
        return Cylinders;
    }

    public void setCylinders(String cylinders) {
        Cylinders = cylinders;
    }

    public String getValvesPerCylinder() {
        return ValvesPerCylinder;
    }

    public void setValvesPerCylinder(String valvesPerCylinder) {
        ValvesPerCylinder = valvesPerCylinder;
    }

    public String getCompressionRatio() {
        return CompressionRatio;
    }

    public void setCompressionRatio(String compressionRatio) {
        CompressionRatio = compressionRatio;
    }

    public String getFuelInjection() {
        return FuelInjection;
    }

    public void setFuelInjection(String fuelInjection) {
        FuelInjection = fuelInjection;
    }

    public String getCombinedFuelConsumption() {
        return CombinedFuelConsumption;
    }

    public void setCombinedFuelConsumption(String combinedFuelConsumption) {
        CombinedFuelConsumption = combinedFuelConsumption;
    }

    public String getUrbanFuelConsumption() {
        return UrbanFuelConsumption;
    }

    public void setUrbanFuelConsumption(String urbanFuelConsumption) {
        UrbanFuelConsumption = urbanFuelConsumption;
    }

    public String getSuburbFuelConsumption() {
        return SuburbFuelConsumption;
    }

    public void setSuburbFuelConsumption(String suburbFuelConsumption) {
        SuburbFuelConsumption = suburbFuelConsumption;
    }

    public String getAcceleration() {
        return Acceleration;
    }

    public void setAcceleration(String acceleration) {
        Acceleration = acceleration;
    }

    public String getMaxSpeed() {
        return MaxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        MaxSpeed = maxSpeed;
    }

    public String getEngineKnowhow() {
        return EngineKnowhow;
    }

    public void setEngineKnowhow(String engineKnowhow) {
        EngineKnowhow = engineKnowhow;
    }

    public String getCatalyst() {
        return Catalyst;
    }

    public void setCatalyst(String catalyst) {
        Catalyst = catalyst;
    }

    public String getCoolingMethod() {
        return CoolingMethod;
    }

    public void setCoolingMethod(String coolingMethod) {
        CoolingMethod = coolingMethod;
    }

    public String getBore() {
        return Bore;
    }

    public void setBore(String bore) {
        Bore = bore;
    }

    public String getStroke() {
        return Stroke;
    }

    public void setStroke(String stroke) {
        Stroke = stroke;
    }

    public String getEngineDescription() {
        return EngineDescription;
    }

    public void setEngineDescription(String engineDescription) {
        EngineDescription = engineDescription;
    }

    public String getTransmissionType() {
        return TransmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        TransmissionType = transmissionType;
    }

    public String getTransmissionDescription() {
        return TransmissionDescription;
    }

    public void setTransmissionDescription(String transmissionDescription) {
        TransmissionDescription = transmissionDescription;
    }

    public String getGearNumber() {
        return GearNumber;
    }

    public void setGearNumber(String gearNumber) {
        GearNumber = gearNumber;
    }

    public String getFrontBrake() {
        return FrontBrake;
    }

    public void setFrontBrake(String frontBrake) {
        FrontBrake = frontBrake;
    }

    public String getRearBrake() {
        return RearBrake;
    }

    public void setRearBrake(String rearBrake) {
        RearBrake = rearBrake;
    }

    public String getFrontSuspension() {
        return FrontSuspension;
    }

    public void setFrontSuspension(String frontSuspension) {
        FrontSuspension = frontSuspension;
    }

    public String getRearSuspension() {
        return RearSuspension;
    }

    public void setRearSuspension(String rearSuspension) {
        RearSuspension = rearSuspension;
    }

    public String getSteering() {
        return Steering;
    }

    public void setSteering(String steering) {
        Steering = steering;
    }

    public String getPowerSteering() {
        return PowerSteering;
    }

    public void setPowerSteering(String powerSteering) {
        PowerSteering = powerSteering;
    }

    public String getMinGroundClearance() {
        return MinGroundClearance;
    }

    public void setMinGroundClearance(String minGroundClearance) {
        MinGroundClearance = minGroundClearance;
    }

    public String getMinTurningRadius() {
        return MinTurningRadius;
    }

    public void setMinTurningRadius(String minTurningRadius) {
        MinTurningRadius = minTurningRadius;
    }

    public String getAccessAngle() {
        return AccessAngle;
    }

    public void setAccessAngle(String accessAngle) {
        AccessAngle = accessAngle;
    }

    public String getDepartureAngle() {
        return DepartureAngle;
    }

    public void setDepartureAngle(String departureAngle) {
        DepartureAngle = departureAngle;
    }

    public String getEngineLocation() {
        return EngineLocation;
    }

    public void setEngineLocation(String engineLocation) {
        EngineLocation = engineLocation;
    }

    public String getDriveMode() {
        return DriveMode;
    }

    public void setDriveMode(String driveMode) {
        DriveMode = driveMode;
    }

    public String getDriveModel() {
        return DriveModel;
    }

    public void setDriveModel(String driveModel) {
        DriveModel = driveModel;
    }

    public String getBodyType() {
        return BodyType;
    }

    public void setBodyType(String bodyType) {
        BodyType = bodyType;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }

    public String getWidth() {
        return Width;
    }

    public void setWidth(String width) {
        Width = width;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWheelbase() {
        return Wheelbase;
    }

    public void setWheelbase(String wheelbase) {
        Wheelbase = wheelbase;
    }

    public String getFrontTrack() {
        return FrontTrack;
    }

    public void setFrontTrack(String frontTrack) {
        FrontTrack = frontTrack;
    }

    public String getRearTrack() {
        return RearTrack;
    }

    public void setRearTrack(String rearTrack) {
        RearTrack = rearTrack;
    }

    public String getCurbWeight() {
        return CurbWeight;
    }

    public void setCurbWeight(String curbWeight) {
        CurbWeight = curbWeight;
    }

    public String getMaxLoading() {
        return MaxLoading;
    }

    public void setMaxLoading(String maxLoading) {
        MaxLoading = maxLoading;
    }

    public String getFuelTankCapacity() {
        return FuelTankCapacity;
    }

    public void setFuelTankCapacity(String fuelTankCapacity) {
        FuelTankCapacity = fuelTankCapacity;
    }

    public String getLuggagePlace() {
        return LuggagePlace;
    }

    public void setLuggagePlace(String luggagePlace) {
        LuggagePlace = luggagePlace;
    }

    public String getRoofType() {
        return RoofType;
    }

    public void setRoofType(String roofType) {
        RoofType = roofType;
    }

    public String getCalash() {
        return Calash;
    }

    public void setCalash(String calash) {
        Calash = calash;
    }

    public String getDoors() {
        return Doors;
    }

    public void setDoors(String doors) {
        Doors = doors;
    }

    public String getSeats() {
        return Seats;
    }

    public void setSeats(String seats) {
        Seats = seats;
    }

    public String getFrontTyre() {
        return FrontTyre;
    }

    public void setFrontTyre(String frontTyre) {
        FrontTyre = frontTyre;
    }

    public String getRearTyre() {
        return RearTyre;
    }

    public void setRearTyre(String rearTyre) {
        RearTyre = rearTyre;
    }

    public String getFrontRim() {
        return FrontRim;
    }

    public void setFrontRim(String frontRim) {
        FrontRim = frontRim;
    }

    public String getRearRim() {
        return RearRim;
    }

    public void setRearRim(String rearRim) {
        RearRim = rearRim;
    }

    public String getRimsMaterial() {
        return RimsMaterial;
    }

    public void setRimsMaterial(String rimsMaterial) {
        RimsMaterial = rimsMaterial;
    }

    public String getSpareWheel() {
        return SpareWheel;
    }

    public void setSpareWheel(String spareWheel) {
        SpareWheel = spareWheel;
    }

    public String getDriverAirbag() {
        return DriverAirbag;
    }

    public void setDriverAirbag(String driverAirbag) {
        DriverAirbag = driverAirbag;
    }

    public String getPassengerAirbag() {
        return PassengerAirbag;
    }

    public void setPassengerAirbag(String passengerAirbag) {
        PassengerAirbag = passengerAirbag;
    }

    public String getFrontSideAirbag() {
        return FrontSideAirbag;
    }

    public void setFrontSideAirbag(String frontSideAirbag) {
        FrontSideAirbag = frontSideAirbag;
    }

    public String getRearSideAirbag() {
        return RearSideAirbag;
    }

    public void setRearSideAirbag(String rearSideAirbag) {
        RearSideAirbag = rearSideAirbag;
    }

    public String getFrontCurtainAirbag() {
        return FrontCurtainAirbag;
    }

    public void setFrontCurtainAirbag(String frontCurtainAirbag) {
        FrontCurtainAirbag = frontCurtainAirbag;
    }

    public String getRearCurtainAirbag() {
        return RearCurtainAirbag;
    }

    public void setRearCurtainAirbag(String rearCurtainAirbag) {
        RearCurtainAirbag = rearCurtainAirbag;
    }

    public String getKneeAirbag() {
        return KneeAirbag;
    }

    public void setKneeAirbag(String kneeAirbag) {
        KneeAirbag = kneeAirbag;
    }

    public String getTirePressureMonitor() {
        return TirePressureMonitor;
    }

    public void setTirePressureMonitor(String tirePressureMonitor) {
        TirePressureMonitor = tirePressureMonitor;
    }

    public String getRunFlatTyre() {
        return RunFlatTyre;
    }

    public void setRunFlatTyre(String runFlatTyre) {
        RunFlatTyre = runFlatTyre;
    }

    public String getSeatbeltWarningLamp() {
        return SeatbeltWarningLamp;
    }

    public void setSeatbeltWarningLamp(String seatbeltWarningLamp) {
        SeatbeltWarningLamp = seatbeltWarningLamp;
    }

    public String getISOFIX() {
        return ISOFIX;
    }

    public void setISOFIX(String ISOFIX) {
        this.ISOFIX = ISOFIX;
    }

    public String getLATCH() {
        return LATCH;
    }

    public void setLATCH(String LATCH) {
        this.LATCH = LATCH;
    }

    public String getEngineAntitheft() {
        return EngineAntitheft;
    }

    public void setEngineAntitheft(String engineAntitheft) {
        EngineAntitheft = engineAntitheft;
    }

    public String getCentralLocking() {
        return CentralLocking;
    }

    public void setCentralLocking(String centralLocking) {
        CentralLocking = centralLocking;
    }

    public String getRemoteControl() {
        return RemoteControl;
    }

    public void setRemoteControl(String remoteControl) {
        RemoteControl = remoteControl;
    }

    public String getKeylessEntry() {
        return KeylessEntry;
    }

    public void setKeylessEntry(String keylessEntry) {
        KeylessEntry = keylessEntry;
    }

    public String getKeylessGo() {
        return KeylessGo;
    }

    public void setKeylessGo(String keylessGo) {
        KeylessGo = keylessGo;
    }

    public String getABS() {
        return ABS;
    }

    public void setABS(String ABS) {
        this.ABS = ABS;
    }

    public String getEBD() {
        return EBD;
    }

    public void setEBD(String EBD) {
        this.EBD = EBD;
    }

    public String getEBA() {
        return EBA;
    }

    public void setEBA(String EBA) {
        this.EBA = EBA;
    }

    public String getASR() {
        return ASR;
    }

    public void setASR(String ASR) {
        this.ASR = ASR;
    }

    public String getESP() {
        return ESP;
    }

    public void setESP(String ESP) {
        this.ESP = ESP;
    }

    public String getEPB() {
        return EPB;
    }

    public void setEPB(String EPB) {
        this.EPB = EPB;
    }

    public String getHDC() {
        return HDC;
    }

    public void setHDC(String HDC) {
        this.HDC = HDC;
    }

    public String getVariableSuspension() {
        return VariableSuspension;
    }

    public void setVariableSuspension(String variableSuspension) {
        VariableSuspension = variableSuspension;
    }

    public String getAirSuspension() {
        return AirSuspension;
    }

    public void setAirSuspension(String airSuspension) {
        AirSuspension = airSuspension;
    }

    public String getVariableSteeringRatio() {
        return VariableSteeringRatio;
    }

    public void setVariableSteeringRatio(String variableSteeringRatio) {
        VariableSteeringRatio = variableSteeringRatio;
    }

    public String getBLIS() {
        return BLIS;
    }

    public void setBLIS(String BLIS) {
        this.BLIS = BLIS;
    }

    public String getActiveBrake() {
        return ActiveBrake;
    }

    public void setActiveBrake(String activeBrake) {
        ActiveBrake = activeBrake;
    }

    public String getActiveSteering() {
        return ActiveSteering;
    }

    public void setActiveSteering(String activeSteering) {
        ActiveSteering = activeSteering;
    }

    public String getLeatherSteeringWheel() {
        return LeatherSteeringWheel;
    }

    public void setLeatherSteeringWheel(String leatherSteeringWheel) {
        LeatherSteeringWheel = leatherSteeringWheel;
    }

    public String getHeightAdjustableSteeringWheel() {
        return HeightAdjustableSteeringWheel;
    }

    public void setHeightAdjustableSteeringWheel(String heightAdjustableSteeringWheel) {
        HeightAdjustableSteeringWheel = heightAdjustableSteeringWheel;
    }

    public String getLengthAdjustableSteeringWheel() {
        return LengthAdjustableSteeringWheel;
    }

    public void setLengthAdjustableSteeringWheel(String lengthAdjustableSteeringWheel) {
        LengthAdjustableSteeringWheel = lengthAdjustableSteeringWheel;
    }

    public String getElectricAdjustableSteeringWheel() {
        return ElectricAdjustableSteeringWheel;
    }

    public void setElectricAdjustableSteeringWheel(String electricAdjustableSteeringWheel) {
        ElectricAdjustableSteeringWheel = electricAdjustableSteeringWheel;
    }

    public String getMultifunctionSteeringWheel() {
        return MultifunctionSteeringWheel;
    }

    public void setMultifunctionSteeringWheel(String multifunctionSteeringWheel) {
        MultifunctionSteeringWheel = multifunctionSteeringWheel;
    }

    public String getSteeringWheelWithShift() {
        return SteeringWheelWithShift;
    }

    public void setSteeringWheelWithShift(String steeringWheelWithShift) {
        SteeringWheelWithShift = steeringWheelWithShift;
    }

    public String getLeatherSeat() {
        return LeatherSeat;
    }

    public void setLeatherSeat(String leatherSeat) {
        LeatherSeat = leatherSeat;
    }

    public String getSportSeat() {
        return SportSeat;
    }

    public void setSportSeat(String sportSeat) {
        SportSeat = sportSeat;
    }

    public String getHeightAdjustableSeat() {
        return HeightAdjustableSeat;
    }

    public void setHeightAdjustableSeat(String heightAdjustableSeat) {
        HeightAdjustableSeat = heightAdjustableSeat;
    }

    public String getLumberSupportAdjustable() {
        return LumberSupportAdjustable;
    }

    public void setLumberSupportAdjustable(String lumberSupportAdjustable) {
        LumberSupportAdjustable = lumberSupportAdjustable;
    }

    public String getShoulderSupportAdjustable() {
        return ShoulderSupportAdjustable;
    }

    public void setShoulderSupportAdjustable(String shoulderSupportAdjustable) {
        ShoulderSupportAdjustable = shoulderSupportAdjustable;
    }

    public String getDriverSeatPowerAdjustable() {
        return DriverSeatPowerAdjustable;
    }

    public void setDriverSeatPowerAdjustable(String driverSeatPowerAdjustable) {
        DriverSeatPowerAdjustable = driverSeatPowerAdjustable;
    }

    public String getPassengerSeatPowerAdjustable() {
        return PassengerSeatPowerAdjustable;
    }

    public void setPassengerSeatPowerAdjustable(String passengerSeatPowerAdjustable) {
        PassengerSeatPowerAdjustable = passengerSeatPowerAdjustable;
    }

    public String getSecondRowBackrestAdjustable() {
        return SecondRowBackrestAdjustable;
    }

    public void setSecondRowBackrestAdjustable(String secondRowBackrestAdjustable) {
        SecondRowBackrestAdjustable = secondRowBackrestAdjustable;
    }

    public String getSecondRowSeatPositionAdjustable() {
        return SecondRowSeatPositionAdjustable;
    }

    public void setSecondRowSeatPositionAdjustable(String secondRowSeatPositionAdjustable) {
        SecondRowSeatPositionAdjustable = secondRowSeatPositionAdjustable;
    }

    public String getRearSeatPowerAdjustable() {
        return RearSeatPowerAdjustable;
    }

    public void setRearSeatPowerAdjustable(String rearSeatPowerAdjustable) {
        RearSeatPowerAdjustable = rearSeatPowerAdjustable;
    }

    public String getMemorySeat() {
        return MemorySeat;
    }

    public void setMemorySeat(String memorySeat) {
        MemorySeat = memorySeat;
    }

    public String getFrontSeatHeater() {
        return FrontSeatHeater;
    }

    public void setFrontSeatHeater(String frontSeatHeater) {
        FrontSeatHeater = frontSeatHeater;
    }

    public String getRearSeatHeater() {
        return RearSeatHeater;
    }

    public void setRearSeatHeater(String rearSeatHeater) {
        RearSeatHeater = rearSeatHeater;
    }

    public String getSeatVentilation() {
        return SeatVentilation;
    }

    public void setSeatVentilation(String seatVentilation) {
        SeatVentilation = seatVentilation;
    }

    public String getSeatMassage() {
        return SeatMassage;
    }

    public void setSeatMassage(String seatMassage) {
        SeatMassage = seatMassage;
    }

    public String getOverallRearSeatsFoldDown() {
        return OverallRearSeatsFoldDown;
    }

    public void setOverallRearSeatsFoldDown(String overallRearSeatsFoldDown) {
        OverallRearSeatsFoldDown = overallRearSeatsFoldDown;
    }

    public String getRearSeatsProportionFoldDown() {
        return RearSeatsProportionFoldDown;
    }

    public void setRearSeatsProportionFoldDown(String rearSeatsProportionFoldDown) {
        RearSeatsProportionFoldDown = rearSeatsProportionFoldDown;
    }

    public String getThirdRowSeat() {
        return ThirdRowSeat;
    }

    public void setThirdRowSeat(String thirdRowSeat) {
        ThirdRowSeat = thirdRowSeat;
    }

    public String getFrontCenterArmrest() {
        return FrontCenterArmrest;
    }

    public void setFrontCenterArmrest(String frontCenterArmrest) {
        FrontCenterArmrest = frontCenterArmrest;
    }

    public String getRearCenterArmrest() {
        return RearCenterArmrest;
    }

    public void setRearCenterArmrest(String rearCenterArmrest) {
        RearCenterArmrest = rearCenterArmrest;
    }

    public String getRearCupHolder() {
        return RearCupHolder;
    }

    public void setRearCupHolder(String rearCupHolder) {
        RearCupHolder = rearCupHolder;
    }

    public String getAmbientesLamp() {
        return AmbientesLamp;
    }

    public void setAmbientesLamp(String ambientesLamp) {
        AmbientesLamp = ambientesLamp;
    }

    public String getRearBackWindowGlassBlind() {
        return RearBackWindowGlassBlind;
    }

    public void setRearBackWindowGlassBlind(String rearBackWindowGlassBlind) {
        RearBackWindowGlassBlind = rearBackWindowGlassBlind;
    }

    public String getRearSideWindowGlassBlind() {
        return RearSideWindowGlassBlind;
    }

    public void setRearSideWindowGlassBlind(String rearSideWindowGlassBlind) {
        RearSideWindowGlassBlind = rearSideWindowGlassBlind;
    }

    public String getSunvisorMirror() {
        return SunvisorMirror;
    }

    public void setSunvisorMirror(String sunvisorMirror) {
        SunvisorMirror = sunvisorMirror;
    }

    public String getPowerTailgate() {
        return PowerTailgate;
    }

    public void setPowerTailgate(String powerTailgate) {
        PowerTailgate = powerTailgate;
    }

    public String getSportBodyDressUpKits() {
        return SportBodyDressUpKits;
    }

    public void setSportBodyDressUpKits(String sportBodyDressUpKits) {
        SportBodyDressUpKits = sportBodyDressUpKits;
    }

    public String getElectricSuctionDoor() {
        return ElectricSuctionDoor;
    }

    public void setElectricSuctionDoor(String electricSuctionDoor) {
        ElectricSuctionDoor = electricSuctionDoor;
    }

    public String getSunroof() {
        return Sunroof;
    }

    public void setSunroof(String sunroof) {
        Sunroof = sunroof;
    }

    public String getPanoramicSunroof() {
        return PanoramicSunroof;
    }

    public void setPanoramicSunroof(String panoramicSunroof) {
        PanoramicSunroof = panoramicSunroof;
    }

    public String getHidHeadlamp() {
        return HidHeadlamp;
    }

    public void setHidHeadlamp(String hidHeadlamp) {
        HidHeadlamp = hidHeadlamp;
    }

    public String getLedHeadlamp() {
        return LedHeadlamp;
    }

    public void setLedHeadlamp(String ledHeadlamp) {
        LedHeadlamp = ledHeadlamp;
    }

    public String getDaytimeRunningLamp() {
        return DaytimeRunningLamp;
    }

    public void setDaytimeRunningLamp(String daytimeRunningLamp) {
        DaytimeRunningLamp = daytimeRunningLamp;
    }

    public String getAdaptiveHeadlamp() {
        return AdaptiveHeadlamp;
    }

    public void setAdaptiveHeadlamp(String adaptiveHeadlamp) {
        AdaptiveHeadlamp = adaptiveHeadlamp;
    }

    public String getCornerHeadlamp() {
        return CornerHeadlamp;
    }

    public void setCornerHeadlamp(String cornerHeadlamp) {
        CornerHeadlamp = cornerHeadlamp;
    }

    public String getFrontFogLamp() {
        return FrontFogLamp;
    }

    public void setFrontFogLamp(String frontFogLamp) {
        FrontFogLamp = frontFogLamp;
    }

    public String getHeightAdjustableHeadlamp() {
        return HeightAdjustableHeadlamp;
    }

    public void setHeightAdjustableHeadlamp(String heightAdjustableHeadlamp) {
        HeightAdjustableHeadlamp = heightAdjustableHeadlamp;
    }

    public String getHeadlampWasher() {
        return HeadlampWasher;
    }

    public void setHeadlampWasher(String headlampWasher) {
        HeadlampWasher = headlampWasher;
    }

    public String getFrontPowerWindow() {
        return FrontPowerWindow;
    }

    public void setFrontPowerWindow(String frontPowerWindow) {
        FrontPowerWindow = frontPowerWindow;
    }

    public String getRearPowerWindow() {
        return RearPowerWindow;
    }

    public void setRearPowerWindow(String rearPowerWindow) {
        RearPowerWindow = rearPowerWindow;
    }

    public String getAntiPinchGlass() {
        return AntiPinchGlass;
    }

    public void setAntiPinchGlass(String antiPinchGlass) {
        AntiPinchGlass = antiPinchGlass;
    }

    public String getInsulatedGlass() {
        return InsulatedGlass;
    }

    public void setInsulatedGlass(String insulatedGlass) {
        InsulatedGlass = insulatedGlass;
    }

    public String getElectricallyAdjustableOutsideMirror() {
        return ElectricallyAdjustableOutsideMirror;
    }

    public void setElectricallyAdjustableOutsideMirror(String electricallyAdjustableOutsideMirror) {
        ElectricallyAdjustableOutsideMirror = electricallyAdjustableOutsideMirror;
    }

    public String getHeatedOutsideMirror() {
        return HeatedOutsideMirror;
    }

    public void setHeatedOutsideMirror(String heatedOutsideMirror) {
        HeatedOutsideMirror = heatedOutsideMirror;
    }

    public String getAutoDimmingInsideMirror() {
        return AutoDimmingInsideMirror;
    }

    public void setAutoDimmingInsideMirror(String autoDimmingInsideMirror) {
        AutoDimmingInsideMirror = autoDimmingInsideMirror;
    }

    public String getPowerFoldOutsideMirror() {
        return PowerFoldOutsideMirror;
    }

    public void setPowerFoldOutsideMirror(String powerFoldOutsideMirror) {
        PowerFoldOutsideMirror = powerFoldOutsideMirror;
    }

    public String getInsideMirrorWithMemory() {
        return InsideMirrorWithMemory;
    }

    public void setInsideMirrorWithMemory(String insideMirrorWithMemory) {
        InsideMirrorWithMemory = insideMirrorWithMemory;
    }

    public String getRearWiper() {
        return RearWiper;
    }

    public void setRearWiper(String rearWiper) {
        RearWiper = rearWiper;
    }

    public String getRainSensingWipers() {
        return RainSensingWipers;
    }

    public void setRainSensingWipers(String rainSensingWipers) {
        RainSensingWipers = rainSensingWipers;
    }

    public String getCruiseControl() {
        return CruiseControl;
    }

    public void setCruiseControl(String cruiseControl) {
        CruiseControl = cruiseControl;
    }

    public String getParkingAssist() {
        return ParkingAssist;
    }

    public void setParkingAssist(String parkingAssist) {
        ParkingAssist = parkingAssist;
    }

    public String getRearViewCamera() {
        return RearViewCamera;
    }

    public void setRearViewCamera(String rearViewCamera) {
        RearViewCamera = rearViewCamera;
    }

    public String getTripComputer() {
        return TripComputer;
    }

    public void setTripComputer(String tripComputer) {
        TripComputer = tripComputer;
    }

    public String getHUD() {
        return HUD;
    }

    public void setHUD(String HUD) {
        this.HUD = HUD;
    }

    public String getGPS() {
        return GPS;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    public String getInteractiveInformationServices() {
        return InteractiveInformationServices;
    }

    public void setInteractiveInformationServices(String interactiveInformationServices) {
        InteractiveInformationServices = interactiveInformationServices;
    }

    public String getLcdScreen() {
        return LcdScreen;
    }

    public void setLcdScreen(String lcdScreen) {
        LcdScreen = lcdScreen;
    }

    public String getManMachineInteractiveSystem() {
        return ManMachineInteractiveSystem;
    }

    public void setManMachineInteractiveSystem(String manMachineInteractiveSystem) {
        ManMachineInteractiveSystem = manMachineInteractiveSystem;
    }

    public String getInternalHardDisk() {
        return InternalHardDisk;
    }

    public void setInternalHardDisk(String internalHardDisk) {
        InternalHardDisk = internalHardDisk;
    }

    public String getBluetooth() {
        return Bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        Bluetooth = bluetooth;
    }

    public String getVehicleTv() {
        return VehicleTv;
    }

    public void setVehicleTv(String vehicleTv) {
        VehicleTv = vehicleTv;
    }

    public String getRearEntertainmentScreen() {
        return RearEntertainmentScreen;
    }

    public void setRearEntertainmentScreen(String rearEntertainmentScreen) {
        RearEntertainmentScreen = rearEntertainmentScreen;
    }

    public String getEntertainmentConnector() {
        return EntertainmentConnector;
    }

    public void setEntertainmentConnector(String entertainmentConnector) {
        EntertainmentConnector = entertainmentConnector;
    }

    public String getMP3() {
        return MP3;
    }

    public void setMP3(String MP3) {
        this.MP3 = MP3;
    }

    public String getSingleDiscCd() {
        return SingleDiscCd;
    }

    public void setSingleDiscCd(String singleDiscCd) {
        SingleDiscCd = singleDiscCd;
    }

    public String getMultiDiscCd() {
        return MultiDiscCd;
    }

    public void setMultiDiscCd(String multiDiscCd) {
        MultiDiscCd = multiDiscCd;
    }

    public String getVirtualMultiDiscCd() {
        return VirtualMultiDiscCd;
    }

    public void setVirtualMultiDiscCd(String virtualMultiDiscCd) {
        VirtualMultiDiscCd = virtualMultiDiscCd;
    }

    public String getSingleDiscDvd() {
        return SingleDiscDvd;
    }

    public void setSingleDiscDvd(String singleDiscDvd) {
        SingleDiscDvd = singleDiscDvd;
    }

    public String getMultiDiscDvd() {
        return MultiDiscDvd;
    }

    public void setMultiDiscDvd(String multiDiscDvd) {
        MultiDiscDvd = multiDiscDvd;
    }

    public String getSpeakerNumber() {
        return SpeakerNumber;
    }

    public void setSpeakerNumber(String speakerNumber) {
        SpeakerNumber = speakerNumber;
    }

    public String getAC() {
        return AC;
    }

    public void setAC(String AC) {
        this.AC = AC;
    }

    public String getAutoAC() {
        return AutoAC;
    }

    public void setAutoAC(String autoAC) {
        AutoAC = autoAC;
    }

    public String getRearAC() {
        return RearAC;
    }

    public void setRearAC(String rearAC) {
        RearAC = rearAC;
    }

    public String getRearVent() {
        return RearVent;
    }

    public void setRearVent(String rearVent) {
        RearVent = rearVent;
    }

    public String getACZoneControl() {
        return ACZoneControl;
    }

    public void setACZoneControl(String ACZoneControl) {
        this.ACZoneControl = ACZoneControl;
    }

    public String getPollenFilter() {
        return PollenFilter;
    }

    public void setPollenFilter(String pollenFilter) {
        PollenFilter = pollenFilter;
    }

    public String getRefrigerator() {
        return Refrigerator;
    }

    public void setRefrigerator(String refrigerator) {
        Refrigerator = refrigerator;
    }

    public String getAutomaticParking() {
        return AutomaticParking;
    }

    public void setAutomaticParking(String automaticParking) {
        AutomaticParking = automaticParking;
    }

    public String getNightVision() {
        return NightVision;
    }

    public void setNightVision(String nightVision) {
        NightVision = nightVision;
    }

    public String getSplitview() {
        return Splitview;
    }

    public void setSplitview(String splitview) {
        Splitview = splitview;
    }

    public String getACC() {
        return ACC;
    }

    public void setACC(String ACC) {
        this.ACC = ACC;
    }

    public String getPanoramicCamera() {
        return PanoramicCamera;
    }

    public void setPanoramicCamera(String panoramicCamera) {
        PanoramicCamera = panoramicCamera;
    }

    public String getRearParkingAid() {
        return RearParkingAid;
    }

    public void setRearParkingAid(String rearParkingAid) {
        RearParkingAid = rearParkingAid;
    }

    public String getTelematics() {
        return Telematics;
    }

    public void setTelematics(String telematics) {
        Telematics = telematics;
    }

    public String getValveSystem() {
        return ValveSystem;
    }

    public void setValveSystem(String valveSystem) {
        ValveSystem = valveSystem;
    }

    public String getCylinderHead() {
        return CylinderHead;
    }

    public void setCylinderHead(String cylinderHead) {
        CylinderHead = cylinderHead;
    }

    public String getCylinderBlock() {
        return CylinderBlock;
    }

    public void setCylinderBlock(String cylinderBlock) {
        CylinderBlock = cylinderBlock;
    }

    public String getBodyStructure() {
        return BodyStructure;
    }

    public void setBodyStructure(String bodyStructure) {
        BodyStructure = bodyStructure;
    }

    public String getParkingBrake() {
        return ParkingBrake;
    }

    public void setParkingBrake(String parkingBrake) {
        ParkingBrake = parkingBrake;
    }

    public String getWarrantyPeriod() {
        return WarrantyPeriod;
    }

    public void setWarrantyPeriod(String warrantyPeriod) {
        WarrantyPeriod = warrantyPeriod;
    }

    public String getVehicleColor() {
        return VehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        VehicleColor = vehicleColor;
    }

    public String getIntelligentStopStart() {
        return IntelligentStopStart;
    }

    public void setIntelligentStopStart(String intelligentStopStart) {
        IntelligentStopStart = intelligentStopStart;
    }

    public String getDippedLights() {
        return DippedLights;
    }

    public void setDippedLights(String dippedLights) {
        DippedLights = dippedLights;
    }

    public String getHighBeam() {
        return HighBeam;
    }

    public void setHighBeam(String highBeam) {
        HighBeam = highBeam;
    }
}
