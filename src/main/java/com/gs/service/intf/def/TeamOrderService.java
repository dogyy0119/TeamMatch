package com.gs.service.intf.def;

import com.gs.model.dto.def.TeamOrderDTO;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.model.entity.jpa.db1.def.TeamOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TeamOrderService {
    /**
     * 根据id查找比赛
     * @param  id 主键id
     * @return UserDTO
     */
    TeamOrderDTO findById(Long id);

    /**
     * 创建比赛
     * @param  dto 用户dto
     * @return PUBGMatchesDTO 创建成功后的dto
     */
    TeamOrderDTO create(TeamOrderDTO dto);

    /**
     * 更新比赛
     * @param  dto 用户dto
     */
    void update(TeamOrderDTO dto);

    /**
     * 删除比赛
     * @param  id 比赛id
     */
    void delete(Long id);

    /**
     * 查询个人报名队内比赛
     * @param memberId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<TeamOrderDTO> findTeamOrderByMember(Long memberId,
                                             Integer pageNum,
                                             Integer pageSize);

    /**
     * 查询个人报名队内比赛
     * @param teamId
     * @param pageNum
     * @param pageSize
     * @return
     */

    Page<TeamOrderDTO> findTeamOrderByTeamId(Long teamId,
                                          Integer pageNum,
                                          Integer pageSize);

    /**
     *
     * @param defMatchOrderId
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<TeamOrderDTO> findTeamOrderByDefMatchOrderIdAndStatus(
            @RequestParam Long defMatchOrderId,
            @RequestParam Integer status,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize);

    /**
     *
     * @param defMatchOrderId
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<TeamOrderDTO> findTeamOrderByDefMatchOrderId(
            @RequestParam Long defMatchOrderId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize);
}
