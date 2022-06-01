package com.gs.model.dto.game;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;

@Data
@ToString
public class PUBGPlayerDTO {

    /**
     * 主键Id
     */
    private Long Id;

    /**
     * 击杀
     */
    private String kills;

    /**
     *  伤害
     */
    private String damageDealt;

    /**
     *  助攻
     */
    private String assists;

    /**
     *  生存时间 （s）
     */
    private String timeSurvived;

    /**
     * pubg 玩家名字
     */
    private String pubgPlayerName;

    /**
     * pubg 玩家 Id
     */
    private String pubgPlayerId;

    /**
     * pubg TeamId
     */
    private String pubgTeamId;
}
