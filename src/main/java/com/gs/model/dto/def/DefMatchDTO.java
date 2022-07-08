package com.gs.model.dto.def;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gs.annotation.IsValidValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class DefMatchDTO implements Serializable {

    private Long id;

    /**
     * 创建者 成员ID，一对于关联Member的主键id
     */
    @NotBlank(message = "创建者不能为空")
    private Long memberId;

    /**
     * 比赛名称
     */
    @NotBlank(message = "比赛名称不能为空")
    private String name;

    /**
     * 比赛类型 TPP FPP
     */
    @NotBlank(message = "比赛类型不能为空")
    private String matchType;

    /**
     * 服务器类型 亚洲 欧洲 美洲等
     */
    @NotBlank(message = "服务器类型不能为空")
    private String serverType;

    /**
     * 比赛模式 单人 战队
     */
    @NotBlank(message = "比赛模式不能为空")
    private Integer gameMode;

    /**
     * 比赛人数 1 2 4
     */
    @NotBlank(message = "比赛人数不能为空")
    @IsValidValue
    private Integer gamePeople;

    /**
     * 比赛队伍数
     */
    @NotBlank(message = "比赛队伍数不能为空")
    @IsValidValue
    private Integer gameTeamNum;

    /**
     * 比赛场次
     */
    @NotBlank(message = "比赛场次不能为空")
    @IsValidValue
    private Integer gameNum;

    /**
     * 比赛花费
     */
    @NotBlank(message = "比赛花费不能为空")
    private Integer gameBill;


    /**
     * 比赛开始时间
     */
    @NotBlank(message = "比赛开始时间不能为空")
    @ApiModelProperty(value = "开始时间", example = "2021-10-05 00:00:00")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gameStartTime;

    /**
     * 比赛时间分钟
     */
    @NotBlank(message = "比赛时间不能为空")
    @IsValidValue
    private Integer gameTime;

    /**
     * 小比赛时间间隔分钟
     */
    @NotBlank(message = "比赛时长不能为空")
    private Integer gameDert;

    /**
     * 比赛时间列表
     */
    @Column(name = "time_list")
    private String timeList;

    /**
     * 排名积分方式
     */
    @NotBlank(message = "排名积分方式不能为空")
    private String gameRankItems;

    /**
     * 杀人积分方式
     */
    @NotBlank(message = "杀人积分方式不能为空")
    private String gameKillItems;

    /**
     * 比赛 Logo
     */
    private String gameLogo;

    /**
     * 比赛简介
     */
    private String gameDesc;

}
