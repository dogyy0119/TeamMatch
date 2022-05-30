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
@RequestMapping("/game/v1.0/gameteam/manager")
@Validated
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "获取历史聊天信息")
    @RequestMapping(value = "/getTeamChatMsgs", method = RequestMethod.GET)
    public R getTeamChatMsgs(@RequestParam String teamId,
                             @RequestParam Long memberId,
                             @RequestParam Integer pageNum,
                             @RequestParam Integer pageSize) {

        return R.success(messageService.getTeamChatMsgs(teamId, memberId, pageNum, pageSize));
    }

    @ApiOperation(value = "删除所有已发送的消息")
    @RequestMapping(value = "/deleteTeamChatMsgs", method = RequestMethod.GET)
    public R deleteTeamChatMsgs(@RequestParam Long teamId,
                             @RequestParam Long memberId) {

        messageService.deleteTeamChatMsgs(teamId, memberId);
        return R.success();
    }
}
