package com.gs.repository.jpa.game;

import com.gs.model.entity.jpa.db1.game.PUBGAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PUBGAchievementRepository extends JpaRepository<PUBGAchievement, Long>, JpaSpecificationExecutor<PUBGAchievement> {

    List<PUBGAchievement> findPUBGAchievementByTeamId(Long teamId);

    PUBGAchievement findPUBGAchievementByDefMatchIdAndAndDefMatchIndex(Long defMatchID, Integer defMatchIndex);
}
