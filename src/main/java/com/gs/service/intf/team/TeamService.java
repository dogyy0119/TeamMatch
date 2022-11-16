package com.gs.service.intf.team;


import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.team.TeamCreateDTO;
import com.gs.model.dto.team.TeamMemberDTO;
import com.gs.model.dto.team.TeamUpdateInfoDTO;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.vo.team.TeamVo;

import java.util.List;

/**
 * 战队Service接口层
 * User: lys
 * DateTime: 2022-05-1
 **/

public interface TeamService {


    /**
     * 根据创建者的成员ID，查看是否已经创建过战队
     *
     * @param createMemberId 创建者的成员ID
     * @return 是否创建过战队
     */
    Boolean isAleadyInTeam(Long createMemberId);

    /**
     * 根据ID，判断是否存在对应的战队
     *
     * @param id 战队ID
     * @return 是否存在team
     */
    Boolean existById(Long id);

    /**
     * @param createMemberId 创建战队的用户ID
     * @param TeamCreateDTO  team相关输入dto
     * @return team相关输出Vo
     */
    TeamVo createTeam(Long createMemberId, TeamCreateDTO TeamCreateDTO);

    /**
     * 分页查询所有的战队
     *
     * @param pageNum  当前获取的页码
     * @param pageSize 每页条数
     * @return Team List
     */
    List<TeamVo> getTeamPage(Integer pageNum, Integer pageSize);

    /**
     * 获取战队成员
     *
     * @param teamId teamId
     * @param memberId  memberId
     * @return 是否成功添加
     */
    TeamMember findTeamMemberByTeamIdAndMemberId(Long teamId, Long memberId);
    /**
     * 处理战队请求的消息
     *
     * @param messageId 消息Id
     * @param flg  是否同意
     * @return 是否成功添加
     */
    CodeEnum doTeamRequest(Long messageId, Integer flg);

    /**
     * 处理成员请求消息
     *
     * @param requestId 消息Id
     * @param flg  是否同意
     * @return 是否成功添加
     */
    CodeEnum doMemberRequest(Long requestId,Integer flg);
    /**
     * 根据teamID获取Team
     *
     * @param id teamID
     * @return team
     */
    TeamVo getTeamByTeamId(Long id);

    /**
     * 删除成员
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功删除
     */
    CodeEnum deleteMember(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO);

    /**
     * 退出战队
     *
     * @param teamMemberDTO 成员相关信息DTO
     * @return 是否成功删除
     */
    CodeEnum quitTeam(TeamMemberDTO teamMemberDTO);

    /**
     * 转让队长
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功转让队长
     */
    CodeEnum transferTeamLeader(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO);

    /**
     * 设置第二队长
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功设置第二队长
     */
    CodeEnum setSecondTeamLeader(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO);

    /**
     * 解除第二队长
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功解除第二队长
     */
    CodeEnum releaseSecondTeamLeader(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO);

    /**
     * 禁言或者解除禁言
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否禁言或者解除禁言成功
     */
    CodeEnum changeSilentFlg(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO);

    Integer getSilentFlg(
            Long teamId,
            Long memberId);
    /**
     * 解散战队
     *
     * @param manageMemberId 管理者ID
     * @param id         战队Id
     * @return 是否成功解散战队
     */
    CodeEnum releaseTeam(
            Long manageMemberId,
            Long id);

    /**
     * 更新战队信息
     *
     * @param manageMemberId    管理者ID
     * @param teamUpdateInfoDTO 战队相关信息DTO
     * @return 是否成功更新战队名称
     */
    CodeEnum updateTeamInfo(
            Long manageMemberId,
            TeamUpdateInfoDTO teamUpdateInfoDTO);

    /**
     * 更新战队名称
     *
     * @param manageMemberId    管理者ID
     * @param teamUpdateInfoDTO 战队相关信息DTO
     * @return 是否成功更新战队名称
     */
    CodeEnum updateTeamName(
            Long manageMemberId,
            TeamUpdateInfoDTO teamUpdateInfoDTO);


    /**
     * 更新战队成员最大数量
     *
     * @param manageMemberId    管理者ID
     * @param teamUpdateInfoDTO 战队相关信息DTO
     * @return 是否成功更新战队成员最大数量
     */
    CodeEnum updateTeamMaxMemberNum(
            Long manageMemberId,
            TeamUpdateInfoDTO teamUpdateInfoDTO);


    /**
     * 更新战队描述信息
     *
     * @param manageMemberId    管理者ID
     * @param teamUpdateInfoDTO 战队相关信息DTO
     * @return 是否成功更新战队描述信息
     */
    CodeEnum updateTeamDescriptionInfo(
            Long manageMemberId,
            TeamUpdateInfoDTO teamUpdateInfoDTO);

    /**
     * 根据关键字进行模糊查询
     *
     * @param key      关键字
     * @param pageNum  当前页
     * @param pageSize 页容量
     * @return 符合条件得Team List
     */
    List<TeamVo> queryTeamBykey(
            Long currentMemberId,
            String key,
            Integer pageNum,
            Integer pageSize);
}
