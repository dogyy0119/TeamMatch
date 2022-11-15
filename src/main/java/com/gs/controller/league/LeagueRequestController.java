package com.gs.controller.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.constant.enums.MemberJobEnum;
import com.gs.model.dto.league.LeagueRequestDTO;
import com.gs.service.intf.league.LeagueRequestService;
import com.gs.service.intf.league.LeagueService;
import com.gs.service.intf.league.LeagueTeamService;
import com.gs.service.intf.team.MemberService;
import com.gs.service.intf.team.TeamMemberService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Api(tags = "战队请求接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
@Slf4j
public class LeagueRequestController {

    private final LeagueRequestService leagueRequestService;
    private final LeagueTeamService leagueTeamService;

    private final LeagueService leagueService;

    private final MemberService memberService;

    private final TeamMemberService teamMemberService;

    @ApiOperation(value = "发送联盟请求")
    @RequestMapping(value = "/sendLeagueRequest", method = RequestMethod.POST)
    public R sendLeagueRequest(@Validated @RequestBody LeagueRequestDTO leagueRequestDTO) {

        log.info("sendLeagueRequest：" + "leagueRequestDTO = " + leagueRequestDTO.toString());

        if (!teamMemberService.isMemberInTeam(leagueRequestDTO.getFromMemberId(), leagueRequestDTO.getFromTeamId())){
            log.error("sendLeagueRequest：" + "该玩家不在该战队中");
            return R.result(CodeEnum.IS_MEMBER_NOT_IN_TEAM);
        }

        if (Objects.equals(teamMemberService.getMemberJobInTeam(leagueRequestDTO.getFromMemberId(), leagueRequestDTO.getFromTeamId()), MemberJobEnum.IS_TEAM_MEMBER.getJob())) {
            log.error("sendLeagueRequest：" + "该玩家没有权限");
            return R.result(CodeEnum.IS_TEAM_LEAGUE_PERMISSION);
        }

        if (leagueService.isAleadyInLeague(leagueRequestDTO.getFromMemberId())){
            return R.result(CodeEnum.IS_ALREADY_IN_LEAGUE);
        }

        if (leagueTeamService.existsByTeamId(leagueRequestDTO.getFromTeamId())){
            log.error("sendLeagueRequest：" + "该战队已经在该联盟中");
            return R.error(CodeEnum.IS_TEAM_ALLEARY_IN_LEAGUE.getCode(), "该战队已经在该联盟中");
        }

        return R.result(leagueRequestService.sendLeagueRequest(leagueRequestDTO));
    }

    @ApiOperation(value = "获取联盟请求列表")
    @RequestMapping(value = "/getLeagueRequests", method = RequestMethod.GET)
    public R getLeagueRequests
            (
                    @RequestParam Long manageMemberId,
                    @RequestParam(value = "leagueId",required = false) Long leagueId,
                    @RequestParam Integer pageNum,
                    @RequestParam Integer pageSize
            ) {
        log.info("getLeagueRequests：" + "manageMemberId = " + manageMemberId + "leagueId = " + leagueId + "pageNum = " + pageNum + "pageSize = " + pageSize);

        if(leagueId==null){
            log.error("getLeagueRequests：" + "leagueId==null");
            return R.success();

        }

        if (!memberService.existsById(manageMemberId)){
            log.error("getLeagueRequests：" + "玩家不存在");
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (!leagueService.existById(leagueId)){
            log.error("getLeagueRequests：" + "联盟不存在");
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        if (!Objects.equals(manageMemberId, leagueService.getCreateMemberId(leagueId))){
            log.error("getLeagueRequests：" + "没有权限");
            return R.result(CodeEnum.IS_LEAGUE_PERMISSION_ERROR);
        }

        return R.success(leagueRequestService.getLeagueRequestLst(leagueId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除联盟请求")
    @RequestMapping(value = "/deleteLeagueRequest", method = RequestMethod.GET)
    public R deleteLeagueRequest(@RequestParam Long manageMemberId, @RequestParam Long id) {

        log.info("deleteLeagueRequest：" + "manageMemberId = " + manageMemberId + "id = " + id);
        if (!memberService.existsById(manageMemberId)){
            log.error("deleteLeagueRequest：" + "玩家不存在");
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (leagueRequestService.existsById(id)){
            log.error("deleteLeagueRequest：" + "请求不存在");
            return R.result(CodeEnum.IS_LEAGUE_REQUEST_NOT_EXIST);
        }


        return R.success(leagueRequestService.deleteTeamRequest(manageMemberId, id));
    }

    @ApiOperation(value = "删除全部联盟请求")
    @RequestMapping(value = "/deleteAllLeagueRequest", method = RequestMethod.GET)
    public R deleteAllLeagueRequest(@RequestParam(value = "leagueId") Long leagueId, @RequestParam Long manageMemberId) {

        log.info("deleteAllLeagueRequest：" + "leagueId = " + leagueId + "manageMemberId = " + manageMemberId);

        if (!memberService.existsById(manageMemberId)){
            log.error("deleteLeagueRequest：" + "玩家不存在");
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (!leagueService.existById(leagueId)){
            log.error("getLeagueRequests：" + "联盟不存在");
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        return R.success(leagueRequestService.deleteAllTeamRequest(leagueId, manageMemberId));
    }

    @ApiOperation(value = "删除全部已读联盟请求")
    @RequestMapping(value = "/deleteAllDoneLeagueRequest", method = RequestMethod.GET)
    public R deleteAllDoneLeagueRequest(@RequestParam(value = "leagueId") Long leagueId, @RequestParam Long manageMemberId) {
        if (!memberService.existsById(manageMemberId)){
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (!leagueService.existById(leagueId)){
            return R.result(CodeEnum.IS_LEAGUE_NOT_EXIST);
        }

        return R.success(leagueRequestService.deleteAllDoneTeamRequest(leagueId, manageMemberId));
    }
}
