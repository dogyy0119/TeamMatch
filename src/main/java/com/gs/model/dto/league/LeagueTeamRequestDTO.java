package com.gs.model.dto.league;

import com.gs.annotation.IsLeagueExist;
import com.gs.annotation.IsMemberExist;
import com.gs.annotation.IsTeamExist;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString
public class LeagueTeamRequestDTO implements Serializable {
    /**
     * 该消息所属的League ID
     */
    @IsLeagueExist
    private Long leagueId;

    /**
     * 发送者
     */
    @IsMemberExist
    private Long fromId;

    /**
     * 接收者
     */
    @IsTeamExist
    private Long toTeamId;

    /**
     * 1、邀请战队加入联盟；
     */
    private Integer type;

    /**
     * 成员请求描述信息
     */
    private String content;
}
