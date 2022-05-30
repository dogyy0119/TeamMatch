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
@Table(name="t_pubg_player")
public class PUBGPlayer extends PUBGPlayerBase implements Serializable {

    /**
     * PUBG 玩家 id ， 从PUBG 官网获取
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

//    /**
//     * member id
//     */
//    @OneToOne
//    @Column(name = "pubg_player_base")
//    private PUBGPlayerBase pubgPlayerBase;

    @Column(name = "kills")
    private String kills;

}