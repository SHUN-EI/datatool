package com.ys.datatool.domain;

/**
 * Created by mo on 2017/7/14.
 */

/**
 * 库存
 */
public class Stock {

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 仓库
     */
    private String storeRoomName;

    /**
     * 仓位
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
                '}';
    }
}
