package org.wain.Models.fromDB;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "period_list")
public class Period {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "period_id")
    private int id;
    @Column(name = "period_name")
    private String periodName;
    @Column(name = "period_time")
    private long periodTime;

    public Period() {
    }

    public Period(int id, String periodName, long periodTime) {
        this.id = id;
        this.periodName = periodName;
        this.periodTime = periodTime;
    }
}
