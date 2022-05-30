package com.gs.model.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ToString
public class PUBGMatchesVO {
    /**
     * Match name
     */
    String defMatchName;

    /**
     * 比赛开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gameTime;

    private Long defMatchId;

    private Integer index;

    private Integer isLike;
}
