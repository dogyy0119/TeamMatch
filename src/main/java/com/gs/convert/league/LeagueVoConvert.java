package com.gs.convert.league;


import com.gs.convert.team.TeamToVoConvert;
import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.entity.jpa.db1.league.LeagueTeam;
import com.gs.model.vo.league.LeagueVo;
import com.gs.model.vo.team.TeamVo;
import com.gs.repository.jpa.league.LeagueRepository;
import com.gs.repository.jpa.team.TeamRepository;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class LeagueVoConvert {

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TeamToVoConvert teamToVoConvert;

    @Autowired
    private TeamRepository teamRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LeagueVo toVo(League entity) {
        if (entity == null) {
            return null;
        }

        LeagueVo leagueVo = new LeagueVo();

        leagueVo.setId(entity.getId());
        leagueVo.setName(entity.getName());
        leagueVo.setCreateMemberId(entity.getCreateMemberId());
        leagueVo.setMaxMemberNum(entity.getMaxMemberNum());
        leagueVo.setLogoUrl(entity.getLogoUrl());
        leagueVo.setDescriptionInfo(entity.getDescriptionInfo());
        List<LeagueTeam> leagueTeamList = entity.getLeagueTeams();

        if (leagueTeamList != null && !leagueTeamList.isEmpty()) {
            List<TeamVo> teamVoList = new ArrayList<>();

            for (LeagueTeam leagueTeam :leagueTeamList){
                teamVoList.add(teamToVoConvert.toVo(teamRepository.findTeamById(leagueTeam.getTeamId())));
            }

            leagueVo.setTeamVoLst(teamVoList);
        }

        leagueVo.setCreateTime(sdf.format(entity.getCreateTime()));

        return leagueVo;
    }

    public List<LeagueVo> toVo(List<League> entityList) {
        if (entityList == null) {
            return null;
        }

        List<LeagueVo> list = new ArrayList<LeagueVo>(entityList.size());
        for (League league : entityList) {
            list.add(toVo(league));
        }

        return list;
    }
}
