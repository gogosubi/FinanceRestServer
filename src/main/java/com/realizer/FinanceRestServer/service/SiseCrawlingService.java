package com.realizer.FinanceRestServer.service;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.realizer.FinanceRestServer.model.StockItem;
import com.realizer.FinanceRestServer.model.StockPrice;

@Service
public class SiseCrawlingService 
{
	@Value("${stock.sise.url}")
	private String siseUrl;
	
	private StockItem getStockItem()
	{
		return null;
	}
	
	private StockPrice getStockPrice(Document document)
	{
		return null;
	}
	
}
