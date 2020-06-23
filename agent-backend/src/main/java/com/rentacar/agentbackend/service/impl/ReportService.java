package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreateReportRequest;
import com.rentacar.agentbackend.dto.response.ReportResponse;
import com.rentacar.agentbackend.dto.response.RequestAdResponse;
import com.rentacar.agentbackend.dto.response.StatisticResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.IAgentRepository;
import com.rentacar.agentbackend.repository.ICarRepository;
import com.rentacar.agentbackend.repository.IReportRepository;
import com.rentacar.agentbackend.repository.IRequestAdRepository;
import com.rentacar.agentbackend.service.IRatingService;
import com.rentacar.agentbackend.service.IReportService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService {

    private final IReportRepository _reportRepository;

    private final IRequestAdRepository _requestAdRepository;

    private final ICarRepository _carRepository;

    private final IAgentRepository _agentRepository;

    private final IRatingService _ratingService;

    public ReportService(IReportRepository reportRepository, IRequestAdRepository requestAdRepository, ICarRepository carRepository, IAgentRepository agentRepository, IRatingService ratingService) {
        _reportRepository = reportRepository;
        _requestAdRepository = requestAdRepository;
        _carRepository = carRepository;
        _agentRepository = agentRepository;
        _ratingService = ratingService;
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

    @Override
    public StatisticResponse getStatisticByAgent(UUID id) throws Exception {
        Agent agent = _agentRepository.findOneById(id);

        Set<Ad> set = agent.getAd();
        List<Ad> ads = new ArrayList<>();
        ads.addAll(set);

        StatisticResponse response = new StatisticResponse();
        Ad mostKilometersTraveledAd = ads.get(0);
        Ad leastKilometersTraveledAd = ads.get(0);

        Ad mostCommentedAd = ads.get(0);
        Ad leastCommentedAd = ads.get(0);

        Ad mostRatedAd = ads.get(0);
        Ad leastRatedAd = ads.get(0);
        Ad beastRatingAd = ads.get(0);
        Ad worstRatingAd = ads.get(0);

        for(int i = 1;i < ads.size();i++){
            if(Float.valueOf(ads.get(i).getCar().getKilometersTraveled()) > Float.valueOf(mostKilometersTraveledAd.getCar().getKilometersTraveled())){
                mostKilometersTraveledAd = ads.get(i);
            }
            if(Float.valueOf(ads.get(i).getCar().getKilometersTraveled()) < Float.valueOf(leastKilometersTraveledAd.getCar().getKilometersTraveled())){
                leastKilometersTraveledAd = ads.get(i);
            }

            if(Float.valueOf(ads.get(i).getComments().size()) > Float.valueOf(mostCommentedAd.getComments().size())){
                mostCommentedAd = ads.get(i); //all comments, not only approved
            }
            if(Float.valueOf(ads.get(i).getComments().size()) < Float.valueOf(leastCommentedAd.getComments().size())){
                leastCommentedAd = ads.get(i);
            }

            if(Float.valueOf(ads.get(i).getRatings().size()) > Float.valueOf(mostRatedAd.getRatings().size())){
                mostRatedAd = ads.get(i);
            }
            if(Float.valueOf(ads.get(i).getRatings().size()) < Float.valueOf(leastRatedAd.getComments().size())){
                leastRatedAd = ads.get(i);
            }

            if(Float.valueOf(_ratingService.getAvgRatingByAd(ads.get(i).getId()).getAvgRating()) > Float.valueOf(_ratingService.getAvgRatingByAd(beastRatingAd.getId()).getAvgRating())){
                beastRatingAd = ads.get(i);
            }
            if(Float.valueOf(_ratingService.getAvgRatingByAd(ads.get(i).getId()).getAvgRating()) < Float.valueOf(_ratingService.getAvgRatingByAd(worstRatingAd.getId()).getAvgRating())){
                worstRatingAd = ads.get(i);
            }
        }

        response.setMostKilometersTraveled(mostKilometersTraveledAd.getCar().getKilometersTraveled());
        response.setLeastKilometersTraveled(leastKilometersTraveledAd.getCar().getKilometersTraveled());
        response.setMostCommentedAd(String.valueOf(mostCommentedAd.getComments().size()));
        response.setLeastCommentedAd(String.valueOf(leastCommentedAd.getComments().size()));
        response.setMostRateddAd(String.valueOf(mostRatedAd.getRatings().size()));
        response.setLeastRatedAd(String.valueOf(leastRatedAd.getRatings().size()));

        response.setAdIdLeastCommented(leastCommentedAd.getId());
        response.setAdIdLeastKilometersTraveled(leastKilometersTraveledAd.getId());
        response.setAdIdLeastRated(leastRatedAd.getId());
        response.setAdIdMostCommented(mostCommentedAd.getId());
        response.setAdIdMostKilometersTraveled(mostKilometersTraveledAd.getId());
        response.setAdIdMostRated(mostRatedAd.getId());

        response.setBrandNameLeastCommented(leastCommentedAd.getCar().getCarModel().getCarBrand().getName());
        response.setBrandNameLeastKilometersTraveled(leastKilometersTraveledAd.getCar().getCarModel().getCarBrand().getName());
        response.setBrandNameLeastRated(leastRatedAd.getCar().getCarModel().getCarBrand().getName());
        response.setBrandNameMostCommented(mostCommentedAd.getCar().getCarModel().getCarBrand().getName());
        response.setBrandNameMostKilometersTraveled(mostKilometersTraveledAd.getCar().getCarModel().getCarBrand().getName());
        response.setBrandNameMostRated(mostRatedAd.getCar().getCarModel().getCarBrand().getName());

        response.setModelNameLeastCommented(leastCommentedAd.getCar().getCarModel().getName());
        response.setModelNameLeastKilometersTraveled(leastKilometersTraveledAd.getCar().getCarModel().getName());
        response.setModelNameLeastRated(leastRatedAd.getCar().getCarModel().getName());
        response.setModelNameMostCommented(mostCommentedAd.getCar().getCarModel().getName());
        response.setModelNameMostKilometersTraveled(mostKilometersTraveledAd.getCar().getCarModel().getName());
        response.setModelNameMostRated(mostRatedAd.getCar().getCarModel().getName());

        response.setBestAvgRating(_ratingService.getAvgRatingByAd(beastRatingAd.getId()).getAvgRating());
        response.setWorstAvgRating(_ratingService.getAvgRatingByAd(worstRatingAd.getId()).getAvgRating());
        response.setAdIdBestRated(beastRatingAd.getId());
        response.setBrandNameBestRated(beastRatingAd.getCar().getCarModel().getCarBrand().getName());
        response.setModelNameBestRated(beastRatingAd.getCar().getCarModel().getName());
        response.setAdIdWorstRated(worstRatingAd.getId());
        response.setBrandNameWorstRated(worstRatingAd.getCar().getCarModel().getCarBrand().getName());
        response.setModelNameWorstRated(worstRatingAd.getCar().getCarModel().getName());

        return response;
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
