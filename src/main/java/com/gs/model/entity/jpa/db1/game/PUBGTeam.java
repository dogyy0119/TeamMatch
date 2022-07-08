package com.gs.model.entity.jpa.db1.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "t_pubg_team")
public class PUBGTeam implements Serializable , Comparable<PUBGTeam>{

    /**
     * PUBG 玩家 id ， 从PUBG 官网获取
     */
    @Id
    @Column(name = "pubgTeamId")
    private String pubgTeamId;

    /**
     * 队伍排名
     */
    @Column(name = "pubgTeamName")
    private String teamName;

    /**
     * 队伍排名
     */
    @Column(name = "pubgTeamIndex")
    private Integer index;

    /**
     * 队伍得分
     */
    @Column(name = "teamScore")
    private Integer teamScore = 0;

    /**
     * Pubg 比赛 Id
     */
    @JsonIgnore
    @ManyToOne(targetEntity=PUBGMatches.class)
    @JoinColumn(name="pubgMatchesId",referencedColumnName="pubgMatchesId")
    private PUBGMatches pubgMatchesId;

    /**
     * 战队成员列表
     */
    @OneToMany(targetEntity = PUBGPlayer.class, cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "pubgTeamId", referencedColumnName = "pubgTeamId")
    private List<PUBGPlayer> teamMembers = new ArrayList<>();


    @Override
    public int compareTo(PUBGTeam o) {
        return this.getIndex().compareTo( o.getIndex());
    }
}