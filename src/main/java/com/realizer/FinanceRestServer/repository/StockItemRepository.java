package com.realizer.FinanceRestServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.StockItem;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

}