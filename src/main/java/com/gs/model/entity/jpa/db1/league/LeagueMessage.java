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
@Table(name = "t_league_message")
public class LeagueMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 该消息所属的team ID
     */
    @Column(name = "leagueId")
    private Long leagueId;

    /**
     * 发送者
     */
    @Column(name = "fromId")
    private Long fromId;


    /**
     * 接收者
     */
    @Column(name = "toId")
    private Long toId;

    /**
     * 消息类型：3，单对单聊天；4、群聊
     */
    @Column(name = "type")
    private Integer type;

    /**
     * message
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