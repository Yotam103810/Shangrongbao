package com.chen.srb.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.chen.srb.oss.service.FileService;
import com.chen.srb.oss.util.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(InputStream inputStream, String module, String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET);

        // 判断BUCKET_NAME是否存在
        if(!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)){
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }

        // 上传文件流。
        // 文件目录结构 "avatar/2021/02/27/uuid.jpg"
        //构建日期路径
        String timeFolder = new DateTime().toString("/yyyy/MM/dd/");
        //文件名生成
        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
        String key = module + timeFolder + fileName;
        ossClient.putObject(OssProperties.BUCKET_NAME, key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //文件的url地址
        //https:// bucketname   .  endpoint / + key
        return "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + key;
    }
}
