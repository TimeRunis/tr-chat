package com.sass.studentactivityscoresystem.service;

import com.sass.studentactivityscoresystem.entity.ReturnBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    ReturnBody upload(MultipartFile[] files,String path, List<String> fileName);
}
