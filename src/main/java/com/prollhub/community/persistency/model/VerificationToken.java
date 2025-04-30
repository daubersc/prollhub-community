package com.prollhub.community.persistency.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"account"})
@Entity
@Table(name= "verification_tokens")
public class VerificationToken {

    private static final Duration EXPIRY_DURATION = Duration.ofMinutes(15);

    @Id
    @Column(nullable = false, length = 36)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant created;

    @Column(nullable = false)
    private Instant expires;

    @PrePersist
    protected void calculateExpiry() {
        if (this.expires == null) {
            this.expires = Instant.now().plus(EXPIRY_DURATION);
        }
    }

    public VerificationToken(String token, Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        if (token == null) {
            throw new IllegalArgumentException("Token (Primary key) cannot be null");
        }
        this.account = account;
        this.token = token;
    }

}
