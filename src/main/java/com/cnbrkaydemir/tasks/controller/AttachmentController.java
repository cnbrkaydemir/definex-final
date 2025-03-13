package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.model.Attachment;
import com.cnbrkaydemir.tasks.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AttachmentDto> get(@PathVariable UUID attachmentId){
        return ResponseEntity.ok(attachmentService.getAttachment(attachmentId));
    }

    @PostMapping("/v1")
    public ResponseEntity<AttachmentDto> create(@RequestBody AttachmentDto attachmentDto){
        return ResponseEntity.created(URI.create("/api/attachment/" + attachmentDto.getId()))
                .body(attachmentService.createAttachment(attachmentDto));
    }

    @PatchMapping("/v1/{attachmentId}")
    public ResponseEntity<AttachmentDto> update(@PathVariable UUID attachmentId, @RequestBody AttachmentDto attachmentDto){
        return ResponseEntity.ok(attachmentService.updateAttachment(attachmentId, attachmentDto));
    }

    @DeleteMapping("/v1/{attachmentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID attachmentId){
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }





}
