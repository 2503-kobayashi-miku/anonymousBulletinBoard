package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;
    private Integer reportId;
    @Autowired
    HttpSession session;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(name="startDate", required=false) String startDate,
                            @RequestParam(name="endDate", required=false) String endDate) {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport(startDate, endDate);
        List<CommentForm> commentData = commentService.findAllComment(startDate, endDate);
        // 画面遷移先を指定
        mav.setViewName("/top");
        //セッションを削除
        session.invalidate();
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("comments", commentData);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") @Validated ReportForm reportForm,
                                   BindingResult result){
        //バリデーション処理
        if(result.hasErrors()){
            List<String> errorMessages = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages",errorMessages);
            return new ModelAndView("redirect:/new");
        }

        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);

        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id){
        reportService.deleteReport(id);

        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView();

        ReportForm report = reportService.selectReport(id);

        mav.addObject("formModel", report);
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 投稿編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@ModelAttribute("formModel")@Validated ReportForm report,
                                      BindingResult result){
        //バリデーション処理
        if(result.hasErrors()){
            List<String> errorMessages = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages",errorMessages);
            return new ModelAndView("redirect:/edit/{id}");
        }

        reportService.saveReport(report);
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント投稿画面表示
     */
    @GetMapping("/comment/{id}")
    public ModelAndView newComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        CommentForm commentForm = new CommentForm();
        //コメントする投稿を取得
        ReportForm content = reportService.selectReport(id);
        // 準備した空のFormを保管
        mav.addObject("commentFormModel", commentForm);
        // 取得したコメント先の投稿を保管
        mav.addObject("content", content);
        // 画面遷移先を指定
        mav.setViewName("/comment");

        return mav;
    }

    /*
     * 新規コメント投稿処理
     */
    @PostMapping("/comment/{id}")
    public ModelAndView addComment(@PathVariable Integer id,
                                   @ModelAttribute("commentFormModel") @Validated CommentForm commentForm,
                                   BindingResult result){
        //バリデーション処理
        if(result.hasErrors()){
            List<String> errorMessages = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages",errorMessages);
            return new ModelAndView("redirect:/new");
        }

        commentForm.setReportId(id);
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント編集画面表示
     */
    @GetMapping("/edit-comment/{id}")
    public ModelAndView editComment(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView();
        //編集するコメントを取得
        CommentForm comment = commentService.selectComment(id);
        mav.addObject("formModel", comment);
        mav.addObject("reportId", id);
        mav.setViewName("/edit-comment");
        return mav;
    }

    /*
     * コメント編集処理
     */
    @PutMapping("/update-comment/{id}")
    public ModelAndView updateComment(@PathVariable Integer id,
                                      @ModelAttribute("formModel")@Validated CommentForm comment,
                                      BindingResult result){
        //バリデーション処理
        if(result.hasErrors()){
            List<String> errorMessages = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages",errorMessages);
            return new ModelAndView("redirect:/edit-comment/{id}");
        }

        comment.setId(id);
        commentService.saveComment(comment);
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete-comment/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id){
        commentService.deleteReport(id);

        return new ModelAndView("redirect:/");
    }
}
