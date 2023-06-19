package com.agilent.cdsa.license;

import com.agilent.cdsa.license.server.LicenseGenerator;
import com.agilent.cdsa.license.server.LicenseProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;


/**
 * License生成类 -- 用于license生成
 */
@Slf4j
public class LicenseCreatorTest {

    @Test
    public void generateLicenseTest() {
        // 生成license需要的一些参数
        LicenseProperties param = new LicenseProperties();
        // 证书授权主体
        param.setSubject("cdsa");
        // 私钥别名
        param.setPrivateAlias("cdsa_privateKey");
        // 私钥密码（需要妥善保管，不能让使用者知道）
        param.setKeyPass("agilent@123");
        // 访问私钥库的密码
        param.setStorePass("agilent@123");
        // 证书存储地址
        param.setLicensePath("license/cdsa_license.lic");
        // 私钥库所在地址
        param.setPrivateKeysStorePath("license/cdsa_privateKeys.keystore");
        // 证书生效时间
        Calendar issueCalendar = Calendar.getInstance();
        param.setIssuedTime(issueCalendar.getTime());
        // 证书失效时间
        Calendar expiryCalendar = Calendar.getInstance();
        // 设置当前时间
        expiryCalendar.setTime(new Date());
        // 往后延长一年 = 授权一年时间
        expiryCalendar.add(Calendar.MONTH, 6);
        param.setExpiryTime(expiryCalendar.getTime());
        // 用户类型
        param.setConsumerType("user");
        // 用户数量
        param.setConsumerAmount(1);
        // 描述
        param.setDescription("所有权归Agilent Technology");
        LicenseGenerator licenseGenerator = new LicenseGenerator();
        licenseGenerator.setLicenseProperties(param);
        // 生成license
        licenseGenerator.generateLicense();
    }
}

