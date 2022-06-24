package com.gs.model.entity.jpa.db1.league;

import com.gs.annotation.IsMemberExist;
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
@Table(name = "t_league_request")
public class LeagueRequest {

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
    @Column(name = "fromMemberId")
    private Long fromMemberId;
    /**
     * 待加入联盟的战队Id
     */
    @Column(name = "fromTeamId")
    private Long fromTeamId;


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
    @Column(name = "content")
    private String content;

    /**
     * 消息发送时间
     */
    @CreatedDate
    @Column(name = "createTime")
    private Date createTime;

}