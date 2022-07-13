package com.gs.scheduled;

import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.service.intf.def.DefMatchService;
import com.gs.service.intf.game.PUBGMatchesService;
import com.gs.utils.ScoreUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  ScheduledTask 配置类。
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

    private static Date GameBefore(Integer minites){
        long time = minites*60*1000;//30分钟
        Date now = new Date();
        Date beforeDate = new Date(now.getTime() + time);//minites分钟之前的时间
        return beforeDate;
    }

    private static Date GameAfter(Integer minites){
        long time = minites*60*1000;//30分钟
        Date now = new Date();
        Date afterDate = new Date(now.getTime() - time);//minites分钟之后的时间
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
     * 提前30分钟 通知比赛
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @Async
    public void gamePrepare(){

        List<DefMatchDTO> defMatchDTOS = defMatchService.getMatchesByTime(GameBefore(30),new Date());
        try {
            lock.lock();
            for(DefMatchDTO defMatchDTO: defMatchDTOS) {
                if (prepareMap.get(defMatchDTO.getId()) == null) {
                    prepareMap.put(defMatchDTO.getId(), defMatchDTO);
                    logger.info("添加 defMatch id", defMatchDTO.getId());

                    Long memberId = defMatchDTO.getMemberId();
                    Member member = memberRepository.findMemberById(memberId);
                    String pubgName = member.getPubgName();

                    List<String> matchIds = pubgMatchesService.getPlayerMatches(pubgName);
                    matchIdMap.put(defMatchDTO.getId(), matchIds);
                }
            }
        } finally {
            lock.unlock();
        }
        logger.info("使用cron  线程名称：{}",Thread.currentThread().getName()  );
    }


    /**
     * 在 start Time之后。获取 PUBG 比赛。
     */
    @Scheduled(cron = "0 */15 * * * ?")
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
                if (date.after( GameAfter(30))) {
                    List<String> ids = matchIdMap.get(defMatchId);
                    Long memberId = defMatchDTO.getMemberId();
                    Member member = memberRepository.findMemberById(memberId);
                    String pubgName = member.getPubgName();
                    List<String> matchIds = pubgMatchesService.getPlayerMatches(pubgName);

                    matchIds.removeAll(ids);
                    if ( matchIds.size() > 0 ) {
                        for (String id : matchIds) {
                            ids.add( id );
                            List<PUBGMatches> pubgMatchesList = pubgMatchesService.findPUBGMatchesByDefMatchId(defMatchId);
                            pubgMatchesService.getPUBGMatches(id ,defMatchDTO.getId(),pubgMatchesList.size()+1, ScoreUtils.perseRank(defMatchDTO.getGameRankItems()), ScoreUtils.perseKill(defMatchDTO.getGameKillItems()));
                            if(pubgMatchesList.size()+1 == defMatchDTO.getGameNum()) {
                                prepareMap.remove(defMatchId);
                                matchIdMap.remove(matchIdMap);
                            }
                        }
                        matchIdMap.put(defMatchId, ids);
                    }
                }

            }
        } finally {
            lock.unlock();
        }
    }
}
