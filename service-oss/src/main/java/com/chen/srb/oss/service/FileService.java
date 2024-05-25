package com.chen.srb.oss.service;

import java.io.InputStream;

public interface FileService {
    String upload(InputStream inputStream, String module, String originalFilename);
}
