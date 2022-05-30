package com.gs.model.vo.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

/**
 * 战队接口输出Vo
 * User: lys
 * DateTime: 2022-04-22
 **/
@Data
@ToString
public class TeamVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 战队名称
     */
    private String name;

    /**
     * 创建者的用户ID
     */
    private Long createMemberId;

    /**
     * 战队的最大成员数量
     */
    private Integer maxMemberNum;

    /**
     * 战队的创建时间
     */
    private String createTime;

    /**
     * 战队logo
     */
    private String logoUrl;

    /**
     * 战队的描述信息
     */
    private String descriptionInfo;

    /**
     * 战队成员列表
     */
    private List<TeamMemberVo> teamMembers = new ArrayList<>();

    /**
     * 战队的最好成绩
     */
    private Integer bestScore;

    /**
     * 战队的最高排名
     */
    private Integer highestOrder;

    /**
     * 战队总积分
     */
    private Integer totalScore;
}
