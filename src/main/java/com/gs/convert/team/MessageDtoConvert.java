package com.gs.convert.team;

import com.gs.model.dto.team.MessageDto;
import com.gs.model.entity.jpa.db1.team.Message;
import com.gs.repository.jpa.team.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 战队实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 **/
//@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public class MessageDtoConvert {
    @Autowired
    MemberRepository memberRepository;

    /**
     * Entity转Vo
     *实体
     * @param messageDto message dto
     * @return message
     */
    public Message toEntity(MessageDto messageDto) {
        if (messageDto == null) {
            return null;
        } else {
            Message message = new Message();
            message.setTeamId(messageDto.getTeamId());
            message.setFromId(messageDto.getFromId());
            message.setToId(messageDto.getToId());
            message.setType(messageDto.getType());
            message.setContent(messageDto.getContent());
            message.setCreateTime(new Date());
            return message;
        }
    }
}

