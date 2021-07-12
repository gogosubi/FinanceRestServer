package com.realizer.FinanceRestServer.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.StockPriceAdditionalInfo;

public interface StockPriceAdditionalInfoRepository extends JpaRepository<StockPriceAdditionalInfo, Long> {

}
