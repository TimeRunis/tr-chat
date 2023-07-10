package com.tr.chat.controller;

import com.tr.chat.entity.Resp;
import com.tr.chat.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
public class FileUploadController {
    private final FileService fileService;
    private Resp resp;
    //文件保存目录
    @Value("${upload.path}")
    private String path;

    @Autowired
    public FileUploadController(FileService fileService, Resp resp) {
        this.fileService = fileService;
        this.resp = resp;
    }

    @PostMapping("/upload")
    public Resp doPost(@RequestParam("file") MultipartFile[] multipartFile, HttpServletRequest request) throws UnsupportedEncodingException {
        Date now=new Date();
        String date= new SimpleDateFormat("yyyy/MM/dd/").format(now);
        String savePath = path+date;
        StringBuilder url= new StringBuilder(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/file");
        url.append("?date=").append(now.getTime()/1000);
        List<String> fileName = new ArrayList<>();
        //空文件检查
        if(multipartFile!=null){
            //生成文件名
            for (MultipartFile f:multipartFile) {
                try{
                    fileName.add(DigestUtils.md5DigestAsHex(f.getInputStream())+"-"+f.getOriginalFilename());
                }catch (IOException e){
                    resp.error(null,"获取MD5失败");
                }
            }
            if(fileService.upload(multipartFile,savePath,fileName)==0){
                for(int i=0;i<fileName.size();i++){
                    fileName.set(i,url+"&md5="+fileName.get(i).split("-")[0]+"&fileName="+ URLEncoder.encode(fileName.get(i).substring(fileName.get(i).indexOf("-")+1),"utf-8"));
                }
                resp.success(fileName,"上传成功");
            }else {
                resp.error(null,"上传失败");
            }
        }else {
            resp.error(null,"未找到文件");
        }

        return resp;
    }


}
