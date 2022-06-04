package com.gs.service.intf.def;

import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.dto.def.DefMatchOrderQuery;

import java.util.List;

public interface DefMatchOrderService {

    /**
     * 根据id查找比赛
     * @param  id 主键id
     * @return DefMatchOrderDTO
     */
    DefMatchOrderDTO findById(Long id);

    /**
     * 根据id查找比赛
     * @param  matchId
     * @param  orderId
     * @return DefMatchOrderDTO
     */
    DefMatchOrderDTO findByMatchIdAndOrderId(Long matchId, Long orderId);

    /**
     * 创建比赛
     * @param  dto 用户dto
     * @return PUBGMatchesDTO 创建成功后的dto
     */
    DefMatchOrderDTO create(DefMatchOrderDTO dto);

    /**
     * 更新比赛
     * @param  dto 用户dto
     */
    Boolean update(DefMatchOrderDTO dto);

    /**
     * 删除比赛
     * @param  id 比赛id
     */
    void delete(Long id);

    List<DefMatchOrderDTO> getMatchOrderPage(DefMatchOrderQuery defMatchOrderQuery,
                                             Integer pageNum,
                                             Integer pageSize);

    /**
     *
     * @param mode        比赛报名方式
     * @param orderId     比赛报名id
     * @param status      比赛报名状态
     * @param pageNum     查询页码
     * @param pageSize    查询页容量
     * @return
     */
    List<DefMatchDTO> getMatchPage(Integer mode,
                                   Long orderId,
                                   Integer status,
                                   Integer pageNum,
                                   Integer pageSize);

    /**
     *
     * @param memberId
     * @param matchId
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<DefMatchOrderDTO> getMatchOrdersPageByMatchId(
            Long memberId,
            Long matchId,
            Integer pageNum,
            Integer pageSize);

    /**
     *
     * @param memberId
     * @param matchId
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<DefMatchOrderDTO> getMatchOrdersPageByMatchIdAndStatus(
            Long memberId,
            Long matchId,
            Integer status,
            Integer pageNum,
            Integer pageSize);

}
