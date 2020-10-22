package com.example.stock.strategy;

import com.example.stock.dataaccess.model.StockDayModel;
import com.example.stock.dataaccess.repository.StockDayRepository;
import com.example.stock.dataaccess.repository.StockRepository;
import com.example.stock.domain.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SaveToDBStrategyTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockDayRepository stockDayRepository;

    private StockDayModel stockDayModel;

    private List<Stock> stocks = new ArrayList<>();

    Date date =  null;

    @Before
    public void setUp() throws JsonProcessingException, ParseException {
        MockitoAnnotations.initMocks(this);
        String minStock = objectMapper.writeValueAsString(new Stock.Builder()
                .Symbol("APPL")
                .Price(120.00)
                .Date(new Date())
                .Build());
        String maxStock = objectMapper.writeValueAsString(new Stock.Builder()
                .Symbol("AMZN")
                .Price(3234.00)
                .Date(new Date())
                .Build());
        stockDayModel = new StockDayModel(minStock, maxStock, new Date());
        date = formatter.parse("2020-09-01");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date =  formatter.parse("2020-09-01");
        stocks.add(new Stock.Builder().Symbol("APPL").Price(120.00).Date(date).Build());
        stocks.add(new Stock.Builder().Symbol("TSLA").Price(500.00).Date(date).Build());
        stocks.add(new Stock.Builder().Symbol("GOOG").Price(1500.00).Date(date).Build());
        stocks.add(new Stock.Builder().Symbol("AMZN").Price(3234.00).Date(date).Build());

    }

    @Test
    public void Test_getMaxMinByDate() throws JsonProcessingException {
        StockStrategy saveToDBStrategy = new SaveToDBStrategy(stockRepository, stockDayRepository);
        saveToDBStrategy.save(stocks);
        Map<String, Stock> minMaxStocks = saveToDBStrategy.getMaxMinByDate(date);
        Assert.assertNotNull(minMaxStocks);
        Assert.assertEquals("AMZN", minMaxStocks.get("max").getSymbol());
        Assert.assertEquals("APPL", minMaxStocks.get("min").getSymbol());
    }

    @Test
    public void Test_getMaxMinByDate_NORecord() {
        StockStrategy saveToDBStrategy = new SaveToDBStrategy(stockRepository, stockDayRepository);
        Map<String, Stock> minMaxStocks = saveToDBStrategy.getMaxMinByDate(new Date());
        Assert.assertNull( minMaxStocks.get("max"));
        Assert.assertNull( minMaxStocks.get("min"));
    }



}
