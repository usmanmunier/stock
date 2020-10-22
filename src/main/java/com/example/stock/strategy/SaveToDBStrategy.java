package com.example.stock.strategy;

import com.example.stock.annotation.LogExecutionTime;
import com.example.stock.dataaccess.model.StockDayModel;
import com.example.stock.dataaccess.model.StockModel;
import com.example.stock.dataaccess.repository.StockDayRepository;
import com.example.stock.dataaccess.repository.StockRepository;
import com.example.stock.domain.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class stores the min, max record in db
 */
public class SaveToDBStrategy implements StockStrategy {
    private static Logger logger = LoggerFactory.getLogger(InMemoryStrategy.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private  final StockRepository stockRepository;
    private  final StockDayRepository stockDayRepository;

    public SaveToDBStrategy(StockRepository stockRepository, StockDayRepository stockDayRepository) {
        this.stockRepository = stockRepository;
        this.stockDayRepository = stockDayRepository;
    }

    /**
     * while saving new stock, we are also saving if its the max or min by date
     * This can help us in query and we dont have to process anything
     * @param stock
     * @throws JsonProcessingException
     */
    @LogExecutionTime
    @Override
    public void save(Stock stock) throws JsonProcessingException {
        StockDayModel stockDayModel = getModelWithMinAndMax(stock, stock, stock.getDate());
        stockDayRepository.save(stockDayModel);
        StockModel model = new StockModel(stock.getSymbol(), stock.getPrice(), stock.getDate());
        stockRepository.save(model);
    }

    /**
     * while saving new stock, we are also saving if its the max or min by date
     * This can help us in query and we dont have to process anything
     * @param stocks
     * @throws JsonProcessingException
     */
    @LogExecutionTime
    @Override
    public void save(List<Stock> stocks) throws JsonProcessingException {

        // first save the stocks to db
        for(Stock stock: stocks) {
            StockModel model = new StockModel(stock.getSymbol(), stock.getPrice(), stock.getDate());
            stockRepository.save(model);
        }

        // now lets calculate min and max by date
        // create map which will have stocks by date
        Map<Date, List<Stock>> stocksByDate = stocksByDateMap(stocks);
        for(Date date: stocksByDate.keySet()) {
            Stock minStock = null;
            Stock maxStock = null;
            List<Stock> stockList = stocksByDate.get(date);
            // get the min and max stock from the list of stock for a given date
            for(Stock stock: stockList) {
                if(minStock == null || stock.getPrice() < minStock.getPrice()) {
                    minStock = stock;
                }
                if(maxStock == null || stock.getPrice() > maxStock.getPrice()) {
                    maxStock = stock;
                }
            }

            // get updated StockDayModel
            StockDayModel stockDayModel = getModelWithMinAndMax(minStock, maxStock, date);

            // save the min max by date
            stockDayRepository.save(stockDayModel);
        }
    }

    /**
     * As min, max record is already preserved in the DB
     * We just get it and return it
     * @param date
     * @return
     */
    @LogExecutionTime
    @Override
    public Map<String, Stock> getMaxMinByDate(Date date) {
        Stock minStock = null;
        Stock maxStock = null;
        Map<String, Stock> stocks = new HashMap<>();
        try{
            StockDayModel stockDayModel = stockDayRepository.findByDate(date);
            if(stockDayModel != null) {
                minStock = objectMapper.readValue(stockDayModel.getMin(), Stock.class);
                maxStock = objectMapper.readValue(stockDayModel.getMax(), Stock.class);
            }
            stocks.put("max", maxStock);
            stocks.put("min",minStock);
        }catch (Exception e) {
            logger.error("method=getMaxMinByDate; message=exception; details={}", e.getMessage());
        }

        return stocks;
    }

    /**
     * create map of stocks by date
     * @param stocks
     * @return
     */
    private Map<Date, List<Stock>> stocksByDateMap(List<Stock> stocks) {
        Map<Date, List<Stock>> stockByDateMap = new HashMap<>();
        for(Stock stock: stocks){
            List<Stock> stockList = stockByDateMap.get(stock.getDate());
            if(stockList == null) {
                stockList = new ArrayList<>();
            }
            stockList.add(stock);
            stockByDateMap.put(stock.getDate(), stockList);
        }
        return stockByDateMap;
    }

    private StockDayModel getModelWithMinAndMax(Stock minStock, Stock maxStock, Date date) throws JsonProcessingException {
        // serialize to json
        String minStockString = objectMapper.writeValueAsString(minStock);
        String maxStockString = objectMapper.writeValueAsString(maxStock);

        // check if min, max already exist in the DB
        StockDayModel stockDayModel = stockDayRepository.findByDate(date);
        if(stockDayModel !=null) {
            Stock dbMinStock = objectMapper.readValue(stockDayModel.getMin(), Stock.class);
            Stock dbMaxStock = objectMapper.readValue(stockDayModel.getMax(), Stock.class);

            if (minStock.getPrice() < dbMinStock.getPrice()) {
                stockDayModel.setMin(minStockString);
            }

            if (maxStock.getPrice() > dbMaxStock.getPrice()) {
                stockDayModel.setMax(maxStockString);
            }

        } else {
            stockDayModel = new StockDayModel(minStockString, maxStockString, date);
        }
        return stockDayModel;
    }
}
