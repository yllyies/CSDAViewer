package com.agilent.cdsa.common.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.agilent.cdsa.config.XiaomiSocketConfig;
import com.agilent.cdsa.dto.XiaomiHumitureDto;
import com.agilent.cdsa.model.PowerHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class PythonUtil {
    public static final String PARAMNAME_XIAOMI_HUMITURE_DIDLIST = "xiaomi.humiture.did";
    public static final String PARAMNAME_XIAOMI_HUMITURE_IP = "xiaomi.humiture.ip";
    public static final String PARAMNAME_XIAOMI_HUMITURE_TOKEN = "xiaomi.humiture.token";
    public static final String FILENAME_HUMITURE_PYTHON = "/static/py/humiture.py";
    public static final String PARAMNAME_XIAOMI_INFO_PROPERTIES = "application-xiaomi-info.properties";

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
//            throw new BusinessException("获取温湿度计信息失败，请检查 application-xiaomi-info.properties 是否存在。");
        }
        try {
            ClassPathResource classPathResource = new ClassPathResource(FILENAME_HUMITURE_PYTHON);

            String[] args = new String[]{"python", classPathResource.getAbsolutePath(), properties.getProperty(PARAMNAME_XIAOMI_HUMITURE_IP),
                    properties.getProperty(PARAMNAME_XIAOMI_HUMITURE_TOKEN), properties.getProperty(PARAMNAME_XIAOMI_HUMITURE_DIDLIST)};

            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                message.append(line);
            }
            in.close();
            proc.waitFor();
        } catch (Exception e) {
            log.warn("获取温湿度计信息失败，请检查 humiture.py及配置的小米账号密码是否正确。");
            e.printStackTrace();
//            throw new BusinessException("获取温湿度计信息失败，请检查 humiture.py及配置的小米账号密码是否正确。");
        }
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
     * @return 仪器功率记录集合
     */
    public static List<PowerHistory> doGetInstrumentPower() {
        StringBuilder message = new StringBuilder();
        ClassPathResource classPathResource = new ClassPathResource("/static/py/instrument_status.py");
        String instrumentInfoList = JSONUtil.parseArray(XiaomiSocketConfig.getList()).toString();
        String[] args = new String[]{"python", classPathResource.getAbsolutePath(), instrumentInfoList};
        pythonExec(args, message);
        if (JSONUtil.isJsonArray(message.toString())) {
            JSONArray jsonArray = JSONUtil.parseArray(message.toString());
            List<PowerHistory> result = new ArrayList<>();
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                if (null != jsonObject && StrUtil.isNotBlank(jsonObject.get("power", String.class))) {
                    result.add(new PowerHistory(jsonObject.get("instrumentName", String.class), jsonObject.get("ip", String.class),
                            jsonObject.get("token", String.class), Double.parseDouble(jsonObject.get("power", String.class)), null));
                }
            }
            return result;
        } else {
            log.info("获取仪器信息失败，instrument_status.py返回信息：" + message);
        }
        return new ArrayList<>();
    }

    private static void pythonExec(String[] args, StringBuilder message) {
        Process process = null;
        try {
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
                        message.append(line2);
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
                log.info("调用脚本失败");
            } else {
                log.info("调用脚本成功");
            }
        } catch (Exception ex){
            log.warn("获取仪器功率信息失败，请检查python脚本是否正确。");
            ex.printStackTrace();
        } finally {
            if (process != null) {
                try {
                    process.getErrorStream().close();
                    process.getInputStream().close();
                    process.getOutputStream().close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    /**
     * 根据某一台仪器的功率记录，计算档位
     *
     * @param powerList 某一台仪器的功率记录
     * @return 档位
     */
    public static String doGetInstrumentPowerLevel(List<Double> powerList) {
        StringBuilder message = new StringBuilder();
        try {
            ClassPathResource classPathResource = new ClassPathResource("/static/py/instrument_power.py");
            String s1 = JSONUtil.parseArray(powerList).toString();
            String[] args = new String[]{"python", classPathResource.getAbsolutePath(), JSONUtil.quote(s1)};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                message.append(line);
            }
            in.close();
            proc.waitFor();
        } catch (Exception e) {
            log.warn("机器学习功率档位失败，请检查 instrument_power.py 是否正确。");
            e.printStackTrace();
//            throw new BusinessException("机器学习功率档位失败，请检查 instrument_power.py 是否正确。");
        }
        return message.toString();
    }
}
