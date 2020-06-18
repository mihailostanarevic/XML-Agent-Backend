package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CreateReportRequest;
import com.rentacar.agentbackend.dto.response.ReportResponse;
import com.rentacar.agentbackend.dto.response.RequestAdResponse;
import com.rentacar.agentbackend.dto.response.StatisticResponse;

import java.util.List;
import java.util.UUID;

public interface IReportService {

    ReportResponse createReport(CreateReportRequest request, UUID requestAdId) throws Exception;

    List<RequestAdResponse> getAllRequestAdsWhichNeedReport(UUID id) throws Exception;

    StatisticResponse getStatisticByAgent(UUID id) throws Exception;
}
