package com.gs.repository.jpa.def;

import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.def.PersonOrder;
import com.gs.model.entity.jpa.db1.team.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonOrderRepository extends JpaRepository<PersonOrder, Long>, JpaSpecificationExecutor<PersonOrder> {
    PersonOrder findPersonOrderByDefMatchOrderAndMember(DefMatchOrder defMatchOrder, Member member);
}
