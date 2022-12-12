package com.agilent.csda.acl.service.impl;

import cn.hutool.core.util.StrUtil;
import com.agilent.csda.acl.repository.ProjectDao;
import com.agilent.csda.acl.repository.RsltDao;
import com.agilent.csda.acl.model.Dx;
import com.agilent.csda.acl.model.Rslt;
import com.agilent.csda.acl.service.DxService;
import com.agilent.csda.acl.service.RsltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
public class RsltServiceImpl implements RsltService {

    @Autowired
    private RsltDao rsltDao;
    @Autowired
    private DxService dxService;
    @Autowired
    private ProjectDao projectDao;

    @Override
    public Map<String, Object> doFindAll() {
        List<Rslt> dataSource = rsltDao.findAll();
        Iterator<Rslt> iterator = dataSource.iterator();
        Set<String> instrumentNames = new TreeSet<>();
        Set<String> projectNames = new TreeSet<>();
        Set<String> creators = new TreeSet<>();
        Map<String, String> graphDataset = new TreeMap<>();
        while (iterator.hasNext()) {
            Rslt next = iterator.next();
            int sum = next.getDxList().stream().filter(dx -> dx.getCollectedTime() != null).mapToInt(Dx::getCollectedTime).sum();
            next.setTotalTime(sum);
            if (StrUtil.isNotBlank(next.getInstrumentName())) {
                instrumentNames.add(next.getInstrumentName());
            }
            if (null != next.getProject() && StrUtil.isNotBlank(next.getProject().getName())) {
                projectNames.add(next.getProject().getName());
            }
            if (StrUtil.isNotBlank(next.getCreator())) {
                creators.add(next.getCreator());
            }
            if (next.getTotalTime() != 0) {
                for (Dx dx : next.getDxList()) {
                    String uploaded = StrUtil.subPre(dx.getUploaded(), 10);
                    if (graphDataset.containsKey(uploaded)) {
                        graphDataset.put(uploaded, String.valueOf(dx.getCollectedTime()));
                    } else {
                        graphDataset.put(uploaded, String.valueOf(dx.getCollectedTime()));
                    }
                }
            }
        }
        return new HashMap<>() {{
            put("dataSource", dataSource);
            put("instrumentNames", instrumentNames);
            put("projectNames", projectNames);
            put("creators", creators);
            put("labels", new String[0]);
            put("barDatasets", new String[0]);
            put("lineDatasets", new String[0]);
            put("graphDataset", new String[0]);
            put("doughnutDatasets", new String[0]);
            put("tableDatasets", new String[0]);
        }};
    }


    @Override
    public Page<Rslt> doFindPage(int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Rslt> pageResult = rsltDao.findAll(pageable);
        return pageResult;
    }

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
