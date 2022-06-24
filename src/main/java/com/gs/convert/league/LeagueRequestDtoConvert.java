package com.gs.convert.league;

import com.gs.model.dto.league.LeagueRequestDTO;
import com.gs.model.dto.team.TeamRequestDTO;
import com.gs.model.entity.jpa.db1.league.LeagueRequest;
import com.gs.model.entity.jpa.db1.team.TeamRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 战队实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 **/
//@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public class LeagueRequestDtoConvert {
    /**
     * Entity转Vo
     * 实体
     *
     * @param leagueRequestDTO league request dto
     * @return team request
     */
    public LeagueRequest toEntity(LeagueRequestDTO leagueRequestDTO) {
        if (leagueRequestDTO == null) {
            return null;
        } else {
            LeagueRequest leagueRequest = new LeagueRequest();
            leagueRequest.setLeagueId(leagueRequestDTO.getLeagueId());
            leagueRequest.setFromMemberId(leagueRequestDTO.getFromMemberId());
            leagueRequest.setFromTeamId(leagueRequestDTO.getFromTeamId());
            leagueRequest.setType(leagueRequestDTO.getType());
            leagueRequest.setContent(leagueRequestDTO.getContent());
            leagueRequest.setStatus(1);
            leagueRequest.setCreateTime(new Date());
            return leagueRequest;
        }
    }
}

