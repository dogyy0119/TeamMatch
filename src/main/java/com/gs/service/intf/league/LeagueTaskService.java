package com.gs.service.intf.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.vo.league.LeagueTaskVo;
import com.gs.model.vo.team.TeamTaskVo;

import java.util.List;

public interface LeagueTaskService {

    boolean existById(Long leagueId);
    /**
     * 根据联盟Id查询所有的队务
     *
     * @param leagueId   联盟Id
     * @param pageNum  当前页
     * @param pageSize 页容量
     * @return 符合条件得Team List
     */
    List<LeagueTaskVo> getLeagueTaskLst(
            Long leagueId,
            Integer pageNum,
            Integer pageSize);

    CodeEnum deleteOneLeagueTask(Long leagueId, Long manageMemberId, Long id);

    CodeEnum deleteLeagueTasks(Long leagueId, Long manageMemberId);

}
