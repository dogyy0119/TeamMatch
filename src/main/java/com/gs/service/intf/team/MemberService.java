package com.gs.service.intf.team;


import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.vo.team.MemberVo;

import java.util.List;

/**
 * 队员Service接口层
 * User: lys
 * DateTime: 2022-05-1
 **/

public interface MemberService {


    /**
     * 根据ID判断队员是否存在
     *
     * @param id id
     * @return 是否存在
     */
    Boolean existsById(Long id);

    /**
     * 根据id获取Member
     *
     * @param memberId id
     * @return member
     */
    Member getMemberById(Long memberId);

    /**
     * 根据关键字进行模糊查询
     *
     * @param key      关键字
     * @param pageNum  当前页
     * @param pageSize 页容量
     * @return 符合条件得Team List
     */
    List<MemberVo> queryMembersBykey(
            Long currentMemberId,
            String key,
            Integer pageNum,
            Integer pageSize);
}
