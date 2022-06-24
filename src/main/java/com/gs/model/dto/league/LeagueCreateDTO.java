package com.gs.model.dto.league;

import com.gs.annotation.IsMemberExist;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建联盟接口输入dto
 * User: lys
 * DateTime: 2022-04-22
 **/

@Data
@ToString
public class    LeagueCreateDTO implements Serializable {
    /**
     * 联盟名称
     */
    @NotBlank(message = "联盟名称不能为空")
    private String name;

    /**
     * 创建者ID
     */
    @IsMemberExist
    private Long createMemberId;

     /**
     * 战队Logo
     */
    private String logoUrl;

    /**
     * 联盟的描述信息
     */
    private String descriptionInfo;

}