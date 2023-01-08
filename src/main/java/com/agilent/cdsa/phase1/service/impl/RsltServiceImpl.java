package com.agilent.cdsa.phase1.service.impl;

import cn.hutool.core.util.StrUtil;
import com.agilent.cdsa.common.util.HttpUtil;
import com.agilent.cdsa.phase1.dto.InstrumentDto;
import com.agilent.cdsa.phase1.model.Dx;
import com.agilent.cdsa.phase1.model.Rslt;
import com.agilent.cdsa.phase1.repository.ProjectDao;
import com.agilent.cdsa.phase1.repository.RsltDao;
import com.agilent.cdsa.phase1.service.DxService;
import com.agilent.cdsa.phase1.service.RsltService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    public List<InstrumentDto> doFindInstrumentsByPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        JSONObject jsonObj = JSONObject.fromObject(new HashMap<>());
        HttpEntity<String> request = new HttpEntity<>(jsonObj.toString(), headers);
        List<InstrumentDto> result = HttpUtil.httpRequest(instrumentInfoUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<InstrumentDto>>(){});
        return result;
    }
}
