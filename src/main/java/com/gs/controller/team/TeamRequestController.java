package com.gs.controller.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.team.TeamRequestDTO;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamMemberRepository;
import com.gs.service.intf.team.TeamRequestService;
import com.gs.service.intf.team.TeamService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "战队请求接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
@Slf4j
public class TeamRequestController {

    private final TeamRequestService teamRequestService;
    private final TeamService teamService;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @ApiOperation(value = "发送战队请求")
    @RequestMapping(value = "/sendTeamRequest", method = RequestMethod.POST)
    public R sendTeamRequest(@Validated @RequestBody TeamRequestDTO teamRequestDTO) {
        log.info("sendTeamRequest：" + "teamRequestDTO = " + teamRequestDTO.toString());

        if (null != teamService.findTeamMemberByTeamIdAndMemberId(teamRequestDTO.getTeamId(), teamRequestDTO.getFromId())){

            log.error("sendTeamRequest：" + "您已经在该战队");
            return R.error(CodeEnum.IS_MEMBER_ALREADY_IN_TEAM.getCode(), "您已经在该战队");
        }

        Member member = memberRepository.findMemberById(teamRequestDTO.getFromId());
        List<TeamMember> teamMemberList = teamMemberRepository.findTeamMembersByMember(member);
        if( teamMemberList.size() > 0 ) {
            log.error("sendTeamRequest：" + "您已经加入过战队");
            return R.error(CodeEnum.IS_ALEARY_IN_TEAM.getCode(), "您已经加入过战队");
        }

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
        log.info("getTeamRequests：" + "teamId = " + teamId + "memberId = " + memberId + "pageNum = " + pageNum + "pageSize = " + pageSize);

        return R.success(teamRequestService.getTeamRequestLst(teamId, memberId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除战队请求")
    @RequestMapping(value = "/deleteTeamRequest", method = RequestMethod.GET)
    public R deleteTeamRequest(@RequestParam Long id, @RequestParam Long memberId) {
        log.info("deleteTeamRequest：" + "id = " + id + "memberId = " + memberId);

        return R.success(teamRequestService.deleteTeamRequest(id, memberId));
    }

    @ApiOperation(value = "删除全部战队请求")
    @RequestMapping(value = "/deleteAllTeamRequest", method = RequestMethod.GET)
    public R deleteAllTeamRequest(@RequestParam(value = "teamId") Long teamId, @RequestParam Long memberId) {

        log.info("deleteAllTeamRequest：" + "teamId = " + teamId + "memberId = " + memberId);
        return R.success(teamRequestService.deleteAllTeamRequest(teamId, memberId));
    }

    @ApiOperation(value = "删除全部已读战队请求")
    @RequestMapping(value = "/deleteAllDoneTeamRequest", method = RequestMethod.GET)
    public R deleteAllDoneTeamRequest(@RequestParam(value = "teamId") Long teamId, @RequestParam Long memberId) {
        log.info("deleteAllDoneTeamRequest：" + "teamId = " + teamId + "memberId = " + memberId);

        return R.success(teamRequestService.deleteAllDoneTeamRequest(teamId, memberId));
    }
}
