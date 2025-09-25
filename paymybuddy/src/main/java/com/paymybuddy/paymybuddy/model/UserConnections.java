package com.paymybuddy.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_connections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserConnections {

    @EmbeddedId
    private UserConnectionsId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @MapsId("contactId")
    @JoinColumn(name = "contact_id", nullable = false)
    private Users contact;


    public UserConnections(Users user, Users contact) {
        this.user = user;
        this.contact = contact;
        this.id = new UserConnectionsId(user.getId(), contact.getId());
    }
}
