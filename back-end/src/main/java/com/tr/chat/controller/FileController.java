package com.tr.chat.controller;

import com.tr.chat.entity.Resp;
import com.tr.chat.service.FileService;
import com.tr.chat.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class FileController {
    private final FileService fileService;
    private Resp resp;
    //文件保存目录
    @Value("${upload.path}")
    private String path;

    @Autowired
    public FileController(FileService fileService, Resp resp) {
        this.fileService = fileService;
        this.resp = resp;
    }

    @GetMapping("/file")
    public Object doGet(@RequestParam Map<Object, String> map, HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            String fileName = map.get("fileName").replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            fileName = fileName.replaceAll("\\+", "%2B");
            File file=fileService.get(Long.valueOf(map.get("date")),map.get("md5"), URLDecoder.decode(fileName,"utf-8"),path);
            LoggerUtil.info("\n请求文件路径:"+file.getPath());
            String filePath = file.getPath();
            String filename = file.getName();
            outputStream = response.getOutputStream();

            if(map.containsKey("type")&&map.get("type").equals("download")){
                // attachment为文件下载
                response.addHeader("Content-Disposition", "attachment;filename=" + filename);
                // 如果设置此请求头，则为下载
                response.setContentType("application/octet-stream");
            }else{
                // inline为预览
                response.addHeader("Content-Disposition", "inline;filename=" + filename);
            }

            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());

            bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            byte[] bytes = new byte[1024 * 1024];
            int b = 0;
            while ((b = bufferedInputStream.read(bytes)) != -1) {
                outputStream.write(bytes);
            }
            return response;
        } catch (FileNotFoundException e) {
            resp.error(null,"未找到文件");
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
            resp.error(null,"未知错误");
            return resp;
        } finally {
            try {
                if(bufferedInputStream!=null&&outputStream!=null) {
                    bufferedInputStream.close();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
