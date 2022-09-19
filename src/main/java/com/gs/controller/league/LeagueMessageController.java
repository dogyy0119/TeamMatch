package com.gs.controller.league;

import com.gs.service.intf.league.LeagueMessageService;
import com.gs.service.intf.team.MessageService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "消息管理接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
@Slf4j
public class LeagueMessageController {

    private final LeagueMessageService messageService;

    @ApiOperation(value = "获取联盟群聊历史信息")
    @RequestMapping(value = "/getLeagueGroupChatMsgs", method = RequestMethod.GET)
    public R getLeagueGroupChatMsgs(@RequestParam Long leagueId,
                              @RequestParam Integer pageNum,
                              @RequestParam Integer pageSize) {
        log.info("getLeagueGroupChatMsgs：" + "leagueId = " + leagueId + "pageNum = " + pageNum + "pageSize = " + pageSize);

        return R.success(messageService.getLeagueGroupChatMsgs(leagueId, pageNum, pageSize));
    }


    @ApiOperation(value = "获取联盟私聊历史信息")
    @RequestMapping(value = "/getLeaguePrivateChatMsgs", method = RequestMethod.GET)
    public R getLeaguePrivateChatMsgs(@RequestParam Long leagueId,
                                @RequestParam Long fromId,
                                @RequestParam Long toId,
                                @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {

        log.info("getLeagueGroupChatMsgs：" + "fromId = " + fromId + "toId = " + toId + "leagueId = " + leagueId + "pageNum = " + pageNum + "pageSize = " + pageSize);
        return R.success(messageService.getLeaguePrivateChatMsgs(leagueId, fromId, toId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除所有已发送的消息")
    @RequestMapping(value = "/deleteLeagueChatMsgs", method = RequestMethod.GET)
    public R deleteLeagueChatMsgs(@RequestParam Long leagueId,
                                @RequestParam Long memberId) {

        log.info("getLeagueGroupChatMsgs：" + "leagueId = " + leagueId + "memberId = " + memberId);
        messageService.deleteLeagueChatMsgs(leagueId, memberId);
        return R.success();
    }
}
