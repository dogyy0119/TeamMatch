package com.gs.model.dto.def;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class DefMatchOrderQuery {

    /**
     * 对应定义比赛表 id
     */
    @NotBlank(message = "比赛id 不能为空")
    private Long matchId;

    /**
     *  查询 memberId
     */
    private Long memberId;

    /**
     * 查询战队 teamId
     */
    private Long teamId;

    /**
     * 报名状态 （申请 0， 拒绝 -1， 通过 1）
     */
    @NotBlank(message = "报名状态不能为空")
    private Integer status;
}
