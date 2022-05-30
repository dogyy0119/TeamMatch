package com.gs.repository.jpa.team;

import com.gs.model.entity.jpa.db1.team.TeamRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long>, JpaSpecificationExecutor<TeamRequest> {

    boolean existsByTeamIdAndFromIdAndType(Long teamId, Long fromId, Integer type);
    List<TeamRequest> findTeamRequestsByTeamId(Long teamId);

    TeamRequest findTeamRequestById(Long id);
}
