package com.gs.constant.enums;

/**
 * 战队成员的职务枚举类
 * User: lys
 * DateTime: 2022-05-1
 **/

public enum MemberJobEnum {
    /**
     * 队长
     */
    IS_TEAM_LEADER(1),

    /**
     * 副队长
     */
    IS_TEAM_SECOND_LEADER(2),

    /**
     * 组员
     */
    IS_TEAM_MEMBER(3);


    private Integer job;

    MemberJobEnum(Integer job) {
        this.job = job;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }
}
