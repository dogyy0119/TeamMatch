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
@Table(name = "t_league_Match_part_team_order")
public class LMPTeamOrder implements Serializable {
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
     * 比赛时间
     */
    @Column(name="matchTime")
    private Date matchTime;

    
}
