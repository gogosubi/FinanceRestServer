package com.realizer.FinanceRestServer.repository.dao.group;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.group.GroupStock;

public interface GroupStockRepository extends JpaRepository<GroupStock, Long> {

}
