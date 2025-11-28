package com.example.springboot.common;

public enum ShelveEnum
{
    YES("是"),

    NO("否");

    public String state;
    ShelveEnum(String state)
    {
        this.state = state;
    }
}
