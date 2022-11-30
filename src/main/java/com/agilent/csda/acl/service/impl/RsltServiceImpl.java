package com.agilent.csda.acl.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.agilent.csda.acl.dao.RsltDao;
import com.agilent.csda.acl.dto.RequestDto;
import com.agilent.csda.acl.model.Dx;
import com.agilent.csda.acl.model.Rslt;
import com.agilent.csda.acl.service.RsltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
public class RsltServiceImpl implements RsltService {

    @Autowired
    private RsltDao rsltDao;

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
        return new HashMap<>(){{
            put("dataSource", dataSource);
            put("instrumentNames", instrumentNames);
            put("projectNames", projectNames);
            put("creators", creators);
            put("graphDataset", graphDataset);
        }};
    }

    @Override
    public Map<String, Object> doQuery(RequestDto requestDto) {
        List<Rslt> dataSource = rsltDao.findAll();
        Iterator<Rslt> iterator = dataSource.iterator();
        Set<String> instrumentNames = new TreeSet<>();
        Set<String> projectNames = new TreeSet<>();
        Set<String> creators = new TreeSet<>();
        Map<String, Long> graphDataset = new TreeMap<>();
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
            // 处理图表数据
            if (next.getTotalTime() != 0) {
                for (Dx dx : next.getDxList()) {
                    if (requestDto.getTimeUnit().equals("YEAR")) {
                        String key = StrUtil.subPre(dx.getUploaded(), 4);
                        if (graphDataset.containsKey(key)) {
                            BigDecimal add = new BigDecimal(graphDataset.get(key)).add(new BigDecimal(dx.getCollectedTime()));
                            graphDataset.put(key, add.longValue());
                        } else {
                            graphDataset.put(key, Long.valueOf(dx.getCollectedTime()));
                        }
                    } else if (requestDto.getTimeUnit().equals("MONTH")) {
                        String key = StrUtil.subPre(dx.getUploaded(), 7);
                        if (graphDataset.containsKey(key)) {
                            BigDecimal add = new BigDecimal(graphDataset.get(key)).add(new BigDecimal(dx.getCollectedTime()));
                            graphDataset.put(key, add.longValue());
                        } else {
                            graphDataset.put(key, Long.valueOf(dx.getCollectedTime()));
                        }
                    } else if (requestDto.getTimeUnit().equals("WEEK")) {
                        // TODO
                        String key = DateUtil.format(dx.getUploadedDate(), "yyyy");
                        if (graphDataset.containsKey(key)) {
                            BigDecimal add = new BigDecimal(graphDataset.get(key)).add(new BigDecimal(dx.getCollectedTime()));
                            graphDataset.put(key, add.longValue());
                        } else {
                            graphDataset.put(key, Long.valueOf(dx.getCollectedTime()));
                        }
                    } else {
                        String key = StrUtil.subPre(dx.getUploaded(), 10);
                        if (graphDataset.containsKey(key)) {
                            BigDecimal add = new BigDecimal(graphDataset.get(key)).add(new BigDecimal(dx.getCollectedTime()));
                            graphDataset.put(key, add.longValue());
                        } else {
                            graphDataset.put(key, Long.valueOf(dx.getCollectedTime()));
                        }
                        /*String key = DateUtil.format(dx.getUploadedDate(), "yyyy-MM-dd");
                        if (graphDataset.containsKey(key)) {
                            BigDecimal add = new BigDecimal(graphDataset.get(key)).add(new BigDecimal(dx.getCollectedTime()));
                            graphDataset.put(key, add.longValue());
                        } else {
                            graphDataset.put(key, Long.valueOf(dx.getCollectedTime()));
                        }*/
                    }
                }
            }
        }
        return new HashMap<>(){{
            put("dataSource", dataSource);
            put("instrumentNames", instrumentNames);
            put("projectNames", projectNames);
            put("creators", creators);
            put("graphDataset", graphDataset);
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
        return new HashMap<>(){{
            put("dataSource", dataSource);
            put("locations", locations);
        }};
    }
}
