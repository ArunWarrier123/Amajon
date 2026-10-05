package com.warrier.amajon.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImageToServer(MultipartFile image, String path) throws IOException {
        System.out.println("Uploading image to server" + path);
        String originalFilename = image.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString() + "." + originalFilename.substring(originalFilename.lastIndexOf("."));

        //upload this file to the path
        String filePath = path + File.pathSeparator +newFileName;
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        Files.copy(image.getInputStream() , Paths.get(filePath));
        return newFileName;
    }
}
