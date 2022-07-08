package com.gs.service.intf.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.team.MemberRequestDTO;
import com.gs.model.vo.team.MemberRequestVo;

import java.util.List;

public interface MemberRequestService {

    MemberRequestVo getMemberRequest(Long id);
    CodeEnum sendMemberRequest(MemberRequestDTO teamRequest);

    List<MemberRequestVo> getMemberRequestLst(Long memberId, Integer pageNum, Integer pageSize);

    CodeEnum deleteMemberRequest(Long id);

    CodeEnum deleteAllMemberRequest(Long memberId);


    /**
     * 删除所有已读请求
     *
     * @param memberId 战队ID
     * @return CodeEnum.IS_SUCCESS
     */
    CodeEnum deleteAllDoneMemberRequest(Long memberId);
}
