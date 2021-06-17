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
public class StockPriceAdditionalInfo {
	@Id
	// 주식가격
	private StockPrice stockPrice;
	
	// 시가총액
	private long totalMarketPrice;
	
	// 52주고가
	private long year_high_price;
	
	// 52주저가
	private long year_low_price;
	
	// PER
}
