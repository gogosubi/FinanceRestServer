package com.realizer.FinanceRestServer.model.item;

public enum MarketType {
	kospi, kosdaq, konex, none;
	
	static MarketType valueof(String name)
	{
		switch (name)
		{
			case "kospi" :
				return kospi;
			case "kosdaq" :
				return kospi;
			case "konex" :
				return kospi;
			default :
				return none;
		}
	}
}
