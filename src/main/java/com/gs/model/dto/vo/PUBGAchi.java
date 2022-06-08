package com.gs.model.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 比赛成就
 */
@Data
@ToString
public class PUBGAchi {
    /**
     * 赛事名称
     */
    String defMatchName;

    /**
     * 比赛开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gameTime;

    /**
     * 比赛Id
     */
    private Long defMatchId;

    /**
     * 队伍排名
     */
    private Integer index;
}
