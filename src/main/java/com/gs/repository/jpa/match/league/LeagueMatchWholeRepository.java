package com.gs.repository.jpa.match.league;

import com.gs.model.entity.jpa.db1.match.league.LMWhole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LeagueMatchWholeRepository extends JpaRepository<LMWhole, Long>, JpaSpecificationExecutor<LMWhole> {

}
