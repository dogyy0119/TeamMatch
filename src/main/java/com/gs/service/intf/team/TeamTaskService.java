package com.gs.service.intf.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.vo.team.TeamTaskVo;

import java.util.List;

public interface TeamTaskService {

    List<TeamTaskVo> getTeamTaskLst(Long teamId, Integer pageNum, Integer pageSize);

    CodeEnum deleteOneTeamTask(Long teamId, Long manageMemberId, Long id);

    CodeEnum deleteTeakTasks(Long teamId, Long manageMemberId);

}
