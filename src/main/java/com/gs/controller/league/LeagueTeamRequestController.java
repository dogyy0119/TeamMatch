package com.gs.controller.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.constant.enums.MemberJobEnum;
import com.gs.model.dto.league.LeagueTeamRequestDTO;
import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.vo.team.MemberVo;
import com.gs.model.vo.team.TeamMemberVo;
import com.gs.model.vo.team.TeamVo;
import com.gs.service.intf.league.LeagueTeamRequestService;
import com.gs.service.intf.league.LeagueTeamService;
import com.gs.service.intf.team.MemberRequestService;
import com.gs.service.intf.team.MemberService;
import com.gs.service.intf.team.TeamMemberService;
import com.gs.service.intf.team.TeamService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Api(tags = "成员请求接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
@Slf4j
public class LeagueTeamRequestController {

    private final LeagueTeamRequestService leagueTeamRequestService;

    private final LeagueTeamService leagueTeamService;
    private final TeamMemberService teamMemberService;

    private MemberService memberService;

    private TeamService teamService;

    @ApiOperation(value = "发送联盟战队请求")
    @RequestMapping(value = "/sendLeagueTeamRequest", method = RequestMethod.POST)
    public R sendLeagueTeamRequest(@Validated @RequestBody LeagueTeamRequestDTO leagueTeamRequestDTO) {
        log.info("sendLeagueTeamRequest：" + "leagueTeamRequestDTO = " + leagueTeamRequestDTO.toString());

        if (leagueTeamService.existsByLeagueIdAndTeamId(leagueTeamRequestDTO.getLeagueId(), leagueTeamRequestDTO.getToTeamId())){
            log.error("sendLeagueTeamRequest：" + "该战队已经在该联盟中");
            return R.error(CodeEnum.IS_TEAM_ALLEARY_IN_LEAGUE.getCode(), "该战队已经在该联盟中");
        }

        return R.result(leagueTeamRequestService.sendLeagueTeamRequest(leagueTeamRequestDTO));
    }

    @ApiOperation(value = "获取联盟战队请求列表")
    @RequestMapping(value = "/getLeagueTeamRequests", method = RequestMethod.GET)
    public R getLeagueTeamRequests
            (
                    @RequestParam(value = "memberId") Long memberId,
                    @RequestParam(value = "teamId") Long teamId,
                    @RequestParam Integer pageNum,
                    @RequestParam Integer pageSize
            ) {

        log.info("getLeagueTeamRequests：" + "memberId = " +memberId + "teamId = " +teamId + "pageNum = " +pageNum + "pageSize = " +pageSize);
        if (!memberService.existsById(memberId)) {
            log.error("sendLeagueTeamRequest：" + "玩家不存在");
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (!teamService.existById(teamId)) {

            log.error("sendLeagueTeamRequest：" + "战队不存在");
            return R.result(CodeEnum.IS_TEAM_NOT_EXIST);
        }

        if (!teamMemberService.isMemberInTeam(memberId, teamId)){
            log.error("sendLeagueTeamRequest：" + "该玩家不在该战队中");
            return R.result(CodeEnum.IS_MEMBER_NOT_IN_TEAM);
        }

        if (Objects.equals(teamMemberService.getMemberJobInTeam(memberId, teamId), MemberJobEnum.IS_TEAM_MEMBER.getJob())) {

            log.error("sendLeagueTeamRequest：" + "没有权限");
            return R.result(CodeEnum.IS_TEAM_LEAGUE_PERMISSION);
        }

        return R.success(leagueTeamRequestService.getLeagueTeamRequests(teamId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除联盟战队请求")
    @RequestMapping(value = "/deleteLeagueTeamRequest", method = RequestMethod.GET)
    public R deleteLeagueTeamRequest(@RequestParam Long id) {
        log.info("deleteLeagueTeamRequest：" + "id = " +id);

        return R.success(leagueTeamRequestService.deleteLeagueTeamRequest(id));
    }

    @ApiOperation(value = "删除全部联盟战队请求")
    @RequestMapping(value = "/deleteAllLeagueTeamRequest", method = RequestMethod.GET)
    public R deleteAllLeagueTeamRequest(
            @RequestParam(value = "memberId") Long memberId,
            @RequestParam(value = "teamId") Long teamId) {

        log.info("deleteAllLeagueTeamRequest：" + "memberId = " +memberId + "teamId = " +teamId);
        if (!memberService.existsById(memberId)) {
            log.error("deleteAllLeagueTeamRequest：" + "玩家不存在");
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (!teamService.existById(teamId)) {
            log.error("deleteAllLeagueTeamRequest：" + "战队不存在");
            return R.result(CodeEnum.IS_TEAM_NOT_EXIST);
        }

        if (!teamMemberService.isMemberInTeam(memberId, teamId)){
            log.error("deleteAllLeagueTeamRequest：" + "该玩家不在该战队中");
            return R.result(CodeEnum.IS_MEMBER_NOT_IN_TEAM);
        }

        if (Objects.equals(teamMemberService.getMemberJobInTeam(memberId, teamId), MemberJobEnum.IS_TEAM_MEMBER.getJob())) {
            log.error("deleteAllLeagueTeamRequest：" + "没有权限");
            return R.result(CodeEnum.IS_TEAM_LEAGUE_PERMISSION);
        }

        return R.success(leagueTeamRequestService.deleteAllLeagueTeamRequest(teamId));
    }

    @ApiOperation(value = "删除全部已读联盟战队请求")
    @RequestMapping(value = "/deleteAllDoneLeagueTeamRequest", method = RequestMethod.GET)
    public R deleteAllDoneLeagueTeamRequest(
            @RequestParam(value = "memberId") Long memberId,
            @RequestParam(value = "teamId") Long teamId) {
        log.info("deleteAllDoneLeagueTeamRequest：" + "memberId = " +memberId + "teamId = " +teamId);

        if (!memberService.existsById(memberId)) {
            log.error("deleteAllLeagueTeamRequest：" + "玩家不存在");
            return R.result(CodeEnum.IS_MEMBER_NOT_EXIST);
        }

        if (!teamService.existById(teamId)) {
            log.error("deleteAllLeagueTeamRequest：" + "战队不存在");
            return R.result(CodeEnum.IS_TEAM_NOT_EXIST);
        }

        if (!teamMemberService.isMemberInTeam(memberId, teamId)){
            log.error("deleteAllLeagueTeamRequest：" + "该玩家不在该战队中");
            return R.result(CodeEnum.IS_MEMBER_NOT_IN_TEAM);
        }

        if (Objects.equals(teamMemberService.getMemberJobInTeam(memberId, teamId), MemberJobEnum.IS_TEAM_MEMBER.getJob())) {
            log.error("deleteAllLeagueTeamRequest：" + "没有权限");
            return R.result(CodeEnum.IS_TEAM_LEAGUE_PERMISSION);
        }

        return R.success(leagueTeamRequestService.deleteAllDoneLeagueTeamRequest(teamId));
    }
}