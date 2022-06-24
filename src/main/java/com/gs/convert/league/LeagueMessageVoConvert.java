package com.gs.convert.league;

import com.gs.model.entity.jpa.db1.league.LeagueMessage;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Message;
import com.gs.model.vo.league.LeagueMessageVo;
import com.gs.model.vo.team.MessageVo;
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
public class LeagueMessageVoConvert {
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
    public LeagueMessageVo toVo(LeagueMessage entity) {
        if (entity == null) {
            return null;
        } else {
            LeagueMessageVo messageVo = new LeagueMessageVo();
            messageVo.setId(entity.getId());
            messageVo.setLeagueId(entity.getLeagueId());
            messageVo.setFromId(entity.getFromId());
            messageVo.setToId(entity.getToId());
            messageVo.setType(entity.getType());
            messageVo.setContent(entity.getContent());
            messageVo.setCreateTime(sdf.format(entity.getCreateTime()));
            if (null != entity.getLeagueId() && this.leagueRepository.existsById(entity.getLeagueId())) {
                messageVo.setLeagueName(this.leagueRepository.findLeagueById(entity.getLeagueId()).getName());
            }

            if (null != entity.getFromId() && this.memberRepository.existsById(entity.getFromId())) {
                Member fromMember = this.memberRepository.findMemberById(entity.getFromId());
                messageVo.setFromName(fromMember.getName());
                messageVo.setFromAvatar(fromMember.getAvatar());
            }

            if (null != entity.getToId() && this.memberRepository.existsById(entity.getToId())) {
                Member toMember = this.memberRepository.findMemberById(entity.getToId());
                messageVo.setToName(toMember.getName());
                messageVo.setToAvatar(toMember.getAvatar());
            }

            return messageVo;
        }
    }

    /**
     * Entity集合转DTO集合
     *
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    public List<LeagueMessageVo> toVo(List<LeagueMessage> entityList) {
        if (entityList == null) {
            return null;
        } else {
            List<LeagueMessageVo> list = new ArrayList(entityList.size());
            Iterator var3 = entityList.iterator();

            while (var3.hasNext()) {
                LeagueMessage message = (LeagueMessage) var3.next();
                list.add(this.toVo(message));
            }

            return list;
        }
    }
}
