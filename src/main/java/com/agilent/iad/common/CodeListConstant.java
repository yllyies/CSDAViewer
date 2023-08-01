package com.agilent.iad.common;

/**
 * @author lifang
 * @since 22023-07-19
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
    public static final String TIME_UNIT_WEEK = "WEEK";
    public static final String TIME_UNIT_DAY = "DAY";
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
    public static final String INSTRUMENT_STATE_PRERUN = "PreRun";
    public static final String INSTRUMENT_STATE_RUNNING = "Running";
    public static final String INSTRUMENT_STATE_NOT_READY = "NotReady";
    public static final String INSTRUMENT_STATE_MAINTENANCE_DUE = "MaintenanceDue";
    public static final String INSTRUMENT_STATE_SLEEP = "Sleep";
    public static final String INSTRUMENT_STATE_OFFLINE = "Offline";
    public static final String INSTRUMENT_STATE_UNKNOWN = "Unknown";

    public static final String INSTRUMENT_STATE_COLOR_NOT_CONNECT = "#a9a9a9";
    public static final String INSTRUMENT_STATE_COLOR_IDLE = "#00ff00";
    public static final String INSTRUMENT_STATE_COLOR_ERROR = "#dc3545";
    public static final String INSTRUMENT_STATE_COLOR_PRERUN = "#007bff";
    public static final String INSTRUMENT_STATE_COLOR_RUNNING = "#007bff";
    public static final String INSTRUMENT_STATE_COLOR_NOT_READY = "#febb07";
    public static final String INSTRUMENT_STATE_COLOR_MAINTENANCE_DUE = "#a9a9a9";
    public static final String INSTRUMENT_STATE_COLOR_SLEEP = "#a9a9a9";
    public static final String INSTRUMENT_STATE_COLOR_OFFLINE = "#a9a9a9";
    public static final String INSTRUMENT_STATE_COLOR_UNKNOWN = "transparent";

    /**
     * 空值
     */
    public static final String NONE = "None";

    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
}
