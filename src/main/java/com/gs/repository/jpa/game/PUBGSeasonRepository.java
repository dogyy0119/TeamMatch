package com.gs.repository.jpa.game;

import com.gs.model.entity.jpa.db1.game.PUBGSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PUBGSeasonRepository extends JpaRepository<PUBGSeason, Long>, JpaSpecificationExecutor<PUBGSeason> {
    PUBGSeason findPUBGSeasonByName(String name);
}
