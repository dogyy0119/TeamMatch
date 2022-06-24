package com.gs.service.intf.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.league.LeagueTeamRequestDTO;
import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.model.vo.league.LeagueRequestVo;
import com.gs.model.vo.league.LeagueTeamRequestVo;
import com.gs.model.vo.team.MemberRequestVo;

import java.util.List;

public interface LeagueTeamRequestService {

    CodeEnum sendLeagueTeamRequest(LeagueTeamRequestDTO leagueTeamRequestDTO);

    List<LeagueTeamRequestVo> getLeagueTeamRequests(Long teamId, Integer pageNum, Integer pageSize);

    CodeEnum deleteLeagueTeamRequest(Long id);

    CodeEnum deleteAllLeagueTeamRequest(Long teamId);


    /**
     * 删除所有已读请求
     *
     * @param teamId 战队ID
     * @return CodeEnum.IS_SUCCESS
     */
    CodeEnum deleteAllDoneLeagueTeamRequest(Long teamId);
}
