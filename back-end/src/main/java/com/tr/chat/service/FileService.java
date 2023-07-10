package com.tr.chat.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface FileService {
    int upload(MultipartFile[] files,String path, List<String> fileName);
    File get(Long date, String md5,String fileName,String path);
}
