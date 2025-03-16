package com.cnbrkaydemir.tasks.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {

String store(MultipartFile file, UUID taskId);
Resource load(String fileName, UUID taskId);

}
