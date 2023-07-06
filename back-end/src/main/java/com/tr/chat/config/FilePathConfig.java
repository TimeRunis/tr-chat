package com.sass.studentactivityscoresystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilePathConfig implements WebMvcConfigurer {
   //文件保存目录
   public static String path="E:/uploads/";
   //url映射路径
   public static String url="/uploads/";

   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler(url+"**")
              .addResourceLocations("file:"+path);
   }
}
