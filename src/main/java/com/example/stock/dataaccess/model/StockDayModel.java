package com.example.stock.dataaccess.model;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "stock_day")
public class StockDayModel extends BaseModel {

    private String min;
    private String max;

    @Temporal(TemporalType.DATE)
    private Date date;

    public StockDayModel() {}

    public StockDayModel(String min, String max, Date date) {
        this.min = min;
        this.max = max;
        this.date = date;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public Date getDate() {
        return date;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
