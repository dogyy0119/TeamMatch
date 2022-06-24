package com.gs.model.vo.league;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;

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
public class LeagueRequestVo {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 该消息所属的league ID
     */
    private Long leagueId;
    /**
     * 联盟名称
     */
    private String leagueName;

    /**
     * 发送者
     */
    private Long fromMemberId;

    /**
     * 待加入联盟的战队Id
     */
    private Long fromTeamId;

    /**
     * 发送者Name
     */
    private String fromTeamName;

    /**
     * 发送者头像
     */
    private String fromTeamAvatar;


    /**
     * 1、申请加入战队
     */
    private Integer type;

    /**
     * 1、未处理；2、已接受；3、已拒绝
     */
    private Integer status;

    /**
     * 战队请求描述信息
     */
    private String content;

    /**
     * 消息发送时间
     */
    private String createTime;
}

