package com.ys.datatool.domain.config;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by mo on @date  2018/4/16.
 * <p>
 * excel表头字段
 */
public class ExcelDatas {

    public static Workbook workbook;

    public static String billNoName = "单据号";

    public static String dateEndName = "提车日期";

    public static String carNumberName = "车牌号";

    public static String mileageName = "进店里程";

    public static String serviceItemName = "报价项目";

    public static String goodsName = "报价商品";

    public static String totalAmountName = "单据金额";

    public static String receptionistName = "业务员";

    public static String remarkName = "备注";


    public static String[] cloudCarModelSomeFields = new String[]{"VIN码", "力洋编号(LevelId)", "厂家", "品牌", "厂家品牌编号",
            "车系", "车型", "年款", "生产年份", "销售名称", "车辆类型", "车辆级别", "排放标准", "进气形式",
            "发动机描述", "排量", "变速箱类型", "变速器描述",
            "车型名称", "车系编号", "代数", "底盘号", "销售版本", "指导价格", "上市年份", "上市月份",
            "停产年份", "生产状态", "销售状态", " 国别", "制造方式(国产合资进口)", "车型代码", "发动机型号",
            "气缸容量", "燃油类型", "燃油标号", "最大马力(PS)", "最大功率(kW)", "最大功率转速(rpm)",
            "最大扭矩(N·m)", "最大功率转速(rpm)", "气缸排列形式", " 气缸数(个)", "每缸气门数(个)",
            "压缩比", "供油方式", "综合工况油耗", "市区工况油耗", "郊区工况油耗", "加速时间(s)",
            "最高车速(km/h)", "发动机特有技术", "三元催化器", "冷却方式", "缸径", "冲程",
            "档位数", "前制动器类型", "后制动器类型", "前悬挂类型", "后悬挂类型",
            "转向机形式", "助力类型", "最小离地间隙(mm)", "最小转弯半径", "离去角", "接近角", "发动机位置", "驱动方式",
            "驱动形式", "车身型式", "长(mm)", "宽(mm)", "高(mm)", "轴距(mm)", "前轮距(mm)", "后轮距(mm)",
            "整备质量(kg)", "最大载重质量(kg)", "油箱容积(L)", "行李厢容积(L)", "车顶形式", "车篷型式",
            "车门数", "座位数(个)", "前轮胎规格", "后轮胎规格", "前轮毂规格", "后轮毂规格", "轮毂材料",
            "备胎规格", "驾驶座安全气囊", "副驾驶安全气囊", "前排侧气囊", "后排侧气囊", "前排头部气囊(气帘)",
            "后排头部气囊(气帘)", "膝部气囊", "胎压监测装置", "零胎压继续行驶", "安全带未系提示", "ISOFIX儿童座椅接口",
            "LATCH座椅接口", "发动机电子防盗", "中控锁", "遥控钥匙", "无钥匙进入系统", "无钥匙启动系统",
            "ABS防抱死", "制动力分配(EBD/CBC等)", "刹车辅助(EBA/BAS/BA等)", "牵引力控制(ASR/TCS/TRC等)",
            "车身稳定控制(ESP/DSC/VSC等)", "自动驻车/上坡辅助", "陡坡缓降", "可变悬挂", "空气悬挂",
            "可变转向比", "并线辅助", "主动刹车", "主动转向系统", "真皮方向盘", "方向盘上下调节", "方向盘前后调节",
            "方向盘电动调节", "多功能方向盘", "方向盘换挡", "真皮座椅", "运动座椅", "座椅高低调节", "腰部支撑调节",
            "肩部支撑调节", "驾驶座座椅电动调节", "副驾驶座座椅电动调节", "第二排靠背角度调节", "第二排座椅移动",
            "后排座椅电动调节", "电动座椅记忆", "前排座椅加热", "后排座椅加热", "座椅通风", "座椅按摩",
            "后排座椅整体放倒", "后排座椅比例放倒", "第三排座椅", "前座中央扶手", "后座中央扶手",
            "后排杯架", "车内氛围灯", "后风挡遮阳帘", "后排侧遮阳帘", "遮阳板化妆镜", "电动后备箱",
            "运动外观套件", "电动吸合门", "电动天窗", "全景天窗", "氙气大灯", "LED大灯", "日间行车灯",
            "自动头灯", "转向头灯", "前雾灯", "大灯高度可调", "大灯清洗装置", "前电动车窗", "后电动车窗",
            "车窗防夹手功能", "隔热玻璃", "后视镜电动调节", "后视镜加热", "后视镜自动防眩目", "后视镜电动折叠",
            "后视镜记忆", "后雨刷", "感应雨刷", "定速巡航", "泊车辅助", "倒车视频影像", "行车电脑显示屏", "HUD抬头数字显示",
            "GPS导航", "定位互动服务", "中控台彩色大屏", "人机交互系统", "内置硬盘", "蓝牙/车载电话", "车载电视", "后排液晶屏",
            "外接音源接口(AUX/USB/iPod等)", "音频支持MP3", "单碟CD", "多碟CD", "虚拟多碟CD", "单碟DVD", "多碟DVD", "扬声器数量",
            "空调", "自动空调", "后排独立空调", "后座出风口", "温度分区控制", "空气调节/花粉过滤", "车载冰箱", "自动泊车入位",
            "夜视系统", "中控液晶屏分屏显示", "自适应巡航", "全景摄像头", "倒车雷达", "车载信息服务", "配气机构", "缸盖材料",
            "缸体材料", "车体结构", "驻车制动类型", "整车质保", "车身颜色", "发动机启停技术", "近光类型", "远光类型", "保养手册备注",
            "WIN码1-3位(厂家名称)", "WIN码4位(车身型式)", "WIN码5位(发动机/变速器)", "WIN码6位(乘员保护系统)",
            "WIN码7-8位(车辆等级)", "WIN码9位(检验位)", "WIN码10位(生产年份)", "WIN码11位(装配厂)", "WIN码12-17位(生产顺序号)"
    };

