package com.gs.controller.team;

import cn.hutool.json.JSONObject;
import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.team.TeamCreateDTO;
import com.gs.model.dto.team.TeamMemberDTO;
import com.gs.model.dto.team.TeamUpdateInfoDTO;
import com.gs.model.vo.team.MemberRequestVo;
import com.gs.service.intf.team.*;
import com.gs.utils.HttpUtils;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 战队管理接口
 * User: Liuhang
 * DateTime: 2022-04-22
 * To change this template use File | Settings | File Templates.
 **/
@Api(tags = "战队管理接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@AllArgsConstructor
@Slf4j
public class TeamManageController {

    @Resource
    TeamService teamService;

    @Resource
    MemberService memberService;

    @Resource
    TeamMemberService teamMemberService;

    @Resource
    LogoService logoService;

    @Resource
    MemberRequestService memberRequestService;


    /**
     * 创建转队REST接口
     *
     * @param createMemberId 创建者的member ID
     * @param teamCreateDTO  team相关输入dto
     * @return
     */
    @ApiOperation(value = "创建战队")
    @RequestMapping(value = "/createTeam", method = RequestMethod.POST)
    public R createTeam(
            @RequestParam("createMemberId") Long createMemberId,
            @Validated @RequestBody TeamCreateDTO teamCreateDTO) {

        log.info("createTeam：" + "createMemberId = " + createMemberId + ", teamCreateDTO = " + teamCreateDTO.toString());

        if (!memberService.existsById(createMemberId)) {
            log.error("createTeam：" + "创建战队失败:该用户不存在:" + createMemberId);
            return R.error(CodeEnum.IS_MEMBER_NOT_EXIST.getCode(), "创建战队失败:该用户不存在");
        }

        if (teamService.isAleadyInTeam(createMemberId)) {
            log.error("createTeam：" + "创建战队失败:该用户已经创建过战队:" + createMemberId);
            return R.error(CodeEnum.IS_ALEARY_IN_TEAM.getCode(), "您已经加入过战队");
        }

        return R.success(teamService.createTeam(createMemberId, teamCreateDTO));
    }

    @ApiOperation(value = "查询所有战队")
    @RequestMapping(value = "/getTeamPage", method = RequestMethod.GET)
    public R getTeamPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        log.info("getTeamPage：" + "pageNum = " + pageNum + ", pageSize = " + pageSize);

        return R.success(teamService.getTeamPage(pageNum, pageSize));
    }

    @ApiOperation(value = "查询某个玩家所在的所有战队")
    @RequestMapping(value = "/getTeamPageByPlayerId", method = RequestMethod.GET)
    public R getTeamPageByPlayerId(
            @RequestParam Long memberId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        log.info("getTeamPageByPlayerId：" + "memberId = " + memberId + ", pageNum = " + pageNum + ", pageSize = " + pageSize);

        return R.success(teamMemberService.getTeamPageByPlayerId(memberId, pageNum, pageSize));
    }

    @ApiOperation(value = "获取战队")
    @RequestMapping(value = "/getTeamByTeamId", method = RequestMethod.GET)
    public R getTeamByTeamId(@RequestParam String teamId) {
        log.info("getTeamByTeamId：" + "teamId = " + teamId);

        try {
            Long myTeamId = Long.valueOf(teamId);
            return R.success(teamService.getTeamByTeamId(myTeamId));
        }catch (Exception e) {
            return R.success("undefinded");
        }
    }

    @ApiOperation(value = "处理战队请求")
    @RequestMapping(value = "/doTeamRequest", method = RequestMethod.POST)
    public R doTeamRequest(
            @RequestParam Long messageId,
            @RequestParam Integer flg) {
        log.info("doTeamRequest：" + "messageId = " + messageId + "flg = " + flg);

        return R.result(teamService.doTeamRequest(messageId, flg));
    }

    @ApiOperation(value = "处理成员请求")
    @RequestMapping(value = "/doMemberRequest", method = RequestMethod.POST)
    public R doMemberRequest(
            @RequestParam Long messageId,
            @RequestParam Integer flg) {

        log.info("doMemberRequest：" + "messageId = " + messageId + "flg = " + flg);
        CodeEnum result = teamService.doMemberRequest(messageId, flg);
        if (flg == 0){
            return R.result(result);
        }else{
            if (result == CodeEnum.IS_SUCCESS){
                MemberRequestVo memberRequestVo = memberRequestService.getMemberRequest(messageId);
                return R.success(teamService.getTeamByTeamId(memberRequestVo.getTeamId()));
            }else{
                return R.result(result);
            }
        }
    }

    @ApiOperation(value = "删除队员")
    @RequestMapping(value = "/deleteMember", method = RequestMethod.POST)
    public R deleteMember(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {
        log.info("deleteMember：" + "manageMemberId = " + manageMemberId + "teamMemberDTO = " + teamMemberDTO.toString());
        return R.result(teamService.deleteMember(manageMemberId, teamMemberDTO));
    }

    @ApiOperation(value = "退出战队")
    @RequestMapping(value = "/quitTeam", method = RequestMethod.POST)
    public R quitTeam(
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {

        log.info("quitTeam：" + "teamMemberDTO = " + teamMemberDTO.toString());

        return R.result(teamService.quitTeam(teamMemberDTO));
    }

    @ApiOperation(value = "转让队长")
    @RequestMapping(value = "/transferTeamLeader", method = RequestMethod.POST)
    public R transferTeamLeader(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {
        log.info("transferTeamLeader：" + "manageMemberId = " + manageMemberId + "teamMemberDTO = " + teamMemberDTO.toString());

        return R.result(teamService.transferTeamLeader(manageMemberId, teamMemberDTO));
    }

    @ApiOperation(value = "设置副队长")
    @RequestMapping(value = "/setSecondTeamLeader", method = RequestMethod.POST)
    public R setSecondTeamLeader(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {

        log.info("setSecondTeamLeader：" + "manageMemberId = " + manageMemberId + "teamMemberDTO = " + teamMemberDTO.toString());
        return R.result(teamService.setSecondTeamLeader(manageMemberId, teamMemberDTO));
    }

    @ApiOperation(value = "解除副队长")
    @RequestMapping(value = "/releaseSecondTeamLeader", method = RequestMethod.POST)
    public R releaseSecondTeamLeader(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {
        log.info("releaseSecondTeamLeader：" + "manageMemberId = " + manageMemberId + "teamMemberDTO = " + teamMemberDTO.toString());

        return R.result(teamService.releaseSecondTeamLeader(manageMemberId, teamMemberDTO));
    }

    @ApiOperation(value = "禁言和解除禁言")
    @RequestMapping(value = "/changeSilentFlg", method = RequestMethod.POST)
    public R changeSilentFlg(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {

        log.info("changeSilentFlg：" + "manageMemberId = " + manageMemberId + "teamMemberDTO = " + teamMemberDTO.toString());
        return R.result(teamService.changeSilentFlg(manageMemberId, teamMemberDTO));

    }

    @ApiOperation(value = "获取禁言标志")
    @RequestMapping(value = "/getSilentFlg", method = RequestMethod.GET)
    public R getSilentFlg(
            @RequestParam Long teamId,
            @RequestParam Long memberId) {
        log.info("getSilentFlg：" + "teamId = " + teamId + "memberId = " + memberId);

        if (!memberService.existsById(memberId)) {
            log.error("getSilentFlg：" + "获取禁言标志:该用户不存在:" + memberId);
            return R.error(CodeEnum.IS_MEMBER_NOT_EXIST.getCode(), "该用户不存在");
        }

        if (!teamService.existById(teamId)){
            log.error("getSilentFlg：" + "获取禁言标志:该战队不存在" + teamId);
            return R.error(CodeEnum.IS_TEAM_NOT_EXIST.getCode(), "该战队不存在");
        }

        if (null == teamService.findTeamMemberByTeamIdAndMemberId(teamId, memberId)){
            return R.error(CodeEnum.IS_MEMBER_NOT_IN_TEAM.getCode(), "该玩家不在该战队中");
        }

        return R.success(teamService.getSilentFlg(teamId, memberId));

    }
    @ApiOperation(value = "解散战队")
    @RequestMapping(value = "/releaseTeam", method = RequestMethod.GET)
    public R releaseTeam(
            @RequestParam("manageMemberId") Long manageMemberId,
            @RequestParam("teamId") Long teamId) {

        log.info("releaseTeam：" + "manageMemberId = " + manageMemberId + "teamId = " + teamId);

        Map<String, Long> requestMap = new HashMap<>();
        requestMap.put("teamId", teamId);

        JSONObject json = new JSONObject(requestMap);

        HttpUtils.doPost("http://127.0.0.1:8083/game/v1.0/paycenter/dismissteam/logout?teamId=" + teamId , json.toString(), null);
        return R.result(teamService.releaseTeam(manageMemberId, teamId));

    }

    @ApiOperation(value = "更新战队信息")
    @RequestMapping(value = "/updateTeamInfo", method = RequestMethod.POST)
    public R updateTeamInfo(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {
        log.info("updateTeamInfo：" + "manageMemberId = " + manageMemberId + "teamUpdateInfoDTO = " + teamUpdateInfoDTO.toString());

        CodeEnum result1 = teamService.updateTeamInfo(manageMemberId, teamUpdateInfoDTO);

        if (result1 == CodeEnum.IS_SUCCESS){
            return R.success(teamService.getTeamByTeamId(teamUpdateInfoDTO.getTeamId()));
        }
        return R.result(result1);
    }

    @Deprecated
    @ApiOperation(value = "更换战队名称")
    @RequestMapping(value = "/updateTeamName", method = RequestMethod.POST)
    public R updateTeamName(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {
        log.info("updateTeamName：" + "manageMemberId = " + manageMemberId + "teamUpdateInfoDTO = " + teamUpdateInfoDTO.toString());

        return R.result(teamService.updateTeamName(manageMemberId, teamUpdateInfoDTO));

    }

    @Deprecated
    @ApiOperation(value = "更换战队最大成员数量")
    @RequestMapping(value = "/updateTeamMaxMemberNum", method = RequestMethod.POST)
    public R updateTeamMaxMemberNum(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {

        log.info("updateTeamMaxMemberNum：" + "manageMemberId = " + manageMemberId + "teamUpdateInfoDTO = " + teamUpdateInfoDTO.toString());
        return R.result(teamService.updateTeamMaxMemberNum(manageMemberId, teamUpdateInfoDTO));

    }

    @Deprecated
    @ApiOperation(value = "更换战队描述信息")
    @RequestMapping(value = "/updateTeamDescriptionInfo", method = RequestMethod.POST)
    public R updateTeamDescriptionInfo(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {

        log.info("updateTeamDescriptionInfo：" + "manageMemberId = " + manageMemberId + "teamUpdateInfoDTO = " + teamUpdateInfoDTO.toString());

        return R.result(teamService.updateTeamDescriptionInfo(manageMemberId, teamUpdateInfoDTO));

    }

    @ApiOperation(value = "模糊查询战队")
    @RequestMapping(value = "/queryTeam", method = RequestMethod.GET)
    public R queryTeam(
            @RequestParam Long currentMemberId,
            @RequestParam String key,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        log.info("queryTeam：" + "currentMemberId = " + currentMemberId + "key = " + key + "pageNum = " + pageNum + "pageSize = " + pageSize);

        return R.success(teamService.queryTeamBykey(currentMemberId, key, pageNum, pageSize));
    }
}
