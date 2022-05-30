package com.gs.convert;

import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.repository.jpa.team.MemberRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DefMatchConvert {

    @Autowired
    protected MemberRepository memberRepository;

    @Mapping(target = "member", expression = "java(memberRepository.findMemberById(dto.getMemberId()))")
    public abstract DefMatch toEntity (DefMatchDTO dto);

    @Mapping(target = "memberId", expression = "java(entity.getMember().getId())")
    public abstract DefMatchDTO toDto (DefMatch entity);
}
