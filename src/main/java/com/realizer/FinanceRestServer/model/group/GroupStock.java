package com.realizer.FinanceRestServer.model.group;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.realizer.FinanceRestServer.model.item.StockPrice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GroupStock {

	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
	private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
	private GroupItem groupItem;

    @ManyToOne
    @JoinColumn(name = "price_id")
	private StockPrice stockPrice;	
}
