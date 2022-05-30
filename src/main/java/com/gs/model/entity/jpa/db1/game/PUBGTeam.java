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
public class PUBGTeam implements Serializable {

    /**
     * PUBG 玩家 id ， 从PUBG 官网获取
     */
    @Id
    @Column(name = "pubgTeamId")
    private String pubgTeamId;

    @Column(name = "pubgTeamIndex")
    private Integer index;

    /**
     * member id
     */
    @JsonIgnore
    @ManyToOne(targetEntity=PUBGMatches.class)
    @JoinColumn(name="pubgMatchesId",referencedColumnName="pubgMatchesId")
    private PUBGMatches pubgMatchesId;

    /**
     * 战队成员列表
     */
    @OneToMany(targetEntity = PUBGPlayer.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pubgTeamId", referencedColumnName = "pubgTeamId")
    private List<PUBGPlayer> teamMembers = new ArrayList<>();

    @Override
    public int hashCode() {
        return pubgTeamId != null ? pubgTeamId.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        PUBGTeam other = (PUBGTeam) obj;

        if (this.pubgTeamId != other.pubgTeamId) {

            return false;
        }
        return true;
    }


}