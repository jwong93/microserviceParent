package com.giftgracious.authserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

@Component
public class KeyUtils {
    @Autowired
    Environment environment;

    @Value("access-token.private")
    private String accessTokenPrivateKey;

    @Value("access-token.public")
    public String accessTokenPublicKey;

    @Value("refresh-token.private")
    public String refreshTokenPrivateKey;

    @Value("refresh-token.public")
    public String refreshTokenPublicKey;

    private KeyPair _accessTokenKeyPair;
    private KeyPair _refreshTokenKeyPair;

    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(_accessTokenKeyPair)){
            _accessTokenKeyPair = getKeyPair(accessTokenPublicKey,accessTokenPrivateKey);
        }
        return _accessTokenKeyPair;
    }

    private KeyPair getRefreshTokenKeyPair()  {
        if (Objects.isNull(_refreshTokenKeyPair)){
            _refreshTokenKeyPair = getKeyPair(refreshTokenPublicKey,refreshTokenPrivateKey);
        }
        return _refreshTokenKeyPair;
    }

    private KeyPair getKeyPair(String publicpath, String privatepath) {
        KeyPair keyPair;

        File publicKey = new File(publicpath);
        File privateKey = new File(privatepath);

        if (publicKey.exists() && privateKey.exists()){
            try{
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicBytes = Files.readAllBytes(publicKey.toPath());
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicBytes);
                PublicKey publicKey1 = keyFactory.generatePublic(publicKeySpec);

                byte[] privateBytes = Files.readAllBytes(privateKey.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
                PrivateKey privateKey1 = keyFactory.generatePrivate(privateKeySpec);

                keyPair = new KeyPair(publicKey1, privateKey1);
                return keyPair;
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e){
                throw new RuntimeException(e);
            }
        }
        else{
            if (Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equals("prod"))) {
                throw new RuntimeException("Keys Does not exist");
            }
            File directory = new File("access-refresh-token-keys");
            if (!directory.exists()){
                directory.mkdirs();
            }
            try{
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();
                try (FileOutputStream fos = new FileOutputStream(publicKey)){
                    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                    fos.write(publicKeySpec.getEncoded());
                }
                try (FileOutputStream fos = new FileOutputStream(privateKey)) {
                    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                    fos.write(privateKeySpec.getEncoded());
                }
            } catch (NoSuchAlgorithmException | IOException e){
                throw new RuntimeException(e);
            }
            return keyPair;

        }

    }
    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }

    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }



}
