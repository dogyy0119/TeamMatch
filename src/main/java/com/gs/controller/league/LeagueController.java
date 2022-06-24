package com.gs.controller.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.league.LeagueCreateDTO;
import com.gs.model.dto.league.LeagueTeamDTO;
import com.gs.model.dto.league.LeagueUpdateInfoDTO;
import com.gs.service.intf.league.LeagueService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 联盟管理接口
 * User: lys
 * DateTime: 2022-04-22
 **/

@Api(tags = "联盟管理接口")
@RestController
@RequestMapping("/game/v1.0/gameteam/manager")
@Validated
@AllArgsConstructor
public class LeagueController {

    private final LeagueService leagueService;

    @ApiOperation(value = "创建联盟")
    @RequestMapping(value = "/createLeague", method = RequestMethod.POST)
    public R createLeague(
            @Validated @RequestBody LeagueCreateDTO leagueCreateDTO){
        if (leagueService.existsByCreateMemberId(leagueCreateDTO.getCreateMemberId())){
            return R.error(CodeEnum.IS_ALREADY_CREATE_LEAGUE.getCode(), "创建战队失败:该用户已经创建过联盟");
        }
        return R.success(leagueService.createLeague(leagueCreateDTO));
    }

    @ApiOperation(value = "查询所有联盟")
    @RequestMapping(value = "/getLeagues", method = RequestMethod.GET)
    public R getLeagues(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize){
        return R.success(leagueService.getLeagues(pageNum, pageSize));
    }

    @ApiOperation(value = "根据Id查询指定联盟")
    @RequestMapping(value = "/getLeague", method = RequestMethod.GET)
    public R getLeague(
            @RequestParam Long leagueId){
        return R.success(leagueService.getLeague(leagueId));
    }

    @ApiOperation(value = "处理加入联盟的请求")
    @RequestMapping(value = "/doJoinLeagueRequest", method = RequestMethod.POST)
    public R doJoinLeagueRequest(
            @RequestParam Long managerId,
            @RequestParam Long messageId,
            @RequestParam Integer flg) {
        return R.result(leagueService.doJoinLeagueRequest(managerId, messageId, flg));
    }

    @ApiOperation(value = "处理添加战队的请求")
    @RequestMapping(value = "/doAddTeamRequest", method = RequestMethod.POST)
    public R doAddTeamRequest(
            @RequestParam Long managerId,
            @RequestParam Long messageId,
            @RequestParam Integer flg) {
        return R.result(leagueService.doAddTeamRequest(managerId, messageId, flg));
    }

    @ApiOperation(value = "删除战队")
    @RequestMapping(value = "/deleteLeagueTeam", method = RequestMethod.POST)
    public R deleteLeagueTeam(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody LeagueTeamDTO leagueTeamDTO) {
        return R.result(leagueService.deleteLeagueTeam(manageMemberId, leagueTeamDTO));
    }

    @ApiOperation(value = "退出战队")
    @RequestMapping(value = "/quitLeague", method = RequestMethod.POST)
    public R quitLeague(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody LeagueTeamDTO leagueTeamDTO) {
        return R.result(leagueService.quitLeague(manageMemberId, leagueTeamDTO));
    }

    @ApiOperation(value = "删除联盟")
    @RequestMapping(value = "/deleteLeague", method = RequestMethod.GET)
    public R deleteLeague(@RequestParam Long managerId, @RequestParam Long leagueId){
        return R.success(leagueService.deleteLeague(managerId, leagueId));
    }

    @ApiOperation(value = "更新联盟信息")
    @RequestMapping(value = "/updateLeagueInfo", method = RequestMethod.POST)
    public R updateTeamInfo(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody LeagueUpdateInfoDTO leagueUpdateInfoDTO) {

        return R.result(leagueService.updateLeagueInfo(manageMemberId, leagueUpdateInfoDTO));
    }

    @ApiOperation(value = "模糊查询联盟")
    @RequestMapping(value = "/queryLeagues", method = RequestMethod.GET)
    public R queryLeagues(@RequestParam String key,
                          @RequestParam Integer pageNum,
                          @RequestParam Integer pageSize) {
        return R.success(leagueService.queryLeaguesBykey(key, pageNum, pageSize));
    }

    //for doing
    @ApiOperation(value = "查询队员可以查看的联盟列表")
    @RequestMapping(value = "/getLeaguesByMember", method = RequestMethod.GET)
    public R getLeaguesByMember(
            @RequestParam Long memberId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize){
        return R.success(leagueService.getLeaguesByMember(memberId, pageNum, pageSize));
    }


}
