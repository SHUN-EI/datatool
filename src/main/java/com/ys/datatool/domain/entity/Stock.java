package com.ys.datatool.domain.entity;

/**
 * Created by mo on 2017/7/14.
 * 库存
 */

public class Stock {

    private String id;

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 仓库
     */
    private String storeRoomName="仓库";

    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品编码
     */
    private String productCode;

    /**
     * 库存数量
     */
    private String inventoryNum;

    /**
     * 入库单价
     */
    private String price;

    /**
     * 备注
     */
    private String remark;

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
     * 品牌
     */
    private String brand;

    /**
     * 规格
     */
    private String spec;

    /**
     * 售价
     */
    private String salePrice;

    /**
     * 元乐车宝-partsGuid
     */
    private String partsGuid;

    /**
     * 元乐车宝-规格
     */
    private String specification;

    /**
     * 元乐车宝-规格Id
     */
    private String specificationValue;

    /**
     * 条形码
     */
    private String barCode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSpecificationValue() {
        return specificationValue;
    }

    public void setSpecificationValue(String specificationValue) {
        this.specificationValue = specificationValue;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getInventoryNum() {
        return inventoryNum;
    }

    public void setInventoryNum(String inventoryNum) {
        this.inventoryNum = inventoryNum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getPartsGuid() {
        return partsGuid;
    }

    public void setPartsGuid(String partsGuid) {
        this.partsGuid = partsGuid;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "companyName='" + companyName + '\'' +
                ", storeRoomName='" + storeRoomName + '\'' +
                ", locationName='" + locationName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", inventoryNum='" + inventoryNum + '\'' +
                ", price='" + price + '\'' +
                ", remark='" + remark + '\'' +
                ", firstCategoryName='" + firstCategoryName + '\'' +
                ", secondCategoryName='" + secondCategoryName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", brand='" + brand + '\'' +
                ", spec='" + spec + '\'' +
                ", salePrice='" + salePrice + '\'' +
                '}';
    }
}
