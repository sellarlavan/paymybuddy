package com.paymybuddy.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_connections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserConnection {

    @EmbeddedId
    private UserConnectionId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("contactId")
    @JoinColumn(name = "contact_id", nullable = false)
    private User contact;


    public UserConnection(User user, User contact) {
        this.user = user;
        this.contact = contact;
        this.id = new UserConnectionId(user.getId(), contact.getId());
    }
}
