package com.gs.convert;

import com.gs.model.dto.game.PUBGMatchesDTO;
import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PUBGMatchesConvert extends EntityDtoConvertBase<PUBGMatchesDTO, PUBGMatches> {

}
