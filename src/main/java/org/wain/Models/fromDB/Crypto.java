package org.wain.Models.fromDB;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "crypto_list")
public class Crypto {

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "user_crypto",
            joinColumns = @JoinColumn(name = "crypto_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> users;
    @Id
    @Column(name = "crypto_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cryptoId;
    @Column(name = "crypto_name", unique = true)
    private String cryptoName;

    public Crypto() {
    }

    public Crypto(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public void addUserToUserslist(User user) {
        if (users == null)
            users = new ArrayList<>();
        users.add(user);
    }

    public void removeUserFromUserList(User user) {
        users.remove(user);
    }

    @Override
    public String toString() {
        return "Crypto{" +
                "cryptoName='" + cryptoName + '\'' +
                '}';
    }
}
