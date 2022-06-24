package com.gs.service.intf.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.league.LeagueRequestDTO;
import com.gs.model.dto.team.TeamRequestDTO;
import com.gs.model.entity.jpa.db1.league.LeagueRequest;
import com.gs.model.vo.league.LeagueRequestVo;
import com.gs.model.vo.team.TeamRequestVo;

import java.util.List;

public interface LeagueRequestService {

    Boolean existsById(Long leagueRequestId);
    CodeEnum sendLeagueRequest(LeagueRequestDTO leagueRequestDTO);

    List<LeagueRequestVo> getLeagueRequestLst(Long leagueId, Integer pageNum, Integer pageSize);

    CodeEnum deleteTeamRequest(Long manageMemberId, Long id);

    CodeEnum deleteAllTeamRequest(Long leagueId,  Long manageMemberId);


    /**
     * 删除所有已读请求
     *
     * @param leagueId 联盟ID
     * @return CodeEnum.IS_SUCCESS
     */
    CodeEnum deleteAllDoneTeamRequest(Long leagueId,  Long manageMemberId);
}
