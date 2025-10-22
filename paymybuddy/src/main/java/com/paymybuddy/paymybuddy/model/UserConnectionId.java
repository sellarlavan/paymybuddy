package com.paymybuddy.paymybuddy.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionId implements Serializable {

    private Long userId;
    private Long contactId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserConnectionId that)) return false;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(contactId, that.contactId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, contactId);
    }
}