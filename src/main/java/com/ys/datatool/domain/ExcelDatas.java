package com.ys.datatool.domain;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by mo on @date  2018/4/16.
 * <p>
 * excel表头字段
 */
public class ExcelDatas {

    public static Workbook workbook;

    public static String[] cloudCarModelDatas=new String[]{"levelId","厂家","品牌",
        "车系","VIN码"
    };

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
            "实收金额", "是否在店等", "计划完成时间", "支付类型", "备注", "开单时间", "结账时间"};

    public static String[] billDetailSomeFields = new String[]{"车店名称", "单据号",
            "商品名称", "数量", "售价(折扣价)", "折扣", "类别", "一级分类名称",
            "二级分类名称", "商品编码", "原价", "车牌", "开单时间"};

    public static String[] memberCardSomeFields = new String[]{"车店名称", "卡号",
            "会员卡名称", "车牌号", "卡类型", "卡品种名称", "开卡日期", "卡内余额",
            "联系人姓名", "联系人手机", "会员等级", "会员折扣", "备注", "会员卡ID", "到期时间"};

    public static String[] memberCardItemSomeFields = new String[]{"车店名称", "卡号",
            "商品名称", "单价", "折扣", "剩余数量", "初始数量", "商品类别", "项目类别",
            "一级分类名称", "二级分类名称", "到期时间(不填为永久有效)",
            "是否永久有效", "商品编码", "开卡日期", "会员卡名称", "联系人姓名", "联系人手机", "会员卡ID"};

    public static String[] YuanLeCheBaoStockDatas = new String[]{"车店名称", "仓库",
            "商品名称", "配件类型", "品牌", "型号", "规格", "销售价", "库存数量", "入库单价", "仓位", "商品编码"};

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
