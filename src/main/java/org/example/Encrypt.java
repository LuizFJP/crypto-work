package org.example;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class Encrypt {
    public static byte[] PBKDF2(char[] password, byte[] salt,
                                int iterationCount) {
        PBEParametersGenerator generator = new PKCS5S2ParametersGenerator(
                new SHA256Digest());

        generator.init(
                PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password),
                salt,
                iterationCount);

        return ((KeyParameter) generator
                .generateDerivedParameters(256)).getKey();
    }

    public static String CBCEncrypt(String password, byte[] iv, String salt) {
        try {
            SecretKeySpec keySalt = generateKey(salt);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySalt, new IvParameterSpec(iv, 0, 16));

            byte[] passwordCBC = cipher.doFinal(password.getBytes());
            return Hex.toHexString(passwordCBC);
        } catch (Exception e) {
            System.out.println("Falha ao cifrar suas informações, tente novamente  " + e.getMessage());
            return "";
        }
    }

    public static String CBCDecrypt(String password, byte[] iv, String salt) {
        try {
            SecretKeySpec keySalt = generateKey(salt);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySalt, new IvParameterSpec(iv, 0, 16));

            byte[] output = cipher.doFinal(Hex.decode(password));
            return new String(output);
        } catch (Exception e) {
            System.out.println("Falha ao decifrar suas informações, tente novamente" + e.getMessage());
            return "";
        }
    }

    private static SecretKeySpec generateKey(String salt) {
        byte[] keyBytes = Hex.decode(salt);

        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String SHA256(String word) {
        try {
            return Hex.toHexString(
                    computeDigest(Strings.toByteArray(word)));
        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            System.out.println("Falha ao encriptar sua informação");
            return "";
        }

    }

    private static byte[] computeDigest(byte[] data)
            throws NoSuchProviderException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        digest.update(data);

        return digest.digest();
    }

    public static void initProvider() {
        Security.addProvider(new BouncyCastleProvider());

    }

}
