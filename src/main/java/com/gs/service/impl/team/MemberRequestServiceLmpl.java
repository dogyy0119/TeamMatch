package com.gs.service.impl.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.convert.team.MemberRequestDtoConvert;
import com.gs.convert.team.MemberRequestVoConvert;
import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.model.entity.jpa.db1.team.MemberRequest;
import com.gs.model.vo.team.MemberRequestVo;
import com.gs.repository.jpa.team.MemberRequestRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.team.MemberRequestService;
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

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MemberRequestServiceLmpl implements MemberRequestService {

    MemberRequestRepository memberRequestRepository;
    MemberRequestDtoConvert memberRequestDtoConvert;
    MemberRequestVoConvert memberRequestVoConvert;
    TeamRepository teamRepository;

    @Override
    public CodeEnum sendMemberRequest(MemberRequestDTO memberRequestDTO) {

        if (memberRequestRepository.existsByTeamIdAndToIdAndType(memberRequestDTO.getTeamId(), memberRequestDTO.getToId(), memberRequestDTO.getType())) {
            return CodeEnum.IS_EXIST;
        }

        memberRequestRepository.save(memberRequestDtoConvert.toEntity(memberRequestDTO));
        return CodeEnum.IS_SUCCESS;
    }

    @Override
    public List<MemberRequestVo> getMemberRequestLst(Long memberId, Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<MemberRequest> memberRequestPage = memberRequestRepository.findAll(new Specification<MemberRequest>() {

            public Predicate toPredicate(Root<MemberRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Path<Long> toIdPath = root.get("toId");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(toIdPath, memberId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);


        List<MemberRequestVo> memberRequestVoList = new ArrayList<>();

        for (MemberRequest memberRequest : memberRequestPage) {
            memberRequestVoList.add(memberRequestVoConvert.toVo(memberRequest));
        }

        return memberRequestVoList;
    }

    @Override
    public CodeEnum deleteMemberRequest(Long id) {
        memberRequestRepository.deleteById(id);
        return CodeEnum.IS_SUCCESS;
    }

    @Override
    //@Transactional
    public CodeEnum deleteAllMemberRequest(Long memberId) {
        List<MemberRequest> teamRequestList = memberRequestRepository.findAll(new Specification<MemberRequest>() {

            public Predicate toPredicate(Root<MemberRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Path<Long> toIdPath = root.get("toId");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(toIdPath, memberId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        });

        memberRequestRepository.deleteAll(teamRequestList);
        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 删除所有已读请求
     *
     * @param memberId 成员ID
     * @return CodeEnum.IS_SUCCESS
     */
    @Override
    public CodeEnum deleteAllDoneMemberRequest(Long memberId) {
        List<MemberRequest> teamRequestList = memberRequestRepository.findAll(new Specification<MemberRequest>() {

            public Predicate toPredicate(Root<MemberRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                Path<Long> toIdPath = root.get("toId");
                Path<Integer> statusPath = root.get("status");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(statusPath, 1).not());
                predicates.add(cb.equal(toIdPath, memberId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        });

        memberRequestRepository.deleteAll(teamRequestList);
        return CodeEnum.IS_SUCCESS;
    }
}
