package org.wcw.chat.domain.vo.request;


import jdk.jfr.Description;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Description("用户提出的建议、问题、bug信息")
public class SubmitIssueCommand {
    @Description("标题")
    private String title;
    @Description("建议、问题、bug信息的详细描述")
    private String issueDescription;
}
