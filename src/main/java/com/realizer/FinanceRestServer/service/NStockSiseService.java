package com.realizer.FinanceRestServer.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.realizer.FinanceRestServer.model.group.GroupItem;
import com.realizer.FinanceRestServer.model.group.GroupPrice;
import com.realizer.FinanceRestServer.model.item.MarketType;
import com.realizer.FinanceRestServer.model.item.StockInvestInfo;
import com.realizer.FinanceRestServer.model.item.StockItem;
import com.realizer.FinanceRestServer.model.item.StockPrice;
import com.realizer.FinanceRestServer.model.item.StockPriceAdditionalInfo;
import com.realizer.FinanceRestServer.repository.dao.group.GroupItemRepository;
import com.realizer.FinanceRestServer.repository.dao.group.GroupPriceRepository;
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
	// 그룹정보
	private GroupItemRepository groupItemRepository;
	// 그룹가격정보
	private GroupPriceRepository groupPriceRepository;
	
	@Value("${stock.sise.url}")
	private String stockSiseUrl;
	
	@Value("${stock.sise.day_url}")
	private String stockSiseDayUrl;
	
	@Value("${stock.sise.upjong}")
	private String stockUpjongUrl;
		
    private SimpleDateFormat sdf;

	public NStockSiseService(StockItemRepository stockItemRepository
								, StockPriceRepository stockPriceRepository
								, StockPriceAdditionalInfoRepository stockPriceAdditionalInfoRepository
								, StockInvestInfoRepository stockInvestInfoRepository
								, GroupItemRepository groupItemRepository
								, GroupPriceRepository groupPriceRepository) 
	{
		this.stockItemRepository = stockItemRepository;
		this.stockPriceRepository = stockPriceRepository;
		this.stockPriceAdditionalInfoRepository = stockPriceAdditionalInfoRepository;
		this.stockInvestInfoRepository = stockInvestInfoRepository;
		this.groupItemRepository = groupItemRepository;
		this.groupPriceRepository = groupPriceRepository;
		this.sdf = new SimpleDateFormat("yyyyMMdd");
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
	
	@Override
	public void saveStockGroupPrice() {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(stockUpjongUrl);
		System.out.println(url);
		Document document = Crawler.getCrawlingSite(url.toString());

        // 종목정보가 처음인 경우 종목 정보 저장
        for ( GroupPrice groupPrice : getGroupPriceList(document) )
        {
            // 그룹정보가 처음인 경우 그룹정보 저장
            groupItemRepository.findBygroupNo(groupPrice.getGroupItem().getGroupNo())
                                .orElse(groupItemRepository.save(groupPrice.getGroupItem()));
        	
        	groupPriceRepository.save(groupPrice);
        }		
	}
	
	@Override
	public void saveStockGroupDetail() 
	{
		// TODO Auto-generated method stub
		for (GroupItem groupItem : groupItemRepository.findAll()) 
		{
			StringBuilder url = new StringBuilder("https://finance.naver.com/sise/sise_group_detail.nhn?type=upjong&no=");
			url.append(groupItem.getGroupNo());
			System.out.println(url);
			Document document = Crawler.getCrawlingSite(url.toString());

			for ( Element element : document.select("#contentarea > div:nth-child(5) > table > tbody > tr") )
			{
				if ( element.hasAttr("onMouseOver") )
				{
					// 여기 보완하자
					StockPrice stockPrice = getStockPrice(element, sdf.format(Calendar.getInstance().getTime()));
				}
			}
		}
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

	private ArrayList<GroupPrice> getGroupPriceList(Document document)
	{
		ArrayList<GroupPrice> groupPriceList = new ArrayList<>();
		
		for ( Element element : document.select("#contentarea_left > table > tbody > tr") )
		{
			Element tdElement = element.select("td:not([colspan])").first();
			if ( tdElement == null)
			{
				continue;
			}
			else
			{
				String subUrl = tdElement.select("a").first().attr("href").toString();
				
				GroupItem groupItem = GroupItem.builder()
												.groupNo(Long.parseLong(subUrl.substring(subUrl.lastIndexOf("&no=") + 4)))
												.groupName(tdElement.text())
												.build();
				GroupPrice groupPrice = GroupPrice.builder()
												.bsDt(sdf.format(Calendar.getInstance().getTime()))
												.groupItem(groupItem)
												.dayToDay((double) TypeUtility.convertType(element.select("[class^=tah p11]").text()))
												.build();
				
				groupPriceList.add(groupPrice);
				
			}
		}
		
		return groupPriceList;
	}
	
	private ArrayList<StockPrice> getPeriodStockPrice(String url, String st_dt, String nd_dt)
	{	
		ArrayList<StockPrice> stockPriceList = new ArrayList<>();
		
		int page = 1;
		boolean searchFlag = true;
		
		while ( searchFlag )
		{
			StringBuilder sb = new StringBuilder(url);
			sb.append(page);
			System.out.println(sb.toString());
			
			Document document = Crawler.getCrawlingSite(sb.toString());
			
			for ( Element element : document.select("body > table.type2 > tbody > tr") )
			{
				if ( element.hasAttr("onMouseOver") )
				{
					System.out.println("내가 원하는 곳");
					String bs_dt = element.select("td:nth-child(1) > span").text().substring(0, 10).replaceAll("\\.",  "");
					System.out.println(bs_dt);
					
					if ( bs_dt.compareTo(st_dt) < 0 )
					{
						searchFlag = false;
						break;
					}
					else if ( bs_dt.compareTo(nd_dt) <= 0 )
					{
						stockPriceList.add(getStockPrice(element, bs_dt));
					}
				}
			}
			
			page += 1;
			System.out.println("Page출력 : " + page);
		}
		
		return stockPriceList;
	}
	
	private StockPrice getStockPrice(Element element, String bs_dt)
	{
		return 
				StockPrice.builder()
				.bsDt(bs_dt)
				.currentPrice((long) TypeUtility.convertType(element.select("td:nth-child(2)").text()))
				.dayToDay((long) TypeUtility.convertType(element.select("td:nth-child(3)").text()))
				.amount((long) TypeUtility.convertType(element.select("td:nth-child(7)").text()))
				.openPrice((long) TypeUtility.convertType(element.select("td:nth-child(4)").text()))
				.highPrice((long) TypeUtility.convertType(element.select("td:nth-child(5)").text()))
				.lowPrice((long) TypeUtility.convertType(element.select("td:nth-child(6)").text()))
				.build();
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
