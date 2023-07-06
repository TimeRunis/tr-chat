package com.sass.studentactivityscoresystem.controller;

import com.sass.studentactivityscoresystem.config.FilePathConfig;
import com.sass.studentactivityscoresystem.entity.RespBody;
import com.sass.studentactivityscoresystem.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UploadController extends BaseController{
    private FileUploadService fileUploadService;

    @Autowired
    public UploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/fileUpload")
    public RespBody doPost(@RequestParam("file") MultipartFile[] multipartFile, HttpServletRequest request){
        String date= new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        String path= FilePathConfig.path+date;
        String url=request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() +FilePathConfig.url+date;
        List<String> fileName = new ArrayList<>();
        System.out.println(path);
        //空文件检查
        if(multipartFile!=null){
            //生成文件名
            for (MultipartFile f:multipartFile) {
                try{
                    fileName.add(DigestUtils.md5DigestAsHex(f.getInputStream()).toString()+"_"+f.getOriginalFilename());
                }catch (IOException e){
                    rep.setResp(-1,null,"获取MD5失败");
                }
            }
            if(fileUploadService.upload(multipartFile,path,fileName).getFlag()==0){
                for(int i=0;i<fileName.size();i++){
                    fileName.set(i,url+fileName.get(i));
                }
                rep.setResp(0,fileName,"上传成功");
            }else {
                rep.setResp(-1,null,"上传失败");
            }
        }else {
            rep.setResp(-1,null,"未找到文件");
        }

        return rep;
    }


}
