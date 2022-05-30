package com.gs.controller.team;

import com.gs.model.dto.team.TeamRequestDTO;
import com.gs.service.intf.team.TeamRequestService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "战队请求接口")
@RestController
@RequestMapping("/game/v1.0/gameteam/manager")
@Validated
@AllArgsConstructor
public class TeamRequestController {

    private final TeamRequestService teamRequestService;

    @ApiOperation(value = "发送战队请求")
    @RequestMapping(value = "/sendTeamRequest", method = RequestMethod.POST)
    public R sendTeamRequest(@RequestBody TeamRequestDTO teamRequestDTO) {
        return R.result(teamRequestService.sendTeamRequest(teamRequestDTO));
    }

    @ApiOperation(value = "获取战队请求列表")
    @RequestMapping(value = "/getTeamRequests", method = RequestMethod.GET)
    public R getTeamRequests
            (
                    @RequestParam(value = "teamId", required = false, defaultValue = "-1") Long teamId,
                    @RequestParam Long memberId,
                    @RequestParam Integer pageNum,
                    @RequestParam Integer pageSize
            ) {

        return R.success(teamRequestService.getTeamRequestLst(teamId, memberId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除战队请求")
    @RequestMapping(value = "/deleteTeamRequest", method = RequestMethod.GET)
    public R deleteTeamRequest(@RequestParam Long id, @RequestParam Long memberId) {
        return R.success(teamRequestService.deleteTeamRequest(id, memberId));
    }

    @ApiOperation(value = "删除全部战队请求")
    @RequestMapping(value = "/deleteAllTeamRequest", method = RequestMethod.GET)
    public R deleteAllTeamRequest(@RequestParam(value = "teamId") Long teamId, @RequestParam Long memberId) {
        return R.success(teamRequestService.deleteAllTeamRequest(teamId, memberId));
    }

    @ApiOperation(value = "删除全部已读战队请求")
    @RequestMapping(value = "/deleteAllDoneTeamRequest", method = RequestMethod.GET)
    public R deleteAllDoneTeamRequest(@RequestParam(value = "teamId") Long teamId, @RequestParam Long memberId) {
        return R.success(teamRequestService.deleteAllDoneTeamRequest(teamId, memberId));
    }
}
