package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@Getter
@Setter
public class CommentForm {

    private int id;
    @NotBlank(message = "コメントを入力してください")
    private String content;
    private int reportId;
    private Date updatedDate;
}
