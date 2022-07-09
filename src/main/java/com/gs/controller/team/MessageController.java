package com.gs.controller.team;

import com.gs.service.intf.team.MessageService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "消息管理接口")
@RestController
@RequestMapping("/game/v1.0/app/gameteam/manager")
@Validated
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "获取群聊历史信息")
    @RequestMapping(value = "/getGroupChatMsgs", method = RequestMethod.GET)
    public R getGroupChatMsgs(@RequestParam String teamId,
                              @RequestParam Integer pageNum,
                              @RequestParam Integer pageSize) {

        return R.success(messageService.getGroupChatMsgs(teamId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除群聊历史信息")
    @RequestMapping(value = "/deleteGroupChatMsgs", method = RequestMethod.GET)
    public R deleteGroupChatMsgs(@RequestParam String teamId) {

        messageService.deleteGroupChatMsgs(teamId);
        return R.success();
    }

    @ApiOperation(value = "获取私聊历史信息")
    @RequestMapping(value = "/getPrivateChatMsgs", method = RequestMethod.GET)
    public R getPrivateChatMsgs(@RequestParam String teamId,
                                @RequestParam Long fromId,
                                @RequestParam Long toId,
                                @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {

        return R.success(messageService.getPrivateChatMsgs(teamId, fromId, toId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除私聊历史信息")
    @RequestMapping(value = "/deletePrivateChatMsgs", method = RequestMethod.GET)
    public R deletePrivateChatMsgs(@RequestParam String teamId,
                                   @RequestParam Long fromId,
                                   @RequestParam Long toId) {

        messageService.deletePrivateChatMsgs(teamId, fromId, toId);
        return R.success();
    }

    @ApiOperation(value = "删除所有已发送的消息")
    @RequestMapping(value = "/deleteTeamChatMsgs", method = RequestMethod.GET)
    public R deleteTeamChatMsgs(@RequestParam Long teamId,
                                @RequestParam Long memberId) {

        messageService.deleteTeamChatMsgs(teamId, memberId);
        return R.success();
    }
}
