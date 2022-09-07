package com.gs.model.entity.jpa.db1.game;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FPP {
       double KD;
       double RangDamage;
       double Win;
       double Top10;
       double LongestKill;
       double HeadShotKill;
       double RankPoints;
       double KDA;
       Integer MaxKillStreaks;
}