    public static String[] cloudCarModelDatas = new String[]{"VIN码", "levelId", "厂家", "品牌", "厂家品牌编号",
            "车系", "车型", "年款", "生产年份", "销售名称", "车辆类型", "车辆级别", "排放标准", "进气形式",
            "发动机描述", "排量", "变速箱类型", "变速器描述",
            "WIN码1-3位(厂家名称)", "WIN码4位(车身型式)", "WIN码5位(发动机/变速器)", "WIN码6位(乘员保护系统)",
            "WIN码7-8位(车辆等级)", "WIN码9位(检验位)", "WIN码10位(生产年份)", "WIN码11位(装配厂)", "WIN码12-17位(生产顺序号)"

    };

    public static String[] consumptionRecordDatas = new String[]{"车店名称", "单据号",
            "提车日期", "车牌号", "进店里程", "报价项目", "报价商品", "单据金额",
            "业务员", "备注"};

    public static String[] billItemDatas = new String[]{"车店名称", "单据号",
            "商品名称", "数量", "售价", "折扣", "类别", "一级分类名称",
            "二级分类名称", "商品编码"};

    public static String[] billDatas = new String[]{"车店名称", "单据号",
            "车牌号", "里程", "联系手机", "联系人", "单据总价", "单据折扣",
            "实收金额", "是否在店等", "计划完成时间", "支付类型", "备注", "开单时间", "结账时间"};

    /**
     * 车酷客-车辆信息详情相关数据
     */
    public static String[] CheKuKeCarInfoDatas = new String[]{"车店名称", "姓名",
            "车牌号", "品牌", "车型", "手机号", "注册日期", "发动机号码",
            "车架号", "保险日期", "承保公司", "交强险日期", "交强险承保公司",
            "客户备注", "车品牌选择框", "车品牌输入框", "车系选择框", "车系输入框", "车型选择框", "车型输入框"};

