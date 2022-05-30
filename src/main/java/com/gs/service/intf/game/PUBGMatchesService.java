package com.gs.service.intf.game;

import com.gs.model.dto.game.PUBGMatchesDTO;
import com.gs.model.dto.vo.PUBGMatchesVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PUBGMatchesService {

    /**
     * 创建比赛
     * @param pubgMatchesId
     * @return
     */
    PUBGMatchesDTO create(String pubgMatchesId, Long defMatchId);

    /**
     * 删除比赛
     * @param  id 比赛id
     */
    void delete(String id);


    /**
     *
     * @param memberId
     * @param defMatchId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PUBGMatchesDTO getPUBGMatchesByDefMatchId(
            @RequestParam Long memberId,
            @RequestParam Long defMatchId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize);

    /**
     *
     * @param memberId
     * @param matchType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesDTO> getPUBGMatchesByMatchType(
            @RequestParam Long memberId,
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize);

    /**
     *
     * @param memberId
     * @param matchType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesVO> getPUBGMatchesSortByMatchType(
            @RequestParam Long memberId,
            @RequestParam Long teamId,
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize);

    /**
     *
     * @param PUBGPlayerName
     * @param matchType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesDTO> findPUBGMatchesByPUBGPlayerNameAndMatchType(
            @RequestParam String PUBGPlayerName,
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize);

    /**
     *
     * @param PUBGPlayerId
     * @param matchType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesDTO> findPUBGMatchesByPUBGPlayerIdAndMatchType(
            @RequestParam String PUBGPlayerId,
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize);
}
