package com.gs.repository.jpa.game;

import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import com.gs.model.entity.jpa.db1.game.PUBGPlayer;
import com.gs.model.entity.jpa.db1.game.PUBGTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PUBGTeamRepository extends JpaRepository<PUBGTeam, String>, JpaSpecificationExecutor<PUBGTeam> {

    PUBGTeam findPUBGTeamByPubgTeamId(Long pubgTeamId);

    PUBGTeam findPUBGTeamByPubgMatchesIdAndTeamMembers(PUBGMatches pubgMatches, PUBGPlayer pubgPlayer);

    PUBGTeam findPUBGTeamByIndexAndAndPubgMatchesId(Integer index, String pubgMatchesId);

//    @Query(value = "SELECT top 1 B.pubgTeamId FROM t_pubg_team B where (B.pubgMatchesId =:vin) order by B.teamScore desc")
//    String findPUBGTeamScoreMost(@Param("vin")String vin);

//    @Query(value = "select teamId,SUM(teamScore) FROM t_pubg_team where (pubgMatchesId in (PUBGMatchesIdList)) ")
//    PUBGTeam findPUBGTeamCoreHigh( List<String> PUBGMatchesIdList);
}
