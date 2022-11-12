package com.gs.controller.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamMemberRepository;
import com.gs.service.intf.team.MemberRequestService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api(tags = "成员请求接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
@Slf4j
public class MemberRequestController {

    private final MemberRequestService memberRequestService;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @ApiOperation(value = "发送成员请求")
    @RequestMapping(value = "/sendMemberRequest", method = RequestMethod.POST)
    public R sendMemberRequest(@Validated @RequestBody MemberRequestDTO memberRequestDTO) {
        log.info("sendMemberRequest：" + "memberRequestDTO = " + memberRequestDTO.toString());

        if (Objects.equals(memberRequestDTO.getFromId(), memberRequestDTO.getToId())){

            log.error("sendMemberRequest：" + "不能邀请自己加入战队");
            return R.error(CodeEnum.IS_REQUEST_INVITATION_OWN.getCode(), "不能邀请自己加入战队");
        }

        Member member = memberRepository.findMemberById(memberRequestDTO.getToId());
        List<TeamMember> teamMemberList = teamMemberRepository.findTeamMembersByMember(member);
        if( teamMemberList.size() > 0 ) {
            log.error("sendMemberRequest：" + "对方已经加入战队");
            return R.error(CodeEnum.IS_REQUEST_INVITATION_OWN.getCode(), "对方已经加入战队");
        }

        return R.result(memberRequestService.sendMemberRequest(memberRequestDTO));
    }

    @ApiOperation(value = "获取联盟战队请求列表")
    @RequestMapping(value = "/getMemberRequests", method = RequestMethod.GET)
    public R getMemberRequests
            (
                    @RequestParam(value = "memberId") Long memberId,
                    @RequestParam Integer pageNum,
                    @RequestParam Integer pageSize
            ) {

        log.info("getMemberRequests：" + "memberId = " + memberId + "pageNum = " + pageNum + "pageSize = " + pageSize);
        return R.success(memberRequestService.getMemberRequestLst(memberId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除成员请求")
    @RequestMapping(value = "/deleteMemberRequest", method = RequestMethod.GET)
    public R deleteMemberRequest(@RequestParam Long id) {
        log.info("deleteMemberRequest：" + "id = " + id);
        return R.success(memberRequestService.deleteMemberRequest(id));
    }

    @ApiOperation(value = "删除全部成员请求")
    @RequestMapping(value = "/deleteAllMemberRequest", method = RequestMethod.GET)
    public R deleteAllMemberRequest(@RequestParam(value="memberId") Long memberId) {

        log.info("deleteAllMemberRequest：" + "memberId = " + memberId);
        return R.success(memberRequestService.deleteAllMemberRequest(memberId));
    }

    @ApiOperation(value = "删除全部已读战队请求")
    @RequestMapping(value = "/deleteAllDoneMemberRequest", method = RequestMethod.GET)
    public R deleteAllDoneMemberRequest(@RequestParam(value="memberId") Long memberId) {
        log.info("deleteAllDoneMemberRequest：" + "memberId = " + memberId);

        return R.success(memberRequestService.deleteAllDoneMemberRequest(memberId));
    }
}
