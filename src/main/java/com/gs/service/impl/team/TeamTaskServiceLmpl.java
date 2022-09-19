package com.gs.service.impl.team;

import com.gs.constant.enums.CodeEnum;
import com.gs.constant.enums.MemberJobEnum;
import com.gs.convert.team.TeamTaskVoConvert;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.entity.jpa.db1.team.TeamTask;
import com.gs.model.vo.team.TeamTaskVo;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.repository.jpa.team.TeamTaskRepository;
import com.gs.service.intf.team.TeamTaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class TeamTaskServiceLmpl implements TeamTaskService {

    private TeamTaskRepository teamTaskRepository;
    private TeamTaskVoConvert teamTaskVoConvert;

    private TeamRepository teamRepository;

    /**
     * 根据战队Id查询所有的队务
     *
     * @param teamId   战队Id
     * @param pageNum  当前页
     * @param pageSize 页容量
     * @return 符合条件得Team List
     */
    @Override
    public List<TeamTaskVo> getTeamTaskLst(
            Long teamId,
            Integer pageNum,
            Integer pageSize) {

        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<TeamTask> taskPage = teamTaskRepository.findAllByTeamId(teamId, pageable);

        List<TeamTaskVo> teamTaskVos = new ArrayList<>();

        for (TeamTask entry : taskPage) {
            teamTaskVos.add(teamTaskVoConvert.toVo(entry));
        }

        return teamTaskVos;
    }

    private TeamMember findTeamMemberByTeamIdAndMemberId(Long teamId, Long memberId) {
        List<TeamMember> teamMembers = teamRepository.findTeamById(teamId).getTeamMembers();

        for (TeamMember teamMember : teamMembers) {
            if (Objects.equals(teamMember.getMember().getId(), memberId)) {
                return teamMember;
            }
        }

        return null;
    }

    @Override
    public CodeEnum deleteOneTeamTask(Long teamId, Long manageMemberId, Long id) {

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == teamMember || teamMember.getJob() != MemberJobEnum.IS_TEAM_LEADER.getJob()) {
            log.error("deleteOneTeamTask：" + "只有队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION2;
        }

        teamTaskRepository.delete(teamTaskRepository.getById(id));

        return CodeEnum.IS_SUCCESS;
    }

    @Override
    @Transactional
    public CodeEnum deleteTeakTasks(Long teamId, Long manageMemberId) {

        TeamMember teamMember = findTeamMemberByTeamIdAndMemberId(teamId, manageMemberId);
        if (null == teamMember || teamMember.getJob() != MemberJobEnum.IS_TEAM_LEADER.getJob()) {
            log.error("deleteOneTeamTask：" + "只有队长有权限");
            return CodeEnum.IS_TEAM_UPDATE_PERMISSION2;
        }

        List<TeamTask> teamTaskList = teamTaskRepository.findTeamTasksByTeamId(teamId);
        teamTaskRepository.deleteAll(teamTaskList);

        return CodeEnum.IS_SUCCESS;

    }
}
