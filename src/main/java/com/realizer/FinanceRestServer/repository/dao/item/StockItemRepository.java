package com.realizer.FinanceRestServer.repository.dao.item;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.item.StockItem;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    Optional<StockItem> findByitemCode(String code);
}
