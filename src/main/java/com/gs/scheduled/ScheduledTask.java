package com.gs.scheduled;

import com.alibaba.druid.sql.visitor.functions.Now;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *  ScheduledTask 配置类。
 */
@Component
@Async
public class ScheduledTask {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private static Date GameBefore(Integer minites){
        long time = minites*60*1000;//30分钟
        Date now = new Date();
        Date beforeDate = new Date(now.getTime() - time);//minites分钟之前的时间
        return beforeDate;
    }

    private static Date GameAfter(Integer minites){
        long time = minites*60*1000;//30分钟
        Date now = new Date();
        Date afterDate = new Date(now.getTime() + time);//minites分钟之后的时间
        return afterDate;
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void scheduled(){


//        Date date = new Date();
//        date.getTime();
////        date.
//        logger.info("使用cron  线程名称：{}",Thread.currentThread().getName()  );
//        logger.info( date.toString() );
//        logger.info( String.valueOf( date.getTime() ));

    }

//    @Scheduled(fixedRate = 5000)
//    public void scheduled1() {
//        logger.info("fixedRate--- 线程名称：{}",Thread.currentThread().getName());
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void scheduled2() {
//        logger.info("fixedDelay  线程名称：{}",Thread.currentThread().getName());
//    }
}
