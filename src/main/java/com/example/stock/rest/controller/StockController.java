package com.example.stock.rest.controller;

import com.example.stock.annotation.Authenticate;
import com.example.stock.domain.Stock;
import com.example.stock.request.StockRequest;
import com.example.stock.service.StockService;
import com.example.stock.service.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockController extends BaseController{

    private final StockService stockService;

    @Autowired
    public StockController(StockServiceImpl stockService) {
        this.stockService = stockService;
    }

    @Authenticate
    @GetMapping (path = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Stock>> getDatabyDate(@PathVariable @Valid String date) {
        validateRequest();
        try {
            return ResponseEntity.ok(stockService.getMaxMinByDate(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Authenticate
    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Stock>> getData(@RequestParam @Valid String date) {
        validateRequest();
        try {
            return ResponseEntity.ok(stockService.getAllStocksByDate(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Authenticate
    @PostMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addStock(@RequestBody StockRequest stockRequest) {
        validateRequest();
        try {
            stockService.addStock(stockRequest);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Authenticate
    @PostMapping (path="/bulk", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBulkStock(@RequestBody List<StockRequest> stockRequests) {
        validateRequest();
        try {
            stockService.addStock(stockRequests);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }
}
