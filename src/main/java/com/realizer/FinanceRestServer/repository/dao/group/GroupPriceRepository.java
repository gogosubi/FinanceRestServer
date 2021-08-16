package com.realizer.FinanceRestServer.repository.dao.group;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.group.GroupItem;
import com.realizer.FinanceRestServer.model.group.GroupPrice;

public interface GroupPriceRepository extends JpaRepository<GroupPrice, Long> {

}
