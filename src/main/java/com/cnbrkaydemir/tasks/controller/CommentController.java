package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.CommentDto;
import com.cnbrkaydemir.tasks.dto.CreateCommentDto;
import com.cnbrkaydemir.tasks.model.Comment;
import com.cnbrkaydemir.tasks.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/v1")
    public ResponseEntity<List<CommentDto>> list(){
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/v1/{commentId}")
    public ResponseEntity<CommentDto> get(@PathVariable UUID commentId){
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @PostMapping("/v1")
    public ResponseEntity<CommentDto> create(@RequestBody CreateCommentDto commentDto){
        CommentDto newComment = commentService.createComment(commentDto);
        return ResponseEntity.created(URI.create("/api/comment/v1/"+newComment.getId()))
                .body(newComment);
    }

    @PatchMapping("/v1/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable UUID commentId, @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.updateComment(commentId, commentDto));
    }

    @DeleteMapping("/v1/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
