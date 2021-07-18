package com.realizer.FinanceRestServer.model.item;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.realizer.FinanceRestServer.model.group.GroupItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StockItem 
{
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
	private Long id;
	
	// 종목코드
	private String itemCode;
	
	// 종목명
	private String itemName;
	
	// 시장구분
	@Enumerated(EnumType.STRING)
	private MarketType type;
	
	@CreationTimestamp
	private Timestamp createDate;
}
