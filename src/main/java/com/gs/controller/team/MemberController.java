package com.gs.controller.team;

import com.gs.service.intf.team.MemberService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 成员管理接口
 * User: lys
 * DateTime: 2022-04-22
 **/

@Api(tags = "成员管理接口")
@RestController
@RequestMapping("/game/v1.0/gameteam/manager")
@Validated
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "获取Member")
    @RequestMapping(value = "/getMember", method = RequestMethod.GET)
    public R getMember(@RequestParam Long memberId) {
        return R.success(memberService.getMemberById(memberId));
    }

    @ApiOperation(value = "模糊查询队员")
    @RequestMapping(value = "/queryMember", method = RequestMethod.GET)
    public R getMember(@RequestParam String key,
                       @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize) {
        return R.success(memberService.queryMembersBykey(key, pageNum, pageSize));
    }
}
