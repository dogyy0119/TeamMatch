package com.gs.service.impl.def;

import com.gs.convert.DefMatchConvert;
import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.repository.jpa.def.DefMatchRepository;
import com.gs.service.intf.def.DefMatchService;
import com.gs.service.intf.team.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DefMatchServiceImpl implements DefMatchService {

    @Autowired
    private DefMatchRepository defMatchRepository;

    @Autowired
    private DefMatchConvert defMatchConvert;

    @Autowired
    private MemberService memberService;

    @Override
    public DefMatchDTO findById(Long id) {
        Optional<DefMatch> optionalDefMatch = defMatchRepository.findById(id);
        return optionalDefMatch.isPresent() ? defMatchConvert.toDto(optionalDefMatch.get()) : null;
    }

    @Override
    public DefMatchDTO create(DefMatchDTO dto) {
        DefMatch defMatch = defMatchRepository.save(defMatchConvert.toEntity(dto));
        return defMatchConvert.toDto( defMatch );
    }

    @Override
    public void update(DefMatchDTO dto) {
        defMatchRepository.save(defMatchConvert.toEntity(dto));
    }

    @Override
    public void delete(Long id) {
        Optional<DefMatch> optionalNews = defMatchRepository.findById(id);
        if(optionalNews.isPresent()){
            defMatchRepository.deleteById(id);
        }
    }


    @Override
    public Page<DefMatch> findMatchesByMember(Long memberId, Pageable pageable) {
        Page<DefMatch> pages = defMatchRepository.findAll(new Specification<DefMatch>() {

            public Predicate toPredicate(Root<DefMatch> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> memberPath = root.join("member").get("id");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(memberPath, memberId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        return pages;
    }

    @Override
    public Page<DefMatch> findMatchesByMatchType(String matchType, Pageable pageable) {
        Page<DefMatch> pages = defMatchRepository.findAll(new Specification<DefMatch>() {

            public Predicate toPredicate(Root<DefMatch> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> matchTypePath = root.get("matchType");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(matchTypePath, matchType));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);
        return pages;
    }

    @Override
    public Page<DefMatch> findMatchesByDate(Date date, Pageable pageable) {
        Page<DefMatch> pages = defMatchRepository.findAll(new Specification<DefMatch>() {

            public Predicate toPredicate(Root<DefMatch> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Date> datePath = root.get("gameStartTime");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(datePath, date));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);
        return pages;
    }

    @Override
    public Page<DefMatch> findMatchesByGameMode(int gameMode, Pageable pageable) {
        Page<DefMatch> pages = defMatchRepository.findAll(new Specification<DefMatch>() {

            public Predicate toPredicate(Root<DefMatch> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Integer> gameModePath = root.get("gameMode");
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(gameModePath, gameMode));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);

        return pages;
    }

    @Override
    public List<DefMatchDTO> getManageMatchesPage(Long memberId, Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<DefMatch> ordersPage = defMatchRepository.findAll(new Specification<DefMatch>() {
            public Predicate toPredicate(Root<DefMatch> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> memberIdPath = root.join("member").get("id");
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal( memberIdPath, memberId));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<DefMatchDTO> defMatchDTOSList = new ArrayList<>();
        for (DefMatch entry : ordersPage) {
            defMatchDTOSList.add( defMatchConvert.toDto(entry) );
        }

        return defMatchDTOSList;
    }

    /**
     *
     * @param littleTime
     * @param bigTime
     * @return
     */
    @Override
    public List<DefMatchDTO> getMatchesByTime(Date littleTime, Date bigTime) {

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<DefMatch> ordersPage = defMatchRepository.findAll(new Specification<DefMatch>() {

            public Predicate toPredicate(Root<DefMatch> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Date> gameStartTimePath = root.get("gameStartTime");
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.greaterThanOrEqualTo( gameStartTimePath, littleTime));
                predicates.add(cb.lessThanOrEqualTo( gameStartTimePath, bigTime));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<DefMatchDTO> defMatchSList = new ArrayList<>();
        for (DefMatch entry : ordersPage) {
            System.out.println("entry:" + entry.getGameStartTime() );
            defMatchSList.add( defMatchConvert.toDto(entry)  );
        }
        return defMatchSList;
    }


    /**
     *
     * @param key
     * @return
     */
    @Override
    public List<DefMatchDTO> getMatchByKey(String key) {

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<DefMatch> ordersPage = defMatchRepository.findAll(new Specification<DefMatch>() {
            public Predicate toPredicate(Root<DefMatch> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String>  namePath= root.get("name");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.like(namePath, key));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<DefMatchDTO> defMatchSList = new ArrayList<>();
        for (DefMatch entry : ordersPage) {
            defMatchSList.add( defMatchConvert.toDto(entry)  );
        }

        return defMatchSList;

    }

}
