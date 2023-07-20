package com.agilent.cdsa.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "仪器实时状态返回值DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentsResponseDto implements Serializable {

    private static final long serialVersionUID = -3245381352482608240L;

    List<InstrumentDto> dataSource;
    String systemTotal;
    String runningCount;
    String notReadyCount;
    String idleCount;
    String errorCount;
    String offlineCount;
    String unknownCount;
    String humiture;
}
