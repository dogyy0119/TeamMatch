package com.gs.convert;


import com.gs.model.dto.game.PUBGPlayerDTO;
import com.gs.model.entity.jpa.db1.game.PUBGPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PUBGPlayerConvert extends EntityDtoConvertBase<PUBGPlayerDTO, PUBGPlayer> {
}
