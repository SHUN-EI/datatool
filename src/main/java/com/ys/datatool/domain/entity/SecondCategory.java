package com.ys.datatool.domain.entity;

import com.ys.datatool.domain.entity.FirstCategory;

/**
 * Created by mo on 2019/4/28
 */
public class SecondCategory {

    private String sid;

    private String name;

    private FirstCategory firstCategory;

    public FirstCategory getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(FirstCategory firstCategory) {
        this.firstCategory = firstCategory;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SecondCategory{" +
                "sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", firstCategory=" + firstCategory +
                '}';
    }
}
