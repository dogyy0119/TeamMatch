package com.gs.repository.jpa.league;

import com.gs.model.entity.jpa.db1.league.LeagueMessage;
import com.gs.model.entity.jpa.db1.team.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface LeagueMessageRepository extends JpaRepository<LeagueMessage, Long>, JpaSpecificationExecutor<LeagueMessage> {
    void deleteAllByLeagueId(Long leagueId);
    Page<LeagueMessage> findAllByLeagueId(Long leagueId, Specification<LeagueMessage> specification, Pageable pageable);
    List<LeagueMessage> findMessagesByLeagueId(Long leagueId);

    void deleteAllByCreateTimeBefore(Date date);
    LeagueMessage findMessageById(Long messageId);
}
