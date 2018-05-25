package com.ys.datatool.domain;

/**
 * Created by mo on  2017/5/30.
 */

/**
 * 单据
 */
public class Bill {

    private String id;

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
    private String carLicense;

    /**
     * 车型
     */
    private String automodel;

    /**
     * 联系人
     */
    private String clientName;

    /**
     * 联系人电话
     */
    private String clientPhone;

    /**
     * 联系人单位
     */
    private String company;

    /**
     * 总计
     */
    private String totalAmount;

    /**
     * 实收
     */
    private String actualAmount;

    /**
     * 单据折扣(基于总计的折扣)
     *
     */
    private String discount;

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
     *是否在店等
     */
    private String waitInStore;

    /**
     * 支付类型
     */
    private String payType;

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

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public String getAutomodel() {
        return automodel;
    }

    public void setAutomodel(String automodel) {
        this.automodel = automodel;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
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

    @Override
    public String toString() {
        return "Bill{" +
                "id='" + id + '\'' +
                ", billNo='" + billNo + '\'' +
                ", cardCode='" + cardCode + '\'' +
                ", carLicense='" + carLicense + '\'' +
                ", automodel='" + automodel + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", company='" + company + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", actualAmount='" + actualAmount + '\'' +
                ", discount='" + discount + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", dateExpect='" + dateExpect + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", remark='" + remark + '\'' +
                ", mileage='" + mileage + '\'' +
                ", state='" + state + '\'' +
                ", waitInStore='" + waitInStore + '\'' +
                ", payType='" + payType + '\'' +
                '}';
    }
}
