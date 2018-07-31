package com.ys.datatool.domain;

import java.util.Objects;

/**
 * Created by mo on @date  2018/7/31.
 * 仓库
 *
 */
public class StoreRoom {

    private String id;

    /**
     * 车店名称
     */
    private String companyName;

    /**
     * 仓库名称
     */
    private String name;


    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 备注
     */
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreRoom)) return false;
        StoreRoom storeRoom = (StoreRoom) o;
        return Objects.equals(name, storeRoom.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
