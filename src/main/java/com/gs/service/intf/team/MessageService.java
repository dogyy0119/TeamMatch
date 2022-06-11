package com.gs.service.intf.team;

import com.gs.model.dto.team.MessageDto;
import com.gs.model.vo.team.MessageVo;

import java.util.Date;
import java.util.List;

public interface MessageService {

    MessageVo insertMessage(MessageDto dto);


    List<MessageVo> getGroupChatMsgs(String teamId, Integer pageNum, Integer pageSize);

    void deleteGroupChatMsgs(String teamId);

    void deletePrivateChatMsgs(String teamId, Long fromId, Long toId);
    List<MessageVo> getPrivateChatMsgs(String teamId, Long fromId, Long toId, Integer pageNum, Integer pageSize);


    void deleteTeamChatMsgs(Date date);

    void deleteTeamChatMsgs(Long teamId, Long memberId);
}
