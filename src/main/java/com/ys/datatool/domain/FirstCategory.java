package com.ys.datatool.domain;

/**
 * Created by mo on 2019/4/28
 */
public class FirstCategory {

    private String fid;

    private String name;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FirstCategory{" +
                "fid='" + fid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
