package com.agilent.iad.common.util;

import cn.hutool.core.collection.CollUtil;
import com.agilent.iad.dto.ChartItemDto;
import com.agilent.iad.dto.InstrumentsResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SnmpUtil {

    @Value("${snmpInfo.communityName}")
    public String communityName;
    public static String COMMUNITY_NAME;
    @Value("${snmpInfo.hostIp}")
    public String hostIp;
    public static String HOST_IP;
    @Value("${snmpInfo.port}")
    public Integer port;
    public static Integer PORT;
    @Value("${snmpInfo.version}")
    public Integer version;
    public static Integer VERSION;
    @Value("${snmpInfo.cpuTable}")
    public String cpuTable;
    public static String CPU_TABLE;
    @Value("${snmpInfo.usedMemory}")
    public String usedMemory;
    public static String USED_MEMORY;
    @Value("${snmpInfo.totalMemory}")
    public String totalMemory;
    public static String TOTAL_MEMORY;

    @PostConstruct
    private void init(){
        COMMUNITY_NAME = communityName;
        HOST_IP = hostIp;
        PORT = port;
        VERSION = version;
        CPU_TABLE = cpuTable;
        USED_MEMORY = usedMemory;
        TOTAL_MEMORY = totalMemory;
    }

    /**
     * 组装服务器系统信息
     *
     * @param responseDto 统计信息Dto
     * @param hostIp 目标服务器IP
     */
    public static void assemblySystemInfo(InstrumentsResponseDto responseDto, String hostIp) {
        try {
            //是否连接
            log.info("是否连接：{}", isEthernetConnection(hostIp));
        } catch (IOException e) {
            log.warn("目标服务器：{} 无法连接", hostIp);
            e.printStackTrace();
        }
        ChartItemDto cpuInfo = new ChartItemDto("CPU利用率", (long) getCpuUtilization());
        ChartItemDto memoryInfo = new ChartItemDto("内存利用率", (long) getMemoryUtilization());
        responseDto.setCpuPercentage(cpuInfo);
        responseDto.setMemoryPercentage(memoryInfo);
    }

    /**
     * 获取指定OID对应的table值
     *
     * @param oid 用于SNMP的命令参数
     */
    public static List<String> walkByTable(String oid) {
        Snmp snmp = null;
        PDU pdu;
        CommunityTarget target;
        List<String> result = new ArrayList<>();

        try {
            DefaultUdpTransportMapping dm = new DefaultUdpTransportMapping();
            snmp = new Snmp(dm);
            snmp.listen();
            target = new CommunityTarget();
            target.setCommunity(new OctetString(COMMUNITY_NAME));
            target.setVersion(VERSION);
            target.setAddress(new UdpAddress(HOST_IP + "/" + PORT));
            target.setTimeout(1000);
            target.setRetries(1);

            TableUtils tutils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
            OID[] columns = new OID[1];
            columns[0] = new VariableBinding(new OID(oid)).getOid();
            List<TableEvent> list = tutils.getTable(target, columns, null, null);
            for (TableEvent e : list) {
                VariableBinding[] vb = e.getColumns();
                if (null == vb) continue;
                result.add(vb[0].getVariable().toString());
            }
            snmp.close();
        } catch (IOException e) {
            //e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (snmp != null) {
                    snmp.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 获取CPU使用率
     *
     * @return 正常返回CPU当前使用率，否则返回-1
     */
    private static Integer getCpuUtilization() {
        List<String> result = walkByTable(CPU_TABLE);
        if (CollUtil.isEmpty(result)) {
            return -1;
        }
        double sum = 0;
        for (String s : result) {
            sum += Double.parseDouble(s);
        }
        return (int) (sum / result.size());
    }

    /**
     * 获取Memory占用率
     *
     * @return 正常返回当前内存使用率，否则返回-1
     */
    private static Integer getMemoryUtilization() {

        // 内存已使用
        try {
            List<String> usedresultList = walkByTable(USED_MEMORY);
            // 内存合计
            List<String> allresultList = walkByTable(TOTAL_MEMORY);
            if (CollUtil.isNotEmpty(usedresultList) && CollUtil.isNotEmpty(allresultList)) {
                double used = 0;
                String usedStr = usedresultList.get(usedresultList.size() - 1);
                used = Double.parseDouble(usedStr);
                double all = 0;
                String allStr = allresultList.get(allresultList.size() - 1);
                all = Double.parseDouble(allStr);
                return (int) ((used / all) * 100);
            }
        } catch (Exception e) {
            log.error("获取Memory占用率失败:" + e.getMessage());
        }
        return -1;
    }

    /**
     * 测网络通不通 类似 ping ip
     *
     * @param ip 目标服务器IP
     * @return 是否可以通讯
     */
    private static boolean isEthernetConnection(String ip) throws IOException {
        InetAddress ad = InetAddress.getByName(ip);
        return ad.isReachable(2000);
    }
}

