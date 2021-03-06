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
     * ????????????????????????ID????????????????????????????????????
     *
     * @param createMemberId ??????????????????ID
     * @return ?????????????????????
     */
    public Boolean existsByCreateMemberId(Long createMemberId) {
        return leagueRepository.existsByCreateMemberId(createMemberId);
    }

    /**
     * @param leagueCreateDTO ??????????????????dto
     * @return team????????????Vo
     */
    public LeagueVo createLeague(LeagueCreateDTO leagueCreateDTO) {
        //??????league
        League league = leagueDtoConvert.toEntity(leagueCreateDTO);
        league = leagueRepository.save(league);

        //????????????
        Member createMember = memberRepository.findMemberById(leagueCreateDTO.getCreateMemberId());
        String teamTaskContent = createMember.getName() + " ????????? " + league.getName();
        createLeagueTeak(league.getId(), teamTaskContent);

        return leagueVoConvert.toVo(league);
    }


    /**
     * ???????????????????????????
     *
     * @param pageNum  ?????????????????????
     * @param pageSize ????????????
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
     * @param manageId ?????????Id
     * @param leagueTeamDTO LeagueTeam dto
     * @return ??????????????????
     */
    @Override
    public CodeEnum addTeam(Long manageId, LeagueTeamDTO leagueTeamDTO){

        Long leagueId = leagueTeamDTO.getLeagueId();

        Long teamId = leagueTeamDTO.getTeamId();

        //????????????????????????????????????????????????
        LeagueTeam leagueTeam = leagueTeamRepository.findLeagueTeamByLeagueIdAndTeamId(leagueId, teamId);
        if (null != leagueTeam) {
            return CodeEnum.IS_TEAM_ALLEARY_IN_LEAGUE;
        }

        //????????????
        League league = leagueRepository.findLeagueById(leagueId);
        //?????????????????????????????????????????????
        if (league.getMaxTeamNum() <= league.getLeagueTeams().size()) {
            return CodeEnum.IS_BEYOND_LIMIT_ERROR;
        }

        //??????leagueTeam
        leagueTeam = new LeagueTeam();
        leagueTeam.setLeague(league);
        leagueTeam.setTeamId(teamId);
        leagueTeam.setJoinTime(new Date());
        league.getLeagueTeams().add(leagueTeam);

        Team team = teamRepository.findTeamById(teamId);
        leagueRepository.save(league);

        //????????????
        String leagueTaskContent = team.getName() + " ???????????? " + league.getName();
        createLeagueTeak(league.getId(), leagueTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * ???????????????????????????
     *
     * @param requestId ??????Id
     * @param flg  ????????????
     * @return ??????????????????
     */
    @Override
    public CodeEnum doAddTeamRequest(Long managerId, Long requestId, Integer flg){
        if (!leagueTeamRequestRepository.existsById(requestId)){
            return CodeEnum.IS_LEAGUE_TEAM_REQUEST_NOT_EXIST;
        }

        LeagueTeamRequest leagueTeamRequest = leagueTeamRequestRepository.findLeagueTeamRequestById(requestId);
        Long leagueId = leagueTeamRequest.getLeagueId();

        Team team = teamRepository.findTeamById(leagueTeamRequest.getToTeamId());
        if (team == null ){
            return CodeEnum.IS_LEAGUE_TEAM_REQUEST_ERROR;
        }

        Member member = memberRepository.findMemberById(managerId);
        if (member == null ){
            return CodeEnum.IS_MEMBER_NOT_EXIST;
        }

        TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberAndTeam(member, team);
        if (teamMember == null){
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }
        if (teamMember.getJob()==MemberJobEnum.IS_TEAM_MEMBER.getJob()){
            return CodeEnum.IS_TEAM_LEAGUE_PERMISSION;
        }

        if (!leagueRepository.existsById(leagueId)){
            return CodeEnum.IS_LEAGUE_NOT_EXIST;
        }

        //???????????????????????????????????????????????????????????????
        if (flg == 0){
            //????????????
            String leagueTaskContent;
            leagueTaskContent = teamRepository.findTeamById(leagueTeamRequest.getToTeamId()).getName() + " ????????????????????????";

            createLeagueTeak(leagueId, leagueTaskContent);

            //??????????????????????????????????????????
            leagueTeamRequest.setStatus(3);
            leagueTeamRequestRepository.save(leagueTeamRequest);

            return CodeEnum.IS_SUCCESS;
        }

        //?????????????????????????????????????????????
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
     * @return ??????????????????
     */
    @Override
    public CodeEnum joinLeague(LeagueTeamDTO leagueTeamDTO){
        Long leagueId = leagueTeamDTO.getLeagueId();

        //????????????????????????????????????????????????
        LeagueTeam leagueTeam = leagueTeamRepository.findLeagueTeamByLeagueIdAndTeamId(leagueId, leagueTeamDTO.getTeamId());
        if (null != leagueTeam) {
            return CodeEnum.IS_TEAM_ALLEARY_IN_LEAGUE;
        }

        //????????????
        League league = leagueRepository.findLeagueById(leagueId);
        //?????????????????????????????????????????????
        if (league.getMaxTeamNum() <= league.getLeagueTeams().size()) {
            return CodeEnum.IS_BEYOND_LIMIT_ERROR;
        }

        leagueTeam = new LeagueTeam();
        leagueTeam.setLeague(league);
        leagueTeam.setTeamId(leagueTeamDTO.getTeamId());
        leagueTeam.setJoinTime(new Date());
        league.getLeagueTeams().add(leagueTeam);

         leagueRepository.save(league);

        //????????????
        Team team = teamRepository.findTeamById(leagueTeamDTO.getTeamId());
        String leagueTaskContent = team.getName() + " ???????????? " + league.getName();
        createLeagueTeak(league.getId(), leagueTaskContent);

        return CodeEnum.IS_SUCCESS;
    }
    /**
     * ????????????????????????
     *
     * @param requestId ??????Id
     * @param flg  ????????????
     * @return ??????????????????
     */
    @Override
    public CodeEnum doJoinLeagueRequest(Long managerId, Long requestId, Integer flg){
        if (!leagueRequestRepository.existsById(requestId)){
            return CodeEnum.IS_LEAGUE_REQUEST_NOT_EXIST;
        }

        LeagueRequest leagueRequest = leagueRequestRepository.findLeagueRequestById(requestId);
        Long leagueId = leagueRequest.getLeagueId();

        League league = leagueRepository.findLeagueById(leagueId);
        if (!managerId.equals(league.getCreateMemberId())){
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        //???????????????????????????????????????????????????????????????
        if (flg == 0){
            //????????????
            String teamTaskContent;
            teamTaskContent = "??????" + teamRepository.findTeamById(leagueRequest.getFromTeamId()).getName() + " ?????????????????????";

            createLeagueTeak(leagueId, teamTaskContent);

            //??????????????????????????????????????????
            leagueRequest.setStatus(3);
            leagueRequestRepository.save(leagueRequest);

            return CodeEnum.IS_SUCCESS;
        }


        //?????????????????????????????????????????????
        leagueRequest.setStatus(2);
        leagueRequestRepository.save(leagueRequest);

        LeagueTeamDTO leagueTeamDTO = new LeagueTeamDTO();
        leagueTeamDTO.setLeagueId(leagueId);
        leagueTeamDTO.setTeamId(leagueRequest.getFromTeamId());

        return joinLeague(leagueTeamDTO);
    }

    /**
     * ??????Id??????????????????
     * @param leagueId ?????? ID
     * @return ??????Vo
     */
    @Override
    public LeagueVo getLeague(Long leagueId){
        return leagueVoConvert.toVo(leagueRepository.findLeagueById(leagueId));
    }
    /**
     * ????????????
     *
     * @param manageMemberId ?????????ID
     * @param leagueTeamDTO  ??????????????????DTO
     * @return ??????????????????
     */
    @Override
    public CodeEnum deleteLeagueTeam(
            Long manageMemberId,
            LeagueTeamDTO leagueTeamDTO){

        Long leagueId = leagueTeamDTO.getLeagueId();

        League league = leagueRepository.findLeagueById(leagueId);
        if (!manageMemberId.equals(league.getCreateMemberId())){
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        //????????????????????????????????????????????????
        LeagueTeam leagueTeam = leagueTeamRepository.findLeagueTeamByLeagueIdAndTeamId(leagueId, leagueTeamDTO.getTeamId());
        if (null == leagueTeam) {
            return CodeEnum.IS_TEAM_NOT_IN_LEAGUE;
        }

        //????????????
        league.getLeagueTeams().removeIf(leagueTeam1 -> {

            if (Objects.equals(leagueTeam1.getTeamId(), leagueTeamDTO.getTeamId())) {
                return true;
            }
            return false;
        });

        leagueRepository.save(league);

        //????????????
        Member manageMember = memberRepository.findMemberById(manageMemberId);
        String manageMemberName = manageMember.getName();
        String teamTaskContent = manageMemberName + " ???????????? " + teamRepository.findTeamById(leagueTeamDTO.getTeamId()).getName();
        createLeagueTeak(leagueId, teamTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * ????????????
     *
     * @param leagueTeamDTO ????????????????????????DTO
     * @return ??????????????????
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
            return CodeEnum.IS_MEMBER_NOT_IN_TEAM;
        }

        if (teamMember.getJob()==MemberJobEnum.IS_TEAM_MEMBER.getJob()){
            return CodeEnum.IS_TEAM_LEAGUE_PERMISSION;
        }

        //????????????????????????????????????????????????
        LeagueTeam leagueTeam = leagueTeamRepository.findLeagueTeamByLeagueIdAndTeamId(leagueId, leagueTeamDTO.getTeamId());
        if (null == leagueTeam) {
            return CodeEnum.IS_TEAM_NOT_IN_LEAGUE;
        }

        //????????????
        league.getLeagueTeams().removeIf(leagueTeam1 -> {

            if (Objects.equals(leagueTeam1.getTeamId(), leagueTeamDTO.getTeamId())) {
                return true;
            }
            return false;
        });

        leagueRepository.save(league);

        //????????????
        String leagueTaskContent = teamRepository.findTeamById(leagueTeamDTO.getTeamId()).getName() + " ???????????? " + team.getName();
        createLeagueTeak(league.getId(), leagueTaskContent);

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * @param leagueId ??????Id
     * @return CodeEnum.IS_SUCCES
     */
    @Override
    public CodeEnum deleteLeague(Long managerId, Long leagueId) {

        League league = leagueRepository.findLeagueById(leagueId);
        if (!Objects.equals(league.getCreateMemberId(), managerId)){
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        leagueRepository.delete(league);

        //??????????????????????????????????????????????????????
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
     * ??????????????????
     *
     * @param manageMemberId    ?????????ID
     * @param leagueUpdateInfoDTO ??????????????????DTO
     * @return ??????????????????????????????
     */
    @Override
    public CodeEnum updateLeagueInfo(
            Long manageMemberId,
            LeagueUpdateInfoDTO leagueUpdateInfoDTO){

        Long leagueId = leagueUpdateInfoDTO.getLeagueId();
        League league = leagueRepository.findLeagueById(leagueId);

        if (!manageMemberId.equals(league.getCreateMemberId())){
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        String[] Field = {"name", "descriptionInfo", "logoUrl"};  //?????? Field??????????????????
        JpaUtil.copyNotNullPropertiesAllow(leagueUpdateInfoDTO, league, Field);

        leagueRepository.save(league);

        //????????????
        String teamTaskContent;

        if (!league.getName().equals(leagueUpdateInfoDTO.getName())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " ?????????????????????";
            createLeagueTeak(league.getId(), teamTaskContent);
        }

        if (!league.getDescriptionInfo().equals(leagueUpdateInfoDTO.getDescriptionInfo())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " ?????????????????????";
            createLeagueTeak(league.getId(), teamTaskContent);
        }


        if (!league.getLogoUrl().equals(leagueUpdateInfoDTO.getLogoUrl())){
            teamTaskContent = memberRepository.findMemberById(manageMemberId).getName() + " ???????????????logo";
            createLeagueTeak(league.getId(), teamTaskContent);
        }

        return CodeEnum.IS_SUCCESS;
    }

    /**
     * ?????????????????????????????????
     * @param key ?????????
     * @param pageNum ?????????
     * @param pageSize ?????????
     * @return ???????????????Team List
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
                 * ??????????????????, ???????????????????????????0..N???????????????
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
        //????????????createMemberId????????????League
        List<League> leagueLst = new ArrayList<>();
        League league = leagueRepository.findLeagueByCreateMemberId(memberId);

        if (league != null){
            leagueLst.add(league);
        }

        //????????????memberId??????????????????????????????????????????????????????
        //1?????????memberId????????????
        Member member = memberRepository.findMemberById(memberId);
        List<TeamMember> teamMemberList = teamMemberRepository.findTeamMembersByMember(member);
        for (TeamMember entry : teamMemberList){
            //2????????????????????????????????????,?????????????????????????????????????????????????????????
            if (entry.getJob() == MemberJobEnum.IS_TEAM_MEMBER.getJob()){
                continue;
            }

            //3?????????????????????league
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
    

}
