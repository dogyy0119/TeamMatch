package com.gs.model.dto.game;

import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class PUBGTeamDTO {

    private String pubgTeamId;

    private PUBGMatches pubgMatchesId;
//    private String pubgMatchesId;


    private List<PUBGPlayerDTO> teamMembers = new ArrayList<>();
}
