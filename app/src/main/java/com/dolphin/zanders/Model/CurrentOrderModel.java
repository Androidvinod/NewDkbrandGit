package com.dolphin.zanders.Model;

public class CurrentOrderModel {

    String order_no,order_date,shipto,status,order_total;

    public CurrentOrderModel(String order_no, String order_date, String shipto, String status, String order_total) {
        this.order_no = order_no;
        this.order_date = order_date;
        this.shipto = shipto;
        this.status = status;
        this.order_total = order_total;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getShipto() {
        return shipto;
    }

    public void setShipto(String shipto) {
        this.shipto = shipto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }
}
