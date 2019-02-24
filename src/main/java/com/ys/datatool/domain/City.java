package com.ys.datatool.domain;

import java.util.List;

/**
 * Created by mo on 2019/2/24
 * 市
 */
public class City {

    private String id;

    private String name;

    private String province;

    private List<Country> countries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
