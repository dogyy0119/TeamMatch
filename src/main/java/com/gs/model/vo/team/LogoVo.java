package com.gs.model.vo.team;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 战队接口输出Vo
 * User: lys
 * DateTime: 2022-04-22
 **/
@Data
@ToString
public class LogoVo implements Serializable {

    /**
     * logoId
     */
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 上传该logo的成员ID
     */
    private Long memberId;

    /**
     * 该图片的url
     */
    private String url;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 上传时间
     */
    private String createTime;

}
