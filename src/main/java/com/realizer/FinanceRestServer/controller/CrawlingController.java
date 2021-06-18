package com.realizer.FinanceRestServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CrawlingController 
{
	@GetMapping("/stock/{code}/sise")
	public String crawlingStock()
	{
		return "stock/sise";
	}
}
