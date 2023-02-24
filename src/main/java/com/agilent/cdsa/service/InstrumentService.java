package com.agilent.cdsa.service;

import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.model.PowerHistory;

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

    List<PowerHistory> doFindByIpOrderByCreatedDateDesc(String ip);

    List<PowerHistory> doFindAll();

    void doBatchCreate(List<PowerHistory> entities);

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
    List<InstrumentDto> doFindInstrumentsByPost();
}
