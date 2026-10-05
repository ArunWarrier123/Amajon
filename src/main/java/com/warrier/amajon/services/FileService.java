package com.warrier.amajon.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadImageToServer(MultipartFile image, String path) throws IOException;
}
