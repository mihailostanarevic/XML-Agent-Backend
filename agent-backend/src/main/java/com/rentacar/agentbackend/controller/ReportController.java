package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateReportRequest;
import com.rentacar.agentbackend.dto.response.ReportResponse;
import com.rentacar.agentbackend.dto.response.RequestAdResponse;
import com.rentacar.agentbackend.service.IReportService;
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
    public ReportResponse writeReport(@RequestBody CreateReportRequest reportRequest, @PathVariable UUID id) throws Exception {
        return _reportService.createReport(reportRequest, id);
    }

    @GetMapping("/{id}/agent")
    public List<RequestAdResponse> getAllRequestAdsWhichNeedReport(@PathVariable UUID id) throws Exception {
        return _reportService.getAllRequestAdsWhichNeedReport(id);
    }
}
