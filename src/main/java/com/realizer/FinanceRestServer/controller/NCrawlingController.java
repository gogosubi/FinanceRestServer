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
	public String getStock(@PathVariable String code)
	{
		stockSiseRepository.saveStockSiseDetail(code);
		return "sise";
	}
	
	@GetMapping("/stock/sise_day/{code}/{st_dt}/{nd_dt}")
	public String getPeriodStock(@PathVariable String st_dt, @PathVariable String nd_dt, @PathVariable String code)
	{
		stockSiseRepository.savePeriodStockPrice(st_dt, nd_dt, code);
		return "";
	}
	
	@GetMapping("/stock/upjong")
	public String getGroupStock()
	{
		stockSiseRepository.saveStockGroupPrice();
		return "group";
	}
}
