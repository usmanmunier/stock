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
 * This class rely on DB order by and does not perform any in memory operation
 */
public class OrderByDBStrategy implements StockStrategy {
    private static Logger logger = LoggerFactory.getLogger(InMemoryStrategy.class);
    private  final StockRepository stockRepository;

    public OrderByDBStrategy(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @LogExecutionTime
    @Override
    public void save(Stock stock) {
        StockModel model = new StockModel(stock.getSymbol(), stock.getPrice(), stock.getDate());
        stockRepository.save(model);
    }

    @LogExecutionTime
    @Override
    public void save(List<Stock> stocks) {
        for(Stock stock : stocks) {
            this.save(stock);
        }
    }

    /**
     * once all the records are in the DB
     * This API pulls the data in ascending order by price
     * top record will be min price
     * last record will be the max price symbol
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
            List<StockModel> stockModels = stockRepository.findByDateOrderByPriceAsc(date);
            if(stockModels.size() > 0) {
                StockModel stockModel = stockModels.get(0);
                minStock = new Stock.Builder().Symbol(stockModel.getSymbol()).Date(stockModel.getDate()).Price(stockModel.getPrice()).Build();

                stockModel = stockModels.get(stockModels.size() - 1);
                maxStock = new Stock.Builder().Symbol(stockModel.getSymbol()).Date(stockModel.getDate()).Price(stockModel.getPrice()).Build();
            }
            stocks.put("max", maxStock);
            stocks.put("min",minStock);
        }catch (Exception e) {
            logger.error("method=getMaxMinByDate; message=exception; details={}", e.getMessage());
        }

        return stocks;
    }
}
