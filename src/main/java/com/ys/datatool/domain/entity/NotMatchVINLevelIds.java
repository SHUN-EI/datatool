package com.ys.datatool.domain.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by mo on  2018/8/15.
 */
public class NotMatchVINLevelIds {

    protected String _id;

    @NotNull
    private String vin;

    private List<String> levelIds;

    /**
     * 记录创建时间
     */
    private Date dateCreated;

    /**
     * 记录最后的更新时间，如果未有levelIds返回或者力洋那边抛异常时，根据这个时间去判断5天后才会再调用力洋接口
     * 重新获取数据，避免浪费限定的调用次数（一天1000次）
     */
    private Date lastUpdateTime;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<String> getLevelIds() {
        return levelIds;
    }

    public void setLevelIds(List<String> levelIds) {
        this.levelIds = levelIds;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
