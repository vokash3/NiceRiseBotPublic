package org.wain.Models.fromDB;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user_last_updated")
public class UserLastUpdated {

    @Id
    @Column(name = "user_id")
    long userId;
    @Column(name = "last_updated")
    Timestamp lastUpdated;
}
