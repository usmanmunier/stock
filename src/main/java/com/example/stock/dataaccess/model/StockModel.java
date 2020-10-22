package com.example.stock.dataaccess.model;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "stock")
public class StockModel extends BaseModel {

    private String symbol;

    private double price;

    @Temporal(TemporalType.DATE)
    private Date date;

    public StockModel () {}

    public StockModel(String symbol, double price, Date date) {
        this.symbol = symbol;
        this.price = price;
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }
}
