package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    private Date expiryDate;
    private String username;

    public VerificationToken(String username,String token) {
        this.token = token;
        this.expiryDate = calculateExpirationDate(EXPIRATION);;
        this.username = username;
    }

    //Expiration in minutes
    private static final int EXPIRATION = 20;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(User user, String token) {
        super();
    	this.user = user;
    	this.token = token;
        this.expiryDate= calculateExpirationDate(EXPIRATION);

    }

    public VerificationToken(String token) {
        super();
    	this.token = token;
        this.expiryDate= calculateExpirationDate(EXPIRATION);

    }

    private Date calculateExpirationDate(int expirationTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }

}
