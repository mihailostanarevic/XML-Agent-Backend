package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreatePriceListRequest;
import com.rentacar.agentbackend.dto.request.UpdatePriceListRequest;
import com.rentacar.agentbackend.dto.response.EarningsResponse;
import com.rentacar.agentbackend.dto.response.PriceListResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.IPriceListService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PriceListService implements IPriceListService {

    private final IPriceListRepository _priceListRepository;

    private final IAgentRepository _agentRepository;

    private final IRequestRepository _requestRepository;

    private final IRequestAdRepository _requestAdRepository;

    private final IAdRepository _adRepository;

    public PriceListService(IPriceListRepository priceListRepository, IAgentRepository agentRepository, IRequestRepository requestRepository, IRequestAdRepository requestAdRepository, IAdRepository adRepository) {
        _priceListRepository = priceListRepository;
        _agentRepository = agentRepository;
        _requestRepository = requestRepository;
        _requestAdRepository = requestAdRepository;
        _adRepository = adRepository;
    }

    @Override
    public PriceListResponse createPriceList(CreatePriceListRequest request) throws Exception {
        PriceList priceList = new PriceList();
        priceList.setDeleted(false);
        priceList.setAgentId(request.getAgentId());
        priceList.setPrice1day(request.getPrice1day());
        priceList.setDiscount7days(request.getDiscount7days());
        priceList.setDiscount15days(request.getDiscount15days());
        priceList.setDiscount30days(request.getDiscount30days());
        PriceList savedPriceList = _priceListRepository.save(priceList);
        return mapPriceListToPriceListResponse(savedPriceList);
    }

    @Override
    public PriceListResponse updatePriceList(UpdatePriceListRequest request) throws Exception {
        PriceList priceList = _priceListRepository.findOneByAgentId(request.getAgentId());
        priceList.setPrice1day(request.getPrice1day());
        priceList.setDiscount7days(request.getDiscount7days());
        priceList.setDiscount15days(request.getDiscount15days());
        priceList.setDiscount30days(request.getDiscount30days());
        _priceListRepository.save(priceList);
        return mapPriceListToPriceListResponse(priceList);
    }

    @Override
    public PriceListResponse getPriceList(UUID id) throws Exception {
        PriceList priceList = _priceListRepository.findOneById(id);
        return mapPriceListToPriceListResponse(priceList);
    }

    @Override
    public PriceListResponse getPriceListByAgent(UUID id) throws Exception {
        PriceList priceList = _priceListRepository.findOneByAgentId(id);
        return mapPriceListToPriceListResponse(priceList);
    }

    @Override
    public List<PriceListResponse> getAllPriceLists() throws Exception {
        List<PriceList> priceLists = _priceListRepository.findAllByDeleted(false);
        return priceLists.stream()
                .map(priceList -> mapPriceListToPriceListResponse(priceList))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePriceList(UUID id) throws Exception {
        PriceList priceList = _priceListRepository.findOneById(id);
        priceList.setDeleted(true);
        _priceListRepository.save(priceList);
    }

    @Override
    public void deletePriceListByAgent(UUID id) throws Exception {
        PriceList priceList = _priceListRepository.findOneByAgentId(id);
        priceList.setDeleted(true);
        _priceListRepository.save(priceList);
    }

    @Override
    public EarningsResponse getTotalEarningsByAgent(UUID id) throws Exception {
        Agent agent = _agentRepository.findOneById(id);
        List<Request> requests = _requestRepository.findAllByStatus(RequestStatus.PAID);
        PriceList priceList = _priceListRepository.findOneByAgentId(id);
        float totalEarnings = 0;
        for(Request r: requests){
            for(RequestAd ra: r.getRequestAds()){
                if(ra.getAd().getAgent() == agent){
                    float days;
                    if(ra.getPickUpDate().getYear() == ra.getReturnDate().getYear()){
                        days = ra.getReturnDate().getDayOfYear() - ra.getPickUpDate().getDayOfYear();
                    }else{
                        days = (ra.getReturnDate().getYear() - ra.getPickUpDate().getYear()) *365 + ra.getReturnDate().getDayOfYear() - ra.getPickUpDate().getDayOfYear();
                    }
//                    System.out.println(ra.getPickUpDate() + " " + ra.getReturnDate() + " " + days + " " + ra.getAd().getCoefficient());
                    if(days < 7){
                        totalEarnings += days * Float.valueOf(priceList.getPrice1day()) * Float.valueOf(ra.getAd().getCoefficient());
                    }else if(days >= 7 && days < 15){
                        totalEarnings += days * Float.valueOf(priceList.getPrice1day()) * Float.valueOf(ra.getAd().getCoefficient()) * ((100 - Float.valueOf(priceList.getDiscount7days())) / 100);
                    }else if(days >=15 && days < 30){
                        totalEarnings += days * Float.valueOf(priceList.getPrice1day()) * Float.valueOf(ra.getAd().getCoefficient()) * ((100 - Float.valueOf(priceList.getDiscount15days())) / 100);
                    }else if(days >= 30){
                        totalEarnings += days * Float.valueOf(priceList.getPrice1day()) * Float.valueOf(ra.getAd().getCoefficient()) * ((100 - Float.valueOf(priceList.getDiscount30days())) / 100);
                    }
                }
            }
        }
        EarningsResponse response = new EarningsResponse();
        response.setTotalEarnings(String.valueOf(totalEarnings));
        return response;
    }

    @Override
    public EarningsResponse getTotalEarningsByAd(UUID id) throws Exception {
        Ad ad = _adRepository.findOneById(id);
        Agent agent = _agentRepository.findOneById(ad.getAgent().getId());
        List<Request> requests = _requestRepository.findAllByStatus(RequestStatus.PAID);
        PriceList priceList = _priceListRepository.findOneByAgentId(agent.getId());
        float totalEarnings = 0;
        for(Request r: requests){
            for(RequestAd ra: r.getRequestAds()){
                if(ra.getAd() == ad){
                    float days;
                    if(ra.getPickUpDate().getYear() == ra.getReturnDate().getYear()){
                        days = ra.getReturnDate().getDayOfYear() - ra.getPickUpDate().getDayOfYear();
                    }else{
                        days = (ra.getReturnDate().getYear() - ra.getPickUpDate().getYear()) *365 + ra.getReturnDate().getDayOfYear() - ra.getPickUpDate().getDayOfYear();
                    }
                    if(days < 7){
                        totalEarnings += days * Float.valueOf(priceList.getPrice1day()) * Float.valueOf(ra.getAd().getCoefficient());
                    }else if(days >= 7 && days < 15){
                        totalEarnings += days * Float.valueOf(priceList.getPrice1day()) * Float.valueOf(ra.getAd().getCoefficient()) * ((100 - Float.valueOf(priceList.getDiscount7days()) / 100));
                    }else if(days >=15 && days < 30){
                        totalEarnings += days * Float.valueOf(priceList.getPrice1day()) * Float.valueOf(ra.getAd().getCoefficient()) * ((100 - Float.valueOf(priceList.getDiscount15days()) / 100));
                    }else if(days >= 30){
                        totalEarnings += days * Float.valueOf(priceList.getPrice1day()) * Float.valueOf(ra.getAd().getCoefficient()) * ((100 - Float.valueOf(priceList.getDiscount30days())) / 100);
                    }
                }
            }
        }
        EarningsResponse response = new EarningsResponse();
        response.setTotalEarnings(String.valueOf(totalEarnings));
        return response;
    }

    private PriceListResponse mapPriceListToPriceListResponse(PriceList priceList) {
        PriceListResponse response = new PriceListResponse();
        response.setAgentName(_agentRepository.findOneById(priceList.getAgentId()).getName());
        response.setId(priceList.getId());
        response.setPrice1day(priceList.getPrice1day());
        response.setDiscount7days(priceList.getDiscount7days());
        response.setDiscount15days(priceList.getDiscount15days());
        response.setDiscount30days(priceList.getDiscount30days());
        return response;
    }
}
