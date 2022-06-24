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
public class LeagueRequestDTO implements Serializable {
    /**
     * 该消息所属的team ID
     */
    @IsLeagueExist
    private Long leagueId;
    /**
     * 发送者
     */
    @IsMemberExist
    private Long fromMemberId;
    /**
     * 待加入联盟的战队Id
     */
    @IsTeamExist
    private Long fromTeamId;

    /**
     * 1、申请加入联盟
     */
    private Integer type;

    /**
     * 战队请求描述信息
     */
    private String content;
}
