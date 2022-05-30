package com.gs.service.impl.team;


import com.gs.convert.team.MemberToVoConvert;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.vo.team.MemberVo;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.service.intf.team.MemberService;
import lombok.AllArgsConstructor;
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
 * 用户Service层实现层
 * User: lys
 * DateTime: 2022-05-1
 **/

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    private MemberToVoConvert memberToVoConvert;

    /**
     * 根据ID判断队员是否存在
     * @param id id
     * @return 是否存在
     */
    public Boolean existsById(Long id) {
        return memberRepository.existsById(id);
    }

    /**
     * 根据id获取Member
     * @param memberId id
     * @return member
     */
    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findMemberById(memberId);
    }

    /**
     * 根据关键字进行模糊查询
     * @param key 关键字
     * @param pageNum 当前页
     * @param pageSize 页容量
     * @return 符合条件得Team List
     */
    @Override
    public List<MemberVo> queryMembersBykey(
            String key,
            Integer pageNum,
            Integer pageSize) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Member> memberPage = memberRepository.findAll(new Specification<Member>() {

            public Predicate toPredicate(Root<Member> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> name = root.get("name");
                Path<String> email = root.get("email");
                Path<String> phone = root.get("phone");
                Path<String> pubgName = root.get("pubgName");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                Predicate p1 = cb.like(name.as(String.class), "%" + key + "%");
                Predicate p2 = cb.or(cb.like(email.as(String.class), "%" + key + "%"), p1);
                Predicate p3 = cb.or(cb.like(phone.as(String.class), "%" + key + "%"), p2);
                Predicate p4 = cb.or(cb.like(pubgName.as(String.class), "%" + key + "%"), p3);

                predicates.add(p4);

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<MemberVo> memberList = new ArrayList<>();

        for (Member entry : memberPage) {
            memberList.add(memberToVoConvert.toVo(entry));
        }

        return memberList;
    }
}
