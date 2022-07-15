package com.gs.repository.jpa.team;

import com.gs.model.entity.jpa.db1.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 战队Dao层
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    boolean existsByCreateMemberId(Long createMemberId);

    Team findTeamById(Long id);

    Boolean existsTeamById(Long id);

}