package com.gs.controller;

import com.gs.service.intf.game.PUBGMatchesService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "获取PUBG比赛数据接口")
@RestController
@RequestMapping("/game/v1.0/matches/PUBG/Manager")
@AllArgsConstructor
public class PUBGMatchesController {

    @Autowired
    private PUBGMatchesService pubgMatchesService;

    @ApiOperation(value = "创建比赛")
    @PostMapping
    public R add(@RequestParam String pubgMatchesId, @RequestParam  Long defMatchId) {
        pubgMatchesService.create(pubgMatchesId, defMatchId);
        return R.success();
    }

    @ApiOperation(value = "删除比赛")
    @DeleteMapping
    public R delete(@RequestParam("id") String id) {
        pubgMatchesService.delete(id);
        return R.success();
    }

    @ApiOperation(value = "查询比赛")
    @RequestMapping(value = "/getPUBGMatchesByKey", method = RequestMethod.GET)
    public R getPUBGMatchesByKey(
            @RequestParam String key,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( pubgMatchesService.queryPBUGMatchBykey(key,pageNum,pageSize) );
    }

    @ApiOperation(value = "查询PUBGName和defMatchId比赛")
    @RequestMapping(value = "/getPUBGMatchesByDefMatchId", method = RequestMethod.GET)
    public R getPUBGMatchesByDefMatchId(
            @RequestParam Long memberId,
            @RequestParam Long defMatchId,
            @RequestParam Integer index) {

        return R.success( pubgMatchesService.getPUBGMatchesByDefMatchId(memberId,defMatchId,index) );
    }


    @ApiOperation(value = "查询PUBGName和MatchType比赛")
    @RequestMapping(value = "/getPUBGMatchesByMatchType", method = RequestMethod.GET)
    public R getPUBGMatchesByMatchType(
            @RequestParam Long memberId,
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( pubgMatchesService.getPUBGMatchesByMatchType(memberId,matchType,pageNum,pageSize) );
    }

    @ApiOperation(value = "查询PUBGName和MatchType比赛排名")
    @RequestMapping(value = "/getPUBGMatchesSortByMatchType", method = RequestMethod.GET)
    public R getPUBGMatchesSortByMatchType(
            @RequestParam Long memberId,
            @RequestParam Long teamId,
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( pubgMatchesService.getPUBGMatchesSortByMatchType(memberId,teamId,matchType,pageNum,pageSize) );
    }



    @ApiOperation(value = "查询PUBGName和MatchType比赛")
    @RequestMapping(value = "/getPUBGMatchesByPUBGPlayerNameAndMatchType", method = RequestMethod.GET)
    public R getPUBGMatchesByPUBGPlayerNameAndMatchType(
            @RequestParam String PUBGPlayerName,
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( pubgMatchesService.findPUBGMatchesByPUBGPlayerNameAndMatchType(PUBGPlayerName,matchType,pageNum,pageSize));
    }

    @ApiOperation(value = "查询PUBGPlayerId和MatchType比赛")
    @RequestMapping(value = "/getPUBGMatchesByPUBGPlayerIdAndMatchType", method = RequestMethod.GET)
    public R getPUBGMatchesByPUBGPlayerIdAndMatchType(
            @RequestParam String PUBGPlayerId,
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( pubgMatchesService.findPUBGMatchesByPUBGPlayerIdAndMatchType(PUBGPlayerId,matchType,pageNum,pageSize));
    }

}