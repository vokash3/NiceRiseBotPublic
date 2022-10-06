package org.wain.Utils;

import org.wain.Models.UserMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FilesUpdater {
    final static Path LAST_MESSAGES = Helper.lastMessage;

    public static void updateLastUserMessage(List<UserMessage> lastMessages) {
        try (FileWriter fw = new FileWriter(LAST_MESSAGES.toFile())) {
            fw.write("");
            lastMessages.forEach(c -> {
                try {
                    fw.append(String.valueOf(c.getId())).append(" ").append(String.valueOf(c.getTime())).append("\n");
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
