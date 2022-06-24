package com.gs.convert.league;

import com.gs.model.entity.jpa.db1.league.LeagueRequest;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamRequest;
import com.gs.model.vo.league.LeagueRequestVo;
import com.gs.model.vo.team.TeamRequestVo;
import com.gs.repository.jpa.league.LeagueRepository;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 消息实体类转Vo工具类
 * User: lys
 * DateTime: 2022-05-10
 **/
//@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public class LeagueRequestVoConvert {
    @Autowired
    protected LeagueRepository leagueRepository;
    @Autowired
    protected TeamRepository teamRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Entity转Vo
     *
     * @param entity 对应数据库的实体
     * @return 输出Vo
     */
    public LeagueRequestVo toVo(LeagueRequest entity) {
        if (entity == null) {
            return null;
        } else {
            LeagueRequestVo leagueRequestVo = new LeagueRequestVo();
            leagueRequestVo.setId(entity.getId());
            leagueRequestVo.setLeagueId(entity.getLeagueId());
            leagueRequestVo.setFromMemberId(entity.getFromMemberId());
            leagueRequestVo.setFromTeamId(entity.getFromTeamId());
            leagueRequestVo.setContent(entity.getContent());
            leagueRequestVo.setType(entity.getType());
            leagueRequestVo.setStatus(entity.getStatus());
            leagueRequestVo.setCreateTime(sdf.format(entity.getCreateTime()));
            if (null != entity.getLeagueId() && this.leagueRepository.existsById(entity.getLeagueId())) {
                leagueRequestVo.setLeagueName(this.leagueRepository.findLeagueById(entity.getLeagueId()).getName());
            }

            if (null != entity.getFromTeamId() && this.teamRepository.existsById(entity.getFromTeamId())) {
                Team team = this.teamRepository.findTeamById(entity.getFromTeamId());

                leagueRequestVo.setFromTeamName(team.getName());
                leagueRequestVo.setFromTeamAvatar(team.getLogoUrl());
            }

            return leagueRequestVo;
        }
    }

    /**
     * Entity集合转DTO集合
     *
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    public List<LeagueRequestVo> toVo(List<LeagueRequest> entityList) {
        if (entityList == null) {
            return null;
        } else {
            List<LeagueRequestVo> list = new ArrayList(entityList.size());
            Iterator var3 = entityList.iterator();

            while (var3.hasNext()) {
                LeagueRequest leagueRequest = (LeagueRequest) var3.next();
                list.add(this.toVo(leagueRequest));
            }

            return list;
        }
    }
}
