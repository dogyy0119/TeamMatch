package com.gs.service.impl.def;

import com.gs.convert.DefMatchConvert;
import com.gs.convert.DefMatchManageConvert;
import com.gs.convert.DefMatchOrderConvert;
import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.dto.vo.DefMatchManagerOrders;
import com.gs.model.dto.vo.DefMatchOrderTeamVO;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.repository.jpa.def.DefMatchManageRepository;
import com.gs.repository.jpa.def.DefMatchOrderRepository;
import com.gs.repository.jpa.def.DefMatchRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.def.DefMatchManageService;
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
public class DefMatchManageServiceImpl implements DefMatchManageService {

    @Autowired
    private DefMatchManageRepository defMatchManageRepository;

    @Autowired
    private DefMatchManageConvert defMatchManageConvert;

    @Autowired
    private DefMatchRepository defMatchRepository;

    @Autowired
    private DefMatchConvert defMatchConvert;

    @Autowired
    private DefMatchOrderRepository defMatchOrderRepository;

    @Autowired
    private DefMatchOrderConvert defMatchOrderConvert;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public DefMatchManageDTO findById(Long id) {
        Optional<DefMatchManage> optionalDefMatch = defMatchManageRepository.findById(id);
        return optionalDefMatch.isPresent() ? defMatchManageConvert.toDto(optionalDefMatch.get()) : null;
    }

    @Override
    public DefMatchManageDTO findByMatchId(Long id) {
        Optional<DefMatch> optionalDefMatch = defMatchRepository.findById(id);
        if( !optionalDefMatch.isPresent()) return null;
        DefMatch defMatch = optionalDefMatch.get();

        DefMatchManage defMatchManage= defMatchManageRepository.findDefMatchManageByDefMatch( defMatch );


        return defMatchManageConvert.toDto(defMatchManage);
    }

    @Override
    public DefMatchManageDTO create(DefMatchManageDTO dto) {
//        DefMatchManageDTO defMatchManageDTO = findById( dto.getId() );
//        if (defMatchManageDTO != null) return defMatchManageDTO;
        defMatchManageRepository.save(defMatchManageConvert.toEntity(dto));
        return dto;
    }

    @Override
    public void update(DefMatchManageDTO dto) {
        defMatchManageRepository.save(defMatchManageConvert.toEntity(dto));
    }

    @Override
    public void delete(Long id) {
        Optional<DefMatchManage> optionalNews = defMatchManageRepository.findById(id);
        if(optionalNews.isPresent()){
            defMatchManageRepository.deleteById(id);
        }
    }

    @Override
    public DefMatchManagerOrders getMatchManagesPageByMatch(Long memberId, Long matchId, Integer pageNum, Integer pageSize) {

        DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByMemberIdAndDefMatchId(memberId, matchId);
        System.out.println( "defMatchManage:" + defMatchManage.getId() );
        if(defMatchManage == null) { return  null; }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatchOrder> ordersPage = defMatchOrderRepository.findAll(new Specification<DefMatchOrder>() {

            public Predicate toPredicate(Root<DefMatchOrder> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> defMatchManageIdPath = root.join("defMatchManage").get("id");
//                Path<Integer> statusPath = root.get("status");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal( defMatchManageIdPath, defMatchManage.getId()));
//                predicates.add(cb.equal( statusPath, 1));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<DefMatchOrderTeamVO> defMatchOrderTeamVOS = new ArrayList<>();

        for (DefMatchOrder entry : ordersPage) {
            DefMatchOrderDTO defMatchOrderDTO = defMatchOrderConvert.toDto(entry);

            Team team = teamRepository.findTeamById(defMatchOrderDTO.getOrderId());

            DefMatchOrderTeamVO defMatchOrderTeamVO = new DefMatchOrderTeamVO( defMatchOrderDTO );
            defMatchOrderTeamVO.setId( team.getId() );
            //defMatchOrderTeamVO.setTeamId( team.getTeamId() );
            defMatchOrderTeamVO.setTeamName( team.getName() );

            defMatchOrderTeamVOS.add( defMatchOrderTeamVO );
        }

        DefMatchManagerOrders defMatchManagerOrders = new DefMatchManagerOrders( defMatchManageConvert.toDto(defMatchManage) );
        defMatchManagerOrders.setDefMatchOrderDTOS( defMatchOrderTeamVOS );
        return defMatchManagerOrders;
    }

}
