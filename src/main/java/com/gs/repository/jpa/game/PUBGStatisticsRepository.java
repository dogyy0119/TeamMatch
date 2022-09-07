package com.gs.repository.jpa.game;

import com.gs.model.entity.jpa.db1.game.PUBGStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PUBGStatisticsRepository extends JpaRepository<PUBGStatistics, Long>, JpaSpecificationExecutor<PUBGStatistics> {
    PUBGStatistics findPUBGStatisticsByMemberId(Long memberId);
}