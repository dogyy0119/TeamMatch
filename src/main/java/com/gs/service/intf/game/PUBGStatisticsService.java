package com.gs.service.intf.game;

import com.gs.model.entity.jpa.db1.game.PUBGSeason;

import java.text.ParseException;
import java.util.List;

public interface PUBGStatisticsService {
    /**
     * 获取赛季列表
     * @return
     */
    List<PUBGSeason> GetSeasons();

    /**
     * 获取当前赛季
     * @return
     */
    PUBGSeason GetCurSeason();

    /**
     * 获取上个赛季
     * @return
     */
    PUBGSeason GetLastSeason();

    /**
     *
     * @param accountid
     * @param seasonid
     * @return
     */
    String GetSeasonStats( String accountid, String seasonid) ;

    /**
     *
     * @param accountid
     * @param seasonid
     * @return
     */
    String GetRankedSeasonStats(String accountid, String seasonid);

    /**
     *
     * @param accountid
     * @return
     */
    String GetLifetimeStats(String accountid);

    /**
     *
     * @param gameMode
     * @param playerIds
     * @param gamepad
     * @return
     */
    String GetLifetimeStats(String gameMode, String playerIds, Boolean gamepad);

    /**
     *
     * @param matchId
     * @return
     */
    String GetMatch(String matchId);

    /**
     * 拉取用户比赛统计数据存入数据库
     * @param memberId
     * @param accountid
     * @param curSeasonid
     * @param lastSeasonid
     */
    Boolean UpdateAll(Long memberId, String accountid, String curSeasonid, String lastSeasonid);

    /**
     * 获取比赛id
     * @param accountid
     * @return
     */
    List<String>  GetPlayerById(String accountid);

    /**
     * 获取比赛数据
     * @param matchid
     * @return
     */
    String GetMatchById(String pubgId, String matchid) throws ParseException;

    /**
     *  按matchId 拉取比赛数据存入数据库
     * @param pubgId
     * @param matchId
     * @return
     */
    public Boolean PullMathchData(String pubgId, String matchId) throws ParseException;
}
