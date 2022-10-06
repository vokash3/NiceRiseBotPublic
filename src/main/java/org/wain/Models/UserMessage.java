package org.wain.Models;

import lombok.Data;

@Data
public class UserMessage {
    private Long id;
    private Long time;

    public UserMessage(Long id, Long time) {
        this.id = id;
        this.time = time;
    }
}
