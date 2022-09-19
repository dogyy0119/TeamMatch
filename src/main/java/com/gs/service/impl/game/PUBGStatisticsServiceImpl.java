package com.gs.service.impl.game;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gs.config.PUBGConfig;
import com.gs.model.entity.jpa.db1.game.*;
import com.gs.repository.jpa.game.PUBGMatchDataRepository;
import com.gs.repository.jpa.game.PUBGSeasonRepository;
import com.gs.repository.jpa.game.PUBGStatisticsRepository;
import com.gs.scheduled.ScheduledTask;
import com.gs.service.intf.game.PUBGStatisticsService;
import com.gs.utils.HttpUtils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PUBGStatisticsServiceImpl implements PUBGStatisticsService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PUBGStatisticsServiceImpl.class);

    @Autowired
    private PUBGConfig pubgConfig = new PUBGConfig();

    @Autowired
    private PUBGSeasonRepository pubgSeasonRepository;

    @Autowired
    private PUBGStatisticsRepository pubgStatisticsRepository;

    @Autowired
    private PUBGMatchDataRepository pubgMatchDataRepository;

    private String squadStr= "{\n" +
            "                    \"currentTier\": {\n" +
            "                        \"tier\": \"Silver\",\n" +
            "                        \"subTier\": \"1\"\n" +
            "                    },\n" +
            "                    \"currentRankPoint\": 0,\n" +
            "                    \"bestTier\": {\n" +
            "                        \"tier\": \"Silver\",\n" +
            "                        \"subTier\": \"1\"\n" +
            "                    },\n" +
            "                    \"bestRankPoint\": 0,\n" +
            "                    \"roundsPlayed\": 0,\n" +
            "                    \"avgRank\": 0,\n" +
            "                    \"avgSurvivalTime\": 0,\n" +
            "                    \"top10Ratio\": 0,\n" +
            "                    \"winRatio\": 0,\n" +
            "                    \"assists\": 1,\n" +
            "                    \"wins\": 0,\n" +
            "                    \"kda\": 0,\n" +
            "                    \"kdr\": 0,\n" +
            "                    \"kills\": 0,\n" +
            "                    \"deaths\": 0,\n" +
            "                    \"roundMostKills\": 0,\n" +
            "                    \"longestKill\": 0,\n" +
            "                    \"headshotKills\": 0,\n" +
            "                    \"headshotKillRatio\": 0,\n" +
            "                    \"damageDealt\": 0.0,\n" +
            "                    \"dBNOs\": 0,\n" +
            "                    \"reviveRatio\": 0,\n" +
            "                    \"revives\": 0,\n" +
            "                    \"heals\": 0,\n" +
            "                    \"boosts\": 0,\n" +
            "                    \"weaponsAcquired\": 0,\n" +
            "                    \"teamKills\": 0,\n" +
            "                    \"playTime\": 0,\n" +
            "                    \"killStreak\": 0\n" +
            "                }";

    @Override
    public List<PUBGSeason> GetSeasons() {
        String url = "/shards/steam/seasons";

        String result = MakeGETRequest(url);
        if (result.equals("")|| result ==null) {
            logger.info("GetSeasons result is null");
            return null;
        }

        List<PUBGSeason> pubgSeasonList = new ArrayList<>();
        JSONObject json = new JSONObject(result);
        JSONArray array = json.getJSONArray("data");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject job = array.getJSONObject(i);

//                System.out.println("type: " + job.get("type").toString());
//                System.out.println("id: " + job.get("id").toString());
                if (job.get("type").toString().equals("season")) {
                    JSONObject attributes = job.getJSONObject("attributes");
                    String isCurrentSeason = attributes.get("isCurrentSeason").toString();
                    PUBGSeason pubgSeason = pubgSeasonRepository.findPUBGSeasonByName(job.get("id").toString());
                    if (pubgSeason == null) {
                        pubgSeason = new PUBGSeason();
                        pubgSeason.setName(job.get("id").toString());
                        pubgSeason.setIsCurrentSeason(isCurrentSeason.equals("true"));
                        pubgSeasonRepository.save(pubgSeason);
                    } else {
                        pubgSeason.setIsCurrentSeason(isCurrentSeason.equals("true"));
                        pubgSeasonRepository.save(pubgSeason);
                    }
                    pubgSeasonList.add(pubgSeason);
                }
            }
        }
        return pubgSeasonList;
    }

    @Override
    public PUBGSeason GetCurSeason() {
        String url = "/shards/steam/seasons";

        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetCurSeason result is null");
            return null;
        }

        PUBGSeason season = new PUBGSeason();
        JSONObject json = new JSONObject(result);
        JSONArray array = json.getJSONArray("data");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject job = array.getJSONObject(i);
                if (job.get("type").toString().equals("season")) {

                    JSONObject attributes = job.getJSONObject("attributes");
                    String isCurrentSeason = attributes.get("isCurrentSeason").toString();
                    PUBGSeason pubgSeason = pubgSeasonRepository.findPUBGSeasonByName(job.get("id").toString());
                    if (pubgSeason == null) {
                        pubgSeason = new PUBGSeason();
                        pubgSeason.setName(job.get("id").toString());
                        pubgSeason.setIsCurrentSeason(isCurrentSeason.equals("true"));
                        pubgSeasonRepository.save(pubgSeason);
                    } else {
                        pubgSeason.setIsCurrentSeason(isCurrentSeason.equals("true"));
                        pubgSeasonRepository.save(pubgSeason);
                    }
                    if (isCurrentSeason.equals("true")) {
                        season = pubgSeason;
                    }
                }
            }
        }

        return season;
    }

    @Override
    public PUBGSeason GetLastSeason() {
        String url = "/shards/steam/seasons";

        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetLastSeason result is null");
            return null;
        }

        PUBGSeason season = new PUBGSeason();
        JSONObject json = new JSONObject(result);
        JSONArray array = json.getJSONArray("data");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject job = array.getJSONObject(i);
                if (job.get("type").toString().equals("season")) {

                    JSONObject attributes = job.getJSONObject("attributes");
                    String isCurrentSeason = attributes.get("isCurrentSeason").toString();
                    PUBGSeason pubgSeason = pubgSeasonRepository.findPUBGSeasonByName(job.get("id").toString());
                    if (pubgSeason == null) {
                        pubgSeason = new PUBGSeason();
                        pubgSeason.setName(job.get("id").toString());
                        pubgSeason.setIsCurrentSeason(isCurrentSeason.equals("true"));
                        pubgSeasonRepository.save(pubgSeason);
                    } else {
                        pubgSeason.setIsCurrentSeason(isCurrentSeason.equals("true"));
                        pubgSeasonRepository.save(pubgSeason);
                    }
                    if (isCurrentSeason.equals("true")) {
                        break;
                    }
                    season = pubgSeason;
                }
            }
        }

        return season;
    }

    @Override
    public String GetSeasonStats( String accountid, String seasonid) {
        String url = "/shards/steam/players/" + accountid + "/seasons/" +seasonid;

        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetSeasonStats result is null");
            return null;
        }
