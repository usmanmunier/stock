package com.example.stock.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Stock {
    private String symbol;
    private double price;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    public Stock () {}

    private Stock(String symbol, double price, Date date) {
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



    public static class Builder {
        private String symbol;
        private double price;
        private Date date;

        public Builder Symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder Price (double price) {
            this.price = price;
            return this;
        }

        public Builder Date(Date date) {
            this.date = date;
            return this;
        }

        public Stock Build() {
            Objects.requireNonNull(symbol, "Symbol can not be null!");
            Objects.requireNonNull(price, "Price can not be null!");
            Objects.requireNonNull(date, "Date can not be null!");
            return new Stock(symbol, price, date);
        }
    }
}
