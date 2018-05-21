package com.jbox.project.task_okex.bean;

import java.math.BigInteger;

/**
 * Created by Administrator on 2018/5/20.
 */
public class KLineBean {
    private Long lDataTime;
    private Float fOpen;
    private Float fHigh;
    private Float fLow;
    private Float fEnd;
    private  Float fVol;

    public Long getlDataTime() {
        return lDataTime;
    }

    public void setlDataTime(Long lDataTime) {
        this.lDataTime = lDataTime;
    }

    public Float getfOpen() {
        return fOpen;
    }

    public void setfOpen(Float fOpen) {
        this.fOpen = fOpen;
    }

    public Float getfHigh() {
        return fHigh;
    }

    public void setfHigh(Float fHigh) {
        this.fHigh = fHigh;
    }

    public Float getfLow() {
        return fLow;
    }

    public void setfLow(Float fLow) {
        this.fLow = fLow;
    }

    public Float getfEnd() {
        return fEnd;
    }

    public void setfEnd(Float fEnd) {
        this.fEnd = fEnd;
    }

    public Float getfVol() {
        return fVol;
    }

    public void setfVol(Float fVol) {
        this.fVol = fVol;
    }
}
