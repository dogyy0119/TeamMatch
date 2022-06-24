package com.gs.model.dto.team;

import com.gs.annotation.IsMemberExist;
import com.gs.annotation.IsTeamExist;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class TeamRequestDTO implements Serializable {
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
     * 1、申请加入战队
     */
    private Integer type;

    /**
     * 战队请求描述信息
     */
    private String content;
}
