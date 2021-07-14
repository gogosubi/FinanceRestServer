package com.realizer.FinanceRestServer.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.StockInvestInfo;

public interface StockInvestInfoRepository extends JpaRepository<StockInvestInfo, Long> {

}
