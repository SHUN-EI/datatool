package com.ys.datatool.domain;

/**
 * Created by mo on  2017/5/30.
 */

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

/**
 * 车辆信息
 */
public class CarInfo {

    private String carId;

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户电话
     */
    private String phone;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 品牌
     */
    @JsonView({CarInfoSimpleView.class})
    private String brand;

    /**
     * 里程
     */
    private String mileage;

    /**
     * 车型
     */
    private String carModel;

    /**
     * 发动机号码
     */
    private String engineNumber;

    /**
     * 注册时间
     */
    private String registerDate;

    /**
     * 车架号
     */
    private String VINcode;

    /**
     * 车型颜色
     */
    @JsonView({CarInfoSimpleView.class})
    private List<String> colors;

    /**
     * 商业保险到期时间
     */
    private String vcInsuranceValidDate;

    /**
     * 保险承保公司
     */
    private String vcInsuranceCompany;

    /**
     * 交强险日期
     */
    private String tcInsuranceValidDate;

    /**
     * 交强险承保公司
     */
    private String tcInsuranceCompany;

    /**
     * 客户备注
     */
    private String remark;

    /**
     * 年审
     */
    private String AnnualTrial;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
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

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getVINcode() {
        return VINcode;
    }

    public void setVINcode(String VINcode) {
        this.VINcode = VINcode;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getVcInsuranceValidDate() {
        return vcInsuranceValidDate;
    }

    public void setVcInsuranceValidDate(String vcInsuranceValidDate) {
        this.vcInsuranceValidDate = vcInsuranceValidDate;
    }

    public String getVcInsuranceCompany() {
        return vcInsuranceCompany;
    }

    public void setVcInsuranceCompany(String vcInsuranceCompany) {
        this.vcInsuranceCompany = vcInsuranceCompany;
    }

    public String getTcInsuranceValidDate() {
        return tcInsuranceValidDate;
    }

    public void setTcInsuranceValidDate(String tcInsuranceValidDate) {
        this.tcInsuranceValidDate = tcInsuranceValidDate;
    }

    public String getTcInsuranceCompany() {
        return tcInsuranceCompany;
    }

    public void setTcInsuranceCompany(String tcInsuranceCompany) {
        this.tcInsuranceCompany = tcInsuranceCompany;
    }


    public interface CarInfoSimpleView{
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

    public String getAnnualTrial() {
        return AnnualTrial;
    }

    public void setAnnualTrial(String annualTrial) {
        AnnualTrial = annualTrial;
    }

    @Override
    public String toString() {
        return "CarInfo{" +
                "carId='" + carId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", brand='" + brand + '\'' +
                ", mileage='" + mileage + '\'' +
                ", carModel='" + carModel + '\'' +
                ", engineNumber='" + engineNumber + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", VINcode='" + VINcode + '\'' +
                ", colors=" + colors +
                ", vcInsuranceValidDate='" + vcInsuranceValidDate + '\'' +
                ", vcInsuranceCompany='" + vcInsuranceCompany + '\'' +
                ", tcInsuranceValidDate='" + tcInsuranceValidDate + '\'' +
                ", tcInsuranceCompany='" + tcInsuranceCompany + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
