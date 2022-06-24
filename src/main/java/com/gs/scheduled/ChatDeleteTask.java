package com.gs.scheduled;

import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import com.gs.model.entity.jpa.db1.league.LeagueMessage;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.MessageRepository;
import com.gs.service.intf.def.DefMatchService;
import com.gs.service.intf.game.PUBGMatchesService;
import com.gs.service.intf.league.LeagueMessageService;
import com.gs.service.intf.team.MessageService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 *  ScheduledTask 配置类。
 */
@Component
@Async
public class ChatDeleteTask {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ChatDeleteTask.class);
    @Resource
    private MessageService messageService;

    @Resource
    private LeagueMessageService leagueMessageService;

    @Scheduled(cron = "59 59 23 * * ?")
    public void deleteChatMsgs() {
        System.err.println("执行静态定时删除任务战队聊天记录: " + LocalDateTime.now());

        Calendar gregorianCalendar = Calendar.getInstance();
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -2);
        //gregorianCalendar.add(Calendar.MINUTE, -100);

        messageService.deleteTeamChatMsgs(gregorianCalendar.getTime());
        leagueMessageService.deleteLeagueChatMsgs(gregorianCalendar.getTime());

    }
}
