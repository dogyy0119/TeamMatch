package com.gs;

import com.gs.constant.enums.MemberJobEnum;
import com.gs.model.dto.league.LeagueCreateDTO;
import com.gs.model.dto.league.LeagueTeamDTO;
import com.gs.model.dto.team.TeamCreateDTO;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.model.entity.jpa.db1.team.TeamMember;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.league.LeagueService;
import com.gs.service.intf.team.TeamService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RunApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TeamTest {

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LeagueService leagueService;

    @Test
    public void test() {

        Long leaderId = 100L;

        for (int loop = 0; loop < 30; loop++,leaderId = leaderId + 6) {
            System.out.println("leaderId:" + (leaderId));
            Team team = new Team();
            team.setName("战队" + leaderId);
            team.setCreateMemberId(leaderId);
            team.setMaxMemberNum(6);//默认值为6
            team.setCreateTime(new Date());
            team.setDescriptionInfo("这是个战队"+leaderId);
            team.setLogoUrl("url:" +leaderId);

            for (int i = 0; i < 6; i++) {
                TeamMember teamMember = new TeamMember();
                System.out.println("adddddddddddd:" + (leaderId+i));
                Member member = memberRepository.findMemberById(leaderId + i);
                teamMember.setMember(member);
                teamMember.setTeam(team);
                if (i == 0) {
                    teamMember.setJob(MemberJobEnum.IS_TEAM_LEADER.getJob());
                } else if (i == 1) {
                    teamMember.setJob(MemberJobEnum.IS_TEAM_SECOND_LEADER.getJob());
                } else {
                    teamMember.setJob(MemberJobEnum.IS_TEAM_MEMBER.getJob());
                }

                teamMember.setSilentFlg(0);
                teamMember.setJoinTime(new Date());
                team.getTeamMembers().add(teamMember);
            }

            team = teamRepository.save(team);
        }

    }

    @Test
    public void addTeamToLeague(){
        Long teamId = 269L;
        for (int i=0; i<10; i++, teamId = teamId+7){

            LeagueTeamDTO leagueTeamDTO = new LeagueTeamDTO();
            leagueTeamDTO.setLeagueId(454L);
            leagueTeamDTO.setTeamId(teamId);
            leagueService.addTeam(44L, leagueTeamDTO);
        }
    }
}
