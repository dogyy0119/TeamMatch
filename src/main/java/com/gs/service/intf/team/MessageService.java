package com.gs.service.intf.team;

import com.gs.model.dto.team.MessageDto;
import com.gs.model.vo.team.MessageVo;

import java.util.List;

public interface MessageService {

    MessageVo insertMessage(MessageDto dto);

    List<MessageVo> getTeamChatMsgs(String teamId, Long memberId, Integer pageNum, Integer pageSize);

    void deleteTeamChatMsgs(Long teamId, Long memberId);
}
