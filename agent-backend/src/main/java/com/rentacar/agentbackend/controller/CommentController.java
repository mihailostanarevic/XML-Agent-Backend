package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.ApproveOrDenyCommentRequest;
import com.rentacar.agentbackend.dto.request.CommentAdRequest;
import com.rentacar.agentbackend.dto.response.CommentResponse;
import com.rentacar.agentbackend.service.ICommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final ICommentService _commentService;

    public CommentController(ICommentService commentService) {
        _commentService = commentService;
    }

    @PostMapping
    CommentResponse commentAd(@RequestBody CommentAdRequest request) throws Exception {
        return _commentService.commentAd(request);
    }

    @PutMapping("approve-comment")
    void approveComment(@RequestBody ApproveOrDenyCommentRequest request) throws Exception {
        _commentService.approveComment(request);
    }

    @PutMapping("deny-comment")
    void denyComment(@RequestBody ApproveOrDenyCommentRequest request) throws Exception {
        _commentService.denyComment(request);
    }

    @GetMapping("/{id}/ad")
    List<CommentResponse> getAllCommentsByAd(@PathVariable UUID id) throws Exception {
        return _commentService.getAllCommentsByAd(id);
    }

    @GetMapping("/pending")
    List<CommentResponse> getAllPendingComments() throws Exception {
        return _commentService.getAllPendingComments();
    }
}
