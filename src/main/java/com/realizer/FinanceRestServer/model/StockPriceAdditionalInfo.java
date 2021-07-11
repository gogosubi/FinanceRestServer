package com.realizer.FinanceRestServer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
	@GeneratedValue 
	private Long id;
	
	// 시가총액
	private long totalMarketPrice;
	
	// 52주고가
	private long yearHighPrice;
	
	// 52주저가
	private long yearLowPrice;
	
	// PER
	private double per;
	
	// EPS
	private long eps;
	
	// 추정PER
	private double guessPer;
	
	// 추정EPS
	private long guessEps;
	
	// PBR
	private long pbr;
	
	// BPS
	private long bps;
	
	// 배당수익률
	private double dyRate;
	
	@OneToOne 
	@JoinColumn(name = "PRICE_ID")
	private StockPrice stockPrice;
}
