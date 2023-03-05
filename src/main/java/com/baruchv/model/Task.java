package com.baruchv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;


@Data
@AllArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private String uid;
    private String name;
    private String type;
    @Basic
    private java.sql.Date startDate;
    @Basic
    private java.sql.Date endDate;
    private String parentUid;

    public Task() {
    }
}


