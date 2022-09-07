package com.gs.model.entity.jpa.db1.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@ToString
@Entity
@Table(name="t_pubgdata")
public class PUBGMatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pubgId")
    private String pubgId;

    @Column(name = "data")
    private String data;

    @Column(name = "matchId")
    private String matchId;

    @Column(name = "createAt")
    private Date createAt;
}