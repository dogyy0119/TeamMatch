package com.gs.service.impl.def;

import com.gs.convert.TeamOrderConvert;
import com.gs.model.dto.def.TeamOrderDTO;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.def.TeamOrder;
import com.gs.repository.jpa.def.DefMatchOrderRepository;
import com.gs.repository.jpa.def.TeamOrderRepository;
import com.gs.service.intf.def.TeamOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamOrderServiceImpl implements TeamOrderService {

    @Autowired
    private TeamOrderRepository teamOrderRepository;

    @Autowired
    private TeamOrderConvert teamOrderConvert;

    @Autowired
    private DefMatchOrderRepository defMatchOrderRepository;

    @Override
    public TeamOrderDTO findById(Long id) {
        Optional<TeamOrder> teamOrderOptional = teamOrderRepository.findById(id);

        return teamOrderOptional.isPresent() ? teamOrderConvert.toDto( teamOrderOptional.get() ): null;
    }

    @Override
    public TeamOrderDTO create(TeamOrderDTO dto) {
        TeamOrder  teamOrder =  teamOrderRepository.save(teamOrderConvert.toEntity(dto));
        return teamOrderConvert.toDto( teamOrder );
    }

    @Override
    public void update(TeamOrderDTO dto) {
        teamOrderRepository.save(teamOrderConvert.toEntity(dto));
    }

    @Override
    public void delete(Long id) {
        Optional<TeamOrder> optionalNews = teamOrderRepository.findById(id);
        if(optionalNews.isPresent()){
            teamOrderRepository.deleteById(id);
        }
    }

    @Override
    public Page<TeamOrderDTO> findTeamOrderByMember(Long memberId, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public Page<TeamOrderDTO> findTeamOrderByTeamId(Long teamId, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public List<TeamOrderDTO> findTeamOrderByDefMatchOrderIdAndStatus(Long defMatchOrderId, Integer status, Integer pageNum, Integer pageSize) {

        DefMatchOrder defMatchOrder = defMatchOrderRepository.findDefMatchOrderById(defMatchOrderId);

        if( defMatchOrder == null) return null;

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<TeamOrder> ordersPage = teamOrderRepository.findAll(new Specification<TeamOrder>() {

            public Predicate toPredicate(Root<TeamOrder> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> defMatchOrderIdPath = root.join("defMatchOrder").get("id");
                Path<Integer> statusPath = root.get("status");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal( defMatchOrderIdPath, defMatchOrderId));
                predicates.add(cb.equal( statusPath, status));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<TeamOrderDTO> teamOrderDTOS = new ArrayList<>();

        for (TeamOrder entry : ordersPage) {
            teamOrderDTOS.add( teamOrderConvert.toDto(entry) );
        }

        return teamOrderDTOS;
    }

    @Override
    public List<TeamOrderDTO> findTeamOrderByDefMatchOrderId(Long defMatchOrderId, Integer pageNum, Integer pageSize) {
        DefMatchOrder defMatchOrder = defMatchOrderRepository.findDefMatchOrderById(defMatchOrderId);

        if( defMatchOrder == null) return null;

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<TeamOrder> ordersPage = teamOrderRepository.findAll(new Specification<TeamOrder>() {

            public Predicate toPredicate(Root<TeamOrder> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> defMatchOrderIdPath = root.join("defMatchOrder").get("id");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal( defMatchOrderIdPath, defMatchOrderId));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<TeamOrderDTO> teamOrderDTOS = new ArrayList<>();

        for (TeamOrder entry : ordersPage) {
            teamOrderDTOS.add( teamOrderConvert.toDto(entry) );
        }

        return teamOrderDTOS;
    }
}
