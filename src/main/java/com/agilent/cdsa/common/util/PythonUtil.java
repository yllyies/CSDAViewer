package com.agilent.cdsa.common.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.agilent.cdsa.phase1.model.PowerHistory;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PythonUtil {

    public static void main(String[] args) {
        String s = PythonUtil.execPythonFileWithReturn("bigdata.py");

    }

    public static String execPythonFileWithReturn(String fileName) {
        StringBuilder message = new StringBuilder();
        try {
            ClassPathResource classPathResource = new ClassPathResource("/static/py/" + fileName);
            List<PowerHistory> requestList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                String ip = "192.168.120.44";
                String token = "19bc2330c330c048b5721195523906d7";
                PowerHistory powerHistory = new PowerHistory(UUID.randomUUID().toString(), ip, token);
                requestList.add(powerHistory);
            }
            String s1 = JSONUtil.parseArray(requestList).toString();
            String[] args = new String[] {"python", classPathResource.getAbsolutePath(), JSONUtil.quote(s1)};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                message.append(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            log.info("执行python失败");
        }
        JSONArray jsonArray = JSONUtil.parseArray(message.toString());
//        long power = jsonArray.stream().filter(item -> StrUtil.isBlank(((JSONObject) item).get("power", String.class))).count();
        return message.toString();
    }
}
