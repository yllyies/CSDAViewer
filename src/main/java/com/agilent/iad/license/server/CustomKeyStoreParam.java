package com.agilent.iad.license.server;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import de.schlichtherle.license.AbstractKeyStoreParam;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 自定义KeyStoreParam，用于将公私钥存储文件存放到其他磁盘位置而不是项目中
 *
 */
@Slf4j
public class CustomKeyStoreParam extends AbstractKeyStoreParam {

    /**
     * 公钥/私钥在磁盘上的存储路径
     */
    private final String storePath;
    private final String alias;
    private final String storePwd;
    private final String keyPwd;

    public CustomKeyStoreParam(Class clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getStorePwd() {
        return storePwd;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }

    /**
     * 复写de.schlichtherle.license.AbstractKeyStoreParam的getStream()方法
     * <br/>
     * 用于将公私钥存储文件存放到其他磁盘位置而不是项目中
     */
    @Override
    public InputStream getStream() {
        String path = new ClassPathResource("").getAbsolutePath() + this.storePath;
        final InputStream in;
        try {
            // 便于 Junit 测试替换路径
            in = new FileInputStream(StrUtil.replace(path, "test-classes", "classes"));
            if (null == in) {
                throw new FileNotFoundException(storePath);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return in;
    }
}

