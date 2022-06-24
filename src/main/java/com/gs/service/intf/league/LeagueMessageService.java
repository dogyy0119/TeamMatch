package com.gs.service.intf.league;

import com.gs.model.dto.league.LeagueMessageDto;
import com.gs.model.dto.team.MessageDto;
import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.vo.league.LeagueMessageVo;
import com.gs.model.vo.team.MessageVo;

import java.util.Date;
import java.util.List;

public interface LeagueMessageService {

    LeagueMessageVo insertLeagueMessage(LeagueMessageDto dto);


    List<LeagueMessageVo> getLeagueGroupChatMsgs(Long leagueId, Integer pageNum, Integer pageSize);

    void deleteLeagueGroupChatMsgs(Long leagueId);

    void deleteLeaguePrivateChatMsgs(Long leagueId, Long fromId, Long toId);
    List<LeagueMessageVo> getLeaguePrivateChatMsgs(Long leagueId, Long fromId, Long toId, Integer pageNum, Integer pageSize);


    void deleteLeagueChatMsgs(Date date);

    void deleteLeagueChatMsgs(Long teamId, Long memberId);
}
