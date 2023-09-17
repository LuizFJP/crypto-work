package org.example;

import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);

        System.out.println("Informe seu nome de usuário");
        String name = keyboard.next();
        System.out.println("Informe sua senha");
        String password = keyboard.next();

        Encrypt.initProvider();
        String nameEncrypted = Encrypt.SHA256(name);

        String salt = Encrypt.SHA256(password);
        byte[] iv = Encrypt.PBKDF2(password.toCharArray(), salt.getBytes(), 1);

        String passwordCBC = Encrypt.CBC(password.toString(), iv, salt);

        File.write(nameEncrypted, passwordCBC, "encrypted-credentials.txt");
        String ivCBC = Encrypt.CBC(iv.toString(), iv, salt);
        String saltCBC = Encrypt.CBC(salt, iv, salt);
        File.write(ivCBC, saltCBC, "iv-salt-encrypted.txt");


        keyboard.close();
    }
}