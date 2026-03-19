package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CSVUtil {

    public static List<String[]> read(InputStream is) throws Exception {

        List<String[]> rows = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line;
        boolean header = true;

        while ((line = reader.readLine()) != null) {

            // bỏ dòng trống
            if (line.trim().isEmpty()) continue;

            // bỏ header
            if (header) {
                header = false;
                continue;
            }

            // -1 để giữ cột trống
            rows.add(line.split(",", -1));
        }

        return rows;
    }
}