package com.realizer.FinanceRestServer.service;

import java.sql.Timestamp;
import java.text.NumberFormat;
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
import com.realizer.FinanceRestServer.repository.dao.StockItemRepository;
import com.realizer.FinanceRestServer.repository.dao.StockPriceRepository;
import com.realizer.FinanceRestServer.repository.service.StockSiseRepository;
import com.realizer.FinanceRestServer.util.Crawler;
import com.realizer.FinanceRestServer.util.NumberUtility;

@Service
public class NStockSiseService implements StockSiseRepository {
	private StockItemRepository stockItemRepository;
	
	private StockPriceRepository stockPriceRepository;
	
	@Value("${stock.sise.url}")
	private String stockSiseUrl;

    @Value("${stock.sise.selector.content}")
    private String content_selector;

	public NStockSiseService(StockItemRepository stockItemRepository, StockPriceRepository stockPriceRepository) 
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
        StockPrice stockPrice = getStockPrice(document)
				.orElseThrow(()->new IllegalArgumentException("전달한 주소[" + stockSiseUrl + "에서 유효한 정보를 가져오지 못했습니다."));
//        stockPriceRepository.save(stockPrice);
	}
	
	private Optional<StockItem> getStockItem(Document document)
	{
        return Optional.of(
        			StockItem.builder()
        			.itemCode(document.select("#middle > div.h_company > div.wrap_company > div > span.code").text())
        			.itemName(document.select("#middle > div.h_company > div.wrap_company > h2").text())
        			.type(MarketType.valueOf(document.select("#middle > div.h_company > div.wrap_company > div > img").attr("class")))
        			.build());
	}
	
	private Optional<StockPrice> getStockPrice(Document document)
	{
		
		// StockPrice 생성
		StockPrice stockPrice = StockPrice.builder()
									.bsDt(document.select("#time > em").text().substring(0, 10).replaceAll(".",  ""))
									.currentPrice(0)
									.openPrice(0)
									.highPrice(0)
									.lowPrice(0)
									.amount(0)
									.build();
    	Elements contents = document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr");

    	int i = 0;
    	for ( Element trContent : contents )
    	{	
    		System.out.println("NODE SIZE " + (i++));
            
            if ( trContent.childNodeSize() == 3 )
            {
            	continue;
            }

            Elements thContents = trContent.select("th");
            Elements tdContents = trContent.select("td");
            
//            System.out.println(thContents.get(0).text());
//            System.out.println(tdContents.get(0).text());
//            Object obj = NumberUtility.convertNumber(tdContents.get(0).text());
            System.out.println(thContents.get(0).text() + " : " + NumberUtility.convertNumber(tdContents.get(0).text()) + "==>" + NumberUtility.convertNumber(tdContents.get(0).text()).getClass().getName());
            System.out.println(thContents.get(1).text() + " : " + NumberUtility.convertNumber(tdContents.get(0).text()) + "==>" + NumberUtility.convertNumber(tdContents.get(1).text()).getClass().getName());

            //System.out.println("ATTRIBUTE : " + tdContents.hasClass("num"));
//            System.out.println(thContents.get(1).text());
//            System.out.println(tdContents.get(1).text());
            
//            StockPrice.builder()
//            	.currentPrice(0)
            
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
