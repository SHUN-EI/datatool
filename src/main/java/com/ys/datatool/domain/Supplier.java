package com.ys.datatool.domain;

/**
 * Created by mo on @date  2017/6/19.
 */

/**
 * 供应商
 */
public class Supplier {

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 供应商手机
     */
    private String phone;

    /**
     * 传真
     */
    private String fax;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 联系人手机
     */
    private String contactPhone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 供应商编码
     */
    private String code;

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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "companyName='" + companyName + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", address='" + address + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", remark='" + remark + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
