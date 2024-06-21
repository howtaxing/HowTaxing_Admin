package com.xmonster.howtaxing_admin.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Customer {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private LocalDateTime localDateTime;

}
