package com.gs.model.dto.def;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class DefMatchManageDTO {

    private Long id;

    /**
     * 对应定义比赛表 id
     */
    @NotBlank(message = "比赛ID不能为空")
    private Long matchId;

    /**
     * 管理者 id
     */
    @NotBlank(message = "管理者不能为空")
    private Long memberId;

    /**
     * 状态 未开始，进行中，已结束，
     */
    private int state;

    /**
     * 报名方式 个人 战队
     */
    private Integer mode;

    /**
     * 报名上线
     */
    private Integer allOrder;

    /**
     * 当前报名人
     */
    private Integer curOrder;

    /**
     * 对应定义比赛表 id
     */
//    private List<DefMatchOrder> orderlist = new ArrayList<>();
}
