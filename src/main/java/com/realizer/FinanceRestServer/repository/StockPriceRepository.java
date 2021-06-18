package com.realizer.FinanceRestServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.StockPrice;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

}