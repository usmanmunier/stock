package com.example.stock.dataaccess.repository;

import com.example.stock.dataaccess.model.StockDayModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface StockDayRepository extends JpaRepository<StockDayModel, Integer> {

    StockDayModel findByDate(Date date);

}
