package com.gs.repository.jpa.def;

import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DefMatchOrderRepository extends JpaRepository<DefMatchOrder, Long>, JpaSpecificationExecutor<DefMatchOrder> {

    DefMatchOrder findDefMatchOrderById(Long id);

    DefMatchOrder findDefMatchOrderByDefMatchManageAndOrderId(DefMatchManage defMatchManage,Long orderId);
}
