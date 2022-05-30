package com.gs.model.entity.jpa.db1.def;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gs.model.entity.jpa.db1.team.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EnableJpaAuditing
@Table(name="t_def_team_order")
public class TeamOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * 对应MatchOrder id,此时 MatchOrder  为team 报名方式。
     */
    @JsonIgnore
    @ManyToOne(targetEntity=DefMatchOrder.class)
    @JoinColumn(name="def_MatchOrder_id",referencedColumnName="id")
    private DefMatchOrder defMatchOrder;

    /**
     * 对应定义比赛表 id
     */
    @OneToOne(targetEntity = Member.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;


    /**
     * 报名状态 （申请， 拒绝， 通过）
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 点赞（ -1 0 1）
     */
//    @Column(name = "like", columnDefinition = "int default 0")
    @Column(name = "isLike")
    private Integer isLike;
}
