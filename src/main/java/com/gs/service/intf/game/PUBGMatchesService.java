package com.gs.service.intf.game;

import com.gs.model.dto.game.PUBGMatchesDTO;
import com.gs.model.dto.vo.PUBGAchievementArrayVO;
import com.gs.model.dto.vo.PUBGMatchesVO;
import com.gs.model.entity.jpa.db1.game.PUBGAchievement;
import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import com.gs.model.entity.jpa.db1.game.PUBGTeam;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
     * 根据PUBG 玩家名字，返回玩家比赛列表
     * @param name
     * @return
     */
    List<String> getPlayerMatches(String name);

    /**
     *
     * @param pubgMatchesId
     * @param defMatchId
     * @param defMatchIndex
     */
    public void getPUBGMatches(String pubgMatchesId, Long defMatchId, Integer defMatchIndex, Map<Integer,Integer> rank, Integer killScore);

    /**
     *
     * @param memberId
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesVO> getPUBGMatchesByMemberId(
            Long memberId,
            Integer pageNum,
            Integer pageSize);

    /**
     *
     * @param memberId
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesVO> getPUBGMatchesByMatchName(
            String matchName,
            Integer pageNum,
            Integer pageSize);

    /**
     *
     * @param key
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesVO> queryPBUGMatchBykey(
            String key,
            Integer pageNum,
            Integer pageSize);


    /**
     * @param defMatchId
     * @return
     */
    PUBGMatchesDTO getPUBGMatchesByDefMatchId(
            Long defMatchId,
            Integer index);

    /**
     * @param defMatchId
     * @return
     */
    void updatePUBGMatchesByDefMatchId(
            Long memberId,
            Long defMatchId,
            Integer index,
            PUBGMatchesDTO pubgMatchesDTO);

    /**
     *
     * @param memberId
     * @param matchType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesDTO> getPUBGMatchesByMatchType(
            Long memberId,
            String matchType,
            Integer pageNum,
            Integer pageSize);

    /**
     *
     * @param memberId
     * @param matchType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesVO> getPUBGMatchesSortByMatchType(
            Long memberId,
            Long teamId,
            String matchType,
            Integer pageNum,
            Integer pageSize);

    /**
     *
     * @param PUBGPlayerName
     * @param matchType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesDTO> findPUBGMatchesByPUBGPlayerNameAndMatchType(
            String PUBGPlayerName,
            String matchType,
            Integer pageNum,
            Integer pageSize);

    /**
     *
     * @param PUBGPlayerId
     * @param matchType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<PUBGMatchesDTO> findPUBGMatchesByPUBGPlayerIdAndMatchType(
            String PUBGPlayerId,
            String matchType,
            Integer pageNum,
            Integer pageSize);

    /**
     *
     * @param defMatchId
     * @return
     */
    List<PUBGMatches> findPUBGMatchesByDefMatchId(Long defMatchId );

    /**
     *  获取正在进行的创建者比赛列表
     * @param memberId
     * @return
     */
    List<PUBGMatchesVO> getRunningPUBGMatchesByCreatId(Long memberId) throws ParseException;

    /**
     *  获取正在进行的参与的比赛列表
     * @param memberId
     * @return
     */
    List<PUBGMatchesVO> getRunningPUBGMatchesByOrderId(Long memberId, Long teamId) throws ParseException;

    /**
     *  获取战队成就
     * @param memberId
     * @param teamId
     * @return
     */
    List<PUBGMatchesVO> getAchievementByTeamId(Long memberId, Long teamId);


    /**
     * 查找得分最多的队务
     * @param defMatchId
     * @return
     */
    PUBGTeam findTopScoreTeamByDefMatchId(Long defMatchId);

    /**
     * 获取战队成就
     * @param teamId
     * @return
     */
    PUBGAchievementArrayVO getTeamSuccessByTeamId(Long teamId);
}
