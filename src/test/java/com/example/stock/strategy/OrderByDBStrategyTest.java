package com.example.stock.strategy;

import com.example.stock.dataaccess.model.StockModel;
import com.example.stock.dataaccess.repository.StockRepository;
import com.example.stock.domain.Stock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class OrderByDBStrategyTest {

    @Mock
    private StockRepository stockRepository;
    List<StockModel> stockModels = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stockModels.add(new StockModel("APPL", 120.00, new Date()));
        stockModels.add(new StockModel("TSLA", 500.00, new Date()));
        stockModels.add(new StockModel("GOOG", 1500.00, new Date()));
        stockModels.add(new StockModel("AMZN", 3234.00, new Date()));
    }

    @Test
    public void Test_getMaxMinByDate() {
        StockStrategy orderByDBStrategy = new OrderByDBStrategy(stockRepository);
        Mockito.when(stockRepository.findByDateOrderByPriceAsc(Mockito.any())).thenReturn(stockModels);
        Map<String, Stock> minMaxStocks = orderByDBStrategy.getMaxMinByDate(new Date());
        Assert.assertNotNull(minMaxStocks);
        Assert.assertEquals("AMZN", minMaxStocks.get("max").getSymbol());
        Assert.assertEquals("APPL", minMaxStocks.get("min").getSymbol());
    }

    @Test
    public void Test_getMaxMinByDate_NORecord() {
        StockStrategy orderByDBStrategy = new OrderByDBStrategy(stockRepository);
        Mockito.when(stockRepository.findByDate(Mockito.any())).thenReturn(new ArrayList<>());
        Map<String, Stock> minMaxStocks = orderByDBStrategy.getMaxMinByDate(new Date());
        Assert.assertNull( minMaxStocks.get("max"));
        Assert.assertNull( minMaxStocks.get("min"));
    }

    @Test
    public void Test_getMaxMinByDate_NULL() {
        StockStrategy orderByDBStrategy = new OrderByDBStrategy(stockRepository);
        Mockito.when(stockRepository.findByDate(Mockito.any())).thenReturn(null);
        Map<String, Stock> minMaxStocks = orderByDBStrategy.getMaxMinByDate(new Date());
        Assert.assertNull( minMaxStocks.get("max"));
        Assert.assertNull( minMaxStocks.get("min"));

    }
}
