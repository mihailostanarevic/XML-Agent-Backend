package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateReportRequest;
import com.rentacar.agentbackend.dto.response.ReportResponse;
import com.rentacar.agentbackend.dto.response.RequestAdResponse;
import com.rentacar.agentbackend.dto.response.StatisticResponse;
import com.rentacar.agentbackend.service.IReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final IReportService _reportService;

    public ReportController(IReportService reportService) {
        _reportService = reportService;
    }

    @PostMapping("/{id}/request-ad")
    @PreAuthorize("hasAuthority('UPDATE_AD')")
    public ReportResponse writeReport(@RequestBody CreateReportRequest reportRequest, @PathVariable UUID id) throws Exception {
        return _reportService.createReport(reportRequest, id);
    }

    @GetMapping("/possible/{id}/agent")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<RequestAdResponse> getAllRequestAdsWhichNeedReport(@PathVariable UUID id) throws Exception {
        return _reportService.getAllRequestAdsWhichNeedReport(id);
    }

    @GetMapping("/statistic/{id}/agent")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public StatisticResponse getStatisticByAgent(@PathVariable UUID id) throws Exception {
        return _reportService.getStatisticByAgent(id);
    }
}
