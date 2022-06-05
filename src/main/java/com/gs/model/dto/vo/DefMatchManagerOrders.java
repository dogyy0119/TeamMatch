package com.gs.model.dto.vo;

import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class DefMatchManagerOrders extends DefMatchManageDTO {
    List<DefMatchOrderDTO> defMatchOrderDTOS = new ArrayList<>();

    public DefMatchManagerOrders(DefMatchManageDTO defMatchManageDTO) {
        this.setId( defMatchManageDTO.getId() );
        this.setMatchId( defMatchManageDTO.getMatchId() );
        this.setAllOrder( defMatchManageDTO.getAllOrder() );
        this.setCurOrder( defMatchManageDTO.getCurOrder() );
        this.setMemberId( defMatchManageDTO.getMemberId() );
        this.setMode( defMatchManageDTO.getMode());
        this.setState(defMatchManageDTO.getState() );
    };
}
