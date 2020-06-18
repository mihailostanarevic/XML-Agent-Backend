package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreateReportRequest;
import com.rentacar.agentbackend.dto.response.ReportResponse;
import com.rentacar.agentbackend.dto.response.RequestAdResponse;
import com.rentacar.agentbackend.entity.Agent;
import com.rentacar.agentbackend.entity.Car;
import com.rentacar.agentbackend.entity.Report;
import com.rentacar.agentbackend.entity.RequestAd;
import com.rentacar.agentbackend.repository.IAgentRepository;
import com.rentacar.agentbackend.repository.ICarRepository;
import com.rentacar.agentbackend.repository.IReportRepository;
import com.rentacar.agentbackend.repository.IRequestAdRepository;
import com.rentacar.agentbackend.service.IReportService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService {

    private final IReportRepository _reportRepository;

    private final IRequestAdRepository _requestAdRepository;

    private final ICarRepository _carRepository;

    private final IAgentRepository _agentRepository;

    public ReportService(IReportRepository reportRepository, IRequestAdRepository requestAdRepository, ICarRepository carRepository, IAgentRepository agentRepository) {
        _reportRepository = reportRepository;
        _requestAdRepository = requestAdRepository;
        _carRepository = carRepository;
        _agentRepository = agentRepository;
    }

    @Override
    public ReportResponse createReport(CreateReportRequest request, UUID requestAdId) throws Exception {
        RequestAd requestAd = _requestAdRepository.findOneById(requestAdId);
        if(requestAd.getReport() != null){
            throw new Exception("You have already written report for this rent."); //unnecessary
        }

        Report report = new Report();
        report.setRequestAd(requestAd);
        report.setDescription(request.getDescription());
        report.setKilometersTraveled(request.getKilometersTraveled());
        _reportRepository.save(report);

        requestAd.setReport(report);
        _requestAdRepository.save(requestAd);

        float kilometersTraveledInTotal = Float.valueOf(requestAd.getAd().getCar().getKilometersTraveled()) + Float.valueOf(request.getKilometersTraveled());
        Car car = requestAd.getAd().getCar();
        car.setKilometersTraveled(String.valueOf(kilometersTraveledInTotal));
        _carRepository.save(car);

        return mapReportToReportResponse(report);
    }

    @Override
    public List<RequestAdResponse> getAllRequestAdsWhichNeedReport(UUID id) throws Exception {
        List<RequestAd> requestAds = _requestAdRepository.findAllByReport(null);
        List<RequestAd> agentsRequestAds = new ArrayList<>();

        Agent agent = _agentRepository.findOneById(id);
        LocalDate now = LocalDate.now();
        for(RequestAd ra: requestAds){
            if(agent == ra.getAd().getAgent() && ra.getRequest().getStatus().equals(RequestStatus.PAID) && now.isAfter(ra.getReturnDate())){
                agentsRequestAds.add(ra);
            }
        }

        return agentsRequestAds.stream()
                .map(requestAd -> mapRequestAdToRequestAdResponse(requestAd))
                .collect(Collectors.toList());
    }

    private ReportResponse mapReportToReportResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setAgentName(report.getRequestAd().getAd().getAgent().getName());
        response.setBrandName(report.getRequestAd().getAd().getCar().getCarModel().getCarBrand().getName());
        response.setModelName(report.getRequestAd().getAd().getCar().getCarModel().getName());
        response.setKilometersTraveled(report.getKilometersTraveled());
        response.setKilometersTraveledInTotal(report.getRequestAd().getAd().getCar().getKilometersTraveled());
        response.setReportId(report.getId());
        return response;
    }

    private RequestAdResponse mapRequestAdToRequestAdResponse(RequestAd requestAd) {
        RequestAdResponse response = new RequestAdResponse();
        response.setBrandName(requestAd.getAd().getCar().getCarModel().getCarBrand().getName());
        response.setModelName(requestAd.getAd().getCar().getCarModel().getName());
        response.setPickUpDate(requestAd.getPickUpDate());
        response.setReturnDate(requestAd.getReturnDate());
        response.setRequestAdId(requestAd.getId());
        return response;
    }
}
