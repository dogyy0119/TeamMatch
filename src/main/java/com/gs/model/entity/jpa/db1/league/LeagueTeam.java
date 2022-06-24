package com.gs.model.entity.jpa.db1.league;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gs.model.entity.jpa.db1.team.Team;
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
@Table(name = "t_league_team")
public class LeagueTeam implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 战队ID
     */
    @Column(name = "teamId", nullable = false)
    private Long teamId;

    /**
     * 联盟Id
     */
    @JsonIgnore
    @ManyToOne(targetEntity= League.class)
    @JoinColumn(name="leagueId",referencedColumnName="id")
    private League league;

    /**
     * 战队加入联盟的时间
     */
    @Column(name = "joinTime", nullable = false)
    private Date joinTime;
}
