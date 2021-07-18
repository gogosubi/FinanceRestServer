package com.realizer.FinanceRestServer.model.item;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class StockInvestInfo {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 
	private Long id;
	
	// 시가총액순위
	private long rank;
	
	// 외국인소진율
	private double foreignBurnRate;
	
	// 추정PER
	private double guessPer;
	
	// 추정EPS
	private long guessEps;
	
	// PBR
	private double pbr;
	
	// BPS
	private long bps;
	
	// 배당수익률
	private double dyRate;
	
	@OneToOne 
	@JoinColumn(name = "PRICE_ID")
	private StockPrice stockPrice;
}
