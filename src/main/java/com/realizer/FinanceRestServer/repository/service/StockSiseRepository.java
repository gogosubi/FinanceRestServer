package com.realizer.FinanceRestServer.repository.service;

public interface StockSiseRepository {
	public void saveStockSiseDetail(String code);
	public void savePeriodStockPrice(String st_dt, String nd_dt, String code);
	public void saveStockGroupPrice();
	public void saveStockGroupDetail();
}
