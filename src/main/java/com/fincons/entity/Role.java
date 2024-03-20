package com.fincons.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(unique = true, nullable = false)
    private String name;


    @ManyToMany(mappedBy="roles")
    private List<User> users;

    @Column(name  = "deleted")
    private boolean deleted;


    @PreRemove
    public void removeUsersAssociations() {
        for (User user: this.users) {
            user.getRoles().remove(this);
        }
    }





}
