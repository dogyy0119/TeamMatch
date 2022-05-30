package com.gs.model.entity.jpa.db1.game;


import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.other.AbstractBaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name="t_pubg_matches")
public class PUBGMatches extends AbstractBaseTimeEntity implements  Serializable {

    /**
     * 比赛 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "pubgMatchesId")
    private String pubgMatchesId;

    @Column(name = "defMatchId")
    private Long defMatchId;

//    /**
//     * 赛季
//     */
//    @OneToOne(targetEntity = PUBGSeason.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
//    @JoinColumn(name = "pubg_season_id", nullable = false)
//    private PUBGSeason season;

    /**
     *  比赛类型
      */
    @Column(name = "pubgMatchType")
    private String type;

    /**
     * PUBG 比赛原始数据
     */
    @Column(name = "pubgData", length = 10000)
    private String data;

    /**
     * 战队成员列表
     */
    @OneToMany(targetEntity = PUBGTeam.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pubgMatchesId", referencedColumnName = "pubgMatchesId")
    private List<PUBGTeam> teamMembers = new ArrayList<>();
}
