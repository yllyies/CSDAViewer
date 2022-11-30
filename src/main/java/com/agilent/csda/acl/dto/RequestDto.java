package com.agilent.csda.acl.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "user-role relationship dto")
@Data
public class RequestDto implements Serializable {
    private String timeUnit;
}
