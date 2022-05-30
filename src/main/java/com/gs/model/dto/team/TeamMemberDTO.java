package com.gs.model.dto.team;

import com.gs.annotation.IsMemberExist;
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
public class TeamMemberDTO implements Serializable {
    /**
     * 成员ID
     */
    @IsMemberExist
    private Long memberId;

    /**
     * 战队Id
     */

    @IsTeamExist
    private Long teamId;

    /**
     * 成员在战队中的职务：队长，副队长，组员
     */

    private Integer job;

    /**
     *
     * 禁言标志位
     */
    private Integer silentFlg;
}