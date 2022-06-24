package com.gs.model.entity.jpa.db1.team;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author lys
 * @date 2022-05-18
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "t_team_request")
public class TeamRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 该消息所属的team ID
     */
    @Column(name = "teamId")
    private Long teamId;

    /**
     * 发送者
     */
    @Column(name = "fromId")
    private Long fromId;

    /**
     * 1、申请加入战队
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 1、未处理；2、已接受；3、已拒绝
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 战队请求描述信息
     */
    private String content;

    /**
     * 消息发送时间
     */
    @CreatedDate
    @Column(name = "createTime")
    private Date createTime;

}