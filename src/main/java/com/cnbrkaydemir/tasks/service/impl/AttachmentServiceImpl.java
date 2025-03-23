package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.exception.notfound.AttachmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.model.Attachment;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.repository.AttachmentRepository;
import com.cnbrkaydemir.tasks.repository.TaskRepository;
import com.cnbrkaydemir.tasks.service.AttachmentService;
import com.cnbrkaydemir.tasks.service.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "attachments")
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final TaskRepository taskRepository;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(key = "'resource_' + #id")
    public Resource getAttachmentAsResource(UUID id) throws AttachmentNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(()->new AttachmentNotFoundException(id));
        return fileService.loadAsResource(attachment.getName());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(key = "#id")
    public AttachmentDto getAttachment(UUID id) throws AttachmentNotFoundException {
        return modelMapper.map(attachmentRepository.findById(id), AttachmentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "allAttachmentResources")
    public List<Resource> getAllAttachmentsAsResource() {
        return attachmentRepository.findAll()
                .stream()
                .map(attachment -> fileService.loadAsResource(attachment.getName()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "allAttachments")
    public List<AttachmentDto> getAllAttachments() {
        return attachmentRepository.findAll()
                .stream()
                .map(attachment -> modelMapper.map(attachment, AttachmentDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"attachments", "allAttachments", "allAttachmentResources"}, allEntries = true)
    public boolean deleteAttachment(UUID id) throws AttachmentNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(()->new AttachmentNotFoundException(id));
        fileService.delete(attachment.getName());
        attachment.setDeleted(true);
        attachmentRepository.save(attachment);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"allAttachments", "allAttachmentResources"}, allEntries = true)
    public AttachmentDto createAttachment(UUID taskId, MultipartFile file) {
        Task task = taskRepository.findById(taskId).orElseThrow(()->new TaskNotFoundException(taskId));

        fileService.store(file);

        Attachment attachment = createAttachmentFromFile(file);
        attachment.setTask(task);
        return modelMapper.map(attachmentRepository.save(attachment), AttachmentDto.class);
    }

    @Override
    public Attachment createAttachmentFromFile(MultipartFile file) {
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setType(file.getContentType());
        attachment.setPath("./uploads/"+file.getOriginalFilename());
        attachment.setCreatedDate(new Date());
        attachment.setDeleted(false);
        return attachment;
    }
}