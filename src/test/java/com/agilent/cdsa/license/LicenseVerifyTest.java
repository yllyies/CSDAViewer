package com.agilent.cdsa.license;

import com.agilent.cdsa.license.client.LicenseVerify;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 许可证验证测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LicenseVerifyTest {

    private LicenseVerify licenseVerify;

    @Autowired
    public void setLicenseVerify(LicenseVerify licenseVerify) {
        this.licenseVerify = licenseVerify;
    }

    @Test
    public void licenseVerify() {
       System.out.println("licese是否有效：" + licenseVerify.verify());
    }

}