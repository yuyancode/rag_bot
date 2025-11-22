package org.wcw.user.domain.vo.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ModifyUserInfoCommand {
    private Long userId;
    private String newPassword;
    private String nickname;
    private String description;
    private MultipartFile avatar;
}
