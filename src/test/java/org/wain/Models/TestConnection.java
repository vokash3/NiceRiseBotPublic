package org.wain.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "test_connection")
public class TestConnection {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private int status;

}
