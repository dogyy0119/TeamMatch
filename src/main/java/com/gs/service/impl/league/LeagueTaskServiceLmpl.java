package com.gs.service.impl.league;

import com.gs.constant.enums.CodeEnum;
import com.gs.constant.enums.MemberJobEnum;
import com.gs.convert.league.LeagueTaskVoConvert;
import com.gs.convert.league.LeagueTeamRequestVoConvert;
import com.gs.convert.team.TeamTaskVoConvert;
import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.entity.jpa.db1.league.LeagueTask;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.model.entity.jpa.db1.team.TeamTask;
import com.gs.model.vo.league.LeagueTaskVo;
import com.gs.model.vo.team.TeamTaskVo;
import com.gs.repository.jpa.league.LeagueRepository;
import com.gs.repository.jpa.league.LeagueTaskRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.repository.jpa.team.TeamTaskRepository;
import com.gs.service.intf.league.LeagueTaskService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class LeagueTaskServiceLmpl implements LeagueTaskService {

    private LeagueTaskRepository leagueTaskRepository;
    private LeagueTaskVoConvert leagueTaskVoConvert;

    private LeagueRepository leagueRepository;

    public boolean existById(Long leagueId){
        return leagueTaskRepository.existsById(leagueId);
    }

    /**
     * 根据联盟Id查询所有的队务
     *
     * @param leagueId   联盟Id
     * @param pageNum  当前页
     * @param pageSize 页容量
     * @return 符合条件得Team List
     */
    @Override
    public List<LeagueTaskVo> getLeagueTaskLst(
            Long leagueId,
            Integer pageNum,
            Integer pageSize) {

        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<LeagueTask> taskPage = leagueTaskRepository.findAllByLeagueId(leagueId, pageable);

        List<LeagueTaskVo> leagueTaskVoList = new ArrayList<>();

        for (LeagueTask entry : taskPage) {
            leagueTaskVoList.add(leagueTaskVoConvert.toVo(entry));
        }

        return leagueTaskVoList;
    }

    @Override
    public CodeEnum deleteOneLeagueTask(Long leagueId, Long manageMemberId, Long id) {

        League league = leagueRepository.findLeagueById(leagueId);
        if (null == league || !Objects.equals(league.getCreateMemberId(), manageMemberId)) {

            log.error("deleteOneLeagueTask：" + "只有联盟创建者有权限");
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }

        leagueTaskRepository.delete(leagueTaskRepository.getById(id));

        return CodeEnum.IS_SUCCESS;
    }

    @Override
    @Transactional
    public CodeEnum deleteLeagueTasks(Long leagueId, Long manageMemberId) {

        League league = leagueRepository.findLeagueById(leagueId);
        if (null == league || !Objects.equals(league.getCreateMemberId(), manageMemberId)) {

            log.error("deleteLeagueTasks：" + "只有联盟创建者有权限");
            return CodeEnum.IS_LEAGUE_PERMISSION_ERROR;
        }


        List<LeagueTask> leagueTaskList = leagueTaskRepository.findLeagueTasksByLeagueId(leagueId);
        leagueTaskRepository.deleteAll(leagueTaskList);

        return CodeEnum.IS_SUCCESS;

    }
}
