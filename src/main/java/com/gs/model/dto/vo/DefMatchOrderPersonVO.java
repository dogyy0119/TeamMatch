package com.gs.model.dto.vo;

import com.gs.model.dto.def.DefMatchOrderDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DefMatchOrderPersonVO extends DefMatchOrderDTO {

    Long id;

    Long personId;

    String personName;

    public DefMatchOrderPersonVO(DefMatchOrderDTO defMatchOrderDTO) {
        this.setOrderId(defMatchOrderDTO.getOrderId());
        this.setMatchId(defMatchOrderDTO.getMatchId());
        this.setMode(defMatchOrderDTO.getMode());
        this.setStatus(defMatchOrderDTO.getStatus());
    };

}