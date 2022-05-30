package com.gs.repository.jpa.game;

import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PUBGMatchesRepository  extends JpaRepository<PUBGMatches, String>, JpaSpecificationExecutor<PUBGMatches> {
    PUBGMatches findPUBGMatchesByPubgMatchesId(String id);

    PUBGMatches findPUBGMatchesByDefMatchId(Long defMatchId);

    void  deletePUBGMatchesByPubgMatchesIdStartsWith(String id);
}