//        PutSeasonStats(46L, result);
        return result;
    }

    @Override
    public String GetRankedSeasonStats(String accountid, String seasonid) {
        String url = "/shards/steam/players/" + accountid + "/seasons/" + seasonid + "/ranked";

        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetRankedSeasonStats result is null");
            return null;
        }

        JSONObject json = new JSONObject(result);
        JSONObject jsonRankedGameModeStats = json.getJSONObject("data").getJSONObject("attributes").getJSONObject("rankedGameModeStats");
        JSONObject squad = jsonRankedGameModeStats.getJSONObject("squad");
        if( squad == null ) {
            squad =  new JSONObject(squadStr);
            jsonRankedGameModeStats.putOpt("squad",squad);
            logger.info( "liuhang: add squad:  " + jsonRankedGameModeStats);
        }

        logger.info( "liuhang: add:  " + json);
        result = json.toString();


//        PutRankedSeasonStats( 46L, result);
        return result;
    }

    @Override
    public String GetLifetimeStats(String accountid) {
        String url = "/shards/steam/players/" + accountid + "/seasons/lifetime";

        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetLifetimeStats result is null");
            return null;
        }

//        String gameData = GetGameDate(result);
//        PutGameData(47L, gameData);
//        PutLifetimeStats( 47L, result);
        return result;
    }

    @Override
    public String GetLifetimeStats(String gameMode, String playerIds, Boolean gamepad) {
        String url = "/shards/steam/seasons/lifetime/gameMode/" + gameMode + "/players?filter[playerIds]="+playerIds+"&filter[gamepad]="+gamepad;

        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetLifetimeStats result is null");
            return null;
        }
        return result;
    }

    @Override
    public String GetMatch(String matchId) {
        String url = "/shards/steam/matches/"+ matchId;

        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetMatch result is null");
            return null;
        }
        return result;
    }

    @Override
    public List<String> GetPlayerById(String accountid) {
        String url = "/shards/steam/players/"+ accountid;

        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetPlayerById result is null");
            return null;
        }
