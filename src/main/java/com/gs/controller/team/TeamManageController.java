package com.gs.controller.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.team.TeamCreateDTO;
import com.gs.model.dto.team.TeamMemberDTO;
import com.gs.model.dto.team.TeamUpdateInfoDTO;
import com.gs.service.intf.team.LogoService;
import com.gs.service.intf.team.MemberService;
import com.gs.service.intf.team.TeamMemberService;
import com.gs.service.intf.team.TeamService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 战队管理接口
 * User: Liuhang
 * DateTime: 2022-04-22
 * To change this template use File | Settings | File Templates.
 **/
@Api(tags = "战队管理接口")
@RestController
@RequestMapping("/game/v1.0/gameteam/manager")
@AllArgsConstructor
public class TeamManageController {

    @Resource
    TeamService teamService;

    @Resource
    MemberService memberService;

    @Resource
    TeamMemberService teamMemberService;

    @Resource
    LogoService logoService;


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

        if (!memberService.existsById(createMemberId)) {
            return R.error(CodeEnum.IS_MEMBER_NOT_EXIST.getCode(), "创建战队失败:该用户不存在");
        }

        if (teamService.existsByCreateMemberId(createMemberId)) {
            return R.error(CodeEnum.IS_ALREADY_CREATE_TEAM.getCode(), "创建战队失败:该用户已经创建过战队");
        }

