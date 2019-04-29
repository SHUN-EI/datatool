package com.ys.datatool.domain.entity;

/**
 * Created by mo on @date  2017/6/19.
 * 会员卡
 */


public class MemberCard {

    private String memberCardId;

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 会员卡卡号
     */
    private String cardCode;

    /**
     * 卡品种
     */
    private String cardSort;

    /**
     * 会员卡名称
     */
    private String memberCardName;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 卡类型
     */
    private String cardType = "储值卡";

    /**
     * 开卡日期
     */
    private String dateCreated;

    /**
     * 卡内余额
     */
    private String balance;

    /**
     * 充值金额
     */
    private String firstCharge;

    /**
     * 赠送金额
     */
    private String firstGift;

    /**
     * 剩余数量
     */
    private String num;

    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 联系人手机
     */
    private String phone;

    /**
     * 客户ID
     */
    private String clientId;

    /**
     * 到期时间
     */
    private String validTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 会员卡状态
     */
    private String state;

    /**
     * 家喻延期时间
     */
    private String changeTime;

    /**
     * 家喻卡种ID
     */
    private String ctId;

    /**
     * 家喻客户ID
     */
    private String cuId;

    /**
     * 元乐车宝-会员等级
     */
    private String grade;

    /**
     * 元乐车宝-会员折扣
     */
    private String discount;

    /**
     * 元乐车宝-会员卡userId
     */
    private String userId;

    /**
     * 车车云-会员卡Id
     */
    private String vipUserId;

    public String getVipUserId() {
        return vipUserId;
    }

    public void setVipUserId(String vipUserId) {
        this.vipUserId = vipUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMemberCardId() {
        return memberCardId;
    }

    public void setMemberCardId(String memberCardId) {
        this.memberCardId = memberCardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getMemberCardName() {
        return memberCardName;
    }

    public void setMemberCardName(String memberCardName) {
        this.memberCardName = memberCardName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFirstCharge() {
        return firstCharge;
    }

    public void setFirstCharge(String firstCharge) {
        this.firstCharge = firstCharge;
    }

    public String getFirstGift() {
        return firstGift;
    }

    public void setFirstGift(String firstGift) {
        this.firstGift = firstGift;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getCtId() {
        return ctId;
    }

    public void setCtId(String ctId) {
        this.ctId = ctId;
    }

    public String getCuId() {
        return cuId;
    }

    public void setCuId(String cuId) {
        this.cuId = cuId;
    }

    public String getCardSort() {
        return cardSort;
    }

    public void setCardSort(String cardSort) {
        this.cardSort = cardSort;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MemberCard{" +
                "memberCardId='" + memberCardId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", cardCode='" + cardCode + '\'' +
                ", memberCardName='" + memberCardName + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", cardType='" + cardType + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", balance='" + balance + '\'' +
                ", firstCharge='" + firstCharge + '\'' +
                ", firstGift='" + firstGift + '\'' +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", clientId='" + clientId + '\'' +
                ", validTime='" + validTime + '\'' +
                ", remark='" + remark + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
