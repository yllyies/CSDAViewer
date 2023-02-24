package com.agilent.cdsa.common;

/**
 * @author lifang
 * @since 2019-09-05
 */
public class CodeListConstant {

    public static final String ROLE_STATUS = "ROLE_STATUS";

    public static final String ROLE_STATUS_ACTIVE = "ACTIVE";
    /**
     * 时间粒度
     **/
    public static final String TIME_UNIT_YEAR = "YEAR";
    public static final String TIME_UNIT_QUARTER = "QUARTER";
    public static final String TIME_UNIT_MONTH = "MONTH";
    /**
     * 统计视图类型
     **/
    public static final String INSTRUMENT_VIEW = "InstrumentView";
    public static final String PROJECT_VIEW = "ProjectView";
    public static final String CREATOR_VIEW = "CreatorView";

    /**
     * 柱状图颜色池
     */
    public static final String[] COLOR_LIST = {"#63B2EE", "#76DA91", "#F8CB7F", "#F89588", "#7CD6CF", "#9192AB", "#7898E1", "#EFA666", "#EDDD86", "#9987CE", "#63B2EE", "#76DA91"};

    /*Not Connect：white；
    Idle：Green；
    Error：Red；
    Pre-run：Purple；
    Running：Blue；
    Not Ready：Yellow；
    Maintenance Due：Orange；
    Sleep：Teal；*/

    public static final String INSTRUMENT_STATE_NOT_CONNECT = "Not Connect";
    public static final String INSTRUMENT_STATE_IDLE = "Idle";
    public static final String INSTRUMENT_STATE_ERROR = "Error";
    public static final String INSTRUMENT_STATE_PRE_RUN = "Pre-run";
    public static final String INSTRUMENT_STATE_RUNNING = "Running";
    public static final String INSTRUMENT_STATE_NOT_READY = "Not Ready";
    public static final String INSTRUMENT_STATE_MAINTENANCE_DUE = "Maintenance Due";
    public static final String INSTRUMENT_STATE_SLEEP = "Sleep";
    public static final String INSTRUMENT_STATE_OFFLINE = "Offline";
    public static final String INSTRUMENT_STATE_UNKNOWN = "Unknown";
}
