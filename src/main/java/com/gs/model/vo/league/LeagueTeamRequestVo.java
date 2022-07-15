package com.gs.model.vo.league;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统全局-系统消息表
 * </p>
 *
 * @author ZhangFz
 * @since 2020-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Team请求对象", description = "系统全局-系统消息表")
public class LeagueTeamRequestVo {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 该消息所属的team ID
     */
    private Long leagueId;
    /**
     * 战队名称
     */
    private String leagueName;
    /**
     * 发送者
     */
    private Long fromId;

    /**
     * 发送者Name
     */
    private String fromName;

    private String fromAvatar;
    /**
     * 接收者
     */
    private Long toTeamId;



    /**
     * 接收者Name
     */
    private String toTeamName;

    private String toTeamAvatar;
    /**
     * 1、邀请人加入战队；2、申请加入战队
     */
    private Integer type;

    /**
     * 1、未处理；2、已接受；3、已拒绝
     */
    private Integer status;

    /**
     * 成员请求描述信息
     */
    private String content;

    /**
     * 消息发送时间
     */
    private String createTime;
}

