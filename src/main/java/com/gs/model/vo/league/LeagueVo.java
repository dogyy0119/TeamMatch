package com.gs.model.vo.league;

import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.vo.team.TeamVo;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 联盟接口输出Vo
 * User: lys
 * DateTime: 2022-04-22
 **/
@Data
@ToString
public class LeagueVo implements Serializable {

    private Long id;

    /**
     * 联盟名称
     */
    private String name;

    /**
     * 创建者的用户ID
     */
    private Long createMemberId;

    /**
     * 联盟的最大战队数量
     */
    private Integer maxTeamNum;

    /**
     * 联盟的创建时间
     */
    private String createTime;

    /**
     * ；联盟logo
     */
    private String logoUrl;


    /**
     * 联盟的描述信息
     */
    private String descriptionInfo;

    /**
     * 联盟战队列表
     */
    private List<TeamVo> teamVoLst;
}
