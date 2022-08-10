package com.gs.model.entity.jpa.db1.game;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name="t_pubg_achievement")
public class PUBGAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "defMatchId")
    private Long defMatchId;

    @Column(name = "teamId")
    private Long teamId;

    @Column(name = "name")
    private String name;

    @Column(name = "gameIndex")
    private Integer gameIndex;

    /**
     * 比赛 Logo
     */
    @Column(name = "game_logo")
    private String gameLogo;

    /**
     * 比赛类型 TPP FPP
     */
    @Column(name = "match_type")
    private String matchType;
}
