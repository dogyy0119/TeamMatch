package com.gs.controller;

import com.gs.convert.DefMatchManageConvert;
import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.model.entity.jpa.db1.def.TeamOrder;
import com.gs.repository.jpa.def.DefMatchManageRepository;
import com.gs.service.intf.def.DefMatchManageService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(tags = "自定义比赛管理接口")
@RestController
@RequestMapping("/game/v1.0/app/matches/DefMatchManage")
@AllArgsConstructor
public class DefMatchManageController {

    @Autowired
    private DefMatchManageService defMatchManageService;

    @Autowired
    private DefMatchManageRepository defMatchManageRepository;

    @Autowired
    private DefMatchManageConvert defMatchManageConvert;

    @ApiOperation(value = "创建自定义管理")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public R addManage(@RequestBody DefMatchManageDTO defMatchManageDTO) {

        DefMatchManageDTO dto1 = defMatchManageService.findByMatchId(defMatchManageDTO.getMatchId());
        if(dto1 != null) {
            return R.error("请勿重复创建");
        }

        DefMatchManageDTO dto = defMatchManageService.create( defMatchManageDTO );
        return R.success(dto);
    }

    @ApiOperation(value = "更新自定义管理")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R updateManage(@RequestBody DefMatchManageDTO defMatchManageDTO) {
        Optional<DefMatchManage> defMatchManage = defMatchManageRepository.findById(defMatchManageDTO.getId());
        if( !defMatchManage.isPresent() ) {
            return R.error("主键 id 有误");
        }

        Boolean res = defMatchManageService.update( defMatchManageDTO );
        if ( res ) {
            return R.success("成功");
        } else  {
            return R.error("失败");
        }
    }

    @ApiOperation(value = "查询自定义管理")
    @RequestMapping(value = "/getManageById", method = RequestMethod.GET)
    public R getManageById(@RequestParam Long  id) {
        DefMatchManageDTO dto = defMatchManageService.findById( id );
        return R.success(dto);
    }

    @ApiOperation(value = "删除自定义管理")
    @RequestMapping(value = "/deleteManageById", method = RequestMethod.POST)
    public R deleteManageById(@RequestParam Long id) {
        defMatchManageService.delete( id );
        return R.success();
    }

    @ApiOperation(value = "根据比赛查询预约申请")
    @RequestMapping(value = "/getMatchManagesPageByMatch", method = RequestMethod.GET)
    public R getMatchManagesPageByMatch(
            @RequestParam Long memberId,
            @RequestParam Long matchId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( defMatchManageService.getMatchManagesPageByMatch(memberId, matchId, pageNum, pageSize));
    }

}
