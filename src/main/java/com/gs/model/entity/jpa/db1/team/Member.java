package com.gs.model.entity.jpa.db1.team;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 成员实体类
 * User: lys
 * DateTime: 2022-05-1
 **/
@Entity
@Getter
@Setter
@Table(name = "t_member")
public class Member  implements Serializable {
    /**
     *  id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "name", length = 200)
    private String name;

    /**
     * 密码
     */
    @Column(name = "pass", nullable = false, length = 200)
    private String pass;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 200)
    private String email;

    /**
     * 头像
     */
    @Column(name = "avatar", length = 500)
    private String avatar;

    /**
     * steamId
     */
    @Column(name = "steamId", length = 200)
    private String steamId;

    /**
     * token
     */
    @Column(name = "token", length = 500)
    private String token;

    /**
     * token有效时间
     */
    @Column(name = "validDate")
    private Date validDate;

    /**
     * 创建时间
     */
    @Column(name = "createTime", nullable = false)
    private Date createTime;

    /**
     * 手机号
     */
    @Column(name = "phone", nullable = false, length = 200)
    private String phone;

    /**
     * 玩家等级
     */
    @Column(name = "\"level\"")
    private Integer level;

    /**
     * 是否在线
     */
    @Column(name = "isOnline", nullable = false)
    private Integer isOnline;

    /**
     * 游戏id
     */
    @Column(name = "gameId")
    private Long gameId;

    /**
     * 房间id
     */
    @Column(name = "roomId")
    private Long roomId;

    /**
     * 队伍id
     */
    @Column(name = "teamId")
    private Long teamId;

    /**
     * 在线时间
     */
    @Column(name = "onlineTime")
    private Date onlineTime;

    /**
     * 服务器
     */
    @Column(name = "serve")
    private Integer serve;

    /**
     * 能否被邀请
     */
    @Column(name = "canInvite", nullable = false)
    private Integer canInvite;

    /**
     * 游戏类型
     */
    @Column(name = "gamelxx", nullable = false)
    private Integer gamelxx;

    /**
     * pubg用户名
     */
    @Column(name = "pubgName", length = 500)
    private String pubgName;

    /**
     * pubgId
     */
    @Column(name = "pubgId", length = 500)
    private String pubgId;

    /**
     * vip到期时间
     */
    @Column(name = "vipTime", nullable = false)
    private Date vipTime;

    /**
     * 被赞次数
     */
    @Column(name = "likes")
    private Integer likes;

    /**
     * 举报次数
     */
    @Column(name = "reports")
    private Integer reports;

    /**
     * 1组队；2房间
     */
    @Column(name = "gameType")
    private Integer gameType;

    /**
     * 是否启用
     */
    @Column(name = "isEnable", nullable = false)
    private Integer isEnable;

    /**
     * 能否建房
     */
    @Column(name = "canCreateRoom", nullable = false)
    private Integer canCreateRoom;
}