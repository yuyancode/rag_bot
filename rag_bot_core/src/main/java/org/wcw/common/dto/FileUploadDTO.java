package org.wcw.common.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadDTO {
    private String fileId;
    private String fileUrl;
    private String filePath;
    private String fileName;
}
