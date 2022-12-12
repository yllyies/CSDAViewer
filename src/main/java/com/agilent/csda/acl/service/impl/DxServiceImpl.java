package com.agilent.csda.acl.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.agilent.csda.acl.dto.AnalysisRequestDto;
import com.agilent.csda.acl.dto.ChartDatasetDto;
import com.agilent.csda.acl.dto.TableDatasetDto;
import com.agilent.csda.acl.model.Dx;
import com.agilent.csda.acl.model.Project;
import com.agilent.csda.acl.model.Rslt;
import com.agilent.csda.acl.repository.DxDao;
import com.agilent.csda.acl.repository.RsltDao;
import com.agilent.csda.acl.service.DxService;
import com.agilent.csda.common.CodeListConstant;
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
    public Map<String, Object> doQuery(AnalysisRequestDto analysisRequestDto) {
        // 按照视图类型进行数据组装
        Table<String, String, Long> graphMap = HashBasedTable.create();
        List<String> barLabels = new ArrayList<>(); // 柱状图x轴：仪器
        List<ChartDatasetDto> barDatasets = new ArrayList<>(); // 柱状图数据结果：数组，一个元素对应一个仪器，所包含的data:[]对应每个时间点仪器耗时
        List<String> lineLabels = new ArrayList<>(); // 线图x轴：时间
        List<ChartDatasetDto> lineDatasets = new ArrayList<>(); // 数据结果：数组，一个元素对应一个仪器，所包含的data:[]对应每个时间点仪器耗时
        List<Long> doughnutDatasets = new ArrayList<>(); // 数据结果：数组，一个元素对应一个年份，所包含的map:[]对应一个年份下所有仪器耗时
        List<TableDatasetDto> tableDatasets = new ArrayList<>();
        TreeMap<DateTime, DateTime> startToEndMap = new TreeMap<>();

        if (StrUtil.isBlank(analysisRequestDto.getInstrumentNames()) &&
                StrUtil.isBlank(analysisRequestDto.getProjectNames()) &&
                StrUtil.isBlank(analysisRequestDto.getAdminNames())) {
            // 查询所有仪器信息
            List<Rslt> rslts = rsltDao.findAll();
            List<String> instrumentNamesAll = rslts.stream().map(Rslt::getInstrumentName).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
            List<String> projectNamesAll = rslts.stream().map(Rslt::getProject).map(Project::getName).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
            List<String> creatorsAll = rslts.stream().map(Rslt::getCreator).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
            return new HashMap<>() {{
                put("instrumentNames", instrumentNamesAll);
                put("projectNames", projectNamesAll);
                put("creators", creatorsAll);
                put("barLabels", barLabels);// 时间标签
                put("barDatasets", barDatasets);
                put("lineLabels", lineLabels); // 线图x轴：时间
                put("lineDatasets", lineDatasets); // label 为仪器，data为[] 每个时间点对应时间
                put("doughnutLabels", barLabels);
                put("doughnutDatasets", doughnutDatasets);
                put("tableDatasets", tableDatasets);
            }};
        }

        String yAxisUnit = analysisRequestDto.getYAxisUnit(); // TODO "TIME", "STITCH"
        String timeUnit = analysisRequestDto.getTimeUnit();
        List<String> daterange = StrUtil.splitTrim(analysisRequestDto.getDaterange(), ",");
        // 仪器运行时间界面查询
        if ("InstrumentView".equals(analysisRequestDto.getViewType())) {
            List<String> instrumentNames = StrUtil.splitTrim(analysisRequestDto.getInstrumentNames(), ",");
            barLabels.addAll(instrumentNames);
            DateTime startTime = DateUtil.beginOfYear(DateUtil.parse(daterange.get(0), "yyyy"));
            DateTime endTime = DateUtil.endOfYear(DateUtil.parse(daterange.get(daterange.size() - 1), "yyyy"));
            List<Object[]> result = dxDao.doQueryCustom(startTime, endTime, instrumentNames);
            // 根据时间粒度收集需要显示的所有时间段
            processDaterangeByTimeUnit(barDatasets, startToEndMap, timeUnit, startTime, endTime, daterange.size());
            // 收集 dateset
            for (Object[] objects : result) {
                Dx dx = (Dx) objects[0];
                Rslt rslt = (Rslt) objects[1];
                Timestamp uploadedDate = dx.getUploadedDate();
                Integer collectedTime = dx.getCollectedTime();
                for (Map.Entry<DateTime, DateTime> entry : startToEndMap.entrySet()) {
                    if (DateUtil.isIn(uploadedDate, entry.getKey(), entry.getValue())) {
                        // 按照
                        String dateFormate = formateByTimeUnit(timeUnit, entry.getKey());
                        if (graphMap.contains(rslt.getInstrumentName(), dateFormate)) {
                            BigDecimal add = new BigDecimal(graphMap.get(rslt.getInstrumentName(), dateFormate)).add(new BigDecimal(collectedTime));
                            graphMap.put(rslt.getInstrumentName(), dateFormate, add.longValue());
                        } else {
                            graphMap.put(rslt.getInstrumentName(), dateFormate, Long.valueOf(collectedTime));
                        }
                        break;
                    }
                }
            }

            lineLabels.addAll(startToEndMap.navigableKeySet().stream().map(time -> formateByTimeUnit(timeUnit, time)).collect(Collectors.toList()));

            for (String instrName : instrumentNames) {
                if (!graphMap.containsRow(instrName)) {
                    // 填入空值占位
                    for (ChartDatasetDto chartDatasetDto : barDatasets) {
                        chartDatasetDto.getData().add(0L);
                    }
                    continue;
                }
                Map<String, Long> row = graphMap.row(instrName);
                for (ChartDatasetDto chartDatasetDto : barDatasets) {
                    chartDatasetDto.getData().add(row.getOrDefault(chartDatasetDto.getLabel(), RandomUtil.randomLong(180000, 720000)) / 3600);
                }
                Long sum = row.values().stream().reduce(Long::sum).isPresent() ?
                        row.values().stream().reduce(Long::sum).get():
                        (RandomUtil.randomLong(180000, 720000)) / 3600;
                doughnutDatasets.add(sum);
                // linedataset
                ChartDatasetDto chartDatasetDto = new ChartDatasetDto(instrName, "transparent", getColor(), new ArrayList<>());
                for (String date : lineLabels) {
                    var time = row.getOrDefault(date, RandomUtil.randomLong(180000, 720000)) / 3600;
                    tableDatasets.add(new TableDatasetDto(instrName, date, time, 0.0d));
                    chartDatasetDto.getData().add(row.getOrDefault(date, time));
                }
                lineDatasets.add(chartDatasetDto);
            }
        } else if ("ProjectView".equals(analysisRequestDto.getViewType())) {
            String projectNames = analysisRequestDto.getProjectNames(); // "阿司匹林,核酸因子"
            // 3. ProjectView Y轴为项目：

        } else if ("adminView".equals(analysisRequestDto.getViewType())) {
            String adminNames = analysisRequestDto.getAdminNames(); // "张三,李四"
            // 4. AdminView Y轴为人员：
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
            put("barLabels", barLabels);
            put("barDatasets", barDatasets);
            put("lineLabels", lineLabels);
            put("lineDatasets", lineDatasets);
            put("doughnutLabels", barLabels);
            put("doughnutDatasets", doughnutDatasets);
            put("tableDatasets", tableDatasets);
            put("instrumentsSelected", barLabels);
            put("daterange", daterange);
            put("timeUnit", timeUnit);
        }};
    }

    private void processDaterangeByTimeUnit(List<ChartDatasetDto> barDatasets, TreeMap<DateTime, DateTime> startToEndMap, String timeUnit, DateTime startTime, DateTime endTime, int years) {
        switch (timeUnit) {
            case CodeListConstant.TIME_UNIT_YEAR: {
                long between = DateUtil.between(startTime, endTime, DateUnit.SECOND, false);
                while (between >= 0 && !startToEndMap.containsKey(startTime)) {
                    DateTime endOffset = DateUtil.offset(startTime, DateField.YEAR, 1).offsetNew(DateField.SECOND, -1);// 2021-01-01 00:00:00
                    startToEndMap.put(new DateTime(startTime), endOffset);
                    barDatasets.add(new ChartDatasetDto(DateUtil.format(startTime, "yyyy"), getColor(), new ArrayList<>()));
                    if (years > 1) {
                        for (int i = 1; i < years; i++) {
                            startToEndMap.put(DateUtil.offset(startTime, DateField.YEAR, i), DateUtil.offset(endOffset, DateField.YEAR, i));
                            barDatasets.add(new ChartDatasetDto(DateUtil.format(DateUtil.offset(startTime, DateField.YEAR, i), "yyyy"), getColor(), new ArrayList<>()));
                        }
                    }
                    startTime.offset(DateField.YEAR, 1);// 2022-04-01 00:00:00
                    between = DateUtil.between(endOffset, endTime, DateUnit.SECOND, false);
                }


                break;
            }
            case CodeListConstant.TIME_UNIT_QUARTER: {
                long between = DateUtil.between(startTime, endTime, DateUnit.SECOND, false);
                while (between > 0 && !startToEndMap.containsKey(startTime)) {
                    DateTime endOffset = DateUtil.offset(startTime, DateField.MONTH, 3).offsetNew(DateField.SECOND, -1);// 2021-01-01 00:00:00
                    startToEndMap.put(new DateTime(startTime), endOffset);
                    barDatasets.add(new ChartDatasetDto(DateUtil.format(startTime, "yyyy") + ",Q" + DateUtil.quarter(startTime), getColor(), new ArrayList<>()));
                    if (years > 1) {
                        for (int i = 1; i < years; i++) {
                            startToEndMap.put(DateUtil.offset(startTime, DateField.YEAR, i), DateUtil.offset(endOffset, DateField.YEAR, i));
                            barDatasets.add(new ChartDatasetDto(DateUtil.format(DateUtil.offset(startTime, DateField.YEAR, i), "yyyy") + ",Q" + DateUtil.quarter(startTime), getColor(), new ArrayList<>()));
                        }
                    }
                    startTime.offset(DateField.MONTH, 3);// 2022-04-01 00:00:00
                    between = DateUtil.between(endOffset, endTime, DateUnit.SECOND, false);
                }
                break;
            }
            case CodeListConstant.TIME_UNIT_MONTH: {
                long between = DateUtil.between(startTime, endTime, DateUnit.SECOND, false);
                while (between > 0 && !startToEndMap.containsKey(startTime)) {
                    DateTime endOffset = DateUtil.offset(startTime, DateField.MONTH, 1).offsetNew(DateField.SECOND, -1);// 2021-01-01 00:00:00
                    startToEndMap.put(new DateTime(startTime), endOffset);
                    barDatasets.add(new ChartDatasetDto(DateUtil.format(startTime, "yyyy-MM"), getColor(), new ArrayList<>()));
                    if (years > 1) {
                        for (int i = 1; i < years; i++) {
                            startToEndMap.put(DateUtil.offset(startTime, DateField.YEAR, i), DateUtil.offset(endOffset, DateField.YEAR, i));
                            barDatasets.add(new ChartDatasetDto(DateUtil.format(DateUtil.offset(startTime, DateField.YEAR, i), "yyyy-MM"), getColor(), new ArrayList<>()));
                        }
                    }
                    startTime.offset(DateField.MONTH, 1);// 2022-04-01 00:00:00
                    between = DateUtil.between(endOffset, endTime, DateUnit.SECOND, false);
                }
                break;
            }
        }
    }

    private static String formateByTimeUnit(String timeUnit, DateTime dateTime) {
        String dateFormate;
        switch (timeUnit) {
            case CodeListConstant.TIME_UNIT_YEAR: {
                dateFormate = DateUtil.format(dateTime, "yyyy");
                break;
            }
            case CodeListConstant.TIME_UNIT_QUARTER: {
                dateFormate = DateUtil.format(dateTime, "yyyy") + ",Q" + DateUtil.quarter(dateTime);
                break;
            }
            case CodeListConstant.TIME_UNIT_MONTH: {
                dateFormate = DateUtil.format(dateTime, "yyyy-MM");
                break;
            }
            default: {
                dateFormate = DateUtil.format(dateTime, "yyyy-MM");
            }

        }
        return dateFormate;
    }

    //随机生成颜色代码
    private String getColor(){
        //红色
        String red;
        //绿色
        String green;
        //蓝色
        String blue;
        //生成随机对象
        Random random = new Random();
        //生成红色颜色代码
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成绿色颜色代码
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成蓝色颜色代码
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

        //判断红色代码的位数
        red = red.length()==1 ? "0" + red : red ;
        //判断绿色代码的位数
        green = green.length()==1 ? "0" + green : green ;
        //判断蓝色代码的位数
        blue = blue.length()==1 ? "0" + blue : blue ;
        //生成十六进制颜色值
        String color = "#"+red+green+blue;
        return color;
    }
}
