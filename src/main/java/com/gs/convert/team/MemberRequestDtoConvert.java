package com.gs.convert.team;

import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.model.entity.jpa.db1.team.MemberRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 战队实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 **/
//@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public class MemberRequestDtoConvert {
    /**
     * Entity转Vo
     * 实体
     *
     * @param memberRequestDTO team request dto
     * @return team request
     */
    public MemberRequest toEntity(MemberRequestDTO memberRequestDTO) {
        if (memberRequestDTO == null) {
            return null;
        } else {
            MemberRequest memberRequest = new MemberRequest();
            memberRequest.setTeamId(memberRequestDTO.getTeamId());
            memberRequest.setFromId(memberRequestDTO.getFromId());
            memberRequest.setToId(memberRequestDTO.getToId());
            memberRequest.setType(memberRequestDTO.getType());
            memberRequest.setStatus(1);
            memberRequest.setContent(memberRequest.getContent());
            memberRequest.setCreateTime(new Date());
            return memberRequest;
        }
    }
}

