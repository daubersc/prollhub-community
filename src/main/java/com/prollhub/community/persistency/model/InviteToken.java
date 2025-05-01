package com.prollhub.community.persistency.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "token")
@ToString()
@Entity
@Table(name= "magic_link_tokens")
public class InviteToken {

    private static final Duration EXPIRY_DURATION = Duration.ofDays(1);

    @Id
    @Column(nullable = false, length = 36)
    private String token;

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

    public InviteToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token (Primary key) cannot be null");
        }
        this.token = token;
    }

}
