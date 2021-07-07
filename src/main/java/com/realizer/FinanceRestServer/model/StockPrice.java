package com.realizer.FinanceRestServer.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

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

	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
	private Long id;
	
	// 기준일자
	private String bsDt;
	
	// 종목정보
	@ManyToOne
	@JoinColumn(name="item_id")
	private StockItem stockItem;
	
	// 현재가
	private long currentPrice;
	
	// 시가
	private long openPrice;
	
	// 고가
	private long highPrice;
	
	// 저가
	private long lowPrice;
	
	// 거래량
	private long amount;
	
	@OneToOne 
	@JoinColumn(name = "ADD_INFO_ID")
	private StockPriceAdditionalInfo stockPriceAdditionalInfo;
	
	@CreationTimestamp
	private Timestamp createDate;
}
