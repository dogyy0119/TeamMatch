package com.gs.model.dto.game;

import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class PUBGTeamDTO {

    /**
     *
     */
    private String pubgTeamId;

    /**
     * 队伍排名
     */
    private String teamName;

    /**
     * 队伍排名
     */
    private Integer index;

    /**
     *
     */
    private String pubgMatchesId;

    /**
     *
     */
    private List<PUBGPlayerDTO> teamMembers = new ArrayList<>();
}
