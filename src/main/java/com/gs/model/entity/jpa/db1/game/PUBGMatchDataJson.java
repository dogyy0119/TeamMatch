package com.gs.model.entity.jpa.db1.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PUBGMatchDataJson {

    Integer DBNOs;
    Integer Assists;
    Integer Boosts;
    Double DamageDealt;
    String DeathType;
    Double HeadshotKills;
    Double Heals;
    Double KillPlace;
    Double KillStreaks;
    Double Kills;
    Double LongestKill;
    String Name;
    String PlayerId;
    Double Revives;
    Double RideDistance;
    Double RoadKills;
    Double SwimDistance;
    Double TeamKills;
    Double TimeSurvived;
    Double VehicleDestroys;
    Double WalkDistance;
    Double WeaponsAcquired;
    Integer WinPlace;
    String PubgTeamID;
    String PubgUserID;
}
