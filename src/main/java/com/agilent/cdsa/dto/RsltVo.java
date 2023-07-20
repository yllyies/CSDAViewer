package com.agilent.cdsa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RsltVo {
    private String instrumentName;
    private String projectName;
    private String creator;
    private Date createdDate;

    public RsltVo(String instrumentName, String projectName, String creator) {
        this.instrumentName = instrumentName;
        this.projectName = projectName;
        this.creator = creator;
    }
}
