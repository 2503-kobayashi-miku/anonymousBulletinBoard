package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("SELECT r FROM Report r WHERE r.createdDate between :start AND :end ORDER BY r.updatedDate DESC")
    List<Report> findAllByCreatdDate(@Param("start")Date start, @Param("end")Date end);

    @Modifying
    @Query("UPDATE Report r SET r.updatedDate = :date WHERE r.id = :id")
    void saveById(@Param("id") int id, @Param("date")Date date);
}
