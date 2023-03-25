package com.backend.backend.services.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(MultipartFile file);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);
    String getImageURL(String fileName);
}
