package com.gs.controller;


import com.gs.convert.TeamOrderConvert;
import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.dto.def.TeamOrderDTO;
import com.gs.model.dto.vo.TeamOrderVO;
import com.gs.model.entity.jpa.db1.def.*;

import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.repository.jpa.def.*;

import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamMemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.def.TeamOrderService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "战队比赛内部申请接口")
@RestController
@RequestMapping("/game/v1.0/app/matches/TeamOrder")
@AllArgsConstructor
@CrossOrigin
public class TeamOrderController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DefMatchOrderRepository defMatchOrderRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TeamOrderService teamOrderService;

    @Autowired
    private DefMatchManageRepository defMatchManageRepository;

    @Autowired
    private DefMatchRepository defMatchRepository;

    @Autowired
    private TeamOrderRepository teamOrderRepository;

    @Autowired
    private TeamOrderConvert teamOrderConvert;

    @Autowired
    private PersonOrderRepository personOrderRepository;

    @ApiOperation(value = "创建战队比赛内部申请")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public R addTeamOrder(@RequestParam Long memberId, @RequestBody TeamOrderVO teamOrderVO) {

        DefMatchOrder defMatchOrder = null;
        try {
            Member member = memberRepository.findMemberById(memberId);
            if (member == null) return R.error("memberid 有误！");

            DefMatch defMatch = defMatchRepository.findDefMatchById(teamOrderVO.getMatchId());

            DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatch);

            defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, teamOrderVO.getTeamId());

            Team team = teamRepository.findTeamById(defMatchOrder.getOrderId());
            if (team == null) return R.error("defMatchOrderId 有误！");

            Member member1 = memberRepository.findMemberById(teamOrderVO.getMemberId());
            if (member1 == null) return R.error("defMatchOrderId 有误！");

            List<TeamOrder> teamOrderList = teamOrderRepository.findTeamOrderByDefMatchOrderAndMember(defMatchOrder, member1);
            if(teamOrderList.size() > 0 ) return R.error("请勿重复操作。");
        } catch (Exception exception) {
            return R.error("请检查参数！");
        }

        if (defMatchOrder != null) {
            TeamOrderDTO teamOrderDTO = new TeamOrderDTO();
            teamOrderDTO.setId(teamOrderVO.getId());
            teamOrderDTO.setMemberId(teamOrderVO.getMemberId());
            teamOrderDTO.setDefMatchOrderId(defMatchOrder.getId());
            teamOrderDTO.setStatus(teamOrderVO.getStatus());
            teamOrderDTO.setIsLike(0);
            teamOrderService.create(teamOrderDTO);
        }

        return R.success();
    }

    @ApiOperation(value = "更新战队比赛内部申请")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R updateTeamOrder(@RequestParam Long memberId, @RequestBody TeamOrderVO teamOrderVO) {
        Optional<TeamOrder> teamOrderOptional = teamOrderRepository.findById(teamOrderVO.getId());
        if( !teamOrderOptional.isPresent() ) {
            return R.error("主键 id 有误");
        }

        DefMatchOrder defMatchOrder = null;
        try {
            Member member = memberRepository.findMemberById(memberId);
            if (member == null) return R.error("memberid 有误！");

            Team team = teamRepository.findTeamById(teamOrderVO.getTeamId());
            if (team == null) return R.error("defMatchOrderId 有误！");

            TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberAndTeam(member, team);
            if (teamMember == null) return R.error("memberId or teamId 有误！");
            if(teamMember.getJob() == 3) return R.error("操作者不是队长或副队长，没有权限！");

            DefMatch defMatch = defMatchRepository.findDefMatchById(teamOrderVO.getMatchId());

            DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatch);

            defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, teamOrderVO.getTeamId());

            Member member1 = memberRepository.findMemberById(teamOrderVO.getMemberId());
            if (member1 == null) return R.error("memberid 有误！");

            TeamOrder teamOrder = teamOrderOptional.get();
            if( teamOrder.getMember().getId() != teamOrderVO.getMemberId()) {
                return R.error("TeamOrderVO 里面 memberid 有问题");
            }

            if (defMatchOrder != null) {
                TeamOrderDTO teamOrderDTO = new TeamOrderDTO();
                teamOrderDTO.setId(teamOrderVO.getId());
                teamOrderDTO.setMemberId(teamOrderVO.getMemberId());
                teamOrderDTO.setDefMatchOrderId(defMatchOrder.getId());
                teamOrderDTO.setStatus(teamOrderVO.getStatus());
                teamOrderDTO.setIsLike(teamOrder.getIsLike());

                teamOrderService.update(teamOrderDTO);
            }
        } catch (Exception exception) {
            return R.error("请检查参数！");
        }

        return R.success();
    }

    @ApiOperation(value = "查询战队比赛内部申请")
    @RequestMapping(value = "/getTeamOrderById", method = RequestMethod.GET)
    public R getTeamOrderById(@RequestParam Long memberId, @RequestParam Long id) {
        try {
            Member member = memberRepository.findMemberById(memberId);
            if (member == null) return R.error("memberid 有误！");

        } catch (Exception exception) {
            return R.error("请检查参数！");
        }
        TeamOrderDTO teamOrderDTO = teamOrderService.findById(id);
        return R.success(teamOrderDTO);
    }

    @ApiOperation(value = "删除战队比赛内部申请")
    @RequestMapping(value = "/deleteTeamOrderById", method = RequestMethod.POST)
    public R deleteTeamOrderById(@RequestParam Long memberId, @RequestParam Long id) {
        try {
            Member member = memberRepository.findMemberById(memberId);
            if (member == null) return R.error("memberid 有误！");

        } catch (Exception exception) {
            return R.error("请检查参数！");
        }

        teamOrderService.delete(id);
        return R.success();
    }

    @ApiOperation(value = "查询指定预约比赛队内报名")
    @RequestMapping(value = "/getMatchesPageByDefMatchOrderIdAndStatus", method = RequestMethod.GET)
    public R getMatchesPageByDefMatchOrderIdAndStatus(
            @RequestParam Long defMatchOrderId,
            @RequestParam Integer status,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success(teamOrderService.findTeamOrderByDefMatchOrderIdAndStatus(defMatchOrderId, status, pageNum, pageSize));
    }

    @ApiOperation(value = "查询指定预约比赛队内报名")
    @RequestMapping(value = "/getMatchesPageByDefMatchOrderId", method = RequestMethod.GET)
    public R getMatchOrdersPageByDefMatchId(
            @RequestParam Long defMatchOrderId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success(teamOrderService.findTeamOrderByDefMatchOrderId(defMatchOrderId, pageNum, pageSize));
    }

    @ApiOperation(value = "点赞")
    @RequestMapping(value = "/putTeamOrderLike", method = RequestMethod.POST)
    public R putTeamOrderLike(@RequestParam Long memberId, @RequestParam Long teamId, @RequestParam Long matchId, @RequestParam Integer isLike) {

        Member member = memberRepository.findMemberById(memberId);
        DefMatchOrder defMatchOrder = null;
        try {
            if (member == null) return R.error("memberid 有误！");

            DefMatch defMatch = defMatchRepository.findDefMatchById(matchId);

            DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatch);

            // 个人赛点赞
            if ( defMatchManage.getMode() == 0 ) {
                defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, member.getId());
                PersonOrder personOrder = personOrderRepository.findPersonOrderByDefMatchOrderAndMember(defMatchOrder, member);
                personOrder.setIsLike(isLike);
                personOrderRepository.save(personOrder);
                return R.success();
            }

            defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, teamId);

            Team team = teamRepository.findTeamById(defMatchOrder.getOrderId());
            if (team == null) R.error("defMatchOrderId 有误！");

        } catch (Exception exception) {
            return R.error("请检查参数！");
        }

        if (defMatchOrder != null) {
            List<TeamOrder> teamOrderList = teamOrderRepository.findTeamOrderByDefMatchOrderAndMember(defMatchOrder, member);
            if(teamOrderList.size() == 0) return R.error("未找到对应报名，无法点赞");
            TeamOrderDTO teamOrderDTO = teamOrderConvert.toDto(teamOrderList.get(0));

            if(teamOrderDTO == null) return R.error("teamOrderDTO is null");
            teamOrderDTO.setIsLike(isLike);

            teamOrderService.update(teamOrderDTO);
        }

        return R.success();
    }

    @ApiOperation(value = "点赞")
    @RequestMapping(value = "/getTeamOrderLike", method = RequestMethod.GET)
    public R getTeamOrderLike(@RequestParam Long memberId, @RequestParam Long teamId, @RequestParam Long matchId) {

        Member member = memberRepository.findMemberById(memberId);
        DefMatchOrder defMatchOrder = null;
        TeamOrderDTO teamOrderDTO = null;
        try {
            if (member == null) return R.error("memberid 有误！");

            DefMatch defMatch = defMatchRepository.findDefMatchById(matchId);

            DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatch);

            // 个人赛点赞
            if ( defMatchManage.getMode() == 0 ) {
                defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, member.getId());
                PersonOrder personOrder = personOrderRepository.findPersonOrderByDefMatchOrderAndMember(defMatchOrder, member);
                System.out.println(" personOrder isLike:" + personOrder.getIsLike());
                return personOrder != null ? R.success( personOrder.getIsLike() ): R.success();
            }

            defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, teamId);

            Team team = teamRepository.findTeamById(defMatchOrder.getOrderId());
            if (team == null) R.error("defMatchOrderId 有误！");

        } catch (Exception exception) {
            return R.error("请检查参数！");
        }

        if (defMatchOrder != null) {
            List<TeamOrder> teamOrderList = teamOrderRepository.findTeamOrderByDefMatchOrderAndMember(defMatchOrder, member);
            if(teamOrderList.size() > 0)
                teamOrderDTO = teamOrderConvert.toDto(teamOrderList.get(0));
        }

        return teamOrderDTO != null ? R.success( teamOrderDTO.getIsLike() ): R.success();
    }

}
