package com.gs.repository.jpa.team;

import com.gs.model.entity.jpa.db1.team.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    void deleteAllByTeamId(Long teamId);
    Page<Message> findAllByTeamId(Long teamId, Specification<Message> specification, Pageable pageable);
    List<Message> findMessagesByTeamId(Long teamId);

    void deleteAllByCreateTimeBefore(Date date);
    Message findMessageById(Long messageId);
}
