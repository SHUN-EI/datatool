package com.ys.datatool.domain;

/**
 * Created by mo on  2017/5/30.
 *
 * 单据
 */

public class Bill {

    private String id;

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 单据号
     */
    private String billNo;

    /**
     * 会员卡卡号
     */
    private String cardCode;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 报价项目
     */
    private String serviceItemNames;

    /**
     * 报价商品
     */
    private String goodsNames;


    /**
     * 车型
     */
    private String automodel;

    /**
     * 联系人
     */
    private String name;

    /**
     * 联系人电话
     */
    private String phone;

    /**
     * 联系人单位
     */
    private String company;

    /**
     * 总计(单据金额)
     */
    private String totalAmount;

    /**
     * 业务员
     */
    private String receptionistName;

    /**
     * 实收 paid = total*(discount/10)
     */
    private String actualAmount;

    /**
     * 单据折扣(基于总计的折扣)
     *
     */
    private String discount="0";

    /**
     * 开单日期
     */
    private String dateAdded;

    /**
     * 预计完工
     */
    private String dateExpect;

    /**
     * 完工日期
     */
    private String dateEnd;

    /**
     * 备注
     */
    private String remark;

    /**
     * 里程
     */
    private String mileage;

    /**
     * 单据状态
     */
    private String state;

    /**
     * 是否在店等
     */
    private String waitInStore="否";

    /**
     * 支付类型
     */
    private String payType="现金";

    /**
     * 车品牌
     */
    private String brand;

    /**
     * 车型
     */
    private String carModel;

    public String getServiceItemNames() {
        return serviceItemNames;
    }

    public void setServiceItemNames(String serviceItemNames) {
        this.serviceItemNames = serviceItemNames;
    }

    public String getGoodsNames() {
        return goodsNames;
    }

    public void setGoodsNames(String goodsNames) {
        this.goodsNames = goodsNames;
    }

    public String getReceptionistName() {
        return receptionistName;
    }

    public void setReceptionistName(String receptionistName) {
        this.receptionistName = receptionistName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }


    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getAutomodel() {
        return automodel;
    }

    public void setAutomodel(String automodel) {
        this.automodel = automodel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateExpect() {
        return dateExpect;
    }

    public void setDateExpect(String dateExpect) {
        this.dateExpect = dateExpect;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getWaitInStore() {
        return waitInStore;
    }

    public void setWaitInStore(String waitInStore) {
        this.waitInStore = waitInStore;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }




}
