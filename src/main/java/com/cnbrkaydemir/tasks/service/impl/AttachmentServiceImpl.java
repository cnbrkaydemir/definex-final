package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.exception.notfound.AttachmentNotFoundException;
import com.cnbrkaydemir.tasks.model.Attachment;
import com.cnbrkaydemir.tasks.repository.AttachmentRepository;
import com.cnbrkaydemir.tasks.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final ModelMapper modelMapper;

    @Override
    public AttachmentDto getAttachment(UUID id) throws AttachmentNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(()->new AttachmentNotFoundException(id));
        return modelMapper.map(attachment, AttachmentDto.class);
    }

    @Override
    public List<AttachmentDto> getAllAttachments() {
        return attachmentRepository.findAll()
                .stream()
                .map(attachment -> modelMapper.map(attachment, AttachmentDto.class))
                .toList();
    }

    @Override
    public boolean deleteAttachment(UUID id) throws AttachmentNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(()->new AttachmentNotFoundException(id));
        attachmentRepository.delete(attachment);
        return true;
    }

    @Override
    public AttachmentDto createAttachment(Attachment attachment) {
        return modelMapper.map(attachmentRepository.save(attachment), AttachmentDto.class);
    }

    @Override
    public AttachmentDto updateAttachment(UUID id, AttachmentDto attachment) throws AttachmentNotFoundException {
        Attachment oldAttachment = attachmentRepository.findById(id).orElseThrow(()->new AttachmentNotFoundException(id));
        modelMapper.map(attachment, oldAttachment);
        return modelMapper.map(attachmentRepository.save(oldAttachment), AttachmentDto.class);
    }
}
