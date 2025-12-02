package org.wcw.library.domain.vo.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author: iohw
 * @date: 2025/4/26 9:12
 * @description:
 */
@Data
public class CreateKnowledgeLibDocCommand {
    private String knowledgeLibId;
    private String documentName;
    private String documentDesc;
    private MultipartFile file;
}
