package com.gs.model.dto.team;

import com.gs.annotation.IsTeamExist;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 创建战队接口输入dto
 * User: lys
 * DateTime: 2022-04-22
 **/

@Data
@ToString
public class TeamUpdateInfoDTO implements Serializable {
    /**
     * 战队ID
     */
    @IsTeamExist
    private Long teamId;
    /**
     * 战队名称
     */
    private String name;

    /**
     * 战队的描述信息
     */
    private String descriptionInfo;

    /**
     * 战队LOGO
     */
    private String logoUrl;

}