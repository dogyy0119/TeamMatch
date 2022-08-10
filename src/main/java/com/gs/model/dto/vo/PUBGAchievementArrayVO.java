package com.gs.model.dto.vo;

import com.gs.model.entity.jpa.db1.game.PUBGAchievement;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PUBGAchievementArrayVO {
    List<PUBGAchievement> TPParray;
    List<PUBGAchievement> Fpparray;
}
