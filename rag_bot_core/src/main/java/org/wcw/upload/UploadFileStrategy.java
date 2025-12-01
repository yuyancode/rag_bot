package org.wcw.upload;

import org.springframework.web.multipart.MultipartFile;
import org.wcw.common.dto.FileUploadDTO;

public interface UploadFileStrategy {
    FileUploadDTO upload(MultipartFile file, String path);

}
