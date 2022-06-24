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
 * Logo管理接口
 * User: Liuhang
 * DateTime: 2022-04-22
 * To change this template use File | Settings | File Templates.
 **/
@Api(tags = "Logo管理接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@AllArgsConstructor
public class LogoController {

    @Resource
    TeamService teamService;

    @Resource
    MemberService memberService;

    @Resource
    TeamMemberService teamMemberService;

    @Resource
    LogoService logoService;


    @ApiOperation(value = "上传logo")
    @RequestMapping(value = "/uploadLogo", method = RequestMethod.POST)
    public R uploadLogo(
            @RequestParam("manageMemberId") Long manageMemberId,
            @RequestParam("type") Integer type,
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

        return R.success(logoService.uploadImage(file, manageMemberId, type));

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
}
