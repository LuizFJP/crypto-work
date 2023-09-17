package org.example;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

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

    public static String CBC(String password, byte[] iv, String salt) {
        SecretKeySpec keySalt = generateKey(salt);

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "BC");
            byte[] input = Hex.decode(password);

            cipher.init(Cipher.ENCRYPT_MODE, keySalt, new IvParameterSpec(iv));

            byte[] passwordCBC = cipher.doFinal(input);

            return passwordCBC.toString();
        } catch (Exception e) {
            System.out.println("Deu ruim fazendo o cipher");
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
            System.out.println("Deu fezes");
            return "";
        }

    }

    private static byte[] computeDigest(byte[] data)
            throws NoSuchProviderException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");

        digest.update(data);

        return digest.digest();
    }
}
