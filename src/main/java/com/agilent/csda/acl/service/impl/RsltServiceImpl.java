package com.agilent.csda.acl.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.agilent.csda.acl.repository.ProjectDao;
import com.agilent.csda.acl.repository.RsltDao;
import com.agilent.csda.acl.dto.RequestDto;
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
            put("datasets", new String[0]);
            put("graphDataset", graphDataset);
        }};
    }

    @Override
    public Map<String, Object> doQuery(RequestDto requestDto) {
        // 查询所有检测结果
//        List<Dx> dxes = dxService.doFindBetweenDate(requestDto.getStartDate(), requestDto.getEndDate());
        // 查询所有仪器信息
//        List<Rslt> rslts = rsltDao.findByNodeIdIn(dxes.stream().map(dx -> dx.getParentNodeId().longValue()).collect(Collectors.toList()));
//        List<Integer> projectIds = rslts.stream().map(rslt -> rslt.getProjectId().intValue()).distinct().collect(Collectors.toList());
//        List<Project> projects = projectDao.findByIdIn(projectIds);
//        List<String> instrumentNames = rslts.stream().map(Rslt::getInstrumentName).sorted().collect(Collectors.toList());
//        List<String> projectNames = projects.stream().map(Project::getName).sorted().collect(Collectors.toList());
//        List<String> creators = rslts.stream().map(Rslt::getCreator).sorted().collect(Collectors.toList());
//        Map<String, Long> graphDataset = new TreeMap<>();
//        dxes.stream().filter(dx -> dx.getCollectedTime() != null).collect(Collectors.groupingBy(dx -> DateUtil.format(dx.getUploadedDate(), "yyyy-MM-dd")));
//        Iterator<Rslt> iterator = rslts.iterator();
        // 按照视图类型进行数据组装
        // 1. DateView Y轴为时间：可以选择同比或者环比，如勾选 2021年Q1
        // 2. ProjectView Y轴为项目：
        // 3. AdminView Y轴为人员：

        // 目前已有查询条件： 1.选择年度、季度范围；2.选择仪器；3.选择项目；4.选择人员；5.选择同比、环比；6.选择Y轴为时间或针数

        List<Rslt> dataSource = rsltDao.findAll();
        Iterator<Rslt> iterator = dataSource.iterator();
        Set<String> instrumentNames = new TreeSet<>();
        Set<String> projectNames = new TreeSet<>();
        Set<String> creators = new TreeSet<>();
        Map<String, Long> graphDataset = new TreeMap<>();
        while (iterator.hasNext()) {
            Rslt next = iterator.next();
            int sum = next.getDxList().stream()
                    .filter(dx -> requestDto.getStartDate() == null || requestDto.getEndDate() == null || DateUtil.isIn(dx.getUploadedDate(), requestDto.getStartDate(), requestDto.getEndDate()) )
                    .filter(dx -> dx.getCollectedTime() != null).mapToInt(Dx::getCollectedTime).sum();
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
                    switch (requestDto.getTimeUnit()) {
                        case "YEAR": {
                            String key = StrUtil.subPre(dx.getUploaded(), 4);
                            if (graphDataset.containsKey(key)) {
                                BigDecimal add = new BigDecimal(graphDataset.get(key)).add(new BigDecimal(dx.getCollectedTime()));
                                graphDataset.put(key, add.longValue());
                            } else {
                                graphDataset.put(key, Long.valueOf(dx.getCollectedTime()));
                            }
                            break;
                        }
                        case "MONTH": {
                            String key = StrUtil.subPre(dx.getUploaded(), 7);
                            if (graphDataset.containsKey(key)) {
                                BigDecimal add = new BigDecimal(graphDataset.get(key)).add(new BigDecimal(dx.getCollectedTime()));
                                graphDataset.put(key, add.longValue());
                            } else {
                                graphDataset.put(key, Long.valueOf(dx.getCollectedTime()));
                            }
                            break;
                        }
                        case "WEEK": {
                            DateTime dateTime = DateUtil.beginOfWeek(dx.getUploadedDate());
                            String key = DateUtil.format(dateTime, "yyyy-MM-dd");
                            if (graphDataset.containsKey(key)) {
                                BigDecimal add = new BigDecimal(graphDataset.get(key)).add(new BigDecimal(dx.getCollectedTime()));
                                graphDataset.put(key, add.longValue());
                            } else {
                                graphDataset.put(key, Long.valueOf(dx.getCollectedTime()));
                            }
                            break;
                        }
                        default: {
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
                            break;
                        }
                    }
                }
            }
        }
        return new HashMap<>() {{
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
        return new HashMap<>() {{
            put("dataSource", dataSource);
            put("locations", locations);
        }};
    }
}
