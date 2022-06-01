package com.gs.convert;

import com.gs.model.dto.game.PUBGTeamDTO;
import com.gs.model.entity.jpa.db1.game.PUBGTeam;
import com.gs.repository.jpa.game.PUBGMatchesRepository;
import com.gs.repository.jpa.game.PUBGTeamRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;


@Mapper(componentModel = "spring")
public abstract class PUBGTeamConvert {

    @Autowired
    protected PUBGTeamRepository pubgTeamRepository;

    @Autowired
    protected PUBGMatchesRepository pubgMatchesRepository;

    @Autowired
    protected PUBGPlayerConvert pubgPlayerConvert;

    @Mapping(target = "pubgMatchesId", expression = "java( pubgMatchesRepository.findById(dto.getPubgMatchesId()).get() )")
    @Mapping(target = "teamMembers", expression = "java( pubgPlayerConvert.toEntity(dto.getTeamMembers()) )")
    public abstract PUBGTeam toEntity (PUBGTeamDTO dto);

    @Mapping(target = "pubgMatchesId", expression = "java( entity.getPubgMatchesId().getPubgMatchesId() )")
    @Mapping(target = "teamMembers", expression = "java( pubgPlayerConvert.toDto(entity.getTeamMembers()) )")
    public abstract PUBGTeamDTO toDto (PUBGTeam entity);

    public abstract List<PUBGTeam> toEntity(List<PUBGTeamDTO> dtos);


    public abstract List<PUBGTeamDTO> toDto(List<PUBGTeam> entitys);
}
