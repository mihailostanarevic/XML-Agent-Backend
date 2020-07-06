package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.response.*;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.IAdRepository;
import com.rentacar.agentbackend.repository.IPriceListRepository;
import com.rentacar.agentbackend.repository.IRatingRepository;
import com.rentacar.agentbackend.repository.IRequestRepository;
import com.rentacar.agentbackend.service.IAdService;
import com.rentacar.agentbackend.service.ISearchService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SearchService implements ISearchService {

    private final IAdRepository _adRepository;

    private final IAdService _adService;

    private final IRequestRepository _requestRepository;

    private final IPriceListRepository _priceListRepository;

    private final IRatingRepository _ratingRepository;

    public SearchService(IAdRepository adRepository, IAdService adService, IRequestRepository requestRepository, IPriceListRepository priceListRepository, IRatingRepository ratingRepository) {
        _adRepository = adRepository;
        _adService = adService;
        _requestRepository = requestRepository;
        _priceListRepository = priceListRepository;
        _ratingRepository = ratingRepository;
    }

    @Override
    public List<SearchResultResponse> search(String city, String from, String to) {
        List<SearchResultResponse> retVal = new ArrayList<SearchResultResponse>();

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

        List<UUID> ids = new ArrayList<>();
        for(Ad ad : allAds){
            ids.add(ad.getId());
        }

        for(Ad ad : allAds){
            found = false;
            for(RequestAd rqAd : requestAds){
                if(rqAd.getAd().getId().equals(ad.getId())){
                    found = true;
                    if(ids.contains(rqAd.getAd().getId())) {
                        ids.remove(rqAd.getAd().getId());
                        SearchResultResponse dto = makeDTO(ad);
                        retVal.add(dto);
                    }
                }
            }

            if(!found){
                SearchResultResponse dto = makeDTO(ad);
                retVal.add(dto);
            }
        }

        return retVal;
    }

    @Override
    public List<SearchResultResponse> advancedSearch(String city, String from, String to, String brand, String model, String fuelType, String gearshiftType, String carClass, int priceFrom, int priceTo, int estimatedDistance, boolean cdw, int childrenSeats) {
        List<SearchResultResponse> retVal = new ArrayList<SearchResultResponse>();

        String paramTimeFrom = from.split(" ")[0];
        String paramDateFrom = from.split(" ")[1];
        String paramTimeTo = to.split(" ")[0];
        String paramDateTo = to.split(" ")[1];

        LocalTime timeFrom = LocalTime.parse(paramTimeFrom);
        LocalDate dateFrom = LocalDate.parse(paramDateFrom);
        LocalTime timeTo = LocalTime.parse(paramTimeTo);
        LocalDate dateTo = LocalDate.parse(paramDateTo);

        List<Ad> allAds = removeIncorrectAds(city, brand, model, fuelType, gearshiftType, carClass, priceFrom, priceTo, estimatedDistance, cdw, childrenSeats);

        List<Request> allRequestsApproved = _requestRepository.findAllByStatus(RequestStatus.APPROVED);
        List<Request> allRequestsPaid = _requestRepository.findAllByStatus(RequestStatus.PAID);
        List<Request> allRequests = new ArrayList<>();

        for(Request request : allRequestsApproved){
            allRequests.add(request);
        }

        for(Request request : allRequestsPaid){
            allRequests.add(request);
        }

        allAds = ignoreNonRelevantAds(allAds, allRequests, dateFrom, timeFrom, dateTo, timeTo, city);
        List<RequestAd> requestAds = passableRequests(allRequests, dateFrom, timeFrom, dateTo, timeTo, city);
        boolean found = false;

        List<UUID> ids = new ArrayList<>();
        for(Ad ad : allAds){
            ids.add(ad.getId());
        }

        for(Ad ad : allAds){
            found = false;
            for(RequestAd rqAd : requestAds){
                if(rqAd.getAd().getId().equals(ad.getId())){
                    found = true;
                    if(ids.contains(rqAd.getAd().getId())) {
                        ids.remove(rqAd.getAd().getId());
                        SearchResultResponse dto = makeDTO(ad);
                        retVal.add(dto);
                    }
                }
            }

            if(!found){
                SearchResultResponse dto = makeDTO(ad);
                retVal.add(dto);
            }
        }

        return retVal;
    }

    private List<Ad> removeIncorrectAds(String city, String brand, String model, String fuelType, String gearshiftType, String carClass, int priceFrom, int priceTo, int estimatedDistance, boolean cdw, int childrenSeats) {
        System.out.println(brand);
        List<Ad> allAds = _adRepository.findAllByDeleted(false);
        return allAds
                .stream()
                .filter(ad -> {
                    boolean found = false;
                    if(city != null && city != "") {
                        for(Address address : ad.getAgent().getAddress()){
                            if(address.getCity().toUpperCase().equals(city.toUpperCase())){
                                found = true;
                                break;
                            }
                        }

                        return found;
                    } else {
                        return true;
                    }
                })
                .filter(ad -> {
                    if(brand != null && brand != "") {
                        return ad.getCar().getCarModel().getCarBrand().getName().toUpperCase().equals(brand.toUpperCase());
                    } else {
                        return true;
                    }
                })
                .filter(ad -> {
                    if(model != null && model != ""){
                        return ad.getCar().getCarModel().getName().toUpperCase().equals(model.toUpperCase());
                    }else {
                        return true;
                    }
                })
                .filter(ad -> {
                    if(fuelType != null && fuelType != ""){
                        return ad.getCar().getFuelType().getType().toUpperCase().equals(fuelType.toUpperCase());
                    }else {
                        return true;
                    }
                })
                .filter(ad -> {
                    if(gearshiftType != null && gearshiftType != ""){
                        return ad.getCar().getGearshiftType().getType().toUpperCase().toUpperCase().equals(gearshiftType.toUpperCase());
                    }else {
                        return true;
                    }
                })
                .filter(ad -> {
                    if(carClass != null && carClass != ""){
                        return ad.getCar().getCarModel().getCarClass().getName().toUpperCase().equals(carClass.toUpperCase());
                    }else {
                        return true;
                    }
                })
//                .filter(ad -> {
//                    if(priceFrom != null){
//                        return ad.getCar().getCarModel().getCarClass().getId().equals(carClassID);
//                    }else {
//                        return true;
//                    }
//                })
//                .filter(ad -> {
//                    if(priceTo != null){
//                        return ad.getCar().getCarModel().getCarClass().getId().equals(priceTo);
//                    }else {
//                        return true;
//                    }
//                })
                .filter(ad -> ad.isCdw() == cdw)
                .filter(ad -> {
                    if(childrenSeats != -1){
                        return ad.getSeats() == childrenSeats;
                    }else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
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

    private SearchResultResponse makeDTO(Ad ad) {
        SearchResultResponse retVal = new SearchResultResponse();
        List<PhotoResponse> photos = _adService.getAllPhotos(ad.getId());
        PriceList priceList = _priceListRepository.findOneByAgentId(ad.getAgent().getId());
        List<Rating> ratings = _ratingRepository.findAllByAd_Id(ad.getId());
        int sum = 0;
        for(Rating rating : ratings){
            sum += Integer.parseInt(rating.getGrade());
        }
        double avgRating = 0;
        if(ratings.size() != 0){
            avgRating = ((double)sum) / ratings.size();
        }
        int price = Integer.parseInt(priceList.getPrice1day())*Integer.parseInt(ad.getCoefficient());
        AdSearchResponse adDTO = new AdSearchResponse(ad.getId(), ad.isLimitedDistance(), ad.getSeats(), ad.isCdw(), ad.getCreationDate(), photos, price, avgRating);
        CarSearchResponse carDTO = new CarSearchResponse();
        carDTO.setCarID(ad.getCar().getId());
        carDTO.setCarModelName(ad.getCar().getCarModel().getName());
        carDTO.setCarBrandName(ad.getCar().getCarModel().getCarBrand().getName());
        carDTO.setCarClassName(ad.getCar().getCarModel().getCarClass().getName());
        carDTO.setCarClassDesc(ad.getCar().getCarModel().getCarClass().getDescription());
        carDTO.setFuelTypeType(ad.getCar().getFuelType().getType());
        carDTO.setFuelTypeTankCapacity(ad.getCar().getFuelType().getTankCapacity());
        carDTO.setFuelTypeGas(ad.getCar().getFuelType().isGas());
        carDTO.setGearshiftTypeType(ad.getCar().getGearshiftType().getType());
        carDTO.setGetGearshiftTypeNumberOfGears(ad.getCar().getGearshiftType().getNumberOfGears());
        carDTO.setKilometersTraveled(ad.getCar().getKilometersTraveled());
        String allLocations = "";
        List<AddressDTO> fullLocations = new ArrayList<>();
        int i = 0;
        for(Address add : ad.getAgent().getAddress()){
            allLocations += add.getCity();
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setId(add.getId());
            addressDTO.setCity(add.getCity());
            addressDTO.setStreet(add.getStreet());
            addressDTO.setNumber(add.getNumber());
            fullLocations.add(addressDTO);
            if(ad.getAgent().getAddress().size() > 1){
                if(i < ad.getAgent().getAddress().size() - 1){
                    allLocations += ",";
                }
            }
            i++;
        }
        AgentSearchResponse agentDTO = new AgentSearchResponse(ad.getAgent().getId(), ad.getAgent().getSimpleUserId(), ad.getAgent().getName(), ad.getAgent().getDateFounded().toString(), allLocations, fullLocations);
        retVal.setAd(adDTO);
        retVal.setAgent(agentDTO);
        retVal.setCar(carDTO);
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
