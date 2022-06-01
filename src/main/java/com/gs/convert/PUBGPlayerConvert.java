package com.gs.convert;


import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.dto.game.PUBGPlayerDTO;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.game.PUBGPlayer;
import com.gs.repository.jpa.game.PUBGTeamRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PUBGPlayerConvert {

    @Autowired
    protected PUBGTeamRepository pubgTeamRepository;

    @Mapping(target = "pubgTeam", expression = "java( pubgTeamRepository.findById(dto.getPubgTeamId()).get() )")
    public abstract PUBGPlayer toEntity (PUBGPlayerDTO dto);

    @Mapping(target = "pubgTeamId", expression = "java(entity.getPubgTeam().getPubgTeamId())")
    public abstract PUBGPlayerDTO toDto (PUBGPlayer entity);

    public abstract List<PUBGPlayerDTO> toDto(List<PUBGPlayer> entitys);

    public abstract List<PUBGPlayer> toEntity(List<PUBGPlayerDTO> dtos);
}
