package com.gs.model.dto.league;

import com.gs.annotation.IsLeagueExist;
import com.gs.annotation.IsTeamExist;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 修改联盟接口输入dto
 * User: lys
 * DateTime: 2022-04-22
 **/

@Data
@ToString
public class LeagueUpdateInfoDTO implements Serializable {
    /**
     * 联盟ID
     */
    @IsLeagueExist
    private Long leagueId;
    /**
     * 联盟名称
     */
    private String name;

    /**
     * 联盟的描述信息
     */
    private String descriptionInfo;

    /**
     * 联盟LOGO
     */
    private String logoUrl;

}