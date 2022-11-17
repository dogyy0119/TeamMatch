package com.gs.repository.jpa.team;

import com.gs.model.entity.jpa.db1.team.MemberRequest;
import com.gs.model.entity.jpa.db1.team.TeamRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MemberRequestRepository extends JpaRepository<MemberRequest, Long>, JpaSpecificationExecutor<MemberRequest> {

    boolean existsByTeamIdAndToIdAndTypeAndStatus(Long teamId, Long toId, Integer type, Integer status);

    Page<MemberRequest> findAllByToId(Long toId, Pageable pageable);

    List<MemberRequest> findAllByToId(Long toId);

    void deleteAllByToId(Long toId);

    void deleteAllByTeamId(Long teamId);
    List<MemberRequest> findMemberRequestsByTeamId(Long teamId);
    MemberRequest findMemberRequestById(Long id);
}
