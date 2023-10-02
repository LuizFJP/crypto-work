package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class File {
    public static void write(String user, String password, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
            writer.write(user);
            writer.append("\n");
            writer.append(password);
            writer.close();
        } catch (Exception err) {
            System.out.println("Falha ao escrever o arquivo" + err.getMessage());
        }
    }

    public static IVDTO readIv(String fileName) {
        String[] lines = read(fileName);
        IVDTO ivAndSalt = new IVDTO();
        assert lines != null;
        if(lines.length >= 2){
            ivAndSalt.setIv(lines[0]);
            ivAndSalt.setSalt(lines[1]);
        }else {
            System.out.println("Problemas ao ler o arquivo de IV e Salt");
        }
        return ivAndSalt;
    }

    public static UserDTO readUser(String fileName) {
        String[] lines = read(fileName);
        UserDTO user = new UserDTO();
        assert lines != null;
        if(lines.length >= 2) {
            user.setName(lines[0]);
            user.setPassword(lines[1]);
        }else {
            System.out.println("Problemas ao ler o arquivo de IV e Salt");
        }
        return user;

    }

    private static String[] read(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String[] lines = new String[2];
            for (int i = 0; i < 2; i++) {
                lines[i] = br.readLine();
            }
            return lines;
        } catch (Exception err) {
            System.out.println("Falha ao ler o arquivo"  + err.getMessage());
            return null;
        }
    }
}
