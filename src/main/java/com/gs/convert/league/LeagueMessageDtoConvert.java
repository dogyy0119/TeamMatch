package com.gs.convert.league;

import com.gs.model.dto.league.LeagueMessageDto;
import com.gs.model.dto.team.MessageDto;
import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.entity.jpa.db1.league.LeagueMessage;
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
public class LeagueMessageDtoConvert {

    /**
     * Entity转Vo
     *实体
     * @param leagueMessageDto message dto
     * @return message
     */
    public LeagueMessage toEntity(LeagueMessageDto leagueMessageDto) {
        if (leagueMessageDto == null) {
            return null;
        } else {
            LeagueMessage message = new LeagueMessage();
            message.setLeagueId(leagueMessageDto.getLeagueId());
            message.setFromId(leagueMessageDto.getFromId());
            message.setToId(leagueMessageDto.getToId());
            message.setType(leagueMessageDto.getType());
            message.setContent(leagueMessageDto.getContent());
            message.setCreateTime(new Date());
            return message;
        }
    }
}

