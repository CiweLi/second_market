package com.example.springboot.common;

public enum GoodsEnum {
    PENDING("待审核"),

    FAIL("未通过"),

    SUCCESS("已通过"),

    SOLD("已售出");

    public String state;
    GoodsEnum(String state) {
        this.state = state;
    }
}
