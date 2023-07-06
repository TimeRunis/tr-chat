package com.sass.studentactivityscoresystem.service.Impl;

import com.sass.studentactivityscoresystem.entity.ReturnBody;
import com.sass.studentactivityscoresystem.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private ReturnBody returnBody;

    @Autowired
    public FileUploadServiceImpl(ReturnBody returnBody) {
        this.returnBody = returnBody;
    }

    @Override
    public ReturnBody upload(MultipartFile[] files,String path, List<String> fileName) {
        File folder = new File(path);
        //文件夹不存在
        if(!folder.isDirectory()){
            folder.mkdirs();
            for(int i=0;i<fileName.size();i++){
                try{
                    System.out.println("路径:"+path);
                    System.out.println("文件名:"+fileName.get(i));
                    files[i].transferTo(new File(path, fileName.get(i)));
                }catch (Exception e){
                    returnBody.setBody(-1,null);
                }
            }
        }else {
            //文件夹存在
            for(int i=0;i<fileName.size();i++){
                try{
                    files[i].transferTo(new File(path, fileName.get(i)));
                }catch (Exception e){
                    returnBody.setBody(-1,null);
                }
            }
        }
        returnBody.setBody(0,null);
        return returnBody;
    }
}
