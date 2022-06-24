package com.gs.model.entity.jpa.db1.league;

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
@Table(name = "t_league_team_request")
public class LeagueTeamRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 该消息所属的联盟ID
     */
    @Column(name = "leagueId")
    private Long leagueId;

    /**
     * 发送者
     */
    @Column(name = "fromId")
    private Long fromId;


    /**
     * 接收teamId
     */
    @Column(name = "toTeamId")
    private Long toTeamId;

    /**
     * 1、邀请加入战队；
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 1、未处理；2、已接受；3、已拒绝
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 成员请求描述信息
     */
    @Column(name = "content")
    private String content;

    /**
     * 消息发送时间
     */
    @CreatedDate
    @Column(name = "createTime")
    private Date createTime;

}