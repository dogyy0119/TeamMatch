package com.gs.service.impl.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.convert.league.LeagueTeamRequestDtoConvert;
import com.gs.convert.league.LeagueTeamRequestVoConvert;
import com.gs.convert.team.MemberRequestDtoConvert;
import com.gs.convert.team.MemberRequestVoConvert;
import com.gs.model.dto.league.LeagueTeamRequestDTO;
import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.entity.jpa.db1.league.LeagueTeam;
import com.gs.model.entity.jpa.db1.league.LeagueTeamRequest;
import com.gs.model.entity.jpa.db1.team.MemberRequest;
import com.gs.model.vo.league.LeagueRequestVo;
import com.gs.model.vo.league.LeagueTeamRequestVo;
import com.gs.model.vo.team.MemberRequestVo;
import com.gs.repository.jpa.league.LeagueRepository;
import com.gs.repository.jpa.league.LeagueTeamRequestRepository;
import com.gs.repository.jpa.team.MemberRequestRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.league.LeagueTeamRequestService;
import com.gs.service.intf.team.MemberRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LeagueTeamRequestServiceLmpl implements LeagueTeamRequestService {

    LeagueTeamRequestRepository leagueTeamRequestRepository;
    LeagueTeamRequestDtoConvert leagueTeamRequestDtoConvert;
    LeagueTeamRequestVoConvert leagueTeamRequestVoConvert;

    LeagueRepository leagueRepository;
    TeamRepository teamRepository;

    @Override
    public CodeEnum sendLeagueTeamRequest(LeagueTeamRequestDTO leagueTeamRequestDTO) {

        League league = leagueRepository.findLeagueById(leagueTeamRequestDTO.getLeagueId());
        if (!Objects.equals(league.getCreateMemberId(), leagueTeamRequestDTO.getFromId())){

            log.error("sendLeagueTeamRequest：" + "只有联盟创建者有权限");
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        if (leagueTeamRequestRepository.existsByLeagueIdAndToTeamIdAndTypeAndStatus(leagueTeamRequestDTO.getLeagueId(), leagueTeamRequestDTO.getToTeamId(), leagueTeamRequestDTO.getType(), 1)) {

            log.error("sendLeagueTeamRequest：" + "战队已经在联盟里");
            return CodeEnum.IS_TEAM_ALLEARY_IN_LEAGUE;
        }

        leagueTeamRequestRepository.save(leagueTeamRequestDtoConvert.toEntity(leagueTeamRequestDTO));
        return CodeEnum.IS_SUCCESS;
    }

    @Override
    public List<LeagueTeamRequestVo> getLeagueTeamRequests(Long teamId, Integer pageNum, Integer pageSize) {


        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<LeagueTeamRequest> leagueTeamRequestPage = leagueTeamRequestRepository.findAll(new Specification<LeagueTeamRequest>() {
            public Predicate toPredicate(Root<LeagueTeamRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Path<Long> toIdPath = root.get("toTeamId");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(toIdPath, teamId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);


        List<LeagueTeamRequestVo> leagueTeamRequestVoList = new ArrayList<>();

        for (LeagueTeamRequest leagueTeamRequest : leagueTeamRequestPage) {
            leagueTeamRequestVoList.add(leagueTeamRequestVoConvert.toVo(leagueTeamRequest));
        }

        return leagueTeamRequestVoList;
    }

    @Override
    public CodeEnum deleteLeagueTeamRequest(Long id) {
        leagueTeamRequestRepository.deleteById(id);
        return CodeEnum.IS_SUCCESS;
    }

    @Override
    //@Transactional
    public CodeEnum deleteAllLeagueTeamRequest(Long teamId) {
        List<LeagueTeamRequest> leagueTeamRequests = leagueTeamRequestRepository.findAll(new Specification<LeagueTeamRequest>() {

            public Predicate toPredicate(Root<LeagueTeamRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Path<Long> toIdPath = root.get("toTeamId");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(toIdPath, teamId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        });

        leagueTeamRequestRepository.deleteAll(leagueTeamRequests);
        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 删除所有已读请求
     *
     * @param teamId 成员ID
     * @return CodeEnum.IS_SUCCESS
     */
    @Override
    public CodeEnum deleteAllDoneLeagueTeamRequest(Long teamId) {
        List<LeagueTeamRequest> leagueTeamRequestList = leagueTeamRequestRepository.findAll(new Specification<LeagueTeamRequest>() {

            public Predicate toPredicate(Root<LeagueTeamRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                Path<Long> toIdPath = root.get("toTeamId");
                Path<Integer> statusPath = root.get("status");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(statusPath, 1).not());
                predicates.add(cb.equal(toIdPath, teamId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        });

        leagueTeamRequestRepository.deleteAll(leagueTeamRequestList);
        return CodeEnum.IS_SUCCESS;
    }

}
