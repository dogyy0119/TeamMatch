package com.gs.convert;

import com.gs.model.dto.game.PUBGTeamDTO;
import com.gs.model.entity.jpa.db1.game.PUBGTeam;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PUBGTeamConvert extends EntityDtoConvertBase<PUBGTeamDTO, PUBGTeam> {
}
