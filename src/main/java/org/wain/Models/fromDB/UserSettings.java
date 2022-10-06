package org.wain.Models.fromDB;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserSettings {

    private long userId;
    private String updatePeriodName;
    private boolean muteElon;
    private Timestamp lastUpdated;

    public UserSettings() {
    }

    public UserSettings(long userId, String updatePeriodName, boolean muteElon, Timestamp lastUpdated) {
        this.userId = userId;
        this.updatePeriodName = updatePeriodName;
        this.muteElon = muteElon;
        this.lastUpdated = lastUpdated;
    }

    public String toString() {
        return "• Период обновления - " + updatePeriodName + "\n";
    }
}
