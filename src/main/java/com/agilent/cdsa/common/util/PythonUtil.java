package com.agilent.cdsa.common.util;

import cn.hutool.core.io.resource.ClassPathResource;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class PythonUtil {

    public static String execPythonFileWithReturn(String fileName) {
        StringBuilder message = new StringBuilder();
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/py/" + fileName);
            String[] args = new String[]{"python", classPathResource.getAbsolutePath()};
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
        }
        return message.toString();
    }

    public static void execPythonFile(String fileName) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/py/" + fileName);
            String[] args = new String[]{"python", classPathResource.getAbsolutePath()};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
            System.out.println(proc.exitValue());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 带参数执行python脚本
     *
     * @param fileName 脚本文件名称
     * @param params   脚本参数
     */
    public static void execPythonFile(String fileName, String params) {

        // 获取python文件所在目录地址
        ClassPathResource classPathResource = new ClassPathResource("static/py/" + fileName);
        // windows执行脚本需要使用 cmd.exe /c 才能正确执行脚本
        Process process = null;
        try {
            process = new ProcessBuilder("cmd.exe", "/c", "python", classPathResource.getAbsolutePath() + fileName, params).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("读取python文件 开始 fileName={}", fileName);
        BufferedReader errorReader = null;
        // 脚本执行异常时的输出信息
        errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        List<String> errorString = read(fileName, errorReader);
        log.info("读取python文件 异常 fileName={}&errorString={}", fileName, errorString);

        // 脚本执行正常时的输出信息
        BufferedReader inputReader = null;
        inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> returnString = read(fileName, inputReader);
        log.info("读取python文件 fileName={}&returnString={}", fileName, returnString);

        try {
            log.info("读取python文件 wait fileName={}", fileName);
            process.waitFor();
        } catch (InterruptedException e) {
            log.error("读取python文件 fileName=" + fileName + " 等待结果返回异常", e);
        }
        log.info("读取python文件 fileName={} == 结束 ==", fileName);
    }

    private static List<String> read(String fileName, BufferedReader reader) {
        List<String> resultList = Lists.newArrayList();
        String res = "";
        while (true) {
            try {
                if ((res = reader.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
                log.error("读取python文件 fileName=" + fileName + " 读取结果异常", e);
            }
            resultList.add(res);
        }
        return resultList;
    }

//    @Test
//    public void print() {
//        System.out.println("result: " + PythonUtil.execPythonFileWithReturn("instrument_status.py"));
//    }
}
