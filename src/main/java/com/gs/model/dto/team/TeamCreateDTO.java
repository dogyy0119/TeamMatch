package com.gs.model.dto.team;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建战队接口输入dto
 * User: lys
 * DateTime: 2022-04-22
 **/

@Data
@ToString
public class TeamCreateDTO implements Serializable {
    /**
     * 战队名称
     */
    @NotBlank(message = "战队名称不能为空")
    private String name;

     /**
     * 战队Logo
     */
    private String logoUrl;

    /**
     * 战队的描述信息
     */
    private String descriptionInfo;

}