package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "report")
@Getter
@Setter
public class Report {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

    @Column(name = "updated_date", insertable = false)
    private Date updatedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date",insertable = false, updatable = false)
    private Date createdDate;
}
