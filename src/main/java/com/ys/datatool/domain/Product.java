package com.ys.datatool.domain;

/**
 * Created by mo on  2017/5/30.
 *
 * 商品(配件/服务项目)
 */

public class Product {

    private String id;

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 配件/服务名称
     */
    private String productName;

    /**
     * 商品编码
     */
    private String code;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 配件编号
     */
    private String productCode;

    /**
     * 一级分类名称
     */
    private String firstCategoryName;

    /**
     * 二级分类名称
     */
    private String secondCategoryName;

    /**
     * 商品类别-配件或服务项
     */
    private String itemType;

    /**
     * 单位(服务项目没有单位)
     */
    private String unit;

    /**
     * 品牌
     */
    private String brandName;

    /**
     * 参考售价
     */
    private String price;

    /**
     * 库存数量
     */
    private String quantity;

    /**
     * 单位成本
     */
    private String unitCost;

    /**
     * 库存总成本
     */
    private String totalCost;

    /**
     * 仓库
     */
    private String storeRoomName;

    /**
     * 产地
     */
    private String origin;

    /**
     * 适用车型
     */
    private String carModel;

    /**
     * 是否共享(连锁店)
     */
    private String isShare;

    /**
     * 是否启用
     */
    private String isActive="是";

    /**
     * 备注
     */
    private String remark;

    /**
     * 厂商
     */
    private String manufactory;

    /**
     * 厂商类型
     */
    private String manufactoryType;

    /**
     * 别称
     */
    private String alias;

    /**
     * 云商品编码
     */
    private String cloudGoodsCode;

    public String getManufactory() {
        return manufactory;
    }

    public void setManufactory(String manufactory) {
        this.manufactory = manufactory;
    }

    public String getManufactoryType() {
        return manufactoryType;
    }

    public void setManufactoryType(String manufactoryType) {
        this.manufactoryType = manufactoryType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCloudGoodsCode() {
        return cloudGoodsCode;
    }

    public void setCloudGoodsCode(String cloudGoodsCode) {
        this.cloudGoodsCode = cloudGoodsCode;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getStoreRoomName() {
        return storeRoomName;
    }

    public void setStoreRoomName(String storeRoomName) {
        this.storeRoomName = storeRoomName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", companyName='" + companyName + '\'' +
                ", productName='" + productName + '\'' +
                ", code='" + code + '\'' +
                ", barCode='" + barCode + '\'' +
                ", productCode='" + productCode + '\'' +
                ", firstCategoryName='" + firstCategoryName + '\'' +
                ", secondCategoryName='" + secondCategoryName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", unit='" + unit + '\'' +
                ", brandName='" + brandName + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unitCost='" + unitCost + '\'' +
                ", totalCost='" + totalCost + '\'' +
                ", storeRoomName='" + storeRoomName + '\'' +
                ", origin='" + origin + '\'' +
                ", carModel='" + carModel + '\'' +
                ", isShare='" + isShare + '\'' +
                ", isActive='" + isActive + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
