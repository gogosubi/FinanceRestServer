package com.realizer.FinanceRestServer.service;

import org.springframework.stereotype.Service;

import com.realizer.FinanceRestServer.repository.StockItemRepository;
import com.realizer.FinanceRestServer.repository.StockPriceRepository;

@Service
public class StockSiseService {
	private StockItemRepository stockItemRepository;
	
	private StockPriceRepository stockPriceRepository;

	public StockSiseService(StockItemRepository stockItemRepository, StockPriceRepository stockPriceRepository) 
	{
		this.stockItemRepository = stockItemRepository;
		this.stockPriceRepository = stockPriceRepository;
	}
	
	public void save()
	{
		System.out.println("SAVE");
	}
}
