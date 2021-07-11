package com.realizer.FinanceRestServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.realizer.FinanceRestServer.repository.service.StockSiseRepository;
import com.realizer.FinanceRestServer.service.NStockSiseService;

@Controller
public class CrawlingController 
{
	
	private StockSiseRepository stockSiseRepository;

	public CrawlingController(StockSiseRepository stockSiseRepository) {
		this.stockSiseRepository = stockSiseRepository;
	}

	@GetMapping("/stock/sise/{code}")
	public String crawlingStock(@PathVariable String code)
	{
		stockSiseRepository.saveStockSise(code);
		return "sise";
	}
}
