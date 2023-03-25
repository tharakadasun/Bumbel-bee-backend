package com.backend.backend.controllers.file;

import com.backend.backend.services.file.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.HttpURLConnection;

@RestController
//@CrossOrigin(origins = "https://shopping-center-lime.vercel.app")
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {
    private final S3Service s3Service;
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file){
        return s3Service.saveFile(file);
    }
    @GetMapping("download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable("filename") String filename){
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; filename="+filename);
        byte[] bytes = s3Service.downloadFile(filename);
        return  ResponseEntity.status(HttpURLConnection.HTTP_OK).headers(headers).body(bytes);
    }
}
