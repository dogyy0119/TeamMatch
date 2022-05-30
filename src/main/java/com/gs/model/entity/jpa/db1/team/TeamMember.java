package com.gs.model.entity.jpa.db1.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * 战队成员实体类
 * User: lys
 * DateTime: 2022-05-1
 **/
@Entity
@Getter
@Setter
@Table(name = "t_gameteam_member")
public class TeamMember  implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 成员ID，一对于关联Member的主键id
     */
    @OneToOne(targetEntity = Member.class, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    /**
     * 多对一关系映射：多个战队成员对应一个战队
     */
    @JsonIgnore
    @ManyToOne(targetEntity=Team.class)
    @JoinColumn(name="teamId",referencedColumnName="id")
    private Team team;

    /**
     * 成员在战队中的职务：队长，副队长，组员
     */
    @Column(name = "job", nullable = false)
    private int job;

    /**
     *  禁言标志位
     */
    @Column(name = "silentFlg", nullable = false)
    private int silentFlg;

    /**
     * 成员加入战队的时间
     */
    @Column(name = "join_time", nullable = false)
    private Date joinTime;
}
