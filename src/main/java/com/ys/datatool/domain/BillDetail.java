package com.ys.datatool.domain;

/**
 * Created by mo on  2017/5/30.
 * 单据明细
 */

public class BillDetail {

    private String detailId;

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 单据号
     */
    private String billNo;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 配件编号
     */
    private String itemCode;

    /**
     * 商品类别
     */
    private String itemType;

    /**
     * 工时
     */
    private String workingHour;

    /**
     * 工时费
     */
    private String salePrice;

    /**
     * 数量
     */
    private String quantity;

    /**
     * 单价
     */
    private String price;

    /**
     * 金额(实收)
     */
    private String totalAmount;

    /**
     * 折扣率
     */
    private String discountRate;

    /**
     * 优惠
     */
    private String deduction;

    /**
     * 里程
     */
    private String mileage;

    /**
     * 支付方式
     */
    private String payment;

    /**
     * 开单日期
     */
    private String dateAdded;

    /**
     * 预计完工日期
     */
    private String dateExpect;

    /**
     * 完工日期
     */
    private String dateEnd;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 一级分类名称
     */
    private String firstCategoryName;

    /**
     * 二级分类名称
     */
    private String secondCategoryName;

    /**
     * 折扣
     */
    private String discount;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(String workingHour) {
        this.workingHour = workingHour;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
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

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "BillDetail{" +
                "detailId='" + detailId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemType='" + itemType + '\'' +
                ", workingHour='" + workingHour + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", discountRate='" + discountRate + '\'' +
                ", deduction='" + deduction + '\'' +
                ", mileage='" + mileage + '\'' +
                ", payment='" + payment + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", dateExpect='" + dateExpect + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", clientName='" + clientName + '\'' +
                ", firstCategoryName='" + firstCategoryName + '\'' +
                ", secondCategoryName='" + secondCategoryName + '\'' +
                '}';
    }
}
