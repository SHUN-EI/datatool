package com.ys.datatool.domain;

/**
 * Created by mo on   2017/6/19.
 */

/**
 * 会员卡卡内项目
 */
public class MemberCardItem {

    private String memberCardItemId;

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 会员卡卡号
     */
    private String cardCode;

    /**
     * 会员卡名称
     */
    private String memberCardName;

    /**
     * 持卡人姓名
     */
    private String name;

    /**
     * 持卡人手机
     */
    private String phone;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 卡内余额
     */
    private String balance;

    /**
     * 售价
     */
    private String price;

    /**
     * 折扣
     */
    private String discount;

    /**
     * 初始数量
     */
    private String originalNum;

    /**
     * 剩余数量
     */
    private String num;

    /**
     * 使用数量
     */
    private String usedNum;

    /**
     * 一级分类名称
     */
    private String firstCategoryName;

    /**
     * 二级分类名称
     */
    private String secondCategoryName;

    /**
     * 到期时间
     */
    private String validTime;

    /**
     * 开卡日期
     */
    private String dateCreated;

    /**
     * 商品编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否永久有效
     */
    private String isValidForever;

    /**
     * 商品类别-配件或服务项
     */
    private String itemType;

    /**
     * 项目类别-计次项
     */
    private String specialType="计次项";

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMemberCardItemId() {
        return memberCardItemId;
    }

    public void setMemberCardItemId(String memberCardItemId) {
        this.memberCardItemId = memberCardItemId;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalNum() {
        return originalNum;
    }

    public void setOriginalNum(String originalNum) {
        this.originalNum = originalNum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getFirstCategoryName() {
        return firstCategoryName;
    }

    public void setFirstCategoryName(String firstCategoryName) {
        this.firstCategoryName = firstCategoryName;
    }

    public String getSecondCategoryName() {
        return secondCategoryName;
    }

    public void setSecondCategoryName(String secondCategoryName) {
        this.secondCategoryName = secondCategoryName;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(String usedNum) {
        this.usedNum = usedNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsValidForever() {
        return isValidForever;
    }

    public void setIsValidForever(String isValidForever) {
        this.isValidForever = isValidForever;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSpecialType() {
        return specialType;
    }

    public void setSpecialType(String specialType) {
        this.specialType = specialType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "MemberCardItem{" +
                "memberCardItemId='" + memberCardItemId + '\'' +
                ", cardCode='" + cardCode + '\'' +
                ", memberCardName='" + memberCardName + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", itemName='" + itemName + '\'' +
                ", balance='" + balance + '\'' +
                ", price='" + price + '\'' +
                ", discount='" + discount + '\'' +
                ", originalNum='" + originalNum + '\'' +
                ", num='" + num + '\'' +
                ", usedNum='" + usedNum + '\'' +
                ", firstCategoryName='" + firstCategoryName + '\'' +
                ", secondCategoryName='" + secondCategoryName + '\'' +
                ", validTime='" + validTime + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", code='" + code + '\'' +
                ", remark='" + remark + '\'' +
                ", isValidForever='" + isValidForever + '\'' +
                ", itemType='" + itemType + '\'' +
                ", specialType='" + specialType + '\'' +
                '}';
    }
}
