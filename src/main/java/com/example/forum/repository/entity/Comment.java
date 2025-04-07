package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

    @Column(name = "report_id")
    private int reportId;

    @Column(name = "updated_date", insertable = false,  updatable = false)
    private Date updatedDate;

    @Column(name = "created_date",insertable = false, updatable = false)
    private Date createdDate;

}
