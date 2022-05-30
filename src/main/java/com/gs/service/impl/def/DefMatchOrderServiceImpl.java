package com.gs.service.impl.def;

import com.gs.convert.DefMatchConvert;
import com.gs.convert.DefMatchOrderConvert;
import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.dto.def.DefMatchOrderDTO;
import com.gs.model.dto.def.DefMatchOrderQuery;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.def.PersonOrder;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.repository.jpa.def.DefMatchManageRepository;
import com.gs.repository.jpa.def.DefMatchOrderRepository;
import com.gs.repository.jpa.def.DefMatchRepository;
import com.gs.repository.jpa.def.PersonOrderRepository;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.service.intf.def.DefMatchOrderService;
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
public class DefMatchOrderServiceImpl implements DefMatchOrderService {

    @Autowired
    private DefMatchOrderRepository defMatchOrderRepository;

    @Autowired
    private DefMatchOrderConvert defMatchOrderConvert;

    @Autowired
    private DefMatchManageRepository defMatchManageRepository;

    @Autowired
    private DefMatchRepository defMatchRepository;

    @Autowired
    private DefMatchConvert defMatchConvert;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PersonOrderRepository personOrderRepository;

    @Override
    public DefMatchOrderDTO findById(Long id) {
        Optional<DefMatchOrder> optionalDefMatch = defMatchOrderRepository.findById(id);
        return optionalDefMatch.isPresent() ? defMatchOrderConvert.toDto(optionalDefMatch.get()) : null;
    }

    @Override
    public DefMatchOrderDTO findByMatchIdAndOrderId(Long matchId, Long orderId) {
        DefMatch defMatch = defMatchRepository.findById( matchId ).get();
        if (defMatch == null) return null;
        DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatch);

