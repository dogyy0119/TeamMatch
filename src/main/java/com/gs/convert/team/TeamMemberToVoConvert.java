package com.gs.convert.team;

import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.vo.team.TeamMemberVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;
/**
 * 战队成员实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 *
 **/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TeamMemberToVoConvert {

    @Autowired
    protected MemberToVoConvert memberToVoConvert;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Entity转Vo
     * @param entity 对应数据库的实体
     * @return 输出Vo
     */
    @Mapping(target = "member", expression = "java(memberToVoConvert.toVo(entity.getMember()))")
    @Mapping(target = "joinTime", expression = "java(sdf.format(entity.getJoinTime()))")
    public abstract TeamMemberVo toVo(TeamMember entity);

    /**
     * Entity集合转DTO集合
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    public abstract List<TeamMemberVo> toVo(List<TeamMember> entityList);
}
