package com.gs.model.dto.vo;

import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import lombok.*;

@Data
@ToString
public class DefMatchOrderTeamVO extends DefMatchOrderDTO {

    Long   id;

    String teamId;

    String teamName;

    public DefMatchOrderTeamVO(DefMatchOrderDTO defMatchOrderDTO) {
        this.setOrderId(defMatchOrderDTO.getOrderId());
        this.setMatchId(defMatchOrderDTO.getMatchId());
        this.setMode(defMatchOrderDTO.getMode());
        this.setStatus(defMatchOrderDTO.getStatus());
    };

}
