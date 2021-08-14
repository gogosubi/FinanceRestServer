package com.realizer.FinanceRestServer.model.group;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GroupPrice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 기준일자
	private String bsDt;
	
	// 종목정보
	@ManyToOne
	@JoinColumn(name="group_id")
	private GroupItem groupItem;
	
	// 전일대비
	private double dayToDay;
	
	@CreationTimestamp
	private Timestamp createDate;
}
