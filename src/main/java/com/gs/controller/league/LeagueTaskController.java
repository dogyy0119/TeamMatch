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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "队务相关接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
@CrossOrigin
public class LeagueTaskController {

    private final LeagueTaskService leagueTaskService;
    private final LeagueService leagueService;

    private final MemberService memberService;

    @ApiOperation(value = "查询联盟快递列表")
    @RequestMapping(value = "/queryLeagueTasks", method = RequestMethod.GET)
    public R queryLeagueTasks(@RequestParam Long leagueId,
                       @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize) {
        if (!leagueService.existById(leagueId)){
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        return R.success(leagueTaskService.getLeagueTaskLst(leagueId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除单条联盟快递信息")
    @RequestMapping(value = "/deleteOneLeagueTask", method = RequestMethod.GET)
    public R deleteOneLeagueTask(@RequestParam Long leagueId, @RequestParam Long manageMemberId, @RequestParam Long id) {
        if (!leagueService.existById(leagueId)){
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        if (!memberService.existsById(manageMemberId)){
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (!leagueTaskService.existById(id)){
            return R.result(CodeEnum.IS_LEAGUE_TASK_NOT_EXIST);
        }
        return R.result(leagueTaskService.deleteOneLeagueTask(leagueId, manageMemberId, id));
    }

    @ApiOperation(value = "删除组内所有快递信息")
    @RequestMapping(value = "/deleteLeagueTasks", method = RequestMethod.GET)
    public R deleteLeagueTasks(@RequestParam Long leagueId, @RequestParam Long manageMemberId) {

        if (!leagueService.existById(leagueId)){
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        if (!memberService.existsById(manageMemberId)){
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        leagueTaskService.deleteLeagueTasks(leagueId, manageMemberId);
        return R.success();
    }
}
