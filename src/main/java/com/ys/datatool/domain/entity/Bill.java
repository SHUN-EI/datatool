package com.ys.datatool.domain.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mo on  2017/5/30.
 * <p>
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
     * 报价项目 （名称X数量（价格））
     */
    private String serviceItemNames;

    /**
     * 报价商品 （名称X数量（价格））
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
     */
    private String discount = "0";

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
    private String waitInStore = "否";

    /**
     * 支付类型
     */
    private String payType = "现金";

    /**
     * 车品牌
     */
    private String brand;

    /**
     * 车型
     */
    private String carModel;

    /**
     * 单据号所在行数集合
     */
    private Set<String> billNoRows = new HashSet<>();

    /**
     * 51车宝-出库编号
     */
    private String stockOutNumber;

    /**
     * 51车宝-记账类型
     */
    private String accountType;


    /**
     * 单据项目
     */
    private String itemName;


    /**
     * 记账金额
     */
    private String debtAmount;

    /**
     * 已收金额
     */
    private String receivedAmount;

    /**
     * 剩余金额
     */
    private String amount;

    /**
     * 支付人
     */
    private String payer;

    /**
     * 单据名称
     */
    private String billCode;

    /**
     * 车赢家-单据ID
     */
    private String billId;


    /**
     * 单据内容
     */
    private String content;


    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(String debtAmount) {
        this.debtAmount = debtAmount;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getStockOutNumber() {
        return stockOutNumber;
    }

    public void setStockOutNumber(String stockOutNumber) {
        this.stockOutNumber = stockOutNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Set<String> getBillNoRows() {
        return billNoRows;
    }

    public void setBillNoRows(Set<String> billNoRows) {
        this.billNoRows = billNoRows;
    }

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
