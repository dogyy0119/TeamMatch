package com.gs.model.entity.jpa.db1.def;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gs.model.entity.jpa.db1.other.AbstractBaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@EnableJpaAuditing
@Table(name="t_def_match_order")
public class DefMatchOrder extends AbstractBaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * 对应定义比赛表 id
     */
    @JsonIgnore
    @ManyToOne(targetEntity=DefMatchManage.class)
    @JoinColumn(name="def_matchmanage_id",referencedColumnName="id")
    private DefMatchManage defMatchManage;

    /**
     * 报名方式 个人 0  战队 1
     */
    @Column(name = "mode")
    private Integer mode;

    /**
     * 报名ID 战队id 或者 个人id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 报名状态 （申请， 拒绝， 通过）
     */
    @Column(name = "status")
    private Integer status;

}
