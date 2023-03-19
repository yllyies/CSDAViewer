package com.agilent.cdsa.service;

import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.model.PowerHistory;

import java.sql.Timestamp;
import java.util.List;

/**
 * 仪器服务接口，包括
 * <ol>
 *     <li>批量保存第三方仪器实时功率</li>
 *     <li>获取仪器实时功率，并机器学习计算仪器档位</li>
 * </ol>
 *
 * @author lifang
 * @since 2022-11-28
 */
public interface InstrumentService {

    /**
     * 批量保存
     *
     * @param entities 待保存的仪器功率状态
     */
    void doBatchSave(List<PowerHistory> entities);

    /**
     * 按照目前设定，每分钟记录一次功率，一天记录 24 * 60 = 1440 记录，除去下班，休息功率为 0 的情况，假设有 50 台仪器，每天需要产生约 25000 条数据。避免数据库数据过多，设定3天就覆盖一次数据。
     *
     * @param powerHistories 待保存的功率信息
     * @param nowDate 当前时间，取年月日时分
     */
    void doBatchReplace(List<PowerHistory> powerHistories, Timestamp nowDate);

    /**
     * 调用 小米 Python SDK，获取仪器功率
     *
     * @return 第三方仪器信息集合
     */
    List<InstrumentDto> doFindThirdPartyInstruments();

    /**
     * 从 CDSA DA WebAPI 获取所有仪器信息
     *
     * @return Agilent 仪器信息集合
     */
    List<InstrumentDto> doFindInstrumentsByRemote();
}
