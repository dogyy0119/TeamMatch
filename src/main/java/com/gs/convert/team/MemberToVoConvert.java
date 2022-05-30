package com.gs.convert.team;

import com.gs.convert.EntityVoConvertBase;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.vo.team.MemberVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
 * 成员实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 *
 **/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberToVoConvert extends EntityVoConvertBase<MemberVo, Member> {

}
