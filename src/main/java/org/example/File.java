package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class File {
    public static void write(String user, String password, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(user);
            writer.append("\n" + " ");
            writer.append(password);
            writer.close();
        } catch (Exception err) {
            System.out.println("Deu ruim pra fazer o writer");
        }
    }
}
