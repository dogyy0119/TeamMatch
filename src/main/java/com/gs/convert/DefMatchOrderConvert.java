package com.gs.convert;

import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.repository.jpa.def.DefMatchManageRepository;
import com.gs.repository.jpa.def.DefMatchRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DefMatchOrderConvert  {
    @Autowired
    protected DefMatchManageRepository defMatchManageRepository;

    @Autowired
    protected DefMatchRepository defMatchRepository;

    @Mapping(target = "defMatchManage", expression = "java(defMatchManageRepository.findDefMatchManageByDefMatch(defMatchRepository.findById(dto.getMatchId()).get()))")
    public abstract DefMatchOrder toEntity (DefMatchOrderDTO dto);

    @Mapping(target = "matchId", expression = "java(entity.getDefMatchManage().getDefMatch().getId())")
    public abstract DefMatchOrderDTO toDto (DefMatchOrder entity);
}
