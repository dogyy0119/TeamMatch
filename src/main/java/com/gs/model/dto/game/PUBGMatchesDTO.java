package com.gs.model.dto.game;


import com.gs.model.entity.jpa.db1.game.PUBGTeam;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class PUBGMatchesDTO {

    private Long id;

    private String pubgMatchesId;

    private Long defMatchId;

    private String type;

//    private String data;

    private List<PUBGTeam> teamMembers = new ArrayList<>();
}
