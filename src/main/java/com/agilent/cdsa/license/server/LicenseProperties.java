package com.agilent.cdsa.license.server;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * License生成类需要的参数，具体参数在 application.yml中配置。
 */
@Data
public class LicenseProperties implements Serializable {

    private static final long serialVersionUID = 1002554775152629135L;

    @ApiModelProperty("证书subject")
    private String subject;

    @ApiModelProperty("私钥别称")
    private String privateAlias;

    @ApiModelProperty("私钥密码（需要妥善保管，不能让使用者知道）")
    private String keyPass;

    @ApiModelProperty("访问私钥库的密码")
    private String storePass;

    @ApiModelProperty("证书生成路径")
    private String licensePath;

    @ApiModelProperty("私钥库存储路径")
    private String privateKeysStorePath;

    @ApiModelProperty("证书生效时间")
    private Date issuedTime = new Date();

    @ApiModelProperty("证书失效时间")
    private Date expiryTime;

    @ApiModelProperty("用户类型")
    private String consumerType;

    @ApiModelProperty("用户数量")
    private Integer consumerAmount;

    @ApiModelProperty("描述信息")
    private String description;
    
}

