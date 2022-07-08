package com.gs.convert.match.league;

import com.gs.model.entity.jpa.db1.match.league.LMWhole;
import com.gs.model.vo.match.league.LeagueMatchWholeVo;
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
@Component
//@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class LeagueMatchWholeVoConvert {
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected TeamRepository teamRepository;
    @Autowired
    protected LeagueRepository leagueRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Entity转Vo
     *
     * @param entity 对应数据库的实体
     * @return 输出Vo
     */
    public LeagueMatchWholeVo toVo(LMWhole entity) {
        if (entity == null) {
            return null;
        } else {
            LeagueMatchWholeVo leagueMatchWholeVo = new LeagueMatchWholeVo();
            leagueMatchWholeVo.setId(entity.getId());
            leagueMatchWholeVo.setLeagueId(entity.getLeagueId());
            leagueMatchWholeVo.setMatchName(entity.getMatchName());
            leagueMatchWholeVo.setStartDate(sdf.format(entity.getStartDate()));
            leagueMatchWholeVo.setEndDate(sdf.format(entity.getEndDate()));
            leagueMatchWholeVo.setMatchCost(entity.getMatchCost());
            leagueMatchWholeVo.setTeamNum(entity.getTeamNum());
            leagueMatchWholeVo.setMatchMode(entity.getMatchMode());
            leagueMatchWholeVo.setMatchNum(entity.getMatchNum());

            return leagueMatchWholeVo;
        }
    }

    /**
     * Entity集合转DTO集合
     *
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    public List<LeagueMatchWholeVo> toVo(List<LMWhole> entityList) {
        if (entityList == null) {
            return null;
        } else {
            List<LeagueMatchWholeVo> list = new ArrayList(entityList.size());
            Iterator var3 = entityList.iterator();

            while (var3.hasNext()) {
                LMWhole LMWhole = (LMWhole) var3.next();
                list.add(this.toVo(LMWhole));
            }

            return list;
        }
    }
}
