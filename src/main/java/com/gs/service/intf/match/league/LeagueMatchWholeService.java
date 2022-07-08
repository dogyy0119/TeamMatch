package com.gs.service.intf.match.league;

import com.gs.model.dto.match.league.LeagueMatchWholeDTO;
import com.gs.model.vo.league.LeagueVo;

public interface LeagueMatchWholeService {

    Boolean isExistLeagueMatch(Long leagueId);
    LeagueVo createLeagueMatch(LeagueMatchWholeDTO leagueMatchWholeDTO);
    LeagueVo deleteLeagueMatch(Long leagueId);
    LeagueVo updateLeagueMatch(Long leagueId);
}
