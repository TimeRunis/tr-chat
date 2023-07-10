package com.tr.chat.service.impl;


import com.tr.chat.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public int upload(MultipartFile[] files,String path, List<String> fileName) {
        File folder = new File(path);
        //文件夹不存在
        if(!folder.isDirectory()){
            folder.mkdirs();
            for(int i=0;i<fileName.size();i++){
                try{
                    files[i].transferTo(new File(path, fileName.get(i)));
                }catch (Exception e){
                    return -1;
                }
            }
        }else {
            //文件夹存在
            for(int i=0;i<fileName.size();i++){
                try{
                    files[i].transferTo(new File(path, fileName.get(i)));
                }catch (Exception e){
                    return -1;
                }
            }
        }
        return 0;
    }

    @Override
    public File get(Long date, String md5,String fileName,String path) {
        return new File(path+new SimpleDateFormat("yyyy/MM/dd/").format(new Date(date*1000))+md5+"-"+fileName);
    }
}
