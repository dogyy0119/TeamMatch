package com.gs.service.impl.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.constant.enums.MemberJobEnum;
import com.gs.convert.team.TeamTaskVoConvert;
import com.gs.convert.team.TeamToVoConvert;
import com.gs.model.dto.team.TeamCreateDTO;
import com.gs.model.dto.team.TeamMemberDTO;
import com.gs.model.dto.team.TeamUpdateInfoDTO;
import com.gs.model.entity.jpa.db1.team.*;
import com.gs.model.vo.team.TeamVo;
import com.gs.repository.jpa.team.*;
import com.gs.service.intf.team.TeamService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 战队Service接口实现层
 * User: lys
 * DateTime: 2022-05-1
 **/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class TeamServiceImpl implements TeamService {

    private TeamRepository teamRepository;

    private MemberRepository memberRepository;

    private TeamMemberRepository teamMemberRepository;

    private TeamToVoConvert teamToVoConvert;

    private TeamTaskRepository teamTaskRepository;

    private TeamTaskVoConvert teamTaskVoConvert;

    private LogoRepository logoRepository;

    private TeamRequestRepository teamRequestRepository;

    private MemberRequestRepository memberRequestRepository;

    private MessageRepository messageRepository;

    /**
     * 添加队务
     * @param id teamId
     * @param content 队务内容
     * @return
     */
    private void createTeamTeak(Long id, String content){
        TeamTask teamTask = new TeamTask();
        teamTask.setTeamId(id);
        teamTask.setTeamTaskContent(content);
        teamTask.setCreateTime(new Date());

        teamTaskVoConvert.toVo(teamTaskRepository.save(teamTask));
    }

    /**
     * 根据创建者的成员ID，查看是否已经创建过战队
     *
     * @param createMemberId 创建者的成员ID
     * @return 是否创建过战队
     */
    @Override
    public Boolean existsByCreateMemberId(Long createMemberId) {
        return teamRepository.existsByCreateMemberId(createMemberId);
    }

    /**
     * 根据ID，判断是否存在对应的战队
     *
     * @param id 战队ID
     * @return 是否存在team
     */
    @Override
    public Boolean existById(Long id) {
        return teamRepository.existsById(id);
    }
    /**
     * @param createMemberId 创建战队的用户ID
     * @param teamCreateDTO  team相关输入dto
     * @return team相关输出Vo
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public TeamVo createTeam(Long createMemberId, TeamCreateDTO teamCreateDTO) {
        Team team = new Team();
        team.setName(teamCreateDTO.getName());
        team.setCreateMemberId(createMemberId);
        team.setMaxMemberNum(6);//默认值为6
        team.setCreateTime(new Date());
        team.setDescriptionInfo(teamCreateDTO.getDescriptionInfo());
        team.setLogoUrl(teamCreateDTO.getLogoUrl());

        TeamMember teamMember = new TeamMember();
        Member member = memberRepository.findMemberById(createMemberId);
        teamMember.setMember(member);
        teamMember.setTeam(team);
        teamMember.setJob(MemberJobEnum.IS_TEAM_LEADER.getJob());
        teamMember.setSilentFlg(0);
        teamMember.setJoinTime(new Date());
        team.getTeamMembers().add(teamMember);

        team = teamRepository.save(team);

        //创建队务
        String teamTaskContent = member.getName() + " 创建了 " + team.getName();
        createTeamTeak(team.getId(), teamTaskContent);

        return teamToVoConvert.toVo(team);
    }

    /**
     * 分页查询所有的战队
     *
     * @param pageNum  当前获取的页码
     * @param pageSize 每页条数
     * @return Team List
     */

    @Override
    public List<TeamVo> getTeamPage(Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Team> teamPage = teamRepository.findAll(pageable);

        List<TeamVo> teamVoList = new ArrayList<>();
        for (Team entity : teamPage) {
            teamVoList.add(teamToVoConvert.toVo(entity));
        }

        return teamVoList;
    }

    @Override
    public TeamMember findTeamMemberByTeamIdAndMemberId(Long teamId, Long memberId) {
        List<TeamMember> teamMembers = teamRepository.findTeamById(teamId).getTeamMembers();

        for (TeamMember teamMember : teamMembers) {
            if (Objects.equals(teamMember.getMember().getId(), memberId)) {
                return teamMember;
            }
        }

        return null;
    }

    /**
     * 加入战队
     *
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功添加
     */

    //@Transactional(rollbackFor = Exception.class)
    private CodeEnum joinTeam(TeamMemberDTO teamMemberDTO) {

        Long teamId = teamMemberDTO.getTeamId();

        //判断待添加的队员是否已经在战队中
        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, teamMemberDTO.getMemberId());
        if (null != teamMember) {

            log.error("joinTeam：" + "该玩家已经在该战队中");
            return CodeEnum.IS_MEMBER_ALREADY_IN_TEAM;
        }

        //添加队员
        Team team = teamRepository.findTeamById(teamId);
        //判断战队的人数是否已经达到上线
        if (team.getMaxMemberNum() <= team.getTeamMembers().size()) {
            log.error("joinTeam：" + "超出战队成员数上限");

            return CodeEnum.IS_BEYOND_LIMIT_ERROR;
        }

        Member member = memberRepository.findMemberById(teamMemberDTO.getMemberId());
        teamMember = new TeamMember();
        teamMember.setMember(member);
        teamMember.setTeam(team);
        teamMember.setJob(MemberJobEnum.IS_TEAM_MEMBER.getJob());
        teamMember.setSilentFlg(0);
        teamMember.setJoinTime(new Date());

        team.getTeamMembers().add(teamMember);
        teamRepository.save(team);

        //创建队务
        String teamTaskContent = member.getName() + " 申请加入 " + team.getName();
        createTeamTeak(team.getId(), teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 处理战队请求消息
     *
     * @param requestId 消息Id
     * @param flg  是否同意
     * @return 是否成功添加
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum doTeamRequest(
            Long requestId,
            Integer flg) {
        if (!teamRequestRepository.existsById(requestId)){
            return CodeEnum.IS_REQUEST_NOT_EXIST;
        }

        TeamRequest teamRequest = teamRequestRepository.findTeamRequestById(requestId);
        Long teamId = teamRequest.getTeamId();
        Integer type = teamRequest.getType();


        //选择拒绝后，直接生成队务，然后删除战队请求
        if (flg == 0){
            //创建队务
            String teamTaskContent;
            teamTaskContent = "拒绝" + memberRepository.findMemberById(teamRequest.getFromId()).getName() + " 加入战队的申请";

            createTeamTeak(teamId, teamTaskContent);

            //处理完毕后更新战队请求的状态
            teamRequest.setStatus(3);
            teamRequestRepository.save(teamRequest);

            return CodeEnum.IS_SUCCESS;
        }


        //添加成员之前更新战队请求的状态
        teamRequest.setStatus(2);
        teamRequestRepository.save(teamRequest);

        TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
        teamMemberDTO.setTeamId(teamRequest.getTeamId());
        teamMemberDTO.setMemberId(teamRequest.getFromId());

        return joinTeam(teamMemberDTO);
    }

    /**
     * 添加成员
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功添加
     */
    //@Transactional(rollbackFor = Exception.class)
    private CodeEnum addMember(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO) {

        Long teamId = teamMemberDTO.getTeamId();

        //校验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {

            log.error("addMember：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长和副队长有更新权限
        if (Objects.equals(MemberJobEnum.IS_TEAM_MEMBER.getJob(), manageTeamMember.getJob())) {
            log.error("addMember：" + "只有队长和副队长有更新权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION;
        }

        //判断待添加的队员是否已经在战队中
        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, teamMemberDTO.getMemberId());
        if (null != teamMember) {
            log.error("addMember：" + "该玩家已经在该战队中");
            return CodeEnum.IS_MEMBER_ALREADY_IN_TEAM;
        }

        //添加队员
        Team team = teamRepository.findTeamById(teamId);
        //判断战队的人数是否已经达到上线
        if (team.getMaxMemberNum() <= team.getTeamMembers().size()) {
            log.error("addMember：" + "超出战队成员数上限");
            return CodeEnum.IS_BEYOND_LIMIT_ERROR;
        }

        Member member = memberRepository.findMemberById(teamMemberDTO.getMemberId());
        teamMember = new TeamMember();
        teamMember.setMember(member);
        teamMember.setTeam(team);
        teamMember.setJob(MemberJobEnum.IS_TEAM_MEMBER.getJob());
        teamMember.setSilentFlg(0);
        teamMember.setJoinTime(new Date());

        team.getTeamMembers().add(teamMember);
        teamRepository.save(team);

        //创建队务
        Member manageMember = memberRepository.findMemberById(manageMemberId);
        String manageMemberName = manageMember.getName();
        String teamTaskContent = manageMemberName + " 邀请 " + member.getName() + " 加入 " + team.getName();
        createTeamTeak(team.getId(), teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 处理成员请求消息
     *
     * @param requestId 消息Id
     * @param flg  是否同意
     * @return 是否成功添加
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum doMemberRequest(
            Long requestId,
            Integer flg) {
        if (!memberRequestRepository.existsById(requestId)){
            return CodeEnum.IS_REQUEST_NOT_EXIST;
        }

        MemberRequest memberRequest = memberRequestRepository.findMemberRequestById(requestId);
        Long teamId = memberRequest.getTeamId();


        //选择拒绝后，直接生成队务，然后删除战队请求
        if (flg == 0){
            //创建队务
            String teamTaskContent;
            teamTaskContent = memberRepository.findMemberById(memberRequest.getToId()).getName() + " 拒绝了战队的邀请";

            createTeamTeak(teamId, teamTaskContent);

            //处理完毕后更新战队请求的状态
            memberRequest.setStatus(3);
            memberRequestRepository.save(memberRequest);

            return CodeEnum.IS_SUCCESS;
        }


        //添加成员之前更新战队请求的状态
        memberRequest.setStatus(2);
        memberRequestRepository.save(memberRequest);

        TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
        teamMemberDTO.setTeamId(memberRequest.getTeamId());
        teamMemberDTO.setMemberId(memberRequest.getToId());
        Long manageMemberId = memberRequest.getFromId();

        return addMember(manageMemberId, teamMemberDTO);
    }

    /**
     * 根据teamID获取Team
     *
     * @param teamId teamID
     * @return team
     */
    @Override
    public TeamVo getTeamByTeamId(Long teamId) {
        return teamToVoConvert.toVo(teamRepository.findTeamById(teamId));
    }

    /**
     * 删除成员
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功删除
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum deleteMember(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO) {

        Long teamId = teamMemberDTO.getTeamId();

        //校验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("deleteMember：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长和副队长有更新权限
        if (Objects.equals(MemberJobEnum.IS_TEAM_MEMBER.getJob(), manageTeamMember.getJob())) {
            log.error("deleteMember：" + "只有队长和副队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION;
        }

        //判断待添加的队员是否已经在战队中
        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, teamMemberDTO.getMemberId());
        if (null == teamMember) {
            log.error("deleteMember：" + "该玩家不在该战队中");
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }

        //判断待删除的队员是否是队长或者副队长
        if (Objects.equals(teamMember.getJob(), MemberJobEnum.IS_TEAM_LEADER.getJob())) {
            log.error("deleteMember：" + "待删除的队员是否是队长或者副队长");
            return CodeEnum.IS_TEAM_DELETE_ERROR;
        }

        //删除队员
        Team team = teamRepository.findTeamById(teamId);

        team.getTeamMembers().removeIf(teamMember1 -> {

            if (Objects.equals(teamMember1.getMember().getId(), teamMemberDTO.getMemberId())) {
                return true;
            }
            return false;
        });

        teamRepository.save(team);

        //创建队务
        Member manageMember = memberRepository.findMemberById(manageMemberId);
        String manageMemberName = manageMember.getName();
        String teamTaskContent = manageMemberName + " 删除队员 " + memberRepository.findMemberById(teamMemberDTO.getMemberId()).getName();
        createTeamTeak(team.getId(), teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 退出战队
     *
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功删除
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum quitTeam(TeamMemberDTO teamMemberDTO) {

        Long teamId = teamMemberDTO.getTeamId();


        //判断待删除的队员是否已经在战队中
        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, teamMemberDTO.getMemberId());
        if (null == teamMember) {
            log.error("quitTeam：" + "该玩家不在该战队中");
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }

        //判断待删除的队员是否是队长或者副队长
        if (Objects.equals(teamMember.getJob(), MemberJobEnum.IS_TEAM_LEADER.getJob())) {
            log.error("quitTeam：" + "待删除的队员是否是队长");
            return CodeEnum.IS_TEAM_DELETE_ERROR;
        }

        //删除队员
        Team team = teamRepository.findTeamById(teamId);

        team.getTeamMembers().removeIf(teamMember1 -> {

            if (Objects.equals(teamMember1.getMember().getId(), teamMemberDTO.getMemberId())) {
                return true;
            }
            return false;
        });

        teamRepository.save(team);

        //创建队务
        String teamTaskContent = memberRepository.findMemberById(teamMemberDTO.getMemberId()).getName() + " 退出战队 " + team.getName();
        createTeamTeak(team.getId(), teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 判断队员是否已经是某个战队的队长
     * @param memberId 队员id
     * @return 是否已经是某个战队的队长
     */
    private Boolean isMemberAreadyLeader(Long memberId){
        List<TeamMember> teamMembers = teamMemberRepository.findAll(new Specification<TeamMember>() {
            @Override
            public Predicate toPredicate(Root<TeamMember> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<Long> memberIdPath = root.join("member").get("id");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(memberIdPath, memberId));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });

        for (TeamMember teamMember : teamMembers){
            if (teamMember.getJob() == MemberJobEnum.IS_TEAM_LEADER.getJob()){
                return true;
            }
        }

        return false;
    }
    /**
     * 转让队长
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功转让队长
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum transferTeamLeader(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO) {

        Long teamId = teamMemberDTO.getTeamId();

        //检验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("transferTeamLeader：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长可以转让队长职务
        if (MemberJobEnum.IS_TEAM_LEADER.getJob() != manageTeamMember.getJob()) {
            log.error("transferTeamLeader：" + "只有队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION2;
        }

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, teamMemberDTO.getMemberId());
        //判断战队中是否存在待转让的队员
        if (null == teamMember) {
            log.error("transferTeamLeader：" + "该玩家不在该战队中");
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }

        //判断待转让的队员是否已经是某一个战队的队长
        if (isMemberAreadyLeader(teamMemberDTO.getMemberId())){
            log.error("transferTeamLeader：" + "该队长已经是某一个战队的队长");
            return CodeEnum.IS_ALEARDY_TEAM_LEADER;
        }
        //转让队长
        Team team = teamRepository.findTeamById(teamId);

        //遍历该战队说有成员，将原队长的职务设置为队员，将待转让的队员设置为队长
        for (TeamMember data : team.getTeamMembers()) {
            if (Objects.equals(data.getMember().getId(), manageTeamMember.getMember().getId())) {
                data.setJob(MemberJobEnum.IS_TEAM_MEMBER.getJob());
                continue;
            }

            if (Objects.equals(data.getMember().getId(), teamMemberDTO.getMemberId())) {
                data.setJob(MemberJobEnum.IS_TEAM_LEADER.getJob());
            }
        }

        teamRepository.save(team);

        //创建队务
        Member manageMember = memberRepository.findMemberById(manageMemberId);
        String manageMemberName = manageMember.getName();
        String teamTaskContent = manageMemberName + " 将队长转让给 " + memberRepository.findMemberById(teamMemberDTO.getMemberId()).getName();
        createTeamTeak(team.getId(), teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 设置第二队长
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功设置第二队长
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum setSecondTeamLeader(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO) {

        Long teamId = teamMemberDTO.getTeamId();

        //检验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("setSecondTeamLeader：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长可以设置副队长职务
        if (MemberJobEnum.IS_TEAM_LEADER.getJob() != manageTeamMember.getJob()) {
            log.error("setSecondTeamLeader：" + "只有队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION2;
        }

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, teamMemberDTO.getMemberId());
        //判断战队中是否存在待转让的队员
        if (null == teamMember) {
            log.error("setSecondTeamLeader：" + "该玩家不在该战队中");
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }

        //设置副队长
        Team team = teamRepository.findTeamById(teamId);

        //判断该战队是否存在副队长，如果存在需要先解除原来的副队长职务，再进行设置
        for (TeamMember data : team.getTeamMembers()) {
            if (Objects.equals(data.getJob(), MemberJobEnum.IS_TEAM_SECOND_LEADER.getJob())) {
                data.setJob(MemberJobEnum.IS_TEAM_MEMBER.getJob());
            }
        }

        //遍历该战队所有成员，设置副队长
        for (TeamMember data : team.getTeamMembers()) {
            if (Objects.equals(data.getMember().getId(), teamMemberDTO.getMemberId())) {
                data.setJob(MemberJobEnum.IS_TEAM_SECOND_LEADER.getJob());
                break;
            }
        }

        teamRepository.save(team);

        //创建队务
        String teamTaskContent = memberRepository.findMemberById(teamMemberDTO.getMemberId()).getName() + " 成为战队 " + team.getName() + "第二队长";
        createTeamTeak(team.getId(), teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 解除第二队长
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否成功解除第二队长
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum releaseSecondTeamLeader(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO) {

        Long teamId = teamMemberDTO.getTeamId();

        //检验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("releaseSecondTeamLeader：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长可以解除副队长职务
        if (MemberJobEnum.IS_TEAM_LEADER.getJob() != manageTeamMember.getJob()) {
            log.error("releaseSecondTeamLeader：" + "只有队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION2;
        }

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, teamMemberDTO.getMemberId());
        //判断战队中是否存在待解除副队长职务的队员
        if (null == teamMember) {
            log.error("releaseSecondTeamLeader：" + "该玩家不在该战队中");
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }

        //解除副队长
        Team team = teamRepository.findTeamById(teamId);

        for (TeamMember data : team.getTeamMembers()) {
            if (Objects.equals(data.getMember().getId(), teamMemberDTO.getMemberId()) && !Objects.equals(data.getJob(), MemberJobEnum.IS_TEAM_SECOND_LEADER.getJob())) {
                log.error("releaseSecondTeamLeader：" + "带解除职务的队员职务不正确");
                return CodeEnum.IS_TEAM_RELEASE_MEMBER_ERROR_JOB;
            }
        }

        //遍历该战队所有成员，解除副队长
        for (TeamMember data : team.getTeamMembers()) {
            if (Objects.equals(data.getMember().getId(), teamMemberDTO.getMemberId())) {
                data.setJob(MemberJobEnum.IS_TEAM_MEMBER.getJob());
                break;
            }
        }

        teamRepository.save(team);
        //创建队务
        String teamTaskContent = "解除了" + memberRepository.findMemberById(teamMemberDTO.getMemberId()).getName() + " 的第二队长职务";
        createTeamTeak(team.getId(), teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 禁言或者解除禁言
     *
     * @param manageMemberId 管理者ID
     * @param teamMemberDTO  成员相关信息DTO
     * @return 是否禁言或者解除禁言成功
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum changeSilentFlg(
            Long manageMemberId,
            TeamMemberDTO teamMemberDTO) {

        Long teamId = teamMemberDTO.getTeamId();

        //校验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("changeSilentFlg：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长和副队长有更新权限
        if (Objects.equals(MemberJobEnum.IS_TEAM_MEMBER.getJob(), manageTeamMember.getJob())) {
            log.error("changeSilentFlg：" + "只有队长和副队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION;
        }

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, teamMemberDTO.getMemberId());
        //判断战队中是否存在待禁言的队员
        if (null == teamMember) {
            log.error("changeSilentFlg：" + "该玩家不在该战队中");
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }

        //禁言或解除禁言
        Team team = teamRepository.findTeamById(teamId);

        for (TeamMember data : team.getTeamMembers()) {
            if (Objects.equals(data.getMember().getId(), teamMemberDTO.getMemberId())) {
                data.setSilentFlg(teamMemberDTO.getSilentFlg());
            }
        }

        teamRepository.save(team);

        //创建队务
        String teamTaskContent;

        if (teamMemberDTO.getSilentFlg() == 1){
            teamTaskContent = memberRepository.findMemberById(teamMemberDTO.getMemberId()).getName() + " 被禁言";
        }else{
            teamTaskContent = "解除了" + memberRepository.findMemberById(teamMemberDTO.getMemberId()).getName() + " 的禁言";
        }

        createTeamTeak(team.getId(), teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 解散战队
     *
     * @param manageMemberId 管理者ID
     * @param teamId         战队Id
     * @return 是否成功解散战队
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum releaseTeam(
            Long manageMemberId,
            Long teamId) {
        //判断战队是否存在
        if (!teamRepository.existsById(teamId)) {
            log.error("releaseTeam：" + "战队不存在");
            return CodeEnum.IS_TEAM_NOT_EXIST;
        }
        //检验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("releaseTeam：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长可以解散战队
        if (MemberJobEnum.IS_TEAM_LEADER.getJob() != manageTeamMember.getJob()) {
            log.error("releaseTeam：" + "只有队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION2;
        }

        Team team = teamRepository.findTeamById(teamId);

        if(team != null) {
            teamRepository.deleteTeamById(teamId);
        }
//        teamRepository.delete(team);

        //战队解散了，删除所有和战队相关的资源
        List<TeamTask> teamTaskList = teamTaskRepository.findTeamTasksByTeamId(teamId);
        teamTaskRepository.deleteAll(teamTaskList);

        List<Message> messageList = messageRepository.findMessagesByTeamId(teamId);
        messageRepository.deleteAll(messageList);

        List<TeamRequest> teamRequestList = teamRequestRepository.findTeamRequestsByTeamId(teamId);
        teamRequestRepository.deleteAll(teamRequestList);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 更新战队信息
     *
     * @param manageMemberId    管理者ID
     * @param teamUpdateInfoDTO 战队相关信息DTO
     * @return 是否成功更新战队名称
     */
    @Override
    public CodeEnum updateTeamInfo(
            Long manageMemberId,
            TeamUpdateInfoDTO teamUpdateInfoDTO){
        Long teamId = teamUpdateInfoDTO.getTeamId();

        //校验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("updateTeamInfo：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长和副队长有更新权限
        if (Objects.equals(MemberJobEnum.IS_TEAM_MEMBER.getJob(), manageTeamMember.getJob())) {
            log.error("updateTeamInfo：" + "只有队长和副队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION;
        }

        Team team = teamRepository.findTeamById(teamId);
        String[] Field = {"name", "descriptionInfo", "logoId"};  //更新 Field指定允许字段
        JpaUtil.copyNotNullPropertiesAllow(teamUpdateInfoDTO, team, Field);

        teamRepository.save(team);

        //创建队务
        String teamTaskContent;

        if (!teamUpdateInfoDTO.getName().isEmpty() && !team.getName().equals(teamUpdateInfoDTO.getName())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " 更改了战队名称";
            createTeamTeak(team.getId(), teamTaskContent);
        }

        if (!teamUpdateInfoDTO.getDescriptionInfo().isEmpty() && !team.getDescriptionInfo().equals(teamUpdateInfoDTO.getDescriptionInfo())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " 更改了战队简介";
            createTeamTeak(team.getId(), teamTaskContent);
        }


        if (teamUpdateInfoDTO.getLogoUrl() != null && !teamUpdateInfoDTO.getLogoUrl().isEmpty() && !team.getLogoUrl().equals(teamUpdateInfoDTO.getLogoUrl())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " 更新了战队logo";
            createTeamTeak(team.getId(), teamTaskContent);
        }


        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 更新战队名称
     *
     * @param manageMemberId    管理者ID
     * @param teamUpdateInfoDTO 战队相关信息DTO
     * @return 是否成功更新战队名称
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum updateTeamName(
            Long manageMemberId,
            TeamUpdateInfoDTO teamUpdateInfoDTO) {

        Long teamId = teamUpdateInfoDTO.getTeamId();

        //校验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("updateTeamInfo：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长和副队长有更新权限
        if (Objects.equals(MemberJobEnum.IS_TEAM_MEMBER.getJob(), manageTeamMember.getJob())) {
            log.error("updateTeamInfo：" + "只有队长和副队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION;
        }

        Team team = teamRepository.findTeamById(teamId);
        team.setName(teamUpdateInfoDTO.getName());


        teamRepository.save(team);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 更新战队成员最大数量
     *
     * @param manageMemberId    管理者ID
     * @param teamUpdateInfoDTO 战队相关信息DTO
     * @return 是否成功更新战队成员最大数量
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum updateTeamMaxMemberNum(
            @PathVariable("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {

        Long teamId = teamUpdateInfoDTO.getTeamId();

//        //判断管理者是否是队长或者或队长
//        if (!checkManagerPermission(manageMemberId, teamId)) {
//            return Boolean.error(CodeEnum.IS_PERMISSION_ERROR.getCode(), "更换战队最大成员数量:只有队长和副队长有权限进行操作");
//
//        }

        Team team = teamRepository.findTeamById(teamId);
        //team.setMaxMemberNum(teamUpdateInfoDTO.getMaxMemberNum());


        teamRepository.save(team);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * 更新战队描述信息
     *
     * @param manageMemberId    管理者ID
     * @param teamUpdateInfoDTO 战队相关信息DTO
     * @return 是否成功更新战队描述信息
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public CodeEnum updateTeamDescriptionInfo(
            @PathVariable("manageMemberId") Long manageMemberId,
            @Validated @RequestBody TeamUpdateInfoDTO teamUpdateInfoDTO) {

        Long teamId = teamUpdateInfoDTO.getTeamId();

        //校验该管理员是否在该战队中
        TeamMember manageTeamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == manageTeamMember) {
            log.error("updateTeamDescriptionInfo：" + "该管理员不在该战队中");
            return CodeEnum.IS_MANAGER_NOT_IN_TEAM;
        }

        //只有队长和副队长有更新权限
        if (Objects.equals(MemberJobEnum.IS_TEAM_MEMBER.getJob(), manageTeamMember.getJob())) {
            log.error("updateTeamDescriptionInfo：" + "只有队长和副队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION;
        }

        Team team = teamRepository.findTeamById(teamId);
        team.setDescriptionInfo(teamUpdateInfoDTO.getDescriptionInfo());


        teamRepository.save(team);

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
    public List<TeamVo> queryTeamBykey(
            Long currentMemberId,
            String key,
            Integer pageNum,
            Integer pageSize) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Team> teamPage = teamRepository.findAll(new Specification<Team>() {

            public Predicate toPredicate(Root<Team> root,
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

        List<TeamVo> teamVoList = new ArrayList<>();

        for (Team entry : teamPage) {
            if (null == findTeamMemberByTeamIdAndMemberId(entry.getId(), currentMemberId)){
                teamVoList.add(teamToVoConvert.toVo(entry));
            }
        }

        return teamVoList;
    }


}
