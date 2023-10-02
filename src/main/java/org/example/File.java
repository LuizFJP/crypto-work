package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class File {
    public static void write(String user, String password, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
            writer.write(user);
            writer.append("\n");
            writer.append(password);
            writer.close();
        } catch (Exception err) {
            System.out.println("Deu ruim pra fazer o writer" + err.getMessage());
        }
    }

    public static IVDTO readIv(String fileName) {
        String[] lines = read(fileName);
        IVDTO ivAndSalt = new IVDTO();
        ivAndSalt.setIv(lines[0]);
        ivAndSalt.setSalt(lines[1]);
        return ivAndSalt;
    }

    public static UserDTO readUser(String fileName) {
        String[] lines = read(fileName);
        UserDTO user = new UserDTO();
        user.setName(lines[0]);
        user.setPassword(lines[1]);
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
            System.out.println("Deu ruim pra fazer o reader" + err.getMessage());
            return null;
        }
    }
}
