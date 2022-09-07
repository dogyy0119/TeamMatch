package com.gs.repository.jpa.game;

import com.gs.model.entity.jpa.db1.game.PUBGMatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PUBGMatchDataRepository extends JpaRepository<PUBGMatchData, Long>, JpaSpecificationExecutor<PUBGMatchData> {
    PUBGMatchData findPUBGMatchDataByPubgIdAndMatchId(String pubgId, String matchId);

    List<PUBGMatchData> findPUBGMatchDataByPubgId(String pubgId);
}