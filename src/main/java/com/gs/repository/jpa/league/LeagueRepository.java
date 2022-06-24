package com.gs.repository.jpa.league;

import com.gs.model.entity.jpa.db1.league.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LeagueRepository extends JpaRepository<League, Long>, JpaSpecificationExecutor<League> {

    boolean existsByCreateMemberId(Long memberId);

    League findLeagueById(Long id);

    League findLeagueByCreateMemberId(Long createMemberId);
}
