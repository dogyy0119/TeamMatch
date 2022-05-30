package com.gs.controller;

import com.gs.convert.DefMatchManageConvert;
import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.repository.jpa.def.DefMatchOrderRepository;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamMemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.def.DefMatchManageService;
import com.gs.service.intf.def.DefMatchOrderService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(tags = "自定义比赛申请接口")
@RestController
@RequestMapping("/game/v1.0/matches/DefMatchOrder")
@AllArgsConstructor
public class DefMatchOrderController {

    @Autowired
    private DefMatchOrderService defMatchOrderService;

    @Autowired
    private DefMatchOrderRepository defMatchOrderRepository;

    @Autowired
    private DefMatchManageService defMatchManageService;

    @Autowired
    private DefMatchManageConvert defMatchManageConvert;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @ApiOperation(value = "创建自定义管理")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public R addMatchOrder(@RequestParam Long memberId, @RequestBody DefMatchOrderDTO defMatchOrderDTO) {

        DefMatchManageDTO defMatchManageDTO = defMatchManageService.findByMatchId(defMatchOrderDTO.getMatchId());
        // 检查报名比赛是否存在
        if (defMatchManageDTO == null) {
            return R.error("报名比赛不存在 ！");
        }

        // 检查报名通过人数
        if (defMatchManageDTO.getCurOrder() == defMatchManageDTO.getAllOrder()) {
            return R.error("报名已满 ！");
        }

        // 检查申请模式
        if (defMatchManageDTO.getMode() != defMatchOrderDTO.getMode()) {
            return R.error("报名模式错误 ！");
        }

        // 队长报名
        if (defMatchManageDTO.getMode() == 1 ) {
           Boolean isExists = teamRepository.existsTeamById(defMatchOrderDTO.getOrderId());
           if( !isExists ) return R.error("战队id 有问题");

            Member member = memberRepository.findMemberById(memberId);
            if(member == null) return R.error("MemberId 有问题 ！");

            Team team = teamRepository.findTeamById(defMatchOrderDTO.getOrderId());

            TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberAndTeam(member,team);

//            System.out.println(" teamMember:" + teamMember.getTeamMemberId() );
//            System.out.println(" teamMember:" + teamMember.getTeam().getTeamId() );
            System.out.println(" teamMember:" + teamMember.getJob() );

            if( teamMember.getJob()  == 3 ) {
                return R.error("MemberId 不是队长或者副队长 ！");
            }

        }

        // 检查报名id
        if (defMatchOrderDTO.getMode() == 0) { // 个人报名
            if (memberRepository.findMemberById(defMatchOrderDTO.getOrderId()) == null) {
                return R.error("报名id有问题");
            }
        }

        // 创建时，确保申请状态
        if (defMatchOrderDTO.getStatus() != 0) {
            defMatchOrderDTO.setStatus(0);
        }

        DefMatchOrder defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManageConvert.toEntity(defMatchManageDTO), defMatchOrderDTO.getOrderId());

        if(defMatchOrder != null)  { return R.error("该比赛已经申请过了"); }

        DefMatchOrderDTO dto = defMatchOrderService.create(defMatchOrderDTO);

        defMatchManageService.update(defMatchManageDTO);
        return R.success(dto);
    }

    @ApiOperation(value = "更新自定义管理")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R updateMatchOrder(@RequestParam Long memberId, @RequestBody DefMatchOrderDTO defMatchOrderDTO) {

        Optional<DefMatchOrder> defMatchManage = defMatchOrderRepository.findById(defMatchOrderDTO.getId());
        if( !defMatchManage.isPresent() ) {
            return R.error("主键 id 有误");
        }

        try {
            DefMatchManageDTO defMatchManageDTO = defMatchManageService.findByMatchId(defMatchOrderDTO.getMatchId());
            DefMatchOrderDTO defMatchOrderDTO1 = defMatchOrderService.findByMatchIdAndOrderId(defMatchManageDTO.getMatchId(), defMatchOrderDTO.getOrderId());
            // 申请加入比赛通过
            if (defMatchOrderDTO.getStatus() == 1
                    && defMatchOrderDTO1.getStatus() != 1
                    && defMatchManageDTO.getAllOrder() > defMatchManageDTO.getCurOrder()) {
                defMatchManageDTO.setCurOrder(defMatchManageDTO.getCurOrder() + 1);
                defMatchManageService.update(defMatchManageDTO);
            }

            if (defMatchOrderDTO.getStatus() == -1) {
                if (defMatchOrderDTO1.getStatus() == 1) {
                    defMatchManageDTO.setCurOrder(defMatchManageDTO.getCurOrder() - 1);
                    defMatchManageService.update(defMatchManageDTO);
                }
            }
        } catch (Exception exception) {
            return R.error("");
        }
        defMatchOrderService.update(defMatchOrderDTO);

        return R.success();
    }

    @ApiOperation(value = "查询自定义管理")
    @RequestMapping(value = "/getMatchOrderById", method = RequestMethod.GET)
    public R getMatchOrderById(@RequestParam Long memberId, @RequestParam Long id) {
        DefMatchOrderDTO defMatchOrderDTO = defMatchOrderService.findById(id);
        System.out.println("find id");
        return R.success(defMatchOrderDTO);
    }

    @ApiOperation(value = "删除自定义管理")
    @RequestMapping(value = "/deleteMatchOrderById", method = RequestMethod.POST)
    public R deleteMatchOrderById(@RequestParam Long memberId, @RequestParam Long id) {
        defMatchOrderService.delete(id);
        return R.success();
    }

    @ApiOperation(value = "分页查询指定用户ID预约比赛")
    @RequestMapping(value = "/getMatchesPageByMemberId", method = RequestMethod.GET)
    public R getMatchOrdersPageByMemberId(
            @RequestParam Long memberId,
            @RequestParam Integer status,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success(defMatchOrderService.getMatchPage( 0, memberId, status, pageNum, pageSize ) );
    }

    @ApiOperation(value = "分页查询指定战队ID预约比赛")
    @RequestMapping(value = "/getMatchesPageByTeamId", method = RequestMethod.GET)
    public R getMatchOrdersPageByTeamId(
            @RequestParam Long teamId,
            @RequestParam Integer status,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success(defMatchOrderService.getMatchPage( 1, teamId, status, pageNum, pageSize ) );
    }

    @ApiOperation(value = "查询指定比赛预约申请")
    @RequestMapping(value = "/getMatchOrdersPageByMatchId", method = RequestMethod.GET)
    public R getMatchOrdersPageByMatchId(
            @RequestParam Long memberId,
            @RequestParam Long matchId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( defMatchOrderService.getMatchOrdersPageByMatchId( memberId, matchId, pageNum, pageSize));
    }

    @ApiOperation(value = "查询指定状态指定比赛预约申请")
    @RequestMapping(value = "/getMatchOrdersPageByMatchIdAndStatus", method = RequestMethod.GET)
    public R getMatchOrdersPageByMatchIdAndStatus(
            @RequestParam Long memberId,
            @RequestParam Long matchId,
            @RequestParam Integer status,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( defMatchOrderService.getMatchOrdersPageByMatchIdAndStatus( memberId, matchId, status, pageNum, pageSize));
    }
}
