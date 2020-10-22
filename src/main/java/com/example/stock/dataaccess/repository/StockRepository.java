package com.example.stock.dataaccess.repository;

import com.example.stock.dataaccess.model.StockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockModel, Integer> {

    List<StockModel> findByDate(Date date);
    List<StockModel> findByDateOrderByPriceAsc(Date date);
}
