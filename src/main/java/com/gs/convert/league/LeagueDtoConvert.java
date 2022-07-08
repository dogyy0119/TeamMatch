package com.gs.convert.league;


import com.gs.model.dto.league.LeagueCreateDTO;
import com.gs.model.entity.jpa.db1.league.League;
import com.gs.repository.jpa.league.LeagueRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Mapper(componentModel = "spring")
public class LeagueDtoConvert {

    @Autowired
    protected LeagueRepository leagueRepository;

    //@Mapping(target = "pubgTeam", expression = "java( pubgTeamRepository.findById(dto.getPubgTeamId()).get() )")
    public League toEntity (LeagueCreateDTO dto){
        if (dto == null) {
            return null;
        } else {
            League league = new League();
            league.setName(dto.getName());
            league.setCreateMemberId(dto.getCreateMemberId());
            league.setCreateTime(new Date());
            league.setMaxTeamNum(dto.getMaxTeamNum());
            league.setLogoUrl(dto.getLogoUrl());
            league.setDescriptionInfo(dto.getDescriptionInfo());

            return league;
        }
    }

    public List<League> toEntity(List<LeagueCreateDTO> dtos) {
        if (dtos == null) {
            return null;
        } else {
            List<League> list = new ArrayList(dtos.size());
            Iterator var3 = dtos.iterator();

            while(var3.hasNext()) {
                LeagueCreateDTO leagueCreateDTO = (LeagueCreateDTO)var3.next();
                list.add(this.toEntity(leagueCreateDTO));
            }

            return list;
        }
    }
}
