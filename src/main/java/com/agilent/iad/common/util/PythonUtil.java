package com.agilent.iad.common.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.agilent.iad.config.XiaomiSocketConfig;
import com.agilent.iad.dto.XiaomiHumitureDto;
import com.agilent.iad.model.PowerHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Component
public class PythonUtil {
    public static final String PARAMNAME_XIAOMI_HUMITURE_DIDLIST = "xiaomi.humiture.did";
    public static final String PARAMNAME_XIAOMI_HUMITURE_IP = "xiaomi.humiture.ip";
    public static final String PARAMNAME_XIAOMI_HUMITURE_TOKEN = "xiaomi.humiture.token";
    public static final String FILENAME_HUMITURE_PYTHON = "/py/humiture.py";
    public static final String PARAMNAME_XIAOMI_INFO_PROPERTIES = "application-xiaomi-info.properties";
    public static final String FILENAME_INSTRUMENT_STATUS_PYTHON = "/py/instrument_status.py";
    public static final String FILENAME_INSTRUMENT_LEVEL_PYTHON = "/py/instrument_level.py";

    /**
     * 获取温湿度计信息
     *
     * @return 仪器功率记录集合
     */
    public static XiaomiHumitureDto doGetHumitureInfo() {
        StringBuilder message = new StringBuilder();
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties(PARAMNAME_XIAOMI_INFO_PROPERTIES);
        } catch (Exception e) {
            log.warn("获取温湿度计信息失败，请检查 application-xiaomi-info.properties 是否存在。");
            e.printStackTrace();
        }
        if (properties == null) {
            return new XiaomiHumitureDto("None", "None");
        }
        ClassPathResource classPathResource = new ClassPathResource(FILENAME_HUMITURE_PYTHON);
        String[] args = new String[]{"python", classPathResource.getAbsolutePath(), properties.getProperty(PARAMNAME_XIAOMI_HUMITURE_IP),
                properties.getProperty(PARAMNAME_XIAOMI_HUMITURE_TOKEN), properties.getProperty(PARAMNAME_XIAOMI_HUMITURE_DIDLIST)};
        PythonUtil.pythonExec(args, message);
        if (JSONUtil.isJsonObj(message.toString())) {
            JSONObject jsonObject = JSONUtil.parseObj(message.toString());
            String temperature = jsonObject.get("temperature", String.class, true);
            String humidity = jsonObject.get("humidity", String.class, true);
            if (StrUtil.isNotBlank(temperature) && StrUtil.isNotBlank(humidity)) {
                return new XiaomiHumitureDto(NumberUtil.div(temperature, "100", 1, RoundingMode.HALF_UP).toString(),
                        NumberUtil.div(humidity, "100", 1, RoundingMode.HALF_UP).toString());
            }
        }
        return new XiaomiHumitureDto("None", "None");
    }

    /**
     * 根据注册ip和token，批量获取仪器运行功率
     *
     * @param excludeNoPower 是否排除无功率的数据
     * @return 仪器功率记录集合
     */
    public static List<PowerHistory> doGetInstrumentPower(Boolean excludeNoPower) {
        StringBuilder message = new StringBuilder();
        ClassPathResource classPathResource = new ClassPathResource(FILENAME_INSTRUMENT_STATUS_PYTHON);
        String instrumentInfoList = JSONUtil.parseArray(XiaomiSocketConfig.getList()).toString();
        String[] args = new String[]{"python ", classPathResource.getAbsolutePath(), JSONUtil.quote(instrumentInfoList)};
        pythonExec(args, message);
        if (JSONUtil.isJsonArray(message.toString())) {
            JSONArray jsonArray = JSONUtil.parseArray(message.toString());
            List<PowerHistory> result = new ArrayList<>();
            for (Object obj : jsonArray) {
                if (obj == null) {
                    continue;
                }
                JSONObject jsonObject = (JSONObject) obj;
                if (excludeNoPower && StrUtil.isBlank(jsonObject.get("power", String.class))) {
                    continue;
                }
                result.add(new PowerHistory(jsonObject.get("instrumentName", String.class),
                        jsonObject.get("ip", String.class),
                        jsonObject.get("token", String.class),
                        StrUtil.isNotBlank(jsonObject.get("power", String.class)) ? Double.parseDouble(jsonObject.get("power", String.class)) : null,
                        null));
            }
            return result;
        } else {
            log.error("获取仪器信息失败，instrument_status.py返回信息：" + message);
        }
        return new ArrayList<>();
    }

    /**
     * 根据某一台仪器的功率记录，计算档位
     *
     * @param powerList 某一台仪器的功率记录
     * @return 档位
     */
    public static String doGetInstrumentPowerLevel(List<Double> powerList) {
        StringBuilder message = new StringBuilder();
        ClassPathResource classPathResource = new ClassPathResource(FILENAME_INSTRUMENT_LEVEL_PYTHON);
        String[] args = new String[]{"python", classPathResource.getAbsolutePath(), JSONUtil.quote(JSONUtil.parseArray(powerList).toString())};
        PythonUtil.pythonExec(args, message);
        return message.toString();
    }

    /**
     * 根据某一台仪器的功率记录，计算档位
     *
     * @param instrumentNameToPowerHistoriesMap key 为仪器名称， value为功率的列表
     * @return 档位
     */
    public static Map<String, JSONArray> doGetInstrumentPowerLevel(Map<String, List<Double>> instrumentNameToPowerHistoriesMap) {
        StringBuilder message = new StringBuilder();
        ClassPathResource classPathResource = new ClassPathResource(FILENAME_INSTRUMENT_LEVEL_PYTHON);
        String[] args = new String[]{"python", classPathResource.getAbsolutePath(), JSONUtil.quote(JSONUtil.parseFromMap(instrumentNameToPowerHistoriesMap).toString())};
        PythonUtil.pythonExec(args, message);
        if (StrUtil.isNotBlank(message) && JSONUtil.isJsonObj(message.toString())) {
            return JSONUtil.toBean(message.toString(), Map.class);
        }
        return new HashMap<>();
    }

    /**
     * 分流执行python脚本，避免阻塞
     *
     * @param args 执行参数
     * @param message 返回信息
     */
    private static void pythonExec(String[] args, StringBuilder message) {
        Process process = null;
        try {
            StringBuilder errorMsg = new StringBuilder();
            process = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));

            //获取进程的标准输入流
            final InputStream is1 = process.getInputStream();
            //获取进程的错误流
            final InputStream is2 = process.getErrorStream();
            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
            new Thread(() -> {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
                try {
                    String line1 = null;
                    while ((line1 = br1.readLine()) != null) {
                        message.append(line1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally{
                    try {
                        is1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                BufferedReader br2 = new  BufferedReader(new  InputStreamReader(is2));
                try {
                    String line2 = null ;
                    while ((line2 = br2.readLine()) !=  null ) {
                        errorMsg.append(line2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally{
                    try {
                        is2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            //可能导致进程阻塞，甚至死锁
            int ret = process.waitFor();
            in.close();
            if (ret == 1) {
                log.warn("调用脚本失败：" + errorMsg);
            } else {
                log.info("调用脚本成功：" + message.toString());
            }
        } catch (Exception ex){
            log.warn("获取仪器功率信息失败，请检查python脚本是否正确。" + message.toString());
            log.warn(ex.getLocalizedMessage());
        } finally {
            if (process != null) {
                try {
                    process.getErrorStream().close();
                    process.getInputStream().close();
                    process.getOutputStream().close();
                } catch (IOException ex) {
                    log.warn("关闭流失败：" + ex.getLocalizedMessage());
                }
            }
        }
    }
}
