package com.gs.model.entity.jpa.db1.league;

import com.gs.model.entity.jpa.db1.match.league.LMWhole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 联盟实体类
 * User: lys
 * DateTime: 2022-05-1
 **/

@Entity
@Getter
@Setter
@Table(name = "t_league")
public class League {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 联盟名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 创建者的用户ID
     */
    @Column(name = "createMemberId", nullable = false)
    private Long createMemberId;

    /**
     * 联盟的最大战队数量
     */
    @Column(name = "maxTeamNum", nullable = false)
    private Integer maxTeamNum;

    /**
     * 联盟的创建时间
     */
    @Column(name = "createTime", nullable = false)
    private Date createTime;

    /**
     * ；联盟logo
     */
    @Column(name = "logoUrl")
    private String logoUrl;


    /**
     * 联盟的描述信息
     */
    @Column(name = "descriptionInfo")
    private String descriptionInfo;

    /**
     * 联盟比赛ID，一对于关联联盟比赛的主键id
     */
    @OneToOne(targetEntity = LMWhole.class, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "leagueMatchId")
    private LMWhole leagueMatch;

    /**
     * 联盟战队列表
     */
    @OneToMany(targetEntity = LeagueTeam.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "leagueId", referencedColumnName = "id")
    private List<LeagueTeam> leagueTeams = new ArrayList<>();

}
