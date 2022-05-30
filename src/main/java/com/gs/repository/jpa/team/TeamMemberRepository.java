package com.gs.repository.jpa.team;

import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 用户Dao层
 */
@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, JpaSpecificationExecutor<TeamMember> {
    TeamMember findTeamMemberByMemberAndTeam(Member member, Team team);
}