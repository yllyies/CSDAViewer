package com.agilent.iad.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.agilent.iad.common.CodeListConstant;
import com.agilent.iad.dto.*;
import com.agilent.iad.model.Dx;
import com.agilent.iad.model.Project;
import com.agilent.iad.model.Rslt;
import com.agilent.iad.repository.DxDao;
import com.agilent.iad.repository.RsltDao;
import com.agilent.iad.service.DxService;
import com.agilent.iad.service.InstrumentStateService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lifang
 * @since 2023-07-19
 */
@Service
public class DxServiceImpl implements DxService {

    @Autowired
    private DxDao dxDao;
    @Autowired
    private RsltDao rsltDao;
    @Autowired
    private InstrumentStateService instrumentStateService;

    @Override
    public Map<String, Object> doQuery(AnalysisRequestDto analysisRequestDto) {
        // 按照视图类型进行数据组装
        Map<String, Object> result = new HashMap<>();
        Table<String, String, Long> graphMap = HashBasedTable.create();
        List<String> barLabels = new ArrayList<>(); // 柱状图x轴：仪器
        List<ChartDatasetDto> barDatasets = new ArrayList<>(); // 柱状图数据结果：数组，一个元素对应一个仪器，所包含的data:[]对应每个时间点仪器耗时
        List<String> lineLabels = new ArrayList<>(); // 线图x轴：时间
        List<ChartDatasetDto> lineDatasets = new ArrayList<>(); // 数据结果：数组，一个元素对应一个仪器，所包含的data:[]对应每个时间点仪器耗时
        List<Long> doughnutDatasets = new ArrayList<>(); // 数据结果：数组，一个元素对应一个年份，所包含的map:[]对应一个年份下所有仪器耗时
        List<TableDatasetDto> tableDatasets = new ArrayList<>(); // 数据结果：表格
        TreeMap<DateTime, DateTime> startToEndMap = new TreeMap<>();
        Map<String, Integer> dateStrToWorkHoursMap = new HashMap<>();
        // 查询所有仪器信息，用于页面
        List<RsltVo> menuInfo = rsltDao.findMenuInfo();
        List<String> instrumentNamesAll = menuInfo.stream().map(RsltVo::getInstrumentName).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        List<String> projectNamesAll = menuInfo.stream().map(RsltVo::getProjectName).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        List<String> creatorsAll = menuInfo.stream().map(RsltVo::getCreator).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        List<Integer> yearRange = menuInfo.stream().filter(rslt -> null != rslt.getCreatedDate()).map(rslt -> DateUtil.year(rslt.getCreatedDate())).distinct().sorted().collect(Collectors.toList());
        result.put("instrumentNames", instrumentNamesAll);
        result.put("yearRange", yearRange);
        result.put("projectNames", projectNamesAll);
        result.put("creators", creatorsAll);
        result.put("barLabels", barLabels);
        result.put("barDatasets", barDatasets);
        result.put("lineLabels", lineLabels);
        result.put("lineDatasets", lineDatasets);
        result.put("doughnutLabels", barLabels);
        result.put("doughnutDatasets", doughnutDatasets);
        result.put("tableDatasets", tableDatasets);
        result.put("requestParams", analysisRequestDto);
        // 无查询条件，返回菜单选项
        if (StrUtil.isBlank(analysisRequestDto.getInstrumentNames()) &&
                StrUtil.isBlank(analysisRequestDto.getProjectNames()) &&
                StrUtil.isBlank(analysisRequestDto.getCreatorNames())) {
            return result;
        }

        String yAxisUnit = analysisRequestDto.getYAxisUnit(); // TODO "TIME", "STITCH"
        // 1.处理查询参数
        if (StrUtil.isNotBlank(analysisRequestDto.getInstrumentNames())) {
            analysisRequestDto.setViewType(CodeListConstant.INSTRUMENT_VIEW);
        } else if (StrUtil.isNotBlank(analysisRequestDto.getProjectNames())) {
            analysisRequestDto.setViewType(CodeListConstant.PROJECT_VIEW);
        } else if (StrUtil.isNotBlank(analysisRequestDto.getCreatorNames())) {
            analysisRequestDto.setViewType(CodeListConstant.CREATOR_VIEW);
        }
        String timeUnit = analysisRequestDto.getTimeUnit();
        List<String> daterange = StrUtil.splitTrim(analysisRequestDto.getDaterange(), ",");

        DateTime startTime = DateUtil.beginOfYear(DateUtil.parse(daterange.get(0), "yyyy"));
        DateTime endTime = DateUtil.endOfYear(DateUtil.parse(daterange.get(daterange.size() - 1), "yyyy"));
        processDaterangeByTimeUnit(barDatasets, startToEndMap, timeUnit, new DateTime(startTime), endTime, daterange.size());
        for (DateTime key : startToEndMap.navigableKeySet()) {
            String dateFormate = formateByTimeUnit(timeUnit, key);
            dateStrToWorkHoursMap.put(dateFormate, computeWorkingDays(key, startToEndMap.get(key)) * 8);
        }
        lineLabels.addAll(startToEndMap.navigableKeySet().stream().map(time -> formateByTimeUnit(timeUnit, time)).collect(Collectors.toList()));
        // 按仪器查询
        if (CodeListConstant.INSTRUMENT_VIEW.equals(analysisRequestDto.getViewType())) {
            List<String> instrumentNames = StrUtil.splitTrim(analysisRequestDto.getInstrumentNames(), ",");
            barLabels.addAll(instrumentNames);
            List<Object[]> list = dxDao.doQueryInstrumentNames(startTime, endTime, instrumentNames);
            // TODO 处理仪器运行时间（分钟）
            /*List<InstrumentState> instrumentStates = instrumentStateService.doFindByInstrumentNameIn(instrumentNames);
            Map<String, Long> instrumentIdToCountMap = instrumentStates.stream().filter(item -> StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_RUNNING) ||
                    StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_PRERUN)).collect(Collectors.groupingBy(is -> StrUtil.toString(is.getInstrumentId()), Collectors.summingLong(InstrumentState::getInstrumentRuntime)));

            if (instrumentIdToCountMap.containsKey(instrumentDto.getInstrumentId())) {
                instrumentDto.setRuntimeString(MapUtil.getLong(instrumentIdToCountMap, instrumentDto.getInstrumentId()) + "分钟");
            } else {
                log.warn("仪器运行时间获取失败：" +  "当前仪器ID：" + instrumentDto.getInstrumentId());
            }*/
            // 收集 dateset
            for (Object[] objects : list) {
                Dx dx = (Dx) objects[0];
                Rslt rslt = (Rslt) objects[1];
                Timestamp updatedDate = dx.getUpdatedDate();
                Integer collectedTime = dx.getCollectedTime();
                for (DateTime key : startToEndMap.navigableKeySet()) {
                    String dateFormate = formateByTimeUnit(timeUnit, key);
                    if (DateUtil.isIn(updatedDate, key, startToEndMap.get(key))) {
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
        } else if (CodeListConstant.PROJECT_VIEW.equals(analysisRequestDto.getViewType())) {
            // 按项目查询
            List<String> projectNames = StrUtil.splitTrim(analysisRequestDto.getProjectNames(), ",");
            barLabels.addAll(projectNames);
            List<Object[]> list = dxDao.doQueryProjectNames(startTime, endTime, projectNames);
            // TODO 处理仪器运行时间（分钟）
            /*List<InstrumentState> instrumentStates = instrumentStateService.doFindByInstrumentNameIn(instrumentNames);
            Map<String, Long> instrumentIdToCountMap = instrumentStates.stream().filter(item -> StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_RUNNING) ||
                    StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_PRERUN)).collect(Collectors.groupingBy(is -> StrUtil.toString(is.getInstrumentId()), Collectors.summingLong(InstrumentState::getInstrumentRuntime)));

            if (instrumentIdToCountMap.containsKey(instrumentDto.getInstrumentId())) {
                instrumentDto.setRuntimeString(MapUtil.getLong(instrumentIdToCountMap, instrumentDto.getInstrumentId()) + "分钟");
            } else {
                log.warn("仪器运行时间获取失败：" +  "当前仪器ID：" + instrumentDto.getInstrumentId());
            }*/
            // 收集 dateset
            for (Object[] objects : list) {
                Dx dx = (Dx) objects[0];
                Project project = (Project) objects[2];
                Timestamp updatedDate = dx.getUpdatedDate();
                Integer collectedTime = dx.getCollectedTime();
                for (DateTime key : startToEndMap.navigableKeySet()) {
                    String dateFormate = formateByTimeUnit(timeUnit, key);
                    if (DateUtil.isIn(updatedDate, key, startToEndMap.get(key))) {
                        if (graphMap.contains(project.getName(), dateFormate)) {
                            BigDecimal add = new BigDecimal(graphMap.get(project.getName(), dateFormate)).add(new BigDecimal(collectedTime));
                            graphMap.put(project.getName(), dateFormate, add.longValue());
                        } else {
                            graphMap.put(project.getName(), dateFormate, Long.valueOf(collectedTime));
                        }
                        break;
                    }
                }
            }
        } else if (CodeListConstant.CREATOR_VIEW.equals(analysisRequestDto.getViewType())) {
            // 按执行人员查询
            // Y轴为人员：
            List<String> creatorNames = StrUtil.splitTrim(analysisRequestDto.getCreatorNames(), ",");
            barLabels.addAll(creatorNames);
            List<Object[]> list = dxDao.doQueryCreatorNames(startTime, endTime, creatorNames);
            // TODO 处理仪器运行时间（分钟）
            /*List<InstrumentState> instrumentStates = instrumentStateService.doFindByInstrumentNameIn(instrumentNames);
            Map<String, Long> instrumentIdToCountMap = instrumentStates.stream().filter(item -> StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_RUNNING) ||
                    StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_PRERUN)).collect(Collectors.groupingBy(is -> StrUtil.toString(is.getInstrumentId()), Collectors.summingLong(InstrumentState::getInstrumentRuntime)));

            if (instrumentIdToCountMap.containsKey(instrumentDto.getInstrumentId())) {
                instrumentDto.setRuntimeString(MapUtil.getLong(instrumentIdToCountMap, instrumentDto.getInstrumentId()) + "分钟");
            } else {
                log.warn("仪器运行时间获取失败：" +  "当前仪器ID：" + instrumentDto.getInstrumentId());
            }*/
            // 收集 dateset
            for (Object[] objects : list) {
                Dx dx = (Dx) objects[0];
                Rslt rslt = (Rslt) objects[1];
                Timestamp uploadedDate = dx.getUpdatedDate();
                Integer collectedTime = dx.getCollectedTime();
                for (DateTime key : startToEndMap.navigableKeySet()) {
                    String dateFormate = formateByTimeUnit(timeUnit, key);
                    if (DateUtil.isIn(uploadedDate, key, startToEndMap.get(key))) {
                        if (graphMap.contains(rslt.getCreator(), dateFormate)) {
                            BigDecimal add = new BigDecimal(graphMap.get(rslt.getCreator(), dateFormate)).add(new BigDecimal(collectedTime));
                            graphMap.put(rslt.getCreator(), dateFormate, add.longValue());
                        } else {
                            graphMap.put(rslt.getCreator(), dateFormate, Long.valueOf(collectedTime));
                        }
                        break;
                    }
                }
            }
        }
        // 根据查询结果，处理图表信息
        processData(graphMap, dateStrToWorkHoursMap, barLabels, barDatasets, lineLabels, lineDatasets, doughnutDatasets, tableDatasets);
        // 更新图表
        result.put("barLabels", barLabels);
        result.put("barDatasets", barDatasets);
        result.put("lineLabels", lineLabels);
        result.put("lineDatasets", lineDatasets);
        result.put("doughnutLabels", barLabels);
        result.put("doughnutDatasets", doughnutDatasets);
        result.put("tableDatasets", tableDatasets);
        return result;
    }

    @Override
    public AnalysisResponseDto doQuery2(AnalysisRequestDto analysisRequestDto) {
        // 按照视图类型进行数据组装
        AnalysisResponseDto result = new AnalysisResponseDto();
        Table<String, String, Long> graphMap = HashBasedTable.create();
        List<String> barLabels = new ArrayList<>(); // 柱状图x轴：仪器
        List<ChartDatasetDto> barDatasets = new ArrayList<>(); // 柱状图数据结果：数组，一个元素对应一个仪器，所包含的data:[]对应每个时间点仪器耗时
        List<String> lineLabels = new ArrayList<>(); // 线图x轴：时间
        List<ChartDatasetDto> lineDatasets = new ArrayList<>(); // 数据结果：数组，一个元素对应一个仪器，所包含的data:[]对应每个时间点仪器耗时
        List<Long> doughnutDatasets = new ArrayList<>(); // 数据结果：数组，一个元素对应一个年份，所包含的map:[]对应一个年份下所有仪器耗时
        List<TableDatasetDto> tableDatasets = new ArrayList<>(); // 数据结果：表格
        TreeMap<DateTime, DateTime> startToEndMap = new TreeMap<>();
        Map<String, Integer> dateStrToWorkHoursMap = new HashMap<>();
        // 查询所有仪器信息，用于页面
        List<RsltVo> menuInfo = rsltDao.findMenuInfo();
        List<String> instrumentNamesAll = menuInfo.stream().map(RsltVo::getInstrumentName).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        List<String> projectNamesAll = menuInfo.stream().map(RsltVo::getProjectName).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        List<String> creatorsAll = menuInfo.stream().map(RsltVo::getCreator).filter(StrUtil::isNotBlank).distinct().sorted().collect(Collectors.toList());
        List<Integer> yearRange = menuInfo.stream().filter(rslt -> null != rslt.getCreatedDate()).map(rslt -> DateUtil.year(rslt.getCreatedDate())).distinct().sorted().collect(Collectors.toList());
        result.setInstrumentNames(instrumentNamesAll);
        result.setYearRange(yearRange);
        result.setProjectNames(projectNamesAll);
        result.setCreators(creatorsAll);
        result.setBarLabels(barLabels);
        result.setBarDatasets(barDatasets);
        result.setLineLabels(lineLabels);
        result.setLineDatasets(lineDatasets);
        result.setDoughnutLabels(barLabels);
        result.setDoughnutDatasets(doughnutDatasets);
        result.setTableDatasets(tableDatasets);
        result.setRequestParams(analysisRequestDto);

        // 无查询条件，返回菜单选项
        if (StrUtil.isBlank(analysisRequestDto.getInstrumentNames()) &&
                StrUtil.isBlank(analysisRequestDto.getProjectNames()) &&
                StrUtil.isBlank(analysisRequestDto.getCreatorNames())) {
            return result;
        }

        String yAxisUnit = analysisRequestDto.getYAxisUnit(); // TODO "TIME", "STITCH"
        // 1.处理查询参数
        if (StrUtil.isNotBlank(analysisRequestDto.getInstrumentNames())) {
            analysisRequestDto.setViewType(CodeListConstant.INSTRUMENT_VIEW);
        } else if (StrUtil.isNotBlank(analysisRequestDto.getProjectNames())) {
            analysisRequestDto.setViewType(CodeListConstant.PROJECT_VIEW);
        } else if (StrUtil.isNotBlank(analysisRequestDto.getCreatorNames())) {
            analysisRequestDto.setViewType(CodeListConstant.CREATOR_VIEW);
        }
        String timeUnit = analysisRequestDto.getTimeUnit();
        List<String> daterange = StrUtil.splitTrim(analysisRequestDto.getDaterange(), ",");

        DateTime startTime = DateUtil.beginOfYear(DateUtil.parse(daterange.get(0), "yyyy"));
        DateTime endTime = DateUtil.endOfYear(DateUtil.parse(daterange.get(daterange.size() - 1), "yyyy"));
        processDaterangeByTimeUnit(barDatasets, startToEndMap, timeUnit, new DateTime(startTime), endTime, daterange.size());
        for (DateTime key : startToEndMap.navigableKeySet()) {
            String dateFormate = formateByTimeUnit(timeUnit, key);
            dateStrToWorkHoursMap.put(dateFormate, computeWorkingDays(key, startToEndMap.get(key)) * 8);
        }
        lineLabels.addAll(startToEndMap.navigableKeySet().stream().map(time -> formateByTimeUnit(timeUnit, time)).collect(Collectors.toList()));
        // 按仪器查询
        if (CodeListConstant.INSTRUMENT_VIEW.equals(analysisRequestDto.getViewType())) {
            List<String> instrumentNames = StrUtil.splitTrim(analysisRequestDto.getInstrumentNames(), ",");
            barLabels.addAll(instrumentNames);
            List<Object[]> list = dxDao.doQueryInstrumentNames(startTime, endTime, instrumentNames);
            // TODO 处理仪器运行时间（分钟）
            /*List<InstrumentState> instrumentStates = instrumentStateService.doFindByInstrumentNameIn(instrumentNames);
            Map<String, Long> instrumentIdToCountMap = instrumentStates.stream().filter(item -> StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_RUNNING) ||
                    StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_PRERUN)).collect(Collectors.groupingBy(is -> StrUtil.toString(is.getInstrumentId()), Collectors.summingLong(InstrumentState::getInstrumentRuntime)));

            if (instrumentIdToCountMap.containsKey(instrumentDto.getInstrumentId())) {
                instrumentDto.setRuntimeString(MapUtil.getLong(instrumentIdToCountMap, instrumentDto.getInstrumentId()) + "分钟");
            } else {
                log.warn("仪器运行时间获取失败：" +  "当前仪器ID：" + instrumentDto.getInstrumentId());
            }*/
            // 收集 dateset
            for (Object[] objects : list) {
                Dx dx = (Dx) objects[0];
                Rslt rslt = (Rslt) objects[1];
                Timestamp updatedDate = dx.getUpdatedDate();
                Integer collectedTime = dx.getCollectedTime();
                for (DateTime key : startToEndMap.navigableKeySet()) {
                    String dateFormate = formateByTimeUnit(timeUnit, key);
                    if (DateUtil.isIn(updatedDate, key, startToEndMap.get(key))) {
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
        } else if (CodeListConstant.PROJECT_VIEW.equals(analysisRequestDto.getViewType())) {
            // 按项目查询
            List<String> projectNames = StrUtil.splitTrim(analysisRequestDto.getProjectNames(), ",");
            barLabels.addAll(projectNames);
            List<Object[]> list = dxDao.doQueryProjectNames(startTime, endTime, projectNames);
            // TODO 处理仪器运行时间（分钟）
            /*List<InstrumentState> instrumentStates = instrumentStateService.doFindByInstrumentNameIn(instrumentNames);
            Map<String, Long> instrumentIdToCountMap = instrumentStates.stream().filter(item -> StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_RUNNING) ||
                    StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_PRERUN)).collect(Collectors.groupingBy(is -> StrUtil.toString(is.getInstrumentId()), Collectors.summingLong(InstrumentState::getInstrumentRuntime)));

            if (instrumentIdToCountMap.containsKey(instrumentDto.getInstrumentId())) {
                instrumentDto.setRuntimeString(MapUtil.getLong(instrumentIdToCountMap, instrumentDto.getInstrumentId()) + "分钟");
            } else {
                log.warn("仪器运行时间获取失败：" +  "当前仪器ID：" + instrumentDto.getInstrumentId());
            }*/
            // 收集 dateset
            for (Object[] objects : list) {
                Dx dx = (Dx) objects[0];
                Project project = (Project) objects[2];
                Timestamp updatedDate = dx.getUpdatedDate();
                Integer collectedTime = dx.getCollectedTime();
                for (DateTime key : startToEndMap.navigableKeySet()) {
                    String dateFormate = formateByTimeUnit(timeUnit, key);
                    if (DateUtil.isIn(updatedDate, key, startToEndMap.get(key))) {
                        if (graphMap.contains(project.getName(), dateFormate)) {
                            BigDecimal add = new BigDecimal(graphMap.get(project.getName(), dateFormate)).add(new BigDecimal(collectedTime));
                            graphMap.put(project.getName(), dateFormate, add.longValue());
                        } else {
                            graphMap.put(project.getName(), dateFormate, Long.valueOf(collectedTime));
                        }
                        break;
                    }
                }
            }
        } else if (CodeListConstant.CREATOR_VIEW.equals(analysisRequestDto.getViewType())) {
            // 按执行人员查询
            // Y轴为人员：
            List<String> creatorNames = StrUtil.splitTrim(analysisRequestDto.getCreatorNames(), ",");
            barLabels.addAll(creatorNames);
            List<Object[]> list = dxDao.doQueryCreatorNames(startTime, endTime, creatorNames);
            // TODO 处理仪器运行时间（分钟）
            /*List<InstrumentState> instrumentStates = instrumentStateService.doFindByInstrumentNameIn(instrumentNames);
            Map<String, Long> instrumentIdToCountMap = instrumentStates.stream().filter(item -> StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_RUNNING) ||
                    StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_PRERUN)).collect(Collectors.groupingBy(is -> StrUtil.toString(is.getInstrumentId()), Collectors.summingLong(InstrumentState::getInstrumentRuntime)));

            if (instrumentIdToCountMap.containsKey(instrumentDto.getInstrumentId())) {
                instrumentDto.setRuntimeString(MapUtil.getLong(instrumentIdToCountMap, instrumentDto.getInstrumentId()) + "分钟");
            } else {
                log.warn("仪器运行时间获取失败：" +  "当前仪器ID：" + instrumentDto.getInstrumentId());
            }*/
            // 收集 dateset
            for (Object[] objects : list) {
                Dx dx = (Dx) objects[0];
                Rslt rslt = (Rslt) objects[1];
                Timestamp uploadedDate = dx.getUpdatedDate();
                Integer collectedTime = dx.getCollectedTime();
                for (DateTime key : startToEndMap.navigableKeySet()) {
                    String dateFormate = formateByTimeUnit(timeUnit, key);
                    if (DateUtil.isIn(uploadedDate, key, startToEndMap.get(key))) {
                        if (graphMap.contains(rslt.getCreator(), dateFormate)) {
                            BigDecimal add = new BigDecimal(graphMap.get(rslt.getCreator(), dateFormate)).add(new BigDecimal(collectedTime));
                            graphMap.put(rslt.getCreator(), dateFormate, add.longValue());
                        } else {
                            graphMap.put(rslt.getCreator(), dateFormate, Long.valueOf(collectedTime));
                        }
                        break;
                    }
                }
            }
        }
        // 根据查询结果，处理图表信息
        processData(graphMap, dateStrToWorkHoursMap, barLabels, barDatasets, lineLabels, lineDatasets, doughnutDatasets, tableDatasets);
        // 更新图表信息
        result.setBarLabels(barLabels);
        result.setBarDatasets(barDatasets);
        result.setLineLabels(lineLabels);
        result.setLineDatasets(lineDatasets);
        result.setDoughnutLabels(barLabels);
        result.setDoughnutDatasets(doughnutDatasets);
        result.setTableDatasets(tableDatasets);
        return result;
    }

    private void processData(Table<String, String, Long> graphMap, Map<String, Integer> dateStrToWorkHoursMap,
                             List<String> labels, List<ChartDatasetDto> barDatasets,
                             List<String> lineLabels, List<ChartDatasetDto> lineDatasets,
                             List<Long> doughnutDatasets, List<TableDatasetDto> tableDatasets) {
        DecimalFormat df = new DecimalFormat("0.00");//设置保留位数
        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            if (!graphMap.containsRow(label)) {
                // 填入空值占位
                for (ChartDatasetDto chartDatasetDto : barDatasets) {
                    chartDatasetDto.getData().add(0L);
                }
                ChartDatasetDto chartDatasetDto = new ChartDatasetDto(label, "transparent", CodeListConstant.COLOR_LIST[i], new ArrayList<>());
                for (String date : lineLabels) {
                    tableDatasets.add(new TableDatasetDto(label, date, 0L, df.format(0)));
                    chartDatasetDto.getData().add(0L);
                }
                lineDatasets.add(chartDatasetDto);
                continue;
            }
            Map<String, Long> row = graphMap.row(label);
            // 柱状图数据处理
            for (ChartDatasetDto chartDatasetDto : barDatasets) {
                chartDatasetDto.getData().add(row.getOrDefault(chartDatasetDto.getLabel(), 0L) / 3600);
            }
            // 线性图
            ChartDatasetDto chartDatasetDto = new ChartDatasetDto(label, "transparent", CodeListConstant.COLOR_LIST[i], new ArrayList<>());
            for (String lineLabel : lineLabels) {
                var time = row.getOrDefault(lineLabel, 0L) / 3600;
                tableDatasets.add(new TableDatasetDto(label, lineLabel, time, df.format((float) time * 100 / dateStrToWorkHoursMap.get(lineLabel))));
                chartDatasetDto.getData().add(time);
            }
            lineDatasets.add(chartDatasetDto);
            // 饼状图
            Long sum = (row.values().stream().reduce(Long::sum).isPresent() ?
                    row.values().stream().reduce(Long::sum).get() : 0L) / 3600;
            doughnutDatasets.add(sum);
        }
    }

    private void processDaterangeByTimeUnit(List<ChartDatasetDto> barDatasets, TreeMap<DateTime, DateTime> startToEndMap, String timeUnit, DateTime startTime, DateTime endTime, int years) {
        switch (timeUnit) {
            case CodeListConstant.TIME_UNIT_YEAR: {
                long between = DateUtil.between(startTime, endTime, DateUnit.SECOND, false);
                while (between > 0 && !startToEndMap.containsKey(startTime)) {
                    DateTime endOffset = DateUtil.offset(startTime, DateField.YEAR, 1).offsetNew(DateField.SECOND, -1);// 2021-01-01 00:00:00
                    startToEndMap.put(new DateTime(startTime), endOffset);
                    barDatasets.add(new ChartDatasetDto(DateUtil.format(startTime, "yyyy"), CodeListConstant.COLOR_LIST[0], new ArrayList<>()));
                    if (years > 1) {
                        for (int i = 1; i < years; i++) {
                            startToEndMap.put(DateUtil.offset(startTime, DateField.YEAR, i), DateUtil.offset(endOffset, DateField.YEAR, i));
                            barDatasets.add(new ChartDatasetDto(DateUtil.format(DateUtil.offset(startTime, DateField.YEAR, i), "yyyy"), CodeListConstant.COLOR_LIST[i], new ArrayList<>()));
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
                    barDatasets.add(new ChartDatasetDto(DateUtil.format(startTime, "yyyy") + ",Q" + DateUtil.quarter(startTime), CodeListConstant.COLOR_LIST[0], new ArrayList<>()));
                    if (years > 1) {
                        for (int i = 1; i < years; i++) {
                            startToEndMap.put(DateUtil.offset(startTime, DateField.YEAR, i), DateUtil.offset(endOffset, DateField.YEAR, i));
                            barDatasets.add(new ChartDatasetDto(DateUtil.format(DateUtil.offset(startTime, DateField.YEAR, i), "yyyy") + ",Q" + DateUtil.quarter(startTime),
                                    CodeListConstant.COLOR_LIST[i], new ArrayList<>()));
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
                    barDatasets.add(new ChartDatasetDto(DateUtil.format(startTime, "yyyy-MM"), CodeListConstant.COLOR_LIST[0], new ArrayList<>()));
                    if (years > 1) {
                        for (int i = 1; i < years; i++) {
                            startToEndMap.put(DateUtil.offset(startTime, DateField.YEAR, i), DateUtil.offset(endOffset, DateField.YEAR, i));
                            barDatasets.add(new ChartDatasetDto(DateUtil.format(DateUtil.offset(startTime, DateField.YEAR, i), "yyyy-MM"), CodeListConstant.COLOR_LIST[i], new ArrayList<>()));
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

    private int computeWorkingDays(Date start, Date end) {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(start);
        endCal.setTime(end);

        int workDays = 0;

        //如果没有严格按照起始结束时间传值，在这里纠正下，可以注释掉
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(end);
            endCal.setTime(start);
        }

        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            //控制台打印出来循环情况，方便查看
            int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                /*if (!HOLIDAY_LIST.contains(current)) {
                    System.out.println(true);
                    workDays++;
                }*/
                workDays++;
            }
            startCal.add(Calendar.DATE, 1);
        }

        return workDays;
    }
}
