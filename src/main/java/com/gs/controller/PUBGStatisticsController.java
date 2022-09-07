package com.gs.controller;

import com.gs.model.entity.jpa.db1.game.PUBGSeason;
import com.gs.service.intf.game.PUBGStatisticsService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@Api(tags = "PUBG比赛数据接口")
@RestController
@RequestMapping("/game/v1.0/app/matches/PUBGStatistics")
@AllArgsConstructor
public class PUBGStatisticsController {

    @Autowired
    private PUBGStatisticsService pubgStatisticsService;

    @ApiOperation(value = "获取赛季列表")
    @RequestMapping(value = "/GetSeasons", method = RequestMethod.GET)
    public R GetSeasons() {
        List<PUBGSeason> pubgSeasonList = pubgStatisticsService.GetSeasons();
        return R.success(pubgSeasonList);
    }

    @ApiOperation(value = "获取当前赛季")
    @RequestMapping(value = "/GetCurSeason", method = RequestMethod.GET)
    public R GetCurSeason() {
        PUBGSeason pubgSeason = pubgStatisticsService.GetCurSeason();
        return R.success(pubgSeason);
    }

    @ApiOperation(value = "获取上个赛季")
    @RequestMapping(value = "/GetLastSeason", method = RequestMethod.GET)
    public R GetLastSeason() {
        PUBGSeason pubgSeason = pubgStatisticsService.GetLastSeason();
        return R.success(pubgSeason);
    }

    @ApiOperation(value = "获取单个玩家的赛季信息")
    @RequestMapping(value = "/GetSeasonStats", method = RequestMethod.GET)
    public R GetSeasonStats(@RequestParam(defaultValue = "account.8ce37bd790b94cdbb4ee6d5226b4fa24") String accountid,
                            @RequestParam(defaultValue = "division.bro.official.pc-2018-19") String seasonid) {
        String pubgSeason = pubgStatisticsService.GetSeasonStats(accountid, seasonid);
        return R.success(pubgSeason);
    }

    @ApiOperation(value = "获取单个玩家的排名统计数据")
    @RequestMapping(value = "/GetRankedSeasonStats", method = RequestMethod.GET)
    public R GetRankedSeasonStats(@RequestParam(defaultValue = "account.8ce37bd790b94cdbb4ee6d5226b4fa24") String accountid,
                                  @RequestParam(defaultValue = "division.bro.official.pc-2018-18") String seasonid) {
        String pubgSeason = pubgStatisticsService.GetRankedSeasonStats(accountid, seasonid);
        return R.success(pubgSeason);
    }

    @ApiOperation(value = "获取职业生涯统计")
    @RequestMapping(value = "/GetLifetimeStats", method = RequestMethod.GET)
    public R GetLifetimeStats(@RequestParam(defaultValue = "account.8ce37bd790b94cdbb4ee6d5226b4fa24") String accountid) {
        String pubgSeason = pubgStatisticsService.GetLifetimeStats(accountid);
        return R.success(pubgSeason);
    }

    @ApiOperation(value = "获取指定模式职业生涯统计")
    @RequestMapping(value = "/GetGameModeLifetimeStats", method = RequestMethod.GET)
    public R GetLifetimeStats(@RequestParam String gameMode,
                              @RequestParam(defaultValue = "account.8ce37bd790b94cdbb4ee6d5226b4fa24")  String playerIds,
                              @RequestParam(defaultValue = "false")  Boolean gamepad) {
        String pubgSeason = pubgStatisticsService.GetLifetimeStats(gameMode, playerIds, gamepad);
        return R.success(pubgSeason);
    }

    @ApiOperation(value = "获取职业生涯统计")
    @RequestMapping(value = "/GetMatch", method = RequestMethod.GET)
    public R GetMatch(@RequestParam String matchId) {
        String pubgSeason = pubgStatisticsService.GetMatch(matchId);
        return R.success(pubgSeason);
    }

    @ApiOperation(value = "获取用户信息统计")
    @RequestMapping(value = "/GetPlayerById", method = RequestMethod.GET)
    public R GetPlayerById(@RequestParam(defaultValue = "account.8ce37bd790b94cdbb4ee6d5226b4fa24")  String playerIds) {
        List<String> result = pubgStatisticsService.GetPlayerById(playerIds);
        return R.success(result);
    }

    @ApiOperation(value = "获取比赛信息")
    @RequestMapping(value = "/GetMatchById", method = RequestMethod.GET)
    public R GetMatchById(@RequestParam(defaultValue = "account.8ce37bd790b94cdbb4ee6d5226b4fa24") String pubgId,@RequestParam  String matchid) throws ParseException {
        String result = pubgStatisticsService.GetMatchById(pubgId, matchid);
        return R.success(result);
    }

}
