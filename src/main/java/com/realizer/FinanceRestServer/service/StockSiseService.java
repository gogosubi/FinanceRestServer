package com.realizer.FinanceRestServer.service;

import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.realizer.FinanceRestServer.model.MarketType;
import com.realizer.FinanceRestServer.model.StockItem;
import com.realizer.FinanceRestServer.model.StockPrice;
import com.realizer.FinanceRestServer.repository.StockItemRepository;
import com.realizer.FinanceRestServer.repository.StockPriceRepository;
import com.realizer.FinanceRestServer.util.Crawler;

@Service
public class StockSiseService {
	private StockItemRepository stockItemRepository;
	
	private StockPriceRepository stockPriceRepository;
	
	@Value("${stock.sise.url}")
	private String stockSiseUrl;
	
    @Value("${stock.sise.selector.company}")
    private String company_selector;

    @Value("${stock.sise.selector.content}")
    private String content_selector;

	public StockSiseService(StockItemRepository stockItemRepository, StockPriceRepository stockPriceRepository) 
	{
		this.stockItemRepository = stockItemRepository;
		this.stockPriceRepository = stockPriceRepository;
	}
	
	public void saveStockSise()
	{
		Document document = Crawler.getCrawlingSite(stockSiseUrl);

        // 종목정보가 처음인 경우 종목 정보 저장
        StockItem stockItem = getStockItem(document)
        						.orElseThrow(()->new IllegalArgumentException("전달한 주소[" + stockSiseUrl + "에서 유효한 정보를 가져오지 못했습니다."));
		
        // 종목정보가 처음인 경우 종목 정보 저장
        stockItemRepository.findByItemcode(stockItem.getItemCode())
                            .orElse(stockItemRepository.save(stockItem));
		System.out.println("SAVE");
	}
	
	private Optional<StockItem> getStockItem(Document document)
	{

        for (Element element : document.select(company_selector) )
        {
            String code = element.select("div > span").text();
            String name = element.select("h2").text();
            String market = element.select("div > img").attr("class");

            return Optional.of(StockItem.builder().itemCode(code)
                    .itemName(name)
                    .type(MarketType.valueOf(market))
                    .build());
        }
        
        return Optional.of(new StockItem());
	}
	
	private Optional<StockPrice> getStockPrice(Document document)
	{

        for (Element element : document.select(company_selector) )
        {
            String code = element.select("div > span").text();
            String name = element.select("h2").text();
            String market = element.select("div > img").attr("class");

            return Optional.of(StockPrice.builder().stockItem(null)
                    .build());
        }
        
        return Optional.of(new StockPrice());
	}
}