        return R.success(teamService.createTeam(createMemberId, teamCreateDTO));
    }

    @ApiOperation(value = "查询所有战队")
    @RequestMapping(value = "/getTeamPage", method = RequestMethod.GET)
    public R getTeamPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        return R.success(teamService.getTeamPage(pageNum, pageSize));
    }

    @ApiOperation(value = "查询某个玩家所在的所有战队")
    @RequestMapping(value = "/getTeamPageByPlayerId", method = RequestMethod.GET)
    public R getTeamPageByPlayerId(
            @RequestParam Long memberId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        return R.success(teamMemberService.getTeamPageByPlayerId(memberId, pageNum, pageSize));
    }

    @ApiOperation(value = "获取战队")
    @RequestMapping(value = "/getTeamByTeamId", method = RequestMethod.GET)
    public R getTeamByTeamId(@RequestParam Long teamId) {
        return R.success(teamService.getTeamByTeamId(teamId));
    }

    @ApiOperation(value = "处理战队请求")
    @RequestMapping(value = "/doTeamRequest", method = RequestMethod.POST)
    public R doTeamRequest(
            @RequestParam Long messageId,
            @RequestParam Integer flg) {
        return R.result(teamService.doTeamRequest(messageId, flg));
    }

    @ApiOperation(value = "处理成员请求")
    @RequestMapping(value = "/doMemberRequest", method = RequestMethod.POST)
    public R doMemberRequest(
            @RequestParam Long messageId,
            @RequestParam Integer flg) {
        return R.result(teamService.doMemberRequest(messageId, flg));
    }

    @ApiOperation(value = "删除队员")
    @RequestMapping(value = "/deleteMember", method = RequestMethod.POST)
    public R deleteMember(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {
        return R.result(teamService.deleteMember(manageMemberId, teamMemberDTO));
    }

    @ApiOperation(value = "退出战队")
    @RequestMapping(value = "/quitTeam", method = RequestMethod.POST)
    public R quitTeam(
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {
        return R.result(teamService.quitTeam(teamMemberDTO));
    }

    @ApiOperation(value = "转让队长")
    @RequestMapping(value = "/transferTeamLeader", method = RequestMethod.POST)
    public R transferTeamLeader(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {

        return R.result(teamService.transferTeamLeader(manageMemberId, teamMemberDTO));
    }

    @ApiOperation(value = "设置副队长")
    @RequestMapping(value = "/setSecondTeamLeader", method = RequestMethod.POST)
    public R setSecondTeamLeader(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {

        return R.result(teamService.setSecondTeamLeader(manageMemberId, teamMemberDTO));

    }

    @ApiOperation(value = "解除副队长")
    @RequestMapping(value = "/releaseSecondTeamLeader", method = RequestMethod.POST)
    public R releaseSecondTeamLeader(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {
        return R.result(teamService.releaseSecondTeamLeader(manageMemberId, teamMemberDTO));
    }

    @ApiOperation(value = "禁言和解除禁言")
    @RequestMapping(value = "/changeSilentFlg", method = RequestMethod.POST)
    public R changeSilentFlg(
            @RequestParam Long manageMemberId,
            @Validated @RequestBody TeamMemberDTO teamMemberDTO) {
        return R.result(teamService.changeSilentFlg(manageMemberId, teamMemberDTO));

    }

    @ApiOperation(value = "解散战队")
    @RequestMapping(value = "/releaseTeam", method = RequestMethod.GET)
    public R releaseTeam(
            @RequestParam("manageMemberId") Long manageMemberId,
            @RequestParam("teamId") Long teamId) {
        return R.result(teamService.releaseTeam(manageMemberId, teamId));

    }

    @ApiOperation(value = "更新战队信息")
    @RequestMapping(value = "/updateTeamInfo", method = RequestMethod.POST)
    public R updateTeamInfo(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO){

        return R.result(teamService.updateTeamInfo(manageMemberId, teamUpdateInfoDTO));
    }

    @Deprecated
    @ApiOperation(value = "更换战队名称")
    @RequestMapping(value = "/updateTeamName", method = RequestMethod.POST)
    public R updateTeamName(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {

        return R.result(teamService.updateTeamName(manageMemberId, teamUpdateInfoDTO));

    }

    @Deprecated
    @ApiOperation(value = "更换战队最大成员数量")
    @RequestMapping(value = "/updateTeamMaxMemberNum", method = RequestMethod.POST)
    public R updateTeamMaxMemberNum(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {

        return R.result(teamService.updateTeamMaxMemberNum(manageMemberId, teamUpdateInfoDTO));

    }

    @Deprecated
    @ApiOperation(value = "更换战队描述信息")
    @RequestMapping(value = "/updateTeamDescriptionInfo", method = RequestMethod.POST)
    public R updateTeamDescriptionInfo(
            @RequestParam("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {
        return R.result(teamService.updateTeamDescriptionInfo(manageMemberId, teamUpdateInfoDTO));

    }

    @ApiOperation(value = "上传logo")
    @RequestMapping(value = "/uploadLogo", method = RequestMethod.POST)
    public R uploadLogo(
            @RequestParam("manageMemberId") Long manageMemberId,
            @RequestParam(name = "file") MultipartFile file) {

//        if (!memberService.existsById(manageMemberId)) {
//            return R.error(CodeEnum.IS_MEMBER_NOT_EXIST.getCode(), "该用户不存在");
//        }

//        if (!teamService.existByTeamId(teamId)) {
//            return R.error(CodeEnum.IS_TEAM_NOT_EXIST.getCode(), "该战队不存在");
//        }

        if (file == null) {
            return R.error("请选择要上传的图片");
        }
        if (file.getSize() > 1024 * 1024 * 10) {
            return R.error("文件大小不能大于10M");
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            return R.error("请选择jpg,jpeg,gif,png格式的图片");
        }

        return R.success(logoService.uploadImage(file, manageMemberId));

    }

    @ApiOperation(value = "获取Logo列表")
    @RequestMapping(value = "/getLogoList", method = RequestMethod.GET)
    public R getLogoListByTeam(@RequestParam(name = "memberId") Long memberId) {
        return R.success(logoService.getLogoList(memberId));
    }

    @ApiOperation(value = "删除logo")
    @RequestMapping(value = "/deleteLogo", method = RequestMethod.GET)
    public R deleteLogo(@RequestParam(name = "logoId") Long logoId) {
        return R.success(logoService.deleteLogo(logoId));
    }

    @ApiOperation(value = "模糊查询战队")
    @RequestMapping(value = "/queryTeam", method = RequestMethod.GET)
    public R queryTeam(@RequestParam String key,
                       @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize) {
        return R.success(teamService.queryTeamBykey(key, pageNum, pageSize));
    }
}
