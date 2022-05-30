package com.gs.convert;

import com.gs.model.dto.def.TeamOrderDTO;
import com.gs.model.entity.jpa.db1.def.TeamOrder;
import com.gs.repository.jpa.def.DefMatchOrderRepository;
import com.gs.repository.jpa.team.MemberRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TeamOrderConvert {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected DefMatchOrderRepository defMatchOrderRepository;

    @Mapping(target = "member",
            expression = "java(memberRepository.findById(dto.getMemberId()).get())")
    @Mapping(target = "defMatchOrder",
            expression = "java(defMatchOrderRepository.findById(dto.getDefMatchOrderId()).get())")
    public abstract TeamOrder toEntity (TeamOrderDTO dto);

    @Mapping(target = "memberId", expression = "java(entity.getMember().getId())")
    @Mapping(target = "defMatchOrderId", expression = "java(entity.getDefMatchOrder().getId())")
    public abstract TeamOrderDTO toDto (TeamOrder entity);
}
