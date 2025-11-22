package org.wcw.user.domain.vo.request;

import lombok.Data;

@Data
public class RegistryCommand {
    private String username;
    private String password;
}