//        System.out.println( result );
        List<String> matchIdList = GetMatchIdList(result);
        return matchIdList;
    }

    @Override
    public String GetMatchById(String pubgId,String matchid) throws ParseException {
        String url = "/shards/steam/matches/"+ matchid;
        String result = MakeGETRequest(url);
        if ( result ==null || result.equals("")) {
            logger.info("GetMatchById result is null");
            return null;
        }
//        System.out.println( result );
//        GetPUBGGameDate(result, pubgId, matchid);
        return result;
    }

    private String MakeGETRequest(String url )
    {
        String BaseUrl = "https://api.pubg.com";

        url = BaseUrl + url;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + pubgConfig.getToken_pull());
        headerMap.put("Accept", pubgConfig.getHead_formate());
        System.out.println( url );
        String result = HttpUtils.doGet(url, headerMap);

        return result;
    }

    private void PutRankedSeasonStats(Long memberId,String rankedSeasonStats) {
        rankedSeasonStats = rankedSeasonStats.replace("solo-fpp","solofpp");
        rankedSeasonStats = rankedSeasonStats.replace("duo-fpp","duofpp");
        rankedSeasonStats = rankedSeasonStats.replace("squad-fpp","squadfpp");

        System.out.println( rankedSeasonStats );

        PUBGStatistics pubgStatistics = pubgStatisticsRepository.findPUBGStatisticsByMemberId(memberId);
        if(pubgStatistics == null) {
            pubgStatistics = new PUBGStatistics();
            pubgStatistics.setMemberId(memberId);
            pubgStatistics.setRankedSeasonStats(rankedSeasonStats);
            pubgStatistics.setDatetime( new Date());
        } else {
            pubgStatistics.setRankedSeasonStats(rankedSeasonStats);
            pubgStatistics.setDatetime( new Date() );
        }
        pubgStatisticsRepository.save(pubgStatistics);
    }

    private void PutLifetimeStats(Long memberId,String lifetimeStats) {
        lifetimeStats = lifetimeStats.replace("solo-fpp","solofpp");
        lifetimeStats = lifetimeStats.replace("duo-fpp","duofpp");
        lifetimeStats = lifetimeStats.replace("squad-fpp","squadfpp");

        PUBGStatistics pubgStatistics = pubgStatisticsRepository.findPUBGStatisticsByMemberId(memberId);
        if(pubgStatistics == null) {
            pubgStatistics = new PUBGStatistics();
            pubgStatistics.setMemberId(memberId);
            pubgStatistics.setLifetimeStats(lifetimeStats);
            pubgStatistics.setDatetime( new Date());
        } else {
            pubgStatistics.setLifetimeStats(lifetimeStats);
            pubgStatistics.setDatetime( new Date() );
        }
        pubgStatisticsRepository.save(pubgStatistics);
    }

    private void PutGameData(Long memberId,String gameData) {
        PUBGStatistics pubgStatistics = pubgStatisticsRepository.findPUBGStatisticsByMemberId(memberId);
        if(pubgStatistics == null) {
            pubgStatistics = new PUBGStatistics();
            pubgStatistics.setMemberId(memberId);
            pubgStatistics.setGameData(gameData);
            pubgStatistics.setDatetime( new Date());
        } else {
            pubgStatistics.setGameData(gameData);
            pubgStatistics.setDatetime( new Date() );
        }
        pubgStatisticsRepository.save(pubgStatistics);
    }

    private void PutSeasonStats(Long memberId,String seasonStats) {
        seasonStats = seasonStats.replace("solo-fpp","solofpp");
        seasonStats = seasonStats.replace("duo-fpp","duofpp");
        seasonStats = seasonStats.replace("squad-fpp","squadfpp");

        PUBGStatistics pubgStatistics = pubgStatisticsRepository.findPUBGStatisticsByMemberId(memberId);
        if(pubgStatistics == null) {
            pubgStatistics = new PUBGStatistics();
            pubgStatistics.setMemberId(memberId);
            pubgStatistics.setSeasonStats(seasonStats);
            pubgStatistics.setDatetime( new Date());
        } else {
            pubgStatistics.setSeasonStats(seasonStats);
            pubgStatistics.setDatetime( new Date() );
        }
        pubgStatisticsRepository.save(pubgStatistics);
    }

    @Override
    public Boolean UpdateAll(Long memberId, String accountid, String curSeasonid, String lastSeasonid) {

        PUBGStatistics pubgStatistics = pubgStatisticsRepository.findPUBGStatisticsByMemberId(memberId);
        if(pubgStatistics == null) {
            pubgStatistics = new PUBGStatistics();
            pubgStatistics.setMemberId(memberId);

            String seasonStats = GetSeasonStats(accountid,curSeasonid);
            if( seasonStats != null && !seasonStats.equals("") ) {
                seasonStats = seasonStats.replace("solo-fpp", "solofpp");
                seasonStats = seasonStats.replace("duo-fpp", "duofpp");
                seasonStats = seasonStats.replace("squad-fpp", "squadfpp");
                pubgStatistics.setSeasonStats(seasonStats);
            }

            String lifetimeStats = GetLifetimeStats(accountid);
            if( lifetimeStats != null && !lifetimeStats.equals("") ) {
                lifetimeStats = lifetimeStats.replace("solo-fpp", "solofpp");
                lifetimeStats = lifetimeStats.replace("duo-fpp", "duofpp");
                lifetimeStats = lifetimeStats.replace("squad-fpp", "squadfpp");
                pubgStatistics.setLifetimeStats(lifetimeStats);

                String gameData = GetGameDate(lifetimeStats);
                pubgStatistics.setGameData(gameData);
            }

            String rankedSeasonStats = GetRankedSeasonStats(accountid,curSeasonid);
            if( rankedSeasonStats != null && !rankedSeasonStats.equals("") ) {
                rankedSeasonStats = rankedSeasonStats.replace("solo-fpp", "solofpp");
                rankedSeasonStats = rankedSeasonStats.replace("duo-fpp", "duofpp");
                rankedSeasonStats = rankedSeasonStats.replace("squad-fpp", "squadfpp");

                JSONObject json = new JSONObject(rankedSeasonStats);
                JSONObject jsonRankedGameModeStats = json.getJSONObject("data").getJSONObject("attributes").getJSONObject("rankedGameModeStats");
                JSONObject squad = jsonRankedGameModeStats.getJSONObject("squad");
                if( squad == null ) {
                    squad =  new JSONObject(squadStr);
                    jsonRankedGameModeStats.putOpt("squad",squad);
                    rankedSeasonStats = json.toString();
                }

                JSONObject squadfpp = jsonRankedGameModeStats.getJSONObject("squadfpp");
                if( squadfpp == null ) {
                    squadfpp =  new JSONObject(squadStr);
                    jsonRankedGameModeStats.putOpt("squadfpp",squadfpp);
                    rankedSeasonStats = json.toString();
                }

                pubgStatistics.setRankedSeasonStats(rankedSeasonStats);
            }

            pubgStatistics.setDatetime( new Date());
        } else {
            if( (new Date().getTime() - pubgStatistics.getDatetime().getTime()) < 18*60*60*1000) {
                logger.info(" time is not small ten one day");
                return false;
            }

            String seasonStats = GetSeasonStats(accountid,curSeasonid);
            if( seasonStats != null && !seasonStats.equals("") ) {
                seasonStats = seasonStats.replace("solo-fpp", "solofpp");
                seasonStats = seasonStats.replace("duo-fpp", "duofpp");
                seasonStats = seasonStats.replace("squad-fpp", "squadfpp");
                pubgStatistics.setSeasonStats(seasonStats);
            }

            String lifetimeStats = GetLifetimeStats(accountid);
            if( lifetimeStats != null && !lifetimeStats.equals("") ) {
                lifetimeStats = lifetimeStats.replace("solo-fpp", "solofpp");
                lifetimeStats = lifetimeStats.replace("duo-fpp", "duofpp");
                lifetimeStats = lifetimeStats.replace("squad-fpp", "squadfpp");
                pubgStatistics.setLifetimeStats(lifetimeStats);

                String gameData = GetGameDate(lifetimeStats);
                pubgStatistics.setGameData(gameData);
            }

            String rankedSeasonStats = GetRankedSeasonStats(accountid,curSeasonid);
            if( rankedSeasonStats != null && !rankedSeasonStats.equals("") ) {
                rankedSeasonStats = rankedSeasonStats.replace("solo-fpp", "solofpp");
                rankedSeasonStats = rankedSeasonStats.replace("duo-fpp", "duofpp");
                rankedSeasonStats = rankedSeasonStats.replace("squad-fpp", "squadfpp");

                JSONObject json = new JSONObject(rankedSeasonStats);
                JSONObject jsonRankedGameModeStats = json.getJSONObject("data").getJSONObject("attributes").getJSONObject("rankedGameModeStats");
                JSONObject squad = jsonRankedGameModeStats.getJSONObject("squad");
                if( squad == null ) {
                    squad =  new JSONObject(squadStr);
                    jsonRankedGameModeStats.putOpt("squad",squad);
                    rankedSeasonStats = json.toString();
                }
                JSONObject squadfpp = jsonRankedGameModeStats.getJSONObject("squadfpp");
                if( squadfpp == null ) {
                    squadfpp =  new JSONObject(squadStr);
                    jsonRankedGameModeStats.putOpt("squadfpp",squadfpp);
                    rankedSeasonStats = json.toString();
                }


                pubgStatistics.setRankedSeasonStats(rankedSeasonStats);
            }

            pubgStatistics.setDatetime( new Date() );
        }
        pubgStatisticsRepository.save(pubgStatistics);
        return true;
    }

    private String GetGameDate(String lifetimeStats) {
        JSONObject json = new JSONObject(lifetimeStats);
        if(json == null) {
            logger.info("GetGameDate json is null");
            return null;
        }
        JSONObject gameModeStats = json.getJSONObject("data").getJSONObject("attributes").getJSONObject("gameModeStats");
        if(gameModeStats == null) {
            logger.info("gameModeStats json is null");
            return null;
        }

        JSONObject duo = gameModeStats.getJSONObject("duo");
        JSONObject duofpp = gameModeStats.getJSONObject("duo-fpp");
        if( duofpp == null ){
            duofpp = gameModeStats.getJSONObject("duofpp");
        }

        JSONObject solo = gameModeStats.getJSONObject("solo");
        JSONObject solofpp = gameModeStats.getJSONObject("solo-fpp");
        if( solofpp == null ){
            solofpp = gameModeStats.getJSONObject("solofpp");
        }

        JSONObject squad = gameModeStats.getJSONObject("squad");
        JSONObject squadfpp = gameModeStats.getJSONObject("squad-fpp");
        if( squadfpp == null ){
            squadfpp = gameModeStats.getJSONObject("squadfpp");
        }

        TPP tpp = new TPP();
        tpp.setWin( Integer.valueOf(duo.get("wins").toString()) + Integer.valueOf(solo.get("wins").toString()) + Integer.valueOf(squad.get("wins").toString()));
        Integer TPPPlayTimes = Integer.valueOf(duo.get("wins").toString()) + Integer.valueOf(solo.get("wins").toString()) + Integer.valueOf(squad.get("wins").toString())
                + Integer.valueOf(duo.get("losses").toString()) + Integer.valueOf(solo.get("losses").toString()) + Integer.valueOf(squad.get("losses").toString());
        Integer TPPKill = Integer.valueOf(duo.get("kills").toString()) + Integer.valueOf(solo.get("kills").toString()) + Integer.valueOf(squad.get("kills").toString());
        Integer TPPDeth = Integer.valueOf(duo.get("losses").toString()) + Integer.valueOf(solo.get("losses").toString()) + Integer.valueOf(squad.get("losses").toString());
        double TPPDamage = Double.valueOf(duo.get("damageDealt").toString()) + Double.valueOf(solo.get("damageDealt").toString()) + Double.valueOf(squad.get("damageDealt").toString());
        tpp.setTop10( Double.valueOf(duo.get("top10s").toString()) + Double.valueOf(solo.get("top10s").toString()) + Double.valueOf(squad.get("top10s").toString()) );
        tpp.setLongestKill( Double.valueOf(duo.get("longestKill").toString()) + Double.valueOf(solo.get("longestKill").toString()) + Double.valueOf(squad.get("longestKill").toString()) );
        tpp.setHeadShotKill( Integer.valueOf(duo.get("headshotKills").toString()) + Integer.valueOf(solo.get("headshotKills").toString()) + Integer.valueOf(squad.get("headshotKills").toString()) );
        tpp.setRankPoints(Double.valueOf(duo.get("rankPoints").toString()) + Double.valueOf(solo.get("rankPoints").toString()) + Double.valueOf(squad.get("rankPoints").toString()) );
        tpp.setMaxKillStreaks( Integer.valueOf(duo.get("maxKillStreaks").toString()) + Integer.valueOf(solo.get("maxKillStreaks").toString()) + Integer.valueOf(squad.get("maxKillStreaks").toString()) );

        double TPPKDA  = ( Integer.valueOf(duo.get("kills").toString()) + Integer.valueOf(duo.get("assists").toString()) )/ (Integer.valueOf(duo.get("losses").toString() )> 0 ? Integer.valueOf(duo.get("losses").toString()):1)
                + ( Integer.valueOf(solo.get("kills").toString()) + Integer.valueOf(solo.get("assists").toString()) )/ (Integer.valueOf(solo.get("losses").toString() )> 0 ? Integer.valueOf(solo.get("losses").toString()):1)
                + ( Integer.valueOf(squad.get("kills").toString()) + Integer.valueOf(squad.get("assists").toString()) )/ (Integer.valueOf(squad.get("losses").toString() )> 0 ? Integer.valueOf(squad.get("losses").toString()):1);

        tpp.setKDA( TPPKDA  );
        tpp.setRangDamage( (TPPDamage / (TPPPlayTimes>0?TPPPlayTimes : 1)) / 3 );
        tpp.setKD( TPPKill / (TPPDeth>0?TPPDeth:1) );

        FPP fpp = new FPP();
        fpp.setWin( Integer.valueOf(duofpp.get("wins").toString()) + Integer.valueOf(solofpp.get("wins").toString()) + Integer.valueOf(squadfpp.get("wins").toString()) );
        Integer FPPPlayTimes = Integer.valueOf(duofpp.get("wins").toString()) + Integer.valueOf(solofpp.get("wins").toString()) + Integer.valueOf(squadfpp.get("wins").toString())
                + Integer.valueOf(duofpp.get("losses").toString()) + Integer.valueOf(solofpp.get("losses").toString()) + Integer.valueOf(squadfpp.get("losses").toString());
        Integer FPPKill = Integer.valueOf(duofpp.get("kills").toString()) + Integer.valueOf(solofpp.get("kills").toString()) + Integer.valueOf(squadfpp.get("kills").toString());
        Integer FPPDeth = Integer.valueOf(duofpp.get("losses").toString()) + Integer.valueOf(solofpp.get("losses").toString()) + Integer.valueOf(squadfpp.get("losses").toString());
        double FPPDamage = Double.valueOf(duofpp.get("damageDealt").toString()) + Double.valueOf(solofpp.get("damageDealt").toString()) + Double.valueOf(squadfpp.get("damageDealt").toString());
        fpp.setTop10( Double.valueOf(duofpp.get("top10s").toString()) + Double.valueOf(solofpp.get("top10s").toString()) + Double.valueOf(squadfpp.get("top10s").toString()) );
        fpp.setLongestKill( Double.valueOf(duofpp.get("longestKill").toString()) + Double.valueOf(solofpp.get("longestKill").toString()) + Double.valueOf(squadfpp.get("longestKill").toString()) );
        fpp.setHeadShotKill( Integer.valueOf(duofpp.get("headshotKills").toString()) + Integer.valueOf(solofpp.get("headshotKills").toString()) + Integer.valueOf(squadfpp.get("headshotKills").toString()) );
        fpp.setRankPoints(Double.valueOf(duofpp.get("rankPoints").toString()) + Double.valueOf(solofpp.get("rankPoints").toString()) + Double.valueOf(squadfpp.get("rankPoints").toString()) );
        fpp.setMaxKillStreaks( Integer.valueOf(duofpp.get("maxKillStreaks").toString()) + Integer.valueOf(solofpp.get("maxKillStreaks").toString()) + Integer.valueOf(squadfpp.get("maxKillStreaks").toString()) );

        double FPPKDA  = ( Integer.valueOf(duofpp.get("kills").toString()) + Integer.valueOf(duofpp.get("assists").toString()) )/ (Integer.valueOf(duofpp.get("losses").toString() )> 0 ? Integer.valueOf(duofpp.get("losses").toString()):1)
                + ( Integer.valueOf(solofpp.get("kills").toString()) + Integer.valueOf(solofpp.get("assists").toString()) )/ (Integer.valueOf(solofpp.get("losses").toString() )> 0 ? Integer.valueOf(solo.get("losses").toString()):1)
                + ( Integer.valueOf(squadfpp.get("kills").toString()) + Integer.valueOf(squadfpp.get("assists").toString()) )/ (Integer.valueOf(squadfpp.get("losses").toString() )> 0 ? Integer.valueOf(squadfpp.get("losses").toString()):1);

        fpp.setKDA( FPPKDA  );
        fpp.setRangDamage( (FPPDamage / (FPPPlayTimes>0?FPPPlayTimes : 1)) / 3 );
        fpp.setKD( FPPKill / (FPPDeth>0?FPPDeth:1) );

        String strJsonTpp = new JSONObject(tpp).toString();
        String strJsonFpp = new JSONObject(fpp).toString();

        String result = "{" + "\"TPP\":" + strJsonTpp + ","+ "\"FPP\":" + strJsonFpp + "}";
        System.out.println( result );
        return result;
    }

    private Map<String, String> GetPUBGGameDate(String pubgGameData, String pubgId, String matchId) throws ParseException {
        JSONObject json = new JSONObject(pubgGameData);
        if(json == null) {
            logger.info("GetPUBGGameDate json is null");
            return null;
        }
//        JSONObject rosters = json.getJSONObject("data").getJSONObject("relationships").getJSONObject("rosters");
//        if(rosters == null) {
//            System.out.println("rosters is null");
//            return null;
//        }
        JSONObject attributes = json.getJSONObject("data").getJSONObject("attributes");
        if( attributes == null) {
            logger.info("GetPUBGGameDate attributes is null");
            return null;
        }
        Map<String,String> result = new HashMap<>();
        String createAt = attributes.get("createdAt").toString();
        result.put("createdAt",createAt);

        PUBGMatchDataJson pubgMatchDataJson = null;
        JSONArray included = json.getJSONArray("included");
        if (included.size() > 0) {
            for (int i = 0; i < included.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject team = included.getJSONObject(i);

                String type = team.get("type").toString();
                String id = team.get("id").toString();
                if (type.equals("participant")) {  // 处理队员数据
                    JSONObject participant_attributes = team.getJSONObject("attributes");
                    JSONObject stats = participant_attributes.getJSONObject("stats");

                    if (pubgId.equals(stats.get("playerId").toString())) {
                        pubgMatchDataJson = new PUBGMatchDataJson();
                        pubgMatchDataJson.setDBNOs(Integer.valueOf(stats.get("DBNOs").toString()));
                        pubgMatchDataJson.setAssists(Integer.valueOf(stats.get("assists").toString()));
                        pubgMatchDataJson.setBoosts(Integer.valueOf(stats.get("boosts").toString()));
                        pubgMatchDataJson.setDamageDealt(Double.valueOf(stats.get("damageDealt").toString()));
                        pubgMatchDataJson.setDeathType(stats.get("deathType").toString());
                        pubgMatchDataJson.setHeadshotKills(Double.valueOf(stats.get("headshotKills").toString()));
                        pubgMatchDataJson.setHeals(Double.valueOf(stats.get("heals").toString()));
                        pubgMatchDataJson.setKillPlace(Double.valueOf(stats.get("killPlace").toString()));
                        pubgMatchDataJson.setKillStreaks(Double.valueOf(stats.get("killStreaks").toString()));
                        pubgMatchDataJson.setKills(Double.valueOf(stats.get("kills").toString()));
                        pubgMatchDataJson.setLongestKill(Double.valueOf(stats.get("longestKill").toString()));
                        pubgMatchDataJson.setName(stats.get("name").toString());
                        pubgMatchDataJson.setPlayerId(stats.get("playerId").toString());
                        pubgMatchDataJson.setRevives(Double.valueOf(stats.get("revives").toString()));
                        pubgMatchDataJson.setRideDistance(Double.valueOf(stats.get("rideDistance").toString()));
                        pubgMatchDataJson.setRoadKills(Double.valueOf(stats.get("roadKills").toString()));
                        pubgMatchDataJson.setSwimDistance(Double.valueOf(stats.get("swimDistance").toString()));
                        pubgMatchDataJson.setTeamKills(Double.valueOf(stats.get("teamKills").toString()));
                        pubgMatchDataJson.setTimeSurvived(Double.valueOf(stats.get("timeSurvived").toString()));
                        pubgMatchDataJson.setVehicleDestroys(Double.valueOf(stats.get("vehicleDestroys").toString()));
                        pubgMatchDataJson.setWalkDistance(Double.valueOf(stats.get("walkDistance").toString()));
                        pubgMatchDataJson.setWeaponsAcquired(Double.valueOf(stats.get("weaponsAcquired").toString()));
                        pubgMatchDataJson.setWinPlace(Integer.valueOf(stats.get("winPlace").toString()));
//                        pubgMatchDataJson.setPubgTeamID();
                        pubgMatchDataJson.setPubgUserID(id);

                    }
                }
            }
        }
        if (pubgMatchDataJson == null) {
            logger.info("GetPUBGGameDate pubgMatchDataJson is null");
            return null;
        }
        if (included.size() > 0) {
            for (int j = 0; j < included.size(); j++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject team = included.getJSONObject(j);

                String type = team.get("type").toString();
                String id = team.get("id").toString();
                if (type.equals("roster")) { // 处理队伍数据

                    JSONObject team_relationships = team.getJSONObject("relationships");
                    JSONObject participants = team_relationships.getJSONObject("participants");
                    JSONArray participants_data = participants.getJSONArray("data");
                    if (participants_data.size() > 0) {
                        for (int k = 0; k < participants_data.size(); k++) {
                            // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                            JSONObject job = participants_data.getJSONObject(k);
                            if(pubgMatchDataJson.getPubgUserID().equals(job.get("id").toString())) {
                                pubgMatchDataJson.setPubgTeamID(id);
                                String data = new JSONObject(pubgMatchDataJson).toString();
                                result.put("data",data);
                                return result;
                            }
                        }
                    }

                }
            }
        }

        return null;
    }

    public List<String> GetMatchIdList(String matchData)
    {
        List<String> matchIdList = new ArrayList<>();
        JSONObject json = new JSONObject(matchData);
        if(json == null) {
            logger.info("GetMatchIdList json is null");
            return null;
        }
        JSONArray matches = json.getJSONObject("data").getJSONObject("relationships").getJSONObject("matches").getJSONArray("data");
        if(matches == null) {
            logger.info("GetMatchIdList matches is null");
            return null;
        }

        if (matches.size() > 0) {
            for (int i = 0; i < matches.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject team = matches.getJSONObject(i);
                String type = team.get("type").toString();
                String id = team.get("id").toString();
                matchIdList.add(id);
            }
        }
        return matchIdList;
    }

    @Override
    public Boolean PullMathchData(String pubgId, String matchId) throws ParseException {
        if( pubgId==null || pubgId.equals("") || matchId==null || matchId.equals("") ) {
            return false;
        }
        PUBGMatchData pubgMatchData = pubgMatchDataRepository.findPUBGMatchDataByPubgIdAndMatchId(pubgId,matchId);
        if (pubgMatchData != null) {
            return false;
        }

        String matchData = GetMatchById(pubgId,matchId);
        Map<String,String> result = GetPUBGGameDate(matchData, pubgId,matchId);
        String createAt = result.get("createdAt");
        Date date = null;
        if(createAt==null||createAt.equals("")) {
            date = new Date();
        }
        else {
            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            mdyFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = mdyFormat.parse(createAt);
        }
        pubgMatchData = new PUBGMatchData();
        pubgMatchData.setPubgId(pubgId);
        pubgMatchData.setMatchId(matchId);
        pubgMatchData.setData(result.get("data"));
        pubgMatchData.setCreateAt(date);

        pubgMatchDataRepository.save(pubgMatchData);

        return true;
    }

}