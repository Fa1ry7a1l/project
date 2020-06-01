package com.example.project;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAHelper {
    //creates Key pair
    public static KeyPair generateKeyPair() {
        try {
            return RSA.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //this methods uses for saving keys
    public static byte[] getPrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate().getEncoded();
    }

    public static byte[] getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic().getEncoded();
    }

    //makes from saved format keys ones and codes or decodes messages
    public static String encrypt(String message, byte[] publicKey) {
        try {
            X509EncodedKeySpec ks = new X509EncodedKeySpec(publicKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pub = kf.generatePublic(ks);
            return RSA.encrypt(message, pub);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String message, byte[] privateKey) {
        try {
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pvt = kf.generatePrivate(ks);
            return RSA.decrypt(message, pvt);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
