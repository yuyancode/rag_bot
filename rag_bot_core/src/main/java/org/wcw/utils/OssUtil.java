package org.wcw.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.wcw.config.properties.ConfigHolder;
import org.wcw.config.properties.OssProperties;

import java.io.InputStream;

public class OssUtil {
    public static final OssProperties properties = ConfigHolder.ossProperties;

    public static String upload(String path, String fileName, InputStream in) {
        OSS ossClient = new OSSClientBuilder().build(properties.getEndpoint(), properties.getAccessKeyId(), properties.getSecretAccessKeyId());
        String name = path + "/" + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), name, in);
        String url = "https://" + properties.getBucketName() + "." + properties.getEndpoint().replace("http://", "").replace("https://", "") + "/" + name;
        ossClient.putObject(putObjectRequest);
        return url;
    }
}
