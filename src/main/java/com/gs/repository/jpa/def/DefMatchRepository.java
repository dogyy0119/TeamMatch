package com.gs.repository.jpa.def;

import com.gs.model.entity.jpa.db1.def.DefMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DefMatchRepository extends JpaRepository<DefMatch, Long>, JpaSpecificationExecutor<DefMatch> {

    DefMatch findDefMatchById(Long id);
}
