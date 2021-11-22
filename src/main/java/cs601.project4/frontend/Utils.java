package cs601.project4.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {
    public static String readFile(Path path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader inBuffer = Files.newBufferedReader(path)){
            String line;
            while((line = inBuffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
