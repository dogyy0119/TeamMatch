package com.gs.model.entity.jpa.db1.def;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gs.model.entity.jpa.db1.other.AbstractBaseTimeEntity;
import com.gs.model.entity.jpa.db1.team.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name="t_def_match")
public class DefMatch extends AbstractBaseTimeEntity implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id")
        private Long id;

        /**
         * 创建者 成员ID，一对于关联Member的主键id
         */
        @OneToOne(targetEntity = Member.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
        @JoinColumn(name = "member_id", nullable = false)
        private Member member;

        /**
         * 比赛名称
         */
        @Column(name = "match_name")
        private String name;

        /**
         * 比赛类型 TPP FPP
         */
        @Column(name = "match_type")
        private String matchType;

        /**
         * 服务器类型 亚洲 欧洲 美洲等
         */
        @Column(name = "server_type")
        private String serverType;

        /**
         * 比赛模式 单人 战队
         */
        @Column(name = "game_mode")
        private Integer gameMode;

        /**
         * 比赛人数 1 2 4
         */
        @Column(name = "game_people")
        private Integer gamePeople;

        /**
         * 比赛队伍数
         */
        @Column(name = "game_team_num")
        private Integer gameTeamNum;

        /**
         * 比赛场次
         */
        @Column(name = "game_num")
        private Integer gameNum;

        /**
         * 比赛花费
         */
        @Column(name = "game_bill")
        private Integer gameBill;

        /**
         * 比赛开始时间
         */
        @Column(name = "game_start_time")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date gameStartTime;

        /**
         * 比赛时间分钟
         */
        @Column(name = "game_time")
        private int gameTime;

        /**
         * 小比赛时间间隔分钟
         */
        @Column(name = "game_Dert")
        private int gameDert;

        /**
         * 比赛时间列表
         */
        @Column(name = "time_list")
        private String timeList;

        /**
         * 排名积分方式
         */
        @Column(name = "game_rank_items")
        private String gameRankItems;

        /**
         * 杀人积分方式
         */
        @Column(name = "game_kill_items")
        private String gameKillItems;

        /**
         * 比赛 Logo
         */
        @Column(name = "game_logo")
        private String gameLogo;

        /**
         * 比赛简介
         */
        @Column(name = "game_desc")
        private String gameDesc;

        /**
         * 比赛地图
         */
        @Column(name = "game_map")
        private String gameMap;

        /**
         * 是否使用比赛地图位置
         */
        @Column(name = "use_game_map_pos")
        private Boolean useGameMapPos;

        /**
         * 比赛地图位置
         */
        @Column(name = "game_map_pos")
        private String gameMapPos;
}
