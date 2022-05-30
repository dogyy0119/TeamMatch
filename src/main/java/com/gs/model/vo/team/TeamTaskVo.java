package com.gs.model.vo.team;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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
public class TeamTaskVo {

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

