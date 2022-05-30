package com.gs.model.dto.def;

import com.gs.annotation.IsValidValue;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
public class DefMatchOrderDTO {

    private Long id;

    /**
     * 对应定义比赛表 id
     */
    private Long matchId;

    /**
     * 报名方式  0个人  1战队
     */
    private Integer mode;

    /**
     * 报名ID
     */
    @IsValidValue
    private Long orderId;

    /**
     * 报名状态 （申请， 拒绝， 通过）
     */
    private Integer status;

}
