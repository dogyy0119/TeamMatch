package com.gs.convert.league;

import com.gs.model.dto.league.LeagueTeamRequestDTO;
import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.model.entity.jpa.db1.league.LeagueTeamRequest;
import com.gs.model.entity.jpa.db1.team.MemberRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 战队实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 **/
//@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public class LeagueTeamRequestDtoConvert {
    /**
     * Entity转Vo
     * 实体
     *
     * @param leagueTeamRequestDTO league team request dto
     * @return team request
     */
    public LeagueTeamRequest toEntity(LeagueTeamRequestDTO leagueTeamRequestDTO) {
        if (leagueTeamRequestDTO == null) {
            return null;
        } else {
            LeagueTeamRequest leagueTeamRequest = new LeagueTeamRequest();
            leagueTeamRequest.setLeagueId(leagueTeamRequestDTO.getLeagueId());
            leagueTeamRequest.setFromId(leagueTeamRequestDTO.getFromId());
            leagueTeamRequest.setToTeamId(leagueTeamRequestDTO.getToTeamId());
            leagueTeamRequest.setType(leagueTeamRequestDTO.getType());
            leagueTeamRequest.setStatus(1);
            leagueTeamRequest.setContent(leagueTeamRequestDTO.getContent());
            leagueTeamRequest.setCreateTime(new Date());
            return leagueTeamRequest;
        }
    }
}