        DefMatchOrder entity =defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage,orderId);

        return defMatchOrderConvert.toDto(entity);
    }

    @Override
    public DefMatchOrderDTO create(DefMatchOrderDTO dto) {
        if(defMatchRepository.findById(dto.getMatchId()).get() == null) return null;

        DefMatchOrder entity = defMatchOrderConvert.toEntity(dto);
        DefMatchOrder defMatchOrder = defMatchOrderRepository.save(entity);

        //创建个人点赞
        if(entity.getMode() == 0) {
            Member member = memberRepository.findMemberById(entity.getOrderId());
            PersonOrder personOrder = new PersonOrder();
            personOrder.setDefMatchOrder(entity);
            personOrder.setMember(member);
            personOrder.setIsLike(0);
            personOrderRepository.save(personOrder);
        }
        return defMatchOrderConvert.toDto(defMatchOrder);
    }

    @Override
    public void update(DefMatchOrderDTO dto) {
        Optional<DefMatch> defMatchOptional = defMatchRepository.findById(dto.getMatchId());
        if ( defMatchOptional == null ) return;
        DefMatch defMatch = defMatchOptional.get();
        if (defMatch == null) return ;
        DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatch);

        DefMatchOrder entity =defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, dto.getOrderId());

        entity.setStatus(dto.getStatus());

        defMatchOrderRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        Optional<DefMatchOrder> optionalNews = defMatchOrderRepository.findById(id);
        if(optionalNews.isPresent()){
            defMatchOrderRepository.deleteById(id);
        }
    }

    @Override
    public List<DefMatchOrderDTO> getMatchOrderPage(DefMatchOrderQuery defMatchOrderQuery, Integer pageNum, Integer pageSize) {

        Optional<DefMatch> defMatchOptional = defMatchRepository.findById(defMatchOrderQuery.getMatchId());
        if ( !defMatchOptional.isPresent() ) return null;
        DefMatch defMatch = defMatchOptional.get();

        if (defMatch == null) return null;
        DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatch);

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatchOrder> ordersPage = defMatchOrderRepository.findAll(new Specification<DefMatchOrder>() {

            public Predicate toPredicate(Root<DefMatchOrder> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> manageId = root.get("defMatchManage").get("id");
                Path<Integer> status = root.get("status");

                System.out.println( "manage id:" + manageId );
                System.out.println( "status:" + status );
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                // 检查matchId
//                if(manageId != null){
                predicates.add(cb.equal(manageId, defMatchManage.getId()));
//                }
                // 检查status
                predicates.add(cb.equal(status, defMatchOrderQuery.getStatus()));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<DefMatchOrderDTO> defMatchOrderDTOSList = new ArrayList<>();

        System.out.println( "ordersPage list size :" + ordersPage.getTotalPages() );

        for (DefMatchOrder entry : ordersPage) {
            System.out.println( "DefMatchOrder  :" + entry.getOrderId() );
            defMatchOrderDTOSList.add(defMatchOrderConvert.toDto(entry));
        }

        System.out.println( "defMatchOrderDTOSList list size :" + defMatchOrderDTOSList.size() );

        return defMatchOrderDTOSList;
    }

    @Override
    public List<DefMatchDTO> getMatchPage(Integer mode, Long orderId, Integer status, Integer pageNum, Integer pageSize) {
        if( mode == 1 ) // 战队
        {
            // 验证 member id 是否存在
            boolean exists = memberRepository.existsById(orderId);
            if( !exists ) return null;

        } else if ( mode == 0 ) //个人报名
        {
            //验证 teamId 是否存在
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatchOrder> ordersPage = defMatchOrderRepository.findAll(new Specification<DefMatchOrder>() {

            public Predicate toPredicate(Root<DefMatchOrder> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> orderIdPath = root.get("orderId");
                Path<Integer> statusPath = root.get("status");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal( orderIdPath, orderId));
                predicates.add(cb.equal( statusPath, status));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<DefMatchDTO> defMatchDTOSList = new ArrayList<>();

        for (DefMatchOrder entry : ordersPage) {
            System.out.println( "DefMatchOrder  :" + entry.getOrderId() );
            DefMatch defMatch =  entry.getDefMatchManage().getDefMatch();

            defMatchDTOSList.add( defMatchConvert.toDto(defMatch) );
        }

        return defMatchDTOSList;
    }

    @Override
    public List<DefMatchOrderDTO> getMatchOrdersPageByMatchId(Long memberId, Long matchId, Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatchOrder> ordersPage = defMatchOrderRepository.findAll(new Specification<DefMatchOrder>() {

            public Predicate toPredicate(Root<DefMatchOrder> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> memberIdPath = root.join("defMatchManage").join("member").get("id");
                Path<Long> matchIdPath = root.join("defMatchManage").join("defMatch").get("id");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal( memberIdPath, memberId));
                predicates.add(cb.equal( matchIdPath, matchId));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<DefMatchOrderDTO> defMatchOrderDTOS = new ArrayList<>();

        for (DefMatchOrder entry : ordersPage) {
            defMatchOrderDTOS.add( defMatchOrderConvert.toDto(entry) );
        }

        return defMatchOrderDTOS;
    }

    @Override
    public List<DefMatchOrderDTO> getMatchOrdersPageByMatchIdAndStatus(Long memberId, Long matchId, Integer status, Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatchOrder> ordersPage = defMatchOrderRepository.findAll(new Specification<DefMatchOrder>() {

            public Predicate toPredicate(Root<DefMatchOrder> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> memberIdPath = root.join("defMatchManage").join("member").get("id");
                Path<Long> matchIdPath = root.join("defMatchManage").join("defMatch").get("id");
                Path<Integer> statusPath = root.get("status");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal( memberIdPath, memberId));
                predicates.add(cb.equal( matchIdPath, matchId));
                predicates.add(cb.equal( statusPath, status));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<DefMatchOrderDTO> defMatchOrderDTOS = new ArrayList<>();

        for (DefMatchOrder entry : ordersPage) {
            defMatchOrderDTOS.add( defMatchOrderConvert.toDto(entry) );
        }

        return defMatchOrderDTOS;
    }

}
