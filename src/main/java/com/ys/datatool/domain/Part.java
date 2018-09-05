package com.ys.datatool.domain;

/**
 * Created by mo on  2018/9/5.
 * <p>
 * 配件
 */
public class Part {

    /**
     * 零部件号
     */
    private String code;

    /**
     * 零部件名称
     */
    private String name;

    /**
     * 替代零部件号
     */
    private String replacePartCode;

    /**
     * PNC
     */
    private String partsCode;

    /**
     * 原产地
     */
    private String origin;


    /**
     * 适用车型（车系）
     */
    private String carModel;


    /**
     * 规格属性
     */
    private String specification;

    /**
     * 规格容量
     */
    private String specificationCapacity;

    /**
     * 成本价
     */
    private String costPrice;

    /**
     * 建议售价
     */
    private String salePrice;

    /**
     * 单位
     */
    private String unit;

    /**
     * 备注
     */
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReplacePartCode() {
        return replacePartCode;
    }

    public void setReplacePartCode(String replacePartCode) {
        this.replacePartCode = replacePartCode;
    }

    public String getPartsCode() {
        return partsCode;
    }

    public void setPartsCode(String partsCode) {
        this.partsCode = partsCode;
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

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSpecificationCapacity() {
        return specificationCapacity;
    }

    public void setSpecificationCapacity(String specificationCapacity) {
        this.specificationCapacity = specificationCapacity;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}