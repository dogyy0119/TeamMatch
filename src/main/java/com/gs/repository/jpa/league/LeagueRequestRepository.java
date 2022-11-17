package com.gs.repository.jpa.league;

import com.gs.model.entity.jpa.db1.league.LeagueRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LeagueRequestRepository extends JpaRepository<LeagueRequest, Long>, JpaSpecificationExecutor<LeagueRequest> {
    boolean existsByLeagueIdAndFromTeamIdAndTypeAndStatus(Long leagueId, Long fromTeamId, Integer type, Integer status);
    List<LeagueRequest> findLeagueRequestsByLeagueId(Long leagueId);

    List<LeagueRequest> findLeagueRequestsByFromTeamId(Long fromTeamId);
    LeagueRequest findLeagueRequestById(Long id);
}
