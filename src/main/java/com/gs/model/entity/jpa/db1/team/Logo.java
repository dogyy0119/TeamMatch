package com.gs.model.entity.jpa.db1.team;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author guozy
 * @date 2019-03-02
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "t_logos")
public class Logo implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * 图片名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 上传该logo的成员ID
     */
    @Column(name = "memberId")
    private Long memberId;

    /**
     * logo类型(1、战队Logo；2、联盟Logo；3、联盟比赛Logo)
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 该Logo存储路径（相对路径）
     */
    @Column(name = "url")
    @NotBlank
    private String url;

    /**
     * 文件大小
     */
    @Column(name = "size")
    private Long size;

    @CreatedDate
    @Column(name = "createTime")
    private Date createTime;
}