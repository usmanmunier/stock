package com.example.stock.service;

import com.example.stock.dataaccess.model.StockModel;
import com.example.stock.dataaccess.repository.StockDayRepository;
import com.example.stock.dataaccess.repository.StockRepository;
import com.example.stock.domain.Stock;
import com.example.stock.request.StockRequest;
import com.example.stock.strategy.InMemoryStrategy;
import com.example.stock.strategy.OrderByDBStrategy;
import com.example.stock.strategy.SaveToDBStrategy;
import com.example.stock.strategy.StockStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
    private  final StockRepository stockRepository;
    private  final StockDayRepository stockDayRepository;
    private StockStrategy stockStrategy;

    @Value("${stock.strategy}")
    private String strategy;

    @Autowired
    public StockServiceImpl(StockRepository repository, StockDayRepository stockDayRepository) {
        this.stockRepository = repository;
        this.stockDayRepository = stockDayRepository;
    }

    /**
     * Based on the property we try to figure out which strategy we have to use
     */
    @PostConstruct
    private void init() {
        if(InMemoryStrategy.class.getSimpleName().equalsIgnoreCase(strategy)) {
            stockStrategy = new InMemoryStrategy(stockRepository);
        } else if(OrderByDBStrategy.class.getSimpleName().equalsIgnoreCase(strategy)) {
            stockStrategy = new OrderByDBStrategy(stockRepository);
        } else if(SaveToDBStrategy.class.getSimpleName().equalsIgnoreCase(strategy)) {
            stockStrategy = new SaveToDBStrategy(stockRepository, stockDayRepository);
        } else {
            throw new RuntimeException("Strategy not found!");
        }

    }


    @Override
    public Map<String, Stock> getMaxMinByDate(String date) {
        try {
            return stockStrategy.getMaxMinByDate(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Stock> getAllStocksByDate(String date) {
        List<Stock> stocks = new ArrayList<>();
        try {
            List<StockModel> stockModels = stockRepository.findByDate(formatter.parse(date));
            for(StockModel stockModel: stockModels) {
                stocks.add(new Stock.Builder().Symbol(stockModel.getSymbol()).Date(stockModel.getDate()).Price(stockModel.getPrice()).Build());
            }
        } catch(Exception e) {
            logger.error("method=getAllStocksByDate; message=exception; details={}", e.getMessage());
        }
        return stocks;
    }

    @Override
    public void addStock(StockRequest stockRequest) {
        try {
            Stock stock = new Stock.Builder()
                    .Symbol(stockRequest.getSymbol())
                    .Price(stockRequest.getPrice())
                    .Date(formatter.parse(stockRequest.getDate()))
                    .Build();
            stockStrategy.save(stock);
        } catch (Exception e) {
            logger.error("method=addStock; message=exception; details={}", e.getMessage());
        }
    }

    @Override
    public void addStock(List<StockRequest> stockRequests) {
        List<Stock> stocks = new ArrayList<>();
        try {
            for(StockRequest stockRequest: stockRequests) {
                Stock stock = new Stock.Builder()
                        .Symbol(stockRequest.getSymbol())
                        .Price(stockRequest.getPrice())
                        .Date(formatter.parse(stockRequest.getDate()))
                        .Build();
               stocks.add(stock);
            }
            stockStrategy.save(stocks);
        } catch (Exception e) {
            logger.error("method=addStock; message=exception; details={}", e.getMessage());
        }
    }
}
