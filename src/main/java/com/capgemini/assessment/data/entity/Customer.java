package com.capgemini.assessment.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private String identityNumber;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<Account> accounts;
    private String name;
    private String surname;

}
