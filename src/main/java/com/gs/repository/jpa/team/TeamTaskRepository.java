package com.gs.repository.jpa.team;

import com.gs.model.entity.jpa.db1.team.TeamTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TeamTaskRepository extends JpaRepository<TeamTask, Long>, JpaSpecificationExecutor<TeamTask> {
    Page<TeamTask> findAllByTeamId(Long teamId, Pageable pageable);
    List<TeamTask> findTeamTasksByTeamId(Long teamId);
    void deleteAllByTeamId(Long teamId);
}
