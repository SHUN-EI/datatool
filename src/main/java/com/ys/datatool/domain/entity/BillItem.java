package com.ys.datatool.domain.entity;

/**
 * Created by mo on  2017/5/30.
 * 单据项目
 */

public class BillItem {

    private String consume_history_id;

    /**
     * 单据号
     */
    private String ref_no;

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 项目名称
     */
    private String content;

    /**
     * 金额
     */
    private String price;

    /**
     * 消费时间
     */
    private String date_added;

    public String getConsume_history_id() {
        return consume_history_id;
    }

    public void setConsume_history_id(String consume_history_id) {
        this.consume_history_id = consume_history_id;
    }

    public String getRef_no() {
        return ref_no;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    @Override
    public String toString() {
        return "BillItem{" +
                "consume_history_id='" + consume_history_id + '\'' +
                ", ref_no='" + ref_no + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", content='" + content + '\'' +
                ", price='" + price + '\'' +
                ", date_added='" + date_added + '\'' +
                '}';
    }
}
