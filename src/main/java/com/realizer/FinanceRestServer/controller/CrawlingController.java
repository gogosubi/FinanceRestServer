package com.realizer.FinanceRestServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.realizer.FinanceRestServer.service.StockSiseService;

@Controller
public class CrawlingController 
{
	private StockSiseService stockSiseService;
	
	public CrawlingController(StockSiseService stockSiseService) {
		this.stockSiseService = stockSiseService;
	}

	@GetMapping("/stock/sise/{code}")
	public String crawlingStock(@PathVariable String code)
	{
		stockSiseService.save();
		return "sise";
	}
}
