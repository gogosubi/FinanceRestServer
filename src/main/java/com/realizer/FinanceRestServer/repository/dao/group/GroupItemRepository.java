package com.realizer.FinanceRestServer.repository.dao.group;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realizer.FinanceRestServer.model.group.GroupItem;

public interface GroupItemRepository extends JpaRepository<GroupItem, Long> {

}
