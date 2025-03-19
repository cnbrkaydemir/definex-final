package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/v1")
    public ResponseEntity<List<AttachmentDto>> list(){
        return ResponseEntity.ok(attachmentService.getAllAttachments());
    }

    @GetMapping("/v1/{attachmentId}")
    public ResponseEntity<Resource> get(@PathVariable UUID attachmentId){
        AttachmentDto attachment = attachmentService.getAttachment(attachmentId);
        Resource attachmentResource = attachmentService.getAttachmentAsResource(attachmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getType()))
                .body(attachmentResource);
    }

    @PostMapping("/v1/{taskId}")
    public ResponseEntity<AttachmentDto> create(@PathVariable UUID taskId,@RequestBody MultipartFile file){
        AttachmentDto attachmentDto = attachmentService.createAttachment(taskId, file);
        return ResponseEntity.ok(attachmentDto);
    }


    @DeleteMapping("/v1/{attachmentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID attachmentId){
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }





}
