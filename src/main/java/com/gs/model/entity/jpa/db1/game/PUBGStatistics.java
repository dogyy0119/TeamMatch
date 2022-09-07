package com.gs.model.entity.jpa.db1.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mapstruct.ap.internal.util.NativeTypes;

import javax.persistence.*;
import java.lang.annotation.Native;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name="t_membertogamedata")
public class PUBGStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "memberId")
    private Long memberId;

    @Column(name = "GameData")
    private String GameData;

    @Column(name = "SeasonStats")
    private String SeasonStats;

    @Column(name = "lifetimeStats")
    private String lifetimeStats;

    @Column(name = "RankedSeasonStats")
    private String RankedSeasonStats;

    @Column(name = "datetime")
    private Date datetime;
}