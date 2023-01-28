package com.agilent.cdsa.license.client;

import cn.hutool.core.io.resource.ClassPathResource;
import com.agilent.cdsa.license.server.CustomKeyStoreParam;
import de.schlichtherle.license.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * License校验类
 */
@Slf4j
public class LicenseVerify {

    @ApiModelProperty("证书subject")
    private final String subject;

    @ApiModelProperty("公钥别称")
    private final String publicAlias;

    @ApiModelProperty("访问公钥库的密码")
    private final String storePassword;

    @ApiModelProperty("证书生成路径")
    private final String licensePath;

    @ApiModelProperty("密钥库存储路径")
    private final String publicKeysStorePath;
    /**
     * LicenseManager
     */
    private LicenseManager licenseManager;
    /**
     * 标识证书是否安装成功
     */
    private boolean installSuccess;

    /**
     * 构造License验证对象时，license文件和公钥文件拼接项目resource目录地址作为前缀
     *
     * @param subject             证书subject
     * @param publicAlias         公钥别名
     * @param storePassword       访问公钥库的密码
     * @param licensePath         证书名称
     * @param publicKeysStorePath 公钥文件名称
     */
    public LicenseVerify(String subject, String publicAlias, String storePassword, String licensePath, String publicKeysStorePath) {
        this.subject = subject;
        this.publicAlias = publicAlias;
        this.storePassword = storePassword;
        this.licensePath = licensePath;
        this.publicKeysStorePath = publicKeysStorePath;
    }

    /**
     * 安装License证书，读取证书相关的信息, 在bean加入容器的时候自动调用
     */
    public void installLicense() {
        try {
            Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);
            CipherParam cipherParam = new DefaultCipherParam(storePassword);
            KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class,
                    publicKeysStorePath,
                    publicAlias,
                    storePassword,
                    null);
            LicenseParam licenseParam = new DefaultLicenseParam(subject, preferences, publicStoreParam, cipherParam);

            licenseManager = new CustomLicenseManager(licenseParam);
            licenseManager.uninstall();
            String path = new ClassPathResource("").getAbsolutePath() + licensePath;
            LicenseContent licenseContent = licenseManager.install(new File(path));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            installSuccess = true;
            log.info("------------------------------- 证书安装成功 -------------------------------");
            log.info(MessageFormat.format("证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
        } catch (Exception e) {
            installSuccess = false;
            log.error("------------------------------- 证书安装失败 -------------------------------");
            log.error("证书安装失败：", e);
        }
    }
/*
    private void generateLicenseTest() {
        // 生成license需要的一些参数
        LicenseProperties param = new LicenseProperties();
        // 证书授权主体
        param.setSubject("cdsAssistant");
        // 私钥别名
        param.setPrivateAlias("cdsAssistant_privateKey");
        // 私钥密码（需要妥善保管，不能让使用者知道）
        param.setKeyPass("agilent@123");
        // 访问私钥库的密码
        param.setStorePass("agilent@123");
        // 证书存储地址
        param.setLicensePath("license/cdsAssistant_license.lic");
        // 私钥库所在地址
        param.setPrivateKeysStorePath("license/cdsAssistant_privateKeys.keystore");
        // 证书生效时间
        Calendar issueCalendar = Calendar.getInstance();
        param.setIssuedTime(issueCalendar.getTime());
        // 证书失效时间
        Calendar expiryCalendar = Calendar.getInstance();
        // 设置当前时间
        expiryCalendar.setTime(new Date());
        // 往后延长一年 = 授权一年时间
        expiryCalendar.add(Calendar.MONTH, 3);
        param.setExpiryTime(expiryCalendar.getTime());
        // 用户类型
        param.setConsumerType("user");
        // 用户数量
        param.setConsumerAmount(1);
        // 描述
        param.setDescription("测试");
        LicenseGenerator licenseGenerator = new LicenseGenerator();
        licenseGenerator.setLicenseProperties(param);
        // 生成license
        licenseGenerator.generateLicense();
        log.info("------------------------------- License生成安装成功 -------------------------------");
    }*/

    /**
     * 卸载证书，在bean从容器移除的时候自动调用
     */
    public void unInstallLicense() {
        if (installSuccess) {
            try {
                licenseManager.uninstall();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * 校验License证书
     */
    public boolean verify() {
        try {
            LicenseContent licenseContent = licenseManager.verify();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

