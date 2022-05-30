package com.gs.service.intf.def;

import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.dto.vo.DefMatchManagerOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DefMatchManageService {

    /**
     * 根据id查找比赛
     * @param  id 主键id
     * @return UserDTO
     */
    DefMatchManageDTO findById(Long id);

    /**
     * 根据id查找比赛
     * @param  id 主键id
     * @return UserDTO
     */
    DefMatchManageDTO findByMatchId(Long id);

    /**
     * 创建比赛
     * @param  dto 用户dto
     * @return PUBGMatchesDTO 创建成功后的dto
     */
    DefMatchManageDTO create(DefMatchManageDTO dto);

    /**
     * 更新比赛
     * @param  dto 用户dto
     */
    void update(DefMatchManageDTO dto);

    /**
     * 删除比赛
     * @param  id 比赛id
     */
    void delete(Long id);

    /**
     *
     * @param memberId
     * @param matchId
     * @param pageNum
     * @param pageSize
     * @return
     */
    DefMatchManagerOrders getMatchManagesPageByMatch(
            Long memberId,
            Long matchId,
            Integer pageNum,
            Integer pageSize);


}
