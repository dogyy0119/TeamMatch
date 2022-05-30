package com.gs.convert.team;

import com.gs.model.entity.jpa.db1.team.MemberRequest;
import com.gs.model.vo.team.MemberRequestVo;
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
public class MemberRequestVoConvert {
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
    public MemberRequestVo toVo(MemberRequest entity) {
        if (entity == null) {
            return null;
        } else {
            MemberRequestVo memberRequestVo = new MemberRequestVo();
            memberRequestVo.setId(entity.getId());
            memberRequestVo.setTeamId(entity.getTeamId());
            memberRequestVo.setFromId(entity.getFromId());
            memberRequestVo.setToId(entity.getToId());
            memberRequestVo.setType(entity.getType());
            memberRequestVo.setStatus(entity.getStatus());
            memberRequestVo.setContent(entity.getContent());
            memberRequestVo.setCreateTime(sdf.format(entity.getCreateTime()));
            if (null != entity.getTeamId() && this.teamRepository.existsById(entity.getTeamId())) {
                memberRequestVo.setTeamName(this.teamRepository.findTeamById(entity.getTeamId()).getName());
            }

            if (null != entity.getFromId() && this.memberRepository.existsById(entity.getFromId())) {
                memberRequestVo.setFromName(this.memberRepository.findMemberById(entity.getFromId()).getName());
            }

            if (null != entity.getToId() && this.memberRepository.existsById(entity.getToId())) {
                memberRequestVo.setToName(this.memberRepository.findMemberById(entity.getToId()).getName());
            }

            return memberRequestVo;
        }
    }

    /**
     * Entity集合转DTO集合
     *
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    public List<MemberRequestVo> toVo(List<MemberRequest> entityList) {
        if (entityList == null) {
            return null;
        } else {
            List<MemberRequestVo> list = new ArrayList(entityList.size());
            Iterator var3 = entityList.iterator();

            while (var3.hasNext()) {
                MemberRequest memberRequest = (MemberRequest) var3.next();
                list.add(this.toVo(memberRequest));
            }

            return list;
        }
    }
}
