package com.gs.convert.team;

import com.gs.model.dto.team.TeamRequestDTO;
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
public class TeamRequestDtoConvert {
    /**
     * Entity转Vo
     * 实体
     *
     * @param teamRequestDTO team request dto
     * @return team request
     */
    public TeamRequest toEntity(TeamRequestDTO teamRequestDTO) {
        if (teamRequestDTO == null) {
            return null;
        } else {
            TeamRequest teamRequest = new TeamRequest();
            teamRequest.setTeamId(teamRequestDTO.getTeamId());
            teamRequest.setFromId(teamRequestDTO.getFromId());
            teamRequest.setType(teamRequestDTO.getType());
            teamRequest.setContent(teamRequestDTO.getContent());
            teamRequest.setStatus(1);
            teamRequest.setCreateTime(new Date());
            return teamRequest;
        }
    }
}

