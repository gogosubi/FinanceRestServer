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
import com.realizer.FinanceRestServer.model.StockPriceAdditionalInfo;
import com.realizer.FinanceRestServer.repository.dao.StockItemRepository;
import com.realizer.FinanceRestServer.repository.dao.StockPriceAdditionalInfoRepository;
import com.realizer.FinanceRestServer.repository.dao.StockPriceRepository;
import com.realizer.FinanceRestServer.repository.service.StockSiseRepository;
import com.realizer.FinanceRestServer.util.Crawler;
import com.realizer.FinanceRestServer.util.TypeUtility;

@Service
public class NStockSiseService implements StockSiseRepository {
	private StockItemRepository stockItemRepository;
	
	private StockPriceRepository stockPriceRepository;
	
	private StockPriceAdditionalInfoRepository stockPriceAdditionalInfoRepository;
	
	@Value("${stock.sise.url}")
	private String stockSiseUrl;

	public NStockSiseService(StockItemRepository stockItemRepository, StockPriceRepository stockPriceRepository, StockPriceAdditionalInfoRepository stockPriceAdditionalInfoRepository) 
	{
		this.stockItemRepository = stockItemRepository;
		this.stockPriceRepository = stockPriceRepository;
		this.stockPriceAdditionalInfoRepository = stockPriceAdditionalInfoRepository;
	}
	
	public void saveStockSiseDetail(String code)
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
        stockPrice.setStockItem(stockItem);
        stockPriceRepository.save(stockPrice);
        
        // 종목시세추가정보
        StockPriceAdditionalInfo stockPriceAdditionalInfo = getStockPriceAdditionalInfo(document)
				.orElseThrow(()->new IllegalArgumentException("전달한 주소[" + stockSiseUrl + "에서 유효한 정보를 가져오지 못했습니다."));
        stockPriceAdditionalInfo.setStockPrice(stockPrice);
        stockPriceAdditionalInfoRepository.save(stockPriceAdditionalInfo);
	}
	
	private Optional<StockItem> getStockItem(Document document)
	{
        return Optional.of(
        			StockItem.builder()
        			.itemCode(document.select("#middle > div.h_company > div.wrap_company > div > span.code").text())
        			.itemName(document.select("#middle > div.h_company > div.wrap_company > h2").text())
        			.type(MarketType.valueOf(document.select("#middle > div.h_company > div.wrap_company > div > img").attr("class")))
        			.build()
        			);
	}
	
	private Optional<StockPrice> getStockPrice(Document document)
	{
		// StockPrice 생성
		return Optional.of(
					StockPrice.builder()
						.bsDt(document.select("#time > em").text().substring(0, 10).replaceAll("\\.",  ""))
						.currentPrice((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(2)").text()))
						.dayToDay((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(2) > td:nth-child(2)").text()))
						.amount((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(4) > td:nth-child(2)").text()))
						.openPrice((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(4) > td:nth-child(4)").text()))
						.highPrice((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(5) > td:nth-child(4)").text()))
						.lowPrice((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(6) > td:nth-child(4)").text()))
						.build()
					);	
	}
	
	private Optional<StockPriceAdditionalInfo> getStockPriceAdditionalInfo(Document document)
	{
		return Optional.of(
					StockPriceAdditionalInfo.builder()
						.totalMarketPrice(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(12) > td:nth-child(2)").text())
						.yearHighPrice(0)
						.yearLowPrice(0)
						.per(0)
						.eps(0)
						.guessPer(0)
						.guessEps(0)
						.pbr(0)
						.bps(0)
						.dyRate(0)
						.build()
					);
		// 시가총액		
		// 52주고가		
		// 52주저가		
		// PER		
		// EPS		
		// 추정PER		
		// 추정EPS		
		// PBR		
		// BPS		
		// 배당수익률
	}
}
