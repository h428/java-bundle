package com.demo.boot.ssm.controller;

import com.demo.base.component.pojo.bean.ResBean;
import com.demo.boot.ssm.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("image")
@Api("图片上传")
public class ImageController {

    @Autowired
    private UploadService uploadService;


    @ApiOperation("上传图片，返回文件名（在 message 中）")
    @PostMapping("upload")
    public ResponseEntity<ResBean> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileName = this.uploadService.updateImage(file);
        return ResBean.ok_200(fileName);
    }

    @DeleteMapping("{fileName}")
    public ResponseEntity<ResBean> deleteImage(@PathVariable String fileName) {
        if (this.uploadService.deleteFile(fileName)) {
            return ResBean.ok_200("删除成功");
        } else {
            return ResBean.badRequest_400("删除失败，可能是文件不存在");
        }
    }


}
