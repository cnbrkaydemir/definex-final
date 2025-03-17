package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.exception.notfound.AttachmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.model.Attachment;
import com.cnbrkaydemir.tasks.model.AttachmentType;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.repository.AttachmentRepository;
import com.cnbrkaydemir.tasks.repository.TaskRepository;
import com.cnbrkaydemir.tasks.service.AttachmentService;
import com.cnbrkaydemir.tasks.service.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final TaskRepository taskRepository;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    @Override
    public Resource getAttachment(UUID id) throws AttachmentNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(()->new AttachmentNotFoundException(id));
        return fileService.loadAsResource(attachment.getName());
    }

    @Override
    public List<Resource> getAllAttachments() {
        return attachmentRepository.findAll()
                .stream()
                .map(attachment -> fileService.loadAsResource(attachment.getName()))
                .toList();
    }

    @Override
    public boolean deleteAttachment(UUID id) throws AttachmentNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(()->new AttachmentNotFoundException(id));
        fileService.delete(attachment.getName());
        attachment.setDeleted(true);
        attachmentRepository.save(attachment);
        return true;
    }

    @Override
    public AttachmentDto createAttachment(UUID taskId, MultipartFile file) {
        Task task = taskRepository.findById(taskId).orElseThrow(()->new TaskNotFoundException(taskId));

        fileService.store(file);

        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setType(AttachmentType.valueOf(file.getContentType()));
        attachment.setPath(file.getOriginalFilename());
        attachment.setTask(task);
        attachment.setDeleted(false);
        return modelMapper.map(attachmentRepository.save(attachment), AttachmentDto.class);
    }

}
