package com.gs.controller.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.service.intf.league.LeagueService;
import com.gs.service.intf.league.LeagueTaskService;
import com.gs.service.intf.team.MemberService;
import com.gs.service.intf.team.TeamTaskService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "队务相关接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
@Slf4j
public class LeagueTaskController {

    private final LeagueTaskService leagueTaskService;
    private final LeagueService leagueService;

    private final MemberService memberService;

    @ApiOperation(value = "查询联盟快递列表")
    @RequestMapping(value = "/queryLeagueTasks", method = RequestMethod.GET)
    public R queryLeagueTasks(@RequestParam(value = "leagueId", required = false) Long leagueId,
                              @RequestParam Integer pageNum,
                              @RequestParam Integer pageSize) {
        log.info("queryLeagueTasks：" + "leagueId = " + leagueId + "pageNum = " + pageNum + "pageSize = " + pageSize);

        if (leagueId == null) {
            log.error("queryLeagueTasks：" + "leagueId == null");

            return R.success();
        }

        if (!leagueService.existById(leagueId)) {
            log.error("queryLeagueTasks：" + "联盟不存在");
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        return R.success(leagueTaskService.getLeagueTaskLst(leagueId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除单条联盟快递信息")
    @RequestMapping(value = "/deleteOneLeagueTask", method = RequestMethod.GET)
    public R deleteOneLeagueTask(@RequestParam Long leagueId, @RequestParam Long manageMemberId, @RequestParam Long id) {

        log.info("deleteOneLeagueTask：" + "leagueId = " + leagueId + "manageMemberId = " + manageMemberId + "id = " + id);
        if (!leagueService.existById(leagueId)) {
            log.error("deleteOneLeagueTask：" + "联盟不存在");
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        if (!memberService.existsById(manageMemberId)) {
            log.error("deleteOneLeagueTask：" + "玩家不存在");
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (!leagueTaskService.existById(id)) {

            log.error("deleteOneLeagueTask：" + "任务不存在");
            return R.result(CodeEnum.IS_LEAGUE_TASK_NOT_EXIST);
        }
        return R.result(leagueTaskService.deleteOneLeagueTask(leagueId, manageMemberId, id));
    }

    @ApiOperation(value = "删除组内所有快递信息")
    @RequestMapping(value = "/deleteLeagueTasks", method = RequestMethod.GET)
    public R deleteLeagueTasks(@RequestParam Long leagueId, @RequestParam Long manageMemberId) {

        log.info("deleteOneLeagueTask：" + "leagueId = " + leagueId + "manageMemberId = " + manageMemberId);

        if (!leagueService.existById(leagueId)) {
            log.error("deleteLeagueTasks：" + "联盟不存在");
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        if (!memberService.existsById(manageMemberId)) {
            log.error("deleteLeagueTasks：" + "玩家不存在");
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        leagueTaskService.deleteLeagueTasks(leagueId, manageMemberId);
        return R.success();
    }
}
