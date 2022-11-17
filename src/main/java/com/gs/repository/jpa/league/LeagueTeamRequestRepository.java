package com.gs.repository.jpa.league;

import com.gs.model.entity.jpa.db1.league.LeagueTeamRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LeagueTeamRequestRepository extends JpaRepository<LeagueTeamRequest, Long>, JpaSpecificationExecutor<LeagueTeamRequest> {

    boolean existsByLeagueIdAndToTeamIdAndTypeAndStatus(Long leagueId, Long toTeamId, Integer type, Integer status);
    List<LeagueTeamRequest> findLeagueRequestsByLeagueId(Long leagueId);

    List<LeagueTeamRequest> findLeagueTeamRequestsByToTeamId(Long toTeamId);
    LeagueTeamRequest findLeagueTeamRequestById(Long id);
}
