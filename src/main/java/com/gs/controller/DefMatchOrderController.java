package com.gs.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gs.convert.DefMatchManageConvert;
import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.dto.vo.OrderMatch;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.repository.jpa.def.DefMatchOrderRepository;
import com.gs.repository.jpa.def.DefMatchRepository;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamMemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.def.DefMatchManageService;
import com.gs.service.intf.def.DefMatchOrderService;
import com.gs.utils.HttpUtils;
import com.gs.utils.PayCenterUtil;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = "自定义比赛申请接口")
@RestController
@RequestMapping("/game/v1.0/app/matches/DefMatchOrder")
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

    @Autowired
    private DefMatchRepository defMatchRepository;

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
           if( !isExists ) return R.error("请先创建或加入战队，再申请加入比赛");

            Member member = memberRepository.findMemberById(memberId);
            if(member == null) return R.error("MemberId 有问题 ！");

            Team team = teamRepository.findTeamById(defMatchOrderDTO.getOrderId());
            TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberAndTeam(member,team);

            if( teamMember.getJob()  == 3 ) {
                return R.error("您还不是队长或者副队长 ！");
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

        if(defMatchOrder != null)  {
            return R.error("该比赛已经申请过了");
        }

        DefMatch defMatch = defMatchRepository.findDefMatchById(defMatchManageDTO.getMatchId());
        if(!defMatch.getGameBill().toString().equals("0")) {
            float fee = 0;
            if (defMatchOrderDTO.getMode() == 1) {
                fee = PayCenterUtil.QueryTeamFunds(defMatchOrderDTO.getOrderId().toString());
                if(fee < defMatch.getGameBill()){
                    return R.error("战队账户余额不足");
                }
            } else {
                fee = PayCenterUtil.QueryMemberFunds(memberId.toString());
                if(fee < defMatch.getGameBill()){
                    return R.error("个人账户余额不足");
                }
            }
        }

        DefMatchOrderDTO dto = defMatchOrderService.create(defMatchOrderDTO);

        defMatchManageService.update(defMatchManageDTO);
        return R.success(dto);
    }

    @ApiOperation(value = "更新自定义管理")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R updateMatchOrder(@RequestParam Long memberId, @RequestBody DefMatchOrderDTO defMatchOrderDTO) {


//        Optional<DefMatchOrder> defMatchOrder = defMatchOrderRepository.findById(defMatchOrderDTO.getId());
//        if( !defMatchOrder.isPresent() ) {
//            return R.error("主键 id 有误");
//        }

        try {
            DefMatchManageDTO defMatchManageDTO = defMatchManageService.findByMatchId(defMatchOrderDTO.getMatchId());
            DefMatchOrderDTO defMatchOrderDTO1 = defMatchOrderService.findByMatchIdAndOrderId(defMatchManageDTO.getMatchId(), defMatchOrderDTO.getOrderId());


            // 申请加入比赛通过
            if (defMatchOrderDTO.getStatus() == 1
                    && defMatchOrderDTO1.getStatus() != 1
                    && defMatchManageDTO.getAllOrder() > defMatchManageDTO.getCurOrder()) {
                defMatchManageDTO.setCurOrder(defMatchManageDTO.getCurOrder() + 1);
                defMatchManageService.update(defMatchManageDTO);

                // 构建cost 请求
                DefMatch defMatch = defMatchRepository.findDefMatchById(defMatchManageDTO.getMatchId());
                if(!defMatch.getGameBill().toString().equals("0")) {
                    float fee = 0;
                    if (defMatchOrderDTO.getMode() == 1) {
                        fee = PayCenterUtil.QueryTeamFunds(defMatchOrderDTO.getOrderId().toString());
                        if(fee >= defMatch.getGameBill()){
                            PayCenterUtil.PayTeamCost(memberId.toString(), defMatchOrderDTO.getOrderId().toString(),defMatch.getGameBill().toString());
                        } else {
                            return R.error("战队账户余额不足");
                        }
                    } else {
                        fee = PayCenterUtil.QueryMemberFunds(memberId.toString());
                        if(fee >= defMatch.getGameBill()){
                            PayCenterUtil.PayPersonCost(memberId.toString(),defMatch.getGameBill().toString());
                        } else {
                            return R.error("个人账户余额不足");
                        }
                    }

                }
            }

            // 比赛退出，退还比赛币
            if (defMatchOrderDTO.getStatus() == -1) {
                if (defMatchOrderDTO1.getStatus() == 1) {
                    defMatchManageDTO.setCurOrder(defMatchManageDTO.getCurOrder() - 1);
                    defMatchManageService.update(defMatchManageDTO);

                    // 构建cost 请求
                    DefMatch defMatch = defMatchRepository.findDefMatchById(defMatchManageDTO.getMatchId());
                    if(!defMatch.getGameBill().toString().equals("0")) {
                        if (defMatchOrderDTO.getMode() == 1) {
                            PayCenterUtil.PayTeamCost(memberId.toString(), defMatchOrderDTO.getOrderId().toString(),defMatch.getGameBill().toString());
                        } else {
                            PayCenterUtil.PayPersonCost(memberId.toString(),defMatch.getGameBill().toString());
                        }
                    }
                }
            }

            boolean success = defMatchOrderService.update(defMatchOrderDTO);
            if ( !success ) {
                return R.error("失败");
            }

        } catch (Exception exception) {
            return R.error("异常");
        }

        return R.success("成功");
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

        List<DefMatchDTO> defMatchDTOList = defMatchOrderService.getMatchPage( 0, memberId, status, pageNum, pageSize );

        List<DefMatchDTO> defMatchDTOS = new ArrayList<>();
        for (DefMatchDTO entity : defMatchDTOList){
            // 过期的比赛不显示
            if(entity.getGameStartTime().after(new Date())) {
                defMatchDTOS.add(entity);
            }
        }

        return R.success( defMatchDTOS );
    }

    @ApiOperation(value = "分页查询指定战队ID预约比赛")
    @RequestMapping(value = "/getMatchesPageByTeamId", method = RequestMethod.GET)
    public R getMatchOrdersPageByTeamId(
            @RequestParam(value = "teamId",required = false) Long teamId,
            @RequestParam Integer status,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        if(teamId == null){
            return R.error("还没加入战队");
        }

        List<DefMatchDTO> defMatchDTOList = defMatchOrderService.getMatchPage( 1, teamId, status, pageNum, pageSize );

        if(defMatchDTOList.size() == 0) {
            return R.success();
        }

        List<DefMatchDTO> defMatchDTOS = new ArrayList<>();
        for (DefMatchDTO entity : defMatchDTOList){
            // 过期的比赛不显示
            if(entity.getGameStartTime().after(new Date())) {
                defMatchDTOS.add(entity);
            }
        }

        return R.success( defMatchDTOS );
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

    @ApiOperation(value = "查询指定状态指定比赛预约申请")
    @RequestMapping(value = "/getMatchOrdersSuccessPageByMatchId", method = RequestMethod.GET)
    public R getMatchOrdersSuccessPageByMatchId(
            @RequestParam Long matchId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        DefMatch defMatch = defMatchRepository.findDefMatchById(matchId);

        if(defMatch == null) {
             return R.error( "请检查 MatchId");
        }

        List<DefMatchOrderDTO> defMatchOrderDTOS = defMatchOrderService.getMatchOrdersSuccessPageByMatchId(  matchId,  pageNum, pageSize);

        if ( defMatchOrderDTOS.size() == 0 ) {
            return R.success();
        }

//        Map<String,String> gameList = new HashMap<>();
        List<OrderMatch> gameList = new ArrayList<>();
        for ( DefMatchOrderDTO defMatchOrderDTO : defMatchOrderDTOS ) {
           if ( defMatchOrderDTO.getMode() == 0 ) {
               Member member = memberRepository.findMemberById( defMatchOrderDTO.getOrderId() );
               if ( member != null ) {

                   OrderMatch orderMatch = new OrderMatch();
                   orderMatch.setName(member.getName());
                   gameList.add(orderMatch);
               }
           } else {
               Team team = teamRepository.findTeamById( defMatchOrderDTO.getOrderId() );
               if ( team != null ) {
//                   gameList.put("name",team.getName());
                   OrderMatch orderMatch = new OrderMatch();
                   orderMatch.setName(team.getName());
                   gameList.add(orderMatch);
               }
           }
        }

        JSONArray jsonArray = new JSONArray(gameList);

        return R.success( jsonArray );
    }

    @ApiOperation(value = "删除战队报名信息")
    @RequestMapping(value = "/deleteMatchOrderByTeamId", method = RequestMethod.GET)
    public R deleteMatchOrderByTeamId(
            @RequestParam Long teamId ) {
            defMatchOrderService.deleteByTeamId(teamId);
            return R.success();
        }
    }
