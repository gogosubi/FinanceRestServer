package com.realizer.FinanceRestServer.model.item;

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
	private String totalMarketPrice;
	
	// 52주고가
	private long yearHighPrice;
	
	// 52주저가
	private long yearLowPrice;
	
	// PER
	private double per;
	
	// EPS
	private long eps;
	
	@OneToOne 
	@JoinColumn(name = "price_id")
	private StockPrice stockPrice;
}
