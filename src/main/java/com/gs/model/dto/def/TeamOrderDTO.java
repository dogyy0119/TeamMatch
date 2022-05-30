package com.gs.model.dto.def;

import com.gs.annotation.IsValidValue;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TeamOrderDTO {

    /**
     * 对应定义比赛表 id
     */
    private Long id;

    /**
     * 报名ID
     */
    @IsValidValue
    private Long memberId;

    /**
     * defMatchOrder
     */
    @IsValidValue
    private Long defMatchOrderId;

    /**
     * 报名状态 （申请， 拒绝， 通过）
     */
    private Integer status;

    /**
     * 点赞
     */
    private Integer isLike;
}
