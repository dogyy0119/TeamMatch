package com.gs.repository.jpa.league;

import com.gs.model.entity.jpa.db1.league.LeagueTask;
import com.gs.model.entity.jpa.db1.team.TeamTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LeagueTaskRepository extends JpaRepository<LeagueTask, Long>, JpaSpecificationExecutor<LeagueTask> {
    Page<LeagueTask> findAllByLeagueId(Long leagueId, Pageable pageable);
    List<LeagueTask> findLeagueTasksByLeagueId(Long leagueId);
    void deleteAllByLeagueId(Long leagueId);
}
