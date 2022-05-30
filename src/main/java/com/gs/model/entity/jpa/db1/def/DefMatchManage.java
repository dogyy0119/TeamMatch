package com.gs.model.entity.jpa.db1.def;

import com.gs.model.entity.jpa.db1.other.AbstractBaseTimeEntity;
import com.gs.model.entity.jpa.db1.team.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Table(name="t_def_match_manage")
public class DefMatchManage extends AbstractBaseTimeEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 对应定义比赛表 id
     */
    @OneToOne(targetEntity = DefMatch.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "def_Match_id", nullable = false)
    private DefMatch defMatch;

    /**
     * 管理者 id
     */
    @OneToOne(targetEntity = Member.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    /**
     * 状态 进行中，已结束，
     */
    @Column(name = "state")
    private Integer state=1;

    /**
     * 报名方式 个人 战队
     */
    @Column(name = "mode")
    private Integer mode;

    /**
     * 报名人数上线
     */
    @Column(name = "all_order")
    private Integer allOrder;

    /**
     * 当前报名人
     */
    @Column(name = "cur_order")
    private Integer curOrder=0;

    /**
     * 对应定义比赛表 id
     */
//    @OneToMany(targetEntity= DefMatchOrder.class, cascade = CascadeType.ALL)
//    @JoinColumn (name = "order_list")
//    private List<DefMatchOrder> orderlist = new ArrayList<>();
}
