package com.agilent.cdsa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.agilent.cdsa.common.CodeListConstant;
import com.agilent.cdsa.common.util.HttpUtil;
import com.agilent.cdsa.dto.InstrumentDto;
import com.agilent.cdsa.model.Dx;
import com.agilent.cdsa.model.InstrumentState;
import com.agilent.cdsa.model.Rslt;
import com.agilent.cdsa.repository.ProjectDao;
import com.agilent.cdsa.repository.RsltDao;
import com.agilent.cdsa.service.DxService;
import com.agilent.cdsa.service.InstrumentStateService;
import com.agilent.cdsa.service.RsltService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lifang
 * @since 2019-09-01
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
    @Value("${cdsa.interface-url}")
    private String instrumentInfoUrl;

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


    /*@Override
    public Page<Rslt> doFindPage(int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Rslt> pageResult = rsltDao.findAll(pageable);
        return pageResult;
    }*/

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

    @Override
    public List<InstrumentDto> doFindInstrumentsByRemote() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        JSONObject jsonObj = JSONObject.fromObject(new HashMap<>());
        HttpEntity<String> request = new HttpEntity<>(jsonObj.toString(), headers);
        String responseStr = HttpUtil.httpRequest(instrumentInfoUrl, HttpMethod.GET, request);
        if (StrUtil.isBlank(responseStr)) {
            log.warn("get data from cdsa extra program api without response -------" + responseStr);
            return new ArrayList<>();
        }
        log.info("cdsa extra program response:" + responseStr);
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(responseStr);
        InstrumentDto[] instrumentStateList = jsonObject.get("instrument_state", InstrumentDto[].class);
        List<InstrumentDto> result = Arrays.asList(instrumentStateList);
        if (CollUtil.isEmpty(result)) {
            log.warn("get data from cdsa extra program api without response");
            return new ArrayList<>();
        }
        log.info("get data from cdsa extra program api, total:" + result.size());
        // 处理仪器运行时间，收集 Prerun Running 状态的数据
        List<BigDecimal> instrumentIds = result.stream().map(InstrumentDto::getInstrumentId).map(BigDecimal::new).collect(Collectors.toList());
        List<InstrumentState> instrumentStates = instrumentStateService.doFindByIds(instrumentIds);
        Map<BigDecimal, Long> instrumentIdToCountMap = instrumentStates.stream().filter(item -> StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_RUNNING) ||
                StrUtil.equals(item.getInstrumentState(), CodeListConstant.INSTRUMENT_STATE_PRERUN)).collect(Collectors.groupingBy(InstrumentState::getInstrumentId, Collectors.mapping(InstrumentState::getInstrumentRuntime, Collectors.counting())));
        // 处理运行时间和状态颜色
        for (InstrumentDto instrumentDto : result) {
            // 处理运行时间（分钟）
            instrumentDto.setRuntimeString(MapUtil.getLong(instrumentIdToCountMap, instrumentDto.getInstrumentId()) + "分钟");
            // 处理序列运行
            instrumentDto.setSequenceInfo(instrumentDto.getCurrentSample() == null ? "" : instrumentDto.getCurrentSample() + " / " + instrumentDto.getSampleTotal());
            // 处理仪器状态颜色
            String instrumentState = instrumentDto.getInstrumentState();
            if (StrUtil.isBlank(instrumentState)) {
                continue;
            }
            switch (instrumentState) {
                case CodeListConstant.INSTRUMENT_STATE_NOT_CONNECT :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_NOT_CONNECT);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_IDLE :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_IDLE);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_ERROR :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_ERROR);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_PRERUN :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_PRERUN);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_RUNNING :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_RUNNING);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_NOT_READY :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_NOT_READY);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_MAINTENANCE_DUE :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_MAINTENANCE_DUE);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_SLEEP :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_SLEEP);
                    break;
                case CodeListConstant.INSTRUMENT_STATE_OFFLINE :
                    instrumentDto.setColor(CodeListConstant.INSTRUMENT_STATE_COLOR_OFFLINE);
                    break;
                default:
                    instrumentDto.setColor("transparent");
            }
        }
        return result;
    }
}
