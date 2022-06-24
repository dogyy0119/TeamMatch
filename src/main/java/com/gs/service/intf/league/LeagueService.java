package com.gs.service.intf.league;


import com.gs.constant.enums.CodeEnum;
import com.gs.model.dto.league.LeagueCreateDTO;
import com.gs.model.dto.league.LeagueTeamDTO;
import com.gs.model.dto.league.LeagueUpdateInfoDTO;
import com.gs.model.vo.league.LeagueVo;

import java.util.List;

/**
 * 队员Service接口层
 * User: lys
 * DateTime: 2022-05-1
 **/

public interface LeagueService {

    Boolean existById(Long leagueId);

    Long getCreateMemberId(Long leagueId);
    /**
     * 根据创建者的成员ID，查看是否已经创建过联盟
     *
     * @param createMemberId 创建者的成员ID
     * @return 是否创建过联盟
     */
    Boolean existsByCreateMemberId(Long createMemberId);

    /**
     * @param leagueCreateDTO  联盟相关输入dto
     * @return team相关输出Vo
     */
    LeagueVo createLeague(LeagueCreateDTO leagueCreateDTO);

    /**
     * 分页查询所有的联盟
     *
     * @param pageNum  当前获取的页码
     * @param pageSize 每页条数
     * @return Team List
     */
    List<LeagueVo> getLeagues(Integer pageNum, Integer pageSize);

    CodeEnum addTeam(Long manageId, LeagueTeamDTO leagueTeamDTO);
    /**
     * 处理战队请求的消息
     *
     * @param messageId 消息Id
     * @param flg  是否同意
     * @return 是否成功添加
     */
    CodeEnum doAddTeamRequest(Long managerId, Long messageId, Integer flg);

    CodeEnum joinLeague(LeagueTeamDTO leagueTeamDTO);
    /**
     * 处理成员请求消息
     *
     * @param requestId 消息Id
     * @param flg  是否同意
     * @return 是否成功添加
     */
    CodeEnum doJoinLeagueRequest(Long managerId, Long requestId,Integer flg);

    /**
     * 根据Id获取联盟实体
     * @param leagueId 联盟 ID
     * @return 联盟Vo
     */
    LeagueVo getLeague(Long leagueId);


    /**
     * 删除战队
     *
     * @param manageMemberId 管理者ID
     * @param leagueTeamDTO  成员相关信息DTO
     * @return 是否成功删除
     */
    CodeEnum deleteLeagueTeam(
            Long manageMemberId,
            LeagueTeamDTO leagueTeamDTO);

    /**
     * 退出联盟
     *
     * @param leagueTeamDTO 联盟战队相关信息DTO
     * @return 是否成功删除
     */
    CodeEnum quitLeague(
            Long manageMemberId,
            LeagueTeamDTO leagueTeamDTO);

    /**
     * @param managerId memberId，只有联盟的创建者可以删除联盟。
     * @param leagueId  联盟Id
     * @return CodeEnum.IS_SUCCES
     */
    CodeEnum deleteLeague(Long managerId, Long leagueId);

    /**
     * 更新联盟信息
     *
     * @param manageMemberId    管理者ID
     * @param leagueUpdateInfoDTO 联盟相关信息DTO
     * @return 是否成功更新战队信息
     */
    CodeEnum updateLeagueInfo(
            Long manageMemberId,
            LeagueUpdateInfoDTO leagueUpdateInfoDTO);

    /**
     * 根据关键字进行模糊查询
     * @param key 关键字
     * @param pageNum 当前页
     * @param pageSize 页容量
     * @return 符合条件得League List
     */
    public List<LeagueVo> queryLeaguesBykey(
            String key,
            Integer pageNum,
            Integer pageSize);

    List<LeagueVo> getLeaguesByMember(Long teamId, Integer pageNum, Integer pageSize);


}
