package com.gs.service.impl.team;


import com.gs.constant.enums.MemberJobEnum;
import com.gs.convert.team.TeamToVoConvert;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.vo.team.TeamVo;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamMemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.team.TeamMemberService;
import lombok.AllArgsConstructor;
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

/**
 * 战队成员 Service接口实现层
 * User: lys
 * DateTime: 2022-05-1
 **/
@Service
@AllArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    private TeamToVoConvert teamToVoConvert;

    private MemberRepository memberRepository;

    private TeamRepository teamRepository;

    @Override
    public Boolean isMemberInTeam(Long memberId, Long teamId){
        Member member = memberRepository.findMemberById(memberId);
        Team team = teamRepository.findTeamById(teamId);
        TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberAndTeam(member, team);
        if (teamMember == null){
            return false;
        }

        return true;
    }

    @Override
    public Integer getMemberJobInTeam(Long memberId, Long teamId){
        Member member = memberRepository.getById(memberId);
        Team team = teamRepository.getById(teamId);
        TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberAndTeam(member, team);

        return teamMember.getJob();
    }
    /**
     * 根据Member Id分页查询所有的战队
     *
     * @param pageNum  当前获取的页码
     * @param pageSize 每页条数
     * @return Team List
     */

    @Override
    public List<TeamVo> getTeamPageByPlayerId(
            Long memberId,
            Integer pageNum,
            Integer pageSize) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<TeamMember> teamMemberPage = teamMemberRepository.findAll(new Specification<TeamMember>() {

            public Predicate toPredicate(Root<TeamMember> root,
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

        List<TeamVo> teamVoList = new ArrayList<>();

        for (TeamMember teamMember : teamMemberPage) {
            teamVoList.add(teamToVoConvert.toVo(teamMember.getTeam()));
        }

        return teamVoList;
    }
}
