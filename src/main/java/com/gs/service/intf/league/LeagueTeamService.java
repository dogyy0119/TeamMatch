package com.gs.service.intf.league;


/**
 * 战队成员 Service接口层
 * User: lys
 * DateTime: 2022-05-1
 **/
public interface LeagueTeamService {
    boolean existsByTeamId(Long teamId);
}
