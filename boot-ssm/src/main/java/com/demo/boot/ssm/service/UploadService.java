package com.demo.boot.ssm.service;

import com.demo.base.component.exception.InternalServerErrorException;
import com.demo.base.component.exception.ParamErrorException;
import com.demo.base.component.util.RegexpString;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

// 如果是在微服务架构中，上传服务应该单独做成微服务，并且为了图片和文件的高可用，应该搭建图片服务器来存储图片和文件
// 在本项目中，则没有必要这样，本项目采取的是：本服务作为图片上传服务，其直接将图片存储到指定的本地文件夹
// 对于上传成功的文件的访问，则需要利用 nginx 进行映射（一般还会配合域名或者ip+端口，若是域名需要修改本地 hosts）
@Service
public class UploadService {

    /**
     * 存储路径前缀
     */
    private static String STORE_PREFIX = "E:\\data\\tmp\\";

    /**
     * 上传图片，返回文件名
     * @param file 待上传的图片
     * @return 文件名
     */
    public String updateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ParamErrorException("空文件");
        }

        String fileName = file.getOriginalFilename();

        // 后缀名校验
        if (StringUtils.isBlank(fileName) || !fileName.matches(RegexpString.IMG)) {
            throw new ParamErrorException("文件类型不正确，仅支持 jpg, jpeg, png 的后缀名");
        }

        // 上传文件并得到新的文件名
        return this.uploadFile(file);
    }


    public String uploadFile(MultipartFile file) {
        try {
            // 获取源文件扩展名
            String fileName = file.getOriginalFilename();

            String ext = fileName.substring(fileName.lastIndexOf(".")); // 带 . 的扩展名

            // 确保生成的随机文件名不存在以避免覆盖
            File dest = null;
            do {
                dest = new File(STORE_PREFIX, RandomUtils.nextLong() + ext);
            } while (dest.exists());

            // 根据文件名在目标位置创建文件，并将上传的文件存储到目标位置
            file.transferTo(dest);
            return dest.getName(); // 返回最终的文件名
        } catch (Exception e) {
            throw new InternalServerErrorException("发生异常，上传失败");
        }
    }

    public boolean deleteFile(String fileName) {
        File file = new File(STORE_PREFIX, fileName);

        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }
}
