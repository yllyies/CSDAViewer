package com.agilent.csda.phase1.service;

import com.agilent.csda.phase1.dto.InstrumentDto;
import com.agilent.csda.phase1.model.Rslt;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author lifang
 * @since 2022-11-28
 */
public interface RsltService {

    /**
     * 查询所有数据
     *
     * @return
     */
    Map<String, Object> doFindAll();

    /**
     * 分页查询
     *
     * @return
     */
    Page<Rslt> doFindPage(int pageNum, int pageSize);

    /**
     * 查询所有仪器信息
     *
     * @return
     */
    Map<String, Object> doFindInstruments();

    /**
     * 查询所有仪器信息
     *
     * @return 仪器信息集合
     */
    List<InstrumentDto> doFindInstrumentsByPost();
}
