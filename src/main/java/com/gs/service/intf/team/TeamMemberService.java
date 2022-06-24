package com.gs.service.intf.team;


import com.gs.constant.enums.MemberJobEnum;
import com.gs.model.vo.team.TeamVo;

import java.util.List;

/**
 * 战队成员 Service接口层
 * User: lys
 * DateTime: 2022-05-1
 **/
public interface TeamMemberService {
    Boolean isMemberInTeam(Long memberId, Long teamId);
    Integer getMemberJobInTeam(Long memberId, Long teamId);
    List<TeamVo> getTeamPageByPlayerId(
            Long memberId,
            Integer pageNum,
            Integer pageSize);
}
