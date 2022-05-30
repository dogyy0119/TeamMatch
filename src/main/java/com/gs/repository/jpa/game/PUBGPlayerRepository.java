package com.gs.repository.jpa.game;

import com.gs.model.entity.jpa.db1.game.PUBGPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PUBGPlayerRepository extends JpaRepository<PUBGPlayer, Long>, JpaSpecificationExecutor<PUBGPlayer> {
    List<PUBGPlayer> findPUBGPlayerByPubgPlayerId(String pubgPlayerId);

    List<PUBGPlayer> findAllPUBGPlayerByPubgPlayerName(String pubgPlayerName);
}
