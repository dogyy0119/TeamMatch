package com.gs.controller.match.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.league.LeagueCreateDTO;
import com.gs.service.intf.league.LeagueRequestService;
import com.gs.service.intf.league.LeagueService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 联盟管理接口
 * User: lys
 * DateTime: 2022-04-22
 **/

@Api(tags = "联盟管理接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
public class LeagueMatchWholeController {

    private final LeagueService leagueService;

    private final LeagueRequestService leagueRequestService;

    @ApiOperation(value = "创建联盟")
    @RequestMapping(value = "/createLeague111", method = RequestMethod.POST)
    public R createLeague(
            @Validated @RequestBody LeagueCreateDTO leagueCreateDTO){
        if (leagueService.existsByCreateMemberId(leagueCreateDTO.getCreateMemberId())){
            return R.error(CodeEnum.IS_ALREADY_CREATE_LEAGUE.getCode(), "创建战队失败:该用户已经创建过联盟");
        }
        return R.success(leagueService.createLeague(leagueCreateDTO));
    }


}
