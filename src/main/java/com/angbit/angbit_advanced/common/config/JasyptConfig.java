package com.angbit.angbit_advanced.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password); // 복호화 키
        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘
        config.setKeyObtentionIterations("1000"); // 해싱 횟수(반복)
        config.setPoolSize("1"); // 인스턴스 Pool
        config.setProviderName("SunJCE"); // 프로바이더
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // Salt 생성 클래스
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64"); // 인코딩 타입
        encryptor.setConfig(config); // encryptor에 설정 정보를 setting 한다.
        return encryptor;
    }
}