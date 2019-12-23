package cn.endv.mytestapp;


import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Timestamp;
import java.util.Date;

public class Test1 {

    private int id;  //
    private int intx;  //
    private float fotlx;  //
    //    @JsonSerialize(using = CustomDateSerializer.class)
    private Date datex = new Date();
    private double decimalx;  //
    private boolean booleanx = true;  //
    //    @JsonSerialize(using = CustomDateSerializer.class)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date datetimex = new Date();  //
    private String textx;  //
    private String longtextx;  //
    private double doublex;  //
    private Timestamp timestampx;  //
    private Timestamp timestampv;  //

    public Test1() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIntx() {
        return this.intx;
    }

    public void setIntx(int intx) {
        this.intx = intx;
    }

    public float getFotlx() {
        return this.fotlx;
    }

    public void setFotlx(float fotlx) {
        this.fotlx = fotlx;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getDatex() {
        return this.datex;
    }

    public void setDatex(Date datex) {
        this.datex = datex;
    }

    public double getDecimalx() {
        return this.decimalx;
    }

    public void setDecimalx(double decimalx) {
        this.decimalx = decimalx;
    }

    public boolean getBooleanx() {
        return this.booleanx;
    }

    public void setBooleanx(boolean booleanx) {
        this.booleanx = booleanx;
    }

    public Date getDatetimex() {
        return this.datetimex;
    }

    public void setDatetimex(Date datetimex) {
        this.datetimex = datetimex;
    }

    public String getTextx() {
        return this.textx;
    }

    public void setTextx(String textx) {
        this.textx = textx;
    }

    public String getLongtextx() {
        return this.longtextx;
    }

    public void setLongtextx(String longtextx) {
        this.longtextx = longtextx;
    }

    public double getDoublex() {
        return this.doublex;
    }

    public void setDoublex(double doublex) {
        this.doublex = doublex;
    }

    public Timestamp getTimestampx() {
        return this.timestampx;
    }

    public void setTimestampx(Timestamp Timestampx) {
        this.timestampx = Timestampx;
    }

    public Timestamp getTimestampv() {
        return this.timestampv;
    }

    public void setTimestampv(Timestamp Timestampv) {
        this.timestampv = Timestampv;
    }
}
