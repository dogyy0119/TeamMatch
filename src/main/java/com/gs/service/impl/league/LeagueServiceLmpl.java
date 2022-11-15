package com.gs.service.impl.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.constant.enums.MemberJobEnum;
import com.gs.convert.league.LeagueDtoConvert;
import com.gs.convert.league.LeagueTaskVoConvert;
import com.gs.convert.league.LeagueVoConvert;
import com.gs.convert.team.TeamTaskVoConvert;
import com.gs.model.dto.league.LeagueCreateDTO;
import com.gs.model.dto.league.LeagueTeamDTO;
import com.gs.model.dto.league.LeagueUpdateInfoDTO;
import com.gs.model.dto.team.TeamMemberDTO;
import com.gs.model.entity.jpa.db1.league.*;
import com.gs.model.entity.jpa.db1.team.*;
import com.gs.model.vo.league.LeagueVo;
import com.gs.repository.jpa.league.*;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamMemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.league.LeagueService;
import com.gs.utils.JpaUtil;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class LeagueServiceLmpl implements LeagueService {

    private LeagueRepository leagueRepository;

    private LeagueTeamRequestRepository leagueTeamRequestRepository;

    private LeagueRequestRepository leagueRequestRepository;

    private LeagueTeamRepository leagueTeamRepository;

    private LeagueDtoConvert leagueDtoConvert;

    private LeagueVoConvert leagueVoConvert;

    private LeagueTaskRepository leagueTaskRepository;
    private LeagueTaskVoConvert leagueTaskVoConvert;

    private MemberRepository memberRepository;
    private TeamRepository teamRepository;

    private TeamMemberRepository teamMemberRepository;

    private void createLeagueTeak(Long leagueId, String content){
        LeagueTask leagueTask = new LeagueTask();
        leagueTask.setLeagueId(leagueId);
        leagueTask.setTeamTaskContent(content);
        leagueTask.setCreateTime(new Date());

        leagueTaskVoConvert.toVo(leagueTaskRepository.save(leagueTask));
    }
    @Override
    public Boolean existById(Long leagueId){
        return leagueRepository.existsById(leagueId);
    }

    @Override
    public Long getCreateMemberId(Long leagueId){
        return leagueRepository.findLeagueById(leagueId).getCreateMemberId();
    }

    /**
     * 根据id。查看是否已经在联盟中
     *
     * @param memberId 创建者的成员ID
     * @return 是否创建过联盟
     */
    public Boolean isAleadyInLeague(Long memberId) {
        //先检查是否已经在联盟中
        List<League> leagueLst = leagueRepository.findAll();
        for (League league : leagueLst){
            for (LeagueTeam leagueTeam : league.getLeagueTeams()){
                Team team = teamRepository.findTeamById(leagueTeam.getTeamId());
                for (TeamMember teamMember : team.getTeamMembers()){
                    if (Objects.equals(teamMember.getMember().getId(), memberId)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @param leagueCreateDTO 联盟相关输入dto
     * @return team相关输出Vo
     */
    public LeagueVo createLeague(LeagueCreateDTO leagueCreateDTO) {
        //构建league
        League league = leagueDtoConvert.toEntity(leagueCreateDTO);
        league = leagueRepository.save(league);

        //创建队务
        Member createMember = memberRepository.findMemberById(leagueCreateDTO.getCreateMemberId());
        String teamTaskContent = createMember.getName() + " 创建了 " + league.getName();
        createLeagueTeak(league.getId(), teamTaskContent);

        return leagueVoConvert.toVo(league);
    }


    /**
     * 分页查询所有的联盟
     *
     * @param pageNum  当前获取的页码
     * @param pageSize 每页条数
     * @return Team List
     */
    @Override
    public List<LeagueVo> getLeagues(Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<League> leaguePage = leagueRepository.findAll(pageable);

        List<LeagueVo> leagueVoList = new ArrayList<>();
        for (League entity : leaguePage) {
            leagueVoList.add(leagueVoConvert.toVo(entity));
        }

        return leagueVoList;
    }

    /**
     *
     * @param manageId 管理者Id
     * @param leagueTeamDTO LeagueTeam dto
     * @return 是否添加成功
     */
    @Override
    public CodeEnum addTeam(Long manageId, LeagueTeamDTO leagueTeamDTO){

        Long leagueId = leagueTeamDTO.getLeagueId();

        Long teamId = leagueTeamDTO.getTeamId();

        //判断待添加的队员是否已经在战队中
        LeagueTeam leagueTeam = leagueTeamRepository.findLeagueTeamByLeagueIdAndTeamId(leagueId, teamId);
        if (null != leagueTeam) {
            log.error("addTeam：" + "战队已经在联盟里");
            return CodeEnum.IS_TEAM_ALLEARY_IN_LEAGUE;
        }

        //添加战队
        League league = leagueRepository.findLeagueById(leagueId);
        //判断战队的人数是否已经达到上线
        if (league.getMaxTeamNum() <= league.getLeagueTeams().size()) {
            log.error("addTeam：" + "超出战队成员数上限");
            return CodeEnum.IS_BEYOND_LIMIT_ERROR;
        }

        //构建leagueTeam
        leagueTeam = new LeagueTeam();
        leagueTeam.setLeague(league);
        leagueTeam.setTeamId(teamId);
        leagueTeam.setJoinTime(new Date());
        league.getLeagueTeams().add(leagueTeam);

        Team team = teamRepository.findTeamById(teamId);
        leagueRepository.save(league);

        //创建队务
        String leagueTaskContent = team.getName() + " 申请加入 " + league.getName();
        createLeagueTeak(league.getId(), leagueTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 处理添加战队的请求
     *
     * @param requestId 消息Id
     * @param flg  是否同意
     * @return 是否成功添加
     */
    @Override
    public CodeEnum doAddTeamRequest(Long managerId, Long requestId, Integer flg){
        if (!leagueTeamRequestRepository.existsById(requestId)){
            log.error("doAddTeamRequest：" + "联盟战队请求不存在");
            return CodeEnum.IS_LEAGUE_TEAM_REQUEST_NOT_EXIST;
        }

        LeagueTeamRequest leagueTeamRequest = leagueTeamRequestRepository.findLeagueTeamRequestById(requestId);
        Long leagueId = leagueTeamRequest.getLeagueId();

        Team team = teamRepository.findTeamById(leagueTeamRequest.getToTeamId());
        if (team == null ){
            log.error("doAddTeamRequest：" + "联盟战队请求错误");
            return CodeEnum.IS_LEAGUE_TEAM_REQUEST_ERROR;
        }

        Member member = memberRepository.findMemberById(managerId);
        if (member == null ){
            log.error("doAddTeamRequest：" + "玩家不存在");
            return CodeEnum.IS_MEMBER_NOT_EXIST;
        }

        TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberAndTeam(member, team);
        if (teamMember == null){
            log.error("doAddTeamRequest：" + "该玩家不在该战队中");
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }
        if (teamMember.getJob()==MemberJobEnum.IS_TEAM_MEMBER.getJob()){
            log.error("doAddTeamRequest：" + "只有队长和副队长有权限查看联盟");
            return CodeEnum.IS_TEAM_LEAGUE_PERMISSION;
        }

        if (!leagueRepository.existsById(leagueId)){
            log.error("doAddTeamRequest：" + "联盟不存在");
            return CodeEnum.IS_LEAGUE_NOT_EXIST;
        }

        //选择拒绝后，直接生成队务，然后删除战队请求
        if (flg == 0){
            //创建队务
            String leagueTaskContent;
            leagueTaskContent = teamRepository.findTeamById(leagueTeamRequest.getToTeamId()).getName() + " 拒绝了联盟的邀请";

            createLeagueTeak(leagueId, leagueTaskContent);

            //处理完毕后更新战队请求的状态
            leagueTeamRequest.setStatus(3);
            leagueTeamRequestRepository.save(leagueTeamRequest);

            return CodeEnum.IS_SUCCESS;
        }

        if (leagueTeamRepository.existsByTeamId(leagueTeamRequest.getToTeamId())){

            //创建队务
            String teamTaskContent;
            teamTaskContent = teamRepository.findTeamById(leagueTeamRequest.getToTeamId()).getName() + " 已经加入其他联盟";

            createLeagueTeak(leagueId, teamTaskContent);

            //处理完毕后更新战队请求的状态
            leagueTeamRequest.setStatus(2);
            leagueTeamRequestRepository.save(leagueTeamRequest);
            return CodeEnum.IS_ALREADY_IN_LEAGUE;
        }

        //添加战队之前更新战队请求的状态
        leagueTeamRequest.setStatus(2);
        leagueTeamRequestRepository.save(leagueTeamRequest);

        LeagueTeamDTO leagueTeamDTO = new LeagueTeamDTO();
        leagueTeamDTO.setTeamId(leagueTeamRequest.getToTeamId());
        leagueTeamDTO.setLeagueId(leagueTeamRequest.getLeagueId());

        Long manageMemberId = leagueTeamRequest.getFromId();

        return addTeam(manageMemberId, leagueTeamDTO);
    }

    /**
     *
     * @param leagueTeamDTO LeagueTeam dto
     * @return 是否添加成功
     */
    @Override
    public CodeEnum joinLeague(LeagueTeamDTO leagueTeamDTO){
        Long leagueId = leagueTeamDTO.getLeagueId();

        //判断待添加的队员是否已经在战队中
        LeagueTeam leagueTeam = leagueTeamRepository.findLeagueTeamByLeagueIdAndTeamId(leagueId, leagueTeamDTO.getTeamId());
        if (null != leagueTeam) {
            log.error("joinLeague：" + "战队已经在联盟里");
            return CodeEnum.IS_TEAM_ALLEARY_IN_LEAGUE;
        }

        //添加战队
        League league = leagueRepository.findLeagueById(leagueId);
        //判断战队的人数是否已经达到上线
        if (league.getMaxTeamNum() <= league.getLeagueTeams().size()) {
            log.error("joinLeague：" + "超出战队成员数上限");
            return CodeEnum.IS_BEYOND_LIMIT_ERROR;
        }

        leagueTeam = new LeagueTeam();
        leagueTeam.setLeague(league);
        leagueTeam.setTeamId(leagueTeamDTO.getTeamId());
        leagueTeam.setJoinTime(new Date());
        league.getLeagueTeams().add(leagueTeam);

         leagueRepository.save(league);

        //创建队务
        Team team = teamRepository.findTeamById(leagueTeamDTO.getTeamId());
        String leagueTaskContent = team.getName() + " 申请加入 " + league.getName();
        createLeagueTeak(league.getId(), leagueTaskContent);

        return CodeEnum.IS_SUCCESS;
    }
    /**
     * 处理加入联盟请求
     *
     * @param requestId 消息Id
     * @param flg  是否同意
     * @return 是否成功添加
     */
    @Override
    public CodeEnum doJoinLeagueRequest(Long managerId, Long requestId, Integer flg){
        if (!leagueRequestRepository.existsById(requestId)){
            log.error("doJoinLeagueRequest：" + "联盟请求不存在");
            return CodeEnum.IS_LEAGUE_REQUEST_NOT_EXIST;
        }

        LeagueRequest leagueRequest = leagueRequestRepository.findLeagueRequestById(requestId);
        Long leagueId = leagueRequest.getLeagueId();

        League league = leagueRepository.findLeagueById(leagueId);
        if (!managerId.equals(league.getCreateMemberId())){
            log.error("doJoinLeagueRequest：" + "只有联盟创建者有权限");
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        //选择拒绝后，直接生成队务，然后删除战队请求
        if (flg == 0){
            //创建队务
            String teamTaskContent;
            teamTaskContent = "拒绝" + teamRepository.findTeamById(leagueRequest.getFromTeamId()).getName() + " 加入联盟的申请";

            createLeagueTeak(leagueId, teamTaskContent);

            //处理完毕后更新战队请求的状态
            leagueRequest.setStatus(3);
            leagueRequestRepository.save(leagueRequest);

            return CodeEnum.IS_SUCCESS;
        }

        if (isAleadyInLeague(leagueRequest.getFromMemberId())){

            //创建队务
            String teamTaskContent;
            teamTaskContent = teamRepository.findTeamById(leagueRequest.getFromTeamId()).getName() + " 已经加入其他联盟";

            createLeagueTeak(leagueId, teamTaskContent);

            //处理完毕后更新战队请求的状态
            leagueRequest.setStatus(2);
            leagueRequestRepository.save(leagueRequest);
            return CodeEnum.IS_ALREADY_IN_LEAGUE;
        }

        //添加成员之前更新战队请求的状态
        leagueRequest.setStatus(2);
        leagueRequestRepository.save(leagueRequest);

        LeagueTeamDTO leagueTeamDTO = new LeagueTeamDTO();
        leagueTeamDTO.setLeagueId(leagueId);
        leagueTeamDTO.setTeamId(leagueRequest.getFromTeamId());

        return joinLeague(leagueTeamDTO);
    }

    /**
     * 根据Id获取联盟实体
     * @param leagueId 联盟 ID
     * @return 联盟Vo
     */
    @Override
    public LeagueVo getLeague(Long leagueId){
        return leagueVoConvert.toVo(leagueRepository.findLeagueById(leagueId));
    }
    /**
     * 删除战队
     *
     * @param manageMemberId 管理者ID
     * @param leagueTeamDTO  成员相关信息DTO
     * @return 是否成功删除
     */
    @Override
    public CodeEnum deleteLeagueTeam(
            Long manageMemberId,
            LeagueTeamDTO leagueTeamDTO){

        Long leagueId = leagueTeamDTO.getLeagueId();

        League league = leagueRepository.findLeagueById(leagueId);
        if (!manageMemberId.equals(league.getCreateMemberId())){
            log.error("deleteLeagueTeam：" + "只有联盟创建者有权限");
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        //判断待添加的队员是否已经在战队中
        LeagueTeam leagueTeam = leagueTeamRepository.findLeagueTeamByLeagueIdAndTeamId(leagueId, leagueTeamDTO.getTeamId());
        if (null == leagueTeam) {
            log.error("deleteLeagueTeam：" + "战队不在联盟里");
            return CodeEnum.IS_TEAM_NOT_IN_LEAGUE;
        }

        //删除战队
        league.getLeagueTeams().removeIf(leagueTeam1 -> {

            if (Objects.equals(leagueTeam1.getTeamId(), leagueTeamDTO.getTeamId())) {
                return true;
            }
            return false;
        });

        leagueRepository.save(league);

        //创建队务
        Member manageMember = memberRepository.findMemberById(manageMemberId);
        String manageMemberName = manageMember.getName();
        String teamTaskContent = manageMemberName + " 删除战队 " + teamRepository.findTeamById(leagueTeamDTO.getTeamId()).getName();
        createLeagueTeak(leagueId, teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 退出联盟
     *
     * @param leagueTeamDTO 联盟战队相关信息DTO
     * @return 是否成功删除
     */
    @Override
    public CodeEnum quitLeague(
            Long manageMemberId,
            LeagueTeamDTO leagueTeamDTO){

        Long leagueId = leagueTeamDTO.getLeagueId();
        League league = leagueRepository.findLeagueById(leagueId);

        Team team = teamRepository.findTeamById(leagueTeamDTO.getTeamId());
        Member member = memberRepository.findMemberById(manageMemberId);

        TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberAndTeam(member, team);

        if (teamMember == null){
            log.error("quitLeague：" + "该玩家不在该战队中");
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }

        if (teamMember.getJob()==MemberJobEnum.IS_TEAM_MEMBER.getJob()){
            log.error("quitLeague：" + "只有队长和副队长有权限查看联盟");
            return CodeEnum.IS_TEAM_LEAGUE_PERMISSION;
        }

        //判断待添加的队员是否已经在战队中
        LeagueTeam leagueTeam = leagueTeamRepository.findLeagueTeamByLeagueIdAndTeamId(leagueId, leagueTeamDTO.getTeamId());
        if (null == leagueTeam) {
            log.error("quitLeague：" + "战队不在联盟里");
            return CodeEnum.IS_TEAM_NOT_IN_LEAGUE;
        }

        //删除战队
        league.getLeagueTeams().removeIf(leagueTeam1 -> {

            if (Objects.equals(leagueTeam1.getTeamId(), leagueTeamDTO.getTeamId())) {
                return true;
            }
            return false;
        });

        leagueRepository.save(league);

        //创建队务
        String leagueTaskContent = teamRepository.findTeamById(leagueTeamDTO.getTeamId()).getName() + " 退出联盟 " + team.getName();
        createLeagueTeak(league.getId(), leagueTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * @param leagueId 联盟Id
     * @return CodeEnum.IS_SUCCES
     */
    @Override
    public CodeEnum deleteLeague(Long managerId, Long leagueId) {

        League league = leagueRepository.findLeagueById(leagueId);
        if (!Objects.equals(league.getCreateMemberId(), managerId)){
            log.error("deleteLeague：" + "只有联盟创建者有权限");
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        leagueRepository.delete(league);

        //联盟解散了，删除所有和联盟相关的资源
        List<LeagueTask> leagueTaskList = leagueTaskRepository.findLeagueTasksByLeagueId(leagueId);
        leagueTaskRepository.deleteAll(leagueTaskList);

//        List<Message> messageList = messageRepository.findMessagesByTeamId(teamId);
//        messageRepository.deleteAll(messageList);

        List<LeagueRequest> leagueRequestList = leagueRequestRepository.findLeagueRequestsByLeagueId(leagueId);
        leagueRequestRepository.deleteAll(leagueRequestList);

        List<LeagueTeamRequest> leagueTeamRequestList = leagueTeamRequestRepository.findLeagueRequestsByLeagueId(leagueId);
        leagueTeamRequestRepository.deleteAll(leagueTeamRequestList);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 更新联盟信息
     *
     * @param manageMemberId    管理者ID
     * @param leagueUpdateInfoDTO 联盟相关信息DTO
     * @return 是否成功更新战队信息
     */
    @Override
    public CodeEnum updateLeagueInfo(
            Long manageMemberId,
            LeagueUpdateInfoDTO leagueUpdateInfoDTO){

        Long leagueId = leagueUpdateInfoDTO.getLeagueId();
        League league = leagueRepository.findLeagueById(leagueId);


        //创建队务
        String teamTaskContent;

        if (!leagueUpdateInfoDTO.getName().isEmpty() && !league.getName().equals(leagueUpdateInfoDTO.getName())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " 更改了联盟名称";
            createLeagueTeak(league.getId(), teamTaskContent);
        }

        if (!leagueUpdateInfoDTO.getDescriptionInfo().isEmpty() && !league.getDescriptionInfo().equals(leagueUpdateInfoDTO.getDescriptionInfo())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " 更改了联盟简介";
            createLeagueTeak(league.getId(), teamTaskContent);
        }


        if ( leagueUpdateInfoDTO.getLogoUrl() != null && !leagueUpdateInfoDTO.getLogoUrl().isEmpty() && !league.getLogoUrl().equals(leagueUpdateInfoDTO.getLogoUrl())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " 更新了联盟logo";
            createLeagueTeak(league.getId(), teamTaskContent);
        }

        if (!manageMemberId.equals(league.getCreateMemberId())){
            log.error("updateLeagueInfo：" + "只有联盟创建者有权限");
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        String[] Field = {"name", "descriptionInfo", "logoUrl"};  //更新 Field指定允许字段
        JpaUtil.copyNotNullPropertiesAllow(leagueUpdateInfoDTO, league, Field);

        leagueRepository.save(league);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 根据关键字进行模糊查询
     * @param key 关键字
     * @param pageNum 当前页
     * @param pageSize 页容量
     * @return 符合条件得Team List
     */
    @Override
    public List<LeagueVo> queryLeaguesBykey(
            String key,
            Integer pageNum,
            Integer pageSize) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<League> leaguePage = leagueRepository.findAll(new Specification<League>() {

            public Predicate toPredicate(Root<League> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> name = root.get("name");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                Predicate p1 = cb.like(name.as(String.class), "%" + key + "%");

                predicates.add(p1);

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<LeagueVo> leagueVoList = new ArrayList<>();

        for (League entry : leaguePage) {
            leagueVoList.add(leagueVoConvert.toVo(entry));
        }

        return leagueVoList;
    }


    @Override
    public List<LeagueVo> getLeaguesByMember(Long memberId, Integer pageNum, Integer pageSize){
        //一、根据createMemberId是否查询League
        List<League> leagueLst = new ArrayList<>();
        League league = leagueRepository.findLeagueByCreateMemberId(memberId);

        if (league != null){

            leagueLst.add(league);
        }

        //二、根据memberId获取战队，然后根据战队查询对应的联盟
        //1、根据memberId获取战队
        Member member = memberRepository.findMemberById(memberId);
        List<TeamMember> teamMemberList = teamMemberRepository.findTeamMembersByMember(member);
        for (TeamMember entry : teamMemberList){
            //2、判断队员在战队中的职务,如果是普通队员，则不可以查看对应的战队
            if (entry.getJob() == MemberJobEnum.IS_TEAM_MEMBER.getJob()){
                continue;
            }

            //3、根据战队获取league
            List<LeagueTeam> leagueTeamList = leagueTeamRepository.findLeagueTeamsByTeamId(entry.getTeam().getId());
            if (leagueTeamList!=null && !leagueTeamList.isEmpty()){
                for (LeagueTeam leagueTeam : leagueTeamList){
                    if (!leagueLst.contains(leagueTeam.getLeague())){
                        leagueLst.add(leagueTeam.getLeague());
                    }
                }
            }
        }
        return leagueVoConvert.toVo(leagueLst);
    }

    /**
     *
     * @param createMemberId  创建者的memberId
     * @return true：可以创建；false不可以创建
     */
    public Boolean isHavePermission(Long createMemberId){
        Member member = memberRepository.findMemberById(createMemberId);
        List<TeamMember> teamMemberList = teamMemberRepository.findTeamMembersByMember(member);
        if (teamMemberList.isEmpty()){
            return true;
        }

        for (TeamMember teamMember : teamMemberList) {
            if (teamMember.getJob() == MemberJobEnum.IS_TEAM_LEADER.getJob()){
                return true;
            }
        }

        return false;
    }
}
