package org.example;

import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);

        System.out.println("Digite 1 para login ou 2 para cadastro");
        int option = keyboard.nextInt();
        System.out.println("Informe seu nome de usuário");
        String name = keyboard.next();
        System.out.println("Informe sua senha");
        String password = keyboard.next();

        Encrypt.initProvider();
        String nameEncrypted = Encrypt.SHA256(name);

        String salt = Encrypt.SHA256(password);
        System.out.println("Salt " + salt);

        byte[] iv = Encrypt.PBKDF2(password.toCharArray(), salt.getBytes(), 1);
        System.out.println("iv " + iv);

        if (option == 1) {
            String ivSaltEncryptedFileName = "iv-salt-encrypted-" + nameEncrypted + ".txt";
            IVDTO ivAndSalt = File.readIv(ivSaltEncryptedFileName);
            String ivRecovered = Encrypt.CBCDecrypt(ivAndSalt.getIv(), iv, salt);
            byte[] ivR= stringToByteArray(ivRecovered);
            System.out.println("iv recovered " + ivR);
            String saltRecovered =  Encrypt.CBCDecrypt(ivAndSalt.getSalt(), iv, salt);
            System.out.println("Salt recovered " + saltRecovered);


            String passwordCBC = Encrypt.CBCEncrypt(password, ivR, saltRecovered);

            String encryptedCredentialsFileName = "encrypted-credentials-" + nameEncrypted + ".txt";
            UserDTO userDTO = File.readUser(encryptedCredentialsFileName);

            if (userDTO.getPassword().equals(passwordCBC)) {
                System.out.println("Logado com sucesso!");
            } else {
                System.out.println("Login incorreto!");
            }

        } else if (option == 2) {
            String passwordCBC = Encrypt.CBCEncrypt(password, iv, salt);
            String ivCBC = Encrypt.CBCEncrypt(Arrays.toString(iv), iv, salt);
            String saltCBC = Encrypt.CBCEncrypt(salt, iv, salt);

            File.write(nameEncrypted, passwordCBC, "encrypted-credentials-" + nameEncrypted + ".txt");

            File.write(ivCBC, saltCBC, "iv-salt-encrypted-" + nameEncrypted + ".txt");
        } else {
            System.out.println("Não inseriu um valor número válido!");
        }
        keyboard.close();
    }

    public static byte[] stringToByteArray(String input) {
        input = input.replaceAll("\\[|\\]", "");
        String[] values = input.split(", "); // Split the input string by commas and spaces
        byte[] byteArray = new byte[values.length]; // Create a byte array of the same length

        for (int i = 0; i < values.length; i++) {
            int intValue = Integer.parseInt(values[i].trim()); // Parse each integer value
            byteArray[i] = (byte) intValue; // Convert the integer to a byte and store it in the array
        }

        return byteArray;
    }
}