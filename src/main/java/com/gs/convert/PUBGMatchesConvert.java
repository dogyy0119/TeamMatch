package com.gs.convert;

import com.gs.model.dto.game.PUBGMatchesDTO;
import com.gs.model.dto.game.PUBGTeamDTO;
import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import com.gs.model.entity.jpa.db1.game.PUBGTeam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PUBGMatchesConvert  {

    @Autowired
    protected PUBGTeamConvert pubgTeamConvert;

    @Mapping(target = "teamMembers", expression = "java( pubgTeamConvert.toEntity( dto.getTeamMembers()) )")
    public abstract PUBGMatches toEntity (PUBGMatchesDTO dto);

    @Mapping(target = "teamMembers", expression = "java( pubgTeamConvert.toDto( entity.getTeamMembers()) )")
    public abstract PUBGMatchesDTO toDto(PUBGMatches entity);
}
