package com.gui.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.gui.dto.FileDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Controller
public class FileController {
    @Value("${aliyun.AccessKeyID}")
    private  String accessKeyIDs;
    @Value("${aliyun.AccessKeySecret}")
    private  String accessKeySecrets;
    @Value("${aliyun.EndPoint}")
    private String endPoint;
    @Value("${aliyun.BucketName}")
    private String bucketNames;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        //获取传过来的文件路径
      MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
        MultipartFile file=multipartRequest.getFile("editormd-image-file");
        //获取上传文件的真实路径
      
       // String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/" + file.getOriginalFilename();
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = endPoint;
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = accessKeyIDs;
        String accessKeySecret =accessKeySecrets;
        String bucketName=bucketNames;
// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint , accessKeyId, accessKeySecret);
// 上传文件流。
        InputStream inputStream = null;
        try {

           inputStream = new FileInputStream("E:/code/community/src/main/resources/static/picture/wow.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ossClient.putObject(bucketName, "testFile", inputStream);
// 关闭OSSClient。
        ossClient.shutdown();
        FileDTO fileDTO=new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/picture/wow.jpg");
        fileDTO.setMessage("upload ok!");
        return fileDTO;
    }
}
