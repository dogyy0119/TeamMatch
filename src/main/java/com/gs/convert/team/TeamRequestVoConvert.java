package com.gs.convert.team;

import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.TeamRequest;
import com.gs.model.vo.team.TeamRequestVo;
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
public class TeamRequestVoConvert {
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
    public TeamRequestVo toVo(TeamRequest entity) {
        if (entity == null) {
            return null;
        } else {
            TeamRequestVo teamRequestVo = new TeamRequestVo();
            teamRequestVo.setId(entity.getId());
            teamRequestVo.setTeamId(entity.getTeamId());
            teamRequestVo.setFromId(entity.getFromId());
            teamRequestVo.setContent(entity.getContent());
            teamRequestVo.setType(entity.getType());
            teamRequestVo.setStatus(entity.getStatus());
            teamRequestVo.setCreateTime(sdf.format(entity.getCreateTime()));
            if (null != entity.getTeamId() && this.teamRepository.existsById(entity.getTeamId())) {
                teamRequestVo.setTeamName(this.teamRepository.findTeamById(entity.getTeamId()).getName());
            }

            if (null != entity.getFromId() && this.memberRepository.existsById(entity.getFromId())) {
                Member member = this.memberRepository.findMemberById(entity.getFromId());

                teamRequestVo.setFromName(member.getName());
                teamRequestVo.setFromAvatar(member.getAvatar());
            }

            return teamRequestVo;
        }
    }

    /**
     * Entity集合转DTO集合
     *
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    public List<TeamRequestVo> toVo(List<TeamRequest> entityList) {
        if (entityList == null) {
            return null;
        } else {
            List<TeamRequestVo> list = new ArrayList(entityList.size());
            Iterator var3 = entityList.iterator();

            while (var3.hasNext()) {
                TeamRequest teamRequest = (TeamRequest) var3.next();
                list.add(this.toVo(teamRequest));
            }

            return list;
        }
    }
}
