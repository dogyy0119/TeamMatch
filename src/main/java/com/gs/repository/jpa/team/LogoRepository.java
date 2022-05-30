package com.gs.repository.jpa.team;

import com.gs.model.entity.jpa.db1.team.Logo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LogoRepository extends JpaRepository<Logo, Long>, JpaSpecificationExecutor<Logo> {

    Logo findLogoById(Long id);
    List<Logo> findAllByMemberId(Long memberId);
}
