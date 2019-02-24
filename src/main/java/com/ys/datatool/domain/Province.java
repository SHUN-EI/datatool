package com.ys.datatool.domain;

import java.util.List;

/**
 * Created by mo on 2019/2/24
 * 省
 */
public class Province {

    private String name;

    private String id;

    private List<City> cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
