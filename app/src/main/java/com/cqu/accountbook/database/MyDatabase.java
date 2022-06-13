package com.cqu.accountbook.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyDatabase {
    @PrimaryKey(autoGenerate = true)//主键是否自动增长，默认为false
    private int id;
    private long money;
    private int year;
    private int month;
    private int day;
    private String type;
    private String remark;
    private Boolean inOut;//false为收入，true为支出

    public MyDatabase(long money, int year, int month, int day, String type, String remark, Boolean inOut) {
        this.money = money;
        this.year = year;
        this.month = month;
        this.day = day;
        this.type = type;
        this.remark = remark;
        this.inOut = inOut;//false为收入，true为支出
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getInOut() {
        return inOut;
    }

    public void setInOut(Boolean inOut) {
        this.inOut = inOut;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
