package com.gs.convert.match.league;

import com.gs.model.dto.match.league.LeagueMatchWholeDTO;
import com.gs.model.entity.jpa.db1.match.league.LMWhole;
import com.gs.repository.jpa.league.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 战队实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 **/
//@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public class LeagueMatchWholeDtoConvert {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    LeagueRepository leagueRepository;
    /**
     * Entity转Vo
     * 实体
     *
     * @param leagueMatchWholeDTO league match dto
     * @return team request
     */
    public LMWhole toEntity(LeagueMatchWholeDTO leagueMatchWholeDTO) {
        if (leagueMatchWholeDTO == null) {
            return null;
        } else {
            LMWhole LMWhole = new LMWhole();
            LMWhole.setLeagueId(leagueMatchWholeDTO.getLeagueId());
            LMWhole.setMatchName(leagueMatchWholeDTO.getMatchName());

            try {
                LMWhole.setStartDate(sdf.parse(leagueMatchWholeDTO.getStartDate()));
                LMWhole.setEndDate(sdf.parse(leagueMatchWholeDTO.getEndDate()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            LMWhole.setMatchCost(leagueMatchWholeDTO.getMatchCost());
            LMWhole.setMatchMode(leagueMatchWholeDTO.getMatchMode());
            LMWhole.setTeamNum(leagueRepository.findLeagueById(LMWhole.getLeagueId()).getMaxTeamNum());
            LMWhole.setMatchNum(LMWhole.getMatchNum());

            return LMWhole;
        }
    }
}

