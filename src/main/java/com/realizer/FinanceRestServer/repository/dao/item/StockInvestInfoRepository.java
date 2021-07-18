package com.realizer.FinanceRestServer.repository.dao.item;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.item.StockInvestInfo;

public interface StockInvestInfoRepository extends JpaRepository<StockInvestInfo, Long> {

}
