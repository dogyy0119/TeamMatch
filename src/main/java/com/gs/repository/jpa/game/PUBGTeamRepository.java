package com.gs.repository.jpa.game;

import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import com.gs.model.entity.jpa.db1.game.PUBGPlayer;
import com.gs.model.entity.jpa.db1.game.PUBGTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PUBGTeamRepository extends JpaRepository<PUBGTeam, String>, JpaSpecificationExecutor<PUBGTeam> {
    PUBGTeam findPUBGTeamByPubgMatchesIdAndTeamMembers(PUBGMatches pubgMatches, PUBGPlayer pubgPlayer);
}
