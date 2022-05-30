package com.gs.model.dto.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MessageDto implements Serializable {
    /**
     * teamId
     */
    private Long teamId;

    /**
     * 发送者ID
     */
    private Long fromId;

    /**
     * 接收者ID
     */
    private Long toId;

    /**
     * 消息类型：1、邀请人加入战队；2、申请加入战队；98、组内群聊；99、单对单聊天
     */
    private Integer type;

    /**
     * content
     */
    private String content;
}
