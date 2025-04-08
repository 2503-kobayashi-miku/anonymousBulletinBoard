package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.entity.Report;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReportRepository reportRepository;

    public List<CommentForm> findAllComment(String startDate, String endDate) {
        Date start = null;
        Date end = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(!StringUtils.isBlank(startDate)) {
                startDate += " 00:00:00";
            } else {
                startDate = "2020-01-01 00:00:00";
            }
            start = sdf.parse(startDate);

            if(!StringUtils.isBlank(endDate)) {
                endDate += " 23:59:59";
                end = sdf.parse(endDate);
            } else {
                end = new Date();
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

        List<Comment> results = commentRepository.findAllByCreatdDate(start,end);
        List<CommentForm> comments = setCommentForm(results);
        return comments;
    }

    private List<CommentForm> setCommentForm(List<Comment> results){
        List<CommentForm> comments = new ArrayList<>();
        for(int i = 0; i < results.size(); i++){
            CommentForm comment = new CommentForm();
            Comment result = results.get(i);
            comment.setId(result.getId());
            comment.setContent(result.getContent());
            comment.setReportId(result.getReportId());
            comments.add(comment);
        }
        return comments;
    }

    @Transactional
    public void saveComment(CommentForm reqComment){
        Comment saveComment = setCommentEntity(reqComment);
        saveComment.setUpdatedDate(new Date());
        commentRepository.save(saveComment);
        //コメント元の投稿のupdated_dateを更新する
        reportRepository.saveById(saveComment.getReportId(), new Date());
    }

    private Comment setCommentEntity(CommentForm reqComment){
        Comment comment = new Comment();
        comment.setId(reqComment.getId());
        comment.setContent(reqComment.getContent());
        comment.setReportId(reqComment.getReportId());
        return comment;
    }

    public CommentForm selectComment(Integer id) {
        List<Comment> results = new ArrayList<>();
        results.add((Comment)commentRepository.findById(id).orElse(null));
        List<CommentForm> reports = setCommentForm(results);
        return reports.get(0);
    }

    public void deleteReport(Integer id) {
        commentRepository.deleteById(id);
    }
}
