package com.gs.repository.jpa.def;

import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.def.TeamOrder;
import com.gs.model.entity.jpa.db1.team.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TeamOrderRepository extends JpaRepository<TeamOrder, Long>, JpaSpecificationExecutor<TeamOrder> {
    List<TeamOrder> findTeamOrderByDefMatchOrderAndMember(DefMatchOrder defMatchOrder, Member member);

}
