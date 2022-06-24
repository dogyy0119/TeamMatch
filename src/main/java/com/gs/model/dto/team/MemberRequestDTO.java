package com.gs.model.dto.team;

import com.gs.annotation.IsMemberExist;
import com.gs.annotation.IsTeamExist;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString
public class MemberRequestDTO implements Serializable {
    /**
     * 该消息所属的team ID
     */
    @IsTeamExist
    private Long teamId;

    /**
     * 发送者
     */
    @IsMemberExist
    private Long fromId;

    /**
     * 接收者
     */
    @IsMemberExist
    private Long toId;

    /**
     * 1、邀请人加入战队；
     */
    private Integer type;

    /**
     * 成员请求描述信息
     */
    private String content;
}
