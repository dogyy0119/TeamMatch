package com.gs.model.dto.game;


import com.gs.model.entity.jpa.db1.game.PUBGTeam;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class PUBGMatchesDTO {

    /**
     * 比赛 ID
     */
//    private Long id;


    /**
     * pubg 比赛ID
     */
    private String pubgMatchesId;

    /**
     *  定义比赛 id
     */
    private Long defMatchId;

    /**
     *  定义比赛 index
     */
    private Integer defMatchIndex;

    /**
     *  比赛类型
     */

    private String type;

    /**
     * 战队成员列表
     */
    private List<PUBGTeamDTO> teamMembers = new ArrayList<>();
}
