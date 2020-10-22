package com.example.stock.strategy;

import com.example.stock.domain.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StockStrategy {

    void save(Stock stock) throws JsonProcessingException;
    void save(List<Stock> stock) throws JsonProcessingException;
    Map<String, Stock> getMaxMinByDate(Date date);

}
