package com.gs.constant.enums;

/**
 * 战队服务返回值枚举类
 * User: lys
 * DateTime: 2022-05-1
 **/
public enum CodeEnum {

    /**
     * 操作成功
     */
    IS_SUCCESS("10000", "操作成功"),
    /**
     * 操作失败
     */
    IS_FAIL("10001", "操作失败"),
    /**
     * 请求参数格式不正确
     */
    IS_PARAM("10005", "请求参数格式不正确"),
    /**
     * 数据已经存在
     */
    IS_EXIST("10006", "数据已存在"),
    /**
     * 登录已过期
     */
    IS_LOGIN_EXPIRE("10007", "登录已过期"),
    /**
     * 数据不存在
     */
    IS_NOT_EXIST("10008", "数据不存在"),
    /**
     * 数据不存在
     */
    IS_PERMISSION_ERROR("10009", "权限错误"),

    /**
     * 超出战队成员数上限
     */
    IS_BEYOND_LIMIT_ERROR("10010", "超出战队成员数上限"),

    /**
     * 玩家不存在
     */
    IS_MEMBER_NOT_EXIST("10011", "玩家不存在"),

    /**
     * 该玩家已经创建过战队
     */
    IS_ALREADY_CREATE_TEAM("10012", "该玩家已经创建过战队"),

    /**
     * 该管理员不在该战队中
     */
    IS_MANAGER_NOT_IN_TEAM("10013", "该管理员不在该战队中"),

    /**
     * 该玩家不在该战队中
     */
    IS_MEMBER_NOT_IN_TEAM("10014", "该玩家不在该战队中"),

    /**
     * 该玩家已经在该战队中
     */
    IS_MEMBER_ALREADY_IN_TEAM("10015", "该玩家已经在该战队中"),

    /**
     * 只有队长和副队长有权限
     */
    IS_TEAM_UPDATE_PERMISSION("10016", "只有队长和副队长有权限"),

    /**
     * 只有队长有权限
     */
    IS_TEAM_UPDATE_PERMISSION2("10017", "只有队长有权限"),

    /**
     * 待删除的队员是否是队长或者副队长
     */
    IS_TEAM_DELETE_ERROR("10018", "待删除的队员是否是队长"),

    /**
     * 该战队已经存在副队长
     */
    IS_TEAM_ALEARDY_EXIST_SECOND_LEADER("10019", "该战队已经存在副队长"),

    /**
     * 带解除职务的队员职务不正确
     */
    IS_TEAM_RELEASE_MEMBER_ERROR_JOB("10020", "带解除职务的队员职务不正确"),

    /**
     * 该队长已经是某一个战队的队长
     */
    IS_ALEARDY_TEAM_LEADER("10020", "该队长已经是某一个战队的队长"),
    /**
     * 战队不存在
     */
    IS_TEAM_NOT_EXIST("10021", "战队不存在"),
    /**
     * 消息不存在
     */
    IS_REQUEST_NOT_EXIST("10022", "战队请求不存在"),
    /**
     * 自己不能邀请自己
     */
    IS_REQUEST_INVITATION_OWN("10022", "自己不能邀请自己"),

    /**
     * 该玩家已经创建过联盟
     */
    IS_ALREADY_CREATE_LEAGUE("10023", "该玩家已经创建过联盟"),
    /**
     * 只有队长和副队长有权限查看联盟
     */
    IS_TEAM_LEAGUE_PERMISSION("10024", "只有队长和副队长有权限查看联盟"),

    /**
     * 联盟不存在
     */
    IS_LEAGUE_NOT_EXIST("10025", "联盟不存在"),

    /**
     * 只有联盟创建者有权限
     */
    IS_LEAGUE_PERMISSION_ERROR("10026", "只有联盟创建者有权限"),

    /**
     * 战队已经在联盟里
     */
    IS_TEAM_ALLEARY_IN_LEAGUE("10027", "战队已经在联盟里"),
    /**
     * 战队不在联盟里
     */
    IS_TEAM_NOT_IN_LEAGUE("10028", "战队不在联盟里"),
    /**
     * 联盟请求不存在
     */
    IS_LEAGUE_REQUEST_NOT_EXIST("10029", "联盟请求不存在"),

    /**
     * 联盟战队请求不存在
     */
    IS_LEAGUE_TEAM_REQUEST_NOT_EXIST("10030", "联盟战队请求不存在"),

    /**
     * 联盟战队请求错误
     */
    IS_LEAGUE_TEAM_REQUEST_ERROR("10031", "联盟战队请求错误"),

    /**
     * 不存在该联盟快递信息
     */
    IS_LEAGUE_TASK_NOT_EXIST("10032", "不存在该联盟快递信息"),
    /**
     * 系统异常
     */
    IS_EXCEPTION("99999", "系统异常");

    private String code;
    private String value;

    CodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
