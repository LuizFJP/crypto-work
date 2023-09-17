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

        String nameEncrypted = Encrypt.SHA256(name);

        String salt = Encrypt.SHA256(password);
        byte[] iv = Encrypt.PBKDF2(password.toCharArray(), salt.getBytes(), 3);

        String passwordCBC = Encrypt.CBC(password, iv, salt);

        File.write(nameEncrypted, passwordCBC, "abobora");

        keyboard.close();
    }
}