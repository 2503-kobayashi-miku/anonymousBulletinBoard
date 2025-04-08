package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport(String startDate, String endDate) {
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

        List<Report> results = reportRepository.findAllByCreatdDate(start,end);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        saveReport.setUpdatedDate(new Date());
        reportRepository.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        return report;
    }

    /*
     * レコードの削除
     */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

    public ReportForm selectReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add((Report)reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }
}
