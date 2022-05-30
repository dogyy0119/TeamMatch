package com.gs.model.entity.jpa.db1.team;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * 队务实体类
 * User: lys
 * DateTime: 2022-05-1
 **/
@Entity
@Getter
@Setter
@ToString
@Table(name = "t_team_task")
public class TeamTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 该队务所属的team id
     */
    @Column(name = "teamId")
    private Long teamId;

    /**
     * 队务对应的内容
     */
    @Column(name = "teamTaskContent")
    private String teamTaskContent;

    /**
     * 队务产生的时间
     */
    @CreatedDate
    @Column(name = "createTime")
    private Date createTime;

}