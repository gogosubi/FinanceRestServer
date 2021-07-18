package com.realizer.FinanceRestServer.repository.dao.item;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.item.StockPrice;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

}
