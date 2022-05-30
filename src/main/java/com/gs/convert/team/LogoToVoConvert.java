package com.gs.convert.team;

import com.gs.model.entity.jpa.db1.team.Logo;
import com.gs.model.vo.team.LogoVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 战队实体类转VO工具类
 * User: lys
 * DateTime: 2022-05-10
 **/
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class LogoToVoConvert {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Entity转Vo
     *
     * @param entity 对应数据库的实体
     * @return 输出Vo
     */
    @Mapping(target = "createTime", expression = "java(sdf.format(entity.getCreateTime()))")
    public abstract LogoVo toVo(Logo entity);

    /**
     * Entity集合转DTO集合
     *
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    public abstract List<LogoVo> toVo(List<Logo> entityList);
}
