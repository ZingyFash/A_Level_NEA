package renderEngine;

import java.io.IOException;
import java.nio.file.*;

public class Utils {
    public static String readFile(String filePath) {
        String str;

        try {
            str = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Could not load file : " + filePath, e);
        }

        return str;
    }
}
