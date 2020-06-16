package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.RateAdRequest;
import com.rentacar.agentbackend.dto.response.AvgRatingResponse;
import com.rentacar.agentbackend.dto.response.RatingResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.IRatingService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RatingService implements IRatingService {

    private final IRatingRepository _ratingRepository;

    private final IRequestRepository _requestRepository;

    private final IRequestAdRepository _requestAdRepository;

    private final ISimpleUserRepository _simpleUserRepository;

    private final IAdRepository _adRepository;

    public RatingService(IRatingRepository ratingRepository, IRequestRepository requestRepository, IRequestAdRepository requestAdRepository, ISimpleUserRepository simpleUserRepository, IAdRepository adRepository) {
        _ratingRepository = ratingRepository;
        _requestRepository = requestRepository;
        _requestAdRepository = requestAdRepository;
        _simpleUserRepository = simpleUserRepository;
        _adRepository = adRepository;
    }

    @Override
    public RatingResponse rateAd(RateAdRequest request) throws Exception {
        SimpleUser simpleUser = _simpleUserRepository.findOneById(request.getSimpleUserId());
        Ad ad = _adRepository.findOneById(request.getAdId());

        List<Request> simpleUsersRequests = _requestRepository.findAllByCustomer_Id(simpleUser.getId());
        if(simpleUsersRequests.isEmpty()){
            throw new Exception("You cannot rate this ad because you did not have any rents.");
        }

        Rating temp = _ratingRepository.findOneByAd_IdAndSimpleUser_Id(ad.getId(), simpleUser.getId());
        if(temp != null){
            throw new Exception("You have already rated this ad.");
        }

        Request ratingRequest = null; //flag
        for(Request r: simpleUsersRequests){
            if(!r.getStatus().equals(RequestStatus.PAID)){
                continue;
            }
            List<RequestAd> requestAds = _requestAdRepository.findAllByRequest(r);
            boolean flag = false;
            for(RequestAd ra: requestAds){
                if(!(ra.getAd() == ad)){
                    continue;
                }
                LocalDate currentDate = LocalDate.now();
                if(ra.getReturnDate().isAfter(currentDate)){
                    continue;
                }
                ratingRequest = r;
                flag = true;
                break;
            }
            if(flag){
                break;
            }
        }
        if(ratingRequest == null){
            throw new Exception("You cannot rate this ad.");
        }

        Rating rating = new Rating();
        rating.setGrade(request.getGrade());
        rating.setSimpleUser(simpleUser);
        rating.setAd(ad);
        Rating savedRating = _ratingRepository.save(rating);
        ad.getRatings().add(savedRating);
        _adRepository.save(ad);
        simpleUser.getRatings().add(savedRating);
        _simpleUserRepository.save(simpleUser);

        return mapRatingToRatingResponse(savedRating);
    }

    @Override
    public List<RatingResponse> getAllRatingsByCustomer(UUID id) throws Exception {
        List<Rating> ratings = _ratingRepository.findAllBySimpleUser_Id(id);
        return ratings.stream()
                .map(rating -> mapRatingToRatingResponse(rating))
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponse> getAllRatingsByAd(UUID id) throws Exception {
        List<Rating> ratings = _ratingRepository.findAllByAd_Id(id);
        return ratings.stream()
                .map(rating -> mapRatingToRatingResponse(rating))
                .collect(Collectors.toList());
    }

    @Override
    public AvgRatingResponse getAvgRatingByAd(UUID id) throws Exception {
        List<Rating> ratings = _ratingRepository.findAllByAd_Id(id);
        float sum = 0;
        float counter = 0;
        for(Rating r: ratings){
            sum += Float.valueOf(r.getGrade());
            counter++;
        }
        AvgRatingResponse response = new AvgRatingResponse();
        response.setAgentEmail(_adRepository.findOneById(id).getAgent().getUser().getUsername());
        response.setAgentName(_adRepository.findOneById(id).getAgent().getName());
        response.setCarBrandName(_adRepository.findOneById(id).getCar().getCarModel().getCarBrand().getName());
        response.setCarModelName(_adRepository.findOneById(id).getCar().getCarModel().getName());
        if(counter == 0){
            response.setAvgRating("0");
            return response;
        }
        response.setAvgRating(String.valueOf(sum / counter));
        return response;
    }

    private RatingResponse mapRatingToRatingResponse(Rating rating){
        RatingResponse response = new RatingResponse();
        response.setGrade(rating.getGrade());
        response.setAgentEmail(rating.getAd().getAgent().getUser().getUsername());
        response.setAgentName(rating.getAd().getAgent().getName());
        response.setCustomerEmail(rating.getSimpleUser().getUser().getUsername());
        response.setCustomerFirstName(rating.getSimpleUser().getFirstName());
        response.setCustomerLastName(rating.getSimpleUser().getLastName());
        response.setCarBrandName(rating.getAd().getCar().getCarModel().getCarBrand().getName());
        response.setCarModelName(rating.getAd().getCar().getCarModel().getName());
        return response;
    }
}
