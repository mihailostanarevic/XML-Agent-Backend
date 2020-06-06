package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.SearchResultDTO;
import com.rentacar.agentbackend.entity.Ad;
import com.rentacar.agentbackend.entity.Address;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.entity.RequestAd;
import com.rentacar.agentbackend.repository.IAdRepository;
import com.rentacar.agentbackend.repository.IRequestRepository;
import com.rentacar.agentbackend.service.ISearchService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SearchService implements ISearchService {

    private final IAdRepository _adRepository;

    private final IRequestRepository _requestRepository;

    public SearchService(IAdRepository adRepository, IRequestRepository requestRepository) {
        _adRepository = adRepository;
        _requestRepository = requestRepository;
    }

    @Override
    public List<SearchResultDTO> search(String city, String from, String to) {
        List<SearchResultDTO> retVal = new ArrayList<SearchResultDTO>();

        String paramTimeFrom = from.split(" ")[0];
        String paramDateFrom = from.split(" ")[1];
        String paramTimeTo = to.split(" ")[0];
        String paramDateTo = to.split(" ")[1];

        LocalTime timeFrom = LocalTime.parse(paramTimeFrom);
        LocalDate dateFrom = LocalDate.parse(paramDateFrom);
        LocalTime timeTo = LocalTime.parse(paramTimeTo);
        LocalDate dateTo = LocalDate.parse(paramDateTo);

        List<Ad> allAds = _adRepository.findAllByDeleted(false);
        if(!city.equals(""))
            allAds = removeIncorrectCityAd(allAds, city);

        List<Request> allRequests = _requestRepository.findAllByStatus(RequestStatus.APPROVED);
        allAds = ignoreNonRelevantAds(allAds, allRequests, dateFrom, timeFrom, dateTo, timeTo, city);
        List<RequestAd> requestAds = passableRequests(allRequests, dateFrom, timeFrom, dateTo, timeTo, city);
        boolean found = false;

        for(Ad ad : allAds){
            found = false;
            for(RequestAd rqAd : requestAds){
                if(rqAd.getAd().getId().equals(ad.getId())){
                    SearchResultDTO dto = makeDTO(ad);
                    found = true;
                    retVal.add(dto);
                }
            }

            if(!found){
                SearchResultDTO dto = makeDTO(ad);
                retVal.add(dto);
            }
        }

        return retVal;
    }

    private List<Ad> ignoreNonRelevantAds(List<Ad> allAds, List<Request> allRequests, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo, String city) {
        List<Ad> retVal = new ArrayList<>();
        for(Ad ad : allAds){
            retVal.add(ad);
        }

        List<UUID> ids = new ArrayList<>();
        for(Request request : allRequests){
            for(RequestAd rqAd : request.getRequestAds()) {
                for (Address address : rqAd.getAd().getAgent().getAddress()) {
                    if (address.getCity().toUpperCase().equals(city.toUpperCase())) {
                        if(!isCompletelyBefore(rqAd, dateFrom, timeFrom) && !isCompletelyAfter(rqAd, dateTo, timeTo)){
                            if(!ids.contains(rqAd.getAd().getId())){
                                ids.add(rqAd.getAd().getId());
                            }
                        }
                    }
                }
            }
        }


        for(Ad ad : allAds){
            for(UUID id : ids){
                if(ad.getId().equals(id)){
                    retVal.remove(ad);
                }
            }
        }

        return retVal;
    }

    private SearchResultDTO makeDTO(Ad ad) {
        SearchResultDTO retVal = new SearchResultDTO();
        retVal.setLimitedDistance(ad.isLimitedDistance());
        retVal.setAvailableKilometersPerRent(ad.getAvailableKilometersPerRent());
        retVal.setSeats(ad.getSeats());
        retVal.setCdw(ad.isCdw());
        retVal.setDate(ad.getCreationDate());
        retVal.setCarID(ad.getCar().getId());
        retVal.setCarBrand(ad.getCar().getCarModel().getCarBrand().getName());
        retVal.setCarModel(ad.getCar().getCarModel().getName());
        retVal.setCarModel(ad.getCar().getCarModel().getCarClass().getName());
        retVal.setFuelType(ad.getCar().getFuelType().getType());
        retVal.setGearshiftType(ad.getCar().getGearshiftType().getType());
        retVal.setAgent(ad.getAgent().getId());
        retVal.setAgentName(ad.getAgent().getName());
        String x = "";
        for(Address add : ad.getAgent().getAddress()){
            x += add.getCity();
            x += ", ";
        }
        retVal.setLocation(x);
        return retVal;
    }

    public List<Ad> removeIncorrectCityAd(List<Ad> allAds, String city) {
        List<Ad> retVal = new ArrayList<>();
        for(Ad ad : allAds){
            for(Address address : ad.getAgent().getAddress()) {
                if(address.getCity().toUpperCase().equals(city.toUpperCase())){
                    retVal.add(ad);
                    break;
                }
            }
        }

        return retVal;
    }

    private List<RequestAd> passableRequests(List<Request> allRequests, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo, String city) {
        List<RequestAd> retVal = new ArrayList<>();
        for(Request request : allRequests){
            for(RequestAd rqAd : request.getRequestAds()) {
                for (Address address : rqAd.getAd().getAgent().getAddress()) {
                    if (address.getCity().toUpperCase().equals(city.toUpperCase())) {
                        if(isCompletelyBefore(rqAd, dateFrom, timeFrom) || isCompletelyAfter(rqAd, dateTo, timeTo)){
                            retVal.add(rqAd);
                        }
                    }
                }
            }
        }

        return retVal;
    }

    public boolean isCompletelyBefore(RequestAd rqAd, LocalDate dateFrom, LocalTime timeFrom){
        //System.out.println(rqAd.getReturnDate() + " " + dateFrom);
        if(rqAd.getReturnDate().isBefore(dateFrom)){
            //System.out.println("usao sam za " + rqAd.getReturnDate() + " datum vracanja, " + dateFrom + "datum iz searcha");
            return true;
        }else {
            if(rqAd.getReturnDate().isEqual(dateFrom) && rqAd.getReturnTime().isBefore(timeFrom)){
                return true;
            }
        }
        return false;
    }

    public boolean isCompletelyAfter(RequestAd rqAd, LocalDate dateTo, LocalTime timeTo){
        //System.out.println(rqAd.getPickUpDate() + " " + dateTo);
        if(rqAd.getPickUpDate().isAfter(dateTo)){
            //System.out.println("usao sam za " + rqAd.getPickUpDate() + " datum vracanja, " + dateTo + "datum iz searcha 213213");
            return true;
        }else {
            if(rqAd.getPickUpDate().isEqual(dateTo) && rqAd.getReturnTime().isAfter(timeTo)){
                return true;
            }
        }
        return false;
    }
}
