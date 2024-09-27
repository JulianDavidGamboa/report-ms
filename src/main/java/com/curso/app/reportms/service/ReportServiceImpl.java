package com.curso.app.reportms.service;

import com.curso.app.reportms.helpers.ReportHelper;
import com.curso.app.reportms.models.Company;
import com.curso.app.reportms.models.WebSite;
import com.curso.app.reportms.repositories.CompaniesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService{

    private final CompaniesRepository cRepository;
    private final ReportHelper reportHelper;

    @Override
    public String makeReport(String name) {
        return reportHelper.readTemplate(this.cRepository.getByName(name).orElseThrow());
    }

    @Override
    public String saveReport(String report) {

        var format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        var placeHolders = this.reportHelper.getPlaceHoldersFromTemplate(report);

        var websites = Stream.of(placeHolders.get(3)).map(website -> WebSite.builder().name(website).build()).toList();

        var company = Company.builder().name(placeHolders.get(0)).foundationDate(LocalDate.parse(placeHolders.get(1), format)).founder(placeHolders.get(2)).webSites(websites).build();

        this.cRepository.postByName(company);

        return "Saved";
    }

    @Override
    public void deleteReport(String name) {
        this.cRepository.deleteByName(name);
    }
}
