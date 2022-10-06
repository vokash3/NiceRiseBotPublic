package org.wain.Models.fromDB;

import lombok.Data;
import org.wain.Models.PeriodFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user_list")
public class User { //TABLE - user_list

    @Id
    @Column(name = "user_id")
    private long userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "language_code")
    private String languageCode;
    @Column(name = "isBot")
    private boolean isBot;
    @Column(name = "phone_number")
    private String telephoneNumber;
    @Column(name = "mute_elon")
    private boolean muteElon;
    @OneToOne // TODO - удаление только индекса
    @JoinColumn(name = "update_period")
    private Period period;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "user_crypto",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "crypto_id")
    )
    private List<Crypto> cryptoList;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserLastUpdated userLastUpdated;

    public User() {
    }

    public User(long userId, String firstName, String lastName, String nickname, String languageCode, boolean isBot, String telephoneNumber, boolean muteElon) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.languageCode = languageCode;
        this.isBot = isBot;
        this.telephoneNumber = telephoneNumber;
        this.muteElon = muteElon;
    }

    public User(org.telegram.telegrambots.meta.api.objects.User user) {
        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.nickname = user.getUserName();
        this.languageCode = user.getLanguageCode();
        this.isBot = user.getIsBot();
        this.telephoneNumber = null;
        this.muteElon = true;
        this.period = PeriodFactory.getPeriod(PeriodFactory.Periods.DEFAULT);
    }

    public void addCryptoToUser(Crypto crypto) {
        if (cryptoList == null)
            cryptoList = new ArrayList<>();
        cryptoList.add(crypto);
    }

    public void removeCryptoFromUser(Crypto crypto) {
        cryptoList.remove(crypto);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", isBot=" + isBot +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", muteElon=" + muteElon +
                ", period=" + period +
                ", cryptoList=" + cryptoList +
                ", userLastUpdated=" + userLastUpdated +
                '}';
    }
}
