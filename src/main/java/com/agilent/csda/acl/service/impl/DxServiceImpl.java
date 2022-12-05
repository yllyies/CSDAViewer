package com.agilent.csda.acl.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.agilent.csda.acl.dto.ChartDatasetDto;
import com.agilent.csda.acl.dto.RequestDto;
import com.agilent.csda.acl.model.Dx;
import com.agilent.csda.acl.model.Project;
import com.agilent.csda.acl.model.Rslt;
import com.agilent.csda.acl.repository.DxDao;
import com.agilent.csda.acl.repository.RsltDao;
import com.agilent.csda.acl.service.DxService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
public class DxServiceImpl implements DxService {

    @Autowired
    private DxDao dxDao;
    @Autowired
    private RsltDao rsltDao;

    @Override
    public List<Dx> doFindBetweenDate(Date startDate, Date endDate) {
        Specification<Dx> querySpecifi = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (startDate != null) {
                //大于或等于传入时间
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate").as(Timestamp.class), startDate));
            }
            if (endDate != null) {
                //小于或等于传入时间
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate").as(Timestamp.class), endDate));
            }
            // and到一起的话所有条件就是且关系，or就是或关系
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return dxDao.findAll(querySpecifi);
    }

    @Override
    public Map<String, Object> doQuery(RequestDto requestDto) {
        // 按照视图类型进行数据组装
        // 1. DateView Y轴为时间：可以选择同比或者环比，如勾选 2021年Q1
        // 2. InstrumentView Y轴为仪器：
        // 3. ProjectView Y轴为项目：
        // 4. AdminView Y轴为人员：
        Table<String, String, Long> graphMap = HashBasedTable.create();

        Map<String, Long> graphDataset = new TreeMap<>();

        // 目前已有查询条件： 1.选择年度、季度、月度 范围；2.选择仪器；3.选择项目；4.选择人员；5.选择同比、环比；6.选择Y轴为时间或针数
        List<ChartDatasetDto> chartDatasetDtoList = new ArrayList<>();
        String instrumentNames = requestDto.getInstrumentNames(); // "ICQO1125,ICQO1186"
        String projectNames = requestDto.getProjectNames(); // "阿司匹林,核酸因子"
        String adminNames = requestDto.getAdminNames(); // "张三,李四"
        String isLinkRelative = requestDto.getIsLinkRelative();// 为环比显示则按daterange依次显示，否则将相同月份的数据放到一起
        String yAxisUnit = requestDto.getYAxisUnit(); // "TIME", "STITCH"
        // 1.数据查询逻辑：
        // 按 dx.uploaded between；
        // 按 rslt.instrument_name in， 按 rslt.project_id in，按 rslt.creator in

        // TODO 处理选择的时间，确定查询区间
        String daterange = requestDto.getDaterange(); // "2021-Q1,2021-Q2,2021-Q3"

        List<Object[]> result = dxDao.doQueryCustom(DateUtil.parseDate("2021-01-01"), DateUtil.parseDate("2021-09-30"),
                StrUtil.isBlank(instrumentNames) ? null : StrUtil.splitTrim(instrumentNames, ","));
        DateTime dateTime1 = DateUtil.parseDate("2021-01-01");
        DateTime dateTime2 = DateUtil.parseDate("2021-03-31");
        DateTime dateTime3 = DateUtil.parseDate("2021-04-01");
        DateTime dateTime4 = DateUtil.parseDate("2021-06-30");
        DateTime dateTime5 = DateUtil.parseDate("2021-07-01");
        DateTime dateTime6 = DateUtil.parseDate("2021-09-30");
        // 2.数据处理逻辑：
        // 按照视图类型进行数据组装
        // 按 同比、环比 排列数据
        // 按 时间、针数取数据
        if ("DateView".equals(requestDto.getViewType())) {
            // Y轴为时间

        } else if ("InstrumentView".equals(requestDto.getViewType())) {
            for (Object[] objects : result) {
                Dx dx = (Dx) objects[0];
                Rslt rslt = (Rslt) objects[1];
                Timestamp uploadedDate = dx.getUploadedDate();

                boolean q1 = DateUtil.isIn(uploadedDate, dateTime1, dateTime2);
                boolean q2 = DateUtil.isIn(uploadedDate, dateTime3, dateTime4);
                boolean q3 = DateUtil.isIn(uploadedDate, dateTime5, dateTime6);
                Integer collectedTime = dx.getCollectedTime();
                if (q1) {
                    if (graphMap.contains(rslt.getInstrumentName(), "2021,Q1")) {
                        BigDecimal add = new BigDecimal(graphMap.get(rslt.getInstrumentName(), "2021,Q1")).add(new BigDecimal(collectedTime));
                        graphMap.put(rslt.getInstrumentName(), "2021,Q1", add.longValue());
                    } else {
                        graphMap.put(rslt.getInstrumentName(), "2021,Q1", Long.valueOf(collectedTime));
                    }
                } else if (q2) {
                    if (graphMap.contains(rslt.getInstrumentName(), "2021,Q2")) {
                        BigDecimal add = new BigDecimal(graphMap.get(rslt.getInstrumentName(), "2021,Q2")).add(new BigDecimal(collectedTime));
                        graphMap.put(rslt.getInstrumentName(), "2021,Q2", add.longValue());
                    } else {
                        graphMap.put(rslt.getInstrumentName(), "2021,Q2", Long.valueOf(collectedTime));
                    }
                } else if (q3) {
                    if (graphMap.contains(rslt.getInstrumentName(), "2021,Q3")) {
                        BigDecimal add = new BigDecimal(graphMap.get(rslt.getInstrumentName(), "2021,Q3")).add(new BigDecimal(collectedTime));
                        graphMap.put(rslt.getInstrumentName(), "2021,Q3", add.longValue());
                    } else {
                        graphMap.put(rslt.getInstrumentName(), "2021,Q3", Long.valueOf(collectedTime));
                    }
                }
            }


            ChartDatasetDto dto1 = new ChartDatasetDto("2021,Q1", "#10A1D2", new ArrayList<>());
            ChartDatasetDto dto2 = new ChartDatasetDto("2021,Q2", "#10A1D2", new ArrayList<>());
            ChartDatasetDto dto3 = new ChartDatasetDto("2021,Q3", "#10A1D2", new ArrayList<>());

            for (String instrName : StrUtil.splitTrim(instrumentNames, ",")) {
                if (!graphMap.containsRow(instrName)) {
                    // 填入空值
                    dto1.getData().add(0L);
                    dto2.getData().add(0L);
                    dto3.getData().add(0L);
                    continue;
                }
                Map<String, Long> row = graphMap.row(instrName);
                dto1.getData().add(row.getOrDefault("2021,Q1", 0L) / 3600);
                dto2.getData().add(row.getOrDefault("2021,Q2", 0L) / 3600);
                dto3.getData().add(row.getOrDefault("2021,Q3", 0L) / 3600);
            }
            chartDatasetDtoList.add(dto1);
            chartDatasetDtoList.add(dto2);
            chartDatasetDtoList.add(dto3);
        }


        // 查询所有仪器信息
        List<Rslt> rslts = rsltDao.findAll();
        List<String> instrumentNamesAll = rslts.stream().map(Rslt::getInstrumentName).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        List<String> projectNamesAll = rslts.stream().map(Rslt::getProject).map(Project::getName).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        List<String> creatorsAll = rslts.stream().map(Rslt::getCreator).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        return new HashMap<>() {{
            put("instrumentNames", instrumentNamesAll);
            put("projectNames", projectNamesAll);
            put("creators", creatorsAll);
            put("graphDataset", graphDataset);
            put("labels", StrUtil.splitTrim(instrumentNames, ","));
            put("datasets", chartDatasetDtoList);
        }};
    }
}
