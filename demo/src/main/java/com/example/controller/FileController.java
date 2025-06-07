package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import com.example.exception.CustomerException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 文件下载
     * 下载路径："http://localhost:8082/file/download/404.jpg"
     */
    @RequestMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        // 找到文件的位置，
        // System.getProperty("user.dir") 获取当前的工作目录
        String filePath = System.getProperty("user.dir") + "/files/";
        String realPath = filePath + fileName;
        boolean exist = FileUtil.exist(realPath);
        if (!exist) {
            throw new CustomerException("文件不存在");
        }
        // 读取文件的字节流
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os = response.getOutputStream();
        // 输出流对象把文件写出到客户端
        os.write(bytes);
        os.flush();
        os.close();
    }

    /**
     * 文件上传
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        // 找到文件的位置
        String filePath = System.getProperty("user.dir") + "/files/";
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();  // 文件的原始名称
        // 写入文件
        FileUtil.writeBytes(bytes, filePath + fileName);
        String url = "http://localhost:8081/file/download/" + fileName;
        return Result.success(url);
    }

}
