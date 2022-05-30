package com.gs.model.entity.jpa.db1.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
@Getter
@Setter
public class PUBGPlayerBase implements Serializable {

    /**
     * PUBG 玩家 id ， 从PUBG 官网获取
     */
    @Column(name = "pubgPlayerName")
    private String pubgPlayerName;

    @Column(name = "pubgPlayerId")
    private String pubgPlayerId;

    /**
     * PUBG 玩家 id ， 从PUBG 官网获取
     */
    @JsonIgnore
    @ManyToOne(targetEntity=PUBGTeam.class)
    @JoinColumn(name="pubgTeamId",referencedColumnName="pubgTeamId")
    private PUBGTeam pubgTeam;

//    /**
//     * member id
//     */
//    @Column(name = "memberId")
//    private Long memberId;

}
