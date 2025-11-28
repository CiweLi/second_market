package com.example.springboot.common;

public enum OrderEnum {
    PENDING("等待见面交易"),

    SHIPPED("已发货"),

    OK("已完成"),

    COMMENT_ALREADY("已评价");

    public String state;
    OrderEnum(String state) {
        this.state = state;
    }
}
