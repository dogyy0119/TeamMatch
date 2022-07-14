package com.gs.controller;

import cn.hutool.json.JSONUtil;
import com.gs.model.dto.game.PUBGMatchesDTO;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.repository.jpa.def.DefMatchRepository;
import com.gs.service.intf.game.PUBGMatchesService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.hutool.json.JSONObject;

@Api(tags = "获取PUBG比赛数据接口")
@RestController
@RequestMapping("/game/v1.0/app/matches/PUBG/Manager")
@AllArgsConstructor
public class PUBGMatchesController {

    @Autowired
    private PUBGMatchesService pubgMatchesService;

    @Autowired
    private DefMatchRepository defMatchRepository;

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

    @ApiOperation(value = "查询memberId 创建的比赛")
    @RequestMapping(value = "/getPUBGMatchesByMemberId", method = RequestMethod.GET)
    public R getPUBGMatchesByMemberId(
            @RequestParam Long memberId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( pubgMatchesService.getPUBGMatchesByMemberId(memberId,pageNum,pageSize) );
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
            @RequestParam Long defMatchId,
            @RequestParam Integer index) {

        return R.success( pubgMatchesService.getPUBGMatchesByDefMatchId(defMatchId,index) );
    }

    @ApiOperation(value = "修改PUBGName和defMatchId比赛")
    @RequestMapping(value = "/updatePUBGMatchesByDefMatchId", method = RequestMethod.POST)
    public R updatePUBGMatchesByDefMatchId(
            @RequestParam Long memberId,
            @RequestParam Long defMatchId,
            @RequestParam Integer index,
            @RequestBody String body) {
        DefMatch defMatch = defMatchRepository.findDefMatchById(defMatchId);
        JSONObject requestJson = new JSONObject(body);
        JSONObject pubgMatchesDTOJson = requestJson.getJSONObject("pubgMatchesDTO");
        String token = requestJson.get("token").toString();

        System.out.println("tokenJson:" + token);

        PUBGMatchesDTO pubgMatchesDTO = JSONUtil.toBean(pubgMatchesDTOJson, PUBGMatchesDTO.class);

        System.out.println("memberId:" + memberId + "defMatchId:" + defMatchId + "index:" + index);
        System.out.println("pubgMatchesDTO:" + pubgMatchesDTO.getPubgMatchesId());

        if(defMatch.getMember().getId() != memberId) return R.error("memberId 不是比赛创建者，无权利修改比赛");

        pubgMatchesService.updatePUBGMatchesByDefMatchId(memberId,defMatchId,index,pubgMatchesDTO);
        return R.success( );
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

    @ApiOperation(value = "创建比赛的管理者查询比赛进行中的数据")
    @RequestMapping(value = "/getRunningPUBGMatchesByCreatId", method = RequestMethod.GET)
    public R getRunningPUBGMatchesByCreatId(
            @RequestParam Long memberId ) {
        return R.success( pubgMatchesService.getRunningPUBGMatchesByCreatId(memberId) );
    }

    @ApiOperation(value = "查询参与比赛进行中的数据")
    @RequestMapping(value = "/getRunningPUBGMatchesByOrderId", method = RequestMethod.GET)
    public R getRunningPUBGMatchesByOrderId(
            @RequestParam Long memberId,
            @RequestParam Long teamId) {
        return R.success( pubgMatchesService.getRunningPUBGMatchesByOrderId(memberId, teamId) );
    }

}