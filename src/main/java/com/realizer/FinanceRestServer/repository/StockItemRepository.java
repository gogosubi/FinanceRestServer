package com.realizer.FinanceRestServer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.StockItem;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    Optional<StockItem> findByitemCode(String code);
}
