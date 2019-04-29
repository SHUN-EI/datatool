package com.ys.datatool.domain.entity;

/**
 * Created by mo on 2019/3/22
 */
public class MemberCardSort {


    private String  memberCardSortId;

    private String name;

    private String discount;

    public String getMemberCardSortId() {
        return memberCardSortId;
    }

    public void setMemberCardSortId(String memberCardSortId) {
        this.memberCardSortId = memberCardSortId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
