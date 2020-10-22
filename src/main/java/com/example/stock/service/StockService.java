package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.request.StockRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StockService {

    Map<String, Stock> getMaxMinByDate(String date);

    List<Stock> getAllStocksByDate(String date);

    void addStock(StockRequest stockRequest);

    void addStock(List<StockRequest> stockRequests);

}
