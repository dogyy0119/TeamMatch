package com.gs.model.entity.jpa.db1.team;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 战队实体类
 * User: lys
 * DateTime: 2022-05-1
 **/

@Entity
@Getter
@Setter
@Table(name = "t_gameteam")
public class Team implements Serializable {


    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 战队名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 创建者的用户ID
     */
    @Column(name = "createMemberId", nullable = false)
    private Long createMemberId;

    /**
     * 战队的最大成员数量
     */
    @Column(name = "maxMemberNum", nullable = false)
    private Integer maxMemberNum;

    /**
     * 战队的创建时间
     */
    @Column(name = "createTime", nullable = false)
    private Date createTime;

    /**
     * 战队logo
     */
    @Column(name = "logoUrl")
    private String logoUrl;


    /**
     * 战队的描述信息
     */
    @Column(name = "descriptionInfo")
    private String descriptionInfo;

    /**
     * 战队成员列表
     */
    @OneToMany(targetEntity = TeamMember.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "teamId", referencedColumnName = "id")
    private List<TeamMember> teamMembers = new ArrayList<>();

    /**
     * 战队的最好成绩
     */
    @Column(name = "bestScore")
    private Integer bestScore = 0;

    /**
     * 战队的最高排名
     */
    @Column(name = "highestOrder")
    private Integer highestOrder = 0;

    /**
     * 战队总积分
     */
    @Column(name = "totalScore")
    private Integer totalScore = 0;
}