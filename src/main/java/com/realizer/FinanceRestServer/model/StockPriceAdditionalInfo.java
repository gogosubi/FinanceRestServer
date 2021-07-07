package com.realizer.FinanceRestServer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
	@GeneratedValue 
	private Long id;
	
	// 시가총액
	private long totalMarketPrice;
	
	// 52주고가
	private long year_high_price;
	
	// 52주저가
	private long year_low_price;
	
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
}
