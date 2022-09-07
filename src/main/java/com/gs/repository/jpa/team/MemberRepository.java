package com.gs.repository.jpa.team;

import com.gs.model.entity.jpa.db1.team.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户Dao层
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Member findMemberById(Long id);

    Member findMemberByPubgId(String pubgId);

    Member findMemberByPubgName(String pubgName);

    boolean existsById(Long id);

    boolean existsMemberEntityById(long id);

    List<Member> findAllByPubgIdContaining(String key);
}