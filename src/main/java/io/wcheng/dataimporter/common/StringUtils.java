package io.wcheng.dataimporter.common;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static boolean isEmpty(String s) {
        return (s == null || s.isEmpty());
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }

    public static List<String> parseTextLine(String line, char delimiter, char qualifier) {
        List<String> rList = new ArrayList<>();
        if (isNotEmpty(line)) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            int qualifierCounter = 0;
            while (i < line.length()) {
                char c = line.charAt(i);
                if (c == qualifier) {
                    qualifierCounter++;
                } else if ((c == delimiter) && (qualifierCounter % 2 == 0)) {
                    rList.add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    sb.append(c);
                }
                i++;
            }
            if (sb.length() > 0) {
                rList.add(sb.toString());
            }
        }
        return rList;
    }
}
