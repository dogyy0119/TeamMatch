package com.gs.model.entity.jpa.db1.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name="t_pubg_season")
public class PUBGSeason {

    /**
     * season id 从PUBG 官网获取
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pubg_season_id")
    private Long id;

    /**
     * season name
     */
    @Column(name = "pubg_season_name")
    private String name;
}
