package com.gs.service.impl.league;


import com.gs.convert.team.TeamToVoConvert;
import com.gs.model.entity.jpa.db1.league.LeagueTeamRequest;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.vo.team.TeamVo;
import com.gs.repository.jpa.league.LeagueTeamRepository;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamMemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.league.LeagueTeamService;
import com.gs.service.intf.team.TeamMemberService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 战队成员 Service接口实现层
 * User: lys
 * DateTime: 2022-05-1
 **/
@Service
@AllArgsConstructor
public class LeagueTeamServiceImpl implements LeagueTeamService {

    private LeagueTeamRepository leagueTeamRepository;

    @Override
    public boolean existsByTeamId(Long teamId){
        return leagueTeamRepository.existsByTeamId(teamId);
    }

}
