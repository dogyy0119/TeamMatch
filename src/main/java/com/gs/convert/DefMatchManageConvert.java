package com.gs.convert;

import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.repository.jpa.def.DefMatchRepository;
import com.gs.repository.jpa.team.MemberRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DefMatchManageConvert  {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected DefMatchRepository defMatchRepository;

    @Mapping(target = "member",
            expression = "java(memberRepository.findById(dto.getMemberId()).get())")
    @Mapping(target = "defMatch",
            expression = "java(defMatchRepository.findById(dto.getMatchId()).get())")
    public abstract DefMatchManage toEntity (DefMatchManageDTO dto);

    @Mapping(target = "memberId", expression = "java(entity.getMember().getId())")
    @Mapping(target = "matchId", expression = "java(entity.getDefMatch().getId())")
    public abstract DefMatchManageDTO toDto (DefMatchManage entity);

}
