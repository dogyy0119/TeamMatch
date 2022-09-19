package com.gs.scheduled;

import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import com.gs.model.entity.jpa.db1.game.PUBGSeason;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.service.intf.def.DefMatchService;
import com.gs.service.intf.game.PUBGMatchesService;
import com.gs.service.intf.game.PUBGStatisticsService;
import com.gs.utils.ScoreUtils;
import org.apache.tomcat.jni.Time;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ScheduledTask 配置类。
 */
@Component
@Async
public class ScheduledTask {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    static Map<Long, DefMatchDTO> prepareMap = new HashMap<>();

    static Map<Long, List<String>> matchIdMap = new HashMap<>();

    private static Lock lock = new ReentrantLock();


    @Autowired
    private DefMatchService defMatchService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PUBGMatchesService pubgMatchesService;

    @Autowired
    private PUBGStatisticsService pubgStatisticsService;


    private static Date GameBefore(Integer minites) {
        long time = minites * 60 * 1000;//30分钟
        Date now = new Date();
        Date beforeDate = new Date(now.getTime() - time);//minites分钟之前的时间
        return beforeDate;
    }

    private static Date GameAfter(Integer minites) {
        long time = minites * 60 * 1000;//30分钟
        Date now = new Date();
        Date afterDate = new Date(now.getTime() + time);//minites分钟之后的时间
        return afterDate;
    }

    public static List<Long> getMatchIdOfGameRunning() {
        List<Long> longList = new ArrayList<>();
        try {
            lock.lock();
            for (Long key : prepareMap.keySet()) {
                longList.add(key);
            }

        } finally {
            lock.unlock();
        }
        return longList;
    }

    /**
     * 每天定时 4点开始
     */
    @Scheduled(cron = "0 0 12 * * ?")
    @Async
    public void PullPUBGMatchData() throws InterruptedException, ParseException {
        List<Member> memberList = memberRepository.findAllByPubgIdContaining("account.");

        for (Member member : memberList) {
            if (member.getId().equals(44L)) {
                logger.info(" member id: {}", member.getId());
                logger.info(" member getPubgId: {}", member.getPubgId());
                if(member.getPubgId() == null || member.getPubgId().equals(""))
                    continue;
                List<String> pubgMatchIdList = pubgStatisticsService.GetPlayerById(member.getPubgId());
                if (pubgMatchIdList.size() > 0) {
                    for (String matchId : pubgMatchIdList) {
                        Boolean update = pubgStatisticsService.PullMathchData(member.getPubgId(), matchId);
                        if (update) {
                            Thread.sleep(10 * 1000);
                        }
                    }
                }
            }
        }

    }

    /**
     * 每天定时 12点开始
     */
    @Scheduled(cron = "0 35  4 * * ?")
    @Async
    public void PullPUBGData() throws InterruptedException {
        List<Member> memberList = memberRepository.findAllByPubgIdContaining("account.");
        System.out.println(memberList.size());
        System.out.println(memberList.get(0).getId());
        PUBGSeason curSeason = pubgStatisticsService.GetCurSeason();
//        PUBGSeason lastSeason = pubgStatisticsService.GetLastSeason();
        for (Member member : memberList) {
            logger.info(" member id: {}", member.getId());
            logger.info(" member getPubgId: {}", member.getPubgId());
            Boolean update = pubgStatisticsService.UpdateAll(member.getId(), member.getPubgId(), curSeason.getName(), null);
            if (update) {
                Thread.sleep(30 * 1000);
            }
        }

    }

    /**
     * 提前30分钟 通知比赛
     */
    @Scheduled(cron = "0 */2 * * * ?")
    @Async
    public void gamePrepare() {

        List<DefMatchDTO> defMatchDTOS = defMatchService.getMatchesByTime(new Date(), GameAfter(30));
        try {
            lock.lock();
            for (DefMatchDTO defMatchDTO : defMatchDTOS) {
                if (prepareMap.get(defMatchDTO.getId()) == null) {
                    prepareMap.put(defMatchDTO.getId(), defMatchDTO);
                    logger.info("添加 defMatch id", defMatchDTO.getId());

                    Long memberId = defMatchDTO.getMemberId();
                    Member member = memberRepository.findMemberById(memberId);
                    String pubgName = member.getPubgName();

                    logger.info("member: {}", member.getId());

                    logger.info("pubgName: {}", pubgName);

                    List<String> matchIds = pubgMatchesService.getPlayerMatches(pubgName);

                    logger.info("matchIds size: {}", matchIds.size());

                    for (String matchid : matchIds) {
                        logger.info("matchid ： {}", matchid);
                    }

                    matchIdMap.put(defMatchDTO.getId(), matchIds);
                }
            }
        } finally {
            lock.unlock();
        }
//        logger.info("使用cron  线程名称：{}",Thread.currentThread().getName()  + new Date() );
    }


    /**
     * 在 start Time之后。获取 PUBG 比赛。
     */
    @Scheduled(cron = "0 */10 * * * ?")
    @Async
    public void gameData() {
        Iterator<Map.Entry<Long, DefMatchDTO>> iterator = prepareMap.entrySet().iterator();
        try {
            lock.lock();
            while (iterator.hasNext()) {
                Map.Entry<Long, DefMatchDTO> entry = iterator.next();
                Long defMatchId = entry.getKey();
                DefMatchDTO defMatchDTO = entry.getValue();
                Date date = defMatchDTO.getGameStartTime();

                logger.info("date ： {}", date.toString());

                if (date.before(GameBefore(24 * 60))) {
                    logger.info(" %ld  is not get all match data, please check.  ", defMatchId);
                    prepareMap.remove(defMatchId);
                    matchIdMap.remove(defMatchId);

                }

                if (date.before(GameBefore(30))) {
                    List<String> ids = matchIdMap.get(defMatchId);
                    Long memberId = defMatchDTO.getMemberId();
                    Member member = memberRepository.findMemberById(memberId);
                    String pubgName = member.getPubgName();
                    List<String> matchIds = pubgMatchesService.getPlayerMatches(pubgName);

                    for (String matchid : matchIds) {
                        logger.info("2  matchid ： {}", matchid);
                    }

                    matchIds.removeAll(ids);

                    logger.info("2  matchIds.size() ： {}", matchIds.size());
                    if (matchIds.size() > 0) {
                        for (String id : matchIds) {
                            ids.add(id);
                            logger.info("id ： {}", id);
                            List<PUBGMatches> pubgMatchesList = pubgMatchesService.findPUBGMatchesByDefMatchId(defMatchId);
                            logger.info("pubgMatchesList.size ： {}", pubgMatchesList.size());
                            logger.info("defMatchDTO.getId() ： {}", defMatchDTO.getId());
                            pubgMatchesService.getPUBGMatches(id, defMatchDTO.getId(), pubgMatchesList.size() + 1, ScoreUtils.perseRank(defMatchDTO.getGameRankItems()), ScoreUtils.perseKill(defMatchDTO.getGameKillItems()));

                            if (pubgMatchesList.size() + 1 == defMatchDTO.getGameNum()) {
                                prepareMap.remove(defMatchId);
                                matchIdMap.remove(defMatchId);
                                pubgMatchesService.findTopScoreTeamByDefMatchId(defMatchId);
                            } else {
                                matchIdMap.put(defMatchId, ids);
                            }
                        }

                    }
                }
            }
        } finally {
            lock.unlock();
        }
//        logger.info("222222 使用cron  线程名称：{}",Thread.currentThread().getName()  + new Date() );
    }
}
