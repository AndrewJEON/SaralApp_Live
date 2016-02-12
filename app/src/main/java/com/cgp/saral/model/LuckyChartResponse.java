package com.cgp.saral.model;

/**
 * Created by veera on 11-02-2016.
 */
public class LuckyChartResponse {
    private String luckyNumber;
    private String luckyChart;
    private String errormsg;
    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getLuckyNumber() {
        return luckyNumber;
    }

    public void setLuckyNumber(String luckyNumber) {
        this.luckyNumber = luckyNumber;
    }

    public String getLuckyChart() {
        return luckyChart;
    }

    public void setLuckyChart(String luckyChart) {
        this.luckyChart = luckyChart;
    }
}
