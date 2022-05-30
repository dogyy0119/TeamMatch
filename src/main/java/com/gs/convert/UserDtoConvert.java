package com.gs.convert;

import com.gs.model.dto.UserDTO;
import com.gs.model.entity.jpa.db1.User;

import org.mapstruct.Mapper;

import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDtoConvert extends EntityDtoConvertBase<UserDTO, User> {
    
}
