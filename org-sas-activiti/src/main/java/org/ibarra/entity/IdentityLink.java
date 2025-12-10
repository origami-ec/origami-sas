package org.ibarra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

//@Data
@Entity
@Table(name = "act_ru_identitylink")
@Getter
@Setter
@NoArgsConstructor

public class IdentityLink implements Serializable {

    @Id
    @Column(name = "id_")
    private String id;

    @Column(name = "user_id_")
    private String userId;

    @Column(name = "proc_inst_id_")
    private String procInstId;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }


}
