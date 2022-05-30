package com.gs.model.vo.team;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
 * @author ZhangFz
 * @since 2020-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Team请求对象", description = "系统全局-系统消息表")
public class TeamRequestVo {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 该消息所属的team ID
     */
    private Long teamId;
    /**
     * 战队名称
     */
    private String teamName;
    /**
     * 发送者
     */
    private Long fromId;

    /**
     * 发送者Name
     */
    private String fromName;

    /**
     * 发送者头像
     */
    private String fromAvatar;


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

