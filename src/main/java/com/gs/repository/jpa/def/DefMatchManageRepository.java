package com.gs.repository.jpa.def;

import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DefMatchManageRepository extends JpaRepository<DefMatchManage, Long>, JpaSpecificationExecutor<DefMatchManage> {

    DefMatchManage findDefMatchManageByDefMatch(DefMatch defMatch);

    DefMatchManage findDefMatchManageByMemberIdAndDefMatchId(Long memberId, Long defMatchId);
}
