package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CommentAdRequest;
import com.rentacar.agentbackend.dto.response.CommentResponse;
import com.rentacar.agentbackend.entity.*;
import com.rentacar.agentbackend.repository.*;
import com.rentacar.agentbackend.service.ICommentService;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService implements ICommentService {

    private final ICommentRepository _commentRepository;

    private final IRequestRepository _requestRepository;

    private final IRequestAdRepository _requestAdRepository;

    private final ISimpleUserRepository _simpleUserRepository;

    private final IAdRepository _adRepository;

    public CommentService(ICommentRepository commentRepository, IRequestRepository requestRepository, IRequestAdRepository requestAdRepository, ISimpleUserRepository simpleUserRepository, IAdRepository adRepository) {
        _commentRepository = commentRepository;
        _requestRepository = requestRepository;
        _requestAdRepository = requestAdRepository;
        _simpleUserRepository = simpleUserRepository;
        _adRepository = adRepository;
    }

    @Override
    public CommentResponse commentAd(CommentAdRequest request) throws Exception {
        SimpleUser simpleUser = _simpleUserRepository.findOneById(request.getSimpleUserId());
        Ad ad = _adRepository.findOneById(request.getAdId());

        List<Request> simpleUsersRequests = _requestRepository.findAllByCustomer_Id(simpleUser.getId());
        if(simpleUsersRequests.isEmpty()){
            throw new Exception("You cannot comment this ad because you did not have any rents.");
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
            throw new Exception("You cannot comment this ad.");
        }

        Comment comment = new Comment();
        comment.setComment(request.getComment());
        comment.setSimpleUser(simpleUser);
        comment.setAd(ad);
        comment.setStatus(RequestStatus.PENDING);
        Comment savedComment = _commentRepository.save(comment);
        ad.getComments().add(savedComment);
        _adRepository.save(ad);
        simpleUser.getComments().add(savedComment);
        _simpleUserRepository.save(simpleUser);

        return mapCommentToCommentResponse(savedComment);
    }

    @Override
    public void approveComment(UUID id) throws Exception {
        Comment comment = _commentRepository.findOneById(id);
        comment.setStatus(RequestStatus.APPROVED);
        _commentRepository.save(comment);
    }

    @Override
    public void denyComment(UUID id) throws Exception {
        Comment comment = _commentRepository.findOneById(id);
        comment.setStatus(RequestStatus.DENIED);
        _commentRepository.save(comment);
    }

    private CommentResponse mapCommentToCommentResponse(Comment comment){
        CommentResponse response = new CommentResponse();
        response.setComment(comment.getComment());
        response.setAgentEmail(comment.getAd().getAgent().getUser().getUsername());
        response.setAgentName(comment.getAd().getAgent().getName());
        response.setCustomerEmail(comment.getSimpleUser().getUser().getUsername());
        response.setCustomerFirstName(comment.getSimpleUser().getFirstName());
        response.setCustomerLastName(comment.getSimpleUser().getLastName());
        response.setCarBrandName(comment.getAd().getCar().getCarModel().getCarBrand().getName());
        response.setCarModelName(comment.getAd().getCar().getCarModel().getName());
        return response;
    }
}