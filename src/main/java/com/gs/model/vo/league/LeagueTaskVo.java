package com.gs.model.vo.league;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 系统全局-系统消息表
 * </p>
 *
 * @author lys
 * @since 2022-05-23
 */
@Data
@ToString
@ApiModel(value = "Message对象", description = "系统全局-系统消息表")
public class LeagueTaskVo {

    private Long id;

    /**
     * 该队务所属的team id
     */
    private Long teamId;

    /**
     * 队务对应的内容
     */
    private String teamTaskContent;

    /**
     * 队务产生的时间
     */
    private String createTime;
}

