package com.agilent.cdsa.service.impl;

import cn.hutool.core.util.StrUtil;
import com.agilent.cdsa.model.Rslt;
import com.agilent.cdsa.repository.ProjectDao;
import com.agilent.cdsa.repository.RsltDao;
import com.agilent.cdsa.service.DxService;
import com.agilent.cdsa.service.InstrumentStateService;
import com.agilent.cdsa.service.RsltService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lifang
 * @since 2023-07-19
 */
@Service
@Slf4j
public class RsltServiceImpl implements RsltService {

    @Autowired
    private RsltDao rsltDao;
    @Autowired
    private DxService dxService;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private InstrumentStateService instrumentStateService;

    @Override
    public Map<String, Object> doFindInstruments() {
        List<Rslt> dataSource = rsltDao.findAll();
        Iterator<Rslt> iterator = dataSource.iterator();
        Set<String> locations = new TreeSet<>();
        while (iterator.hasNext()) {
            Rslt next = iterator.next();
            // 处理位置层级信息
            if (StrUtil.isNotBlank(next.getCmPath())) {
                String[] cmPathes = StrUtil.split(next.getCmPath(), "/");
                if (cmPathes.length > 2) {
                    next.setLocation(cmPathes[1]);
                    locations.add(cmPathes[1]);
                }
            }
        }
        return new HashMap<>() {{
            put("dataSource", dataSource);
            put("locations", locations);
        }};
    }
}
