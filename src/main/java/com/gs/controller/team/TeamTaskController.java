package com.gs.controller.team;

import com.gs.service.intf.team.TeamTaskService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "队务相关接口")
@RestController
@RequestMapping("/game/v1.0/gameteam/manager")
@Validated
@AllArgsConstructor
public class TeamTaskController {

    private final TeamTaskService teamTaskService;

    @ApiOperation(value = "查询队务列表")
    @RequestMapping(value = "/queryTeamTasks", method = RequestMethod.GET)
    public R queryTeamTasks(@RequestParam Long teamId,
                       @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize) {
        return R.success(teamTaskService.getTeamTaskLst(teamId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除单条队务")
    @RequestMapping(value = "/deleteOneTeamTask", method = RequestMethod.GET)
    public R deleteOneTeamTask(@RequestParam Long teamId, @RequestParam Long manageMemberId, @RequestParam Long id) {
        return R.result(teamTaskService.deleteOneTeamTask(teamId, manageMemberId, id));
    }

    @ApiOperation(value = "删除组内所有队务")
    @RequestMapping(value = "/deleteTeamTasks", method = RequestMethod.GET)
    public R deleteTeamTasks(@RequestParam Long teamId, @RequestParam Long manageMemberId) {
        teamTaskService.deleteTeakTasks(teamId, manageMemberId);
        return R.success();
    }
}
