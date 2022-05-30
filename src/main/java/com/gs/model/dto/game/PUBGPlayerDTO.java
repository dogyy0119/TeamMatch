package com.gs.model.dto.game;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PUBGPlayerDTO {

    private Long Id;

    private String kills;

    private String pubgPlayerName;

    private String pubgPlayerId;

    private String pubgTeamId;
}
