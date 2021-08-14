package com.realizer.FinanceRestServer.model.group;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GroupItem {
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
	private Long id;
	
	// 종목코드
	private String groupCode;
	
	/* 업종명 */
	private String groupName;
	
	// 전일대비
	private double dayToDayRate;
	
    @OneToMany(mappedBy = "groupItem",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupStock> groupStockList;
}
