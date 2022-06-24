package com.gs.convert.team;

import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.vo.team.TeamVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 战队实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 *
 **/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract  class TeamToVoConvert {
    @Autowired
    protected TeamMemberToVoConvert teamMemberToVoConvert;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Entity转Vo
     * @param entity 对应数据库的实体
     * @return 输出Vo
     */
    @Mapping(target = "teamMembers", expression = "java(teamMemberToVoConvert.toVo(entity.getTeamMembers()))")
    @Mapping(target = "createTime", expression = "java(sdf.format(entity.getCreateTime()))")
    public abstract TeamVo toVo(Team entity);

    public abstract List<TeamVo> toVo(List<Team> entityLst);
}
