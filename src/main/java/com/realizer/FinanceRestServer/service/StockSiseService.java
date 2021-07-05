package com.realizer.FinanceRestServer.service;

import java.sql.Timestamp;
import java.util.Optional;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
	
    @Value("${stock.sise.selector.item}")
    private String item_selector;

    @Value("${stock.sise.selector.content}")
    private String content_selector;

	public StockSiseService(StockItemRepository stockItemRepository, StockPriceRepository stockPriceRepository) 
	{
		this.stockItemRepository = stockItemRepository;
		this.stockPriceRepository = stockPriceRepository;
	}
	
	public void saveStockSise(String code)
	{
		StringBuilder url = new StringBuilder(stockSiseUrl);
		url.append(code);
		System.out.println(url);
		Document document = Crawler.getCrawlingSite(url.toString());

        // 종목정보가 처음인 경우 종목 정보 저장
        StockItem stockItem = getStockItem(document)
        						.orElseThrow(()->new IllegalArgumentException("전달한 주소[" + stockSiseUrl + "에서 유효한 정보를 가져오지 못했습니다."));
		
        // 종목정보가 처음인 경우 종목 정보 저장
        stockItemRepository.findByitemCode(stockItem.getItemCode())
                            .orElse(stockItemRepository.save(stockItem));
        
        // 종목시세정보
        StockPrice stockPrice = getStockPrice(document, stockItem)
				.orElseThrow(()->new IllegalArgumentException("전달한 주소[" + stockSiseUrl + "에서 유효한 정보를 가져오지 못했습니다."));
//        stockPriceRepository.save(stockPrice);
	}
	
	private Optional<StockItem> getStockItem(Document document)
	{
        for (Element element : document.select(item_selector) )
        {
            String code = element.select("div > span.code").text();
            String name = element.select("h2").text();
            String market = element.select("div > img").attr("class");

            return Optional.of(StockItem.builder().itemCode(code)
                    .itemName(name)
                    .type(MarketType.valueOf(market))
                    .build());
        }
        
        return Optional.of(new StockItem());
	}
	
	private Optional<StockPrice> getStockPrice(Document document, StockItem stockItem)
	{

        for (Element element : document.select(item_selector) )
        {
        	System.out.println("시간 : " + element.select("#time > em").text());
        }
        
    	Elements contents = document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr");

    	for ( Element trContent : contents )
    	{	
    		System.out.println("NODE SIZE " + trContent.childNodeSize());
            
            if ( trContent.childNodeSize() == 3 )
            {
            	continue;
            }
            
                Elements tdContents = trContent.select("td");
                System.out.println("ATTRIBUTE : " + tdContents.hasClass("num"));
                System.out.println(tdContents.get(0).text());
                System.out.println(tdContents.get(1).text());
//
//                KoreaStats koreaStats = KoreaStats.builder()
//                        .country(content.select("th").text())
//                        .diffFromPrevDay(Integer.parseInt(tdContents.get(0).text()))
//                        .total(Integer.parseInt(tdContents.get(1).text()))
//                        .death(Integer.parseInt(tdContents.get(2).text()))
//                        .incidence(Double.parseDouble(tdContents.get(3).text()))
//                        .inspection(Integer.parseInt(tdContents.get(4).text()))
//                        .build();
//
//                System.out.println(koreaStats.toString());

//    	}
//        	System.out.println("현재가 : " + element.select("#_nowVal").text());
//        	System.out.println("시가 : " + element.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(4) > td:nth-child(4) > span").text());
//        	System.out.println("고가 : " + element.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(5) > td:nth-child(4) > span").text());
//        	System.out.println("저가 : " + element.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(6) > td:nth-child(4) > span").text());
        	
        	
        	// #content > div.section.inner_sub > div:nth-child(1) > table > tbody
        	// #content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(1) > th:nth-child(1)
        	// 현재가 #_nowVal
        	// 거래량 #_quant
        	// 시가 #content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(4) > td:nth-child(4) > span
        	// 고가 #content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(5) > td:nth-child(4) > span
        	// 저가 #content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(6) > td:nth-child(4) > span
//            String code = element.select("div > span").text();
//            String name = element.select("h2").text();
//            String market = element.select("div > img").attr("class");
//            body > table.type2 > tbody > tr:nth-child(3)
//
//        	// 기준일자
//        	private String bsDt;
//        	
//        	// 종목정보
//        	@ManyToOne
//        	@JoinColumn(name="item_id")
//        	private StockItem stockItem;
//        	
//        	// 종가
//        	private long closePrice;
//        	
//        	// 시가
//        	private long openPrice;
//        	
//        	// 고가
//        	private long highPrice;
//        	
//        	// 저가
//        	private long lowPrice;
//        	
//        	// 거래량
//        	private long amount;
//        	
//        	@CreationTimestamp
//        	private Timestamp createDate;
//
//            return Optional.of(StockPrice.builder().stockItem(null)
//                    .build());
        }
        
        return Optional.of(new StockPrice());
	}
}
