package com.gs.convert.league;

import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.entity.jpa.db1.league.LeagueTeamRequest;
import com.gs.model.entity.jpa.db1.team.MemberRequest;
import com.gs.model.vo.league.LeagueTeamRequestVo;
import com.gs.model.vo.team.MemberRequestVo;
import com.gs.repository.jpa.league.LeagueRepository;
import com.gs.repository.jpa.league.LeagueTeamRequestRepository;
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
public class LeagueTeamRequestVoConvert {

    @Autowired
    protected LeagueRepository leagueRepository;

    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected TeamRepository teamRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Entity转Vo
     *
     * @param entity 对应数据库的实体
     * @return 输出Vo
     */
    public LeagueTeamRequestVo toVo(LeagueTeamRequest entity) {
        if (entity == null) {
            return null;
        } else {
            LeagueTeamRequestVo leagueTeamRequestVo = new LeagueTeamRequestVo();
            leagueTeamRequestVo.setId(entity.getId());
            leagueTeamRequestVo.setLeagueId(entity.getLeagueId());
            leagueTeamRequestVo.setFromId(entity.getFromId());
            leagueTeamRequestVo.setToTeamId(entity.getToTeamId());
            leagueTeamRequestVo.setType(entity.getType());
            leagueTeamRequestVo.setStatus(entity.getStatus());
            leagueTeamRequestVo.setContent(entity.getContent());
            leagueTeamRequestVo.setCreateTime(sdf.format(entity.getCreateTime()));
            if (null != entity.getToTeamId() && this.teamRepository.existsById(entity.getToTeamId())) {
                leagueTeamRequestVo.setToTeamName(this.teamRepository.findTeamById(entity.getToTeamId()).getName());
            }

            if (null != entity.getFromId() && this.memberRepository.existsById(entity.getFromId())) {
                leagueTeamRequestVo.setFromName(this.memberRepository.findMemberById(entity.getFromId()).getName());
            }

            if (null != entity.getLeagueId() && this.leagueRepository.existsById(entity.getLeagueId())) {
                leagueTeamRequestVo.setLeagueName(this.leagueRepository.findLeagueById(entity.getLeagueId()).getName());
            }

            return leagueTeamRequestVo;
        }
    }

    /**
     * Entity集合转DTO集合
     *
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    public List<LeagueTeamRequestVo> toVo(List<LeagueTeamRequest> entityList) {
        if (entityList == null) {
            return null;
        } else {
            List<LeagueTeamRequestVo> list = new ArrayList(entityList.size());
            Iterator var3 = entityList.iterator();

            while (var3.hasNext()) {
                LeagueTeamRequest leagueTeamRequest = (LeagueTeamRequest) var3.next();
                list.add(this.toVo(leagueTeamRequest));
            }

            return list;
        }
    }
}
