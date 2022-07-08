package com.gs.model.entity.jpa.db1.match.league;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 联盟战队实体类
 * User: lys
 * DateTime: 2022-05-1
 **/
@Entity
@Getter
@Setter
@Table(name = "t_league_Match_whole")
public class LMWhole implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 联盟Id
     */
    @Column(name = "leagueId")
    private Long leagueId;
    /**
     * 联盟比赛名称
     */
    @Column(name = "matchName", nullable = false)
    private String matchName;

    /**
     * 联赛开始时间
     */
    @Column(name="startDate")
    private Date startDate;

    /**
     * 联赛结束时间
     */
    @Column(name = "endDate")
    private Date endDate;

    /**
     * 比赛模式
     */
    @Column(name = "matchMode")
    private String matchMode;

    /**
     * 对局数量
     */
    @Column(name = "matchNum")
    private Integer matchNum;

    /**
     * 战队数量
     */
    @Column(name = "teamNum")
    private Integer teamNum;


    /**
     * 比赛报名费用
     */
    @Column(name = "matchCost")
    private Double matchCost;
}
