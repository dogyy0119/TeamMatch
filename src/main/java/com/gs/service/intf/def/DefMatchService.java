package com.gs.service.intf.def;

import com.gs.model.dto.def.DefMatchDTO;

import com.gs.model.entity.jpa.db1.def.DefMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface DefMatchService {

    /**
     * 根据id查找比赛
     * @param  id 主键id
     * @return UserDTO
     */
    DefMatchDTO findById(Long id);

    /**
     * 创建比赛
     * @param  dto 用户dto
     * @return PUBGMatchesDTO 创建成功后的dto
     */
    DefMatchDTO create(DefMatchDTO dto);

    /**
     * 更新比赛
     * @param  dto 用户dto
     */
    void update(DefMatchDTO dto);

    /**
     * 删除比赛
     * @param  id 比赛id
     */
    void delete(Long id);

    /**
     * 查询比赛分页
     * @param memberId
     * @param pageable
     * @return
     */
    Page<DefMatch> findMatchesByMember(Long memberId, Pageable pageable);

    /**
     *
     * @param date
     * @param pageable
     * @return
     */
    Page<DefMatch> findMatchesByDate(Date date, Pageable pageable);

    /**
     * 按比赛类型查询比赛分页
     * @param matchType
     * @param pageable
     * @return
     */
    Page<DefMatch> findMatchesByMatchType(String matchType, Pageable pageable);

    /**
     * 按比赛类型查询比赛分页
     * @param gameMode
     * @param pageable
     * @return
     */
    Page<DefMatch> findMatchesByGameMode(int gameMode, Pageable pageable);

    /**
     *
     * @param memberId
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<DefMatchDTO> getManageMatchesPage(
            Long memberId,
            Integer pageNum,
            Integer pageSize);


    /**
     *
     * @param littleTime
     * @param bigTime
     * @return
     */
    List<DefMatchDTO> getMatchesByTime(Date littleTime, Date bigTime);

    /**
     *
     * @param key
     * @return
     */
    List<DefMatchDTO> getMatchByKey(String key,
                                    Integer pageNum,
                                    Integer pageSize);
}
