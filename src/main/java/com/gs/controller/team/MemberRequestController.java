package com.gs.controller.team;

import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.service.intf.team.MemberRequestService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "成员请求接口")
@RestController
@RequestMapping("/game/v1.0/gameteam/manager")
@Validated
@AllArgsConstructor
public class MemberRequestController {

    private final MemberRequestService memberRequestService;

    @ApiOperation(value = "发送成员请求")
    @RequestMapping(value = "/sendMemberRequest", method = RequestMethod.POST)
    public R sendMemberRequest(@RequestBody MemberRequestDTO memberRequestDTO) {
        return R.result(memberRequestService.sendMemberRequest(memberRequestDTO));
    }

    @ApiOperation(value = "获取成员请求列表")
    @RequestMapping(value = "/getMemberRequests", method = RequestMethod.GET)
    public R getMemberRequests
            (
                    @RequestParam(value = "memberId") Long memberId,
                    @RequestParam Integer pageNum,
                    @RequestParam Integer pageSize
            ) {

        return R.success(memberRequestService.getMemberRequestLst(memberId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除成员请求")
    @RequestMapping(value = "/deleteMemberRequest", method = RequestMethod.GET)
    public R deleteMemberRequest(@RequestParam Long id) {
        return R.success(memberRequestService.deleteMemberRequest(id));
    }

    @ApiOperation(value = "删除全部成员请求")
    @RequestMapping(value = "/deleteAllMemberRequest", method = RequestMethod.GET)
    public R deleteAllMemberRequest(@RequestParam(value="memberId") Long memberId) {
        return R.success(memberRequestService.deleteAllMemberRequest(memberId));
    }

    @ApiOperation(value = "删除全部已读战队请求")
    @RequestMapping(value = "/deleteAllDoneMemberRequest", method = RequestMethod.GET)
    public R deleteAllDoneMemberRequest(@RequestParam(value="memberId") Long memberId) {
        return R.success(memberRequestService.deleteAllDoneMemberRequest(memberId));
    }
}
