package com.gs.service.intf.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.team.TeamRequestDTO;
import com.gs.model.vo.team.TeamRequestVo;

import java.util.List;

public interface TeamRequestService {

    CodeEnum sendTeamRequest(TeamRequestDTO teamRequest);

    List<TeamRequestVo> getTeamRequestLst(Long teamId, Long memberId, Integer pageNum, Integer pageSize);

    CodeEnum deleteTeamRequest(Long id, Long memberId);

    CodeEnum deleteAllTeamRequest(Long teamId, Long memberId);


    /**
     * 删除所有已读请求
     *
     * @param teamId 战队ID
     * @return CodeEnum.IS_SUCCESS
     */
    CodeEnum deleteAllDoneTeamRequest(Long teamId, Long memberId);
}
