package com.gs.repository.jpa.league;

import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.entity.jpa.db1.league.LeagueTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LeagueTeamRepository extends JpaRepository<LeagueTeam, Long>, JpaSpecificationExecutor<LeagueTeam> {
    LeagueTeam findLeagueTeamByLeagueIdAndTeamId(Long leagueId, Long teamId);
    List<LeagueTeam> findLeagueTeamsByTeamId(Long teamId);
    Boolean existsByLeagueIdAndTeamId(Long leagueId, Long teamId);
}
