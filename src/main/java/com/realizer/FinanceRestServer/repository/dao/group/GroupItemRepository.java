package com.realizer.FinanceRestServer.repository.dao.group;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.group.GroupItem;
import com.realizer.FinanceRestServer.model.item.StockItem;

public interface GroupItemRepository extends JpaRepository<GroupItem, Long> {
    Optional<GroupItem> findBygroupNo(Long no);
}
