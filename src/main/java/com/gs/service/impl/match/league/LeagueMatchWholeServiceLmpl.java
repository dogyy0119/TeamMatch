package com.gs.service.impl.match.league;

import com.gs.convert.match.league.LeagueMatchWholeDtoConvert;
import com.gs.convert.league.LeagueVoConvert;
import com.gs.model.dto.match.league.LeagueMatchWholeDTO;
import com.gs.model.entity.jpa.db1.league.*;
import com.gs.model.entity.jpa.db1.match.league.LMWhole;
import com.gs.model.vo.league.LeagueVo;
import com.gs.repository.jpa.league.*;
import com.gs.service.intf.match.league.LeagueMatchWholeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LeagueMatchWholeServiceLmpl implements LeagueMatchWholeService {
    private LeagueRepository leagueRepository;
    private LeagueMatchWholeDtoConvert leagueMatchWholeDtoConvert;

    private LeagueVoConvert leagueVoConvert;

    @Override
    public Boolean isExistLeagueMatch(Long leagueId){
        League league = leagueRepository.findLeagueById(leagueId);
        if (league.getLeagueMatch() != null){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public LeagueVo createLeagueMatch( LeagueMatchWholeDTO leagueMatchWholeDTO){
        League league = leagueRepository.findLeagueById(leagueMatchWholeDTO.getLeagueId());

        LMWhole LMWhole = leagueMatchWholeDtoConvert.toEntity(leagueMatchWholeDTO);
        league.setLeagueMatch(LMWhole);

        return leagueVoConvert.toVo(leagueRepository.save(league));
    }

    @Override
    public LeagueVo deleteLeagueMatch(Long leagueId){
        League league = leagueRepository.findLeagueById(leagueId);
        league.setLeagueMatch(null);
        return leagueVoConvert.toVo(leagueRepository.save(league));
    }
    public LeagueVo updateLeagueMatch(Long leagueId){
        League league = leagueRepository.findLeagueById(leagueId);
        league.setLeagueMatch(null);
        return leagueVoConvert.toVo(leagueRepository.save(league));
    }



}
