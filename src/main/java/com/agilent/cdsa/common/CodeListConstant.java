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

    public static final String INSTRUMENT_STATE_NOT_CONNECT = "NotConnect";
    public static final String INSTRUMENT_STATE_IDLE = "Idle";
    public static final String INSTRUMENT_STATE_ERROR = "Error";
    public static final String INSTRUMENT_STATE_PRERUN = "Prerun";
    public static final String INSTRUMENT_STATE_RUNNING = "Running";
    public static final String INSTRUMENT_STATE_NOT_READY = "NotReady";
    public static final String INSTRUMENT_STATE_MAINTENANCE_DUE = "MaintenanceDue";
    public static final String INSTRUMENT_STATE_SLEEP = "Sleep";
    public static final String INSTRUMENT_STATE_OFFLINE = "Offline";
    public static final String INSTRUMENT_STATE_UNKNOWN = "Unknown";

    public static final String INSTRUMENT_STATE_COLOR_NOT_CONNECT = "#23272b";
    public static final String INSTRUMENT_STATE_COLOR_IDLE = "#00ff00";
    public static final String INSTRUMENT_STATE_COLOR_ERROR = "#dc3545";
    public static final String INSTRUMENT_STATE_COLOR_PRERUN = "#32cd32";
    public static final String INSTRUMENT_STATE_COLOR_RUNNING = "#007bff";
    public static final String INSTRUMENT_STATE_COLOR_NOT_READY = "#febb07";
    public static final String INSTRUMENT_STATE_COLOR_MAINTENANCE_DUE = "#ffd700";
    public static final String INSTRUMENT_STATE_COLOR_SLEEP = "#ffd700";
    public static final String INSTRUMENT_STATE_COLOR_OFFLINE = "#343a40";
    public static final String INSTRUMENT_STATE_COLOR_UNKNOWN = "#343a40";
}
