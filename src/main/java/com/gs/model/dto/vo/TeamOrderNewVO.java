package com.gs.model.dto.vo;

import com.gs.model.dto.def.TeamOrderDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TeamOrderNewVO extends TeamOrderDTO {

    String avatar;

    String name;

    public TeamOrderNewVO(TeamOrderDTO teamOrderDTO) {
        this.setId( teamOrderDTO.getId() );
        this.setDefMatchOrderId( teamOrderDTO.getDefMatchOrderId() );
        this.setStatus( teamOrderDTO.getStatus() );
        this.setIsLike( teamOrderDTO.getIsLike() );
        this.setMemberId(teamOrderDTO.getMemberId());
    };
}
