package com.gs.model.vo.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 战队成员输出Vo
 * User: lys
 * DateTime: 2022-05-1
 **/
@Data
@ToString
public class TeamMemberVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 成员ID，一对于关联Member的主键id
     */
    private MemberVo member;

    /**
     * 成员在战队中的职务：队长，副队长，组员
     */
    private int job;

    /**
     * 禁言标志位
     */
    private int silentFlg;

    /**
     * 成员加入战队的时间
     */
    private String joinTime;
}
