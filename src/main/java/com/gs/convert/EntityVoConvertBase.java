package com.gs.convert;

import java.util.List;

/**
 * @author lys
 * @date 2022-05-10
 */
public interface EntityVoConvertBase<V, E> {

    /**
     * Entity转Vo
     * @param entity 对应数据库的实体
     * @return 输出Vo
     */

    V toVo(E entity);

    /**
     * Entity集合转DTO集合
     * @param entityList 对应数据库的实体
     * @return 输出Vo
     */
    List <V> toVo(List<E> entityList);
}
