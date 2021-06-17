package com.realizer.FinanceRestServer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class StockPrice {

	@Id
	// 기준일자
	private String bsDt;
	
	// 종목정보
	private StockItem stockItem;
	
	// 종가
	private long closePrice;
	
	// 시가
	private long openPrice;
	
	// 고가
	private long highPrice;
	
	// 저가
	private long lowPrice;
	
	// 거래량
	private long amount;
}
