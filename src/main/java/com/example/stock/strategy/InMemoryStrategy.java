package com.example.stock.strategy;

import com.example.stock.annotation.LogExecutionTime;
import com.example.stock.dataaccess.model.StockModel;
import com.example.stock.dataaccess.repository.StockRepository;
import com.example.stock.domain.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class gets the date data from the DB and then find the min and max
 */
public class InMemoryStrategy implements StockStrategy {
    private static Logger logger = LoggerFactory.getLogger(InMemoryStrategy.class);
    private  final StockRepository stockRepository;

    public InMemoryStrategy(StockRepository repository) {
        this.stockRepository = repository;
    }

    /**
     * saves the stock details to DB
     * @param stock
     */
    @LogExecutionTime
    @Override
    public void save(Stock stock) {
        StockModel model = new StockModel(stock.getSymbol(), stock.getPrice(), stock.getDate());
        stockRepository.save(model);
    }

    /**
     * saves the stock details to DB
     * @param stocks
     */
    @LogExecutionTime
    @Override
    public void save(List<Stock> stocks) {
        for(Stock stock: stocks) {
            this.save(stock);
        }
    }


    /**
     * Gets the data from db by date
     * then loop through and figures out which one is Max and which one is min
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
            List<StockModel> stockModels = stockRepository.findByDate(date);
            for(StockModel stockModel: stockModels) {
                if(minStock == null || stockModel.getPrice() < minStock.getPrice()) {
                    minStock = new Stock.Builder().Symbol(stockModel.getSymbol()).Date(stockModel.getDate()).Price(stockModel.getPrice()).Build();
                }

                if(maxStock == null || stockModel.getPrice() > maxStock.getPrice()) {
                    maxStock = new Stock.Builder().Symbol(stockModel.getSymbol()).Date(stockModel.getDate()).Price(stockModel.getPrice()).Build();
                }
            }
            stocks.put("max", maxStock);
            stocks.put("min",minStock);
        }catch (Exception e) {
            logger.error("method=getMaxMinByDate; message=exception; details={}", e.getMessage());
        }

        return stocks;
    }
}
