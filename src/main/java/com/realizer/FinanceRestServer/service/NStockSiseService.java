package com.realizer.FinanceRestServer.service;

import java.util.ArrayList;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.realizer.FinanceRestServer.model.item.MarketType;
import com.realizer.FinanceRestServer.model.item.StockInvestInfo;
import com.realizer.FinanceRestServer.model.item.StockItem;
import com.realizer.FinanceRestServer.model.item.StockPrice;
import com.realizer.FinanceRestServer.model.item.StockPriceAdditionalInfo;
import com.realizer.FinanceRestServer.repository.dao.item.StockInvestInfoRepository;
import com.realizer.FinanceRestServer.repository.dao.item.StockItemRepository;
import com.realizer.FinanceRestServer.repository.dao.item.StockPriceAdditionalInfoRepository;
import com.realizer.FinanceRestServer.repository.dao.item.StockPriceRepository;
import com.realizer.FinanceRestServer.repository.service.StockSiseRepository;
import com.realizer.FinanceRestServer.util.Crawler;
import com.realizer.FinanceRestServer.util.TypeUtility;

@Service
public class NStockSiseService implements StockSiseRepository {
	// 종목정보
	private StockItemRepository stockItemRepository;
	// 가격정보
	private StockPriceRepository stockPriceRepository;
	// 추가정보
	private StockPriceAdditionalInfoRepository stockPriceAdditionalInfoRepository;
	// 투자정보
	private StockInvestInfoRepository stockInvestInfoRepository;
	
	@Value("${stock.sise.url}")
	private String stockSiseUrl;
	
	@Value("${stock.sise.day_url}")
	private String stockSiseDayUrl;

	public NStockSiseService(StockItemRepository stockItemRepository, StockPriceRepository stockPriceRepository, StockPriceAdditionalInfoRepository stockPriceAdditionalInfoRepository, StockInvestInfoRepository stockInvestInfoRepository) 
	{
		this.stockItemRepository = stockItemRepository;
		this.stockPriceRepository = stockPriceRepository;
		this.stockPriceAdditionalInfoRepository = stockPriceAdditionalInfoRepository;
		this.stockInvestInfoRepository = stockInvestInfoRepository;
	}
	
	public void savePeriodStockPrice(String st_dt, String nd_dt, String code)
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
        
        url = new StringBuilder(stockSiseDayUrl);
        url.append(code);
        url.append("&page=");
        
        // 종목시세정보
        for ( StockPrice stockPrice : getPeriodStockPrice(url.toString(), st_dt, nd_dt) )
        {
        	stockPrice.setStockItem(stockItem);
            stockPriceRepository.save(stockPrice);
        }		
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
        
        // 종목투자정보
        StockInvestInfo stockInvestInfo = getStockInvestInfo(document)
				.orElseThrow(()->new IllegalArgumentException("전달한 주소[" + stockSiseUrl + "에서 유효한 정보를 가져오지 못했습니다."));
        stockInvestInfo.setStockPrice(stockPrice);
        stockInvestInfoRepository.save(stockInvestInfo);
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
	
	private ArrayList<StockPrice> getPeriodStockPrice(String url, String st_dt, String nd_dt)
	{	
		ArrayList<StockPrice> stockPriceList = new ArrayList<>();
		System.out.println("들어오다.");
		int page = 1;
		boolean searchFlag = true;
		
		while ( searchFlag )
		{
			StringBuilder sb = new StringBuilder(url);
			sb.append(page);
			System.out.println(sb.toString());
			
			Document document = Crawler.getCrawlingSite(sb.toString());
			
			for ( Element element : document.select("body > table.type2 > tbody") )
			{
				System.out.println("테이블");
				int trCount = 0;
				
				for ( Element trElement : element.select("tr") )
				{
					trCount++;
					System.out.println("TR COUNT : " + trCount);
					
					System.out.println(trElement.text());
					if ( trElement.hasAttr("onMouseOver") )
					{
						System.out.println("내가 원하는 곳");
						String bs_dt = trElement.select("td:nth-child(1) > span").text().substring(0, 10).replaceAll("\\.",  "");
						System.out.println(bs_dt);
						
						if ( bs_dt.compareTo(st_dt) < 0 )
						{
							searchFlag = false;
							break;
						}
						else if ( bs_dt.compareTo(nd_dt) <= 0 )
						{
							stockPriceList.add(
									StockPrice.builder()
										.bsDt(bs_dt)
										.currentPrice((long) TypeUtility.convertType(trElement.select("td:nth-child(2)").text()))
										.dayToDay((long) TypeUtility.convertType(trElement.select("td:nth-child(3)").text()))
										.amount((long) TypeUtility.convertType(trElement.select("td:nth-child(7)").text()))
										.openPrice((long) TypeUtility.convertType(trElement.select("td:nth-child(4)").text()))
										.highPrice((long) TypeUtility.convertType(trElement.select("td:nth-child(5)").text()))
										.lowPrice((long) TypeUtility.convertType(trElement.select("td:nth-child(6)").text()))
										.build()
									);
						}
					}
				}
			}
			
			page += 1;
			System.out.println("Page출력 : " + page);
		}
		
		return stockPriceList;
	}
	
	private Optional<StockPriceAdditionalInfo> getStockPriceAdditionalInfo(Document document)
	{
		return Optional.of(
					StockPriceAdditionalInfo.builder()
						.totalMarketPrice(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(12) > td:nth-child(2)").text())
						.yearHighPrice((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(11) > td:nth-child(2)").text()))
						.yearLowPrice((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(11) > td:nth-child(4)").text()))
						.per((double) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(10) > td:nth-child(2)").text()))
						.eps((long) TypeUtility.convertType(document.select("#content > div.section.inner_sub > div:nth-child(1) > table > tbody > tr:nth-child(10) > td:nth-child(4)").text()))
						.build()
					);
	}
	
	private Optional<StockInvestInfo> getStockInvestInfo(Document document)
	{
		return Optional.of(
					StockInvestInfo.builder()
						.rank((long) TypeUtility.convertType(document.select("#tab_con1 > div.first > table > tbody > tr:nth-child(2) > td > em").text()))
						.foreignBurnRate((double) TypeUtility.convertType(document.select("#tab_con1 > div:nth-child(3) > table > tbody > tr.strong > td > em").text()))
						.guessPer((double) TypeUtility.convertType(document.select("#_cns_per").text()))
						.guessEps((long) TypeUtility.convertType(document.select("#_cns_eps").text()))
						.pbr((double) TypeUtility.convertType(document.select("#_pbr").text()))
						.bps((long) TypeUtility.convertType(document.select("#tab_con1 > div:nth-child(5) > table > tbody:nth-child(3) > tr:nth-child(2) > td > em:nth-child(3)").text()))
						.dyRate((double) TypeUtility.convertType(document.select("#_dvr").text()))
						.build()
					);
	}
}
