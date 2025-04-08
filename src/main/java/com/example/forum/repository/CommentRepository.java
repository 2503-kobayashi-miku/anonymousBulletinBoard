package com.example.forum.repository;

import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Integer> {
    @Query("SELECT r FROM Comment r WHERE r.createdDate between :start AND :end ORDER BY id DESC")
    List<Comment> findAllByCreatdDate(@Param("start") Date start, @Param("end")Date end);
}
