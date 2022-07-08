package com.gs.model.vo.match.league;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 联盟战队实体类
 * User: lys
 * DateTime: 2022-05-1
 **/
@Data
@ToString
public class LeagueMatchWholeVo implements Serializable{

    private Long id;
    /**
     * 联盟ID
     */
    private Long leagueId;
    /**
     * 联盟比赛名称
     */
    @NotBlank
    private String matchName;

    /**
     * 联赛开始时间
     */
    private String startDate;

    /**
     * 联赛结束时间
     */
    private String endDate;

    /**
     * 比赛模式
     */
    private String matchMode;

    /**
     * 对局数量
     */
    private Integer matchNum;

    /**
     * 战队数量
     */
    private Integer teamNum;

    /**
     * 比赛报名费用
     */
    private Double matchCost;
}
