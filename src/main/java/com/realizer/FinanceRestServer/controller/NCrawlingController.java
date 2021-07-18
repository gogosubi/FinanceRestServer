package com.realizer.FinanceRestServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.realizer.FinanceRestServer.repository.service.StockSiseRepository;

@Controller
public class NCrawlingController 
{
	
	private StockSiseRepository stockSiseRepository;

	public NCrawlingController(StockSiseRepository stockSiseRepository) {
		this.stockSiseRepository = stockSiseRepository;
	}

	@GetMapping("/stock/sise/{code}")
	public String crawlingStock(@PathVariable String code)
	{
		stockSiseRepository.saveStockSiseDetail(code);
		return "sise";
	}
}
