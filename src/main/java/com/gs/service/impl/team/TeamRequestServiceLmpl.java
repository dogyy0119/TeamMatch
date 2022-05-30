package com.gs.service.impl.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.constant.enums.MemberJobEnum;
import com.gs.convert.team.TeamRequestDtoConvert;
import com.gs.convert.team.TeamRequestVoConvert;
import com.gs.model.dto.team.TeamRequestDTO;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.entity.jpa.db1.team.TeamRequest;
import com.gs.model.vo.team.TeamRequestVo;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.repository.jpa.team.TeamRequestRepository;
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
public class TeamRequestServiceLmpl implements TeamRequestService {

    TeamRequestRepository teamRequestRepository;
    TeamRequestDtoConvert teamRequestDtoConvert;
    TeamRequestVoConvert teamRequestVoConvert;
    TeamRepository teamRepository;

    private TeamMember findTeamMemberByTeamIdAndMemberId(Long teamId, Long memberId) {
        List<TeamMember> teamMembers = teamRepository.findTeamById(teamId).getTeamMembers();

        for (TeamMember teamMember : teamMembers) {
            if (Objects.equals(teamMember.getMember().getId(), memberId)) {
                return teamMember;
            }
        }

        return null;
    }
    @Override
    public CodeEnum sendTeamRequest(TeamRequestDTO teamRequestDTO) {

        if (teamRequestRepository.existsByTeamIdAndFromIdAndType(teamRequestDTO.getTeamId(), teamRequestDTO.getFromId(), teamRequestDTO.getType())) {
            return CodeEnum.IS_EXIST;
        }

        teamRequestRepository.save(teamRequestDtoConvert.toEntity(teamRequestDTO));
        return CodeEnum.IS_SUCCESS;
    }

    @Override
    public List<TeamRequestVo> getTeamRequestLst(Long teamId,  Long memberId, Integer pageNum, Integer pageSize) {

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, memberId);
        if ((null == teamMember ) || teamMember.getJob() == MemberJobEnum.IS_TEAM_MEMBER.getJob()) {
            return null;
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<TeamRequest> teamRequestPage = teamRequestRepository.findAll(new Specification<TeamRequest>() {

            public Predicate toPredicate(Root<TeamRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                Path<Long> teamIdPath = root.get("teamId");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(teamIdPath, teamId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<TeamRequestVo> teamRequestVoList = new ArrayList<>();

        for (TeamRequest teamRequest : teamRequestPage) {
            teamRequestVoList.add(teamRequestVoConvert.toVo(teamRequest));
        }

        return teamRequestVoList;
    }

    @Override
    public CodeEnum deleteTeamRequest(Long id,  Long memberId) {

        TeamRequest teamRequest = teamRequestRepository.findTeamRequestById(id);
        if (teamRequest == null){
            return CodeEnum.IS_NOT_EXIST;
        }
        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamRequest.getTeamId(), memberId);
        if ((null == teamMember ) || teamMember.getJob() == MemberJobEnum.IS_TEAM_MEMBER.getJob()) {
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION;
        }

        teamRequestRepository.deleteById(id);
        return CodeEnum.IS_SUCCESS;
    }

    @Override
    //@Transactional
    public CodeEnum deleteAllTeamRequest(Long teamId,  Long memberId) {

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, memberId);
        if ((null == teamMember ) || teamMember.getJob() == MemberJobEnum.IS_TEAM_MEMBER.getJob()) {
            return null;
        }

        List<TeamRequest> teamRequestList = teamRequestRepository.findAll(new Specification<TeamRequest>() {

            public Predicate toPredicate(Root<TeamRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                Path<Long> teamIdPath = root.get("teamId");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(teamIdPath, teamId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        });

        teamRequestRepository.deleteAll(teamRequestList);
        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 删除所有已读请求
     * @param teamId 战队ID
     * @return CodeEnum.IS_SUCCESS
     */
    @Override
    public CodeEnum deleteAllDoneTeamRequest(Long teamId,  Long memberId){

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, memberId);
        if ((null == teamMember ) || teamMember.getJob() == MemberJobEnum.IS_TEAM_MEMBER.getJob()) {
            return null;
        }

        List<TeamRequest> teamRequestList = teamRequestRepository.findAll(new Specification<TeamRequest>() {

            public Predicate toPredicate(Root<TeamRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Path<Long> teamIdPath = root.get("teamId");
                Path<Integer> statusPath = root.get("status");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(statusPath, 1).not());
                predicates.add(cb.equal(teamIdPath, teamId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });

        teamRequestRepository.deleteAll(teamRequestList);
        return CodeEnum.IS_SUCCESS;
    }
}
