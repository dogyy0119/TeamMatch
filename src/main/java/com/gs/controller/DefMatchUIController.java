package com.gs.controller;


import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "A用户比赛UI添加权限")
@RestController
@RequestMapping("/game/v1.0/app/matches/UIControl")
@AllArgsConstructor
public class DefMatchUIController {

    @Autowired
    private MemberRepository memberRepository;

    @ApiOperation(value = "添加用户创建比赛权限")
    @RequestMapping(value = "/addMemberPermission", method = RequestMethod.POST)
    public R addMemberCreateMatchPermission( @RequestParam Long memberId ) {

        Member member = memberRepository.findMemberById(memberId);
        if( member == null ) {
            return R.error("用户不存在！！！");
        }

        member.setServe(1);
        memberRepository.save(member);
        return R.success( "添加权限成功！" );
    }

    @ApiOperation(value = "删除用户创建比赛权限")
    @RequestMapping(value = "/deleteMemberPermission", method = RequestMethod.POST)
    public R deleteMemberCreateMatchPermission( @RequestParam Long memberId ) {

        Member member = memberRepository.findMemberById(memberId);
        if( member == null ) {
            return R.error("用户不存在！！！");
        }

        member.setServe(0);
        memberRepository.save(member);
        return R.success( "删除权限成功！" );

    }

    @ApiOperation(value = "查询创建比赛权限")
    @RequestMapping(value = "/checkMemberPermission", method = RequestMethod.POST)
    public R checkMemberCreateMatchPermission( @RequestParam Long memberId ) {

        Member member = memberRepository.findMemberById(memberId);
        if( member == null ) {
            return R.error("用户不存在！！！");
        }

        if(member.getServe() == null) {
            member.setServe(0);
            memberRepository.save(member);
        }

        if( member.getServe() == 0) {
            return R.success(false );
        } else if( member.getServe() == 1) {
            return R.success(true);
        }

        return R.success( false );
    }
}