    public static String[] billSomeFields = new String[]{"车店名称", "单据号",
            "车牌号", "里程", "联系手机", "联系人", "单据总价", "单据折扣",
            "实收金额", "是否在店等", "计划完成时间", "支付类型", "备注",
            "开单时间", "结账时间","消费项目","业务员","会员卡卡号",
            "总金额","记账金额","已收金额","剩余金额","支付人", "单据名称","单据内容"};

    public static String[] billDetailSomeFields = new String[]{"车店名称", "单据号",
            "商品名称", "数量", "售价(折扣价)", "折扣", "类别", "一级分类名称",
            "二级分类名称", "商品编码", "原价", "车牌", "开单时间",
            "初始数量","已使用数量","剩余数量","本次使用数量"};

    public static String[] memberCardSomeFields = new String[]{"车店名称", "卡号",
            "会员卡名称", "车牌号", "卡类型", "卡品种名称", "开卡日期", "卡内余额",
            "联系人姓名", "联系人手机", "会员等级", "会员折扣", "备注", "会员卡ID", "到期时间","原系统卡号"};

    public static String[] memberCardItemSomeFields = new String[]{"车店名称", "卡号",
            "商品名称", "单价", "折扣", "剩余数量", "初始数量", "商品类别", "项目类别",
            "一级分类名称", "二级分类名称", "到期时间(不填为永久有效)",
            "是否永久有效", "商品编码", "开卡日期", "会员卡名称", "联系人姓名", "联系人手机", "会员卡ID"};

    /**
     * 元乐车宝-库存相关数据
     */
    public static String[] YuanLeCheBaoStockDatas = new String[]{"车店名称", "仓库",
            "商品名称", "配件类型", "品牌", "型号", "规格", "销售价", "库存数量", "入库单价", "仓位", "商品编码"};


    /**
     * 51车宝-记账数据
     */
    public static String[] WuYiCheBaoAccountDatas = new String[]{"车店名称", "记账编号", "出库编号", "收支类型", "记账类型", "金额", "添加时间", "操作人员", "描述"};

    /**
     * 配件
     */
    public static String[] partDatas = new String[]{"车店名称", "零部件号", "零部件名称", "替代零部件号", "PNC", "原产地", "适用车型（车系）",
            "规格属性", "规格容量", "成本价", "建议售价", "单位", "备注"};

    public static String[] storeRoomDatas = new String[]{"车店名称", "仓库名称",
            "备注", "库位名称"};

    public static String[] stockDatas = new String[]{"车店名称", "仓库",
            "商品名称", "库存数量", "入库单价", "仓位", "商品编码"};

    public static String[] carInfoDatas = new String[]{"车店名称", "姓名",
            "车牌号", "品牌", "车型", "手机号", "注册日期", "发动机号码",
            "车架号", "保险日期", "承保公司", "交强险日期", "交强险承保公司",
            "客户备注"};

    public static String[] itemDatas = new String[]{"车店名称", "商品名称",
            "商品类别", "条形码", "售价", "一级分类名称", "二级分类名称", "品牌",
            "厂商", "是否共享", "别称", "商品编码", "适用车型", "单位", "产地",
            "厂商类型", "是否启用", "备注", "云编码"};

    public static String[] supplierDatas = new String[]{"车店名称", "供应商名称",
            "传真", "地址", "联系人名称", "联系人号码", "银行卡户名", "银行卡开户行",
            "银行卡账号", "备注", "供应商编码", "是否共享(连锁店)"};

    public static String[] memberCardDatas = new String[]{"车店名称", "卡号",
            "会员卡名称", "车牌号", "卡类型", "卡品种名称",
            "开卡日期", "卡内余额", "联系人姓名", "联系人手机"};

    public static String[] memberCardItemDatas = new String[]{"车店名称", "卡号",
            "商品名称", "售价", "折扣", "剩余数量", "初始数量", "商品类别", "项目类别",
            "一级分类名称", "二级分类名称", "到期时间(不填为永久有效)",
            "是否永久有效", "商品编码"};

}
