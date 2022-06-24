package com.gs.model.dto.league;

import com.gs.annotation.IsLeagueExist;
import com.gs.annotation.IsMemberExist;
import com.gs.annotation.IsTeamExist;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 联盟team dto
 * User: lys
 * DateTime: 2022-04-22
 **/

@Data
@ToString
public class LeagueTeamDTO implements Serializable {
    /**
     * 联盟 ID
     */
    @IsLeagueExist
    private Long leagueId;

    /**
     * teamId
     */
    @IsTeamExist
    private Long teamId;

}