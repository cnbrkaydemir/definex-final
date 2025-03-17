package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.CommentDto;
import com.cnbrkaydemir.tasks.dto.CreateCommentDto;
import com.cnbrkaydemir.tasks.exception.notfound.CommentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.model.Comment;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.repository.CommentRepository;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CreateCommentDto commentDto) {
        Users user =  usersRepository.findById(commentDto.getUserId()).orElseThrow(()->new UserNotFoundException(commentDto.getUserId()));
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setDeleted(false);
        comment.setCommentedBy(user);
        return modelMapper.map(commentRepository.save(comment), CommentDto.class);
    }

    @Override
    public CommentDto getComment(UUID id) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(()->new CommentNotFoundException(id));
        return modelMapper.map(comment, CommentDto.class);
    }

    @Override
    public CommentDto updateComment(UUID id, CommentDto comment) throws CommentNotFoundException {
        Comment oldComment = commentRepository.findById(id).orElseThrow(()->new CommentNotFoundException(id));
        modelMapper.map(comment, oldComment);
        return modelMapper.map(commentRepository.save(oldComment), CommentDto.class);
    }

    @Override
    public boolean deleteComment(UUID id) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(()->new CommentNotFoundException(id));
        comment.setDeleted(true);
        commentRepository.save(comment);
        return false;
    }

    @Override
    public List<CommentDto> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .toList();
    }
}
