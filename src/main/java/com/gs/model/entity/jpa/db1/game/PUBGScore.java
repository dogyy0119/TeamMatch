package com.gs.model.entity.jpa.db1.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Getter
@Setter
@ToString
@Table(name="t_pubg_score")
public class PUBGScore  implements  Serializable {

    /**
     * PUBG 玩家 id ， 从PUBG 官网获取
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * 击杀
     */
    @Column(name = "kills")
    private Integer score;

    /**
     * PUBGPlayer
     */
    @OneToOne(targetEntity = PUBGPlayer.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    private PUBGPlayer pubgPlayer;

    /**
     * PUBGMatch
     */
    @OneToOne(targetEntity = PUBGMatches.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "pubgMatchesId", nullable = false)
    private PUBGMatches pubgMatches;
}