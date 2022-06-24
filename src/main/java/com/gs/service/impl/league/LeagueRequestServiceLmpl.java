package com.gs.service.impl.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.constant.enums.MemberJobEnum;
import com.gs.convert.league.LeagueRequestDtoConvert;
import com.gs.convert.league.LeagueRequestVoConvert;
import com.gs.convert.team.TeamRequestDtoConvert;
import com.gs.convert.team.TeamRequestVoConvert;
import com.gs.model.dto.league.LeagueRequestDTO;
import com.gs.model.dto.team.TeamRequestDTO;
import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.entity.jpa.db1.league.LeagueRequest;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.entity.jpa.db1.team.TeamRequest;
import com.gs.model.vo.league.LeagueRequestVo;
import com.gs.model.vo.team.TeamRequestVo;
import com.gs.repository.jpa.league.LeagueRepository;
import com.gs.repository.jpa.league.LeagueRequestRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.repository.jpa.team.TeamRequestRepository;
import com.gs.service.intf.league.LeagueRequestService;
import com.gs.service.intf.team.TeamRequestService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LeagueRequestServiceLmpl implements LeagueRequestService {

    LeagueRequestRepository leagueRequestRepository;
    LeagueRequestDtoConvert leagueRequestDtoConvert;
    LeagueRequestVoConvert leagueRequestVoConvert;
    LeagueRepository leagueRepository;

    @Override
    public Boolean existsById(Long leagueRequestId){
        return leagueRequestRepository.existsById(leagueRequestId);
    }
    @Override
    public CodeEnum sendLeagueRequest(LeagueRequestDTO leagueRequestDTO) {

        if (leagueRequestRepository.existsByLeagueIdAndFromTeamIdAndTypeAndStatus(leagueRequestDTO.getLeagueId(), leagueRequestDTO.getFromTeamId(), leagueRequestDTO.getType(), 1)) {
            return CodeEnum.IS_EXIST;
        }

        LeagueRequest leagueRequest = leagueRequestDtoConvert.toEntity(leagueRequestDTO);
        leagueRequestRepository.save(leagueRequest);
        return CodeEnum.IS_SUCCESS;
    }

    @Override
    public List<LeagueRequestVo> getLeagueRequestLst(Long leagueId, Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<LeagueRequest> leagueRequestPage = leagueRequestRepository.findAll(new Specification<LeagueRequest>() {

            public Predicate toPredicate(Root<LeagueRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                Path<Long> leagueIdPath = root.get("leagueId");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(leagueIdPath, leagueId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<LeagueRequestVo> leagueRequestVoList = new ArrayList<>();

        for (LeagueRequest leagueRequest : leagueRequestPage) {
            leagueRequestVoList.add(leagueRequestVoConvert.toVo(leagueRequest));
        }

        return leagueRequestVoList;
    }

    @Override
    public CodeEnum deleteTeamRequest(Long manageMemberId, Long id) {

        LeagueRequest leagueRequest = leagueRequestRepository.findLeagueRequestById(id);

        League league = leagueRepository.findLeagueById(leagueRequest.getLeagueId());
        if (null == league){
            return CodeEnum.IS_LEAGUE_NOT_EXIST;
        }

        if (!league.getCreateMemberId().equals(manageMemberId))
        {
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        leagueRequestRepository.deleteById(id);

        return CodeEnum.IS_SUCCESS;
    }

    @Override
    //@Transactional
    public CodeEnum deleteAllTeamRequest(Long leagueId,  Long manageMemberId) {

        League league = leagueRepository.findLeagueById(leagueId);
        if (null == league){
            return CodeEnum.IS_LEAGUE_NOT_EXIST;
        }

        if (!league.getCreateMemberId().equals(manageMemberId))
        {
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }


        List<LeagueRequest> leagueRequestList = leagueRequestRepository.findAll(new Specification<LeagueRequest>() {

            public Predicate toPredicate(Root<LeagueRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                Path<Long> leagueIdPath = root.get("leagueId");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(leagueIdPath, leagueId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        });

        leagueRequestRepository.deleteAll(leagueRequestList);
        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 删除所有已读请求
     * @param leagueId 战队ID
     * @return CodeEnum.IS_SUCCESS
     */
    @Override
    public CodeEnum deleteAllDoneTeamRequest(Long leagueId,  Long manageMemberId){

        League league = leagueRepository.findLeagueById(leagueId);
        if (null == league){
            return CodeEnum.IS_LEAGUE_NOT_EXIST;
        }

        if (!league.getCreateMemberId().equals(manageMemberId))
        {
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        List<LeagueRequest> leagueRequestList = leagueRequestRepository.findAll(new Specification<LeagueRequest>() {

            public Predicate toPredicate(Root<LeagueRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Path<Long> leagueIdPath = root.get("leagueId");
                Path<Integer> statusPath = root.get("status");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(statusPath, 1).not());
                predicates.add(cb.equal(leagueIdPath, leagueId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });

        leagueRequestRepository.deleteAll(leagueRequestList);
        return CodeEnum.IS_SUCCESS;
    }
}
