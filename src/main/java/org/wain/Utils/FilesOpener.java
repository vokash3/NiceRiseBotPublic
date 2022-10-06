package org.wain.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FilesOpener {
    final static Path LAST_MESSAGES = Helper.lastMessage;

    public static Map<Long, Long> getLastMessageStats() {
        Map<Long, Long> temp = new HashMap<>();
        try {
            Files.readAllLines(LAST_MESSAGES).forEach(s -> {
                String[] mFlds = s.split(" ");
                try {
                    temp.put(Long.parseLong(mFlds[0]), Long.parseLong(mFlds[1]));
                } catch (Exception e) {
                    temp.put(0L, 0L);
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<>(temp);
    }

    public static boolean isEmpty(Path path) {
        boolean result = true;
        try {
            result = Files.readAllLines(path).isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

