package com.agilent.cdsa.license.server;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.agilent.cdsa.license.client.CustomLicenseManager;
import de.schlichtherle.license.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.text.MessageFormat;
import java.util.prefs.Preferences;

/**
 * License生成类 -- 用于license生成
 */
@Data
@Slf4j
public class LicenseGenerator {

    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=AGILENT, OU=AGILENT, O=AGILENT, L=WUHAN, ST=HUBEI, C=CHINA");

    private LicenseProperties licenseProperties;

    /**
     * 生成License证书
     */
    public void generateLicense() {
        try {
            LicenseManager licenseManager = new CustomLicenseManager(initLicenseParam());
            LicenseContent licenseContent = initLicenseContent();
            String path = new ClassPathResource("").getAbsolutePath() + licenseProperties.getLicensePath();
            licenseManager.store(licenseContent, new File(StrUtil.replace(path, "test-classes", "classes")));
        } catch (Exception e) {
            log.error(MessageFormat.format("证书生成失败：{0}", licenseProperties), e);
        }
    }

    /**
     * 初始化证书生成参数
     */
    private LicenseParam initLicenseParam() {
        Preferences preferences = Preferences.userNodeForPackage(LicenseGenerator.class);

        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(licenseProperties.getStorePass());
        KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseGenerator.class
                , licenseProperties.getPrivateKeysStorePath()
                , licenseProperties.getPrivateAlias()
                , licenseProperties.getStorePass()
                , licenseProperties.getKeyPass());

        return new DefaultLicenseParam(licenseProperties.getSubject()
                , preferences
                , privateStoreParam
                , cipherParam);
    }

    /**
     * 设置证书生成正文信息
     */
    private LicenseContent initLicenseContent() {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setSubject(licenseProperties.getSubject());
        licenseContent.setIssued(licenseProperties.getIssuedTime());
        licenseContent.setNotBefore(licenseProperties.getIssuedTime());
        licenseContent.setNotAfter(licenseProperties.getExpiryTime());
        licenseContent.setConsumerType(licenseProperties.getConsumerType());
        licenseContent.setConsumerAmount(licenseProperties.getConsumerAmount());
        licenseContent.setInfo(licenseProperties.getDescription());
        return licenseContent;
    }
}

