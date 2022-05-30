package com.gs.model.vo.team;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import java.sql.Timestamp;
import java.util.Date;

/**
 * 消息Vo
 *
 * @author lys
 * @since 2022-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Message对象", description = "系统全局-系统消息表")
public class MessageVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "该消息所属的team ID")
    private Long teamId;

    @ApiModelProperty(value = "该消息所属的team Name")
    private String teamName;

    @ApiModelProperty(value = "发送者ID")
    private Long fromId;

    @ApiModelProperty(value = "发送者Name")
    private String fromName;

    @ApiModelProperty(value = "接收者Id")
    private Long toId;

    @ApiModelProperty(value = "接收者Name")
    private String toName;

    @ApiModelProperty(value = "消息类型：1、用户上线消息；2、用户下线消息；98，单对单聊天；99、群聊")
    private Integer type;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息发送时间")
    private String createTime;
}

